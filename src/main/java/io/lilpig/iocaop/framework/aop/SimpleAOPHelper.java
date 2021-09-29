package io.lilpig.iocaop.framework.aop;

import io.lilpig.iocaop.framework.annotations.*;
import io.lilpig.iocaop.framework.aop.exceptions.AOPMethodExecuteException;
import io.lilpig.iocaop.framework.aop.exceptions.CannotCreateAspectException;
import io.lilpig.iocaop.framework.aop.exceptions.NoPointCutInThisClassException;
import io.lilpig.iocaop.framework.exceptions.MissingRequiredConfigItemException;
import io.lilpig.iocaop.framework.utils.ClassUtils;
import io.lilpig.iocaop.framework.utils.PropertyReader;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SimpleAOPHelper implements AOPHelper{

    private Map<String, List<Object>> aspectList;

    public SimpleAOPHelper(PropertyReader config) throws IOException, ClassNotFoundException {
        aspectList = new HashMap<>();

        String pkgStr = config.getProperty("aop.scanPackages");
        if (pkgStr==null) {
            throw new MissingRequiredConfigItemException("aop.scanPackages", "configFile classpath:" + config.getSource());
        }

        scanPackages(pkgStr.split(","));

    }

    private void scanPackages(String[] packages) {
        for (String pkg: packages) scanPackage(pkg);
    }

    private void scanPackage(String pkg) {
        try {
            Set<Class<?>> classSet = ClassUtils.getAllClassesInPackage(pkg);
            for (Class<?> clz : classSet) {
                Aspect aspect = clz.getAnnotation(Aspect.class);
                if (aspect==null) continue;

                Object aspectInstance;

                try {
                    aspectInstance = clz.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new CannotCreateAspectException(clz.getName());
                }

                String beanClasses[] = aspect.beanClasses();

                for (String bclz: beanClasses) {
                    if(!aspectList.containsKey(bclz)) aspectList.put(bclz, new ArrayList<>());
                    aspectList.get(bclz).add(aspectInstance);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasPointCut(Class<?> targetClass) {
        return aspectList.containsKey(targetClass.getName());
    }

    @Override
    public Object weaving(Object originalBean, Class<?> targetClass) {
        if (!hasPointCut(targetClass)) throw new NoPointCutInThisClassException(targetClass.getName());
        List<Object> aspects = aspectList.get(targetClass.getName());

        // 创建动态代理
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                Object methodResult = null;
//                Class<?> argClz[] = new Class[args.length];
//                for(int i=0; i < args.length; i++) argClz[i] = args[i].getClass();
//

                for (Object aspect: aspects) {
                    Method notifications[] = aspect.getClass().getMethods();
                    // 如果存在环形注解，那么取消之后的遍历，只允许添加一次环形注解
                    if(handleAround(aspect, notifications, originalBean, method, args)) break;
                    for (Method notification : notifications) handleBefore(aspect,notification, method);
                    try{
                        methodResult = method.invoke(originalBean, args);
                        for (Method notification : notifications) handleAfterReturning(aspect,notification, method);
                    }catch (Throwable t) {
                        for (Method notification : notifications) handleAfterThrowing(aspect,notification, method);
                        t.printStackTrace();
                    }
                }
                return methodResult;
            }
        });
        return enhancer.create();
    }

    public boolean handleAround(Object aspect, Method notifications[], Object originalBean, Method originalMethod, Object[] args) {
        // 循环扫描获取第一个环形注解
        Around around = null;
        Method notification = null;
        for (Method _notification: notifications) {
            around = _notification.getAnnotation(Around.class);
            if (around!=null) {
                notification = _notification;
                break;
            }
        }

        // 如果没有环形注解
        if (around==null) return false;

        // 使用一个MethodProxy代理包装实际的方法调用，允许用户在特定位置通过代理调用实际方法，使用回调模式接收调用通知并发起实际调用动作
        io.lilpig.iocaop.framework.aop.MethodProxy proxy = new DefaultMethodProxy(new MethodProxyInvokeListener() {
            @Override
            public Object onMethodProxyInvoked() {
                try {
                    return originalMethod.invoke(originalBean, args);
                } catch (Exception e) {
                    throw new AOPMethodExecuteException(e);
                }
            }
        });

        try {
            notification.invoke(aspect, proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
    public void handleBefore(Object aspect, Method notification, Method originalMethod) {
        handleJoinPoint(aspect,notification,originalMethod,Before.class);
    }

    private void handleAfterReturning(Object aspect, Method notification, Method method) {
        handleJoinPoint(aspect,notification,method, AfterReturning.class);
    }

    private void handleAfterThrowing(Object aspect, Method notification, Method method){
        handleJoinPoint(aspect,notification,method, AfterThrowing.class);
    }

    public <T extends Annotation> void handleJoinPoint(Object aspect, Method notification, Method originalMethod, Class<T> annotationClass) {
        T t = notification.getAnnotation(annotationClass);
        if (t==null) return;

        String pointCutMethodName = null;
        try { pointCutMethodName = (String) t.getClass().getMethod("methodName").invoke(t);} catch (Exception e) {}

        if (pointCutMethodName.equals("*") || pointCutMethodName.equals(originalMethod.getName())){
            try {
                notification.invoke(aspect);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
