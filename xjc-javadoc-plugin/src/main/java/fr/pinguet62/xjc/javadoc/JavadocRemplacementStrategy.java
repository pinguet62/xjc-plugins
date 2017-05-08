package fr.pinguet62.xjc.javadoc;

import com.sun.codemodel.JDocComment;

public enum JavadocRemplacementStrategy {

    /** @see String#trim() */
    APPEND_BEGIN {
        @Override
        public void apply(JDocComment javadoc, String documentation) {
            javadoc.append(documentation);
        }

        @Override
        public String getMavenOption() {
            return "appendBegin";
        }
    },
    REPLACE {
        @Override
        public void apply(JDocComment javadoc, String documentation) {
            javadoc.clear();
            javadoc.append(documentation);
        }

        @Override
        public String getMavenOption() {
            return "replace";
        }
    };

    public static JavadocRemplacementStrategy determineStrategy(String mavenOption) {
        for (JavadocRemplacementStrategy strategy : values())
            if (strategy.getMavenOption().equals(mavenOption))
                return strategy;
        throw new UnsupportedOperationException("Invalid strategy: " + mavenOption);
    }

    /**
     * Apply the javadoc on element.
     *
     * @param javadoc The {@link JDocComment} to modify.
     * @param documentation The javadoc of component (class, field, method, ...).
     */
    public abstract void apply(JDocComment javadoc, String documentation);

    public abstract String getMavenOption();

}