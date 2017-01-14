package fr.pinguet62.jxb.javadoc;

public enum DocumentationFormatterStrategy {

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
    },
    ALL_SPACES {
        @Override
        public String getMavenOption() {
            return "allSpaces";
        }

        @Override
        public String process(String input) {
            return input.replaceAll("^\\s*", "").replaceAll("(\\s*\r?\n\\s*)", "\r\n").replaceAll("\\s*$", "");
        }
    };

    public static DocumentationFormatterStrategy determineStrategy(String mavenOption) {
        for (DocumentationFormatterStrategy strategy : values())
            if (strategy.getMavenOption().equals(mavenOption))
                return strategy;
        throw new UnsupportedOperationException("Invalid strategy: " + mavenOption);
    }

    /**
     * @param input The XSD message.<br>
     *        Cannot be {@code null}.
     */
    public abstract String process(String input);

    public abstract String getMavenOption();

}