package fr.pinguet62.xjc.javadoc;

import static fr.pinguet62.xjc.javadoc.Utils.getDocumentation;
import static fr.pinguet62.xjc.javadoc.Utils.getMethod;
import static fr.pinguet62.xjc.javadoc.Utils.resolveIndirectAccessToField;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.EnumConstantOutline;
import com.sun.tools.xjc.outline.EnumOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.xml.xsom.XSComponent;

public class JavadocPlugin extends Plugin {

    @Override
    public String getOptionName() {
        return "Xjavadoc";
    }

    @Override
    public String getUsage() {
        return "  -" + getOptionName() + "            : Generates javadoc, based on XSD.";
    }

    @Override
    public boolean run(Outline outline, Options options, ErrorHandler errorHandler) throws SAXException {
        // TODO Maven configuration
        DocumentationFormatterStrategy formatter = DocumentationFormatterStrategy.determineStrategy("allSpaces");
        JavadocRemplacementStrategy javadocApplier = JavadocRemplacementStrategy.determineStrategy("replace");

        // Classes
        for (ClassOutline classOutline : outline.getClasses()) {
            // Class
            String classDocumentation = getDocumentation(classOutline.target.getSchemaComponent());
            if (classDocumentation != null)
                classDocumentation = formatter.process(classDocumentation);
            javadocApplier.apply(classOutline.ref.javadoc(), classDocumentation);

            // Fields
            for (FieldOutline fieldOutline : classOutline.getDeclaredFields()) {
                XSComponent schemaComponent = resolveIndirectAccessToField(fieldOutline.getPropertyInfo().getSchemaComponent());

                String propertyDocumentation = getDocumentation(schemaComponent);
                if (propertyDocumentation != null)
                    propertyDocumentation = formatter.process(propertyDocumentation);

                // Field
                JFieldVar fieldVar = classOutline.implClass.fields().get(fieldOutline.getPropertyInfo().getName(false));
                javadocApplier.apply(fieldVar.javadoc(), propertyDocumentation);

                // Getter
                String getterName = "get" + fieldOutline.getPropertyInfo().getName(true);
                JMethod getter = getMethod(classOutline, getterName);
                // boolean & "is" getter prefix name
                if (getter == null && fieldOutline.getRawType().boxify().getPrimitiveType() == outline.getCodeModel().BOOLEAN) {
                    getterName = getterName.replaceFirst("^get", "is");
                    getter = getMethod(classOutline, getterName);
                }
                javadocApplier.apply(getter.javadoc(), propertyDocumentation);

                // Setter
                JMethod setter = getMethod(classOutline, "set" + fieldOutline.getPropertyInfo().getName(true));
                if (setter == null) // one-to-many has only getter
                    continue;
                javadocApplier.apply(setter.javadoc(), propertyDocumentation);
            }
        }

        // Enumerations
        for (EnumOutline enumOutline : outline.getEnums()) {
            // Class
            String classDocumentation = getDocumentation(enumOutline.target.getSchemaComponent());
            if (classDocumentation != null)
                classDocumentation = formatter.process(classDocumentation);
            javadocApplier.apply(enumOutline.clazz.javadoc(), classDocumentation);

            // Values
            for (EnumConstantOutline enumConstantOutline : enumOutline.constants) {
                String fieldDocumentation = getDocumentation(enumConstantOutline.target.getSchemaComponent());
                if (fieldDocumentation != null)
                    fieldDocumentation = formatter.process(fieldDocumentation);
                javadocApplier.apply(enumConstantOutline.constRef.javadoc(), fieldDocumentation);
            }
        }

        return false;
    }

}