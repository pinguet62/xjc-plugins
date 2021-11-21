package fr.pinguet62.xjc.javadoc;

import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.*;
import com.sun.xml.xsom.XSComponent;
import fr.pinguet62.xjc.common.argparser.ArgumentParser;
import fr.pinguet62.xjc.common.argparser.CompositeArgumentParser;
import fr.pinguet62.xjc.common.argparser.EnumArgumentParser;
import fr.pinguet62.xjc.common.argparser.RegexArgumentParser;
import fr.pinguet62.xjc.javadoc.option.Strategy;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import java.io.IOException;

import static fr.pinguet62.xjc.common.Utils.getDocumentation;
import static fr.pinguet62.xjc.common.Utils.getMethod;
import static fr.pinguet62.xjc.common.Utils.resolveIndirectAccessToField;
import static fr.pinguet62.xjc.javadoc.option.Formatting.DEFAULT;
import static fr.pinguet62.xjc.javadoc.option.Strategy.REPLACE;

public class JavadocPlugin extends Plugin {

    private static final String PREFIX = "Xjavadoc";

    private final RegexArgumentParser optFormatting = new RegexArgumentParser("formatting", DEFAULT);

    private final EnumArgumentParser<Strategy> optStrategy = new EnumArgumentParser<>("strategy=", Strategy.class, REPLACE);

    @Override
    public String getOptionName() {
        return PREFIX;
    }

    @Override
    public String getUsage() {
        return "  -" + getOptionName() + "            : Generates javadoc, based on XSD.";
    }

    @Override
    public int parseArgument(Options opt, String[] args, int start) throws BadCommandLineException, IOException {
        ArgumentParser parser = new CompositeArgumentParser("-" + PREFIX, optStrategy, optFormatting).ignoringFirst();
        return parser.parse(args, start);
    }

    @Override
    public boolean run(Outline outline, Options options, ErrorHandler errorHandler) throws SAXException {
        // Classes
        for (ClassOutline classOutline : outline.getClasses()) {
            // Class
            String classDocumentation = getDocumentation(classOutline.target.getSchemaComponent());
            if (classDocumentation != null)
                classDocumentation = optFormatting.transform(classDocumentation);
            optStrategy.getSelected().apply(classOutline.ref.javadoc(), classDocumentation);

            // Fields
            for (FieldOutline fieldOutline : classOutline.getDeclaredFields()) {
                XSComponent schemaComponent = resolveIndirectAccessToField(fieldOutline.getPropertyInfo().getSchemaComponent());

                String propertyDocumentation = getDocumentation(schemaComponent);
                if (propertyDocumentation != null)
                    propertyDocumentation = optFormatting.transform(propertyDocumentation);

                // Field
                JFieldVar fieldVar = classOutline.implClass.fields().get(fieldOutline.getPropertyInfo().getName(false));
                optStrategy.getSelected().apply(fieldVar.javadoc(), propertyDocumentation);

                // Getter
                String getterName = "get" + fieldOutline.getPropertyInfo().getName(true);
                JMethod getter = getMethod(classOutline, getterName);
                // boolean & "is" getter prefix name
                if (getter == null && fieldOutline.getRawType().boxify().getPrimitiveType() == outline.getCodeModel().BOOLEAN) {
                    getterName = getterName.replaceFirst("^get", "is");
                    getter = getMethod(classOutline, getterName);
                }
                optStrategy.getSelected().apply(getter.javadoc(), propertyDocumentation);

                // Setter
                JMethod setter = getMethod(classOutline, "set" + fieldOutline.getPropertyInfo().getName(true));
                if (setter == null) // one-to-many has only getter
                    continue;
                optStrategy.getSelected().apply(setter.javadoc(), propertyDocumentation);
            }
        }

        // Enumerations
        for (EnumOutline enumOutline : outline.getEnums()) {
            // Class
            String classDocumentation = getDocumentation(enumOutline.target.getSchemaComponent());
            if (classDocumentation != null)
                classDocumentation = optFormatting.transform(classDocumentation);
            optStrategy.getSelected().apply(enumOutline.clazz.javadoc(), classDocumentation);

            // Values
            for (EnumConstantOutline enumConstantOutline : enumOutline.constants) {
                String fieldDocumentation = getDocumentation(enumConstantOutline.target.getSchemaComponent());
                if (fieldDocumentation != null)
                    fieldDocumentation = optFormatting.transform(fieldDocumentation);
                optStrategy.getSelected().apply(enumConstantOutline.constRef.javadoc(), fieldDocumentation);
            }
        }

        return true;
    }

}
