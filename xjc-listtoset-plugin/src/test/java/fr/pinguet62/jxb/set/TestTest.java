package fr.pinguet62.jxb.set;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.sun.tools.xjc.Driver;

public class TestTest {

    @Before
    public void clear() throws IOException {
        FileUtils.deleteDirectory(new File("target/fr/pinguet62"));
    }

    @Test
    public void test_processAll() throws Exception {
        Driver.run(new String[] { "src/test/resources/test.xsd", "-extension", "-XlistToSet", "-XlistToSet-processAll", "-d",
                "target" }, System.out, System.out);
    }

    @Test
    public void test_processListedInBinding() throws Exception {
        Driver.run(new String[] { "src/test/resources/test.xsd", "-b", "src/test/resources/binding.xjb", "-extension",
                "-XlistToSet", "-d", "target" }, System.out, System.out);
    }

}