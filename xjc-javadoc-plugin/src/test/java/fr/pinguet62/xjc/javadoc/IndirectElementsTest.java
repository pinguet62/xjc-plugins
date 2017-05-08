package fr.pinguet62.xjc.javadoc;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** {@code <xs:simpleContent>} */
public class IndirectElementsTest extends AbstractXjcTest {

    @Override
    protected String getXsdFile() {
        return "IndirectElements.xsd";
    }

    @Test
    public void test_simpleContent() {
        assertTrue(classFieldCommentedWith("IndirectElementsSimpleContentClass", "attr", "Attribute documentation..."));
    }

}