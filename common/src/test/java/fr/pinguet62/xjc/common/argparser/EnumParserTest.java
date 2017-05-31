package fr.pinguet62.xjc.common.argparser;

import static fr.pinguet62.xjc.common.argparser.EnumParserTest.Param.FIRST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/** @see EnumArgumentParser */
public class EnumParserTest {

    static enum Param {
        FIRST, SECOND;
    }

    private static final String PREFIX = "-Xenum=";

    @Test
    public void test_parse() {
        EnumArgumentParser<Param> parser = new EnumArgumentParser<>(PREFIX, Param.class);

        int consumed = parser.parse(new String[] { PREFIX + "FIRST" }, 0);
        assertEquals(1, consumed);
        assertEquals(FIRST, parser.getSelected());
    }

    @Test
    public void test_parse_default() {
        assertEquals(FIRST, new EnumArgumentParser<>("", Param.class, FIRST).getSelected());
        assertNull(new EnumArgumentParser<>("", Param.class).getSelected());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_parse_unknown() {
        new EnumArgumentParser<>(PREFIX, Param.class).parse(new String[] { PREFIX + "UNKNOWN" }, 0);
    }

}