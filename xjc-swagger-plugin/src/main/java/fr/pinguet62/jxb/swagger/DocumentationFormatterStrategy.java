package fr.pinguet62.jxb.swagger;

public enum DocumentationFormatterStrategy {

    ALL_SPACES {
        @Override
        public String getMavenOption() {
            return "allSpaces";
        }

        @Override
        public String process(String input) {
            return input.replaceAll("^\\s*", "").replaceAll("(\\s*\r?\n\\s*)", "\r\n").replaceAll("\\s*$", "");
        }
    },
    /** @see String#trim() */
    TRIM {
        @Override
        public String getMavenOption() {
            return "trim";
        }

        @Override
        public String process(String input) {
            return input.trim();
        }
    };

    public static DocumentationFormatterStrategy determineStrategy(String mavenOption) {
        for (DocumentationFormatterStrategy strategy : values())
            if (strategy.getMavenOption().equals(mavenOption))
                return strategy;
        throw new UnsupportedOperationException("Invalid strategy: " + mavenOption);
    }

    public abstract String getMavenOption();

    /**
     * @param input The XSD message.<br>
     *        Cannot be {@code null}.
     * @return The formatted documentation.
     */
    public abstract String process(String input);

}