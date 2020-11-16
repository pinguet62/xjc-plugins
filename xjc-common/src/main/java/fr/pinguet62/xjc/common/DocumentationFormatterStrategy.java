package fr.pinguet62.xjc.common;

public enum DocumentationFormatterStrategy {

    /**
     * Remove white space <i>indentation</i>.<br>
     * Add <code>&lt;br&gt;</code> at end of each line for <i>line breaking</i>.
     */
    HTML {
        @Override
        public String getMavenOption() {
            return "html";
        }

        @Override
        public String process(String documentation) {
            // indentation
            documentation = documentation.replaceFirst("^( |\t)+", "").replaceAll("\n( |\t)+", "\n");

            // first & last empty lines
            documentation = documentation.replaceFirst("^\n+", "").replaceFirst("\n+$", "");

            return documentation;
        }
    };

    public static DocumentationFormatterStrategy determineStrategy(String mavenOption) {
        for (DocumentationFormatterStrategy strategy : values())
            if (strategy.getMavenOption().equals(mavenOption))
                return strategy;
        throw new UnsupportedOperationException("Invalid strategy: " + mavenOption);
    }

    public static DocumentationFormatterStrategy getDefault() {
        return HTML;
    }

    public abstract String getMavenOption();

    /**
     * @param documentation
     *            The XSD documentation.<br>
     *            Cannot be {@code null}.
     * @return The formatted documentation.
     */
    public abstract String process(String documentation);

}
