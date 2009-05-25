import FileUtilities
import BuildObject

__author__="Dave Culyba"
__date__ ="$May 11, 2009 6:30:37 PM$"


class AliceBuildObject(BuildObject.ZipBuildObject):

	def __init__(self, name, platform, sourceDir, outputDir):
		BuildObject.ZipBuildObject.__init__(self, name, sourceDir, outputDir)
		self.isMainInstallObject = True
		self.platform = platform

	def getRelativeLocation(self):
		return ""


class AliceBuildCloserObject(BuildObject.ZipBuildObject):

	def __init__(self, name, platform, sourceDir, outputDir):
		BuildObject.ZipBuildObject.__init__(self, name, sourceDir, outputDir)
		self.isMainInstallObject = False
		self.isCloser = True
		self.platform = platform

	def getRelativeLocation(self):
		return ""





