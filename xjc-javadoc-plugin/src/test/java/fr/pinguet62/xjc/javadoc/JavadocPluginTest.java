package fr.pinguet62.xjc.javadoc;

import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.junit.jupiter.api.Test;

import static fr.pinguet62.xjc.common.test.JavaParserUtils.findFieldComment;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.formatComments;
import static fr.pinguet62.xjc.javadoc.JavadocPluginTestRunner.runDriverAndParseClass;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JavadocPluginTest {

    @Test
    void test_class_class() {
        assertTrue(formatComments(runDriverAndParseClass("UncommentedClass").getComment().get()).isEmpty());
        assertEquals("Comment of xs:element CommentedClass", formatComments(runDriverAndParseClass("CommentedClass").getComment().get()).get(0));
    }

    @Test
    void test_class_field() {
        TypeDeclaration<?> type = runDriverAndParseClass("FieldClass");
        assertTrue(formatComments(findFieldComment(type, "uncommentedAttr")).isEmpty());
        assertEquals("Comment of xs:element commentedAttr", formatComments(findFieldComment(type, "commentedAttr")).get(0));
    }

    @Test
    void test_enum_class() {
        assertTrue(formatComments(runDriverAndParseClass("UncommentedEnum").getComment().get()).isEmpty());
        assertEquals("Comment of xs:simpleType CommentedEnum", formatComments(runDriverAndParseClass("CommentedEnum").getComment().get()).get(0));
    }

    @Test
    void test_enum_entry() {
        EnumDeclaration type = (EnumDeclaration) runDriverAndParseClass("EntryEnum");
        assertTrue(formatComments(findFieldComment(type, "UNCOMMENTED_VALUE")).isEmpty());
        assertEquals("Comment of xs:enumeration", formatComments(findFieldComment(type, "COMMENTED_VALUE")).get(0));
    }

    @Test
    void test_group_field() {
        TypeDeclaration<?> type = runDriverAndParseClass("SampleGroupClass");
        assertEquals("Comment of xs:group GroupClass.attr", formatComments(findFieldComment(type, "attr")).get(0));
    }

}
