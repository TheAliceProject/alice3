__author__="dennisc"
__date__ ="$Feb 25, 2014 10:26:29 PM$"

import FileUtilities
import MavenUtilities
import com
import java
import os

TEMP_LOCATION_FOR_BUILD = java.io.File( FileUtilities.DEFAULT_DIRECTORY, "temp_space_for_build" )
TEMP_LOCATION_FOR_DOCS = java.io.File( TEMP_LOCATION_FOR_BUILD, "docs" )

def _createCoreSrcDirectory( subName ):
	return java.io.File( MavenUtilities.LOCATION_OF_BUILD_ROOT_POM, "core/" + subName + "/src/main/java" )

LOCATION_OF_STORY_API_SRC = _createCoreSrcDirectory( "story-api" )

def _generateJavaDocs( srcPath ):
	dstPath = TEMP_LOCATION_FOR_DOCS.getAbsolutePath()
	TEMP_LOCATION_FOR_DOCS.mkdirs()
	FileUtilities.clearDir(dstPath)
	javadocargs = ["-d", dstPath, "-sourcepath", srcPath, "-encoding", "UTF-8", "-docencoding", "UTF-8", "-subpackages", "org.lgna.story", "-exclude", "org.lgna.story.implementation:org.lgna.story.resourceutilities"]
	com.sun.tools.javadoc.Main.execute(javadocargs)

def generateJavaDocsZip( dst ):
	srcPath = ""
	for subName in [ "util", "scenegraph", "ast", "story-api" ]:
		srcPath += str( _createCoreSrcDirectory( subName ).getAbsolutePath() )
		srcPath += os.pathsep
	dstPath = dst.getAbsolutePath()
	_generateJavaDocs( srcPath )
	FileUtilities.zipFilesInDirs( str( TEMP_LOCATION_FOR_DOCS.getAbsolutePath() ), [], [], str( dstPath ) )
