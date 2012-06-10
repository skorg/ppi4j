#!/usr/bin/perl
use strict;

use Data::Dumper;

use File::Spec qw();

use PPI::Tokenizer;

my $path = $ARGV[0];

if (!-e $path)
{
    die "unable to find [$path]";
}

my $tokenizer = PPI::Tokenizer->new($path);

&all($tokenizer);
#&each($tokenizer);

sub all
{
    my ($tokenizer) = @_;
    
    foreach my $token (@{$tokenizer->all_tokens})
    {                
        &print($token);
    }
}

sub each
{
    my ($tokenizer) = @_;
    
    while (my $token = $tokenizer->get_token)
    {
        &print($token);
    }
}

sub print
{
    my ($token) = @_;
    
    my $text = $token;    
    if ($token->isa('PPI::Token::HereDoc'))
    {
        $text .= sprintf " '%s'", join("", $token->heredoc);
    }

    $text =~ s/\n/\\n/g;
    printf "%s \t'%s'\n", ref($token), $text; 
#    print Dumper($token);
}
