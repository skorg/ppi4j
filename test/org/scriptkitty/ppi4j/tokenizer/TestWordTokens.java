package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Document;
import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;

import java.util.List;


public class TestWordTokens
{
    //~ Methods

    @Test public void testDashedWordToken1()
    {
        // dashed words are just treated like word tokens
        Token token = TestCaseProvider.getToken("-dash");

        Assert.assertThat(token, IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("-dash", token.getContent());
    }

    @Test public void testIsClassName()
    {
        Document document = TestCaseProvider.parseSnippet("Foo->");
        List<WordToken> list = document.find(WordToken.class);

        Assert.assertEquals(1, list.size());
        Assert.assertTrue(list.get(0).isClassName());
    }

    @Test public void testIsFunctionCall()
    {
        Document document = TestCaseProvider.parseSnippet("::bar");
        List<WordToken> list = document.find(WordToken.class);

        Assert.assertEquals(1, list.size());
        Assert.assertFalse(list.get(0).isMethodCall());
        Assert.assertTrue(list.get(0).isFunctionCall());
    }

    @Test public void testIsIncludedModuleName()
    {
        Document document = TestCaseProvider.parseSnippet("use strict;");
        List<WordToken> list = document.find(WordToken.class);

        Assert.assertEquals(2, list.size());

        Assert.assertFalse(list.get(0).isIncludedModuleName());
        Assert.assertTrue(list.get(1).isIncludedModuleName());
    }

    @Test public void testIsLabelPointer()
    {
        Document document = TestCaseProvider.parseSnippet("goto FOO");
        List<WordToken> list = document.find(WordToken.class);

        Assert.assertEquals(2, list.size());
        Assert.assertTrue(list.get(0).isBuiltin());
        Assert.assertTrue(list.get(1).isLabelPointer());
    }

    @Test public void testIsMethodCall()
    {
        Document document = TestCaseProvider.parseSnippet("->bar");
        List<WordToken> list = document.find(WordToken.class);

        Assert.assertEquals(1, list.size());
        Assert.assertTrue(list.get(0).isMethodCall());
        Assert.assertFalse(list.get(0).isFunctionCall());
    }

    @Test public void testIsSubroutineName()
    {
        Document document = TestCaseProvider.parseSnippet("sub foo;");
        List<WordToken> list = document.find(WordToken.class);

        Assert.assertEquals(2, list.size());

        Assert.assertTrue(list.get(0).isBareword());
        Assert.assertTrue(list.get(1).isSubroutineName());
    }

    @Test public void testWordToken1()
    {
        Token token = TestCaseProvider.getToken("sub");

        Assert.assertThat(token, IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", token.getContent());
    }

    @Test public void testWordToken2()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub ");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());
    }

    @Test public void testWordToken3()
    {
        Token token = TestCaseProvider.getToken("::foo");

        Assert.assertThat(token, IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("::foo", token.getContent());
    }

    @Test public void testWordToken4()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub v1");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("v1", tokens[2].getContent());
    }

    @Test public void testWordToken5()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub v1f");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("v1f", tokens[2].getContent());
    }

    @Test public void testWordToken6()
    {
        Token[] tokens = TestCaseProvider.getTokens("package v1f");

        Assert.assertEquals(3, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("package", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("v1f", tokens[2].getContent());
    }

    @Test public void testWordToken7()
    {
        Token token = TestCaseProvider.getToken("Foo'Bar");

        Assert.assertThat(token, IsInstanceOf.instanceOf(WordToken.class));

        Assert.assertEquals("Foo'Bar", token.getContent());
        Assert.assertEquals("Foo::Bar", ((WordToken) token).getLiteral());
    }
}
