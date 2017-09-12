package fr.pinguet62.xjc.swagger;

import static fr.pinguet62.xjc.common.test.JavaParserUtils.findAnnotation;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.findArgument;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.findEntry;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.findFieldAnnotation;
import static fr.pinguet62.xjc.swagger.SwaggerPluginTestRunner.runDriverAndParseClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Test;

import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class SwaggerPluginTest {

    /**
     * @see ApiModel#description()
     * @see ApiModelProperty#value()
     */
    @Test
    public void test_comments() {
        // Class
        TypeDeclaration<?> commentType = runDriverAndParseClass("CommentClass");
        // * class
        assertEquals("Comment of xs:element CommentType",
                ((StringLiteralExpr) findArgument(findAnnotation(commentType, ApiModel.class), "description")).getValue());
        // * field
        assertEquals("Comment of xs:element attr", ((StringLiteralExpr) findArgument(
                findFieldAnnotation(commentType, "commentedAttr", ApiModelProperty.class), "value")).getValue());
        assertNull((findArgument(findFieldAnnotation(commentType, "uncommentedAttr", ApiModelProperty.class), "value")));

        // Enum
        TypeDeclaration<?> enumType = runDriverAndParseClass("EnumCommentClass");
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
        TypeDeclaration<?> type = runDriverAndParseClass("AllTypesClass");

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
                    ((StringLiteralExpr) findArgument(findFieldAnnotation(type, attribute, ApiModelProperty.class),
                            "dataType")).getValue());
        }
    }

    /** @see ApiModelProperty#required() */
    @Test
    public void test_required() {
        TypeDeclaration<?> requiredType = runDriverAndParseClass("RequiredClass");
        Class<ApiModelProperty> annotation = ApiModelProperty.class;

        assertFalse(((BooleanLiteralExpr) findArgument(findFieldAnnotation(requiredType, "optionalAttr", annotation),
                "required")).getValue());
        assertTrue(((BooleanLiteralExpr) findArgument(findFieldAnnotation(requiredType, "requiredAttr", annotation),
                "required")).getValue());
        assertFalse(((BooleanLiteralExpr) findArgument(findFieldAnnotation(requiredType, "optionalListAttr", annotation),
                "required")).getValue());
        assertTrue(((BooleanLiteralExpr) findArgument(findFieldAnnotation(requiredType, "requiredListAttr", annotation),
                "required")).getValue());
    }

}