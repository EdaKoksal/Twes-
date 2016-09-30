#!/usr/bin/perl
#Server Section
$|=1;

open (DEBUG, '>>/tmp/progLog.log');
autoflush DEBUG 1;

while (<>) {
        chomp $_;
	
	if($_ =~/(EDA)/)
	{	
		my @names = split('EDA',$_);		
		print "http://127.0.0.1/consolidation/$names[1]\n";		
	}	
	else 
	{
                print "$_\n";
   }
	
	
}

	
	
	
