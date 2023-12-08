package io.github.honoriuss.mapr.query.models;

import org.junit.Assert;
import org.junit.Test;

public class OrderByTest {
    @Test
    public void OrderByConstructorTest() {
        var methodName = "public void findByTitleOrderByTitle(String title)";
        var obj = new OrderBy(methodName);
        Assert.assertNotNull(obj);
        var isAsc = obj.isAsc();
        Assert.assertFalse(isAsc);
        Assert.assertTrue(obj.getAttributesToOrderBy().isPresent());
        Assert.assertEquals(1, obj.getAttributesToOrderBy().get().size());
        Assert.assertEquals("Title", obj.getAttributesToOrderBy().get().get(0));
    }

    @Test
    public void OrderByConstructorTest1() {
        var methodName = "public void findByTitleOrderByTitleDESC(String title)";
        var obj = new OrderBy(methodName);
        Assert.assertNotNull(obj);
        var isAsc = obj.isAsc();
        Assert.assertFalse(isAsc);
        Assert.assertTrue(obj.getAttributesToOrderBy().isPresent());
        Assert.assertEquals(1, obj.getAttributesToOrderBy().get().size());
        Assert.assertEquals("Title", obj.getAttributesToOrderBy().get().get(0));

        methodName = "public void findByTitleOrderByTitleDesc(String title)";
        obj = new OrderBy(methodName);
        Assert.assertNotNull(obj);
        isAsc = obj.isAsc();
        Assert.assertFalse(isAsc);
        Assert.assertTrue(obj.getAttributesToOrderBy().isPresent());
        Assert.assertEquals(1, obj.getAttributesToOrderBy().get().size());
        Assert.assertEquals("Title", obj.getAttributesToOrderBy().get().get(0));
    }
    @Test
    public void OrderByConstructorTest2() {
        var methodName = "public void findByTitleOrderByTitleAsc(String title)";
        var obj = new OrderBy(methodName);
        Assert.assertNotNull(obj);
        var isAsc = obj.isAsc();
        Assert.assertTrue(isAsc);
        Assert.assertTrue(obj.getAttributesToOrderBy().isPresent());
        Assert.assertEquals(1, obj.getAttributesToOrderBy().get().size());
        Assert.assertEquals("Title", obj.getAttributesToOrderBy().get().get(0));
    }
    @Test
    public void OrderByConstructorTest3() {
        var methodName = "public void findByTitleOrderByTitleAuthorAsc(String title, String author)";
        var obj = new OrderBy(methodName);
        Assert.assertNotNull(obj);
        var isAsc = obj.isAsc();
        Assert.assertTrue(isAsc);
        Assert.assertTrue(obj.getAttributesToOrderBy().isPresent());
        Assert.assertEquals(2, obj.getAttributesToOrderBy().get().size());
        Assert.assertEquals("Title", obj.getAttributesToOrderBy().get().get(0));
        Assert.assertEquals("Author", obj.getAttributesToOrderBy().get().get(1));
    }
}
