package org.scriptkitty.ppi4j.statements;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Document;
import org.scriptkitty.ppi4j.Statement;
import org.scriptkitty.ppi4j.statement.IncludeStatement;
import org.scriptkitty.ppi4j.util.TestCaseProvider;

import java.util.List;


public class TestIncludeStatement
{
    //~ Methods

    @Test public void testArguments1()
    {
        Document document = TestCaseProvider.parseSnippet("use Foo qw< bar >, \"baz\";");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getArguments().size());

        Assert.assertTrue(elements.get(0).getModuleVersion().isNull());
    }

    @Test public void testArguments2()
    {
        Document document = TestCaseProvider.parseSnippet("use Test::More tests => 5 * 9");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(5, elements.get(0).getArguments().size());

        Assert.assertTrue(elements.get(0).getModuleVersion().isNull());
    }

    @Test public void testModuleVersion1()
    {
        Document document = TestCaseProvider.parseSnippet("use Integer::Version 1;");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertTrue(elements.get(0).getArguments().isEmpty());

        Assert.assertFalse(elements.get(0).getModuleVersion().isNull());
        Assert.assertEquals("1", elements.get(0).getModuleVersion().getContent());
    }

    @Test public void testModuleVersion2()
    {
        Document document = TestCaseProvider.parseSnippet("use Float::Version 1.5;");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertTrue(elements.get(0).getArguments().isEmpty());

        Assert.assertFalse(elements.get(0).getModuleVersion().isNull());
        Assert.assertEquals("1.5", elements.get(0).getModuleVersion().getContent());
    }

    @Test public void testModuleVersion3()
    {
        Document document = TestCaseProvider.parseSnippet("use Version::With::Argument 1 2;");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(1, elements.get(0).getArguments().size());

        Assert.assertFalse(elements.get(0).getModuleVersion().isNull());
        Assert.assertEquals("1", elements.get(0).getModuleVersion().getContent());
    }

    @Test public void testModuleVersion4()
    {
        Document document = TestCaseProvider.parseSnippet("use No::Version;");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertTrue(elements.get(0).getModuleVersion().isNull());
    }

    @Test public void testModuleVersion5()
    {
        Document document = TestCaseProvider.parseSnippet("use No::Version::With::Argument 'x';");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(1, elements.get(0).getArguments().size());

        Assert.assertTrue(elements.get(0).getModuleVersion().isNull());
    }

    @Test public void testModuleVersion6()
    {
        Document document = TestCaseProvider.parseSnippet("use No::Version::With::Arguments 1, 2;");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getArguments().size());

        Assert.assertTrue(elements.get(0).getModuleVersion().isNull());
    }

    @Test public void testStatement1()
    {
        Document document = TestCaseProvider.parseSnippet("use utf8;");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());

        Assert.assertTrue(elements.get(0).isPragma());
        Assert.assertFalse(elements.get(0).isVersion());
        Assert.assertEquals(Statement.Type.USE, elements.get(0).getType());
    }

    @Test public void testStatement2()
    {
        Document document = TestCaseProvider.parseSnippet("no warnings;");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());

        Assert.assertTrue(elements.get(0).isPragma());
        Assert.assertFalse(elements.get(0).isVersion());
        Assert.assertEquals(Statement.Type.NO, elements.get(0).getType());
    }

    @Test public void testStatement3()
    {
        Document document = TestCaseProvider.parseSnippet("require Module;");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());

        Assert.assertFalse(elements.get(0).isPragma());
        Assert.assertFalse(elements.get(0).isVersion());
        Assert.assertEquals(Statement.Type.REQUIRE, elements.get(0).getType());
    }

    @Test public void testStatement4()
    {
        Document document = TestCaseProvider.parseSnippet("use Foo;");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());

        Assert.assertFalse(elements.get(0).isPragma());
        Assert.assertFalse(elements.get(0).isVersion());
        Assert.assertEquals(Statement.Type.USE, elements.get(0).getType());
    }

    @Test public void testStatement5()
    {
        Document document = TestCaseProvider.parseSnippet("use 5.6.1;");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());

        Assert.assertTrue(elements.get(0).isVersion());
        Assert.assertFalse(elements.get(0).isPragma());
        Assert.assertTrue(elements.get(0).getModuleVersion().isNull());
        Assert.assertEquals(Statement.Type.USE, elements.get(0).getType());
    }

    @Test public void testStatement6()
    {
        Document document = TestCaseProvider.parseSnippet("use v5.6.1;");
        List<IncludeStatement> elements = document.find(IncludeStatement.class);

        Assert.assertEquals(1, elements.size());

        Assert.assertTrue(elements.get(0).isVersion());
        Assert.assertFalse(elements.get(0).isPragma());
        Assert.assertEquals(Statement.Type.USE, elements.get(0).getType());
    }
}
