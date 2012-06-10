package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.HereDocToken;
import org.scriptkitty.ppi4j.token.HereDocToken.Mode;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.token.quote.DoubleQuoteToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestHereDocTokens
{
    //~ Methods

    @Test public void testHereDoc1() throws Exception
    {
        Token token = TestCaseProvider.getToken("<<EOF");

        Assert.assertThat(token, IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<EOF", token.getContent());

        Assert.assertEquals("EOF", ((HereDocToken) token).getTerminator());
        Assert.assertTrue(((HereDocToken) token).isMode(Mode.INTERPOLATE));
    }

    @Test public void testHereDoc10() throws Exception
    {
        Token[] tokens = TestCaseProvider.getTokens("<<EOF\nEOF");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<EOF", tokens[0].getContent());

        Assert.assertEquals("EOF", ((HereDocToken) tokens[0]).getTerminator());
    }

    @Test public void testHereDoc11() throws Exception
    {
        Token[] tokens = TestCaseProvider.getTokens("<<EOF\nheredocEOF");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<EOF", tokens[0].getContent());

        Assert.assertEquals("EOF", ((HereDocToken) tokens[0]).getTerminator());
        Assert.assertEquals(1, ((HereDocToken) tokens[0]).getLineCount());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[1].getContent());
    }

    @Test public void testHereDoc12() throws Exception
    {
        Token[] tokens = TestCaseProvider.getTokens("<<EOF foo");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<EOF", tokens[0].getContent());

        Assert.assertEquals("EOF", ((HereDocToken) tokens[0]).getTerminator());
        Assert.assertEquals(0, ((HereDocToken) tokens[0]).getLineCount());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("foo", tokens[2].getContent());
    }

    @Test public void testHereDoc13() throws Exception
    {
        Token[] tokens = TestCaseProvider.getTokens("(<<EOF");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("(", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<EOF", tokens[1].getContent());
    }

    @Test public void testHereDoc14() throws Exception
    {
        Token[] tokens = TestCaseProvider.getTokens("<<BEGIN.\"middle\\n\".<<END;\nbeginning\nBEGIN\nend\nEND");

        Assert.assertEquals(7, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<BEGIN", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(".", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(DoubleQuoteToken.class));
        Assert.assertEquals("\"middle\\n\"", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(".", tokens[3].getContent());

        Assert.assertThat(tokens[4], IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<END", tokens[4].getContent());

        Assert.assertThat(tokens[5], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[5].getContent());

        Assert.assertThat(tokens[6], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[6].getContent());
    }

    @Test public void testHereDoc2() throws Exception
    {
        Token token = TestCaseProvider.getToken("<<\\EOF");

        Assert.assertThat(token, IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<\\EOF", token.getContent());

        Assert.assertEquals("EOF", ((HereDocToken) token).getTerminator());
        Assert.assertTrue(((HereDocToken) token).isMode(Mode.LITERAL));
    }

    @Test public void testHereDoc3() throws Exception
    {
        Token token = TestCaseProvider.getToken("<<\"EOF\"");

        Assert.assertThat(token, IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<\"EOF\"", token.getContent());

        Assert.assertEquals("EOF", ((HereDocToken) token).getTerminator());
        Assert.assertTrue(((HereDocToken) token).isMode(Mode.INTERPOLATE));
    }

    @Test public void testHereDoc4() throws Exception
    {
        Token token = TestCaseProvider.getToken("<<\"EO$F\"");

        Assert.assertThat(token, IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<\"EO$F\"", token.getContent());

        Assert.assertEquals("EO$F", ((HereDocToken) token).getTerminator());
        Assert.assertTrue(((HereDocToken) token).isMode(Mode.INTERPOLATE));
    }

    @Test public void testHereDoc5() throws Exception
    {
        Token token = TestCaseProvider.getToken("<<'EOF'");

        Assert.assertThat(token, IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<'EOF'", token.getContent());

        Assert.assertEquals("EOF", ((HereDocToken) token).getTerminator());
        Assert.assertTrue(((HereDocToken) token).isMode(Mode.LITERAL));
    }

    @Test public void testHereDoc6() throws Exception
    {
        Token token = TestCaseProvider.getToken("<<'EO$F'");

        Assert.assertThat(token, IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<'EO$F'", token.getContent());

        Assert.assertEquals("EO$F", ((HereDocToken) token).getTerminator());
        Assert.assertTrue(((HereDocToken) token).isMode(Mode.LITERAL));
    }

    @Test public void testHereDoc7() throws Exception
    {
        Token token = TestCaseProvider.getToken("<<`EOF`");

        Assert.assertThat(token, IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<`EOF`", token.getContent());

        Assert.assertEquals("EOF", ((HereDocToken) token).getTerminator());
        Assert.assertTrue(((HereDocToken) token).isMode(Mode.COMMAND));
    }

    @Test public void testHereDoc8() throws Exception
    {
        Token token = TestCaseProvider.getToken("<<`EO$F`");

        Assert.assertThat(token, IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<`EO$F`", token.getContent());

        Assert.assertEquals("EO$F", ((HereDocToken) token).getTerminator());
        Assert.assertTrue(((HereDocToken) token).isMode(Mode.COMMAND));
    }

    @Test public void testHereDoc9() throws Exception
    {
        Token[] tokens = TestCaseProvider.getTokens("<<EOF\n");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(HereDocToken.class));
        Assert.assertEquals("<<EOF", tokens[0].getContent());

        Assert.assertEquals("EOF", ((HereDocToken) tokens[0]).getTerminator());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals("\n", tokens[1].getContent());
    }
}
