#!/usr/bin/python

import httplib
import string
import cgi
import sys 
import cStringIO
import os


# Required header that tells the browser how to render the text.
print "Content-Type: text/plain\n"

sys.stderr=sys.stdout 



# request the page
def GetUrl(ServerAdr,PagePath):
    http = httplib.HTTP(ServerAdr)
    http.putrequest('GET', PagePath)
    http.putheader('Accept', 'text/html')  
    http.putheader('Accept', 'text/plain')  
    http.endheaders()
    httpcode, httpmsg, headers = http.getreply()  
    if httpcode != 200:
      print "Could not get document: Check URL and Path."   
    doc = http.getfile()
    data = doc.read()  # read file
    doc.close()
    return data




#ServerAdr = "sixray.ethz.ch"
#PagePath = "/cgi-bin/icsd/icsd.cgi?"
ServerAdr = "crystals.ethz.ch"
PagePath = "/icsd/index.php?"
args = ""

fields = cgi.FieldStorage()


for i in fields.keys():
  args = args+i+"="+fields[i].value+"&"
  
  
#print ServerAdr+PagePath+args

#print args.encode("utf-8")
  

args = string.replace(args, " ", "+")  
#print PagePath+args

RawData=GetUrl(ServerAdr, PagePath+args)

print RawData


#print args
#print fields.keys()
#  print fields[i].value, i
#ServerAdr = '195.70.6.102:8080'
#PagePath = '/NFPortal/JSP/carte/cartetrafic_small.jsp'


