package fr.pinguet62.xjc.common.argparser;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ArgumentParser} to list multiple replace/by arguments.
 * <p>
 * Example: to replace {@code "A"} by {@code "B"} and {@code "X"} by {@code "Y"}:
 *
 * <pre>
 * -X...-regex
 *    -X...-regex-replace=A
 *         -X...-regex-by=B
 *    -X...-regex-replace=X
 *         -X...-regex-by=Y
 * </pre>
 */
public class RegexArgumentParser implements ArgumentParser {

    public static class Replacement {
        String by;

        final String replace;

        public Replacement(String replace, String by) {
            this.replace = replace;
            this.by = by;
        }
    }

    private final String argumentName;

    private final List<Replacement> defaultReplacements;

    private final List<Replacement> replacements = new ArrayList<>();

    public RegexArgumentParser(String prefix) {
        this(prefix, new ArrayList<Replacement>());
    }

    public RegexArgumentParser(String argumentName, List<Replacement> defaultReplacements) {
        this.argumentName = argumentName;
        this.defaultReplacements = defaultReplacements;
    }

    public List<Replacement> getReplacements() {
        return replacements;
    }

    // TODO secured parsing
    @Override
    public int parse(String[] args, int start) {
        final String argumentName = this.argumentName + "-regex-";

        int consumed = 0;
        String arg;
        while (start + consumed < args.length && (arg = args[start + consumed]).startsWith(argumentName)) {
            consumed++;
            arg = arg.substring(argumentName.length());
            if (arg.startsWith("replace=")) {
                arg = arg.substring("replace=".length());
                replacements.add(new Replacement(arg, null));
            } else if (arg.startsWith("by=")) {
                arg = arg.substring("by=".length());
                replacements.get(replacements.size() - 1).by = arg;
            } else
                throw new UnsupportedOperationException("Unknown suffix: " + arg);
        }
        return consumed;
    }

    /**
     * @param input The {@link String} to process.
     * @return The processed {@link String}.
     * @see String#replaceAll(String, String)
     */
    public String transform(String input) {
        List<Replacement> replacementsToApply = replacements;
        if (replacementsToApply.isEmpty())
            replacementsToApply = defaultReplacements;

        for (Replacement replacement : replacementsToApply)
            input = input.replaceAll(replacement.replace, replacement.by);
        return input;
    }

}
