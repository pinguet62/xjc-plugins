package fr.pinguet62.xjc.listtoset;

import static fr.pinguet62.xjc.common.test.JavaParserUtils.findField;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.findMethod;
import static fr.pinguet62.xjc.listtoset.ListtosetPluginTestRunner.generateAndParse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.javaparser.ast.body.TypeDeclaration;
import org.junit.jupiter.api.Test;

class ListtosetPluginTest {

    /** Properties declared into {@code binding.jxb} file. */
    @Test
    void test_default() throws Exception {
        String[] args = new String[] { "-extension", "-b", "src/test/resources/binding.xjb" };

        TypeDeclaration<?> modelType = generateAndParse("Model", args);

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
    void test_processAll() throws Exception {
        String[] args = new String[] { "-Xlisttoset-processAll" };

        TypeDeclaration<?> modelType = generateAndParse("Model", args);

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