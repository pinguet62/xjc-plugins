package fr.pinguet62.jxb.javadoc;

import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.io.IOUtils.readLines;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.tools.xjc.Driver;

public class ApplicationTest {

    private static final String OUTPUT = "target";

    @BeforeClass
    public static void beforeClass() throws Exception {
        Driver.run(new String[] { "src/test/resources/model.xsd", "-Xjavadoc", "-d", "target" }, System.out, System.out);
    }

    private boolean classCommentedWith(String type, String documentation) {
        return findPresence(type, "/\\*\\*", "@Xml.*", documentation);
    }

    /**
     * Check if javadoc is present on {@link Field}.
     *
     * @param type The {@link Class#getSimpleName()} to parse.
     * @param field The {@link Field} to analyze.
     * @param documentation The documentation to find.<br>
     *        Must be short, because multi-line is not supported.
     * @return {@code true} if javadoc found, {@code false} otherwise.
     */
    private boolean classFieldCommentedWith(String type, String field, String documentation) {
        return findPresence(type, "    /\\*\\*", "    protected String " + field + ";", documentation);
    }

    private boolean enumFieldCommentedWith(String type, String field, String documentation) {
        return findPresence(type, "    /\\*\\*", "    " + field + "(,|;)", documentation);
    }

    /**
     * @param type The {@link Class#getSimpleName()} to read.
     * @param regexBegin The first line regex to include.
     * @param regexEnd The line regex who triggers end of process.
     * @param documentation The documentation to find.
     * @return If any line found.
     */
    private boolean findPresence(String type, String regexBegin, String regexEnd, String documentation) {
        // All lines
        String path = OUTPUT + "/fr/pinguet62/jaxb/javadoc/model/" + type + ".java";
        List<String> file;
        try (InputStream inputStream = new FileInputStream(path)) {
            file = readLines(inputStream, defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Find bloc
        List<String> bloc = new ArrayList<>();
        for (String line : file) {
            if (line.matches(regexEnd))
                break; // end of bloc
            if (line.matches(regexBegin))
                bloc.clear(); // start of bloc
            bloc.add(line);
        }

        // Find matching
        for (String line : bloc)
            if (line.contains(documentation))
                return true;
        return false; // not found
    }

    @Test
    public void test() {
        // Class
        assertTrue(classCommentedWith("CommentClass", "Comment of xs:element CommentType"));
        // assertTrue(classCommentedWith(UncommentClass.class, null));
        // Field
        assertTrue(classFieldCommentedWith("CommentClass", "commentedAttr", "Comment of xs:element attr"));
        // assertTrue(classFieldCommentedWith(CommentClass.class, "uncommentedAttr", null));
        // Enum
        assertTrue(classCommentedWith("EnumCommentClass", "Comment of xs:simpleType EnumCommentClass"));
        assertTrue(enumFieldCommentedWith("EnumCommentClass", "COMMENTED_VALUE", "Comment of xs:enumeration COMMENTED_VALUE"));
        // assertTrue(enumFieldCommentedWith(EnumCommentClass.class, "UNCOMMENTED_VALUE", null));
    }

}