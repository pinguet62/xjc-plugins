package fr.pinguet62.jxb.javadoc;

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

public class Application extends Plugin {

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
            String classDocumentation = Utils.getDocumentation(classOutline.target.getSchemaComponent());
            if (classDocumentation != null)
                classDocumentation = formatter.process(classDocumentation);
            javadocApplier.apply(classOutline.ref.javadoc(), classDocumentation);

            // Fields
            for (FieldOutline fieldOutline : classOutline.getDeclaredFields()) {
                String propertyDocumentation = Utils.getDocumentation(
                        Utils.resolveIndirectAccessToField(fieldOutline.getPropertyInfo().getSchemaComponent()));
                if (propertyDocumentation != null)
                    propertyDocumentation = formatter.process(propertyDocumentation);

                // Field
                JFieldVar fieldVar = classOutline.implClass.fields().get(fieldOutline.getPropertyInfo().getName(false));
                javadocApplier.apply(fieldVar.javadoc(), propertyDocumentation);

                // Getter
                boolean primitiveBoolean = (fieldOutline.getRawType().isPrimitive()
                        && fieldOutline.getRawType().boxify().getPrimitiveType() == outline.getCodeModel().BOOLEAN);
                String getterName = (primitiveBoolean ? "is" : "get") + fieldOutline.getPropertyInfo().getName(true);
                JMethod getter = Utils.getMethod(classOutline, getterName);
                javadocApplier.apply(getter.javadoc(), propertyDocumentation);

                // Setter
                JMethod setter = Utils.getMethod(classOutline, "set" + fieldOutline.getPropertyInfo().getName(true));
                javadocApplier.apply(setter.javadoc(), propertyDocumentation);
            }
        }

        // Enumerations
        for (EnumOutline enumOutline : outline.getEnums()) {
            // Class
            String classDocumentation = Utils.getDocumentation(enumOutline.target.getSchemaComponent());
            if (classDocumentation != null)
                classDocumentation = formatter.process(classDocumentation);
            javadocApplier.apply(enumOutline.clazz.javadoc(), classDocumentation);

            // Values
            for (EnumConstantOutline enumConstantOutline : enumOutline.constants) {
                String fieldDocumentation = Utils.getDocumentation(enumConstantOutline.target.getSchemaComponent());
                if (fieldDocumentation != null)
                    fieldDocumentation = formatter.process(fieldDocumentation);
                javadocApplier.apply(enumConstantOutline.constRef.javadoc(), fieldDocumentation);
            }
        }

        return false;
    }

}