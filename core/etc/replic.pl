#!/usr/bin/perl -w
#
# Written by Brian Cunnie
# Available under the standard JDOM License (see LICENSE.txt)
#
# Description
#
#    Replaces the first comment (as delimited by "/* ... */",
#    can be multi-line) in a series of files with the contents
#    of a special file (usually a license).
#
# Usage
#
#    replic.pl license-file target-file [ target-file ... ]
#
# Example
#
#    replic.pl LICENSE.txt `find . -name \*.java`
#
# Warnings
#
#    Remember to put the comment-characters (i.e. "/*" & "*/")
#    in your license file--replic.pl won't do it for you!
#
#    Uses a non-greedy algorithm (i.e. it will match
#    against the first "*/" it finds), for example,
#    "/* this will be replaced */ but this will not */" )
# 

#usual checks
die "Need Perl5!" if ( $] < 5 );
die "Need license-file & target-file" if ( @ARGV < 1 );
die "Need at least one target-file" if ( @ARGV < 2 );
die "Can't read $ARGV[0]" if ( ! open(LICENSE,$ARGV[0]) );

# slurp in license; make one long string
while(<LICENSE>) {
    $license .= $_;
}

shift(@ARGV);
foreach $argv (@ARGV) {
    if ( ! open(TARGET,"<$argv") ) {
        warn "Couldn't open $argv for reading!";
    } else {
        #reset $target
        local($target);
        #slurp in TARGET; make one long string
        while(<TARGET>) {
            $target .= $_;
        }
        # now let's open target for writing
        close(TARGET);
        if ( ! open(TARGET,">$argv") ) {
            warn "Couldn't open $argv for writing!";
        } else {
            # this is the heart of the program: the search-replace:
            # note the ".*?" to make it a non-greedy match (thanks jason!)
            # note the "~s" to make it match across multiple lines.
            $target =~ s~/\*.*?\*/\n?~$license$1~s;
            print(TARGET $target);
        }
    }
}
