package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.CommentToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestCommentTokens
{
    //~ Methods

    @Test public void testComment1()
    {
        Token[] tokens = TestCaseProvider.getTokens("# comment");

        Assert.assertEquals(1, tokens.length);
        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(CommentToken.class));

        Assert.assertEquals("# comment", tokens[0].getContent());

        Assert.assertFalse(((CommentToken) tokens[0]).isLineComment());
        Assert.assertTrue(((CommentToken) tokens[0]).isInlineComment());
    }

    @Test public void testComment2()
    {
        Token[] tokens = TestCaseProvider.getTokens("# comment\n");

        Assert.assertEquals(1, tokens.length);
        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(CommentToken.class));

        Assert.assertEquals("# comment\n", tokens[0].getContent());

        Assert.assertTrue(((CommentToken) tokens[0]).isLineComment());
        Assert.assertFalse(((CommentToken) tokens[0]).isInlineComment());
    }

    @Test public void testComment3()
    {
        Token[] tokens = TestCaseProvider.getTokens("print# comment");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("print", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals("# comment", tokens[1].getContent());

        Assert.assertFalse(((CommentToken) tokens[1]).isLineComment());
        Assert.assertTrue(((CommentToken) tokens[1]).isInlineComment());
    }

    @Test public void testComment4()
    {
        Token[] tokens = TestCaseProvider.getTokens("print # comment");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("print", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals("# comment", tokens[2].getContent());

        Assert.assertFalse(((CommentToken) tokens[2]).isLineComment());
        Assert.assertTrue(((CommentToken) tokens[2]).isInlineComment());
    }

    @Test public void testComment5()
    {
        Token[] tokens = TestCaseProvider.getTokens("print # comment\n");

        Assert.assertEquals(4, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("print", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals("# comment", tokens[2].getContent());

        // it's not a line comment b/c it doesn't start at the beginning of the line...
        Assert.assertFalse(((CommentToken) tokens[2]).isLineComment());
        Assert.assertTrue(((CommentToken) tokens[2]).isInlineComment());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[3].getContent());
    }
}
