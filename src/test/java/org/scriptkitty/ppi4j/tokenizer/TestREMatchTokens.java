package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.regexp.REMatchToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestREMatchTokens
{
    //~ Methods

    @Test public void testMatch1()
    {
        Token token = TestCaseProvider.getToken("m");

        Assert.assertThat(token, IsInstanceOf.instanceOf(REMatchToken.class));
        Assert.assertEquals("m", token.getContent());

        Assert.assertEquals(null, ((REMatchToken) token).getMatch());
        Assert.assertFalse(((REMatchToken) token).isMatchComplete());

        Assert.assertFalse(((REMatchToken) token).supportsSubstitution());
        Assert.assertNull(((REMatchToken) token).getSubstitution());
    }

    @Test public void testMatch2()
    {
        // you'd think by itself it would be an operator, but it's not :)
        Token token = TestCaseProvider.getToken("/");

        Assert.assertThat(token, IsInstanceOf.instanceOf(REMatchToken.class));
        Assert.assertEquals("/", token.getContent());

        Assert.assertNull(((REMatchToken) token).getMatch());
        Assert.assertFalse(((REMatchToken) token).isMatchComplete());

        Assert.assertFalse(((REMatchToken) token).supportsSubstitution());
        Assert.assertNull(((REMatchToken) token).getSubstitution());
    }

    @Test public void testMatch3()
    {
        // you'd think by itself it would be an operator, but it's not :)
        Token token = TestCaseProvider.getToken("/=");

        Assert.assertThat(token, IsInstanceOf.instanceOf(REMatchToken.class));
        Assert.assertEquals("/=", token.getContent());

        Assert.assertNull(((REMatchToken) token).getMatch());
        Assert.assertFalse(((REMatchToken) token).isMatchComplete());

        Assert.assertFalse(((REMatchToken) token).supportsSubstitution());
        Assert.assertNull(((REMatchToken) token).getSubstitution());
    }

    @Test public void testMatch4()
    {
        // you'd think by itself it would be an operator, but it's not :)
        Token[] tokens = TestCaseProvider.getTokens("//;");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(REMatchToken.class));
        Assert.assertEquals("//", tokens[0].getContent());

        Assert.assertEquals("", ((REMatchToken) tokens[0]).getMatch());
        Assert.assertTrue(((REMatchToken) tokens[0]).isMatchComplete());

        Assert.assertFalse(((REMatchToken) tokens[0]).supportsSubstitution());
        Assert.assertNull(((REMatchToken) tokens[0]).getSubstitution());
    }

    @Test public void testMatch5()
    {
        Token[] tokens = TestCaseProvider.getTokens("//=;");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(REMatchToken.class));
        Assert.assertEquals("//", tokens[0].getContent());

        Assert.assertEquals("", ((REMatchToken) tokens[0]).getMatch());
        Assert.assertTrue(((REMatchToken) tokens[0]).isMatchComplete());

        Assert.assertFalse(((REMatchToken) tokens[0]).supportsSubstitution());
        Assert.assertNull(((REMatchToken) tokens[0]).getSubstitution());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("=", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[2].getContent());
    }

    @Test public void testMatch6()
    {
        Token[] tokens = TestCaseProvider.getTokens("m/test/msixpgcadlu;");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(REMatchToken.class));
        Assert.assertEquals("m/test/msixpgcadlu", tokens[0].getContent());

        Assert.assertEquals("test", ((REMatchToken) tokens[0]).getMatch());
        Assert.assertTrue(((REMatchToken) tokens[0]).isMatchComplete());

        Assert.assertFalse(((REMatchToken) tokens[0]).supportsSubstitution());
        Assert.assertNull(((REMatchToken) tokens[0]).getSubstitution());

        Assert.assertTrue(((REMatchToken) tokens[0]).hasModifiers());
        Assert.assertEquals(11, ((REMatchToken) tokens[0]).getModifiers().size());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[1].getContent());
    }

    @Test public void testMatch7()
    {
        Token[] tokens = TestCaseProvider.getTokens("m [te#st]msixpgcadlu;");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(REMatchToken.class));
        Assert.assertEquals("m [te#st]msixpgcadlu", tokens[0].getContent());

        Assert.assertEquals("te#st", ((REMatchToken) tokens[0]).getMatch());
        Assert.assertTrue(((REMatchToken) tokens[0]).isMatchComplete());

        Assert.assertFalse(((REMatchToken) tokens[0]).supportsSubstitution());
        Assert.assertNull(((REMatchToken) tokens[0]).getSubstitution());

        Assert.assertTrue(((REMatchToken) tokens[0]).hasModifiers());
        Assert.assertEquals(11, ((REMatchToken) tokens[0]).getModifiers().size());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[1].getContent());
    }

    @Test public void testMatch8()
    {
        Token[] tokens = TestCaseProvider.getTokens("/a/;");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(REMatchToken.class));
        Assert.assertEquals("/a/", tokens[0].getContent());

        Assert.assertEquals("a", ((REMatchToken) tokens[0]).getMatch());
        Assert.assertTrue(((REMatchToken) tokens[0]).isMatchComplete());

        Assert.assertFalse(((REMatchToken) tokens[0]).supportsSubstitution());
        Assert.assertNull(((REMatchToken) tokens[0]).getSubstitution());

        Assert.assertFalse(((REMatchToken) tokens[0]).hasModifiers());
        Assert.assertEquals(0, ((REMatchToken) tokens[0]).getModifiers().size());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[1].getContent());
    }

    @Test public void testMatch9()
    {
        Token[] tokens = TestCaseProvider.getTokens("(/:\\s*$/);");

        Assert.assertEquals(4, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("(", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(REMatchToken.class));
        Assert.assertEquals("/:\\s*$/", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(")", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[3].getContent());
    }
}
