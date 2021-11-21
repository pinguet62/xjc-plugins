package fr.pinguet62.xjc.common.argparser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @see BooleanArgumentParser
 */
public class BooleanArgumentParserTest {

    @Test
    public void test_absent() {
        BooleanArgumentParser parser = new BooleanArgumentParser("XXX");

        int consumed = parser.parse(new String[]{"none"}, 0);

        assertEquals(0, consumed);
        assertFalse(parser.isPresent());
    }

    @Test
    public void test_present() {
        BooleanArgumentParser parser = new BooleanArgumentParser("XXX");

        int consumed = parser.parse(new String[]{"XXX"}, 0);

        assertEquals(1, consumed);
        assertTrue(parser.isPresent());
    }
}
