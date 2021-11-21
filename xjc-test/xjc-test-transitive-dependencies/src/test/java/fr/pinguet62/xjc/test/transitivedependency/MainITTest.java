package fr.pinguet62.xjc.test.transitivedependency;

import fr.pinguet62.CommentClass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MainITTest {

    @Test
    void test() {
        assertNotNull(CommentClass.class);
    }

}
