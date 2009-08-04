import java
import edu
import BuildJava
import FileUtilities
import BuildObject
import os

__author__="Dave Culyba"
__date__ ="$May 5, 2009 2:27:34 PM$"

JAR_CMD = "C:/Java/jdk1.5.0_18/bin/jar"
EXEC_METHOD = eval( "edu.cmu.cs.dennisc.lang.RuntimeUtilities.exec" )

class JarBuildObject(BuildObject.BuildObject):

	RELATIVE_JAR_LOCATION = "lib/"
	JAR_EXTENSION = ".jar"

	def getRelativeLocation(self):
		return JarBuildObject.RELATIVE_JAR_LOCATION+str(self.versionNum)+"/"

	def getOutputPath(self):
		return self.getBaseOutputDirectory() + "/" + self.getRelativeLocation() + self.name + JarBuildObject.JAR_EXTENSION

	def buildObject(self):
		for repository in self.repositories:
			projectDir = java.io.File( repository.localDir )
			BuildJava.buildProject(projectDir)
		self.dataLocation = self.getOutputPath()
		FileUtilities.makeDirsForFile(self.dataLocation)
		option = "cvf"
		for repository in self.repositories:
			binPath = repository.localDir + "/bin"
			binDir = java.io.File( binPath )
			print binDir
			subDirs = os.listdir(binPath)
			for directory in subDirs:
				print "Addding "+binPath+"/"+directory+" to "+self.name
				cmdarray = [
					JAR_CMD,
					option,
					self.dataLocation,
					directory
				]
				result = EXEC_METHOD( binDir, cmdarray )
				#result = edu.cmu.cs.dennisc.lang.RuntimeUtilities.exec( binDir, cmdarray )
				option = "uvf"
				print result
		self.isZip = False
		self.buildSuccessful = True


		

		