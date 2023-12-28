package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.query.enums.EQueryType;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author H0n0riuss
 */
public class QueryPartTest {

    public class TestClass {
        public int attr1;
        public String attr2;
    }

    @Test
    public void queryTypeConstructorTest() {
        var source = "public void findByAttr1Like(String attr1);";
        var queryType = new QueryPart(source, TestClass.class);
        var opt = queryType.getQueryTypeStringList();
        Assert.assertNotNull(opt);
        Assert.assertTrue(opt.isPresent());
        Assert.assertEquals(2, opt.get().size());
        Assert.assertEquals("Attr1", opt.get().get(0));
        Assert.assertEquals("Like", opt.get().get(1));
    }

    @Test
    public void queryTypeConstructorTest2() {
        var source = "public void findByAttr1LikeLimit(String attr1, String attr2)";
        var queryType = new QueryPart(source, TestClass.class);
        var opt = queryType.getQueryTypeStringList();
        Assert.assertNotNull(opt);
        Assert.assertTrue(opt.isPresent());
        Assert.assertEquals(3, opt.get().size());
        Assert.assertEquals("Attr1", opt.get().get(0));
        Assert.assertEquals("Like", opt.get().get(1));
        Assert.assertEquals("Limit", opt.get().get(2));
    }

    @Test
    public void queryTypeConstructorTest3() {
        var source = "public void findByAttr1LikeOrderByAttr2(String attr1, String attr2)";
        var queryType = new QueryPart(source, TestClass.class);
        var opt = queryType.getEQueryTypeList();
        Assert.assertNotNull(opt);
        Assert.assertTrue(opt.isPresent());
        var value = opt.get();
        Assert.assertEquals(4, value.size());
        if (value.get(0) instanceof EQueryType) {
            Assert.assertEquals(EQueryType.LIKE, value.get(0));
        }
        Assert.assertEquals("Attr1", value.get(1));
    }

    @Test
    public void queryTypeConstructorTest4() {
        var source = "public void findByAttr1LikeLimit(String attr1, String attr2);";
        var queryType = new QueryPart(source, TestClass.class);
        var opt = queryType.getEQueryTypeList();
        Assert.assertNotNull(opt);
        Assert.assertTrue(opt.isPresent());

        var value = opt.get();
        Assert.assertEquals(4, value.size());
        if (value.get(0) instanceof EQueryType) {
            Assert.assertEquals(EQueryType.LIKE, value.get(0));
        }
        Assert.assertEquals("Attr1", value.get(1));
        if (value.get(0) instanceof EQueryType) {
            Assert.assertEquals(EQueryType.LIMIT, value.get(2));
        }
        Assert.assertEquals("Like", value.get(3));
    }

    @Test
    public void getColumnListTest(){
        var source = "public void findByAttr1LikeLimit(String likeIt, String limitIt);";
        var expectedList = new ArrayList<String>();
        expectedList.add("Attr1");
        var opt = new QueryPart(source, TestClass.class).getColumnList();
        Assert.assertTrue(opt.isPresent());
        var resList = opt.get();
        Assert.assertEquals(expectedList, resList);
    }
}
