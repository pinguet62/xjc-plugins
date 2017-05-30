package fr.pinguet62.xjc.common.argparser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import fr.pinguet62.xjc.common.argparser.RegexParser.Replacement;

/** @see RegexParser */
public class RegexParserTest {

    private static final String PREFIX = "-Xfoo";

    @Test
    public void test_parse() {
        String[] args = { "*", "*", PREFIX + "-regex-replace=foo", PREFIX + "-regex-by=bar", PREFIX + "-regex-replace=first",
                PREFIX + "-regex-by=second", "*" };
        RegexParser parser = new RegexParser(PREFIX);
        int consumed = parser.parse(args, 2);
        assertEquals(4, consumed);
        assertEquals("foo", parser.getReplacements().get(0).replace);
        assertEquals("bar", parser.getReplacements().get(0).by);
        assertEquals("first", parser.getReplacements().get(1).replace);
        assertEquals("second", parser.getReplacements().get(1).by);
    }

    @Test
    public void test_transform() {
        RegexParser parser = new RegexParser(PREFIX);
        parser.getReplacements().clear();
        parser.getReplacements().add(new Replacement("\r?\n", "<br>"));
        parser.getReplacements().add(new Replacement("\\* ", "<li>"));
        assertEquals("<li>entry1<br><li>entry2", parser.transform("* entry1\r\n* entry2"));
    }

}