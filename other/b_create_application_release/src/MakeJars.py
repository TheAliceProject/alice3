print "having difficulties?"
print "install ant."
print "JAVA_HOME set?"
print "dave's JAVA_HOME?"
print "build.xmls for lg?"

import JarUtilities
import SVNUtilities
import VersionUtilities
import AliceDataBuildObject
import FileUtilities
from org.tmatesoft.svn import core
import java
import sys


##fix the ant build.xml files
#FileUtilities.replaceStringsInFiles([("../../", "../")], "C:/AliceSource", ["build.xml"])
#sys.exit(0)

__author__="Dave Culyba"
__date__ ="$Jan 14, 2010 4:58:39 PM$"


HTTP_SVN_USERNAME = "dculyba"
HTTP_SVN_PASSWORD = "bkmdlmc"
SSH_SVN_USERNAME = "culybad"
SSH_KEY_FILE = java.io.File("C:/putty/washU_private.key")
SSH_KEY_PASSPHRASE = "bkmdlmc"

BUILD_FROM_TRUNK = True
USE_WALK_AND_TOUCH_JAR = False
VERSION_TO_MAKE = VersionUtilities.VersionNum("3.0.0.0.65")
JAR_SRC_DIR = "C:/AliceSource2/"
BUILD_OUTPUT_DIR = "C:/AliceJars"

#sys.stdout = sys.stderr

CHECKOUT_SOURCE = True

SYNC_SOURCE = True
BUILD_CODE = True


CURRENT_JAR_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3/branches/"+str(VERSION_TO_MAKE)+"/"
CURRENT_JAR_SSH_SVN_URL = "svn+ssh://culybad@invent.cse.wustl.edu/project/invent/lookingglass/repos/trunk/"
CURRENT_BUILD_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/branches/"+str(VERSION_TO_MAKE)+"/"

TRUNK_JAR_HTTP_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3/trunk/"
TRUNK_JAR_SSH_SVN_URL = "svn+ssh://culybad@invent.cse.wustl.edu/project/invent/lookingglass/repos/trunk/"
TRUNK_BUILD_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/"

INSTALLER_BUILD_SVN_URL = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3_build/"

currentAliceSourceRepository = None
currentWashUSourceRepository = None

if (CHECKOUT_SOURCE or SYNC_SOURCE):
    if (BUILD_FROM_TRUNK):
	currentAliceSourceRepository = SVNUtilities.SVNConnection(TRUNK_JAR_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD)
	currentWashUSourceRepository = SVNUtilities.SVNConnection(TRUNK_JAR_SSH_SVN_URL, SSH_SVN_USERNAME, SSH_KEY_PASSPHRASE, SSH_KEY_FILE)
    else:
	currentAliceSourceRepository = SVNUtilities.SVNConnection(CURRENT_JAR_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD)
	currentWashUSourceRepository = SVNUtilities.SVNConnection(CURRENT_JAR_SSH_SVN_URL, SSH_SVN_USERNAME, SSH_KEY_PASSPHRASE, SSH_KEY_FILE)

#we only want to switch the source if we're syncing and we aren't doing a fresh checkout
shouldSwitchSource = not CHECKOUT_SOURCE and SYNC_SOURCE

#We never compare to the previous revision, so we don't need the previous repositories
previousAliceSourceRepository = None
previousWashUSourceRepository = None


componentList = [
	( ("foundation", "jar"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_foundation", JAR_SRC_DIR + "rt_foundation", shouldSwitchSource),
	] ),
	( ("moveandturn", "jar"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_moveandturn", JAR_SRC_DIR + "rt_moveandturn", shouldSwitchSource),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_moveandturn_generated", JAR_SRC_DIR + "rt_moveandturn_generated", shouldSwitchSource),
	] ),
	( ("lg_walkandtouch", "jar"), [
		SVNUtilities.Repository(previousWashUSourceRepository, currentWashUSourceRepository, "lg_walkandtouch", JAR_SRC_DIR + "lg_walkandtouch", shouldSwitchSource),
		SVNUtilities.Repository(previousWashUSourceRepository, currentWashUSourceRepository, "lg_walkandtouch_gallery_generated", JAR_SRC_DIR + "lg_walkandtouch_gallery_generated", shouldSwitchSource),
	] ),
	( ("stage", "jar"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_storytelling", JAR_SRC_DIR + "rt_storytelling", shouldSwitchSource),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_storytelling_private", JAR_SRC_DIR + "rt_storytelling_private", shouldSwitchSource),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_storytelling_generated_depended_upon", JAR_SRC_DIR + "rt_storytelling_generated_depended_upon", shouldSwitchSource)
	] ),
	( ("ide", "jar"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_issue", JAR_SRC_DIR + "rt_issue", shouldSwitchSource),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_ide", JAR_SRC_DIR + "x_ide", shouldSwitchSource),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_ide_stage", JAR_SRC_DIR + "x_ide_stage", shouldSwitchSource),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_ide_i18n", JAR_SRC_DIR + "x_ide_i18n", shouldSwitchSource),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_ide_private", JAR_SRC_DIR + "x_ide_private", shouldSwitchSource),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_youtube", JAR_SRC_DIR + "x_youtube", shouldSwitchSource)
	] ),
	( ("jar_swingworker", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_swingworker", JAR_SRC_DIR + "jar_swingworker", shouldSwitchSource),
	] ),
	( ("jar_jogl", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_jogl", JAR_SRC_DIR + "jar_jogl", shouldSwitchSource),
	] ),
	( ("jar_mail", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_mail", JAR_SRC_DIR + "jar_mail", shouldSwitchSource),
	] ),
	( ("jar_rpc", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_rpc", JAR_SRC_DIR + "jar_rpc", shouldSwitchSource),
	] ),
	( ("jar_apple", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_apple", JAR_SRC_DIR + "jar_apple", shouldSwitchSource),
	] ),
	( ("jar_jaf", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_jaf", JAR_SRC_DIR + "jar_jaf", shouldSwitchSource),
	] ),
	( ("jar_jira", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_jira", JAR_SRC_DIR + "jar_jira", shouldSwitchSource),
	] ),
	( ("jar_youtube", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_youtube", JAR_SRC_DIR + "jar_youtube", shouldSwitchSource),
	] ),
]

buildObjects = []

CHECK_FOR_NEW_DATA = True
HEAD_REVISION = core.wc.SVNRevision.HEAD

REVISION_TO_USE = HEAD_REVISION

jarObjects = []

if (CHECKOUT_SOURCE):
    for (componentName, componentType), repositories in componentList:
	for repository in repositories:
	    repository.checkoutCurrent()


print "Checking and pulling source data from source control..."
for (componentName, componentType), repositories in componentList:
	print componentName, componentType
	currentBuildObject = None
	if (componentType == "jar"):
		currentBuildObject = JarUtilities.JarBuildObject(componentName, repositories, BUILD_OUTPUT_DIR)
		jarObjects.append(currentBuildObject)
	elif (componentType == "external"):
		currentBuildObject = AliceDataBuildObject.AliceDataBuildObject(componentName, repositories, BUILD_OUTPUT_DIR)
		currentBuildObject.includeInBuild = False
	print "trying to update "+currentBuildObject.name
	if (SYNC_SOURCE):
	    currentBuildObject.updateFromRepository()
	if (currentBuildObject != None):
		buildObjects.append(currentBuildObject)

print "Done getting source data."

baseDir = BUILD_OUTPUT_DIR + "/" + str(VERSION_TO_MAKE)
print "Making new version "+str(VERSION_TO_MAKE) + " at "+baseDir
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
	if (needNew):
	    initedObject = False
	    if (USE_WALK_AND_TOUCH_JAR and buildObject.name == "lg_walkandtouch"):
		if (buildObject.forceFromExistingVersion(VERSION_TO_MAKE, JAR_SRC_DIR + "jar_walkandtouch/lg_walkandtouch.jar")):
		    newComponent = VersionUtilities.ComponentVersion(buildObject.name, VERSION_TO_MAKE)
		    newVersionObject.addComponent(newComponent)
		    initedObject = True
		    print "successful init from existing data for "+buildObject.name
	    if (not initedObject):
		print "Making version "+str(VERSION_TO_MAKE)+" for component "+buildObject.name
		buildObject.setVersionNum(VERSION_TO_MAKE)
		buildObject.buildObject(compileCode = BUILD_CODE)
		print "Done making new "+buildObject.name
