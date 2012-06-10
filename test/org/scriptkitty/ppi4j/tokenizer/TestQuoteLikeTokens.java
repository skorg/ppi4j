package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.token.quotelike.QLReadlineToken;
import org.scriptkitty.ppi4j.token.quotelike.QLWordsToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;

import java.util.List;


public class TestQuoteLikeTokens
{
    //~ Methods

    @Test public void testQLReadline1()
    {
        Token[] tokens = TestCaseProvider.getTokens("<>;");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(QLReadlineToken.class));
        Assert.assertEquals("<>", tokens[0].getContent());
        Assert.assertEquals("", ((QLReadlineToken) tokens[0]).getString());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[1].getContent());
    }

    @Test public void testQLReadline2()
    {
        Token[] tokens = TestCaseProvider.getTokens("= <FOO>;");

        Assert.assertEquals(4, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("=", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(QLReadlineToken.class));
        Assert.assertEquals("<FOO>", tokens[2].getContent());
        Assert.assertEquals("FOO", ((QLReadlineToken) tokens[2]).getString());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[3].getContent());
    }

    @Test public void testQLWords1()
    {
        Token token = TestCaseProvider.getToken("qw//");

        Assert.assertThat(token, IsInstanceOf.instanceOf(QLWordsToken.class));
        Assert.assertEquals("qw//", token.getContent());

        Assert.assertEquals("", ((QLWordsToken) token).getString());
        Assert.assertTrue(((QLWordsToken) token).getLiteral().isEmpty());
    }

    @Test public void testQLWords2()
    {
        Token token = TestCaseProvider.getToken("qw/    /");

        Assert.assertThat(token, IsInstanceOf.instanceOf(QLWordsToken.class));
        Assert.assertEquals("qw/    /", token.getContent());

        Assert.assertEquals("    ", ((QLWordsToken) token).getString());
        Assert.assertTrue(((QLWordsToken) token).getLiteral().isEmpty());
    }

    @Test public void testQLWords3()
    {
        Token token = TestCaseProvider.getToken("qw/abc/");

        Assert.assertThat(token, IsInstanceOf.instanceOf(QLWordsToken.class));
        Assert.assertEquals("qw/abc/", token.getContent());

        Assert.assertEquals("abc", ((QLWordsToken) token).getString());
        Assert.assertEquals(1, ((QLWordsToken) token).getLiteral().size());
    }

    @Test public void testQLWords4()
    {
        Token token = TestCaseProvider.getToken("qw/foo bar baz/");

        Assert.assertThat(token, IsInstanceOf.instanceOf(QLWordsToken.class));
        Assert.assertEquals("qw/foo bar baz/", token.getContent());
        Assert.assertEquals("foo bar baz", ((QLWordsToken) token).getString());

        List<String> literal = ((QLWordsToken) token).getLiteral();

        Assert.assertEquals(3, literal.size());
        Assert.assertEquals("foo", literal.get(0));
        Assert.assertEquals("bar", literal.get(1));
        Assert.assertEquals("baz", literal.get(2));
    }

    @Test public void testQLWords5()
    {
        Token token = TestCaseProvider.getToken("qw/  foo bar baz  /");

        Assert.assertThat(token, IsInstanceOf.instanceOf(QLWordsToken.class));
        Assert.assertEquals("qw/  foo bar baz  /", token.getContent());
        Assert.assertEquals("  foo bar baz  ", ((QLWordsToken) token).getString());

        List<String> literal = ((QLWordsToken) token).getLiteral();

        Assert.assertEquals(3, literal.size());
        Assert.assertEquals("foo", literal.get(0));
        Assert.assertEquals("bar", literal.get(1));
        Assert.assertEquals("baz", literal.get(2));
    }

    @Test public void testQLWords6()
    {
        Token token = TestCaseProvider.getToken("qw {foo bar baz}");

        Assert.assertThat(token, IsInstanceOf.instanceOf(QLWordsToken.class));
        Assert.assertEquals("qw {foo bar baz}", token.getContent());
        Assert.assertEquals("foo bar baz", ((QLWordsToken) token).getString());

        List<String> literal = ((QLWordsToken) token).getLiteral();

        Assert.assertEquals(3, literal.size());
        Assert.assertEquals("foo", literal.get(0));
        Assert.assertEquals("bar", literal.get(1));
        Assert.assertEquals("baz", literal.get(2));
    }
}
