package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.NumberToken;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.token.number.BinaryNumberToken;
import org.scriptkitty.ppi4j.token.number.ExpNumberToken;
import org.scriptkitty.ppi4j.token.number.FloatNumberToken;
import org.scriptkitty.ppi4j.token.number.HexNumberToken;
import org.scriptkitty.ppi4j.token.number.OctalNumberToken;
import org.scriptkitty.ppi4j.token.number.VersionNumberToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestNumberTokens
{
    //~ Methods

    @Test public void testBinary1()
    {
        Token token = TestCaseProvider.getToken("0b1110011");

        Assert.assertThat(token, IsInstanceOf.instanceOf(BinaryNumberToken.class));
        Assert.assertEquals("0b1110011", token.getContent());
        Assert.assertEquals(115, ((BinaryNumberToken) token).toNumber().intValue());
    }

    @Test(expected = NumberFormatException.class)
    public void testBinary2()
    {
        // this is not a valid binary number, but it must be treated as one by the tokenizer
        Token token = TestCaseProvider.getToken("0b1210011");

        Assert.assertThat(token, IsInstanceOf.instanceOf(BinaryNumberToken.class));
        Assert.assertEquals("0b1210011", token.getContent());
        Assert.assertTrue(token.hasAttribute(Token.Attribute.INVALID));

        // this should throw exception, nothing to assert
        ((BinaryNumberToken) token).toNumber();
    }

    @Test public void testExp1()
    {
        Token token = TestCaseProvider.getToken("1e+1");

        Assert.assertThat(token, IsInstanceOf.instanceOf(ExpNumberToken.class));
        Assert.assertEquals("1e+1", token.getContent());
        Assert.assertEquals(Float.valueOf(10), ((ExpNumberToken) token).toNumber());
    }

    @Test public void testExp10()
    {
        Token[] tokens = TestCaseProvider.getTokens("1e-2a");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(ExpNumberToken.class));
        Assert.assertEquals("1e-2", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("a", tokens[1].getContent());
    }

    @Test public void testExp11()
    {
        Token[] tokens = TestCaseProvider.getTokens("1e+2.a");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(ExpNumberToken.class));
        Assert.assertEquals("1e+2", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(".", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("a", tokens[2].getContent());
    }

    @Test public void testExp12()
    {
        Token[] tokens = TestCaseProvider.getTokens("1e.+1");

        Assert.assertEquals(4, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(ExpNumberToken.class));
        Assert.assertEquals("1e", tokens[0].getContent());
        Assert.assertTrue(tokens[0].hasAttribute(Token.Attribute.INVALID));

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(".", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("+", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("1", tokens[3].getContent());
    }

    @Test(expected = NumberFormatException.class)
    public void testExp13()
    {
        Token[] tokens = TestCaseProvider.getTokens("1e.1");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(ExpNumberToken.class));
        Assert.assertEquals("1e", tokens[0].getContent());
        Assert.assertTrue(tokens[0].hasAttribute(Token.Attribute.INVALID));

        // this should throw exception, nothing to assert
        ((ExpNumberToken) tokens[0]).toNumber();

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(FloatNumberToken.class));
        Assert.assertEquals(".1", tokens[1].getContent());
        Assert.assertEquals(Float.valueOf(.1f), ((FloatNumberToken) tokens[1]).toNumber());
    }

    @Test public void testExp2()
    {
        Token token = TestCaseProvider.getToken("1.e+1");

        Assert.assertThat(token, IsInstanceOf.instanceOf(ExpNumberToken.class));
        Assert.assertEquals("1.e+1", token.getContent());
    }

    @Test public void testExp3()
    {
        Token token = TestCaseProvider.getToken("1.0e+1");

        Assert.assertThat(token, IsInstanceOf.instanceOf(ExpNumberToken.class));
        Assert.assertEquals("1.0e+1", token.getContent());
    }

    @Test public void testExp4()
    {
        Token[] tokens = TestCaseProvider.getTokens("1e+2345,6789");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(ExpNumberToken.class));
        Assert.assertEquals("1e+2345", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(",", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("6789", tokens[2].getContent());
    }

    @Test public void testExp5()
    {
        Token token = TestCaseProvider.getToken("1e2");

        Assert.assertThat(token, IsInstanceOf.instanceOf(ExpNumberToken.class));
        Assert.assertEquals("1e2", token.getContent());
    }

    @Test public void testExp6() throws Exception
    {
        Token token = TestCaseProvider.getToken("1e-2");

        Assert.assertThat(token, IsInstanceOf.instanceOf(ExpNumberToken.class));
        Assert.assertEquals("1e-2", token.getContent());
    }

    @Test public void testExp7()
    {
        Token[] tokens = TestCaseProvider.getTokens("1e--2");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(ExpNumberToken.class));
        Assert.assertEquals("1e-", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("-2", tokens[1].getContent());
    }

    @Test public void testExp8()
    {
        Token[] tokens = TestCaseProvider.getTokens("1e-+2");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(ExpNumberToken.class));
        Assert.assertEquals("1e-", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("+", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("2", tokens[2].getContent());
    }

    @Test public void testExp9()
    {
        Token[] tokens = TestCaseProvider.getTokens("1e-2.2");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(ExpNumberToken.class));
        Assert.assertEquals("1e-2", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(FloatNumberToken.class));
        Assert.assertEquals(".2", tokens[1].getContent());
    }

    @Test public void testFloat1()
    {
        Token token = TestCaseProvider.getToken("1.2345");

        Assert.assertThat(token, IsInstanceOf.instanceOf(FloatNumberToken.class));
        Assert.assertEquals("1.2345", token.getContent());
        Assert.assertEquals(Float.valueOf(1.2345f), ((FloatNumberToken) token).toNumber());
    }

    @Test public void testFloat2()
    {
        Token token = TestCaseProvider.getToken(".0");

        Assert.assertThat(token, IsInstanceOf.instanceOf(FloatNumberToken.class));
        Assert.assertEquals(".0", token.getContent());
        Assert.assertEquals(Float.valueOf(.0f), ((FloatNumberToken) token).toNumber());
    }

    @Test public void testHex1()
    {
        Token token = TestCaseProvider.getToken("0x0123456789abcdef");

        Assert.assertThat(token, IsInstanceOf.instanceOf(HexNumberToken.class));
        Assert.assertEquals("0x0123456789abcdef", token.getContent());
        Assert.assertEquals(81985529216486895L, ((HexNumberToken) token).toNumber().longValue());
    }

    @Test public void testHex2()
    {
        Token[] tokens = TestCaseProvider.getTokens("0x0123456789abcdefz");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(HexNumberToken.class));
        Assert.assertEquals("0x0123456789abcdef", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("z", tokens[1].getContent());
    }

    @Test public void testNumber1()
    {
        Token token = TestCaseProvider.getToken("0");

        Assert.assertThat(token, IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("0", token.getContent());
        Assert.assertEquals(0, ((NumberToken) token).toNumber().intValue());
    }

    @Test public void testNumber2()
    {
        Token[] tokens = TestCaseProvider.getTokens("1,e+1");

        Assert.assertEquals(5, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("1", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(",", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("e", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("+", tokens[3].getContent());

        Assert.assertThat(tokens[4], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("1", tokens[4].getContent());
    }

    @Test public void testNumber3()
    {
        Token[] tokens = TestCaseProvider.getTokens("1..1");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("1", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("..", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("1", tokens[2].getContent());
    }

    @Test public void testOctal1()
    {
        Token token = TestCaseProvider.getToken("0777");

        Assert.assertThat(token, IsInstanceOf.instanceOf(OctalNumberToken.class));
        Assert.assertEquals("0777", token.getContent());
        Assert.assertEquals(511, ((OctalNumberToken) token).toNumber().intValue());
    }

    @Test(expected = NumberFormatException.class)
    public void testOctal2()
    {
        // this is not a valid octal number, but it must be treated as one by the tokenizer
        Token token = TestCaseProvider.getToken("0778");

        Assert.assertThat(token, IsInstanceOf.instanceOf(OctalNumberToken.class));
        Assert.assertEquals("0778", token.getContent());
        Assert.assertTrue(token.hasAttribute(Token.Attribute.INVALID));

        // this should throw exception, nothing to assert
        ((OctalNumberToken) token).toNumber();
    }

    @Test(expected = NumberFormatException.class)
    public void testVersion1()
    {
        Token token = TestCaseProvider.getToken("1.2.3.4");

        Assert.assertThat(token, IsInstanceOf.instanceOf(VersionNumberToken.class));
        Assert.assertEquals("1.2.3.4", token.getContent());

        // this should throw exception, nothing to assert
        ((VersionNumberToken) token).toNumber();
    }

    @Test public void testVersion2() throws Exception
    {
        Token token = TestCaseProvider.getToken("v1");

        Assert.assertThat(token, IsInstanceOf.instanceOf(VersionNumberToken.class));
        Assert.assertEquals("v1", token.getContent());
    }

    @Test public void testVersion3()
    {
        Token token = TestCaseProvider.getToken("v1.2.3.4");

        Assert.assertThat(token, IsInstanceOf.instanceOf(VersionNumberToken.class));
        Assert.assertEquals("v1.2.3.4", token.getContent());
    }

    @Test public void testVersion4()
    {
        Token[] tokens = TestCaseProvider.getTokens("v1..2");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(VersionNumberToken.class));
        Assert.assertEquals("v1", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("..", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("2", tokens[2].getContent());
    }

    @Test public void testVersion5()
    {
        Token token = TestCaseProvider.getToken("1_0.1_0.1");

        Assert.assertThat(token, IsInstanceOf.instanceOf(VersionNumberToken.class));
        Assert.assertEquals("1_0.1_0.1", token.getContent());
    }

    @Test public void testVersion6()
    {
        Token token = TestCaseProvider.getToken("1_0.1_0.1_0");

        Assert.assertThat(token, IsInstanceOf.instanceOf(VersionNumberToken.class));
        Assert.assertEquals("1_0.1_0.1_0", token.getContent());
    }
}
