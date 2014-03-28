package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.CastToken;
import org.scriptkitty.ppi4j.token.MagicToken;
import org.scriptkitty.ppi4j.token.NumberToken;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestMagicTokens
{
    //~ Methods

    @Test public void testAll()
    {
        for (String snippet : MagicScanner.MAGIC)
        {
            Token[] tokens = TestCaseProvider.getTokens(snippet + ";");

            Assert.assertEquals(2, tokens.length);

            Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(MagicToken.class));
            Assert.assertEquals(snippet, tokens[0].getContent());

            Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
            Assert.assertEquals(";", tokens[1].getContent());
        }
    }

    @Test public void testMagic1()
    {
        Token token = TestCaseProvider.getToken("$^WIDE_SYSTEM_CALLS");

        Assert.assertThat(token, IsInstanceOf.instanceOf(MagicToken.class));
        Assert.assertEquals("$^WIDE_SYSTEM_CALLS", token.getContent());
    }

    @Test public void testMagic2()
    {
        Token token = TestCaseProvider.getToken("${^MATCH}");

        Assert.assertThat(token, IsInstanceOf.instanceOf(MagicToken.class));
        Assert.assertEquals("${^MATCH}", token.getContent());
    }

    @Test public void testMagic3()
    {
        Token token = TestCaseProvider.getToken("@{^_Bar}");

        Assert.assertThat(token, IsInstanceOf.instanceOf(MagicToken.class));
        Assert.assertEquals("@{^_Bar}", token.getContent());
    }

    @Test public void testMagic4()
    {
        Token[] tokens = TestCaseProvider.getTokens("${^_Bar}[0]");

        Assert.assertEquals(4, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(MagicToken.class));
        Assert.assertEquals("${^_Bar}", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("[", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("0", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("]", tokens[3].getContent());
    }

    @Test public void testMagic5()
    {
        Token token = TestCaseProvider.getToken("%{^_Baz}");

        Assert.assertThat(token, IsInstanceOf.instanceOf(MagicToken.class));
        Assert.assertEquals("%{^_Baz}", token.getContent());
    }

    @Test public void testMagic6()
    {
        Token[] tokens = TestCaseProvider.getTokens("${^_Baz}{burfle}");

        Assert.assertEquals(4, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(MagicToken.class));
        Assert.assertEquals("${^_Baz}", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("{", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("burfle", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("}", tokens[3].getContent());
    }

    @Test public void testMagic7()
    {
        Token[] tokens = TestCaseProvider.getTokens("$${^MATCH}");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(CastToken.class));
        Assert.assertEquals("$", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(MagicToken.class));
        Assert.assertEquals("${^MATCH}", tokens[1].getContent());
    }

    @Test public void testMagic8()
    {
        Token[] tokens = TestCaseProvider.getTokens("\\${^MATCH}");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(CastToken.class));
        Assert.assertEquals("\\", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(MagicToken.class));
        Assert.assertEquals("${^MATCH}", tokens[1].getContent());
    }

    @Test public void testMagic9()
    {
        Token[] tokens = TestCaseProvider.getTokens("$'1");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(MagicToken.class));
        Assert.assertEquals("$'", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("1", tokens[1].getContent());
    }

    @Test public void testNotMagic1()
    {
        Token[] tokens = TestCaseProvider.getTokens("${^F}");

        Assert.assertEquals(5, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(CastToken.class));
        Assert.assertEquals("$", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("{", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("^", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("F", tokens[3].getContent());

        Assert.assertThat(tokens[4], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("}", tokens[4].getContent());
    }
}
