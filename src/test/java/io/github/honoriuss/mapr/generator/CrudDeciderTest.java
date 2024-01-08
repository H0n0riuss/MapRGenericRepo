package io.github.honoriuss.mapr.generator;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author H0n0riuss
 */
public class CrudDeciderTest {
    @Test
    public void decideCreate() {
        var methodName = "create";
        var obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("insert", obj);

        methodName = "insert";
        obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("insert", obj);
    }

    @Test
    public void decideCreateWithMore() {
        var methodName = "createBy";
        var obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("insert", obj);

        methodName = "insertBy";
        obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("insert", obj);
    }

    @Test
    public void decideRead() {
        var methodName = "find";
        var obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("find", obj);

        methodName = "get";
        obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("find", obj);

        methodName = "read";
        obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("find", obj);
    }

    @Test
    public void decideFindWithMore() {
        var methodName = "findBy";
        var obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("find", obj);

        methodName = "getBy";
        obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("find", obj);

        methodName = "readBy";
        obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("find", obj);
    }

    @Test
    public void decideUpdate() {
        var methodName = "replace";
        var obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("replace", obj);

        methodName = "update";
        obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("replace", obj);
    }

    @Test
    public void decideUpdateWithMore() {
        var methodName = "replaceBy";
        var obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("replace", obj);

        methodName = "updateBy";
        obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("replace", obj);
    }

    @Test
    public void decideDelete() {
        var methodName = "delete";
        var obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("delete", obj);

        methodName = "remove";
        obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("delete", obj);
    }

    @Test
    public void decideDeleteWithMore() {
        var methodName = "deleteBy";
        var obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("delete", obj);

        methodName = "removeBy";
        obj = CrudDecider.getCrudTranslation(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals("delete", obj);
    }

    @Test
    public void getCrudTypeTest(){
        var methodName = "readBy";
        var obj = CrudDecider.getCrudType(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals(ECrudType.READ, obj);

        methodName = "insertBy";
        obj = CrudDecider.getCrudType(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals(ECrudType.CREATE, obj);

        methodName = "deleteBy";
        obj = CrudDecider.getCrudType(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals(ECrudType.DELETE, obj);

        methodName = "updateBy";
        obj = CrudDecider.getCrudType(methodName);
        Assert.assertNotNull(obj);
        Assert.assertEquals(ECrudType.UPDATE, obj);
    }
}
