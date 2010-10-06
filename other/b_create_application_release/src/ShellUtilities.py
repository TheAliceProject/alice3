import shutil

def copyTree( srcRoot, dstRoot, extension=None, isOverwriteDesired=True ):
	import edu
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

def removeIfNecessary( dir ):
	if dir.exists():
		print "removing", dir
		import shutil
		shutil.rmtree( dir.getAbsolutePath() )
