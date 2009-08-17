import JarUtilities
import SVNUtilities
import VersionUtilities
import FileUtilities
import InstallerUtilities
import AliceBuildObject
import BuildObject
import AliceDataBuildObject
import GalleryBuildObject
import ObfuscationUtilities
import NetbeansPluginUtilities
from org.tmatesoft.svn import core
import java

__author__="Dave Culyba"
__date__ ="$May 4, 2009 4:43:15 PM$"

#SIMS_LIBRARY_SSH_SVN_URL = "svn+ssh://svn@vigoor.pc.cc.cmu.edu:15217/aliceprivate/"
#SIMS_LIBRARY_SRC_DIR = "Y:/Alice_Private_Source/pre_obfuscated"
##
#SIMS_SSH_SVN_USERNAME = "svn"
#SIMS_SSH_SVN_PASSPHRASE = "shaqDrains2"
#SIMS_SSH_KEY_FILE = java.io.File("C:/putty/pc_build.key")
##
#simsSshPreviousRespository = SVNUtilities.SVNConnection(SIMS_LIBRARY_SSH_SVN_URL, SIMS_SSH_SVN_USERNAME, SIMS_SSH_SVN_PASSPHRASE, SIMS_SSH_KEY_FILE)
#simsSshCurrentRespository = SVNUtilities.SVNConnection(SIMS_LIBRARY_SSH_SVN_URL, SIMS_SSH_SVN_USERNAME, SIMS_SSH_SVN_PASSPHRASE, SIMS_SSH_KEY_FILE)
#simsSshRepository = SVNUtilities.Repository(simsSshPreviousRespository, simsSshCurrentRespository, "pre_obfuscated", "Y:/Alice_Private_Source/pre_obfuscated", shouldRelocate = False)
#
#print "Getting latest version of the Sims Library code..."
#simsSshRepository.syncCodeToHead()
#print "Done updating the Sims Library."
#ObfuscationUtilities.doObfuscation()

#fix the ant build.xml files
#FileUtilities.replaceStringsInFiles([("../../", "../")], "C:/AliceSource", ["build.xml"])

HTTP_SVN_USERNAME = "dculyba"
HTTP_SVN_PASSWORD = "bkmdlmc"
SSH_SVN_USERNAME = "culybad"
SSH_KEY_FILE = java.io.File("C:/putty/washU_private.key")
SSH_KEY_PASSPHRASE = "bkmdlmc"

FORCE_NEW_VERSION = True
FORCE_NEW_COMPONENTS = False
OLD_VERSION = VersionUtilities.VersionNum("3.0.0.0.59")
NEW_VERSION = VersionUtilities.VersionNum("3.0.0.0.60")

PREVIOUS_JAR_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3/branches/"+str(OLD_VERSION)+"/"
PREVIOUS_JAR_SSH_SVN_URL = "svn+ssh://culybad@invent.cse.wustl.edu/project/invent/lookingglass/svn/"
PREVIOUS_BUILD_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/branches/"+str(OLD_VERSION)+"/"

CURRENT_JAR_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3/branches/"+str(NEW_VERSION)+"/"
CURRENT_JAR_SSH_SVN_URL = "svn+ssh://culybad@invent.cse.wustl.edu/project/invent/lookingglass/svn/"
CURRENT_BUILD_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/branches/"+str(NEW_VERSION)+"/"

INSTALLER_BUILD_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/"


#NEW_VERSION = VersionUtilities.VersionNum("3.0.0.0.60")

previousAliceSourceRepository = SVNUtilities.SVNConnection(PREVIOUS_JAR_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD)
previousWashUSourceRepository = SVNUtilities.SVNConnection(PREVIOUS_JAR_SSH_SVN_URL, SSH_SVN_USERNAME, SSH_KEY_PASSPHRASE, SSH_KEY_FILE)
previousBuildRepository = SVNUtilities.SVNConnection(PREVIOUS_BUILD_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD)

currentAliceSourceRepository = SVNUtilities.SVNConnection(CURRENT_JAR_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD)
currentWashUSourceRepository = SVNUtilities.SVNConnection(CURRENT_JAR_SSH_SVN_URL, SSH_SVN_USERNAME, SSH_KEY_PASSPHRASE, SSH_KEY_FILE)
currentBuildRepository = SVNUtilities.SVNConnection(CURRENT_BUILD_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD)

installerBuildRepository = SVNUtilities.SVNConnection(INSTALLER_BUILD_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD)

PLATFORMS = ["mac", "windows", "linux"]

componentMap = {
	("macauxiliary", "auxiliary") : [
		SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceAuxiliary/MacAuxiliary", "C:/AliceAuxiliary/MacAuxiliary")
	],
	("windowsauxiliary", "auxiliary") : [
		SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceAuxiliary/WindowsAuxiliary", "C:/AliceAuxiliary/WindowsAuxiliary")
	],
	("linuxauxiliary", "auxiliary") : [
		SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceAuxiliary/LinuxAuxiliary", "C:/AliceAuxiliary/LinuxAuxiliary")
	],
	("macnativelib", "auxiliary") : [
		SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceNativeLibraries/MacNative", "C:/AliceNative/MacNative")
	],
	("windowsnativelib", "auxiliary") : [
		SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceNativeLibraries/WindowsNative", "C:/AliceNative/WindowsNative")
	],
	("linuxnativelib", "auxiliary") : [
		SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceNativeLibraries/LinuxNative", "C:/AliceNative/LinuxNative")
	],
	("aliceData", "aliceData") : [
		SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceBaseApplication", "C:/AliceDistribution")
	],
	("AliceClassicGallery", "gallery") : [
		SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceGalleries/org.alice.apis.moveandturn.gallery", "C:/AliceGalleries/org.alice.apis.moveandturn.gallery")
	],
	("AliceLookingGlassGallery", "gallery") : [
		SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceGalleries/edu.wustl.cse.lookingglass.apis.walkandtouch.gallery", "C:/AliceGalleries/edu.wustl.cse.lookingglass.apis.walkandtouch.gallery")
	],
	("AliceSimsGallery" , "gallery") : [
		SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceGalleries/org.alice.apis.stage", "C:/AliceGalleries/org.alice.apis.stage")
	],
	("foundation", "jar") : [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_foundation", "C:/AliceSource/rt_foundation"),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_zoot", "C:/AliceSource/x_zoot"),
	],
	("moveandturn", "jar") : [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_moveandturn", "C:/AliceSource/rt_moveandturn"),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_moveandturn_generated", "C:/AliceSource/rt_moveandturn_generated"),
	],
	("lg_walkandtouch", "jar") : [
		SVNUtilities.Repository(previousWashUSourceRepository, currentWashUSourceRepository, "lg_walkandtouch", "C:/AliceSource/lg_walkandtouch"),
		SVNUtilities.Repository(previousWashUSourceRepository, currentWashUSourceRepository, "lg_walkandtouch_gallery_generated", "C:/AliceSource/lg_walkandtouch_gallery_generated"),
	],
	("stage", "jar") : [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_storytelling", "C:/AliceSource/rt_storytelling"),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_storytelling_private", "C:/AliceSource/rt_storytelling_private"),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_storytelling_generated_depended_upon", "C:/AliceSource/rt_storytelling_generated_depended_upon")
	],
	("ide", "jar") : [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_issue", "C:/AliceSource/rt_issue"),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_ide", "C:/AliceSource/x_ide"),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_ide_stage", "C:/AliceSource/x_ide_stage"),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_ide_i18n", "C:/AliceSource/x_ide_i18n"),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_ide_private", "C:/AliceSource/x_ide_private"),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_youtube", "C:/AliceSource/x_youtube")
	],
	("jar_swingworker", "external") : [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_swingworker", "C:/AliceSource/jar_swingworker"),
	],
	("jar_jogl", "external") : [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_jogl", "C:/AliceSource/jar_jogl"),
	],
	("jar_mail", "external") : [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_mail", "C:/AliceSource/jar_mail"),
	],
	("jar_rpc", "external") : [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_rpc", "C:/AliceSource/jar_rpc"),
	],
	("jar_apple", "external") : [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_apple", "C:/AliceSource/jar_apple"),
	],
	("jar_jaf", "external") : [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_jaf", "C:/AliceSource/jar_jaf"),
	],
	("jar_jira", "external") : [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_jira", "C:/AliceSource/jar_jira"),
	],
	("jar_youtube", "external") : [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_youtube", "C:/AliceSource/jar_youtube"),
	],
}

FORCE_NEW_VERSION = [
"lg_walkandtouch",
#"foundation",
#"moveandturn",
#"stage",
#"ide"
]

FORCE_VERSION = [
#("aliceData", VersionUtilities.VersionNum("3.0.0.0.55")),
]

for (name, version) in FORCE_VERSION:
	print name, str(version)

INSTALLER_REPOSITORY = SVNUtilities.Repository(installerBuildRepository, installerBuildRepository, "AliceInstaller", "C:/AliceBuild/InstallerTemplates/installerTemplate")
INSTALLER_CLOSER_REPOSITORY = SVNUtilities.Repository(installerBuildRepository, installerBuildRepository, "AliceInstaller/ext/components/products/AliceCloser", "C:/AliceBuild/InstallerTemplates/AliceCloserTemplate")
INSTALLER_LAUNCHER_REPOSITORY = SVNUtilities.Repository(installerBuildRepository, installerBuildRepository, "AliceInstaller/ext/components/products/Alice", "C:/AliceBuild/InstallerTemplates/AliceComponentTemplate")
WINDOWS_LAUNCHER_REPOSITORY = SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceLaunchers/WindowsLauncher", "C:/AliceLaunchers/WindowsLauncher")
MAC_LAUNCHER_REPOSITORY = SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceLaunchers/MacLauncher", "C:/AliceLaunchers/MacLauncher")
LINUX_LAUNCHER_REPOSITORY = SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceLaunchers/LinuxLauncher", "C:/AliceLaunchers/LinuxLauncher")
WINDOWS_CLOSER_REPOSITORY = SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceClosers/WindowsCloser", "C:/AliceClosers/WindowsCloser")
MAC_CLOSER_REPOSITORY = SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceClosers/MacCloser", "C:/AliceClosers/MacCloser")
LINUX_CLOSER_REPOSITORY = SVNUtilities.Repository(previousBuildRepository, currentBuildRepository, "AliceClosers/LinuxCloser", "C:/AliceClosers/LinuxCloser")

ALICE_INSTALLER_ID = "alice"
BUILD_OUTPUT_DIR = "C:/AliceVersions"
INSTALLERS_DIR = "C:/AliceInstallers"
DEFAULT_INSTALL_DIR_NAME = "Alice3Beta"
JAR_SRC_DIR = "C:/AliceSource/"

print "Getting latest version of installer code..."
INSTALLER_REPOSITORY.syncCodeToHead()
INSTALLER_LAUNCHER_REPOSITORY.syncCodeToHead()
INSTALLER_CLOSER_REPOSITORY.syncCodeToHead()
print "Done updating installer."

print "Getting latest version of the Windows Launcher code..."
WINDOWS_LAUNCHER_REPOSITORY.syncCodeToHead()
print "Done updating Windows Launcher."

print "Getting latest version of the Mac Launcher code..."
MAC_LAUNCHER_REPOSITORY.syncCodeToHead()
print "Done updating Mac Launcher."

print "Getting latest version of the Linux Launcher code..."
LINUX_LAUNCHER_REPOSITORY.syncCodeToHead()
print "Done updating Linux Launcher."

print "Getting latest version of the Windows Closer code..."
WINDOWS_CLOSER_REPOSITORY.syncCodeToHead()
print "Done updating Windows Launcher."

print "Getting latest version of the Mac Closer code..."
MAC_CLOSER_REPOSITORY.syncCodeToHead()
print "Done updating Mac Launcher."

print "Getting latest version of the Linux Closer code..."
LINUX_CLOSER_REPOSITORY.syncCodeToHead()
print "Done updating Linux Launcher."

launcherBuildObjects = [
	BuildObject.PlatformSpecificBuildObject("aliceWindows", "windows", [WINDOWS_LAUNCHER_REPOSITORY], BUILD_OUTPUT_DIR),
	BuildObject.PlatformSpecificBuildObject("aliceMac", "mac", [MAC_LAUNCHER_REPOSITORY], BUILD_OUTPUT_DIR),
	BuildObject.PlatformSpecificBuildObject("aliceLinux", "linux", [LINUX_LAUNCHER_REPOSITORY], BUILD_OUTPUT_DIR)
]
closerBuildObjects = [
	AliceBuildObject.AliceBuildCloserObject("aliceCloser", [WINDOWS_CLOSER_REPOSITORY], BUILD_OUTPUT_DIR),
]
buildObjects = []

CHECK_FOR_NEW_DATA = True
HEAD_REVISION = core.wc.SVNRevision.HEAD
SPECIFIC_REVISION = 561

REVISION_TO_USE = HEAD_REVISION

jarObjects = []

FOUNDATION_OBJECT = None

print "Checking and pulling source data from source control..."
for (componentName, componentType), repositories in componentMap.items():
	print componentName, componentType
	currentBuildObject = None
	if (componentType == "jar"):
		currentBuildObject = JarUtilities.JarBuildObject(componentName, repositories, BUILD_OUTPUT_DIR)
		jarObjects.append(currentBuildObject)
	elif (componentType == "aliceData"):
		currentBuildObject = AliceDataBuildObject.AliceDataBuildObject(componentName, repositories, BUILD_OUTPUT_DIR)
	elif (componentType == "external"):
		currentBuildObject = AliceDataBuildObject.AliceDataBuildObject(componentName, repositories, BUILD_OUTPUT_DIR)
		currentBuildObject.includeInBuild = False
	elif (componentType == "gallery"):
		currentBuildObject = GalleryBuildObject.GalleryBuildObject(componentName, repositories, BUILD_OUTPUT_DIR)
	elif (componentType == "auxiliary"):
		platform = None
		if (componentName.find("windows") != -1):
			platform = BuildObject.PlatformSpecificBuildObject.WINDOWS_PLATFORM
		elif (componentName.find("mac") != -1):
			platform = BuildObject.PlatformSpecificBuildObject.MAC_PLATFORM
		elif (componentName.find("linux") != -1):
			platform = BuildObject.PlatformSpecificBuildObject.LINUX_PLATFORM
		currentBuildObject = BuildObject.PlatformSpecificBuildObject(componentName, platform, repositories, BUILD_OUTPUT_DIR)

	if (componentName == "foundation"):
	    FOUNDATION_OBJECT = currentBuildObject
	print "trying to update "+currentBuildObject.name
	currentBuildObject.updateFromRepository()
	if (currentBuildObject != None):
		buildObjects.append(currentBuildObject)

print "Done getting source data."

VERSION_FILE_NAME = "C:/AliceSource/rt_foundation/src/edu/cmu/cs/dennisc/alice/Version.txt"
VERSION_FILE = open(VERSION_FILE_NAME,'w')
VERSION_FILE.write(NEW_VERSION.getAliceFormat())
VERSION_FILE.close()

if (FOUNDATION_OBJECT != None):
    FOUNDATION_OBJECT.commitChanges()

versionInfo = VersionUtilities.VersionInfo(BUILD_OUTPUT_DIR)
needNewVersion = True
newVersionNum = NEW_VERSION
print "New version num: "+str(newVersionNum)
needNewComponents = FORCE_NEW_COMPONENTS
lastVersion = versionInfo.getLatestVersion(newVersionNum)
print "Last version was "+str(lastVersion)
if (lastVersion == None):
    needNewComponents = True


if (needNewVersion):
	baseDir = BUILD_OUTPUT_DIR + "/" + str(newVersionNum)
	FileUtilities.deleteDir(baseDir) #this checks to see if the directory exists, and deletes it if it's there
	FileUtilities.makeDir(baseDir)
	print "Making new version "+str(newVersionNum) + " at "+baseDir
	newVersionObject = VersionUtilities.SingleVersion(newVersionNum)

	if (FORCE_NEW_COMPONENTS):
		print "Forcing new components..."

	print "Building components..."

	#loop through all the versioned elements and make new components for anything that's new
	#add the old version to the new version object for anything that hasn't changed
	for buildObject in buildObjects:
		if (not buildObject.includeInBuild):
			print "Not building "+buildObject.name+" since it's not part of the build"
			continue
		needNew = True
		componentToAdd = None
		forceNewComponent = False
		forceVersion = None
		oldComponent = None
		for componentName in FORCE_NEW_VERSION:
			if (componentName == buildObject.name):
				forceNewComponent = True
				break
		if (not forceNewComponent):
			for (componentName, forcedVersion) in FORCE_VERSION:
				if (componentName == buildObject.name):
					desiredVersion = versionInfo.getVersion(forcedVersion)
					if (desiredVersion != None):
						oldComponent = desiredVersion.getComponentNamed(buildObject.name)
						print "Forcing "+buildObject.name+" to be version "+str(forcedVersion)
						break
		if (oldComponent == None and not forceNewComponent and not needNewComponents and not buildObject.isNew() ):
			print "Using object version "+str(lastVersion.version)+" for component "+buildObject.name
			oldComponent = lastVersion.getComponentNamed(buildObject.name)
		if (oldComponent == None):
			needNew = True
		else:
			if (buildObject.initFromExistingVersion(oldComponent.version)): #this returns true if the older version of the file exists
				newVersionObject.addComponent(oldComponent)
				needNew = False
		if (needNew):
			print "Making version "+str(newVersionNum)+" for component "+buildObject.name
			buildObject.setVersionNum(newVersionNum)
			buildObject.buildObject()
			print "Done making new "+buildObject.name
			newComponent = VersionUtilities.ComponentVersion(buildObject.name, newVersionNum)
			newVersionObject.addComponent(newComponent)

	#launcher objects are not versioned. they are always the most recent version
	#build the launcher objects to be the new version regardless of changes

	launcherBuildObject = AliceBuildObject.AliceBuildObject("alice")
	for launcher in launcherBuildObjects:
		launcherBuildObject.addPlatform(launcher)

	launcherBuildObject.setVersionNum(newVersionNum)
	launcherBuildObject.buildObject()
	buildObjects.append(launcherBuildObject)

	for closer in closerBuildObjects:
		closer.setVersionNum(newVersionNum)
		closer.buildObject()
		#add the launcher object to the build objects so it will get added to the installer
		buildObjects.append(closer)

	platformSpecificObjects = []
	for buildObject in buildObjects:
		if (buildObject.platform != None):
			platformSpecificObjects.append(buildObject)
			buildObjects.remove(buildObject)
	crossPlatformObjectNames = []

	for platformObject in platformSpecificObjects:
		objectName = ""
		for platformName in PLATFORMS:
			if (platformObject.name.startswith(platformName)):
				objectName = platformObject.name[len(platformName):len(platformObject.name)]
				break
		alreadyContains = False
		for crossPlatformName in crossPlatformObjectNames:
			if (crossPlatformName == objectName):
				alreadyContains = True
				break
		if (not alreadyContains):
			crossPlatformObjectNames.append(objectName)
	crossPlatformObjects = []
	for objectName in crossPlatformObjectNames:
		crossPlatformBuildObject = BuildObject.MultiPlatformBuildObject(objectName)
		for platformObject in platformSpecificObjects:
			if (platformObject.name.find(objectName) != -1):
				crossPlatformBuildObject.addPlatform(platformObject)
				if (crossPlatformBuildObject.versionNum == None or crossPlatformBuildObject.versionNum < platformObject.versionNum):
					crossPlatformBuildObject.versionNum = platformObject.versionNum
		crossPlatformObjects.append(crossPlatformBuildObject)
	for crossPlatform in crossPlatformObjects:
		print "Adding cross platform object "+crossPlatform.name
		crossPlatform.buildObject() #this will build all platforms that have not already been built
		buildObjects.append(crossPlatform)

	print "Done building components: "
	for buildObject in buildObjects:
		print "  "+buildObject.name

	newVersionObject.writeToFile(BUILD_OUTPUT_DIR)
	print "Wrote new version data: "+str(newVersionObject)

	print "Making installer..."
	FileUtilities.makeDir(INSTALLERS_DIR)
	installer = InstallerUtilities.InstallerProject("AliceInstaller", newVersionNum)

	mainInstallers = []
	closerInstallers = []
	nonMainInstallersIDs = []
	for buildObject in buildObjects:
		print "Trying to add installer for "+buildObject.name
		shouldAddToBuild = True
		if (not buildObject.buildSuccessful):
			print "Not adding "+buildObject.name+" to the installer becuase it wasn't built properly"
			shouldAddToBuild = False
		if (not buildObject.includeInBuild):
			print "Not adding "+buildObject.name+" to the installer becuase it does not belong in the build"
			shouldAddToBuild = False
		if (shouldAddToBuild):
			"Adding installer for "+buildObject.name
			buildComponent = InstallerUtilities.InstallComponent(buildObject)
			buildComponent.setInstallPath(buildObject.getRelativeLocation())

			#Setting the dependencies
			minVersion = VersionUtilities.VersionNum()
			minVersion.setVals([3,0,0,0,0])
			maxVersion = VersionUtilities.VersionNum()
			maxVersion.setVals([3,0,0,1,0])
			dependencyName = InstallerUtilities.makeValidID(launcherBuildObject.name)

			buildComponent.setDependency(dependencyName, minVersion, maxVersion)

			if (buildObject.isMainInstallObject):
				print "Main installer: "+buildComponent.name
				mainInstallers.append(buildComponent)
			elif (buildObject.isCloser):
				closerInstallers.append(buildComponent)
			else:
				nonMainInstallersIDs.append(buildComponent.id)
			installer.addComponent(buildComponent)
		else:
			print "Skipping installer for "+buildObject.name + " since it appears its data was not successfully built"

	for closer in closerInstallers:
		dependencies = nonMainInstallersIDs[:]
		for mainInstaller in mainInstallers:
			dependencies.append(mainInstaller.id)
		installAftersString = "product.install-afters.length="+str(len(dependencies))+"\n"
		count = 1
		print "Install afters string: "+installAftersString
		for prodID in dependencies:
			installAftersString += "product.install-afters."+str(count)+".uid="+prodID+"\n"
			count += 1

		closerDependenciesString = "product.requirements.length="+str(len(dependencies))+"\n"
		count = 1
		print "dependencies string: "+closerDependenciesString
		minVersion = VersionUtilities.VersionNum()
		minVersion.setVals([3,0,0,0,0])
		maxVersion = VersionUtilities.VersionNum()
		maxVersion.setVals([3,0,0,1,0])
		for prodID in dependencies:
			closerDependenciesString += "product.requirements."+str(count)+".uid="+prodID+"\n"
			closerDependenciesString += "product.requirements."+str(count)+".version-lower="+str(minVersion)+"\n"
			closerDependenciesString += "product.requirements."+str(count)+".version-upper="+str(maxVersion)+"\n"
			count += 1
		closer.setInstallAfters(installAftersString)
		closer.setDependencies(closerDependenciesString)

	for mainInstaller in mainInstallers:
		mainInstaller.setDefaultInstallDirectoryName(DEFAULT_INSTALL_DIR_NAME)

	print "Creating installer "+str(installer.version)
	installer.writeToFile(INSTALLERS_DIR)
	print "Made installer: "+installer.projectDir

print "Making NetBeans plugins..."
ALICE_PLUGIN_FOLDER = "C:/AlicePlugins/"

plugin = NetbeansPluginUtilities.PluginObject(ALICE_PLUGIN_FOLDER, NEW_VERSION, OLD_VERSION)
plugin.getJars(jarObjects)
plugin.getLibraries("C:/AliceNative/WindowsNative/application/windows-i586/", "C:/AliceNative/MacNative/application/macosx-universal/", "C:/AliceNative/LinuxNative/application/linux-i586/")
plugin.makeNBMs()
print "Made NetBeans plugins."




