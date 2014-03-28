package org.scriptkitty.ppi4j.statements;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Document;
import org.scriptkitty.ppi4j.statement.ExpressionStatement;
import org.scriptkitty.ppi4j.statement.IncludeStatement;
import org.scriptkitty.ppi4j.util.TestCaseProvider;


public class TestExpressionStatement
{
    //~ Methods

    @Test public void testStatement1()
    {
        Document document = TestCaseProvider.parseSnippet("$a{lc($b)}");
        List<IncludeStatement> elements = document.find(ExpressionStatement.class);

        Assert.assertEquals(2, elements.size());
    }
}
