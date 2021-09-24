package io.lilpig.iocaop;

import io.lilpig.iocaop.framework.AnnotationClasspathResourceBeanFactory;
import io.lilpig.iocaop.test.beans.Fighter;
import org.junit.Test;

public class AnnotationClasspathResourceBeanFactoryTest {
    @Test
    public void testScanAndInjection() {
        AnnotationClasspathResourceBeanFactory beanFactory = new AnnotationClasspathResourceBeanFactory();
        beanFactory.init();
        Fighter fighter = beanFactory.getBean(Fighter.class);
        fighter.explore();
    }
}
