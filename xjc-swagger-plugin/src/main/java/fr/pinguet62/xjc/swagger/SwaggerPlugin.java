package fr.pinguet62.xjc.swagger;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JFieldVar;
import com.sun.tools.xjc.BadCommandLineException;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.*;
import com.sun.xml.xsom.XSComponent;
import fr.pinguet62.xjc.common.Utils;
import fr.pinguet62.xjc.common.argparser.ArgumentParser;
import fr.pinguet62.xjc.common.argparser.CompositeArgumentParser;
import fr.pinguet62.xjc.common.argparser.EnumArgumentParser;
import fr.pinguet62.xjc.common.argparser.RegexArgumentParser;
import fr.pinguet62.xjc.swagger.option.DataTypeStrategy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import java.io.IOException;

import static fr.pinguet62.xjc.swagger.option.DataTypeStrategy.FULL_NAME;
import static fr.pinguet62.xjc.swagger.option.DescriptionFormatting.DEFAULT;

public class SwaggerPlugin extends Plugin {

    private static final String PREFIX = "Xswagger";

    private final EnumArgumentParser<DataTypeStrategy> optClassStrategy = new EnumArgumentParser<>("dataTypeStrategy=",
            DataTypeStrategy.class, FULL_NAME);

    private final RegexArgumentParser optFormatting = new RegexArgumentParser("formatting", DEFAULT);

    private String getAndFormatDocumentation(XSComponent schemaComponent) {
        String documentation = Utils.getDocumentation(schemaComponent);
        if (documentation == null)
            return null;
        return optFormatting.transform(documentation);
    }

    @Override
    public String getOptionName() {
        return PREFIX;
    }

    @Override
    public String getUsage() {
        return "  -" + getOptionName() + "            : Generates @" + ApiModel.class.getSimpleName() + " & @"
                + ApiModelProperty.class.getSimpleName() + " annotations, based on XSD.";
    }

    @Override
    public int parseArgument(Options opt, String[] args, int start) throws BadCommandLineException, IOException {
        ArgumentParser parser = new CompositeArgumentParser("-" + PREFIX, optClassStrategy, optFormatting).ignoringFirst();
        return parser.parse(args, start);
    }

    @Override
    public boolean run(Outline outline, Options options, ErrorHandler errorHandler) throws SAXException {
        for (ClassOutline classOutline : outline.getClasses()) {
            // Swagger
            JAnnotationUse apiClass = classOutline.implClass.annotate(ApiModel.class);
            Utils.setAnnotationValue(apiClass, "value", optClassStrategy.getSelected().getValue(classOutline.implClass));
            Utils.setAnnotationValue(apiClass, "description",
                    getAndFormatDocumentation(classOutline.target.getSchemaComponent()));

            for (FieldOutline fieldOutline : classOutline.getDeclaredFields()) {
                JFieldVar fieldVar = classOutline.implClass.fields().get(fieldOutline.getPropertyInfo().getName(false));
                // Swagger
                JAnnotationUse apiProperty = fieldVar.annotate(ApiModelProperty.class);
                Utils.setAnnotationValue(apiProperty, "value", getAndFormatDocumentation(
                        Utils.resolveIndirectAccessToField(fieldOutline.getPropertyInfo().getSchemaComponent())));
                Utils.setAnnotationValue(apiProperty, "required", Utils.isRequired(fieldVar));
                Utils.setAnnotationValue(apiProperty, "dataType", optClassStrategy.getSelected().getValue(fieldVar.type()));
            }
        }
        for (EnumOutline enumOutline : outline.getEnums()) {
            // Swagger
            JAnnotationUse apiClass = enumOutline.clazz.annotate(ApiModel.class);
            Utils.setAnnotationValue(apiClass, "value", optClassStrategy.getSelected().getValue(enumOutline.clazz));
            Utils.setAnnotationValue(apiClass, "description",
                    getAndFormatDocumentation(enumOutline.target.getSchemaComponent()));
            // Swagger
            for (EnumConstantOutline enumConstantOutline : enumOutline.constants) {
                JAnnotationUse apiProperty = enumConstantOutline.constRef.annotate(ApiModelProperty.class);
                Utils.setAnnotationValue(apiProperty, "value",
                        getAndFormatDocumentation(enumConstantOutline.target.getSchemaComponent()));
            }
        }

        return true;
    }

}
