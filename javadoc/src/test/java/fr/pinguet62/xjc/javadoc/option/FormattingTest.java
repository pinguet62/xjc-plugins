package fr.pinguet62.xjc.javadoc.option;

import static fr.pinguet62.xjc.javadoc.JavadocPluginTestRunner.runDriverAndParseClass;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.github.javaparser.ast.body.TypeDeclaration;

/** @see Formatting */
public class FormattingTest {

    @Test
    public void test_custom() {
        String[] args = { "-Xjavadoc-formatting-regex-replace=[a-z3469]", "-Xjavadoc-formatting-regex-by=" };

        TypeDeclaration type = runDriverAndParseClass("FormattingClass", args);
        List<String> lines = asList(type.getComment().getContent().split("\r?\n"));
        assertEquals(" * ", lines.get(1));
        assertEquals(" * ", lines.get(2));
        assertEquals(" * ", lines.get(3));
        assertEquals(" * \t\t\t\t:                          ", lines.get(4));
        assertEquals(" * \t\t\t\t:                          ", lines.get(5));
        assertEquals(" * ", lines.get(6));
        assertEquals(" * \t\t\t\t:                          ", lines.get(7));
        assertEquals(" * ", lines.get(8));
        assertEquals(" * ", lines.get(9));
        assertEquals(" * \t\t\t\t:                          ", lines.get(10));
    }

    /** @see Formatting#DEFAULT */
    @Test
    public void test_default() {
        TypeDeclaration type = runDriverAndParseClass("FormattingClass");
        List<String> lines = asList(type.getComment().getContent().split("\r?\n"));
        assertEquals(" *  3: a b c d e f g h i j k l m n o p q r s t u v w x y z<br>", lines.get(1));
        assertEquals(" *  4: a b c d e f g h i j k l m n o p q r s t u v w x y z<br>", lines.get(2));
        assertEquals(" * <br>", lines.get(3));
        assertEquals(" *  6: a b c d e f g h i j k l m n o p q r s t u v w x y z<br>", lines.get(4));
        assertEquals(" * <br>", lines.get(5));
        assertEquals(" * <br>", lines.get(6));
        assertEquals(" *  9: a b c d e f g h i j k l m n o p q r s t u v w x y z", lines.get(7));
    }

}