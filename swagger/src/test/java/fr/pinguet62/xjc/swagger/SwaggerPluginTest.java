package fr.pinguet62.xjc.swagger;

import static fr.pinguet62.xjc.common.test.JavaParserUtils.findAnnotation;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.findArgument;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.findEntry;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.findField;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Paths;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.sun.tools.xjc.Driver;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class SwaggerPluginTest {

    @BeforeClass
    public static void clearAndGenerate() throws Exception {
        deleteDirectory(new File("target/fr/pinguet62"));
        Driver.run(new String[] { "src/test/resources/model.xsd", "-Xswagger", "-d", "target" }, System.out, System.out);
    }

    private static TypeDeclaration parseClass(String name) {
        try {
            CompilationUnit compilationUnit = JavaParser.parse(Paths.get("target/fr/pinguet62/" + name + ".java").toFile());
            return compilationUnit.getTypes().get(0);
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see ApiModel#description()
     * @see ApiModelProperty#value()
     */
    @Test
    public void test_comments() {
        // Class
        TypeDeclaration commentType = parseClass("CommentClass");
        // * class
        assertEquals("Comment of xs:element CommentType",
                ((StringLiteralExpr) findArgument(findAnnotation(commentType, ApiModel.class), "description")).getValue());
        // * field
        assertEquals("Comment of xs:element attr", ((StringLiteralExpr) findArgument(
                findAnnotation(findField(commentType, "commentedAttr"), ApiModelProperty.class), "value")).getValue());
        assertNull((findArgument(findAnnotation(findField(commentType, "uncommentedAttr"), ApiModelProperty.class), "value")));

        // Enum
        TypeDeclaration enumType = parseClass("EnumCommentClass");
        // * class
        assertEquals("Comment of xs:simpleType EnumCommentClass",
                ((StringLiteralExpr) findArgument(findAnnotation(enumType, ApiModel.class), "description")).getValue());
        // * entry
        assertEquals("Comment of xs:enumeration",
                ((StringLiteralExpr) findArgument(
                        findAnnotation(findEntry((EnumDeclaration) enumType, "COMMENTED_VALUE"), ApiModelProperty.class),
                        "value")).getValue());
        assertNull(findArgument(
                findAnnotation(findEntry((EnumDeclaration) enumType, "UNCOMMENTED_VALUE"), ApiModelProperty.class), "value"));
    }

    /** @see ApiModelProperty#dataType() */
    @Test
    public void test_dataType() {
        TypeDeclaration type = parseClass("AllTypesClass");

        // List of attribute name and dataType value
        // @formatter:off
        String[][] attrValues = {
                { "booleanAttr", "boolean" },
                { "byteAttr", "byte" },
                { "dateAttr", XMLGregorianCalendar.class.getName() },
                { "dateTimeAttr", XMLGregorianCalendar.class.getName() },
                { "decimalAttr", BigDecimal.class.getName() },
                { "doubleAttr", "double" },
                { "durationAttr", Duration.class.getName() },
                { "intAttr", "int" },
                { "integerAttr", BigInteger.class.getName() },
                { "longAttr", "long" },
                { "shortAttr", "short" },
                { "stringAttr", String.class.getName() },
                { "timeAttr", XMLGregorianCalendar.class.getName() },
                { "complexAttr", "fr.pinguet62.CommentClass" },
                { "enumAttr", "fr.pinguet62.EnumCommentClass" }
        };
        // @formatter:on
        for (String[] attrValue : attrValues) {
            String attribute = attrValue[0];
            String dataType = attrValue[1];
            assertEquals(dataType,
                    ((StringLiteralExpr) findArgument(findAnnotation(findField(type, attribute), ApiModelProperty.class),
                            "dataType")).getValue());
        }
    }

    /** @see ApiModelProperty#required() */
    @Test
    public void test_required() {
        TypeDeclaration requiredType = parseClass("RequiredClass");
        Class<ApiModelProperty> annotation = ApiModelProperty.class;

        assertFalse(((BooleanLiteralExpr) findArgument(findAnnotation(findField(requiredType, "optionalAttr"), annotation),
                "required")).getValue());
        assertTrue(((BooleanLiteralExpr) findArgument(findAnnotation(findField(requiredType, "requiredAttr"), annotation),
                "required")).getValue());
        assertFalse(((BooleanLiteralExpr) findArgument(findAnnotation(findField(requiredType, "optionalListAttr"), annotation),
                "required")).getValue());
        assertTrue(((BooleanLiteralExpr) findArgument(findAnnotation(findField(requiredType, "requiredListAttr"), annotation),
                "required")).getValue());
    }

}