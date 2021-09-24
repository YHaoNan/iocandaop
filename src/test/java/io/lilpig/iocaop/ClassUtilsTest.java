package io.lilpig.iocaop;

import io.lilpig.iocaop.framework.utils.ClassUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

public class ClassUtilsTest {
    @Test
    public void testGetAllClassesInPackage(){
        try {
            Set<Class<?>> classesSet = ClassUtils.getAllClassesInPackage("io.lilpig.iocaop.framework");
            System.out.println(classesSet);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
