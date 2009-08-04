import FileUtilities
import BuildObject

__author__="Dave Culyba"
__date__ ="$May 13, 2009 3:34:07 PM$"

class GalleryBuildObject(BuildObject.ZipBuildObject):

	def __init__(self, name, repositories, outputDir):
		BuildObject.ZipBuildObject.__init__(self, name, repositories, outputDir)
		dirPathSplit = repositories[0].localDir.split("/")
		self.setDirectoryName(dirPathSplit[len(dirPathSplit) - 1])


	def getRelativeLocation(self):
		return "gallery/assets/"+self.directoryName+"/"

	def getOutputPath(self):
		return self.getBaseOutputDirectory() + "/gallery/assets/" + self.directoryName+".zip"

	def getZipSourceDir(self):
		return self.getBaseOutputDirectory() + "/" +self.getRelativeLocation()