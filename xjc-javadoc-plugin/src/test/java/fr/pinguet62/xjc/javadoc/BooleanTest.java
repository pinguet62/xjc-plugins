package fr.pinguet62.xjc.javadoc;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * {@code <xs:element type="xs:boolean">} & {@link Boolean} (primitive or object)<br>
 * <i>getter</i> can start with {@code "get"} or {@code "is"}
 */
public class BooleanTest extends AbstractXjcTest {

    @Override
    protected String getXsdFile() {
        return "boolean.xsd";
    }

    @Test
    public void test_object() {
        String type = "BooleanClass";
        String field = "objectAttr";
        String javadoc = "Comment of xs:element objectAttr";
        assertTrue(classFieldCommentedWith(type, field, javadoc));
        assertTrue(classGetterCommentedWith(type, field, javadoc));
        assertTrue(classSetterCommentedWith(type, field, javadoc));
    }

    @Test
    public void test_primitive() {
        String type = "BooleanClass";
        String field = "primitiveAttr";
        String javadoc = "Comment of xs:element primitiveAttr";
        assertTrue(classFieldCommentedWith(type, field, javadoc));
        assertTrue(classGetterCommentedWith(type, field, javadoc));
        assertTrue(classSetterCommentedWith(type, field, javadoc));
    }

}