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
JAR_SRC_DIR = "C:/AliceSource/"
BUILD_OUTPUT_DIR = "C:/AliceJars"

#sys.stdout = sys.stderr

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

if (SYNC_SOURCE):
    if (BUILD_FROM_TRUNK):
	currentAliceSourceRepository = SVNUtilities.SVNConnection(TRUNK_JAR_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD)
	currentWashUSourceRepository = SVNUtilities.SVNConnection(TRUNK_JAR_SSH_SVN_URL, SSH_SVN_USERNAME, SSH_KEY_PASSPHRASE, SSH_KEY_FILE)
    else:
	currentAliceSourceRepository = SVNUtilities.SVNConnection(CURRENT_JAR_HTTP_SVN_URL, HTTP_SVN_USERNAME, HTTP_SVN_PASSWORD)
	currentWashUSourceRepository = SVNUtilities.SVNConnection(CURRENT_JAR_SSH_SVN_URL, SSH_SVN_USERNAME, SSH_KEY_PASSPHRASE, SSH_KEY_FILE)

#We never compare to the previous revision, so we don't need the previous repositories
previousAliceSourceRepository = None
previousWashUSourceRepository = None


componentList = [
	( ("foundation", "jar"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_foundation", "C:/AliceSource/rt_foundation", SYNC_SOURCE),
	] ),
	( ("moveandturn", "jar"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_moveandturn", "C:/AliceSource/rt_moveandturn", SYNC_SOURCE),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_moveandturn_generated", "C:/AliceSource/rt_moveandturn_generated", SYNC_SOURCE),
	] ),
	( ("lg_walkandtouch", "jar"), [
		SVNUtilities.Repository(previousWashUSourceRepository, currentWashUSourceRepository, "lg_walkandtouch", "C:/AliceSource/lg_walkandtouch", SYNC_SOURCE),
		SVNUtilities.Repository(previousWashUSourceRepository, currentWashUSourceRepository, "lg_walkandtouch_gallery_generated", "C:/AliceSource/lg_walkandtouch_gallery_generated", SYNC_SOURCE),
	] ),
	( ("stage", "jar"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_storytelling", "C:/AliceSource/rt_storytelling", SYNC_SOURCE),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_storytelling_private", "C:/AliceSource/rt_storytelling_private", SYNC_SOURCE),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_storytelling_generated_depended_upon", "C:/AliceSource/rt_storytelling_generated_depended_upon", SYNC_SOURCE)
	] ),
	( ("ide", "jar"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "rt_issue", "C:/AliceSource/rt_issue", SYNC_SOURCE),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_ide", "C:/AliceSource/x_ide", SYNC_SOURCE),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_ide_stage", "C:/AliceSource/x_ide_stage", SYNC_SOURCE),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_ide_i18n", "C:/AliceSource/x_ide_i18n", SYNC_SOURCE),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_ide_private", "C:/AliceSource/x_ide_private", SYNC_SOURCE),
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "x_youtube", "C:/AliceSource/x_youtube", SYNC_SOURCE)
	] ),
	( ("jar_swingworker", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_swingworker", "C:/AliceSource/jar_swingworker", SYNC_SOURCE),
	] ),
	( ("jar_jogl", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_jogl", "C:/AliceSource/jar_jogl", SYNC_SOURCE),
	] ),
	( ("jar_mail", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_mail", "C:/AliceSource/jar_mail", SYNC_SOURCE),
	] ),
	( ("jar_rpc", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_rpc", "C:/AliceSource/jar_rpc", SYNC_SOURCE),
	] ),
	( ("jar_apple", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_apple", "C:/AliceSource/jar_apple", SYNC_SOURCE),
	] ),
	( ("jar_jaf", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_jaf", "C:/AliceSource/jar_jaf", SYNC_SOURCE),
	] ),
	( ("jar_jira", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_jira", "C:/AliceSource/jar_jira", SYNC_SOURCE),
	] ),
	( ("jar_youtube", "external"), [
		SVNUtilities.Repository(previousAliceSourceRepository, currentAliceSourceRepository, "jar_youtube", "C:/AliceSource/jar_youtube", SYNC_SOURCE),
	] ),
]

buildObjects = []

CHECK_FOR_NEW_DATA = True
HEAD_REVISION = core.wc.SVNRevision.HEAD

REVISION_TO_USE = HEAD_REVISION

jarObjects = []

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
