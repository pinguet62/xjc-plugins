package fr.pinguet62.jxb.swagger;

import static java.nio.charset.Charset.defaultCharset;
import static org.apache.commons.io.IOUtils.readLines;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.tools.xjc.Driver;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class ApplicationTest {

    static class ClassAnnotation {
        static ClassAnnotation parse(String line) {
            ClassAnnotation annotation = new ClassAnnotation();
            annotation.description = RegexUtils.get(line, "description = \"[^\"]*\"");
            if (annotation.description != null)
                annotation.description = annotation.description.replace("description = \"", "").replace("\"", "");
            return annotation;
        }

        String description;
    }

    static class FieldAnnotation {
        static FieldAnnotation parse(String line) {
            FieldAnnotation annotation = new FieldAnnotation();
            annotation.value = RegexUtils.get(line, "value = \"[^\"]*\"");
            if (annotation.value != null)
                annotation.value = annotation.value.replace("value = \"", "").replace("\"", "");
            annotation.dataType = RegexUtils.get(line, "dataType = \"[^\"]*\"");
            if (annotation.dataType != null)
                annotation.dataType = annotation.dataType.replace("dataType = \"", "").replace("\"", "");
            annotation.required = line.contains("required = true");
            return annotation;
        }

        String dataType;

        Boolean required;

        String value;
    }

    private static final String OUTPUT = "target";

    @BeforeClass
    public static void beforeClass() throws Exception {
        Driver.run(new String[] { "src/test/resources/model.xsd", "-Xswagger", "-d", "target" }, System.out, System.out);
    }

    private ClassAnnotation parseApiModel(String type) {
        List<String> lines = readType(type);
        for (String line : lines)
            if (line.matches("^@" + ApiModel.class.getSimpleName() + ".*"))
                return ClassAnnotation.parse(line);
        throw new RuntimeException("Annotation " + ApiModel.class + " not found");
    }

    private FieldAnnotation parseApiModelProperty(String type, String field) {
        List<String> lines = readType(type);
        String foundLine = null;
        for (String line : lines)
            if (line.matches("^\\s*@" + ApiModelProperty.class.getSimpleName() + ".*"))
                foundLine = line;
            else if (line.matches("^\\s*(private|protected) .+ " + field + ";"))
                return FieldAnnotation.parse(foundLine);
        throw new RuntimeException("Annotation " + ApiModelProperty.class + " not found");
    }

    private List<String> readType(String type) {
        String path = OUTPUT + "/fr/pinguet62/jaxb/swagger/model/" + type + ".java";
        try (InputStream inputStream = new FileInputStream(path)) {
            return readLines(inputStream, defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_comments_element() {
        assertNotNull(parseApiModel("CommentClass"));
        assertEquals("Comment of xs:element CommentType", parseApiModel("CommentClass").description);
        assertNotNull(parseApiModelProperty("CommentClass", "commentedAttr"));
        assertEquals("Comment of xs:element attr", parseApiModelProperty("CommentClass", "commentedAttr").value);
        assertNull(parseApiModelProperty("CommentClass", "uncommentedAttr").value);
    }

    // @Test
    // public void test_comments_enum() {
    // assertThat(EnumCommentClass.class, allOf(hasApiModelAnnotation()));
    // assertThat(EnumCommentClass.class, enumValue("COMMENTED_VALUE",
    // allOf(hasApiModelPropertyAnnotation(), hasValue("Comment of xs:enumeration COMMENTED_VALUE"))));
    // assertThat(EnumCommentClass.class, enumValue("UNCOMMENTED_VALUE", allOf(hasApiModelPropertyAnnotation(),
    // hasValue(""))));
    // }

    @Test
    public void test_complexElement() {
        assertEquals("boolean", parseApiModelProperty("AllTypesClass", "booleanAttr").dataType);
        assertEquals("byte", parseApiModelProperty("AllTypesClass", "byteAttr").dataType);
        assertEquals(XMLGregorianCalendar.class.getName(), parseApiModelProperty("AllTypesClass", "dateAttr").dataType);
        assertEquals(XMLGregorianCalendar.class.getName(), parseApiModelProperty("AllTypesClass", "dateTimeAttr").dataType);
        assertEquals(BigDecimal.class.getName(), parseApiModelProperty("AllTypesClass", "decimalAttr").dataType);
        assertEquals("double", parseApiModelProperty("AllTypesClass", "doubleAttr").dataType);
        assertEquals(Duration.class.getName(), parseApiModelProperty("AllTypesClass", "durationAttr").dataType);
        assertEquals("int", parseApiModelProperty("AllTypesClass", "intAttr").dataType);
        assertEquals(BigInteger.class.getName(), parseApiModelProperty("AllTypesClass", "integerAttr").dataType);
        assertEquals("long", parseApiModelProperty("AllTypesClass", "longAttr").dataType);
        assertEquals("short", parseApiModelProperty("AllTypesClass", "shortAttr").dataType);
        assertEquals(String.class.getName(), parseApiModelProperty("AllTypesClass", "stringAttr").dataType);
        assertEquals(XMLGregorianCalendar.class.getName(), parseApiModelProperty("AllTypesClass", "timeAttr").dataType);
        assertEquals("fr.pinguet62.jaxb.swagger.model.CommentClass",
                parseApiModelProperty("AllTypesClass", "complexAttr").dataType);
        assertEquals("fr.pinguet62.jaxb.swagger.model.EnumCommentClass",
                parseApiModelProperty("AllTypesClass", "enumAttr").dataType);
    }

    @Test
    public void test_group() throws Exception {
        assertNotNull(parseApiModel("UseGroupClass"));
        assertEquals("First value", parseApiModelProperty("UseGroupClass", "first").value);
    }

    @Test
    public void test_required() {
        assertFalse(parseApiModelProperty("RequiredClass", "optionalAttr").required);
        assertTrue(parseApiModelProperty("RequiredClass", "requiredAttr").required);
        assertFalse(parseApiModelProperty("RequiredClass", "optionalListAttr").required);
        assertTrue(parseApiModelProperty("RequiredClass", "requiredListAttr").required);
    }

}