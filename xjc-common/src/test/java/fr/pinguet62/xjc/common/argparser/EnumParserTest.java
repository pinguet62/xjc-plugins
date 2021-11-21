package fr.pinguet62.xjc.common.argparser;

import org.junit.jupiter.api.Test;

import static fr.pinguet62.xjc.common.argparser.EnumParserTest.Param.FIRST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** @see EnumArgumentParser */
class EnumParserTest {

    static enum Param {
        FIRST, SECOND;
    }

    static final String PREFIX = "-Xenum=";

    @Test
    void test_parse() {
        EnumArgumentParser<Param> parser = new EnumArgumentParser<>(PREFIX, Param.class);

        int consumed = parser.parse(new String[] { PREFIX + "FIRST" }, 0);
        assertEquals(1, consumed);
        assertEquals(FIRST, parser.getSelected());
    }

    @Test
    void test_parse_default() {
        assertEquals(FIRST, new EnumArgumentParser<>("", Param.class, FIRST).getSelected());
        assertNull(new EnumArgumentParser<>("", Param.class).getSelected());
    }

    @Test
    void test_parse_unknown() {
        assertThrows(IllegalArgumentException.class, () ->
                new EnumArgumentParser<>(PREFIX, Param.class).parse(new String[]{PREFIX + "UNKNOWN"}, 0));
    }

}
