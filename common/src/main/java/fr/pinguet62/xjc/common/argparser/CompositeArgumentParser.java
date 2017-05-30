package fr.pinguet62.xjc.common.argparser;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <b>Composite</b> pattern: {@link ArgumentParser} who contains many other {@link ArgumentParser}.
 * <p>
 * <b>Chain-of-responsibility</b> pattern: the {@link ArgumentParser} try {@link ArgumentParser#parse(String[], int) parse} the
 * arguments and continue until it can no longer, and next do the operation but with rest of previous arguments.
 */
public class CompositeArgumentParser implements ArgumentParser {

    private final ArgumentParser[] parsers;

    public CompositeArgumentParser(ArgumentParser... parsers) {
        this.parsers = parsers;
    }

    @Override
    public int parse(String[] args, int start) {
        int totalConsumed = 0;

        List<ArgumentParser> residual = new ArrayList<>(asList(parsers));
        while (true) {
            int itConsumed = 0;

            for (Iterator<ArgumentParser> it = residual.iterator(); it.hasNext();) {
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