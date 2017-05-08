package fr.pinguet62.xjc.javadoc;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** {@code <xs:complexType} & {@code <xs:element>} */
public class ClassAndFieldsTest extends AbstractXjcTest {

    @Override
    protected String getXsdFile() {
        return "ClassAndFields.xsd";
    }

    @Test
    public void test_class() {
        assertTrue(classCommentedWith("CommentedClass", "Comment of xs:element CommentedType"));
        assertTrue(classCommentedWith("UncommentedClass", null));
    }

    @Test
    public void test_field() {
        assertTrue(classFieldCommentedWith("CommentedClass", "commentedAttr", "Comment of xs:element attr"));
        assertTrue(classFieldCommentedWith("CommentedClass", "uncommentedAttr", null));
    }

}