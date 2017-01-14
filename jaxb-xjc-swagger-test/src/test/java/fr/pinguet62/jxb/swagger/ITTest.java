package fr.pinguet62.jxb.swagger;

import static fr.pinguet62.jxb.swagger.MatchersUtils.field;
import static fr.pinguet62.jxb.swagger.MatchersUtils.hasApiModelAnnotation;
import static fr.pinguet62.jxb.swagger.MatchersUtils.hasApiModelPropertyAnnotation;
import static fr.pinguet62.jxb.swagger.MatchersUtils.hasDataType;
import static fr.pinguet62.jxb.swagger.MatchersUtils.hasDescription;
import static fr.pinguet62.jxb.swagger.MatchersUtils.hasValue;
import static fr.pinguet62.jxb.swagger.MatchersUtils.isRequired;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Test;

import fr.pinguet62.jaxb.swagger.model.AllTypesClass;
import fr.pinguet62.jaxb.swagger.model.CommentClass;
import fr.pinguet62.jaxb.swagger.model.EnumCommentClass;
import fr.pinguet62.jaxb.swagger.model.RequiredClass;
import fr.pinguet62.jaxb.swagger.model.UseGroupClass;

public class ITTest {

    @Test
    public void test_comments_element() {
        assertThat(CommentClass.class, allOf(hasApiModelAnnotation(), hasDescription("Comment of xs:element CommentType")));
        assertThat(CommentClass.class,
                field("commentedAttr", allOf(hasApiModelPropertyAnnotation(), hasValue("Comment of xs:element attr"))));
        assertThat(CommentClass.class, field("uncommentedAttr", allOf(hasApiModelPropertyAnnotation(), hasValue(""))));
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
    public void test_required() {
        assertThat(RequiredClass.class, field("optionalAttr", allOf(hasApiModelPropertyAnnotation(), isRequired(false))));
        assertThat(RequiredClass.class, field("requiredAttr", allOf(hasApiModelPropertyAnnotation(), isRequired(true))));
        assertThat(RequiredClass.class, field("optionalListAttr", allOf(hasApiModelPropertyAnnotation(), isRequired(false))));
        assertThat(RequiredClass.class, field("requiredListAttr", allOf(hasApiModelPropertyAnnotation(), isRequired(true))));
    }

    @Test
    public void test_complexElement() {
        assertThat(AllTypesClass.class, field("booleanAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType("boolean"))));
        assertThat(AllTypesClass.class, field("byteAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType("byte"))));
        assertThat(AllTypesClass.class,
                field("dateAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType(XMLGregorianCalendar.class.getName()))));
        assertThat(AllTypesClass.class, field("dateTimeAttr",
                allOf(hasApiModelPropertyAnnotation(), hasDataType(XMLGregorianCalendar.class.getName()))));
        assertThat(AllTypesClass.class,
                field("decimalAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType(BigDecimal.class.getName()))));
        assertThat(AllTypesClass.class, field("doubleAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType("double"))));
        assertThat(AllTypesClass.class,
                field("durationAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType(Duration.class.getName()))));
        assertThat(AllTypesClass.class, field("intAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType("int"))));
        assertThat(AllTypesClass.class,
                field("integerAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType(BigInteger.class.getName()))));
        assertThat(AllTypesClass.class, field("longAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType("long"))));
        assertThat(AllTypesClass.class, field("shortAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType("short"))));
        assertThat(AllTypesClass.class,
                field("stringAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType(String.class.getName()))));
        assertThat(AllTypesClass.class,
                field("timeAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType(XMLGregorianCalendar.class.getName()))));
        assertThat(AllTypesClass.class,
                field("complexAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType(CommentClass.class.getName()))));
        assertThat(AllTypesClass.class,
                field("enumAttr", allOf(hasApiModelPropertyAnnotation(), hasDataType(EnumCommentClass.class.getName()))));
    }

    @Test
    public void test_group() throws Exception {
        assertThat(UseGroupClass.class, allOf(hasApiModelAnnotation()));
        assertThat(UseGroupClass.class, field("first", allOf(hasApiModelPropertyAnnotation(), hasValue("First value"))));
    }

}