package org.scriptkitty.ppi4j.token;

import java.util.ArrayList;
import java.util.List;


public interface SectionedToken
{
    //~ Methods

    /**
     * add a modifier
     *
     * @param  modifier modifier
     *
     * @throws UnsupportedOperationException if the token does not support modifiers
     */
    void addModifier(String modifier) throws UnsupportedOperationException;

    /**
     * add a section
     *
     * @param  section section
     *
     * @throws UnsupportedOperationException if the token does not support sections
     */
    void addSection(Section section) throws UnsupportedOperationException;

    //~ Inner Classes

    public final class Contents implements SectionedToken
    {
        // TODO: provide methods to get delimiters

        private List<String> modifiers = new ArrayList<>();

        private List<Section> sections = new ArrayList<>();

        @Override public void addModifier(String modifier)
        {
            //J-
            /*
             * PPI stores regexp modifiers in a hash, w/ the character in lowercase form, ie:
             *   $self->{modifiers}->{lc $char} = 1;
             *
             * i'm not really sure what the reason behind that is b/c it doesn't give the user a valid way to
             * determine if the modifier is valid. it also does not allow the user to determine if the modifier
             * has been specified twice, hence the decision here to use an array.
             */
            //J+
            modifiers.add(modifier);
        }

        @Override public void addSection(Section section)
        {
            sections.add(section);
        }

        public List<String> getModifiers()
        {
            return modifiers;
        }

        public Section getSection(int index)
        {
            try
            {
                return sections.get(index);
            }
            catch (IndexOutOfBoundsException e)
            {
                return null;
            }
        }

        public boolean hasSections()
        {
            return !sections.isEmpty();
        }
    }

    public class Section
    {
        /** indicates if the section ends with its delimiter */
        public boolean complete;

        public int position;

        public int size;

        public String type;
    }
}
