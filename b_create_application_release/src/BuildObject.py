import BuildObject
import os
import FileUtilities
import java

__author__="Dave Culyba"
__date__ ="$May 11, 2009 4:58:50 PM$"

class BuildObject:
	
	def __init__(self, name, sourceDir, outputDir):
		self.name = name
		self.previousVersion = 0
		self.newVersion = 0
		self.packages = []
		self.versionNum = None
		self.outputDir = outputDir
		self.sourceDir = sourceDir
		self.dataLocation = None
		self.isMainInstallObject = False
		self.isCloser = False
		self.isZip = False
		self.directoryName = name

	def setVersionNum(self, versionNum):
		self.versionNum = versionNum

	def addPackage(self, package):
		self.packages.append(package)

	def isNew(self):
		return self.previousVersion != self.newVersion

	def addPackage(self, package):
		#If we add a package that has changed (the previous revision is less than the new revision)
		#mark this object as having a new version
		if (package.previousRevision < package.newRevision):
			self.newVersion = self.previousVersion + 1
		self.packages.append(package)

	def initFromExistingVersion(self, versionNum):
		self.setVersionNum(versionNum)
		fileLocation = self.getOutputPath()
		if (os.path.exists(fileLocation)):
			self.dataLocation = fileLocation
			print "build object "+self.name+" is located at "+self.dataLocation
			return True
		else:
			self.versionNum = None
			self.dataLocation = None
			return False

	def getBaseOutputDirectory(self):
		return self.outputDir + "/" +  str(self.versionNum)

	def getRelativeLocation(self):
		return ""

	def getOutputPath(self):
		return ""
	
	def buildObject(self):
		print "No Build Defined"
	
class ZipBuildObject(BuildObject):
	def __init__(self, name, sourceDir, outputDir):
		BuildObject.__init__(self, name, sourceDir, outputDir)
		self.isZip = True

	def setDirectoryName(self, directoryName):
		self.directoryName = directoryName

	def getZipSourceDir(self):
		return self.getBaseOutputDirectory() + "/" +self.getRelativeLocation() + self.directoryName

	def getOutputPath(self):
		return self.getBaseOutputDirectory() + "/" +self.getRelativeLocation() + self.directoryName + ".zip"

	def addPackage(self, package):
		BuildObject.addPackage(self, package)
		packageDir = java.io.File(self.sourceDir, package.name)
		self.sourceDir = packageDir.getAbsolutePath()

	def buildObject(self):
		#copy the gallery to new directory and remove .svn folders
		zipFile = self.getOutputPath()
		zipDir = self.getZipSourceDir()
		print "Copying "+self.sourceDir+" to "+zipDir
		FileUtilities.copyDirIgnoreSVN(self.sourceDir, zipDir)
		print "zipping dir "+zipDir+" to file "+zipFile
		self.dataLocation = FileUtilities.zipDir(zipDir, zipFile)
		print "saved data to: "+self.dataLocation
		FileUtilities.deleteDir(zipDir)
		self.isZip = True
	