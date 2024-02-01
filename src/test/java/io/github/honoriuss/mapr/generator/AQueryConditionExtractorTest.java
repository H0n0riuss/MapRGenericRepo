package io.github.honoriuss.mapr.generator;

import com.squareup.javapoet.ClassName;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author H0n0riuss
 */
public class AQueryConditionExtractorTest {
    public class TestClass {
        public String title;
    }
    @Test
    public void extractionTest() {
        var methodName = "LikeTitleNotLikeTitleLimit";
        var arguments = new ArrayList<>(Arrays.asList("title", "notTitle", "limit"));
        var classAttributes = new ArrayList<String>();
        classAttributes.add("title");
        var res = AQueryConditionExtractor.extractQueryCondition(methodName, arguments, classAttributes);
        Assert.assertNotNull(res);
        Assert.assertEquals(2, res.getConditionPartList().size());
        Assert.assertEquals(1, res.getQueryPartList().size());
    }
}
