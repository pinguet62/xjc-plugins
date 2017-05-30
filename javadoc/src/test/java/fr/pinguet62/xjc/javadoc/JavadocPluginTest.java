package fr.pinguet62.xjc.javadoc;

import static fr.pinguet62.xjc.common.test.JavaParserUtils.findEntry;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.findField;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.formatComments;
import static fr.pinguet62.xjc.javadoc.JavadocPluginTestRunner.runDriverAndParseClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

public class JavadocPluginTest {

    @Test
    public void test_class_class() {
        assertTrue(formatComments(runDriverAndParseClass("UncommentedClass").getComment()).isEmpty());
        assertEquals("Comment of xs:element CommentedClass",
                formatComments(runDriverAndParseClass("CommentedClass").getComment()).get(0));
    }

    @Test
    public void test_class_field() {
        TypeDeclaration type = runDriverAndParseClass("FieldClass");
        assertTrue(formatComments(findField(type, "uncommentedAttr").getComment()).isEmpty());
        assertEquals("Comment of xs:element commentedAttr",
                formatComments(findField(type, "commentedAttr").getComment()).get(0));
    }

    @Test
    public void test_enum_class() {
        assertTrue(formatComments(runDriverAndParseClass("UncommentedEnum").getComment()).isEmpty());
        assertEquals("Comment of xs:simpleType CommentedEnum",
                formatComments(runDriverAndParseClass("CommentedEnum").getComment()).get(0));
    }

    @Test
    public void test_enum_entry() {
        EnumDeclaration type = (EnumDeclaration) runDriverAndParseClass("EntryEnum");
        assertTrue(formatComments(findEntry(type, "UNCOMMENTED_VALUE").getComment()).isEmpty());
        assertEquals("Comment of xs:enumeration", formatComments(findEntry(type, "COMMENTED_VALUE").getComment()).get(0));
    }

    @Test
    public void test_group_field() {
        TypeDeclaration type = runDriverAndParseClass("SampleGroupClass");
        assertEquals("Comment of xs:group GroupClass.attr", formatComments(findField(type, "attr").getComment()).get(0));
    }

}