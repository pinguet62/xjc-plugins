package fr.pinguet62.xjc.test.otherplugins;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;
import java.util.Set;

import javax.jws.WebService;

import org.junit.Test;

import fr.pinguet62.Sample;

@WebService
public class MainITTest {

    @Test
    public void test() throws NoSuchFieldException, SecurityException {
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