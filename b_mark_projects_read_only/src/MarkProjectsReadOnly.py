import java
import edu

if edu.cmu.cs.dennisc.lang.SystemUtilities.isMac():
	pre = "/Applications/Programming/Alice/"
	post = "/Alice.app/Contents/Resources/Java"
else:
	pre = "/Program Files/Alice/"
	post = ""

version = edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText()

dir = java.io.File( pre + version + post + "/application/projects" )
print dir
for file in edu.cmu.cs.dennisc.io.FileUtilities.listDescendants( dir, "a3p" ):
	if file.setReadOnly():
		print "success setReadOnly:", file 
	else:
		print "FAILURE setReadOnly:", file 

print "done"