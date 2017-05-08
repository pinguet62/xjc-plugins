package fr.pinguet62.xjc.javadoc;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

/** {@code <xs:element maxOccurs="unbounded">} */
public class CardinalityTest extends AbstractXjcTest {

    @Override
    protected String getXsdFile() {
        return "Cardinality.xsd";
    }

    /** No <i>setter</i> for {@link List} */
    @Test
    public void test_many() {
        String type = "CardinalityClass";
        String field = "multipleAttr";
        String javadoc = "Comment of xs:element multipleAttr";
        assertTrue(classFieldCommentedWith(type, field, javadoc));
        assertTrue(classGetterCommentedWith(type, field, javadoc));
        try {
            classSetterCommentedWith(type, field, javadoc);
            fail();
        } catch (NotFoundException e) {}
    }

    @Test
    public void test_single() {
        String type = "CardinalityClass";
        String field = "singleAttr";
        String javadoc = "Comment of xs:element singleAttr";
        assertTrue(classFieldCommentedWith(type, field, javadoc));
        assertTrue(classGetterCommentedWith(type, field, javadoc));
        assertTrue(classSetterCommentedWith(type, field, javadoc));
    }

}