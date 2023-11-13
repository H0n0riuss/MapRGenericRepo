package io.github.honoriuss.mapr.query.parser;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * @author H0n0riuss
 */
public class PartTreeTest {

    @Test
    public void testSplitByKeywords() {
        String input = "Keyword1Text1Keyword2Text2Keyword3Text3";
        /*var result = PartTree.splitByKeywords(input, "Keyword1", "Keyword2", "Keyword3");

        assertEquals(3, result.size());
        assertEquals("Keyword1Text1", result.get(0));
        assertEquals("Keyword2Text2", result.get(1));
        assertEquals("Keyword3Text3", result.get(2));*/
    }
}
