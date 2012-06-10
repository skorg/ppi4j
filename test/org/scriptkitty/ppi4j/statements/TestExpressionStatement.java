package org.scriptkitty.ppi4j.statements;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Document;
import org.scriptkitty.ppi4j.statement.ExpressionStatement;
import org.scriptkitty.ppi4j.statement.IncludeStatement;
import org.scriptkitty.ppi4j.util.TestCaseProvider;

import java.util.List;


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
