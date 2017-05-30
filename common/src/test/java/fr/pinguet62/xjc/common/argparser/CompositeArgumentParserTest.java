package fr.pinguet62.xjc.common.argparser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** @see CompositeArgumentParser */
public class CompositeArgumentParserTest {

    /**
     * Process:
     * <ul>
     * <li>{@code [A=0, B=3, C=0]}</li>
     * <li>{@code [A=0, C=2]}</li>
     * <li>{@code [A=0]}</li>
     * </ul>
     *
     * @see CompositeArgumentParser#parse(String[], int)
     */
    @Test
    public void test() {
        String[] args = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

        ArgumentParser ignored = new ArgumentParser() {
            @Override
            public int parse(String[] args, int start) {
                return 0;
            }
        };
        ArgumentParser process3 = new ArgumentParser() {
            boolean processed = false;

            @Override
            public int parse(String[] args, int start) {
                if (processed)
                    throw new AssertionError("Can processed only 1");
                processed = true;
                return 3;
            }
        };
        ArgumentParser ignoreFirstThenProcess2 = new ArgumentParser() {
            int process = 0; // first is 1, second is 2, ...

            @Override
            public int parse(String[] args, int start) {
                process++;
                if (process == 1)
                    return 0;
                else if (process == 2)
                    return 2;
                else
                    throw new AssertionError("Can processed only 1");
            }
        };

        CompositeArgumentParser compositeArgumentParser = new CompositeArgumentParser(ignored, process3,
                ignoreFirstThenProcess2);
        int consumed = compositeArgumentParser.parse(args, 3/* start */);

        assertEquals(3 + 2, consumed);
    }

}