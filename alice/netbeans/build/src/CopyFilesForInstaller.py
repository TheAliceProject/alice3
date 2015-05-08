import java
import edu
import FileUtilities
import MavenUtilities

import shutil

LOCATION_OF_ROOT_FOR_DEVELOPMENT = java.io.File(FileUtilities.DEFAULT_DIRECTORY, "gits/alice")
LOCATION_FOR_INSTALL_DATA_ROOT = java.io.File(LOCATION_OF_ROOT_FOR_DEVELOPMENT, "installer/aliceInstallData/Alice3")
LOCATION_FOR_INSTALL_DATA_LIBS = java.io.File(LOCATION_FOR_INSTALL_DATA_ROOT, "lib")

LOCATION_OF_RESOURCES = java.io.File(LOCATION_OF_ROOT_FOR_DEVELOPMENT, "core/resources/target/distribution")

jarsToCopy = [
	( "core", [
		"util",
		"scenegraph",
		"glrender",
		"story-api",
		"ast",
		"story-api-migration",
		"croquet",
		"i18n",
		"image-editor",
		"issue-reporting",
		"ide",
	] ),
	( "alice", [
		"alice-ide"
	] )
]


def _copyJars():
	for dirName, projectNames in jarsToCopy:
		for projectName in projectNames:
			filename = projectName + "-0.0.1-SNAPSHOT.jar"
			src = java.io.File(MavenUtilities.LOCATION_OF_BUILD_ROOT_POM, dirName + "/" + projectName + "/target/" + filename)
			dst = java.io.File(LOCATION_FOR_INSTALL_DATA_LIBS, filename)
			edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile(src, dst)
			print dst

def _copyResourcesDirectory():
	for name in ["application", "platform"]:
		shutil.copytree(LOCATION_OF_RESOURCES.getAbsolutePath() + "/" + name, LOCATION_FOR_INSTALL_DATA_ROOT.getAbsolutePath() + "/" + name)

#edu.cmu.cs.dennisc.java.io.FileUtilities.delete( LOCATION_FOR_INSTALL_DATA_ROOT )
_copyJars()
_copyResourcesDirectory()

import re
print LOCATION_FOR_INSTALL_DATA_ROOT
N = len( LOCATION_FOR_INSTALL_DATA_ROOT.getAbsolutePath() ) + 1
for file in edu.cmu.cs.dennisc.java.io.FileUtilities.listDescendants( LOCATION_FOR_INSTALL_DATA_ROOT, "jar" ):
	print "<archive location=\"" + re.sub( "\\\\", "/", file.getAbsolutePath()[N:] ) + "\" failOnError=\"true\" />"
