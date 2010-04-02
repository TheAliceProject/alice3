import java
import edu
import BuildJava
import FileUtilities
import BuildJava
import shutil
import os


__author__="Dave Culyba"
__date__ ="$Jun 26, 2009 5:49:01 PM$"


class PluginObject:
	PROJECT_ROOT = "C:/AliceBuild/AliceNetbeansPlugin/"

	def __init__(self, outputDir, version, joglDir):
		self.outputDir = outputDir
		self.version = version
		self.versionPattern = "\d+\.\d+\.\d+\.\d+\.\d+"
		self.joglDir = joglDir
		if (not self.outputDir.endswith("/")):
			self.outputDir = self.outputDir + "/"
		self.outputDir = self.outputDir + str(version) +"/"

	def getJOGLJars(self):
	    ALICE_WIZARD_INSTALL_ROOT="AliceProjectWizard/release/libs/jogl"
	    JOGL_JAR = "/jogl.jar"
	    GLUEGEN_JAR = "/gluegen-rt.jar"
	    FileUtilities.makeDirsForFile(PluginObject.PROJECT_ROOT+ALICE_WIZARD_INSTALL_ROOT+JOGL_JAR)
	    shutil.copy(self.joglDir+JOGL_JAR, PluginObject.PROJECT_ROOT+ALICE_WIZARD_INSTALL_ROOT+JOGL_JAR)
	    shutil.copy(self.joglDir+GLUEGEN_JAR, PluginObject.PROJECT_ROOT+ALICE_WIZARD_INSTALL_ROOT+GLUEGEN_JAR)

	def getJars(self, jarObjects):
	    foundationJarPath = ""
	    walkAndTouchJarPath = ""
	    moveAndTurnJarPath = ""
	    stageJarPath = ""
	    for jarObject in jarObjects:
		    if (jarObject.name == "foundation"):
			    foundationJarPath = jarObject.getOutputPath()
			    print "Found foundation jar: "+foundationJarPath
		    elif (jarObject.name == "lg_walkandtouch"):
			    walkAndTouchJarPath = jarObject.getOutputPath()
			    print "Found lg_walkandtouch jar: "+walkAndTouchJarPath
		    elif (jarObject.name == "moveandturn"):
			    moveAndTurnJarPath = jarObject.getOutputPath()
			    print "Found moveandturn jar: "+moveAndTurnJarPath
		    elif (jarObject.name == "stage"):
			    stageJarPath = jarObject.getOutputPath()
			    print "Found stage jar: "+stageJarPath
	    self.getJarsFromPaths(foundationJarPath, walkAndTouchJarPath, moveAndTurnJarPath, stageJarPath)
		

	def getJarsFromPaths(self, foundationPath, walkAndTouchPath, moveAndTurnPath, storyTellingPath):
	    FOUNDATION_JAR="foundation.jar"
	    WALKANDTOUCH_JAR="lg_walkandtouch.jar"
	    MOVEANDTURN_JAR="moveandturn.jar"
	    STORYTELLING_JAR="storytelling.jar"

	    ALICE_WIZARD_INSTALL_ROOT="AliceProjectWizard/release/libs/"

	    shutil.copy(foundationPath,PluginObject.PROJECT_ROOT+ALICE_WIZARD_INSTALL_ROOT+FOUNDATION_JAR)
	    shutil.copy(walkAndTouchPath, PluginObject.PROJECT_ROOT+ALICE_WIZARD_INSTALL_ROOT+WALKANDTOUCH_JAR)
	    shutil.copy(moveAndTurnPath, PluginObject.PROJECT_ROOT+ALICE_WIZARD_INSTALL_ROOT+MOVEANDTURN_JAR)
	    shutil.copy(storyTellingPath, PluginObject.PROJECT_ROOT+ALICE_WIZARD_INSTALL_ROOT+STORYTELLING_JAR)

	    self.getJOGLJars()

	def getLibraries(self, windowsDir, windows64Dir, macUniversalDir, mac104Dir, linuxDir, linux64Dir):
		MAC_FILE = "libjni_nebulous.jnilib"
		WINDOWS_FILE = "jni_nebulous.dll"
		LINUX_FILE = "libjni_nebulous.so"
		LIBRARY_WINDOWS_DIR = "AliceProjectWizard/release/libs/stage.jar-natives-windows-i586/"
		LIBRARY_WINDOWS64_DIR = "AliceProjectWizard/release/libs/stage.jar-natives-windows-amd64/"
		LIBRARY_MAC_UNIVERSAL_DIR = "AliceProjectWizard/release/libs/stage.jar-natives-macosx-universal/"
		LIBRARY_MAC_10_4_DIR = "AliceProjectWizard/release/libs/stage.jar-natives-macosx-10.4/"
		LIBRARY_LINUX_DIR = "AliceProjectWizard/release/libs/stage.jar-natives-linux-i586/"
		LIBRARY_LINUX64_DIR = "AliceProjectWizard/release/libs/stage.jar-natives-linux-amd64/"

		JOGL_WINDOWS_64BIT_DIR = self.joglDir + "/windows-amd64"
		JOGL_WINDOWS_32BIT_DIR = self.joglDir + "/windows-i586"
		JOGL_LINUX_64BIT_DIR = self.joglDir + "/linux-amd64"
		JOGL_LINUX_32BIT_DIR = self.joglDir + "/linux-i586"
		JOGL_MAC_UNIVERSAL_DIR = self.joglDir + "/macosx-universal"
		JOGL_MAC_10_4_DIR = self.joglDir + "/macosx-universal" #JOGL uses the same library for this

		FileUtilities.makeDirsForFile(PluginObject.PROJECT_ROOT+LIBRARY_WINDOWS_DIR+WINDOWS_FILE)
		shutil.copy(windowsDir+WINDOWS_FILE, PluginObject.PROJECT_ROOT+LIBRARY_WINDOWS_DIR+WINDOWS_FILE)
		FileUtilities.copyDir(JOGL_WINDOWS_32BIT_DIR, PluginObject.PROJECT_ROOT+LIBRARY_WINDOWS_DIR)

		FileUtilities.makeDirsForFile(PluginObject.PROJECT_ROOT+LIBRARY_WINDOWS64_DIR+WINDOWS_FILE)
		shutil.copy(windows64Dir+WINDOWS_FILE, PluginObject.PROJECT_ROOT+LIBRARY_WINDOWS64_DIR+WINDOWS_FILE)
		FileUtilities.copyDir(JOGL_WINDOWS_64BIT_DIR, PluginObject.PROJECT_ROOT+LIBRARY_WINDOWS64_DIR)

		FileUtilities.makeDirsForFile(PluginObject.PROJECT_ROOT+LIBRARY_MAC_UNIVERSAL_DIR+MAC_FILE)
		shutil.copy(macUniversalDir+MAC_FILE, PluginObject.PROJECT_ROOT+LIBRARY_MAC_UNIVERSAL_DIR+MAC_FILE)
		FileUtilities.copyDir(JOGL_MAC_UNIVERSAL_DIR, PluginObject.PROJECT_ROOT+LIBRARY_MAC_UNIVERSAL_DIR)

		FileUtilities.makeDirsForFile(PluginObject.PROJECT_ROOT+LIBRARY_MAC_10_4_DIR+MAC_FILE)
		shutil.copy(mac104Dir+MAC_FILE, PluginObject.PROJECT_ROOT+LIBRARY_MAC_10_4_DIR+MAC_FILE)
		FileUtilities.copyDir(JOGL_MAC_10_4_DIR, PluginObject.PROJECT_ROOT+LIBRARY_MAC_10_4_DIR)

		FileUtilities.makeDirsForFile(PluginObject.PROJECT_ROOT+LIBRARY_LINUX_DIR+LINUX_FILE)
		shutil.copy(linuxDir+LINUX_FILE, PluginObject.PROJECT_ROOT+LIBRARY_LINUX_DIR+LINUX_FILE)
		FileUtilities.copyDir(JOGL_LINUX_32BIT_DIR, PluginObject.PROJECT_ROOT+LIBRARY_LINUX_DIR)

		FileUtilities.makeDirsForFile(PluginObject.PROJECT_ROOT+LIBRARY_LINUX64_DIR+LINUX_FILE)
		shutil.copy(linux64Dir+LINUX_FILE, PluginObject.PROJECT_ROOT+LIBRARY_LINUX64_DIR+LINUX_FILE)
		FileUtilities.copyDir(JOGL_LINUX_64BIT_DIR, PluginObject.PROJECT_ROOT+LIBRARY_LINUX64_DIR)


	def makeNBMs(self):
		FileUtilities.regexReplaceStringsInFiles([(str(self.versionPattern), str(self.version))], "C:/AliceBuild/AliceNetbeansPlugin", ["manifest.mf", "org-alice-alice3.xml", "org-alice-netbeans-aliceprojectwizard.xml"])

		BuildJava.cleanProject(java.io.File(PluginObject.PROJECT_ROOT+"Alice3PluginSuite"))
		BuildJava.buildProject(java.io.File(PluginObject.PROJECT_ROOT+"Alice3PluginSuite"))
		BuildJava.commandProject(java.io.File(PluginObject.PROJECT_ROOT+"Alice3PluginSuite"), "nbms")
		#LIBRARY_NBM = PluginObject.PROJECT_ROOT+"Alice3PluginSuite/build/updates/org-alice-alice3.nbm"
		WIZARD_NBM = PluginObject.PROJECT_ROOT+"Alice3PluginSuite/build/updates/org-alice-netbeans-aliceprojectwizard.nbm"

		FileUtilities.makeDir(self.outputDir)
		newWizardNBM = self.outputDir+"Alice3ProjectWizard"+str(self.version)+".nbm"
		#newLibraryNBM = self.outputDir+"Alice3Library"+str(self.version)+".nbm"
		#shutil.copy(LIBRARY_NBM, newLibraryNBM)
		shutil.copy(WIZARD_NBM, newWizardNBM)

