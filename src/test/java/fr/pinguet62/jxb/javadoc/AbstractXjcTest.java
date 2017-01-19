package fr.pinguet62.jxb.javadoc;

import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.io.IOUtils.readLines;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import com.sun.tools.xjc.Driver;

/**
 * Base class for unit-tests.
 * <p>
 * Process XSD files with this plugin.<br>
 * Declare utility methods for output file parsing.
 */
public abstract class AbstractXjcTest {

    public static class NotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1;
    }

    public static final String OUTPUT_FOLDER = "target";

    public static final String TARGET_NAMESPACE = "/fr/pinguet62/jaxb/javadoc/model";

    /**
     * Check if target type/field contains expected documentation.
     *
     * @param type The {@link Class#getSimpleName()} to read.
     * @param regexBegin The first line regex to include.
     * @param regexEnd The line regex who triggers end of process.
     * @param expectedDoc The documentation to find.
     * @return If javadoc bloc contains expected javadoc.
     */
    private static boolean checkDocumentation(String type, String regexBegin, String regexEnd, String expectedDoc) {
        // All lines
        String path = OUTPUT_FOLDER + TARGET_NAMESPACE + "/" + type + ".java";
        List<String> file;
        try (InputStream inputStream = new FileInputStream(path)) {
            file = readLines(inputStream, defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Find bloc
        List<String> bloc = new ArrayList<>();
        boolean found = false;
        for (String line : file) {
            if (line.matches(regexEnd)) {
                found = true;
                break; // end of bloc
            }
            if (line.matches(regexBegin))
                bloc.clear(); // start of bloc
            bloc.add(line);
        }
        if (!found)
            throw new NotFoundException();

        String outputDoc = bloc.get(1).replaceFirst("^ *\\* ", "");
        if (expectedDoc == null)
            return outputDoc.isEmpty();
        return outputDoc.equals(expectedDoc);
    }

    public static boolean classCommentedWith(String type, String expectedDoc) {
        return checkDocumentation(type, "/\\*\\*", "@Xml.*", expectedDoc);
    }

    public static boolean classFieldCommentedWith(String type, String field, String expectedDoc) {
        return checkDocumentation(type, "    /\\*\\*", "    protected [^ ]+ " + field + ";", expectedDoc);
    }

    /** @param field The field name (without {@code "get"} prefix). */
    public static boolean classGetterCommentedWith(String type, String field, String expectedDoc) {
        field = String.valueOf(field.charAt(0)).toUpperCase() + field.substring(1);
        return checkDocumentation(type, "    /\\*\\*", "    public [^ ]+ (get|is)" + field + "\\(\\) \\{", expectedDoc);
    }

    /** @param field The field name (without {@code "set"} prefix). */
    public static boolean classSetterCommentedWith(String type, String field, String expectedDoc) {
        field = String.valueOf(field.charAt(0)).toUpperCase() + field.substring(1);
        return checkDocumentation(type, "    /\\*\\*", "    public void set" + field + "\\([^\\)]+\\) \\{", expectedDoc);
    }

    public static boolean enumFieldCommentedWith(String type, String field, String expectedDoc) {
        return checkDocumentation(type, "    /\\*\\*", "    " + field + "(,|;)", expectedDoc);
    }

    /** Process XSD file. */
    @Before
    public final void before() throws Exception {
        Driver.run(new String[] { "src/test/resources/" + getXsdFile(), "-Xjavadoc", "-d", "target" }, System.out, System.out);
    }

    /** Get XSD filename, with extension. */
    protected abstract String getXsdFile();

}