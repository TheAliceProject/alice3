__author__ = "dennisc"
__date__ = "$Feb 25, 2014 10:05:35 PM$"

import Commands
import FileUtilities
import JavaDocUtilities
import MavenUtilities
import edu
import java

LOCATION_OF_ROOT_FOR_DEVELOPMENT = java.io.File(FileUtilities.DEFAULT_DIRECTORY, "gits/alice")
LOCATION_OF_PLUGIN_8 = java.io.File(LOCATION_OF_ROOT_FOR_DEVELOPMENT, "alice/netbeans/Alice3ModuleSuite")
LOCATION_FOR_PLUGIN_8_JARS = java.io.File(LOCATION_OF_PLUGIN_8, "Alice3Module/release/modules/ext")
LOCATION_FOR_PLUGIN_8_DISTRIBUTION = java.io.File(LOCATION_OF_PLUGIN_8, "Alice3Module/release/src/aliceSource.jar_root")

LOCATION_OF_PROJECT_TEMPLATE_8 = java.io.File(LOCATION_OF_ROOT_FOR_DEVELOPMENT, "alice/netbeans/ProjectTemplate")

projectNames = [
	"util",
	"scenegraph",
	"story-api",
	"ast",
	"story-api-migration",
	"croquet"
]

mavenRepoJarPaths = [
	"org/jogamp/gluegen/gluegen-rt/2.1.3/gluegen-rt-2.1.3.jar",
	"org/jogamp/jogl/jogl-all/2.1.3/jogl-all-2.1.3.jar",
	"javax/media/jmf/2.1.1e/jmf-2.1.1e.jar",
	"com/sun/javamp3/1.0/javamp3-1.0.jar",
	"org/alice/alice-model-source/2014.08.20/alice-model-source-2014.08.20.jar",
	"org/alice/nonfree/nebulous-model-source/2014.08.20/nebulous-model-source-2014.08.20.jar",
]

def _copyJars():
	for projectName in projectNames:
		filename = projectName + "-0.0.1-SNAPSHOT.jar"
		src = java.io.File(MavenUtilities.LOCATION_OF_BUILD_ROOT_POM, "core/" + projectName + "/target/" + filename)
		dst = java.io.File(LOCATION_FOR_PLUGIN_8_JARS, filename)
		edu.cmu.cs.dennisc.java.io.FileUtilities.copyFile(src, dst)
		print dst

	for mavenRepoJarPath in mavenRepoJarPaths:
		MavenUtilities.copyMavenRepoJar(mavenRepoJarPath, LOCATION_FOR_PLUGIN_8_JARS)

DISTRIBUTION_DIR = java.io.File(MavenUtilities.LOCATION_OF_BUILD_ROOT_POM, "/core/resources/target/distribution")

def _copyNatives():
	FileUtilities.deleteDir(LOCATION_FOR_PLUGIN_8_DISTRIBUTION.getAbsolutePath())
	FileUtilities.makeDir(LOCATION_FOR_PLUGIN_8_DISTRIBUTION.getAbsolutePath())
	FileUtilities.copyDirIgnoreFolders(DISTRIBUTION_DIR.getAbsolutePath(), LOCATION_FOR_PLUGIN_8_DISTRIBUTION.getAbsolutePath(), [".svn", "ffmpeg", "libvlc", "application"])

def _createDocs():
	JavaDocUtilities.generateJavaDocsZip(java.io.File(LOCATION_OF_PLUGIN_8, "Alice3Module/release/doc/aliceDocs.zip"))

def _createSrc():
	srcPath = JavaDocUtilities.LOCATION_OF_STORY_API_SRC.getAbsolutePath()
	dst = java.io.File(LOCATION_OF_PLUGIN_8, "Alice3Module/release/src/aliceSource.jar")
	dstPath = dst.getAbsolutePath()
	FileUtilities.zipFilesInDirs(srcPath, ["org/lgna/story"], [".java"], dstPath)

def _zipProjectTemplate(src, dst):
	srcSourceDirectory = java.io.File( src, "src" )
	if srcSourceDirectory.exists():
		pass
	else:
		srcSourceDirectory.mkdirs()

	srcPath = src.getAbsolutePath()
	dstPath = dst.getAbsolutePath()
	print srcPath
	print dstPath
	if src.exists():
		if dst.exists():
			dst.delete()
		cmdarray = [Commands.ZIP_CMD, "a", dstPath]
		Commands.EXEC_METHOD(src, cmdarray)
	else:
		raise srcPath

def setUpNetbeans8Plugin():
	#MavenUtilities.runMavenCleanCompilePackage()
	#MavenUtilities.runMavenCompilePackage()
	#MavenUtilities.runMavenPackage()
	_copyJars()
	_copyNatives()
	_createDocs()
	_createSrc()
	_zipProjectTemplate(LOCATION_OF_PROJECT_TEMPLATE_8, java.io.File(LOCATION_OF_PLUGIN_8, "Alice3Module/src/org/alice/netbeans/ProjectTemplate.zip"))

setUpNetbeans8Plugin()

LOCATION_OF_PROJECT_TEMPLATE_6 = java.io.File(LOCATION_OF_ROOT_FOR_DEVELOPMENT, "alice/netbeans6/ProjectTemplate")

def setUpNetbeans6Plugin():
	src = java.io.File(LOCATION_OF_ROOT_FOR_DEVELOPMENT, "alice/netbeans/Alice3ModuleSuite/Alice3Module/release")
	dst = java.io.File(LOCATION_OF_ROOT_FOR_DEVELOPMENT, "alice/netbeans6/AliceProjectWizard/release")

	if dst.exists():
		pass
	else:
		dst.mkdirs()
	FileUtilities.copyDirIgnoreFolders(src.getAbsolutePath(), dst.getAbsolutePath(), [])
	_zipProjectTemplate(LOCATION_OF_PROJECT_TEMPLATE_6, java.io.File(LOCATION_OF_ROOT_FOR_DEVELOPMENT, "alice/netbeans6/AliceProjectWizard/src/org/alice/netbeans/aliceprojectwizard/AliceProjectTemplateProject.zip"))

setUpNetbeans6Plugin()