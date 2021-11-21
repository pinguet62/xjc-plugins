package fr.pinguet62.xjc.common.argparser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        String[] args = { "X-0", "X-1", "X-2", "X-3", "X-4", "X-5", "X-6", "X-7", "X-8", "X-9" };

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

        CompositeArgumentParser compositeArgumentParser = new CompositeArgumentParser("X", ignored, process3,
                ignoreFirstThenProcess2);
        int consumed = compositeArgumentParser.parse(args, 3/* start */);

        assertEquals(3 + 2, consumed);
    }

    @Test
    public void test_argumentAndPrefix() {
        CompositeArgumentParser parser = new CompositeArgumentParser("Xxxx", new BooleanArgumentParser("aaa"),
                new BooleanArgumentParser("bbb"));
        int consumed = parser.parse(new String[] { "Xxxx-aaa", "Xxxx-bbb" }, 0);
        assertEquals(2, consumed);
    }

    @Test
    public void test_ignoreFirst() {
        CompositeArgumentParser parser = new CompositeArgumentParser("Xxxx", new BooleanArgumentParser("aaa"),
                new BooleanArgumentParser("bbb")).ignoringFirst();
        int consumed = parser.parse(new String[] { "Xxxx", "Xxxx-aaa", "Xxxx-bbb" }, 0);
        assertEquals(3, consumed);
    }

}