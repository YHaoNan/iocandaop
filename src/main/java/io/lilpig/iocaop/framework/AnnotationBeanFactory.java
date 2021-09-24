package io.lilpig.iocaop.framework;

import io.lilpig.iocaop.framework.annotations.Autowired;
import io.lilpig.iocaop.framework.annotations.Component;
import io.lilpig.iocaop.framework.exceptions.*;
import io.lilpig.iocaop.framework.utils.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public abstract class AnnotationBeanFactory implements BeanFactory{

    Map<String, Object> beanMap;

    public AnnotationBeanFactory() {
        beanMap = new HashMap<>();
    }

    @Override
    public void init() {
        beforeBeanScan(this);
        startToScanBeans(getBasePackages());
        afterBeanScan(this);

        beforeInjection(this);
        injectDependencies();
        afterInjection(this);
    }

    protected void injectDependencies() {
        for (String beanName: beanMap.keySet()) {
            Object bean = beanMap.get(beanName);
            // 属性注入
            for (Field field: bean.getClass().getDeclaredFields()){
                Autowired autowired = field.getAnnotation(Autowired.class);
                if (autowired==null)continue;
                Object obj = getBean(field.getType());
                field.setAccessible(true);
                onInjecting(field, obj);
                try {
                    field.set(bean,obj);
                    Object newBean = afterOnceInjecting(bean, field, obj);
                    if (newBean != null) beanMap.put(beanName, newBean);
                } catch (IllegalAccessException e) {}
            }
            // set方法注入
            // 构造器注入
        }
    }

    /**
     * 扫描带有@Component注解的类，创建Bean，解决依赖关系
     * @param basePackages
     */
    private void startToScanBeans(String[] basePackages) {
        for (String pkg: basePackages) scanPackage(pkg);
    }

    private void scanPackage(String pkg) {
        try {
            Set<Class<?>> classes = ClassUtils.getAllClassesInPackage(pkg);
            for (Class<?> clz: classes) {

                Component component = clz.getAnnotation(Component.class);
                if (component==null)continue;

                String componentName = component.name().equals("") ? clz.getSimpleName() : component.name();
                if (containsBean(componentName))
                    throw new DuplicateBeanDefinitionException(componentName,clz.getName(),getType(componentName).getName());

                Object beanObj;
                try {
                    beanObj = clz.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new CannotCreateBeanException(componentName, clz.getName(), e);
                }

                Object newObject = onBeanScaning(new AbstractMap.SimpleEntry<>(componentName,beanObj));
                if (newObject==null) newObject = beanObj;
                beanMap.put(componentName, newObject);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    abstract String[] getBasePackages();

    @Override
    public Object getBean(String name) throws BeansException {
        if (containsBean(name)) return beanMap.get(name);
        throw new NoSuchBeanDefinitionException(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        Object bean = getBean(name);
        if (requiredType.isInstance(bean)) return (T) bean;
        throw new NoSuchBeanDefinitionException(name, requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        List<T> beans = new ArrayList<>();
        for (Object bean: beanMap.values()) {
            if (requiredType.isInstance(bean)) beans.add((T) bean);
        }
        if (beans.size() == 1) return beans.get(0);
        else if (beans.size() > 1) throw new AmbiguousBeanDefinitionException(requiredType, (Class<?>[]) beans.stream().map(b->b.getClass()).toArray());
        throw new NoSuchBeanDefinitionException(requiredType);
    }

    @Override
    public boolean containsBean(String name) {
        return beanMap.containsKey(name);
    }

    @Override
    public boolean isSingleTon(String name) throws NoSuchBeanDefinitionException {
        return true;
    }

    @Override
    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException {
        Object bean = getBean(name);
        return targetType.isInstance(bean);
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getBean(name).getClass();
    }

    @Override
    public Map<String, Object> getBeans() {
        return beanMap;
    }
}
