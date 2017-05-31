package fr.pinguet62.xjc.common.argparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** @see BooleanArgumentParser */
public class BooleanArgumentParserTest {

    @Test
    public void test_absent() {
        BooleanArgumentParser parser = new BooleanArgumentParser("XXX");

        int consumed = parser.parse(new String[] { "none" }, 0);

        assertEquals(0, consumed);
        assertFalse(parser.isPresent());
    }

    @Test
    public void test_present() {
        BooleanArgumentParser parser = new BooleanArgumentParser("XXX");

        int consumed = parser.parse(new String[] { "XXX" }, 0);

        assertEquals(1, consumed);
        assertTrue(parser.isPresent());
    }

}