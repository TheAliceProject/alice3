__author__="dennisc"
__date__ ="$Feb 25, 2014 10:26:29 PM$"

import FileUtilities
import MavenUtilities
import com
import java

TEMP_LOCATION_FOR_BUILD = java.io.File( FileUtilities.DEFAULT_DIRECTORY, "temp_space_for_build" )
TEMP_LOCATION_FOR_DOCS = java.io.File( TEMP_LOCATION_FOR_BUILD, "docs" )
LOCATION_OF_STORY_API_SRC = java.io.File( MavenUtilities.LOCATION_OF_BUILD_ROOT_POM, "core/story-api/src/main/java" )

def _generateJavaDocs( srcPath ):
	dstPath = TEMP_LOCATION_FOR_DOCS.getAbsolutePath()
	TEMP_LOCATION_FOR_DOCS.mkdirs()
	FileUtilities.clearDir(dstPath)
	javadocargs = ["-d", dstPath, "-sourcepath", srcPath, "-encoding", "UTF-8", "-docencoding", "UTF-8", "-subpackages", "org.lgna.story"]
	com.sun.tools.javadoc.Main.execute(javadocargs)

def generateJavaDocsZip( dst ):
	srcPath = LOCATION_OF_STORY_API_SRC.getAbsolutePath()
	dstPath = dst.getAbsolutePath()
	_generateJavaDocs( srcPath )
	FileUtilities.zipFilesInDirs( srcPath, [], [], dstPath)
