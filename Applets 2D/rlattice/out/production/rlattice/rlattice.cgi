#!/usr/bin/python

import string
import cgi


print "Content-Type: text/html\n\n"

fields = cgi.FieldStorage()

print "<center>"
print "	<applet code=rlattice.class archive=../rlattice/rlattice.jar width=800 height=360>"
print "		<param Name=\"patternChoice\" Value=\""+fields['sel'].value+"\">"
print "		<param Name=\"offset\" Value=\""+fields['off'].value+"\">"
print "	</applet>"
