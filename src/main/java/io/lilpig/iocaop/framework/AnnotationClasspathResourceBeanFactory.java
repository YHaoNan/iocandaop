package io.lilpig.iocaop.framework;

import io.lilpig.iocaop.framework.aop.AOPHelper;
import io.lilpig.iocaop.framework.constants.ConfigFileConstants;
import io.lilpig.iocaop.framework.exceptions.ClassPathResourceFileNotFoundException;
import io.lilpig.iocaop.framework.exceptions.MissingRequiredConfigItemException;
import io.lilpig.iocaop.framework.utils.PropertyReader;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 当AnnotationClasspathResourceBeanFactory创建，它会读取classpath下的iocaop.properties并依据其中的配置创建Bean
 */
public class AnnotationClasspathResourceBeanFactory extends AnnotationBeanFactory {

    PropertyReader propertyReader;
    AOPHelper aopHelper;


    public AnnotationClasspathResourceBeanFactory() {
        this(ConfigFileConstants.DEFAULT_ANNOTATION_CLASSPATH_RESOURCE_FILENAME);
    }

    public AnnotationClasspathResourceBeanFactory(String configFile) {
        try {
            propertyReader = new PropertyReader(configFile);
            String aopImpl = propertyReader.getProperty("aop.aopHelperImpl");
            if (aopImpl!=null) {
                this.aopHelper = (AOPHelper) Class.forName(aopImpl).getConstructor(PropertyReader.class).newInstance(propertyReader);
            }
        } catch (IOException e) {
            throw new ClassPathResourceFileNotFoundException(configFile);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    String[] getBasePackages() {
        String scanPackagesStr = propertyReader.getProperty("ioc.scanPackages");

        if (scanPackagesStr == null)
            throw new MissingRequiredConfigItemException("ioc.scanPackages",
                    "configFile: classpath:"+propertyReader.getSource());

        return scanPackagesStr.split(",");
    }

    @Override
    public void beforeBeanScan(BeanFactory factory) {}

    @Override
    public void afterBeanScan(BeanFactory factory) {}

    @Override
    public Object onBeanScaning(Map.Entry<String, Object> beanEntry) {
        return null;
    }

    @Override
    public void beforeInjection(BeanFactory factory) {}

    @Override
    public void afterInjection(BeanFactory factory) {
    }

    @Override
    public Object afterOnceInjecting(Object bean, Field injectedField, Object appropriateBean) {
        if (aopHelper==null) return null;
        if(aopHelper.hasPointCut(bean.getClass()))
            return aopHelper.weaving(bean, bean.getClass());
        return null;
    }

    @Override
    public void onInjecting(Field injectingField, Object appropriateBean) {}
}
