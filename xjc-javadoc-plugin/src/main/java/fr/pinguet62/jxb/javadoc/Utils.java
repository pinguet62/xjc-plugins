package fr.pinguet62.jxb.javadoc;

import com.sun.codemodel.JMethod;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BindInfo;
import com.sun.xml.xsom.XSAnnotation;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComponent;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.impl.RestrictionSimpleTypeImpl;

public final class Utils {

    /**
     * Extract the XSD documentation.
     *
     * @param schemaComponent The top level tag.
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

    public static JMethod getMethod(ClassOutline classOutline, String methodName) {
        for (JMethod method : classOutline.implClass.methods())
            if (method.name().equals(methodName))
                return method;
        return null;
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

    // util
    private Utils() {}

}