package org.scriptkitty.ppi4j.util;

import org.scriptkitty.ppi4j.Document;
import org.scriptkitty.ppi4j.Token;
import org.scriptkitty.ppi4j.exception.TokenizerException;
import org.scriptkitty.ppi4j.parser.DefaultTokenProvider;
import org.scriptkitty.ppi4j.parser.Parser;
import org.scriptkitty.ppi4j.parser.ParserFactory;
import org.scriptkitty.ppi4j.tokenizer.Tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;


/**
 */
public class TestCaseProvider
{
    //~ Static fields/initializers

    private static String SEPARATOR = File.separator;

    //~ Methods

    public static String getContents(String filePart)
    {
        String path = SEPARATOR + "resources" + SEPARATOR + filePart;
        StringBuffer buffer = new StringBuffer();

        InputStream stream = TestCaseProvider.class.getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        try
        {
            int c = 0;
            while ((c = reader.read()) != -1)
            {
                buffer.append((char) c);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            close(reader);
        }

        return buffer.toString();
    }

    public static Token getToken(String snippet)
    {
        try
        {
            Tokenizer tokenizer = new Tokenizer(snippet);
            return tokenizer.next();
        }
        catch (TokenizerException e)
        {
            System.out.println("line: " + e.getLineNumber());
            throw new RuntimeException(e);
        }
    }

    public static Token[] getTokens(String snippet)
    {
        List<Token> tokens = new ArrayList<Token>();
        Tokenizer tokenizer = new Tokenizer(snippet);

        try
        {
            Token token = tokenizer.next();
            while (!token.isEOF())
            {
                tokens.add(token);
                token = tokenizer.next();
            }

            return tokens.toArray(new Token[tokens.size()]);
        }
        catch (TokenizerException e)
        {
            throw new RuntimeException(e.getCause());
        }
    }

    public static Document parseFile(String filePart)
    {
        return parse(getContents(filePart), true);
    }

    public static Document parseSnippet(String source)
    {
        return parse(source, false);
    }

    private static void close(BufferedReader reader)
    {
        try
        {
            reader.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static Document parse(String source, boolean extend)
    {
        try
        {
            DefaultTokenProvider provider = new DefaultTokenProvider(source);

            Parser parser = ParserFactory.createParser(provider);
            return parser.parse();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
