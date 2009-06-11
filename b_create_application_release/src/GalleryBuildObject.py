import FileUtilities
import BuildObject

__author__="Dave Culyba"
__date__ ="$May 13, 2009 3:34:07 PM$"

class GalleryBuildObject(BuildObject.ZipBuildObject):
	def getRelativeLocation(self):
		return "gallery/assets/"+self.directoryName+str(self.versionNum)+"/"

	def getOutputPath(self):
		return self.getBaseOutputDirectory() + "/gallery/assets/" + self.directoryName+".zip"

	def getZipSourceDir(self):
		return self.getBaseOutputDirectory() + "/" +self.getRelativeLocation()