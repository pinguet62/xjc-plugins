package fr.pinguet62.xjc.javadoc;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** {@code <xs:group>} */
public class GroupTest extends AbstractXjcTest {

    @Override
    protected String getXsdFile() {
        return "group.xsd";
    }

    @Test
    public void test_simpleContent() {
        assertTrue(classFieldCommentedWith("GroupUserClass", "attr", "Group.attr documentation..."));
    }

}