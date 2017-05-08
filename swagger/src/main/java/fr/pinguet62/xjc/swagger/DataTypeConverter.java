package fr.pinguet62.xjc.swagger;

import com.sun.codemodel.JFieldVar;

public class DataTypeConverter
// TODO Java 8: implements Function<JFieldVar, String>
{

    // TODO See Swagger strategy
    // TODO Java 8: @Override
    public String apply(JFieldVar fieldVar) {
        return fieldVar.type().fullName();
    }

}