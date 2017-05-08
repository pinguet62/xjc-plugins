package fr.pinguet62.xjc.listtoset;

import com.sun.codemodel.JMethod;
import com.sun.tools.xjc.outline.ClassOutline;

public final class Utils {

    public static JMethod getMethod(ClassOutline classOutline, String methodName) {
        for (JMethod method : classOutline.implClass.methods())
            if (method.name().equals(methodName))
                return method;
        return null;
    }

    // util
    private Utils() {
    }

}