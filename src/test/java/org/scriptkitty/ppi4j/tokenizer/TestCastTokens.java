package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.CastToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.SymbolToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestCastTokens
{
    //~ Methods

    @Test public void testCast1()
    {
        Token token = TestCaseProvider.getToken("@");

        Assert.assertThat(token, IsInstanceOf.instanceOf(CastToken.class));
        Assert.assertEquals("@", token.getContent());
    }

    @Test public void testCast2()
    {
        Token[] tokens = TestCaseProvider.getTokens("@$");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(CastToken.class));
        Assert.assertEquals("@", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(CastToken.class));
        Assert.assertEquals("$", tokens[1].getContent());
    }

    @Test public void testCast3()
    {
        Token[] tokens = TestCaseProvider.getTokens("@$foo");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(CastToken.class));
        Assert.assertEquals("@", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("$foo", tokens[1].getContent());
    }

    @Test public void testCast4()
    {
        Token[] tokens = TestCaseProvider.getTokens("@{$foo}");

        Assert.assertEquals(4, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(CastToken.class));
        Assert.assertEquals("@", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("{", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("$foo", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("}", tokens[3].getContent());
    }

    @Test public void testCast5()
    {
        Token[] tokens = TestCaseProvider.getTokens("$#{");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(CastToken.class));
        Assert.assertEquals("$#", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("{", tokens[1].getContent());
    }

    @Test public void testCast6()
    {
        Token[] tokens = TestCaseProvider.getTokens("$$foo");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(CastToken.class));
        Assert.assertEquals("$", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("$foo", tokens[1].getContent());
    }
}
