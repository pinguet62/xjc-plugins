package fr.pinguet62.xjc.javadoc;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** {@code <xs:restriction>} & {@link Enum} */
public class EnumerationTest extends AbstractXjcTest {

    @Override
    protected String getXsdFile() {
        return "Enumeration.xsd";
    }

    @Test
    public void test() {
        assertTrue(classCommentedWith("EnumCommentClass", "Comment of xs:simpleType EnumCommentClass"));
        assertTrue(enumFieldCommentedWith("EnumCommentClass", "COMMENTED_VALUE", "Comment of xs:enumeration COMMENTED_VALUE"));
        assertTrue(enumFieldCommentedWith("EnumCommentClass", "UNCOMMENTED_VALUE", null));
    }

}