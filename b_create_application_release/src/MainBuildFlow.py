import JarUtilities
import SVNUtilities
import VersionUtilities
import FileUtilities
import InstallerUtilities
import AliceBuildObject
import AliceDataBuildObject
import GalleryBuildObject

__author__="Dave Culyba"
__date__ ="$May 4, 2009 4:43:15 PM$"

#fix the ant build.xml files
#FileUtilities.replaceStringsInFiles([("../../", "../")], "C:/AliceSource", ["build.xml"])


componentMap = {
	("aliceData", "aliceData") : [
		[
			#SVN folder name to pull
			"",
			#Directory within SVN folder to build from (is applicable)
			[]
		]
	],
	(("AliceClassicGallery","org.alice.apis.moveandturn.gallery"), "gallery") : [
		[
			#SVN folder name to pull
			"org.alice.apis.moveandturn.gallery",
			#Directory within SVN folder to build from (is applicable)
			[]
		]
	],
	(("AliceLookingGlassGallery", "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery"), "gallery") : [
		[
			#SVN folder name to pull
			"edu.wustl.cse.lookingglass.apis.walkandtouch.gallery",
			#Directory within SVN folder to build from (is applicable)
			[]
		]
	],
	(("AliceSimsGallery","org.alice.apis.stage") , "gallery") : [
		[
			#SVN folder name to pull
			"org.alice.apis.stage",
			#Directory within SVN folder to build from (is applicable)
			[]
		]
	],
	("foundation", "jar") : [
		[
			"rt_foundation",
			[ "edu" ]
		],
		[
			"rt_foundation",
			[ "org" ]
		],
		[
			"x_zoot",
			[ "cascade", "swing", "zoot" ]
		],
	],
	("moveandturn", "jar") : [
		[
			"rt_moveandturn",
			[ "org" ]
		],
		[
			"rt_moveandturn_generated",
			[ "org" ]
		]
	],
	("lg_walkandtouch", "jar") : [
		[
			"lg_walkandtouch",
			[ "edu" ]
		],
		[
			"lg_walkandtouch_gallery_generated",
			[ "edu" ]
		],
	],
	("stage", "jar") : [
		[
			"rt_storytelling",
			[ "org" ]
		],
		[
			"rt_storytelling_private",
			[ "edu" ]
		],
		[
			"rt_storytelling_generated_depended_upon",
			[ "org" ]
		]
	],
	("ide", "jar") : [
		#todo: move rt_issue
		[
			"rt_issue",
			[ "edu" ]
		],
		[
			"x_ide",
			[ "org" ]
		],
		[
			"x_ide_stage",
			[ "org" ]
		],
		[
			"x_ide_i18n",
			[ "org", "edu" ]
		],
		[
			"x_ide_private",
			[ "org" ]
		]
	],
}

ALICE_INSTALLER_ID = "alice"

BUILD_OUTPUT_DIR = "C:/AliceVersions"
INSTALLERS_DIR = "C:/AliceInstallers"
DEFAULT_INSTALL_DIR_NAME = "Alice3Beta"

HTTP_SVN_USERNAME = "dculyba"
HTTP_SVN_PASSWORD = "bkmdlmc"
SSH_SVN_USERNAME = "culybad"
SSH_SVN_PASSWORD = "bkmDlmc00"

JAR_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3/trunk/"
JAR_SSH_SVN_URL = "svn+ssh://culybad@invent.cse.wustl.edu/project/invent/lookingglass/svn/"
JAR_SRC_DIR = "C:/AliceSource/"

ALICE_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/AliceBaseApplication/"
ALICE_APP_SRC_DIR = "C:/AliceDistribution"

GALLERY_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/AliceGalleries/"
GALLERY_SRC_DIR = "C:/AliceGalleries"

INSTALLER_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/AliceInstaller"
INSTALLER_SRC_DIR = "C:/AliceBuild/InstallerTemplates/installerTemplate"

INSTALLER_CLOSER_SOURCE_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/AliceInstaller/ext/components/products/AliceCloser"
INSTALLER_CLOSER_SRC_DIR = "C:/AliceBuild/InstallerTemplates/AliceCloserTemplate"

INSTALLER_LAUNCHER_SOURCE_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/AliceInstaller/ext/components/products/Alice"
INSTALLER_LAUNCHER_SRC_DIR = "C:/AliceBuild/InstallerTemplates/AliceComponentTemplate"

WINDOWS_LAUNCHER_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/AliceLaunchers/WindowsLauncher"
WINDOWS_LAUNCHER_SRC_DIR = "C:/AliceLaunchers/WindowsLauncher"

MAC_LAUNCHER_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/AliceLaunchers/MacLauncher"
MAC_LAUNCHER_SRC_DIR = "C:/AliceLaunchers/MacLauncher"

WINDOWS_CLOSER_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/AliceClosers/WindowsCloser"
WINDOWS_CLOSER_SRC_DIR = "C:/AliceClosers/WindowsCloser"

MAC_CLOSER_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/AliceClosers/MacCloser"
MAC_CLOSER_SRC_DIR = "C:/AliceClosers/MacCloser"


FORCE_NEW_VERSION = True
FORCE_NEW_COMPONENTS = False
BASE_VERSION = 55

print "Creating Alice SVN connection..."
aliceAppHttpRepository = SVNUtilities.Repository(ALICE_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD, ALICE_APP_SRC_DIR)
print "Creating Gallery SVN connection..."
galleryAppHttpRepository = SVNUtilities.Repository(GALLERY_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD, GALLERY_SRC_DIR)
print "Creating Jar http SVN connection..."
jarHttpRepository = SVNUtilities.Repository(JAR_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD, JAR_SRC_DIR)
print "Creating Jar ssh SVN connection..."
jarSshRespository = SVNUtilities.Repository(JAR_SSH_SVN_URL, SSH_SVN_USERNAME, SSH_SVN_PASSWORD, JAR_SRC_DIR)
print "Creating Installer SVN connection..."
installerHttpRespository = SVNUtilities.Repository(INSTALLER_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD, INSTALLER_SRC_DIR)
print "Creating Closer Installer SVN connection..."
installerCloserHttpRespository = SVNUtilities.Repository(INSTALLER_CLOSER_SOURCE_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD, INSTALLER_CLOSER_SRC_DIR)
print "Creating Launcher Installer SVN connection..."
installerLauncherHttpRespository = SVNUtilities.Repository(INSTALLER_LAUNCHER_SOURCE_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD, INSTALLER_LAUNCHER_SRC_DIR)
print "Creating Windows Launcher SVN connection"
windowsLauncherHttpRepository = SVNUtilities.Repository(WINDOWS_LAUNCHER_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD, WINDOWS_LAUNCHER_SRC_DIR)
print "Creating Mac Launcher SVN connection"
macLauncherHttpRepository = SVNUtilities.Repository(MAC_LAUNCHER_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD, MAC_LAUNCHER_SRC_DIR)
print "Creating Windows Closer SVN connection"
windowsCloserHttpRepository = SVNUtilities.Repository(WINDOWS_CLOSER_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD, WINDOWS_CLOSER_SRC_DIR)
print "Creating Mac Closer SVN connection"
macCloserHttpRepository = SVNUtilities.Repository(MAC_CLOSER_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD, MAC_CLOSER_SRC_DIR)


print "Getting latest version of installer code..."
installerHttpRespository.updatePath("")
installerCloserHttpRespository.updatePath("")
windowsLauncherHttpRepository.updatePath("")
print "Done updating installer."

print "Getting latest version of the Windows Launcher code..."
windowsLauncherHttpRepository.updatePath("")
print "Done updating Windows Launcher."

print "Getting latest version of the Mac Launcher code..."
macLauncherHttpRepository.updatePath("")
print "Done updating Mac Launcher."

print "Getting latest version of the Windows Closer code..."
windowsCloserHttpRepository.updatePath("")
print "Done updating Windows Launcher."

print "Getting latest version of the Mac Closer code..."
macCloserHttpRepository.updatePath("")
print "Done updating Mac Launcher."


launcherBuildObjects = [
	AliceBuildObject.AliceBuildObject("aliceWindows", "windows", WINDOWS_LAUNCHER_SRC_DIR, BUILD_OUTPUT_DIR),
	#AliceBuildObject.AliceBuildObject("aliceMac", "mac", MAC_LAUNCHER_SRC_DIR, BUILD_OUTPUT_DIR)
]
closerBuildObjects = [
	AliceBuildObject.AliceBuildCloserObject("aliceWindowsCloser", "windows", WINDOWS_CLOSER_SRC_DIR, BUILD_OUTPUT_DIR),
	#AliceBuildObject.AliceBuildCloserObject("aliceMacCloser", "mac", MAC_CLOSER_SRC_DIR, BUILD_OUTPUT_DIR),
]
buildObjects = []

CHECK_FOR_NEW_DATA = False

print "Checking and pulling source data from source control..."
for (componentName, componentType), packages in componentMap.items():
	print componentName, componentType
	currentBuildObject = None
	if (componentType == "jar"):
		currentBuildObject = JarUtilities.JarBuildObject(componentName, JAR_SRC_DIR, BUILD_OUTPUT_DIR)
	elif (componentType == "aliceData"):
		currentBuildObject = AliceDataBuildObject.AliceDataBuildObject(componentName, ALICE_APP_SRC_DIR, BUILD_OUTPUT_DIR)
	elif (componentType == "gallery"):
		(actualName, directoryName) = componentName
		currentBuildObject = GalleryBuildObject.GalleryBuildObject(actualName, GALLERY_SRC_DIR, BUILD_OUTPUT_DIR)
		currentBuildObject.setDirectoryName(directoryName)
	for packageName, roots in packages:
		print "package name: "+packageName+", roots: "+str(roots)
		currentPackage = SVNUtilities.SVNPackage(packageName, roots)
		repository = None
		#Pick the repository to pull the data from
		if (componentType == "jar"):
			repository = jarHttpRepository
			if (packageName.startswith("lg")):
				repository = jarSshRespository
		elif (componentType == "aliceData"):
			repository = aliceAppHttpRepository
		elif (componentType == "gallery"):
			repository = galleryAppHttpRepository

		currentPackage.previousRevision = repository.getLocalCommittedRevision(packageName)
		if (CHECK_FOR_NEW_DATA):
			currentPackage.newRevision = repository.getRemoteRevision(packageName)
			if (currentPackage.isNew()):
					print packageName + " is at revision "+str(currentPackage.previousRevision) + " and is being updated to "+str(currentPackage.newRevision)
					repository.updatePath(packageName)
					print "done updating "+packageName
		currentBuildObject.addPackage(currentPackage)

	if (currentBuildObject != None):
		buildObjects.append(currentBuildObject)

print "Done getting source data."

versionInfo = VersionUtilities.VersionInfo(BUILD_OUTPUT_DIR)
needNewVersion = False
for buildObject in buildObjects:
	if (buildObject.isNew()):
		needNewVersion = True



needNewVersion = FORCE_NEW_VERSION
needNewComponents = FORCE_NEW_COMPONENTS
lastVersion = versionInfo.getLatestVersion()
if (lastVersion == None):
	needNewVersion = True
	needNewComponents = True


if (needNewVersion):
	newVersionNum = versionInfo.getNewVersionNum()
	print "New version num: "+str(newVersionNum)
	if newVersionNum == None:
		newVersionNum = VersionUtilities.VersionNum()
		newVersionNum.setVals([3,0,0,0,BASE_VERSION])
		print "New version num is now: "+str(newVersionNum)
	baseDir = BUILD_OUTPUT_DIR + "/" + str(newVersionNum)
	FileUtilities.makeDir(baseDir)
	if (needNewVersion):
		print "Data is new..."
	else:
		print "Forcing new version..."
	print "Making new version "+str(newVersionNum) + " at "+baseDir
	newVersionObject = VersionUtilities.SingleVersion(newVersionNum)

	if (FORCE_NEW_COMPONENTS):
		print "Forcing new components..."

	print "Building components..."

	#loop through all the versioned elements and make new components for anything that's new
	#add the old version to the new version object for anything that hasn't changed
	for buildObject in buildObjects:
		needNew = True
		componentToAdd = None
		if ( not needNewComponents and not buildObject.isNew() ):
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
	for launcher in launcherBuildObjects:
		launcher.setVersionNum(newVersionNum)
		launcher.buildObject()
		#add the launcher object to the build objects so it will get added to the installer
		buildObjects.append(launcher)

	for closer in closerBuildObjects:
		closer.setVersionNum(newVersionNum)
		closer.buildObject()
		#add the launcher object to the build objects so it will get added to the installer
		buildObjects.append(closer)

	print "Done building components."

	newVersionObject.writeToFile(BUILD_OUTPUT_DIR)
	print "Wrote new version data: "+str(newVersionObject)

	print "Making installer..."
	FileUtilities.makeDir(INSTALLERS_DIR)
	installer = InstallerUtilities.InstallerProject("AliceTestInstaller", newVersionNum)

	mainInstallers = []
	closerInstallers = []
	nonMainInstallersIDs = []
	for buildObject in buildObjects:
		print "Trying to add installer for "+buildObject.name+", with data at "+str(buildObject.dataLocation)
		if (buildObject.dataLocation != None):
			"Adding installer for "+buildObject.name
			buildComponent = InstallerUtilities.InstallComponent(buildObject)
			buildComponent.setInstallPath(buildObject.getRelativeLocation())
			
			#Setting the dependencies
			minVersion = VersionUtilities.VersionNum()
			minVersion.setVals([3,0,0,0,0])
			maxVersion = VersionUtilities.VersionNum()
			maxVersion.setVals([3,0,0,1,0])
			dependencyName = InstallerUtilities.makeValidID(launcherBuildObjects[0].name)
			
			buildComponent.setDependency(dependencyName, minVersion, maxVersion)

			if (buildObject.isMainInstallObject):
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
			if (mainInstaller.platform == closer.platform):
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











