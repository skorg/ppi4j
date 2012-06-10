package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.quote.DoubleQuoteToken;
import org.scriptkitty.ppi4j.token.quote.InterpolateQuoteToken;
import org.scriptkitty.ppi4j.token.quote.LiteralQuoteToken;
import org.scriptkitty.ppi4j.token.quote.SingleQuoteToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestQuoteTokens
{
    //~ Methods

    @Test public void testDoubleQuote1()
    {
        Token token = TestCaseProvider.getToken("\"");

        Assert.assertThat(token, IsInstanceOf.instanceOf(DoubleQuoteToken.class));

        Assert.assertEquals("\"", token.getContent());
        Assert.assertFalse(((DoubleQuoteToken) token).hasInterpolations());
    }

    @Test public void testDoubleQuote2()
    {
        Token token = TestCaseProvider.getToken("\"\"");

        Assert.assertThat(token, IsInstanceOf.instanceOf(DoubleQuoteToken.class));

        Assert.assertEquals("\"\"", token.getContent());
        Assert.assertFalse(((DoubleQuoteToken) token).hasInterpolations());
    }

    @Test public void testDoubleQuote3()
    {
        Token token = TestCaseProvider.getToken("\"no interpolations\"");

        Assert.assertThat(token, IsInstanceOf.instanceOf(DoubleQuoteToken.class));

        Assert.assertEquals("\"no interpolations\"", token.getContent());
        Assert.assertFalse(((DoubleQuoteToken) token).hasInterpolations());
    }

    @Test public void testDoubleQuote4()
    {
        Token token = TestCaseProvider.getToken("\"has $interpolation\"");

        Assert.assertThat(token, IsInstanceOf.instanceOf(DoubleQuoteToken.class));

        Assert.assertEquals("\"has $interpolation\"", token.getContent());
        Assert.assertTrue(((DoubleQuoteToken) token).hasInterpolations());
    }

    @Test public void testDoubleQuote5()
    {
        Token token = TestCaseProvider.getToken("\"has @interpolation\"");

        Assert.assertThat(token, IsInstanceOf.instanceOf(DoubleQuoteToken.class));

        Assert.assertEquals("\"has @interpolation\"", token.getContent());
        Assert.assertTrue(((DoubleQuoteToken) token).hasInterpolations());
    }

    @Test public void testDoubleQuote6()
    {
        Token token = TestCaseProvider.getToken("\"has \\@interpolation\"");

        Assert.assertThat(token, IsInstanceOf.instanceOf(DoubleQuoteToken.class));

        Assert.assertEquals("\"has \\@interpolation\"", token.getContent());
        Assert.assertTrue(((DoubleQuoteToken) token).hasInterpolations());
    }

    @Test public void testInterpolate1()
    {
        Token token = TestCaseProvider.getToken("qq{foo}");

        Assert.assertThat(token, IsInstanceOf.instanceOf(InterpolateQuoteToken.class));
        Assert.assertEquals("qq{foo}", token.getContent());

        Assert.assertEquals("foo", ((InterpolateQuoteToken) token).getString());
    }

    @Test public void testInterpolate2()
    {
        Token token = TestCaseProvider.getToken("qq!bar!");

        Assert.assertThat(token, IsInstanceOf.instanceOf(InterpolateQuoteToken.class));
        Assert.assertEquals("qq!bar!", token.getContent());

        Assert.assertEquals("bar", ((InterpolateQuoteToken) token).getString());
    }

    @Test public void testInterpolate3()
    {
        Token token = TestCaseProvider.getToken("qq <foo>");

        Assert.assertThat(token, IsInstanceOf.instanceOf(InterpolateQuoteToken.class));
        Assert.assertEquals("qq <foo>", token.getContent());

        Assert.assertEquals("foo", ((InterpolateQuoteToken) token).getString());
    }

    @Test public void testLiteralQuote1()
    {
        Token token = TestCaseProvider.getToken("q//");

        Assert.assertThat(token, IsInstanceOf.instanceOf(LiteralQuoteToken.class));
        Assert.assertEquals("q//", token.getContent());

        Assert.assertEquals("", ((LiteralQuoteToken) token).getString());
        Assert.assertEquals("", ((LiteralQuoteToken) token).getLiteral());
    }

    @Test public void testLiteralQuote2()
    {
        Token token = TestCaseProvider.getToken("q //");

        Assert.assertThat(token, IsInstanceOf.instanceOf(LiteralQuoteToken.class));
        Assert.assertEquals("q //", token.getContent());

        Assert.assertEquals("", ((LiteralQuoteToken) token).getString());
        Assert.assertEquals("", ((LiteralQuoteToken) token).getLiteral());
    }

    @Test public void testLiteralQuote3()
    {
        Token token = TestCaseProvider.getToken("q{foo}");

        Assert.assertThat(token, IsInstanceOf.instanceOf(LiteralQuoteToken.class));
        Assert.assertEquals("q{foo}", token.getContent());

        Assert.assertEquals("foo", ((LiteralQuoteToken) token).getString());
        Assert.assertEquals("foo", ((LiteralQuoteToken) token).getLiteral());
    }

    @Test public void testLiteralQuote4()
    {
        Token token = TestCaseProvider.getToken("q!bar!");

        Assert.assertThat(token, IsInstanceOf.instanceOf(LiteralQuoteToken.class));
        Assert.assertEquals("q!bar!", token.getContent());

        Assert.assertEquals("bar", ((LiteralQuoteToken) token).getString());
        Assert.assertEquals("bar", ((LiteralQuoteToken) token).getLiteral());
    }

    @Test public void testLiteralQuote5()
    {
        Token token = TestCaseProvider.getToken("q <foo>");

        Assert.assertThat(token, IsInstanceOf.instanceOf(LiteralQuoteToken.class));
        Assert.assertEquals("q <foo>", token.getContent());

        Assert.assertEquals("foo", ((LiteralQuoteToken) token).getString());
        Assert.assertEquals("foo", ((LiteralQuoteToken) token).getLiteral());
    }

    @Test public void testLiteralQuote6()
    {
        Token[] tokens = TestCaseProvider.getTokens("q#\nuse Inline;\nInline->bind( %s => $module_src );\n#;");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(LiteralQuoteToken.class));
        Assert.assertEquals("q#\nuse Inline;\nInline->bind( %s => $module_src );\n#", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[1].getContent());
    }

    @Test public void testSingleQuote1()
    {
        Token token = TestCaseProvider.getToken("'");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SingleQuoteToken.class));
        Assert.assertEquals("'", token.getContent());

        Assert.assertEquals("", ((SingleQuoteToken) token).getString());
        Assert.assertEquals("", ((SingleQuoteToken) token).getLiteral());
    }

    @Test public void testSingleQuote2()
    {
        Token token = TestCaseProvider.getToken("''");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SingleQuoteToken.class));
        Assert.assertEquals("''", token.getContent());

        Assert.assertEquals("", ((SingleQuoteToken) token).getString());
        Assert.assertEquals("", ((SingleQuoteToken) token).getLiteral());
    }

    @Test public void testSingleQuote3()
    {
        Token token = TestCaseProvider.getToken("'f'");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SingleQuoteToken.class));
        Assert.assertEquals("'f'", token.getContent());

        Assert.assertEquals("f", ((SingleQuoteToken) token).getString());
        Assert.assertEquals("f", ((SingleQuoteToken) token).getLiteral());
    }

    @Test public void testSingleQuote4()
    {
        Token token = TestCaseProvider.getToken("'f\\'b'");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SingleQuoteToken.class));
        Assert.assertEquals("'f\\'b'", token.getContent());

        Assert.assertEquals("f\\'b", ((SingleQuoteToken) token).getString());
        Assert.assertEquals("f'b", ((SingleQuoteToken) token).getLiteral());
    }

    @Test public void testSingleQuote5()
    {
        Token token = TestCaseProvider.getToken("'f\\nb'");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SingleQuoteToken.class));
        Assert.assertEquals("'f\\nb'", token.getContent());

        Assert.assertEquals("f\\nb", ((SingleQuoteToken) token).getString());
        Assert.assertEquals("f\\nb", ((SingleQuoteToken) token).getLiteral());
    }

    @Test public void testSingleQuote6()
    {
        Token token = TestCaseProvider.getToken("'f\\\\b'");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SingleQuoteToken.class));
        Assert.assertEquals("'f\\\\b'", token.getContent());

        Assert.assertEquals("f\\\\b", ((SingleQuoteToken) token).getString());
        Assert.assertEquals("f\\b", ((SingleQuoteToken) token).getLiteral());
    }

    @Test public void testSingleQuote7()
    {
        Token token = TestCaseProvider.getToken("'f\\\\\\b'");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SingleQuoteToken.class));
        Assert.assertEquals("'f\\\\\\b'", token.getContent());

        Assert.assertEquals("f\\\\\\b", ((SingleQuoteToken) token).getString());
        Assert.assertEquals("f\\\\b", ((SingleQuoteToken) token).getLiteral());
    }

    @Test public void testSingleQuote8()
    {
        Token token = TestCaseProvider.getToken("'f\\\\\\\''");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SingleQuoteToken.class));
        Assert.assertEquals("'f\\\\\\\''", token.getContent());

        Assert.assertEquals("f\\\\\\\'", ((SingleQuoteToken) token).getString());
        Assert.assertEquals("f\\'", ((SingleQuoteToken) token).getLiteral());
    }
}
