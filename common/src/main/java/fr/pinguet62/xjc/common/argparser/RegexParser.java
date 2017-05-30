package fr.pinguet62.xjc.common.argparser;

import java.util.ArrayList;
import java.util.List;

/**
 * Multiple replace/by arguments.
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
public class RegexParser implements ArgumentParser {

    public static class Replacement {
        String by;

        final String replace;

        public Replacement(String replace, String by) {
            this.replace = replace;
            this.by = by;
        }
    }

    private final List<Replacement> defaultReplacements;

    private final String prefix;

    private final List<Replacement> replacements = new ArrayList<>();

    public RegexParser(String prefix) {
        this(prefix, new ArrayList<Replacement>());
    }

    public RegexParser(String prefix, List<Replacement> defaultReplacements) {
        this.prefix = prefix;
        this.defaultReplacements = defaultReplacements;
    }

    public List<Replacement> getReplacements() {
        return replacements;
    }

    // TODO secured parsing
    @Override
    public int parse(String[] args, int start) {
        final String prefix = this.prefix + "-regex-";
        int consumed = 0;
        String arg;
        while (start + consumed < args.length & (arg = args[start + consumed]).startsWith(prefix)) {
            consumed++;
            arg = arg.substring(prefix.length());
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
     * @param input
     *            The {@link String} to process.
     * @return The new processed {@link String}.
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