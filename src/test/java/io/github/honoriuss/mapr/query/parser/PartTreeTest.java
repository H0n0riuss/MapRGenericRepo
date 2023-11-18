package io.github.honoriuss.mapr.query.parser;

import io.github.honoriuss.mapr.repositories.entities.AEntity;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

/**
 * @author H0n0riuss
 */
public class PartTreeTest {

    public class TestClazz extends AEntity {
        public String text;
    }
    @Test
    public void testSplitByKeywords() {
        String input = "findByTextLike";
        var result = new PartTree(input, TestClazz.class);
        assertNotNull(result);
/*
        assertEquals(3, result.size());
        assertEquals("Keyword1Text1", result.get(0));
        assertEquals("Keyword2Text2", result.get(1));
        assertEquals("Keyword3Text3", result.get(2));*/
    }
}
