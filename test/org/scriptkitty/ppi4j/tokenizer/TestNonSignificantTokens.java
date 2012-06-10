package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.CommentToken;
import org.scriptkitty.ppi4j.token.PodToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestNonSignificantTokens
{
    //~ Methods

    @Test public void testComment1()
    {
        Token token = TestCaseProvider.getToken("#");

        Assert.assertThat(token, IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals("#", token.getContent());
    }

    @Test public void testComment2()
    {
        Token token = TestCaseProvider.getToken("# this is a comment");

        Assert.assertThat(token, IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals("# this is a comment", token.getContent());
    }

    @Test public void testComment3()
    {
        Token token = TestCaseProvider.getToken("# this is a comment\n");

        Assert.assertThat(token, IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals("# this is a comment\n", token.getContent());
    }

    @Test public void testComment4()
    {
        Token token = TestCaseProvider.getToken(" # this is a comment\n");

        Assert.assertThat(token, IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals(" # this is a comment\n", token.getContent());
    }

    @Test public void testComment5()
    {
        Token[] tokens = TestCaseProvider.getTokens("# this is a comment\n\n");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals("# this is a comment\n", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[1].getContent());
    }

    @Test public void testPod1()
    {
        Token token = TestCaseProvider.getToken("=pod");

        Assert.assertThat(token, IsInstanceOf.instanceOf(PodToken.class));
        Assert.assertEquals("=pod", token.getContent());
    }

    @Test public void testPod2()
    {
        Token token = TestCaseProvider.getToken("=pod\n");

        Assert.assertThat(token, IsInstanceOf.instanceOf(PodToken.class));
        Assert.assertEquals("=pod\n", token.getContent());
    }

    @Test public void testPod3()
    {
        Token token = TestCaseProvider.getToken("=pod\ntall this text\nshoud\n\nbe\n\n\npod");

        Assert.assertThat(token, IsInstanceOf.instanceOf(PodToken.class));
        Assert.assertEquals("=pod\ntall this text\nshoud\n\nbe\n\n\npod", token.getContent());
    }

    @Test public void testPod4()
    {
        Token token = TestCaseProvider.getToken("=pod=cut");

        Assert.assertThat(token, IsInstanceOf.instanceOf(PodToken.class));
        Assert.assertEquals("=pod=cut", token.getContent());
    }

    @Test public void testPod6()
    {
        Token token = TestCaseProvider.getToken("=pod\ntall this text\nsh=oud\n\nbe\n\n\npod=cut");

        Assert.assertThat(token, IsInstanceOf.instanceOf(PodToken.class));
        Assert.assertEquals("=pod\ntall this text\nsh=oud\n\nbe\n\n\npod=cut", token.getContent());
    }

    @Test public void testPod7()
    {
        Token token = TestCaseProvider.getToken("=pod=cut\n");

        Assert.assertThat(token, IsInstanceOf.instanceOf(PodToken.class));
        Assert.assertEquals("=pod=cut\n", token.getContent());
    }

    @Test public void testPod9()
    {
        Token[] tokens = TestCaseProvider.getTokens("=pod\n=cut\n\n");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(PodToken.class));
        Assert.assertEquals("=pod\n=cut\n", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[1].getContent());
    }

    @Test public void testWhitespace1()
    {
        Token token = TestCaseProvider.getToken(" ");

        Assert.assertThat(token, IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", token.getContent());
    }

    @Test public void testWhitespace2()
    {
        Token token = TestCaseProvider.getToken(" \n");

        Assert.assertThat(token, IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" \n", token.getContent());
    }

    @Test public void testWhitespace3()
    {
        Token[] tokens = TestCaseProvider.getTokens(" \n\n");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" \n", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[1].getContent());
    }
}
