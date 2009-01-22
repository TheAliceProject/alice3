import java
import edu

if edu.cmu.cs.dennisc.lang.SystemUtilities.isMac():
	root = "/Applications/Programming/Alice"
else:
	root = "/Program Files/Alice/"

version = edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText()

dir = java.io.File( root + version + "/application/projects" )

for file in edu.cmu.cs.dennisc.io.FileUtilities.listDescendants( dir, "a3p" ):
	if file.setReadOnly():
		print "success setReadOnly:", file 
	else:
		print "FAILURE setReadOnly:", file 

print "done"