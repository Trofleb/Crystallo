#!/usr/bin/python

import string
import cgi


print "Content-Type: text/html\n\n"

fields = cgi.FieldStorage()

print "<html>"
print "  <head>"
print "    <title>"+fields['title'].value+"</title>"
print "  </head>"
print ""
print "  <body>"
print "    <h1>"+fields['title'].value+"</h1>"
print "    <p>"+fields['text'].value
print "  </body>"
print "</html>"

