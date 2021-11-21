package fr.pinguet62.xjc.swagger.option;

import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import io.swagger.annotations.ApiModelProperty;
import org.junit.jupiter.api.Test;

import static fr.pinguet62.xjc.common.test.JavaParserUtils.findArgument;
import static fr.pinguet62.xjc.common.test.JavaParserUtils.findFieldAnnotation;
import static fr.pinguet62.xjc.swagger.SwaggerPluginTestRunner.runDriverAndParseClass;
import static fr.pinguet62.xjc.swagger.option.DataTypeStrategy.SIMPLE_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @see DataTypeStrategy
 */
class DataTypeStrategyTest {

    /**
     * @see DataTypeStrategy#SIMPLE_NAME
     */
    @Test
    void test_custom() {
        String[] args = {"-Xswagger-dataTypeStrategy=" + SIMPLE_NAME.name()};

        TypeDeclaration<?> type = runDriverAndParseClass("AllTypesClass", args);

        assertEquals("BigDecimal", ((StringLiteralExpr) findArgument(findFieldAnnotation(type, "decimalAttr", ApiModelProperty.class), "dataType")).getValue());
    }

    @Test
    void test_default() {
        String[] args = {};

        TypeDeclaration<?> type = runDriverAndParseClass("AllTypesClass", args);

        assertEquals("java.math.BigDecimal", ((StringLiteralExpr) findArgument(findFieldAnnotation(type, "decimalAttr", ApiModelProperty.class), "dataType")).getValue());
    }

}
