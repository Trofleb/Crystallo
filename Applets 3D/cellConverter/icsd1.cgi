#!/usr/bin/python

import urllib2
import sys 
import os
import string

# Required header that tells the browser how to render the text.
print "Content-Type: text/plain\n"

sys.stderr=sys.stdout 

the_url = 'http://chemcrys.ethz.ch/icsd/index.php'+'?'+os.environ['QUERY_STRING']
req = urllib2.Request(the_url)
handle = urllib2.urlopen(req)
the_page = handle.read()

print the_page
