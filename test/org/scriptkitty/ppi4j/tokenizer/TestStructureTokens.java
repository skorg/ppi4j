package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestStructureTokens
{
    //~ Methods

    @Test public void testStructure1()
    {
        Token token = TestCaseProvider.getToken(";");

        Assert.assertThat(token, IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", token.getContent());
    }

    @Test public void testStructure2()
    {
        Token token = TestCaseProvider.getToken("[");

        Assert.assertThat(token, IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("[", token.getContent());
    }

    @Test public void testStructure3()
    {
        Token token = TestCaseProvider.getToken("]");

        Assert.assertThat(token, IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("]", token.getContent());
    }

    @Test public void testStructure4()
    {
        Token token = TestCaseProvider.getToken("{");

        Assert.assertThat(token, IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("{", token.getContent());
    }

    @Test public void testStructure5()
    {
        Token token = TestCaseProvider.getToken("}");

        Assert.assertThat(token, IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("}", token.getContent());
    }

    @Test public void testStructure6()
    {
        Token token = TestCaseProvider.getToken("(");

        Assert.assertThat(token, IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("(", token.getContent());
    }

    @Test public void testStructure7()
    {
        Token token = TestCaseProvider.getToken(")");

        Assert.assertThat(token, IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(")", token.getContent());
    }
}
