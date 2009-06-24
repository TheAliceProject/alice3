import shutil
import os

import java
import edu

def copyTree( srcRoot, dstRoot, extension, isOverwriteDesired ):
	srcFiles = edu.cmu.cs.dennisc.io.FileUtilities.listDescendants( srcRoot, extension )
	for srcFile in srcFiles:
		dstFile = edu.cmu.cs.dennisc.io.FileUtilities.getAnalogousFile( srcFile, srcRoot, dstRoot )
		if isOverwriteDesired == False and dstFile.exists():
			pass
			#print "skipping", dstFile
		else:
			print "copy", srcFile, dstFile
			dstFile.getParentFile().mkdirs()
			shutil.copy( srcFile.getAbsolutePath(), dstFile.getAbsolutePath() )

srcVersion = "3.beta.0005"
dstVersion = edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText()
print dstVersion

srcDir = java.io.File( "/Program Files/Alice/" + srcVersion + "/application" )
dstDir = java.io.File( "/Program Files/Alice/" + dstVersion + "/application" )

copyTree( java.io.File( srcDir, "projects" ), java.io.File( dstDir, "projects" ), "a3p", isOverwriteDesired=True )
copyTree( java.io.File( srcDir, "projects" ), java.io.File( dstDir, "projects" ), "png", isOverwriteDesired=True )
copyTree( java.io.File( srcDir, "classinfos" ), java.io.File( dstDir, "classinfos" ), "bin", isOverwriteDesired=True )

print "done"