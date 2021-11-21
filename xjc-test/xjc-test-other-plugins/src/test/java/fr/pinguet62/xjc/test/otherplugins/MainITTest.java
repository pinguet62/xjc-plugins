package fr.pinguet62.xjc.test.otherplugins;

import fr.pinguet62.Sample;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MainITTest {

    @Test
    void test() throws NoSuchFieldException, SecurityException {
        // before
        Exception inheritance = new Sample(); // tested by compiler
        assertNotNull(inheritance);

        // test
        Set<String> attr = new Sample().getAttr(); // tested by compiler
        assertNotNull(attr);

        // after
        Field attrField = Sample.class.getDeclaredField("attr");
        assertNotNull(attrField.getDeclaredAnnotation(java.lang.Deprecated.class));
    }

}
