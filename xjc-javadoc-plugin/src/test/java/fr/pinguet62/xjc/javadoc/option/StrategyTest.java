package fr.pinguet62.xjc.javadoc.option;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.pinguet62.xjc.common.test.JavaParserUtils.formatComments;
import static fr.pinguet62.xjc.javadoc.JavadocPluginTestRunner.runDriverAndParseClass;
import static fr.pinguet62.xjc.javadoc.option.Strategy.APPEND_BEGIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @see Strategy
 */
class StrategyTest {

    /**
     * @see Strategy#APPEND_BEGIN
     */
    @Test
    void test_custom() {
        String[] args = {"-Xjavadoc-strategy=" + APPEND_BEGIN.name()};

        TypeDeclaration<?> type = runDriverAndParseClass("CommentedClass", args);

        List<String> comments = formatComments(type.getComment().get());
        assertEquals("Comment of xs:element CommentedClass", comments.get(0));
        assertEquals("", comments.get(1));
        assertTrue(comments.get(2).matches("<p>.*CommentedClass.*"));
        assertEquals("", comments.get(3));
        assertTrue(comments.get(4).matches("<p>.*"));
    }

    @Test
    void test_default() {
        String[] args = {};

        TypeDeclaration<?> type = runDriverAndParseClass("CommentedClass", args);

        List<String> comments = formatComments(type.getComment().get());
        assertEquals(1, comments.size());
        assertEquals("Comment of xs:element CommentedClass", comments.get(0));
    }

}
