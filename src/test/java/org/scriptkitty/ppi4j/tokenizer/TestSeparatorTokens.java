package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.CommentToken;
import org.scriptkitty.ppi4j.token.DataToken;
import org.scriptkitty.ppi4j.token.EndToken;
import org.scriptkitty.ppi4j.token.SeparatorToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestSeparatorTokens
{
    //~ Methods

    @Test public void testData1()
    {
        Token token = TestCaseProvider.getToken("__DATA__");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SeparatorToken.class));
        Assert.assertEquals("__DATA__", token.getContent());
    }

    @Test public void testData2()
    {
        Token[] tokens = TestCaseProvider.getTokens("__DATA__\n");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SeparatorToken.class));
        Assert.assertEquals("__DATA__", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[1].getContent());
    }

    @Test public void testData3()
    {
        Token[] tokens = TestCaseProvider.getTokens("__DATA__\nthis\nis all\ndata");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SeparatorToken.class));
        Assert.assertEquals("__DATA__", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(DataToken.class));
        Assert.assertEquals("this\nis all\ndata", tokens[2].getContent());
    }

    @Test public void testData4()
    {
        Token[] tokens = TestCaseProvider.getTokens("__DATA__ comment");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SeparatorToken.class));
        Assert.assertEquals("__DATA__", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals(" comment", tokens[1].getContent());
    }

    @Test public void testData5()
    {
        Token[] tokens = TestCaseProvider.getTokens("__DATA__;comment\n");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SeparatorToken.class));
        Assert.assertEquals("__DATA__", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals(";comment", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[2].getContent());
    }

    @Test public void testData6()
    {
        Token[] tokens = TestCaseProvider.getTokens("__DATA__ comment\nthis\nis all\ndata");

        Assert.assertEquals(4, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SeparatorToken.class));
        Assert.assertEquals("__DATA__", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals(" comment", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(DataToken.class));
        Assert.assertEquals("this\nis all\ndata", tokens[3].getContent());
    }

    @Test public void testEnd1()
    {
        Token token = TestCaseProvider.getToken("__END__");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SeparatorToken.class));
        Assert.assertEquals("__END__", token.getContent());
    }

    @Test public void testEnd2()
    {
        Token[] tokens = TestCaseProvider.getTokens("__END__\n");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SeparatorToken.class));
        Assert.assertEquals("__END__", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[1].getContent());
    }

    @Test public void testEnd3()
    {
        Token[] tokens = TestCaseProvider.getTokens("__END__\nthis\nis all\ndata");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SeparatorToken.class));
        Assert.assertEquals("__END__", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(EndToken.class));
        Assert.assertEquals("this\nis all\ndata", tokens[2].getContent());
    }

    @Test public void testEnd4()
    {
        Token[] tokens = TestCaseProvider.getTokens("__END__ comment");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SeparatorToken.class));
        Assert.assertEquals("__END__", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals(" comment", tokens[1].getContent());
    }

    @Test public void testEnd5()
    {
        Token[] tokens = TestCaseProvider.getTokens("__END__;comment\n");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SeparatorToken.class));
        Assert.assertEquals("__END__", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals(";comment", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[2].getContent());
    }

    @Test public void testEnd6()
    {
        Token[] tokens = TestCaseProvider.getTokens("__END__ comment\nthis\nis all\ndata");

        Assert.assertEquals(4, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SeparatorToken.class));
        Assert.assertEquals("__END__", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(CommentToken.class));
        Assert.assertEquals(" comment", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(EndToken.class));
        Assert.assertEquals("this\nis all\ndata", tokens[3].getContent());
    }

    @Test public void testWord1()
    {
        Token token = TestCaseProvider.getToken("__DATA__a");

        Assert.assertThat(token, IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("__DATA__a", token.getContent());
    }

    @Test public void testWord2()
    {
        Token token = TestCaseProvider.getToken("__END__a");

        Assert.assertThat(token, IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("__END__a", token.getContent());
    }

}
