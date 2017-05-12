package fr.pinguet62.xjc.test.transitivedependency;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import fr.pinguet62.CommentClass; // tested by compiler

public class MainITTest {

    @Test
    public void test() {
        assertNotNull(CommentClass.class);
    }

}