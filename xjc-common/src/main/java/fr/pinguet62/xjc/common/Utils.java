package fr.pinguet62.xjc.common;

import com.sun.codemodel.*;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.impl.RestrictionSimpleTypeImpl;
import jakarta.xml.bind.annotation.XmlElement;

import java.io.StringWriter;

/**
 * Utility methods for
 */
public class Utils {

    /**
     * <u>Warning:</u> If several?
     *
     * @param schemaComponent The top level tag.
     * @return The found {@link JAnnotationUse}<br>
     * {@code null} if not present.
     */
    private static JAnnotationUse getAnnotation(JFieldVar fieldVar, Class<?> annotation) {
        for (JAnnotationUse ann : fieldVar.annotations())
            if (ann.getAnnotationClass().fullName().equals(annotation.getName()))
                return ann;
        return null;
    }

    private static String getAnnotationAttribute(JAnnotationUse annotation, String attribute) {
        JAnnotationValue annotationValue = annotation.getAnnotationMembers().get(attribute);
        if (annotationValue == null)
            return null;
        StringWriter writer = new StringWriter();
        JFormatter formatter = new JFormatter(writer);
        annotationValue.generate(formatter);
        return writer.toString();
    }

    /**
     * Extract the XSD documentation.
     *
     * @param schemaComponent The top level tag.
     * @return The content of XSD tag.<br>
     * {@code null} if tag is absent.
     */
    public static String getDocumentation(XSComponent schemaComponent) {
        XSAnnotation xsAnnotation = schemaComponent.getAnnotation();
        if (xsAnnotation == null)
            return null;
        BindInfo annotation = (BindInfo) xsAnnotation.getAnnotation();
        if (annotation == null)
            return null;

        return annotation.getDocumentation();
    }

    public static JMethod getMethod(ClassOutline classOutline, String methodName) {
        for (JMethod method : classOutline.implClass.methods())
            if (method.name().equals(methodName))
                return method;
        return null;
    }

    /**
     * Parse {@link XmlElement} annotation and get {@link XmlElement#required()} attribute.
     *
     * @param field The field to analyze.
     * @return If the field is required.
     * @see XmlElement#required()
     */
    public static boolean isRequired(JFieldVar field) {
        JAnnotationUse xmlElement = getAnnotation(field, XmlElement.class);
        if (xmlElement == null)
            return false;
        return "true".equals(getAnnotationAttribute(xmlElement, "required"));
    }

    public static XSComponent resolveIndirectAccessToField(XSComponent component) {
        if (component instanceof XSParticle)
            return ((XSParticle) component).getTerm();
        else if (component instanceof XSAttributeUse)
            return ((XSAttributeUse) component).getDecl();
        else if (component instanceof RestrictionSimpleTypeImpl)
            return component;
        else
            throw new UnsupportedOperationException(
                    "Unknown " + XSComponent.class.getSimpleName() + " type: " + component.getClass());
    }

    public static void setAnnotationValue(JAnnotationUse annotation, String key, Boolean value) {
        if (value == null)
            return; // value = default;
        annotation.param(key, value);
    }

    public static void setAnnotationValue(JAnnotationUse annotation, String key, String value) {
        if (value == null)
            return; // value = default;
        annotation.param(key, value);
    }

}
