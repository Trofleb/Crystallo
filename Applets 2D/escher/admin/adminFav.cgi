#!/usr/bin/perl

$dir = "../favdir";
$head = "";
$columns=1;

print "Content-type: text/html\n\n";
print "<html>
<head>
<title>Admin : FavDir</title>
<script type=\"text/javascript\">

window.onload=function() {
	var allDivs = document.body.getElementsByTagName(\"div\");
	for(i = 0; i < allDivs.length; i++){
	   if (allDivs(i).state!=null) {
			allDivs(i).onclick=swap;
	   }
	}
};

function swap() {
		if (this.state == \"blank\") {
			this.state = \"del\";
			this.children(1).src = \"delete.gif\";
		}
		else if (this.state == \"del\") {
			this.state = \"unfav\";
			this.children(1).src = \"unfav.gif\";
		}
		else if (this.state == \"unfav\") {
			this.state = \"blank\";
			this.children(1).src = \"blank.gif\";
		}
};

function apply() {
	var allDivs = document.body.getElementsByTagName(\"div\");
	var args = \"dooperations.cgi?\";
	var del=0;
	var fav=0;

	for(i = 0; i < allDivs.length; i++){
		 var idiv = allDivs(i);
	   if (idiv.state!=null && idiv.state==\"del\") {
	   	 del++;
		   var a = args+idiv.children(0).src;
		   var a1 = a.lastIndexOf(\"/\");
		   var a2 = a.lastIndexOf(\".\");
		   a = a.substring(a1+1, a2);
		   args = args+a+\"=v&\";
	   }
	   else if (idiv.state!=null && idiv.state==\"unfav\") {
	   	 fav++;
		   var a = args+idiv.children(0).src;
		   var a1 = a.lastIndexOf(\"/\");
		   var a2 = a.lastIndexOf(\".\");
		   a = a.substring(a1+1, a2);
		   args = args+a+\"=u&\";
	   }
	}
	var sure = confirm(\"You are about to delete \"+del+\" elements and move out \"+fav+\" elements from favorites. Are you sure?\");
	if (sure==true) {
		document.location.href=args;
	}
};

function reset() {
	var allDivs = document.body.getElementsByTagName(\"div\");
	for(i = 0; i < allDivs.length; i++){
	   if (allDivs(i).state!=null) {
			allDivs(i).state=\"blank\";
			allDivs(i).children(1).src=\"blank.gif\";
	   }
	}
};

</script>
</head>
<body>


<button onClick=\"apply()\"><strong>Apply</strong></button>
<button onClick=\"reset()\"><strong>Reset</strong></button>

<br>
<br>";

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
    print "<div state=\"blank\">";
    print "<img border=1 src=\"$i\"";
    print " width=" . $data{'x'} if (defined($data{'x'}));
    print " height=" . $data{'y'} if (defined($data{'y'}));
    print ">";
    print "<img style=\"position:absolute;left:0px\" src=\"blank.gif\">";
    print "</div>";
    print "<br>" . $data{'type'} . "</br>\n" if defined($data{'type'});
    print "</td>\n";
    $count++;
    if ($count == $columns) {
	print "</tr>\n";
	$count = 0;
    }
}
print "</table>\n";
