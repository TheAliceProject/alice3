import BuildObject
import os
import FileUtilities
import java

__author__="Dave Culyba"
__date__ ="$May 11, 2009 4:58:50 PM$"

class BuildObject:
	
	def __init__(self, name, repositories, outputDir):
		self.name = name
		self.previousVersion = 0
		self.newVersion = 0
		self.versionNum = None
		self.outputDir = outputDir
		self.repositories = []
		for repository in repositories:
			self.addRepository(repository)
		self.dataLocation = None
		self.isMainInstallObject = False
		self.isCloser = False
		self.isZip = False
		self.directoryName = name
		self.buildSuccessful = False
		self.isMultiPlatform = False
		self.relativeLocation = ""
		self.platform = None
		self.includeInBuild = True

	def setRelativeLocation(self, relativeLocation):
		self.relativeLocation = relativeLocation

	def getRelativeLocation(self):
		return self.relativeLocation

	def setVersionNum(self, versionNum):
		self.versionNum = versionNum

	def addRepository(self, repository):
		if (repository.isNew()):
			self.newVersion = self.previousVersion + 1
		self.repositories.append(repository)

	def isNew(self):
		return self.previousVersion != self.newVersion

	def updateFromRepository(self):
	    for repository in self.repositories:
		    repository.syncCodeToHead()

	def commitChanges(self):
	    for repository in self.repositories:
		    repository.commitChanges()

#	def addPackage(self, package):
#		#If we add a package that has changed (the previous revision is less than the new revision)
#		#mark this object as having a new version
#		if (package.previousRevision < package.newRevision):
#			self.newVersion = self.previousVersion + 1
#		self.packages.append(package)

	def initFromExistingVersion(self, versionNum):
		self.setVersionNum(versionNum)
		fileLocation = self.getOutputPath()
		if (os.path.exists(fileLocation)):
			self.dataLocation = fileLocation
			self.buildSuccessful = True
			print "build object "+self.name+" is located at "+self.dataLocation
			return True
		else:
			self.versionNum = None
			self.dataLocation = None
			self.buildSuccessful = False
			return False

	def getBaseOutputDirectory(self):
		return self.outputDir + "/" +  str(self.versionNum)

	def getOutputPath(self):
		return ""
	
	def buildObject(self):
		print "No Build Defined"
	
class ZipBuildObject(BuildObject):
	def __init__(self, name, repositories, outputDir):
		BuildObject.__init__(self, name, repositories, outputDir)
		self.isZip = True

	def getSourceDir(self):
		return self.repositories[0].localDir

	def setDirectoryName(self, directoryName):
		self.directoryName = directoryName

	def getZipSourceDir(self):
		return self.getBaseOutputDirectory() + "/" +self.getRelativeLocation() + self.directoryName

	def getOutputPath(self):
		return self.getBaseOutputDirectory() + "/" +self.getRelativeLocation() + self.directoryName + ".zip"

	def addRepository(self, repository):
		self.repositories = []
		BuildObject.addRepository(self, repository)

	def buildObject(self):
		#copy the gallery to new directory and remove .svn folders
		zipFile = self.getOutputPath()
		zipDir = self.getZipSourceDir()
		print "Copying "+self.getSourceDir()+" to "+zipDir
		FileUtilities.copyDirIgnoreSVN(self.getSourceDir(), zipDir)
		print "zipping dir "+zipDir+" to file "+zipFile
		self.dataLocation = FileUtilities.zipDir(zipDir, zipFile)
		if (self.dataLocation != None):
			self.buildSuccessful = True
		print "saved data to: "+self.dataLocation
		FileUtilities.deleteDir(zipDir)
		self.isZip = True

class PlatformSpecificBuildObject(ZipBuildObject):
	WINDOWS_PLATFORM = "windows"
	MAC_PLATFORM = "mac"

	def __init__(self, name, platform, repositories, outputDir):
		ZipBuildObject.__init__(self, name, repositories, outputDir)
		self.platform = platform


class MultiPlatformBuildObject(BuildObject):
	

	PLATFORMS = [PlatformSpecificBuildObject.WINDOWS_PLATFORM, PlatformSpecificBuildObject.MAC_PLATFORM]
	PLATFORMS_TO_KEYS = {PlatformSpecificBuildObject.WINDOWS_PLATFORM:"_WindowsDataPath_", PlatformSpecificBuildObject.MAC_PLATFORM:"_MacDataPath_"}

	def __init__(self, name):
		BuildObject.__init__(self, name, "", "")
		self.isZip = True
		self.name = name
		self.platformObjects = []
		self.platform = None
		self.isMultiPlatform = True

	def isNew(self):
		for platform in self.platformObjects:
			if (platform.isNew()):
				return True
		return False

	def buildObject(self):
		badBuild = False
		for platform in self.platformObjects:
			if (not platform.buildSuccessful):
				platform.buildObject()
			if (not platform.buildSuccessful):
				badBuild = True
		self.buildSuccessful = not badBuild

	def setVersionNum(self, versionNum):
		self.versionNum = versionNum
		for platform in self.platformObjects:
			platform.setVersionNum(versionNum)

	def getOutputPath(self, platformID):
		for platform in self.platformObjects:
			if (platform.platform == platformID):
				return platform.getOutputPath()
		return ""

	def initFromExistingVersion(self, versionNum):
		return False

	def getDataLocation(self, platformID):
		for platform in self.platformObjects:
			if (platform.platform == platformID):
				return platform.dataLocation
		return ""

	def addPlatform(self, platformBuildObject):
		self.platformObjects.append(platformBuildObject)
	