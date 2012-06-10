package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Document;
import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.NumberToken;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.SymbolToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;

import java.util.List;


public class TestSymbolTokens
{
    //~ Methods

    @Test public void testAssignment()
    {
        Token[] tokens = TestCaseProvider.getTokens("$c = 1;");

        Assert.assertEquals(6, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("$c", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals("=", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[3].getContent());

        Assert.assertThat(tokens[4], IsInstanceOf.instanceOf(NumberToken.class));
        Assert.assertEquals("1", tokens[4].getContent());

        Assert.assertThat(tokens[5], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[5].getContent());
    }

    @Test public void testSymbol1()
    {
        Token token = TestCaseProvider.getToken("&a");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("&a", token.getContent());

        Assert.assertEquals("&a", ((SymbolToken) token).getCanonical());
        Assert.assertEquals("&a", ((SymbolToken) token).getSymbol());
    }

    @Test public void testSymbol10()
    {
        Document document = TestCaseProvider.parseSnippet("$foo[1];");
        List<SymbolToken> list = document.find(SymbolToken.class);

        Assert.assertEquals(1, list.size());
        Assert.assertEquals("$foo", list.get(0).getContent());

        Assert.assertEquals("$foo", list.get(0).getCanonical());
        Assert.assertEquals("@foo", list.get(0).getSymbol());
    }

    @Test public void testSymbol11()
    {
        Document document = TestCaseProvider.parseSnippet("$foo{1};");
        List<SymbolToken> list = document.find(SymbolToken.class);

        Assert.assertEquals(1, list.size());
        Assert.assertEquals("$foo", list.get(0).getContent());

        Assert.assertEquals("$foo", list.get(0).getCanonical());
        Assert.assertEquals("%foo", list.get(0).getSymbol());
    }

    @Test public void testSymbol13()
    {
        Token token = TestCaseProvider.getToken("*foo");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("*foo", token.getContent());

        Assert.assertEquals("*foo", ((SymbolToken) token).getCanonical());
        Assert.assertEquals("*foo", ((SymbolToken) token).getSymbol());
    }

    @Test public void testSymbol2()
    {
        Token token = TestCaseProvider.getToken("$::foo");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("$::foo", token.getContent());

        Assert.assertEquals("$main::foo", ((SymbolToken) token).getCanonical());
        Assert.assertEquals("$main::foo", ((SymbolToken) token).getSymbol());
    }

    @Test public void testSymbol3()
    {
        Token token = TestCaseProvider.getToken("$'foo");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("$'foo", token.getContent());

        Assert.assertEquals("$::foo", ((SymbolToken) token).getCanonical());
        Assert.assertEquals("$::foo", ((SymbolToken) token).getSymbol());
    }

    @Test public void testSymbol4()
    {
        Token[] tokens = TestCaseProvider.getTokens("%::;");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("%::", tokens[0].getContent());

        Assert.assertEquals("%main::", ((SymbolToken) tokens[0]).getCanonical());
        Assert.assertEquals("%main::", ((SymbolToken) tokens[0]).getSymbol());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[1].getContent());
    }

    @Test public void testSymbol5()
    {
        Token token = TestCaseProvider.getToken("$::foo'bar::baz");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("$::foo'bar::baz", token.getContent());

        Assert.assertEquals("$main::foo::bar::baz", ((SymbolToken) token).getCanonical());
        Assert.assertEquals("$main::foo::bar::baz", ((SymbolToken) token).getSymbol());
    }

    @Test public void testSymbol6()
    {
        Token token = TestCaseProvider.getToken("$foo");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("$foo", token.getContent());

        Assert.assertEquals("$foo", ((SymbolToken) token).getCanonical());
        Assert.assertEquals("$foo", ((SymbolToken) token).getSymbol());
    }

    @Test public void testSymbol7()
    {
        Token token = TestCaseProvider.getToken("$FOO");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("$FOO", token.getContent());

        Assert.assertEquals("$FOO", ((SymbolToken) token).getCanonical());
        Assert.assertEquals("$FOO", ((SymbolToken) token).getSymbol());
    }

    @Test public void testSymbol8()
    {
        Token token = TestCaseProvider.getToken("$_foo");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("$_foo", token.getContent());

        Assert.assertEquals("$_foo", ((SymbolToken) token).getCanonical());
        Assert.assertEquals("$_foo", ((SymbolToken) token).getSymbol());
    }

    @Test public void testSymbol9()
    {
        Token token = TestCaseProvider.getToken("@foo");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("@foo", token.getContent());

        Assert.assertEquals("@foo", ((SymbolToken) token).getCanonical());
        Assert.assertEquals("@foo", ((SymbolToken) token).getSymbol());
    }

    @Test public void testSymbolColon1()
    {
        Token[] tokens = TestCaseProvider.getTokens("&a:");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("&a", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(":", tokens[1].getContent());
    }

    @Test public void testSymbolColon2()
    {
        Token token = TestCaseProvider.getToken("&a::");

        Assert.assertThat(token, IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("&a::", token.getContent());
    }

    @Test public void testSymbolColon3()
    {
        Token[] tokens = TestCaseProvider.getTokens("&a:::");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("&a::", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(":", tokens[1].getContent());
    }

    @Test public void testSymbolColon4()
    {
        Token[] tokens = TestCaseProvider.getTokens("&a::::");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("&a::", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("::", tokens[1].getContent());
    }

    @Test public void testSymbolColon5()
    {
        Token[] tokens = TestCaseProvider.getTokens("&a:::::");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(SymbolToken.class));
        Assert.assertEquals("&a::", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("::", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(":", tokens[2].getContent());
    }
}
