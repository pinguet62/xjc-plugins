package fr.pinguet62.xjc.common.argparser;

import fr.pinguet62.xjc.common.argparser.RegexArgumentParser.Replacement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegexParserTest {

    static final String PREFIX = "-Xfoo";

    @Test
    void test_parse() {
        String[] args = {"*", "*", PREFIX + "-regex-replace=foo", PREFIX + "-regex-by=bar", PREFIX + "-regex-replace=first",
                PREFIX + "-regex-by=second", "*"};
        RegexArgumentParser parser = new RegexArgumentParser(PREFIX);
        int consumed = parser.parse(args, 2);
        assertEquals(4, consumed);
        assertEquals("foo", parser.getReplacements().get(0).replace);
        assertEquals("bar", parser.getReplacements().get(0).by);
        assertEquals("first", parser.getReplacements().get(1).replace);
        assertEquals("second", parser.getReplacements().get(1).by);
    }

    @Test
    void test_transform() {
        RegexArgumentParser parser = new RegexArgumentParser(PREFIX);
        parser.getReplacements().clear();
        parser.getReplacements().add(new Replacement("\r?\n", "<br>"));
        parser.getReplacements().add(new Replacement("\\* ", "<li>"));
        assertEquals("<li>entry1<br><li>entry2", parser.transform("* entry1\r\n* entry2"));
    }
}
