import FileUtilities
import BuildObject

__author__="Dave Culyba"
__date__ ="$May 11, 2009 6:30:37 PM$"


class AliceBuildObject(BuildObject.MultiPlatformBuildObject):

	def __init__(self, name):
		BuildObject.MultiPlatformBuildObject.__init__(self, name)
		self.isMainInstallObject = True
		self.isCloser = False

	def getRelativeLocation(self):
		return ""
	
	


class AliceBuildCloserObject(BuildObject.ZipBuildObject):

	def __init__(self, name, repositories, outputDir):
		BuildObject.ZipBuildObject.__init__(self, name, repositories, outputDir)
		self.isMainInstallObject = False
		self.isCloser = True

	def getRelativeLocation(self):
		return ""





