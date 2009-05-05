import java
import edu

__author__="Dave Culyba"
__date__ ="$May 5, 2009 2:27:34 PM$"

JAR_CMD = "C:/Java/jdk1.5.0_18/bin/jar"
EXEC_METHOD = eval( "edu.cmu.cs.dennisc.lang.RuntimeUtilities.exec" )

#def removeJar( jarName ):
#	dstJar = java.io.File( dstDir, jarName + ".jar" )
#	dstJar.delete()

def buildJar( jarName, value ):
	dstFile = java.io.File( dstDir, jarName + ".jar" )
	option = "cvf"
	for name, directories in value:
		if name:
			pass
		else:
			name = jarName
		binDir = java.io.File( srcDir, name + "/bin" )
		print binDir
		for directory in directories:
			cmdarray = [
				JAR_CMD,
				option,
				dstFile.getAbsolutePath(),
				directory
			]
			result = EXEC_METHOD( binDir, cmdarray )
			#result = edu.cmu.cs.dennisc.lang.RuntimeUtilities.exec( binDir, cmdarray )
			option = "uvf"
	print result