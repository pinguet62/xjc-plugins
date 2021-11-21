package fr.pinguet62.xjc.swagger.option;

import com.sun.codemodel.JType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @see ApiModel#value()
 * @see ApiModelProperty#value()
 */
public enum DataTypeStrategy {

    /**
     * To use to evict <b>naming collision</b> if several classes have the same {@link Class#getSimpleName()}.
     *
     * @see Class#getName()
     */
    FULL_NAME {
        @Override
        public String getValue(JType classInfo) {
            return classInfo.fullName();
        }
    },
    /**
     * @see Class#getSimpleName()
     */
    SIMPLE_NAME {
        @Override
        public String getValue(JType classInfo) {
            return classInfo.name();
        }
    };

    public abstract String getValue(JType classInfo);

}
