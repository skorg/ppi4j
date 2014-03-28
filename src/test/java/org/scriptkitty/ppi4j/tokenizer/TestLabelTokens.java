package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.LabelToken;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestLabelTokens
{
    //~ Methods

    @Test public void testLabel1()
    {
        Token token = TestCaseProvider.getToken("LABEL:");

        Assert.assertThat(token, IsInstanceOf.instanceOf(LabelToken.class));
        Assert.assertEquals("LABEL:", token.getContent());
    }

    @Test public void testLabel2()
    {
        Token token = TestCaseProvider.getToken("LABEL :");

        Assert.assertThat(token, IsInstanceOf.instanceOf(LabelToken.class));
        Assert.assertEquals("LABEL :", token.getContent());
    }

    @Test public void testLabel3()
    {
        Token token = TestCaseProvider.getToken("sub :");

        Assert.assertThat(token, IsInstanceOf.instanceOf(LabelToken.class));
        Assert.assertEquals("sub :", token.getContent());
    }

    @Test public void testLabel4()
    {
        Token[] tokens = TestCaseProvider.getTokens("sub :;");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(LabelToken.class));
        Assert.assertEquals("sub :", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[1].getContent());
    }

}
