package org.scriptkitty.ppi4j.tokenizer;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.token.StructureToken;
import org.scriptkitty.ppi4j.token.regexp.RESubstituteToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestRESubstituteTokens
{
    //~ Methods

    @Test public void testSubstitute1()
    {
        Token token = TestCaseProvider.getToken("s");

        Assert.assertThat(token, IsInstanceOf.instanceOf(RESubstituteToken.class));
        Assert.assertEquals("s", token.getContent());
    }

    @Test public void testSubstitute2()
    {
        Token token = TestCaseProvider.getToken("s[");

        Assert.assertThat(token, IsInstanceOf.instanceOf(RESubstituteToken.class));
        Assert.assertEquals("s[", token.getContent());
    }

    @Test public void testSubstitute3()
    {
        Token[] tokens = TestCaseProvider.getTokens("s/a/b/msixpgcadlu;");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(RESubstituteToken.class));
        Assert.assertEquals("s/a/b/msixpgcadlu", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[1].getContent());
    }

    @Test public void testSubstitute4()
    {
        Token[] tokens = TestCaseProvider.getTokens("s (a)/b/msixpgcadlu;");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(RESubstituteToken.class));
        Assert.assertEquals("s (a)/b/msixpgcadlu", tokens[0].getContent());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[1].getContent());
    }

    @Test public void testSubstitute5()
    {
        Token[] tokens = TestCaseProvider.getTokens("s (a) [b]msixpgcadlu;");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(RESubstituteToken.class));
        Assert.assertEquals("s (a) [b]msixpgcadlu", tokens[0].getContent());

        Assert.assertEquals("a", ((RESubstituteToken) tokens[0]).getMatch());
        Assert.assertTrue(((RESubstituteToken) tokens[0]).isMatchComplete());

        Assert.assertEquals("b", ((RESubstituteToken) tokens[0]).getSubstitution());
        Assert.assertTrue(((RESubstituteToken) tokens[0]).isSubstitutionComplete());

        Assert.assertTrue(((RESubstituteToken) tokens[0]).hasModifiers());
        Assert.assertEquals(11, ((RESubstituteToken) tokens[0]).getModifiers().size());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[1].getContent());
    }

    @Test public void testSubstitute6()
    {
        Token[] tokens = TestCaseProvider.getTokens("s{a}{if($1){foo()}};");

        Assert.assertEquals(2, tokens.length);

        Assert.assertThat(tokens[0], IsInstanceOf.instanceOf(RESubstituteToken.class));
        Assert.assertEquals("s{a}{if($1){foo()}}", tokens[0].getContent());

        Assert.assertEquals("a", ((RESubstituteToken) tokens[0]).getMatch());
        Assert.assertTrue(((RESubstituteToken) tokens[0]).isMatchComplete());

        Assert.assertEquals("if($1){foo()}", ((RESubstituteToken) tokens[0]).getSubstitution());
        Assert.assertTrue(((RESubstituteToken) tokens[0]).isSubstitutionComplete());

        Assert.assertThat(tokens[1], IsInstanceOf.instanceOf(StructureToken.class));
        Assert.assertEquals(";", tokens[1].getContent());
    }
}
