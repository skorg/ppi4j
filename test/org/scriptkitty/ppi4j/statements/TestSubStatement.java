package org.scriptkitty.ppi4j.statements;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Document;
import org.scriptkitty.ppi4j.Statement;
import org.scriptkitty.ppi4j.statement.SubStatement;
import org.scriptkitty.ppi4j.util.TestCaseProvider;

import java.util.List;


public class TestSubStatement
{
    //~ Methods

    @Test public void testIsReserved1()
    {
        Document document = TestCaseProvider.parseSnippet("sub FOO { }");
        List<SubStatement> elements = document.find(SubStatement.class);

        Assert.assertEquals(1, elements.size());

        Assert.assertTrue(elements.get(0).isReserved());
    }

    @Test public void testIsReserved2()
    {
        Document document = TestCaseProvider.parseSnippet("sub foo{ }");
        List<SubStatement> elements = document.find(SubStatement.class);

        Assert.assertEquals(1, elements.size());

        Assert.assertFalse(elements.get(0).isReserved());
    }

    @Test public void testStatement1()
    {
        Document document = TestCaseProvider.parseSnippet("sub");
        List<SubStatement> elements = document.find(SubStatement.class);

        Assert.assertEquals(1, elements.size());
    }

    @Test public void testStatement2()
    {
        Document document = TestCaseProvider.parseSnippet("sub {");
        List<Statement> elements = document.find(Statement.class);

        Assert.assertEquals(1, elements.size());
    }

    @Test public void testStatement3()
    {
        Document document = TestCaseProvider.parseSnippet("sub foo;");
        List<SubStatement> elements = document.find(SubStatement.class);

        Assert.assertEquals(1, elements.size());

        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());
        Assert.assertTrue(elements.get(0).isForward());
    }

    @Test public void testStatement4()
    {
        Document document = TestCaseProvider.parseSnippet("sub foo {");
        List<SubStatement> elements = document.find(SubStatement.class);

        Assert.assertEquals(1, elements.size());

        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());
    }

    @Test public void testStatement5()
    {
        Document document = TestCaseProvider.parseSnippet("sub foo { }");
        List<SubStatement> elements = document.find(SubStatement.class);

        Assert.assertEquals(1, elements.size());

        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());
        Assert.assertTrue(elements.get(0).getBody() != null);
    }
}
