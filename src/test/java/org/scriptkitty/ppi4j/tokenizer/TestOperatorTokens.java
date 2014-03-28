package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsNot;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.CastToken;
import org.scriptkitty.ppi4j.token.NumberToken;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.token.quote.DoubleQuoteToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestOperatorTokens
{
    //~ Methods

    @Test public void testAll()
    {
        for (String snippet : OperatorScanner.OPERATORS)
        {
            // these by themselves are not tokenized to an operator
            if (snippet.startsWith("/") || snippet.equals("<>"))
            {
                continue;
            }

            Token[] tokens = TestCaseProvider.getTokens(snippet + ";");

            Assert.assertEquals(snippet, 2, tokens.length);

            Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(OperatorToken.class));
            Assert.assertEquals(snippet, tokens[0].getContent());

            Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
            Assert.assertEquals(";", tokens[1].getContent());
        }
    }

    @Test public void testOperator1() throws Exception
    {
        Token token = TestCaseProvider.getToken(".");

        Assert.assertThat(token, IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(".", token.getContent());
    }

    @Test public void testOperator10() throws Exception
    {
        Token[] tokens = TestCaseProvider.getTokens("{<FOO>};");

        Assert.assertEquals(6, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("{", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("<", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("FOO", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(">", tokens[3].getContent());

        Assert.assertThat(tokens[4], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("}", tokens[4].getContent());

        Assert.assertThat(tokens[5], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[5].getContent());
    }

    @Test public void testOperator2() throws Exception
    {
        Token token = TestCaseProvider.getToken(".1");

        Assert.assertThat(token, IsNot.not(IsInstanceOf.instanceOf(OperatorToken.class)));
        Assert.assertEquals(".1", token.getContent());
    }

    @Test public void testOperator3() throws Exception
    {
        Token token = TestCaseProvider.getToken("<<");

        Assert.assertThat(token, IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("<<", token.getContent());
    }

    @Test public void testOperator4() throws Exception
    {
        Token token = TestCaseProvider.getToken("-e");

        Assert.assertThat(token, IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("-e", token.getContent());
    }

    @Test public void testOperator5() throws Exception
    {
        Token[] tokens = TestCaseProvider.getTokens("%2");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("%", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("2", tokens[1].getContent());
    }

    @Test public void testOperator6() throws Exception
    {
        Token[] tokens = TestCaseProvider.getTokens("*2");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("*", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("2", tokens[1].getContent());
    }

    @Test public void testOperator7() throws Exception
    {
        Token[] tokens = TestCaseProvider.getTokens("1/");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("1", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("/", tokens[1].getContent());
    }

    @Test public void testOperator8() throws Exception
    {
        Token[] tokens = TestCaseProvider.getTokens("1//");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("1", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("//", tokens[1].getContent());
    }

    @Test public void testOperator9() throws Exception
    {
        Token[] tokens = TestCaseProvider.getTokens("<<\\\"EOF\"");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("<<", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(CastToken.class));
        Assert.assertEquals("\\", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(DoubleQuoteToken.class));
        Assert.assertEquals("\"EOF\"", tokens[2].getContent());
    }
}
