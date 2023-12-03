package io.github.honoriuss.mapr.query.models;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author H0n0riuss
 */
public class QueryTypeTest {

    public class TestClass {
        public int attr1;
        public String attr2;
    }

    @Test
    public void queryTypeConstructorTest(){
        var source = "public void findByAttr1Like";
        var queryType = new QueryType(source, TestClass.class);
        var opt = queryType.getEQueryTypeList();
        Assert.assertNotNull(opt);
        Assert.assertTrue(opt.isPresent());
        Assert.assertEquals(2, opt.get().size());
        Assert.assertEquals("Attr1", opt.get().get(0));
        Assert.assertEquals("Like", opt.get().get(1));
    }

    @Test
    public void queryTypeConstructorTest2(){
        var source = "public void findByAttr1LikeOrderByAttr2";
        var queryType = new QueryType(source, TestClass.class);
        var opt = queryType.getEQueryTypeList();
        Assert.assertNotNull(opt);
        Assert.assertTrue(opt.isPresent());
        Assert.assertEquals(4, opt.get().size());
        Assert.assertEquals("Attr1", opt.get().get(0));
        Assert.assertEquals("Like", opt.get().get(1));
        Assert.assertEquals("OrderBy", opt.get().get(2));
        Assert.assertEquals("Attr2", opt.get().get(3));
    }
}
