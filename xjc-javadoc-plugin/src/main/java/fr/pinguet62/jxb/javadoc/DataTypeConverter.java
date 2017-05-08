package fr.pinguet62.jxb.javadoc;

import com.sun.codemodel.JFieldVar;

// TODO Java 8
public class DataTypeConverter /* implements Function<JFieldVar, String> */ {

    // tmp implementation
    // @Override
    public String apply(JFieldVar fieldVar) {
        return fieldVar.type().fullName();
    }

}