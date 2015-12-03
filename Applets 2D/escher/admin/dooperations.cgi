#!/usr/bin/python

import string
import cgi
import os
import shutil


# Required header that tells the browser how to render the text.
print "Content-Type: text/html\n"


fields = cgi.FieldStorage()

for i in fields.keys():
	if fields[i].value=="d" :
		print "removing "+fields[i].name+"<br>"
		shutil.copy("../savedir/"+fields[i].name+".gif", "../trashdir/")
		shutil.copy("../savedir/"+fields[i].name+".data", "../trashdir/")
		os.remove("../savedir/"+fields[i].name+".gif")
		os.remove("../savedir/"+fields[i].name+".data")
	elif fields[i].value=="f" :
		print "moving to favorites "+fields[i].name+"<br>"
		shutil.copy("../savedir/"+fields[i].name+".gif", "../favdir/")
		shutil.copy("../savedir/"+fields[i].name+".data", "../favdir/")
		os.remove("../savedir/"+fields[i].name+".gif")
		os.remove("../savedir/"+fields[i].name+".data")
	elif fields[i].value=="r" :
		print "restoring "+fields[i].name+"<br>"
		shutil.copy("../trashdir/"+fields[i].name+".gif", "../savedir/")
		shutil.copy("../trashdir/"+fields[i].name+".data", "../savedir/")
		os.remove("../trashdir/"+fields[i].name+".gif")
		os.remove("../trashdir/"+fields[i].name+".data")
	elif fields[i].value=="u" :
		print "retrograding from favorites "+fields[i].name+"<br>"
		shutil.copy("../favdir/"+fields[i].name+".gif", "../savedir/")
		shutil.copy("../favdir/"+fields[i].name+".data", "../savedir/")
		os.remove("../favdir/"+fields[i].name+".gif")
		os.remove("../favdir/"+fields[i].name+".data")
	elif fields[i].value=="v" :
		print "removing "+fields[i].name+"<br>"
		shutil.copy("../favdir/"+fields[i].name+".gif", "../trashdir/")
		shutil.copy("../favdir/"+fields[i].name+".data", "../trashdir/")
		os.remove("../favdir/"+fields[i].name+".gif")
		os.remove("../favdir/"+fields[i].name+".data")

print "<br>"
print "done"
print "<br><br>"
print "Go to directory<br>"
print "<a href=\"adminSaved.cgi\">Save directory</a><br>"
print "<a href=\"adminFav.cgi\">Favorites directory</a><br>"
print "<a href=\"adminTrash.cgi\">Trash directory</a><br>"


