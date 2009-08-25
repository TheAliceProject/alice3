import java
import edu
import BuildJava
import FileUtilities
import BuildJava
import shutil


__author__="Dave Culyba"
__date__ ="$Jun 26, 2009 5:49:01 PM$"


class PluginObject:
	PROJECT_ROOT = "C:/AliceBuild/AliceNetbeansPlugin/"

	def __init__(self, outputDir, version, oldVersion):
		self.outputDir = outputDir
		self.version = version
		self.oldVersion = oldVersion
		if (not self.outputDir.endswith("/")):
			self.outputDir = self.outputDir + "/"
		self.outputDir = self.outputDir + str(version) +"/"

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
	    
	def getLibraries(self, windowsDir, macDir, linuxDir):
		MAC_FILE = "libjni_nebulous.jnilib"
		WINDOWS_FILE = "jni_nebulous.dll"
		LINUX_FILE = "libjni_nebulous.so"

		#LIBRARY_LIB_DIR = "AliceProjectWizard/release/modules/lib/"
		#MODULE_LIB_DIR = "Alice3PluginSuite/build/cluster/modules/lib/"

		#SUITE_MODULE_WINDOWS_DIR = "Alice3PluginSuite/build/cluster/libs/storytelling.jar-natives-windows-i586/"
		LIBRARY_WINDOWS_DIR = "AliceProjectWizard/release/libs/stage.jar-natives-windows-i586/"

		#SUITE_MODULE_MAC_DIR = "Alice3PluginSuite/build/cluster/libs/storytelling.jar-natives-macosx-universal/"
		LIBRARY_MAC_DIR = "AliceProjectWizard/release/libs/stage.jar-natives-macosx-universal/"
		LIBRARY_LINUX_DIR = "AliceProjectWizard/release/libs/stage.jar-natives-linux-i586/"

		#shutil.copy(windowsDir+WINDOWS_FILE, PluginObject.PROJECT_ROOT+LIBRARY_LIB_DIR+WINDOWS_FILE)
		#shutil.copy(windowsDir+WINDOWS_FILE, PluginObject.PROJECT_ROOT+MODULE_LIB_DIR+WINDOWS_FILE)
		#shutil.copy(windowsDir+WINDOWS_FILE, PluginObject.PROJECT_ROOT+SUITE_MODULE_WINDOWS_DIR+WINDOWS_FILE)
		shutil.copy(windowsDir+WINDOWS_FILE, PluginObject.PROJECT_ROOT+LIBRARY_WINDOWS_DIR+WINDOWS_FILE)

		#shutil.copy(macDir+MAC_FILE, PluginObject.PROJECT_ROOT+LIBRARY_LIB_DIR+MAC_FILE)
		#shutil.copy(macDir+MAC_FILE, PluginObject.PROJECT_ROOT+MODULE_LIB_DIR+MAC_FILE)
		#shutil.copy(macDir+MAC_FILE, PluginObject.PROJECT_ROOT+SUITE_MODULE_MAC_DIR+MAC_FILE)
		shutil.copy(macDir+MAC_FILE, PluginObject.PROJECT_ROOT+LIBRARY_MAC_DIR+MAC_FILE)

		shutil.copy(linuxDir+LINUX_FILE, PluginObject.PROJECT_ROOT+LIBRARY_LINUX_DIR+LINUX_FILE)


	def makeNBMs(self):
		FileUtilities.replaceStringsInFiles([(str(self.oldVersion), str(self.version))], "C:/AliceBuild/AliceNetbeansPlugin", ["manifest.mf", "org-alice-alice3.xml", "org-alice-netbeans-aliceprojectwizard.xml"])

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

