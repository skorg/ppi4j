#!/usr/bin/perl
use strict;

use File::Spec qw();

use PPI::Document;

my $path = $ARGV[0];
my $type = $ARGV[1];

if (!-e $path)
{
    die "unable to find [$path]";
}

my $pdom = PPI::Document->new($path);
my $list = $pdom->find($type);

printf "count: %s\n", scalar(@{$list});
