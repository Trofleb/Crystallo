#!/usr/bin/perl

$dir = "savedir";
$head = "";
if ("$ENV{'QUERY_STRING'}" =~ /^favorites$/) {
    $dir = "favdir";
    $head="favorites";
}
$columns=4;

print "Content-type: text/html\n\n";
print "<title>Escher Web Sketch Saved Images</title> 
<body bgcolor=#ffffff>
<h2>Archive of saved images from the <a href=\"http://marie.epfl.ch/escher/\">Escher Web Sketch</a> applet.</h2>
Click on an image to see it patterned as a full page background.\n<p>

<b>Note</b>: It takes a few minutes for newly submitted images to
appear here, so if you just submitted one, you may have to hit reload
in a few minutes to see it appear.<br>\n";

print "<p>I also scan the submitted entries occasionally and pick out my <a href=\"scanSaved.cgi?favorites\">favorites</a>" if ($head eq "");
print "<br><br>\n";

@files=split(/[ \n]/,`ls $dir/index_*.gif`);
$count = 0;
print "<table border=1>\n";
foreach $i (sort {$a =~ /index_([0-9]+).gif/;$a1 = $1;
		  $b =~ /index_([0-9]+).gif/;$b1 = $1;
	          $a1 <=> $b1;} @files) {
    chomp($i);
    if ($count == 0) {
	print "<tr>\n";
    }
    $sfile = $i;
    $sfile =~ s/index_/save_/;
    $_ = $i;
    /index_([0-9]+).gif/;
    $num=$1;
    print "<td>\n";
    %data = ();
    if (-f "$dir/index_$num.data") {
	open(I,"$dir/index_$num.data");
	while (<I>) {
	    @a = split;
	    $data{$a[0]} = $a[1];
	}
	close(I);
  }
  	#if ($data{'type'} eq 'p2' || $data{'type'} eq 'p1') {
			print "<a href=\"showSavedEx.cgi?img=$num&fav=$head"."x&off=".$data{'xoff'}."\">";
  	#}
  	#else {
		#	print "<a href=\"showSaved.cgi?$head$num\">";
		#}
    print "<img border=1 src=\"$i\"";
    print " width=" . $data{'x'} if (defined($data{'x'}));
    print " height=" . $data{'y'} if (defined($data{'y'}));
    print "></a>\n";
    print "<br>" . $data{'type'} . "</br>\n" if defined($data{'type'});
    print "</td>\n";
    $count++;
    if ($count == $columns) {
	print "</tr>\n";
	$count = 0;
    }
}
print "</table>\n";
