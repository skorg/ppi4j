package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Document;
import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.AttributeToken;
import org.scriptkitty.ppi4j.token.OperatorToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.WhitespaceToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;

import java.util.List;


public class TestAttributeTokens
{
    //~ Methods

    @Test public void testAttributeToken1()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub foo : attr();");

        Assert.assertEquals(8, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("foo", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[3].getContent());

        Assert.assertThat(tokens[4], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(":", tokens[4].getContent());

        Assert.assertThat(tokens[5], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[5].getContent());

        Assert.assertThat(tokens[6], IsInstanceOf.instanceOf(AttributeToken.class));
        Assert.assertEquals("attr()", tokens[6].getContent());

        Assert.assertThat(tokens[7], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[7].getContent());
    }

    @Test public void testAttributeToken2()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub foo : attr(());");

        Assert.assertEquals(8, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("foo", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[3].getContent());

        Assert.assertThat(tokens[4], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(":", tokens[4].getContent());

        Assert.assertThat(tokens[5], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[5].getContent());

        Assert.assertThat(tokens[6], IsInstanceOf.instanceOf(AttributeToken.class));
        Assert.assertEquals("attr(())", tokens[6].getContent());

        Assert.assertThat(tokens[7], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[7].getContent());
    }

    @Test public void testAttributeToken3()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub foo : attr(\n(\n)\n);");

        Assert.assertEquals(8, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("foo", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[3].getContent());

        Assert.assertThat(tokens[4], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(":", tokens[4].getContent());

        Assert.assertThat(tokens[5], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[5].getContent());

        Assert.assertThat(tokens[6], IsInstanceOf.instanceOf(AttributeToken.class));
        Assert.assertEquals("attr(\n(\n)\n)", tokens[6].getContent());

        Assert.assertThat(tokens[7], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[7].getContent());
    }

    @Test public void testAttributeToken4()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub foo : attr ();");

        Assert.assertEquals(11, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("foo", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[3].getContent());

        Assert.assertThat(tokens[4], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(":", tokens[4].getContent());

        Assert.assertThat(tokens[5], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[5].getContent());

        Assert.assertThat(tokens[6], IsInstanceOf.instanceOf(AttributeToken.class));
        Assert.assertEquals("attr", tokens[6].getContent());

        Assert.assertThat(tokens[7], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[7].getContent());

        Assert.assertThat(tokens[8], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals("(", tokens[8].getContent());

        Assert.assertThat(tokens[9], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(")", tokens[9].getContent());

        Assert.assertThat(tokens[10], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[10].getContent());
    }

    @Test public void testAttributeToken5()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub foo : __END__();");

        Assert.assertEquals(8, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("sub", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[1].getContent());

        Assert.assertThat(tokens[2], IsInstanceOf.instanceOf(WordToken.class));
        Assert.assertEquals("foo", tokens[2].getContent());

        Assert.assertThat(tokens[3], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[3].getContent());

        Assert.assertThat(tokens[4], IsInstanceOf.instanceOf(OperatorToken.class));
        Assert.assertEquals(":", tokens[4].getContent());

        Assert.assertThat(tokens[5], IsInstanceOf.instanceOf(WhitespaceToken.class));
        Assert.assertEquals(" ", tokens[5].getContent());

        Assert.assertThat(tokens[6], IsInstanceOf.instanceOf(AttributeToken.class));
        Assert.assertEquals("__END__()", tokens[6].getContent());

        Assert.assertThat(tokens[7], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[7].getContent());
    }

    @Test public void testIdentParams1()
    {
        Document document = TestCaseProvider.parseSnippet("sub foo : attr");
        List<AttributeToken> list = document.find(AttributeToken.class);

        Assert.assertEquals(1, list.size());
        Assert.assertEquals("attr", list.get(0).getIdentifier());
        Assert.assertEquals(null, list.get(0).getParameters());
    }

    @Test public void testIdentParams2()
    {
        Document document = TestCaseProvider.parseSnippet("sub foo : attr();");
        List<AttributeToken> list = document.find(AttributeToken.class);

        Assert.assertEquals(1, list.size());
        Assert.assertEquals("attr", list.get(0).getIdentifier());
        Assert.assertEquals("", list.get(0).getParameters());
    }

    @Test public void testIdentParams3()
    {
        Document document = TestCaseProvider.parseSnippet("sub foo : attr(1);");
        List<AttributeToken> list = document.find(AttributeToken.class);

        Assert.assertEquals(1, list.size());
        Assert.assertEquals("attr", list.get(0).getIdentifier());
        Assert.assertEquals("1", list.get(0).getParameters());
    }
}
