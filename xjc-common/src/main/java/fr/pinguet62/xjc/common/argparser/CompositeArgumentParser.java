package fr.pinguet62.xjc.common.argparser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * <ul>
 * <li><b>Composite</b> pattern: {@link ArgumentParser} who contains many other {@link ArgumentParser}.</li>
 * <li><b>Chain-of-responsibility</b> pattern: the {@link ArgumentParser} try {@link ArgumentParser#parse(String[], int) parse}
 * the arguments and continue until it can no longer, and next do the operation but with rest of previous arguments.</li>
 * </ul>
 * Example: for {@code argumentName="aaa"}, sub-{@link ArgumentParser} with {@code argumentName="iii"}, it will have the
 * resultant {@code argumentName="-Xaaa-iii"}.
 */
public class CompositeArgumentParser implements ArgumentParser {

    private final String argumentName;

    private final List<ArgumentParser> parsers;

    /**
     * @param argumentName The base argument name.
     * @param parsers      The other {@link ArgumentParser}.<br>
     *                     Their name {@code argumentName} is added to this {@code argumentName}.
     */
    public CompositeArgumentParser(String argumentName, ArgumentParser... parsers) {
        this.argumentName = argumentName;
        this.parsers = new ArrayList<>(asList(parsers));
    }

    /**
     * Filter values, after {@code start}, {@link String#startsWith(String) starting with} {@link #argumentName}, and remove
     * this {@link #argumentName}.
     * <p>
     * Example, for {@link #argumentName}={@code "X"} and input={@code ["a", "b", "X-1", "X-2", "c", "d"]}, the output will be
     * {@code [null, null, "1", "2"]}.
     *
     * @return A new argument array.
     */
    private String[] filterAndRemovePrefix(String[] args, int start) {
        List<String> updated = new ArrayList<>();
        for (int i = 0; i < start; i++)
            updated.add(null);
        for (int i = start; i < args.length; i++) {
            String arg = args[i];

            // ignore first
            if (arg.equals(argumentName) && parsers.size() > 0 && parsers.get(0) instanceof SkipArgumentParser)
                updated.add(arg);
                // end
            else if (!arg.startsWith(argumentName + "-"))
                break;
            else
                updated.add(arg.substring(argumentName.length() + 1));
        }
        return updated.toArray(new String[updated.size()]);
    }

    /**
     * Because for composed arguments, the first is just the trigger for others.
     *
     * @return {@code this}
     */
    public CompositeArgumentParser ignoringFirst() {
        parsers.add(0, new SkipArgumentParser(argumentName));
        return this;
    }

    @Override
    public int parse(String[] args, int start) {
        int totalConsumed = 0;

        args = filterAndRemovePrefix(args, start);
        List<ArgumentParser> residual = new ArrayList<>(parsers); // copy
        while (true && start + totalConsumed < args.length) {
            int itConsumed = 0;

            for (Iterator<ArgumentParser> it = residual.iterator(); it.hasNext()
                    && start + itConsumed + totalConsumed < args.length; ) {
                ArgumentParser parser = it.next();
                int consumed = parser.parse(args, start + itConsumed + totalConsumed);
                if (consumed != 0) {
                    itConsumed += consumed;
                    it.remove();
                }
            }

            // no more to process: end
            if (itConsumed == 0)
                break;
            totalConsumed += itConsumed;
        }

        return totalConsumed;
    }

}
