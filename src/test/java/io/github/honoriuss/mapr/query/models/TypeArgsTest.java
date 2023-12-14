package io.github.honoriuss.mapr.query.models;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author H0n0riuss
 */
public class TypeArgsTest {
    @Test
    public void TypeArgConstructorTest() {
        var methodName = "public void findByText(String text);";
        var obj = new TypeArgs(methodName);
        Assert.assertNotNull(obj);
        Assert.assertTrue(obj.getTypeArgModelList().isPresent());
        Assert.assertEquals("String", obj.getTypeArgModelList().get().get(0).argType);
        Assert.assertEquals("text", obj.getTypeArgModelList().get().get(0).argName);
    }

    @Test
    public void TypeArgConstructorTest1() {
        var methodName = "public void findByText(String text, int number, TestClass clazz);";
        var obj = new TypeArgs(methodName);
        Assert.assertNotNull(obj);
        Assert.assertTrue(obj.getTypeArgModelList().isPresent());
        Assert.assertEquals("String", obj.getTypeArgModelList().get().get(0).argType);
        Assert.assertEquals("text", obj.getTypeArgModelList().get().get(0).argName);
        Assert.assertEquals("int", obj.getTypeArgModelList().get().get(1).argType);
        Assert.assertEquals("number", obj.getTypeArgModelList().get().get(1).argName);
        Assert.assertEquals("TestClass", obj.getTypeArgModelList().get().get(2).argType);
        Assert.assertEquals("clazz", obj.getTypeArgModelList().get().get(2).argName);
    }
}
