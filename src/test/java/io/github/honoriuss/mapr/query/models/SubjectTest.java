package io.github.honoriuss.mapr.query.models;

import org.junit.Assert;
import org.junit.Test;

public class SubjectTest {
    @Test
    public void findByTextShouldBeESubjectTypeRead() {
        String input = "findByTextLike";
        var type = Subject.ESubjectType.getSubjectType(input);
        Assert.assertNotNull(type);
        Assert.assertTrue(type.isPresent());
        var subject = type.get();
        Assert.assertEquals(subject, Subject.ESubjectType.READ);
    }

    @Test
    public void deleteShouldBeESubjectTypeDelete() {
        String input = "delete";
        var type = Subject.ESubjectType.getSubjectType(input);
        Assert.assertNotNull(type);
        Assert.assertTrue(type.isPresent());
        var subject = type.get();
        Assert.assertEquals(subject, Subject.ESubjectType.DELETE);
    }

    @Test
    public void updateMethodShouldBeESubjectTypeUpdate() {
        String input = "public void updateByText(String text);";
        var subject = new Subject(input);
        Assert.assertNotNull(subject);
        Assert.assertTrue(subject.getSubjectType().isPresent());
        Assert.assertEquals(subject.getSubjectType().get(), Subject.ESubjectType.UPDATE);
    }

    @Test
    public void createMethodShouldBeESubjectTypeUpdate() {
        String input = "String saveByText(String text);";
        var subject = new Subject(input);
        Assert.assertNotNull(subject);
        Assert.assertTrue(subject.getSubjectType().isPresent());
        Assert.assertEquals(subject.getSubjectType().get(), Subject.ESubjectType.CREATE);
    }
}
