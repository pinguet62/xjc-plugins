package fr.pinguet62.xjc.swagger;

import com.sun.codemodel.JDefinedClass;

public enum ApiModelValueStrategy {

    /**
     * To use to evict <b>naming collision</b> if several classes have the same {@link Class#getSimpleName()}.
     * 
     * @see Class#getName()
     */
    FULL_NAME {
        @Override
        public String getMavenOption() {
            return "fullName";
        }

        @Override
        public String getValue(JDefinedClass classInfo) {
            return classInfo.fullName();
        }
    },
    /** @see Class#getSimpleName() */
    SIMPLE_NAME {
        @Override
        public String getMavenOption() {
            return "simpleName";
        }

        @Override
        public String getValue(JDefinedClass classInfo) {
            return classInfo.name();
        }
    };

    public static ApiModelValueStrategy determineStrategy(String mavenOption) {
        for (ApiModelValueStrategy strategy : values())
            if (strategy.getMavenOption().equals(mavenOption))
                return strategy;
        throw new UnsupportedOperationException("Invalid strategy: " + mavenOption);
    }

    public abstract String getValue(JDefinedClass classInfo);

    public abstract String getMavenOption();

}