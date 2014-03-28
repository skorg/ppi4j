package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.PrototypeToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestPrototypeTokens
{
    //~ Methods

    @Test public void testPrototype1()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub(");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(PrototypeToken.class));
        Assert.assertEquals("(", tokens[1].getContent());
    }

    @Test public void testPrototype2()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub($");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(PrototypeToken.class));
        Assert.assertEquals("($", tokens[1].getContent());
    }

    @Test public void testPrototype3()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub($)");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(PrototypeToken.class));
        Assert.assertEquals("($)", tokens[1].getContent());
    }

    @Test public void testPrototype4()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub($);");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(PrototypeToken.class));
        Assert.assertEquals("($)", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[2].getContent());
    }

    @Test public void testPrototype5()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub ($);");

        Assert.assertEquals(4, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(PrototypeToken.class));
        Assert.assertEquals("($)", tokens[2].getContent());
        Assert.assertEquals("$", ((PrototypeToken) tokens[2]).getPrototype());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[3].getContent());
    }

    @Test public void testPrototype6()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub foo($);");

        Assert.assertEquals(5, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("foo", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(PrototypeToken.class));
        Assert.assertEquals("($)", tokens[3].getContent());

        Assert.assertThat(tokens[4], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[4].getContent());
    }
}
