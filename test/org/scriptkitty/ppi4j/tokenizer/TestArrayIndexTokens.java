package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.ArrayIndexToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestArrayIndexTokens
{
    //~ Methods

    @Test public void testArrayIndex1()
    {
        Token token = TestCaseProvider.getToken("$#a");

        Assert.assertThat(token, IsInstanceOf.instanceOf(ArrayIndexToken.class));
        Assert.assertEquals("$#a", token.getContent());
    }

    @Test public void testArrayIndex2()
    {
        Token token = TestCaseProvider.getToken("$#_");

        Assert.assertThat(token, IsInstanceOf.instanceOf(ArrayIndexToken.class));
        Assert.assertEquals("$#_", token.getContent());
    }

    @Test public void testArrayIndex3()
    {
        // this is invalid syntax, however PPI still considers it to be an array index
        Token token = TestCaseProvider.getToken("$#1");

        Assert.assertThat(token, IsInstanceOf.instanceOf(ArrayIndexToken.class));
        Assert.assertEquals("$#1", token.getContent());
    }
}
