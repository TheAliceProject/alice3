import BuildObject

__author__="Dave Culyba"
__date__ ="$May 18, 2009 4:37:20 PM$"


class AliceDataBuildObject(BuildObject.ZipBuildObject):

	def __init__(self, name, sourceDir, outputDir):
		BuildObject.ZipBuildObject.__init__(self, name, sourceDir, outputDir)

	def getRelativeLocation(self):
		return ""
	