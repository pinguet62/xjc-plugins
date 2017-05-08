package fr.pinguet62.xjc.listtoset;

import static fr.pinguet62.xjc.common.test.JavaParserUtils.findField;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.findMethod;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.sun.tools.xjc.Driver;

public class ListtosetPluginTest {

    private static TypeDeclaration generateAndParse(String[] args) throws Exception {
        Driver.run(args, System.out, System.out);

        CompilationUnit compilationUnit = JavaParser.parse(Paths.get("target/fr/pinguet62/Model.java").toFile());
        return compilationUnit.getTypes().get(0);
    }

    @Before
    public void clear() throws IOException {
        deleteDirectory(new File("target/fr/pinguet62"));
    }

    /** Properties declared into {@code binding.jxb} file. */
    @Test
    public void test_binding() throws Exception {
        TypeDeclaration modelType = generateAndParse(new String[] { "src/test/resources/test.xsd", "-b",
                "src/test/resources/binding.xjb", "-extension", "-XlistToSet", "-d", "target" });

        // Field
        assertEquals("String", findField(modelType, "single").getType().toString());
        assertEquals("Set<String>", findField(modelType, "multiple").getType().toString());
        assertEquals("List<String>", findField(modelType, "notInBinding").getType().toString());

        // Getter
        assertEquals("String", findMethod(modelType, "getSingle").getType().toString());
        assertEquals("Set<String>", findMethod(modelType, "getMultiple").getType().toString());
        assertEquals("List<String>", findMethod(modelType, "getNotInBinding").getType().toString());
    }

    /** Process all properties. */
    @Test
    public void test_processAll() throws Exception {
        TypeDeclaration modelType = generateAndParse(new String[] { "src/test/resources/test.xsd", "-extension", "-XlistToSet",
                "-XlistToSet-processAll", "-d", "target" });

        // Field
        assertEquals("String", findField(modelType, "single").getType().toString());
        assertEquals("Set<String>", findField(modelType, "multiple").getType().toString());
        assertEquals("Set<String>", findField(modelType, "notInBinding").getType().toString());

        // Getter
        assertEquals("String", findMethod(modelType, "getSingle").getType().toString());
        assertEquals("Set<String>", findMethod(modelType, "getMultiple").getType().toString());
        assertEquals("Set<String>", findMethod(modelType, "getNotInBinding").getType().toString());
    }

}