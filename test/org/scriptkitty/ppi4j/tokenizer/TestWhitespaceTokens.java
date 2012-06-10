package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestWhitespaceTokens
{
    //~ Methods

    @Test public void testWhitespace1()
    {
        Token token = TestCaseProvider.getToken(" ");

        Assert.assertThat(token, IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", token.getContent());
    }

    @Test public void testWhitespace2()
    {
        Token[] tokens = TestCaseProvider.getTokens("a  b");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("a", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("  ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("b", tokens[2].getContent());
    }
}
