package fr.pinguet62.xjc.javadoc;

import static fr.pinguet62.xjc.common.test.JavaParserUtils.findEntry;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.findField;
import static java.util.Arrays.asList;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.sun.tools.xjc.Driver;

public class JavadocPluginTest {

    @BeforeClass
    public static void clearAndGenerate() throws Exception {
        deleteDirectory(new File("target/fr/pinguet62"));
        Driver.run(new String[] { "src/test/resources/model.xsd", "-Xjavadoc", "-d", "target" }, System.out, System.out);
    }

    /** Remove all {@code "*"} and empty first & last lines. */
    private static final List<String> formatComments(Comment comment) {
        String[] lines = comment.getContent().split("\r?\n");
        List<String> comments = new ArrayList<>();
        for (int i = 1; i < lines.length - 2; i++)
            comments.add(lines[i].replaceFirst("^(    )? \\* ", ""));
        return comments;
    }

    private static TypeDeclaration parseClass(String name) {
        try {
            CompilationUnit compilationUnit = JavaParser.parse(Paths.get("target/fr/pinguet62/" + name + ".java").toFile());
            return compilationUnit.getTypes().get(0);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_class_class() {
        assertTrue(formatComments(parseClass("UncommentedClass").getComment()).isEmpty());
        assertEquals("Comment of xs:element CommentedClass", formatComments(parseClass("CommentedClass").getComment()).get(0));
    }

    @Test
    public void test_class_field() {
        TypeDeclaration type = parseClass("FieldClass");
        assertTrue(formatComments(findField(type, "uncommentedAttr").getComment()).isEmpty());
        assertEquals("Comment of xs:element commentedAttr",
                formatComments(findField(type, "commentedAttr").getComment()).get(0));
    }

    @Test
    public void test_enum_class() {
        assertTrue(formatComments(parseClass("UncommentedEnum").getComment()).isEmpty());
        assertEquals("Comment of xs:simpleType CommentedEnum", formatComments(parseClass("CommentedEnum").getComment()).get(0));
    }

    @Test
    public void test_enum_entry() {
        EnumDeclaration type = (EnumDeclaration) parseClass("EntryEnum");
        assertTrue(formatComments(findEntry(type, "UNCOMMENTED_VALUE").getComment()).isEmpty());
        assertEquals("Comment of xs:enumeration", formatComments(findEntry(type, "COMMENTED_VALUE").getComment()).get(0));
    }

    @Test
    public void test_formatting() {
        TypeDeclaration type = parseClass("FormattingClass");
        List<String> lines = asList(type.getComment().getContent().split("\r?\n"));
        assertEquals(" *  3: a b c d e f g h i j k l m n o p q r s t u v w x y z<br>", lines.get(1));
        assertEquals(" *  4: a b c d e f g h i j k l m n o p q r s t u v w x y z<br>", lines.get(2));
        assertEquals(" * <br>", lines.get(3));
        assertEquals(" *  6: a b c d e f g h i j k l m n o p q r s t u v w x y z<br>", lines.get(4));
        assertEquals(" * <br>", lines.get(5));
        assertEquals(" * <br>", lines.get(6));
        assertEquals(" *  9: a b c d e f g h i j k l m n o p q r s t u v w x y z", lines.get(7));
    }

    @Test
    public void test_group_field() {
        TypeDeclaration type = parseClass("SampleGroupClass");
        assertEquals("Comment of xs:group GroupClass.attr", formatComments(findField(type, "attr").getComment()).get(0));
    }

}