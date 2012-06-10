package org.scriptkitty.ppi4j.statements;

import org.hamcrest.core.IsInstanceOf;

import org.junit.Assert;
import org.junit.Test;

import org.scriptkitty.ppi4j.Document;
import org.scriptkitty.ppi4j.Structure;
import org.scriptkitty.ppi4j.statement.CompoundStatement;
import org.scriptkitty.ppi4j.structure.BlockStructure;
import org.scriptkitty.ppi4j.structure.ConditionStructure;
import org.scriptkitty.ppi4j.structure.ForLoopStructure;
import org.scriptkitty.ppi4j.structure.ListStructure;
import org.scriptkitty.ppi4j.util.TestCaseProvider;

import java.util.List;


public class TestCompoundStatement
{
    //~ Methods

    @Test public void testStatement1()
    {
        Document document = TestCaseProvider.parseSnippet("while (1) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ConditionStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement10()
    {
        Document document = TestCaseProvider.parseSnippet("foreach $x (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement11()
    {
        Document document = TestCaseProvider.parseSnippet("for my $x (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(5, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement12()
    {
        Document document = TestCaseProvider.parseSnippet("foreach my $x (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(5, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement13()
    {
        Document document = TestCaseProvider.parseSnippet("for state $x (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(5, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement14()
    {
        Document document = TestCaseProvider.parseSnippet("foreach state $x (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(5, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement15()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: for (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement16()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: foreach (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement17()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: for $x (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(5, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement18()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: foreach $x (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(5, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement19()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: for my $x (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(6, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement2()
    {
        Document document = TestCaseProvider.parseSnippet("until (1) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ConditionStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement20()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: foreach my $x (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(6, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement21()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: for state $x (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(6, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement22()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: foreach state $x (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(6, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement23()
    {
        Document document = TestCaseProvider.parseSnippet("for qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement24()
    {
        Document document = TestCaseProvider.parseSnippet("foreach qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement25()
    {
        Document document = TestCaseProvider.parseSnippet("for $x qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement26()
    {
        Document document = TestCaseProvider.parseSnippet("foreach $x qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement27()
    {
        Document document = TestCaseProvider.parseSnippet("for my $x qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(5, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement28()
    {
        Document document = TestCaseProvider.parseSnippet("foreach my $x qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(5, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement29()
    {
        Document document = TestCaseProvider.parseSnippet("for state $x qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(5, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement3()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: while (1) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ConditionStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement30()
    {
        Document document = TestCaseProvider.parseSnippet("foreach state $x qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(5, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement31()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: for qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement32()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: foreach qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement33()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: for $x qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(5, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement34()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: foreach $x qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(5, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement35()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: for my $x qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(6, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement36()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: foreach my $x qw{foo} { }");
        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(6, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement37()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: for state $x qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(6, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement38()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: foreach state $x qw{foo} { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(6, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(1, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement39()
    {
        Document document = TestCaseProvider.parseSnippet("for ( ; ; ) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ForLoopStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement4()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: until (1) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ConditionStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement40()
    {
        Document document = TestCaseProvider.parseSnippet("foreach ( ; ; ) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ForLoopStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement41()
    {
        Document document = TestCaseProvider.parseSnippet("for ($x = 0; $x < 1; $x++) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ForLoopStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement42()
    {
        Document document = TestCaseProvider.parseSnippet("foreach ($x = 0; $x < 1; $x++) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ForLoopStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement43()
    {
        Document document = TestCaseProvider.parseSnippet("for (my $x = 0; $x < 1; $x++) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ForLoopStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement44()
    {
        Document document = TestCaseProvider.parseSnippet("foreach (my $x = 0; $x < 1; $x++) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ForLoopStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement45()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: for ( ; ; ) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ForLoopStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement46()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: foreach ( ; ; ) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ForLoopStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement47()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: for ($x = 0; $x < 1; $x++) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ForLoopStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement48()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: foreach ($x = 0; $x < 1; $x++) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ForLoopStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement49()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: for (my $x = 0; $x < 1; $x++) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ForLoopStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement5()
    {
        Document document = TestCaseProvider.parseSnippet("if (1) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ConditionStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement50()
    {
        Document document = TestCaseProvider.parseSnippet("LABEL: foreach (my $x = 0; $x < 1; $x++) { }");

        List<CompoundStatement> compound = document.find(CompoundStatement.class);

        Assert.assertEquals(1, compound.size());
        Assert.assertEquals(4, compound.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ForLoopStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement6()
    {
        Document document = TestCaseProvider.parseSnippet("unless (1) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ConditionStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement7()
    {
        Document document = TestCaseProvider.parseSnippet("for (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement8()
    {
        Document document = TestCaseProvider.parseSnippet("foreach (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(3, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }

    @Test public void testStatement9()
    {
        Document document = TestCaseProvider.parseSnippet("for $x (@foo) { }");

        List<CompoundStatement> elements = document.find(CompoundStatement.class);

        Assert.assertEquals(1, elements.size());
        Assert.assertEquals(4, elements.get(0).getSigChidrenCount());

        List<Structure> struct = document.find(Structure.class, true);

        Assert.assertEquals(2, struct.size());
        Assert.assertThat(struct.get(0), IsInstanceOf.instanceOf(ListStructure.class));
        Assert.assertThat(struct.get(1), IsInstanceOf.instanceOf(BlockStructure.class));
    }
}
