package org.scriptkitty.ppi4j;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.token.NumberToken;
import org.scriptkitty.ppi4j.token.SymbolToken;
import org.scriptkitty.ppi4j.token.WordToken;
import org.scriptkitty.ppi4j.util.TestCaseProvider;

import java.util.List;


public class TestElement
{
    //~ Methods

    @Test public void isHashKey1()
    {
        Document document = TestCaseProvider.parseSnippet("$hash{foo} = bar");

        List<WordToken> words = document.find(WordToken.class);
        Assert.assertEquals(2, words.size());

        Assert.assertTrue(words.get(0).isHashKey());
        Assert.assertFalse(words.get(1).isHashKey());
    }

    @Test public void isHashKey2()
    {
        Document document = TestCaseProvider.parseSnippet("$hash{foo()} = bar");

        List<WordToken> words = document.find(WordToken.class);
        Assert.assertEquals(2, words.size());

        Assert.assertFalse(words.get(0).isHashKey());
        Assert.assertFalse(words.get(1).isHashKey());
    }

    @Test public void isHashKey3()
    {
        Document document = TestCaseProvider.parseSnippet("$hash{1} = bar");

        List<WordToken> words = document.find(WordToken.class);
        Assert.assertEquals(1, words.size());
        Assert.assertFalse(words.get(0).isHashKey());

        List<NumberToken> numbers = document.find(NumberToken.class);
        Assert.assertEquals(1, numbers.size());
        Assert.assertTrue(numbers.get(0).isHashKey());
    }

    @Test public void isHashKey4()
    {
        Document document = TestCaseProvider.parseSnippet("%hash = (foo => bar);");

        List<WordToken> words = document.find(WordToken.class);
        Assert.assertEquals(2, words.size());

        Assert.assertTrue(words.get(0).isHashKey());
        Assert.assertFalse(words.get(1).isHashKey());
    }

    @Test public void testIsAncestorOf()
    {
        Document document = TestCaseProvider.parseSnippet("( [ thingy ] ); $blarg = 1");
        Assert.assertTrue(document.isDescendantOf(document));

        List<WordToken> words = document.find(WordToken.class);
        Assert.assertEquals(1, words.size());

        List<SymbolToken> symbols = document.find(SymbolToken.class);
        Assert.assertEquals(1, symbols.size());

        WordToken word = words.get(0);
        Assert.assertTrue(word.isAncestorOf(word));
        Assert.assertTrue(document.isAncestorOf(word));
        Assert.assertFalse(word.isAncestorOf(document));

        SymbolToken symbol = symbols.get(0);
        Assert.assertFalse(word.isAncestorOf(symbol));
        Assert.assertFalse(symbol.isAncestorOf(word));
    }

    @Test public void testIsDescendantOf()
    {
        Document document = TestCaseProvider.parseSnippet("( [ thingy ] ); $blarg = 1");
        Assert.assertTrue(document.isDescendantOf(document));

        List<WordToken> words = document.find(WordToken.class);
        Assert.assertEquals(1, words.size());

        List<SymbolToken> symbols = document.find(SymbolToken.class);
        Assert.assertEquals(1, symbols.size());

        WordToken word = words.get(0);
        Assert.assertTrue(word.isDescendantOf(word));
        Assert.assertTrue(word.isDescendantOf(document));
        Assert.assertFalse(document.isDescendantOf(word));

        SymbolToken symbol = symbols.get(0);
        Assert.assertFalse(word.isDescendantOf(symbol));
        Assert.assertFalse(symbol.isDescendantOf(word));
    }
}
