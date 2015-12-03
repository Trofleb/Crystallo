#!/usr/bin/python

import string
import cgi
import os
import shutil
import os.path


print "Content-type: text/plain\n"



for i in os.listdir("../savedir/"):
	s = ""+i
	if string.find(s, "save")!=-1:
		ss = s[string.find(s, "_")+1:string.find(s, ".")]
		if not os.path.exists("../savedir/index_"+ss+".gif"):
			print "index not found "+ss



	if string.find(s, "index")!=-1:
		ss = s[string.find(s, "_")+1:string.find(s, ".")]
		if not os.path.exists("../savedir/save_"+ss+".gif"):
			print "save not found "+ss

print "done"

#			os.remove("../savedir/save_"+ss+".gif")

#			shutil.copy("../savedir/save_"+ss+".gif", "../favdir/")
	

	

