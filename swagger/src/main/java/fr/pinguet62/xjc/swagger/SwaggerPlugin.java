package fr.pinguet62.xjc.swagger;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JFieldVar;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.EnumConstantOutline;
import com.sun.tools.xjc.outline.EnumOutline;
import com.sun.tools.xjc.outline.FieldOutline;
import com.sun.tools.xjc.outline.Outline;
import com.sun.xml.xsom.XSComponent;

import fr.pinguet62.xjc.common.DocumentationFormatterStrategy;
import fr.pinguet62.xjc.common.Utils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class SwaggerPlugin extends Plugin {

    private static String getAndFormatDocumentation(XSComponent schemaComponent, DocumentationFormatterStrategy formatter) {
        String documentation = Utils.getDocumentation(schemaComponent);
        if (documentation == null)
            return null;
        return formatter.process(documentation);
    }

    @Override
    public String getOptionName() {
        return "Xswagger";
    }

    @Override
    public String getUsage() {
        return "  -" + getOptionName() + "            : Generates @" + ApiModel.class.getSimpleName() + " & @"
                + ApiModelProperty.class.getSimpleName() + " annotations, based on XSD.";
    }

    @Override
    public boolean run(Outline outline, Options options, ErrorHandler errorHandler) throws SAXException {
        // TODO Maven configuration
        ApiModelValueStrategy nameStrategy = ApiModelValueStrategy.determineStrategy("fullName");
        DocumentationFormatterStrategy formatter = DocumentationFormatterStrategy.determineStrategy("allSpaces");

        for (ClassOutline classOutline : outline.getClasses()) {
            // Swagger
            JAnnotationUse apiClass = classOutline.implClass.annotate(ApiModel.class);
            Utils.setAnnotationValue(apiClass, "value", nameStrategy.getValue(classOutline.implClass));
            Utils.setAnnotationValue(apiClass, "description",
                    getAndFormatDocumentation(classOutline.target.getSchemaComponent(), formatter));

            for (FieldOutline fieldOutline : classOutline.getDeclaredFields()) {
                JFieldVar fieldVar = classOutline.implClass.fields().get(fieldOutline.getPropertyInfo().getName(false));
                // Swagger
                JAnnotationUse apiProperty = fieldVar.annotate(ApiModelProperty.class);
                Utils.setAnnotationValue(apiProperty, "value", getAndFormatDocumentation(
                        Utils.resolveIndirectAccessToField(fieldOutline.getPropertyInfo().getSchemaComponent()), formatter));
                Utils.setAnnotationValue(apiProperty, "required", Utils.isRequired(fieldVar));
                Utils.setAnnotationValue(apiProperty, "dataType", new DataTypeConverter().apply(fieldVar));
            }
        }
        for (EnumOutline enumOutline : outline.getEnums()) {
            // Swagger
            JAnnotationUse apiClass = enumOutline.clazz.annotate(ApiModel.class);
            Utils.setAnnotationValue(apiClass, "value", nameStrategy.getValue(enumOutline.clazz));
            Utils.setAnnotationValue(apiClass, "description",
                    getAndFormatDocumentation(enumOutline.target.getSchemaComponent(), formatter));
            // Swagger
            for (EnumConstantOutline enumConstantOutline : enumOutline.constants) {
                JAnnotationUse apiProperty = enumConstantOutline.constRef.annotate(ApiModelProperty.class);
                Utils.setAnnotationValue(apiProperty, "value",
                        getAndFormatDocumentation(enumConstantOutline.target.getSchemaComponent(), formatter));
            }
        }

        return true;
    }

}