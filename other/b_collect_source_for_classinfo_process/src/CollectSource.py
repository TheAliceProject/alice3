import java
import edu

import shutil
import os

def copyTree( srcRoot, dstRoot, extension=None, isOverwriteDesired=True ):
	srcFiles = edu.cmu.cs.dennisc.io.FileUtilities.listDescendants( srcRoot, extension )
	for srcFile in srcFiles:
		path = srcFile.getCanonicalPath()
		if "\\.svn\\" in path:
			pass
		elif "\\bin\\" in path:
			pass
		else:
			dstFile = edu.cmu.cs.dennisc.io.FileUtilities.getAnalogousFile( srcFile, srcRoot, dstRoot )
			print "copy", srcFile, dstFile
			dstFile.getParentFile().mkdirs()
			shutil.copy( srcFile.getAbsolutePath(), dstFile.getAbsolutePath() )

defaultDirectory = edu.cmu.cs.dennisc.io.FileUtilities.getDefaultDirectory()
srcDir = java.io.File( defaultDirectory, "Eclipse Workspaces/Alice3/" )
dstDir = java.io.File( defaultDirectory, "all" )

for directoryName in [ "rt_foundation", "x_zoot", "rt_moveandturn", "rt_moveandturn_generated", "lg_walkandtouch", "lg_walkandtouch_gallery_generated", "rt_storytelling", "rt_storytelling_generated_depended_upon", "rt_storytelling_private" ]:
	dir = java.io.File( srcDir, directoryName )
	copyTree( dir, dstDir )
	
