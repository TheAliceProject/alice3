__author__="Dennis Cosgrove"

import edu
import java

import FileUtilities

if edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows():
	MAVEN_COMMAND = "C:\\apache-maven-3.1.1\\bin\\mvn.bat"
else:
	MAVEN_COMMAND = "mvn"

MAVEN_REPOSITORY_DIRECTORY = java.io.File( FileUtilities.USER_DIRECTORY, ".m2/repository" )

LOCATION_OF_BUILD_ROOT_POM = java.io.File( FileUtilities.DEFAULT_DIRECTORY, "gits/alice_for_build" )

def _runMaven( *args ):
	if LOCATION_OF_BUILD_ROOT_POM.exists():
		processBuilder = java.lang.ProcessBuilder( ( MAVEN_COMMAND, ) + args  )
		processBuilder.directory( LOCATION_OF_BUILD_ROOT_POM )
		#processBuilder.start()
		edu.cmu.cs.dennisc.java.lang.ProcessUtilities.startAndWaitFor( processBuilder, java.lang.System.out, java.lang.System.err )
	else:
		print LOCATION_OF_BUILD_ROOT_POM
		raise "LOCATION_OF_BUILD_ROOT_POM.getAbsolutePath()"

def runMavenCleanCompilePackage():
	_runMaven( "clean", "compile", "package" )

def runMavenCompilePackage():
	_runMaven( "compile", "package" )

def runMavenPackage():
	_runMaven( "package" )

def copyMavenRepoJar( mavenRepoJarPath, dstDirectory ):
	src = java.io.File( MAVEN_REPOSITORY_DIRECTORY, mavenRepoJarPath )
	dst = java.io.File( dstDirectory, src.getName() )
	if src.exists():
		edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile( src, dst )
		print dst
	else:
		raise str( src )
