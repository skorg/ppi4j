#!/usr/bin/perl
use strict;

use File::Spec qw();

my $path = $ARGV[0];

if (!-e $path)
{
    die "unable to find [$path]";
}

my $pdom = Document->new($path);
bless $pdom, 'Document';

my $dumper = Dumper->new($pdom, 
    locations   => 0,
    comments    => 0,
    whitespace  => 0,
    significant => 1,
);
bless $dumper, 'Dumper';

printf "%s\n", ('-' x 28);
$dumper->print;

package Document;
use base qw(PPI::Document);

use strict;

sub index_locations
{
    my $self   = shift;
    my @Tokens = $self->tokens;

    # Whenever we hit a heredoc we will need to increment by
    # the number of lines in it's content section when when we
    # encounter the next token with a newline in it.
    my $heredoc = 0;

    # Find the first Token without a location
    my ($first, $location) = ();
    foreach (0 .. $#Tokens)
    {
        my $Token = $Tokens[$_];
        next if $Token->{_location};

        # Found the first Token without a location
        # Calculate the new location if needed.
        $location =
            $_
          ? $self->_add_location($location, $Tokens[$_ - 1], \$heredoc)
          : [1, 1, 1, 0];
        $first = $_;
        last;
    }

    # Calculate locations for the rest
    foreach ($first .. $#Tokens)
    {
        my $Token = $Tokens[$_];
        $Token->{_location} = $location;
        $location = $self->_add_location($location, $Token, \$heredoc);

        # Add any here-doc lines to the counter
        if ($Token->isa('PPI::Token::HereDoc'))
        {
            $heredoc += $Token->heredoc + 1;
        }
    }

    1;
}

sub _add_location
{
    my ($self, $start, $Token, $heredoc) = @_;
    my $content = $Token->{content};

    $self->{offset} += length($content);

    # Does the content contain any newlines
    my $newlines = () = $content =~ /\n/g;
    unless ($newlines)
    {
        # Handle the simple case
        return [
            $start->[0],
            $start->[1] + length($content),
            $start->[2] + $self->_visual_length($content, $start->[2]),
            $self->{offset},
        ];
    }

    # This is the more complex case where we hit or
    # span a newline boundary.
    my $location = [$start->[0] + $newlines, 1, 1, $self->{offset}];
    if ($heredoc and $$heredoc)
    {
        $location->[0] += $$heredoc;
        $$heredoc = 0;
    }

    # Does the token have additional characters
    # after their last newline.
    if ($content =~ /\n([^\n]+?)\z/)
    {
        $location->[1] += length($1);
        $location->[2] += $self->_visual_length($1, $location->[2]);
        $location->[3] += length($1);
        $location->[4] = $self->{offset},
    }

    $location;
}

package Dumper;
use base qw(PPI::Dumper);

sub new
{
    my $self = shift;
    my ($dom, %args) = @_;
    
    $self = $self->SUPER::new($dom, %args);
    
    $self->{comments}    = $args{comments};
    $self->{significant} = $args{significant};
   
    return $self;
}

sub _element_string {
    my $self    = ref $_[0] ? shift : shift->new(shift);
    my $Element = $_[0]->isa('PPI::Element') ? shift : $self->{root};
    my $indent  = shift || '';
    my $string  = '';

    # Add the memory location
    if ( $self->{display}->{memaddr} ) {
        $string .= $Element->refaddr . '  ';
    }
        # Add the location if such exists
    if ( $self->{display}->{locations} ) {
        my $loc_string;
        if ( $Element->isa('PPI::Token')) {
            if (!$Element->significant && $self->{significant})
            {
                return;
            }

            if ($Element->isa('PPI::Token::Pod') && !$self->{comments})
            {
                return;
            }

            if ($Element->isa('PPI::Token::Comment') && !$self->{comments})
            {
                return;
            }

            my $location = $Element->location;
            if ($location) {
                $loc_string = sprintf("[ % 4d, % 3d, % 3d, % 3d ] ", @$location);
            }
        }
        # Output location or pad with 20 spaces
        $string .= $loc_string || " " x 22;
    }
        
    # Add the indent
    if ( $self->{display}->{indent} ) {
        $string .= $indent;
    }

    # Add the class name
    if ( $self->{display}->{class} ) {
        $string .= ref $Element;
    }

    if ( $Element->isa('PPI::Token') ) {
        # Add the content
        if ( $self->{display}->{content} ) {
            my $content = $Element->content;
            $content =~ s/\n/\\n/g;
            $content =~ s/\t/\\t/g;
            $string .= "  \t'$content'";
        }
    } elsif ( $Element->isa('PPI::Structure') ) {
        # Add the content
        if ( $self->{display}->{content} ) {
            my $start = $Element->start
                ? $Element->start->content
                : '???';
            my $finish = $Element->finish
                ? $Element->finish->content
                : '???';
            $string .= "  \t$start ... $finish";
        }
    }
    
    $string;
}
