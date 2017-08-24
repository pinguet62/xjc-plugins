package fr.pinguet62.xjc.swagger.option;

import static fr.pinguet62.xjc.common.test.JavaParserUtils.findAnnotation;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.findArgument;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.findField;
import static fr.pinguet62.xjc.swagger.SwaggerPluginTestRunner.runDriverAndParseClass;
import static fr.pinguet62.xjc.swagger.option.DataTypeStrategy.SIMPLE_NAME;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.StringLiteralExpr;

import io.swagger.annotations.ApiModelProperty;

/** @see DataTypeStrategy */
public class DataTypeStrategyTest {

    /** @see DataTypeStrategy#SIMPLE_NAME */
    @Test
    public void test_custom() {
        String[] args = { "-Xswagger-dataTypeStrategy=" + SIMPLE_NAME.name() };

        TypeDeclaration type = runDriverAndParseClass("AllTypesClass", args);

        assertEquals("BigDecimal",
                ((StringLiteralExpr) findArgument(findAnnotation(findField(type, "decimalAttr"), ApiModelProperty.class),
                        "dataType")).getValue());
    }

    @Test
    public void test_default() {
        String[] args = {};

        TypeDeclaration type = runDriverAndParseClass("AllTypesClass", args);

        assertEquals("java.math.BigDecimal",
                ((StringLiteralExpr) findArgument(findAnnotation(findField(type, "decimalAttr"), ApiModelProperty.class),
                        "dataType")).getValue());
    }

}