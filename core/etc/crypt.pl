#!/usr/bin/perl
#
# Written by Karl Fogel
# Available under the standard JDOM License (see LICENSE.txt)
#
# Description
#
#    Generates a password hash from a plaintext password, to be used 
#    by new committers who send the hash to the CVS maintainer.
#
# Usage
#
#    crypt.pl plaintextpassword
#
# Example
#
#    crypt.pl try2gu3ss


srand (time());
my $randletter = "(int (rand (26)) + (int (rand (1) + .5) % 2 ? 65 : 97))";
my $salt = sprintf ("%c%c", eval $randletter, eval $randletter);
my $plaintext = shift;
my $crypttext = crypt ($plaintext, $salt);

print "${crypttext}\n";
