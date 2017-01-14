package fr.pinguet62.jxb.swagger;

import java.io.StringWriter;

import javax.xml.bind.annotation.XmlElement;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JAnnotationValue;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JFormatter;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSParticle;

public final class Utils {

    /**
     * Extract the XSD documentation.
     * 
     * @return The content of XSD tag.<br>
     *         {@code null} if tag is absent.
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

    public static XSComponent resolveIndirectAccessToField(XSComponent component) {
        if (component instanceof XSParticle)
            return ((XSParticle) component).getTerm();
        else if (component instanceof XSAttributeUse)
            return ((XSAttributeUse) component).getDecl();
        else
            throw new UnsupportedOperationException(
                    "Unknown " + XSComponent.class.getSimpleName() + " type: " + component.getClass());
    }

    /** {@code XmlElement::required() default false} */
    public static boolean isRequired(JFieldVar field) {
        JAnnotationUse xmlElement = getAnnotation(field, XmlElement.class);
        if (xmlElement == null)
            return false;
        return "true".equals(getAnnotationAttribute(xmlElement, "required"));
    }

    /**
     * <u>Warning:</u> If several?
     * 
     * @return The found {@link JAnnotationUse}<br>
     *         {@code null} if not present.
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

    public static void setAnnotationValue(JAnnotationUse annotation, String key, String value) {
        if (value == null)
            return; // value = defaul;
        annotation.param(key, value);
    }

    public static void setAnnotationValue(JAnnotationUse annotation, String key, Boolean value) {
        if (value == null)
            return; // value = defaul;
        annotation.param(key, value);
    }

    // util
    private Utils() {}

}