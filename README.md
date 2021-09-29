# UPDATE
- 2021/09/29  
    - 新增`@Around`环形通知支持

# IoC&AOP

只是一个简单的Ioc和AOP框架

随便写着玩的，写到最后代码自己都看不懂了，好垃圾

IoC使用反射实现，模仿了Spring的API，AOP使用CGLib实现，写乱套了

不支持SpEL表达式

随缘优化代

# 大概齐是这样吧

## framework包
框架在这

## test包
一个简单的测试用例在这

## framework.annotations
定义了基本的注解，比如`@Component`、`@Aspect`

## framework.aop
aop功能相关代码

### framework.aop.exceptions
异常定义

### AOPHelper
AOPHelper是一个AOP功能的接口，它负责在创建时扫描切面，将切面绑定到接入点上，在对应的时机发送通知等

```java
/**
 * 是否有目标类的切点
 */
boolean hasPointCut(Class<?> targetClass);
/**
 * 织入到orignalBean
 */
Object weaving(Object orignalBean,Class<?> targetClass);
```

这个API设计的太简单了，导致扩展什么的都很难，整个耦合在项目中。我先写的IoC功能，然后不想写了...

### SimpleAOPHelper
一个简单的AOPHelper实现类

## framework.constants包
定义了一些常量

## framework.excetpions包
定义了整个框架公用的一些异常

## framework.utils
定义了一些工具类

## BeanFactory
Bean工厂接口，Bean工厂负责扫描Bean，以及自动注入依赖。

```java
/**
 * BeanFactory进行初始化，在这个阶段中，BeanFactory需要扫描Bean。
 */
void init();

/**
 * 根据Bean名获取Bean
 * @param name
 * @return 获取到的Bean
 * @throws BeansException 创建Bean的过程中出现了一些错误，如Bean未定义，Bean是一个接口等
 */
Object getBean(String name) throws BeansException;

/**
 * 根据Bean名和类型获取一个Bean
 * @param name
 * @param requiredType
 * @param <T>
 * @return 获取到的Bean
 * @throws BeansException
 */
<T> T getBean(String name, Class<T> requiredType) throws BeansException;

/**
 * 根据类型获取一个Bean
 * @param requiredType
 * @param <T>
 * @return 获取到的Bean
 * @throws BeansException
 */
<T> T getBean(Class<T> requiredType) throws BeansException;

/**
 * Bean工厂中是否包含Bean定义
 * @param name Bean名
 * @return
 */
boolean containsBean(String name);

/**
 * Bean是否是单例
 * @param name
 * @return
 * @throws NoSuchBeanDefinitionException
 */
boolean isSingleTon(String name) throws NoSuchBeanDefinitionException;

/**
 * Bean是否是原型
 * @param name
 * @return
 * @throws NoSuchBeanDefinitionException
 */
boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

/**
 * 指定名字的Bean是否具有特定类型
 * @param name
 * @param targetType
 * @return
 * @throws NoSuchBeanDefinitionException
 */
boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException;

/**
 * 获取Bean的类型
 * @param name
 * @return
 * @throws NoSuchBeanDefinitionException
 */
Class<?> getType(String name) throws NoSuchBeanDefinitionException;

// 千万不要用啊！！！
Map<String, Object> getBeans();
```

## LifeCycle
定义了一些针对IoC操作的生命周期方法，BeanFactory实现了LifeCycle，也就是说所有BeanFactory都有这些生命周期方法

```java
/**
 * 执行bean扫描前回调
 * @param factory 当前bean工厂（其实LifeCycle不应该和Bean工厂耦合，应该再抽象出一层Context啥的）
 */
void beforeBeanScan(BeanFactory factory);

/**
 * 执行bean扫描后回调
 * @param factory 当前Bean工厂
 */
void afterBeanScan(BeanFactory factory);

/**
 * 每次扫描到一个Bean但还没设置到factory中时调用
 * @param beanEntry key代表当前bean的名字，value代表即将设置到factory中的bean
 * @return 该方法主要提供一个机会，给即将设置到工厂中的Bean做一些包装或者其他工作，返回的对象会代替原对象进入工厂
 *          如果不希望对Bean进行进一步的处理，可以返回原对象或者返回null
 */
Object onBeanScaning(Map.Entry<String, Object> beanEntry);

// 注入操作执行之前
void beforeInjection(BeanFactory factory);

// 注入操作执行之后
void afterInjection(BeanFactory factory);

// 注入一个属性之后
Object afterOnceInjecting(Object bean, Field injectedField, Object appropriateBean);

// 注入一个属性之前
void onInjecting(Field injectingField, Object appropriateBean);
```
## AnnotationBeanFactory
一个使用模板设计模式的基于注解的`BeanFactory`，只会在LifeCycle方法和获取扫描包的时候调用子类。

AnnotationBeanFactory会在init方法被执行时询问子类，子类需要返回被扫描的包，这些包通过什么途径获得是子类自己决定的事，可以通过properties文件，xml文件，注解，甚至网络。

AnnotationBeanFactory会在创建Bean和执行依赖注入的对应时机调用子类的LifeCycle方法。


## AnnotationClasspathResourceBeanFactory
基于扫描类路径下的Properties资源文件实现的`AnnotationBeanFactory`

## test包
test包不是单元测试，是一个真实的测试用例

## test.beans
使用`@Component`定义了两个bean

Fighter类型的BraveFighter实现类，代表一个勇敢的勇士

勇士依赖一个任务，这个任务需要在外部注入。

Task类型的KillTheDragonTask实现类，代表一个杀掉恶龙的任务，会被注入到BraveFighter中

## test.aop
使用`@Aspect`定义了一个切面

Poet代表一个吟游诗人，他在勇士执行任务前鼓励勇士，在勇士执行任务后歌颂勇士，在勇士任务执行失败时安慰勇士。由于使用了AOP，这一切并不被勇士所知。


# 特别注意
在AOP编程中请不要将`@Around`通知和其他通知在一个方法中一起使用，也不要将多个`@Around`一起使用

如果`@Around`注解被先行扫描，那么其他所有切面将会不工作，本切面中的所有其他通知也会不工作。

如果`@Around`注解并且后扫描，那么后面所有的切面和本切面中的所有其他通知不工作，当前`@Around`通知和之前的通知的稳定性不被保证。

实际上在同一个切面中，`@Around`会被先扫描，其他通知会被后扫描，所以不存在同一个切面中其它的通知会比环形通知先被扫描这一说法。