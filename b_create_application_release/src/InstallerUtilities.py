import BuildObject
import os.path
import os,sys
import shutil
import FileUtilities
import java
import BuildJava

__author__="Dave Culyba"
__date__ ="$May 7, 2009 3:10:42 PM$"

BASE_DIR = "C:/AliceBuild_Test"


def makeValidID(name):
	print "Name before: "+name
	name = name.strip()
	name = name.lower()
	toDelete = []

	for i in range(len(name)):
		c = name[i]
		if (not c.isalnum()):
			toDelete.append(i)
	deleteCount = 0
	for i in toDelete:
		index = i - deleteCount
		name = name[0:index] + name[index+1:len(name)]
		#name.remove(i - deleteCount)
		deleteCount += 1
	print "Name after: "+name
	return name

class InstallComponent:
	COMPONENT_TEMPLATE = "C:/AliceBuild/InstallerTemplates/componentTemplate"
	ALICE_COMPONENT_TEMPLATE = "C:/AliceBuild/InstallerTemplates/AliceComponentTemplate"
	ALICE_COMPONENT_CLOSER_TEMPLATE = "C:/AliceBuild/InstallerTemplates/AliceCloserTemplate"
	COMPONENT_PATH = "ext/components/products"
	INFRA_TEMPLATE = "C:/AliceBuild/InstallerTemplates/infraTemplate"
	MULTI_PLATFORM_INFRA_TEMPLATE = "C:/AliceBuild/InstallerTemplates/MultiplatformInfraTemplate"
	ALICE_INFRA_TEMPLATE = "C:/AliceBuild/InstallerTemplates/AliceInfraTemplate"
	ALICE_INFRA_CLOSER_TEMPLATE = "C:/AliceBuild/InstallerTemplates/AliceCloserInfraTemplate"
	INFRA_PATH = "ext/infra/build/products"

	PRODUCT_DESCRIPTION_KEY = "_ProductDescription_"
	PRODUCT_ID_KEY = "_ProductID_"
	PRODUCT_NAME_KEY = "_ProductName_"
	PRODUCT_DIRECTORY_KEY = "_ProductDirectory_"
	VISIBLE_NAME_KEY = "_VisibleProductName_"
	DATA_PATH_KEY = "_DataPath_"
	WINDOWS_DATA_PATH_KEY = "_WindowsDataPath_"
	MAC_DATA_PATH_KEY = "_MacDataPath_"
	INSTALL_LOCATION_KEY = "_InstallLocation_"
	VERSION_KEY = "_VersionNumber_"
	IS_ZIP_KEY = "_IsZip_"
	DEFAULT_INSTALL_DIRECTORY = "_DefaultDirectory_"
	DEPENDENT_ID_KEY = "_DependentID_"
	DEPENDENT_MIN_VERSION_KEY = "_DependentMinVersion_"
	DEPENDENT_MAX_VERSION_KEY = "_DependentMaxVersion_"
	INSTALL_AFTERS_KEY = "_InstallAfters_"
	DEPENDENCIES_KEY = "_ProductDependencies_"


	def __init__(self, buildObject):
		self.name = buildObject.name
		self.version = buildObject.versionNum
		self.isLauncher = buildObject.isMainInstallObject
		self.replacementMap = []
		self.isCloser = buildObject.isCloser
		self.isMultiPlatform = buildObject.isMultiPlatform
		self.platform = buildObject.platform
		if (self.isMultiPlatform):
			for platform in BuildObject.MultiPlatformBuildObject.PLATFORMS:
				self.addStringReplacement((BuildObject.MultiPlatformBuildObject.PLATFORMS_TO_KEYS[platform], "file:/"+buildObject.getDataLocation(platform)))
		else:
			self.dataPath = "file:/"+buildObject.dataLocation
			self.addStringReplacement((InstallComponent.DATA_PATH_KEY, self.dataPath))
		self.id = makeValidID(self.name)
		
		

		self.productDir = self.id
		self.isZip = buildObject.isZip
		self.setIsZip(self.isZip)
		self.addStringReplacement((InstallComponent.PRODUCT_ID_KEY, self.id))
		self.addStringReplacement((InstallComponent.PRODUCT_NAME_KEY, self.name))
		self.addStringReplacement((InstallComponent.PRODUCT_DIRECTORY_KEY, self.productDir))
		self.addStringReplacement((InstallComponent.VERSION_KEY, str(self.version)))
		

	def hasKey(self, key):
		for toFind, replacement in self.replacementMap:
			if (toFind == key):
				return True
		return False

	def removeKey(self, key):
		index = -1
		count = 0
		for toFind, replacement in self.replacementMap:
			if (toFind == key):
				index = count
				break
			count += 1
		if (index != -1):
			self.replacementMap.remove(index)

	def setInstallAfters(self, installAfters):
		self.removeKey(InstallComponent.INSTALL_AFTERS_KEY)
		self.addStringReplacement((InstallComponent.INSTALL_AFTERS_KEY, installAfters))

	def setDependencies(self, dependencies):
		self.removeKey(InstallComponent.DEPENDENCIES_KEY)
		self.addStringReplacement((InstallComponent.DEPENDENCIES_KEY, dependencies))

	def setDependency(self, id, minVersion, maxVersion):
		self.removeKey(InstallComponent.DEPENDENT_ID_KEY)
		self.removeKey(InstallComponent.DEPENDENT_MIN_VERSION_KEY)
		self.removeKey(InstallComponent.DEPENDENT_MAX_VERSION_KEY)

		self.addStringReplacement((InstallComponent.DEPENDENT_ID_KEY, id))
		self.addStringReplacement((InstallComponent.DEPENDENT_MIN_VERSION_KEY, str(minVersion)))
		self.addStringReplacement((InstallComponent.DEPENDENT_MAX_VERSION_KEY, str(maxVersion)))

	def setIsZip(self,isZip):
		self.removeKey(InstallComponent.IS_ZIP_KEY)
		if (isZip):
			self.addStringReplacement((InstallComponent.IS_ZIP_KEY, "true"))
		else:
			self.addStringReplacement((InstallComponent.IS_ZIP_KEY, "false"))
	
	def setProductDirectory(self, directory):
		self.productDir = directory
		self.removeKey(InstallComponent.PRODUCT_DIRECTORY_KEY)
		self.addStringReplacement((InstallComponent.PRODUCT_DIRECTORY_KEY, directory))

	def setDefaultInstallDirectoryName(self, defaultDirectory):
		self.removeKey(InstallComponent.DEFAULT_INSTALL_DIRECTORY)
		self.addStringReplacement((InstallComponent.DEFAULT_INSTALL_DIRECTORY, defaultDirectory))

	def setInstallPath(self, installPath):
		print self.name + " set install path to "+str(installPath)
		self.removeKey(InstallComponent.INSTALL_LOCATION_KEY)
		self.addStringReplacement((InstallComponent.INSTALL_LOCATION_KEY, installPath))

	def setVisibleName(self, visibleName):
		self.removeKey(InstallComponent.VISIBLE_NAME_KEY)
		self.addStringReplacement((InstallComponent.VISIBLE_NAME_KEY, visibleName))

	def setDescription(self, description):
		self.removeKey(InstallComponent.PRODUCT_DESCRIPTION_KEY)
		self.addStringReplacement((InstallComponent.PRODUCT_DESCRIPTION_KEY, description))

	def addStringReplacement(self, stringReplacement):
		self.replacementMap.append(stringReplacement)

	def makeNewInstaller(self, dir):
		print self.productDir
		self.componentDir = dir + "/" + InstallComponent.COMPONENT_PATH + "/" + self.productDir
		print self.componentDir

		#put default values in for any keys that aren't in the map
		if (not self.hasKey(InstallComponent.PRODUCT_DESCRIPTION_KEY)):
			self.addStringReplacement((InstallComponent.PRODUCT_DESCRIPTION_KEY, "Description for "+self.name))
		if (not self.hasKey(InstallComponent.VISIBLE_NAME_KEY)):
			self.addStringReplacement((InstallComponent.VISIBLE_NAME_KEY, self.name))
		if (not self.hasKey(InstallComponent.INSTALL_LOCATION_KEY)):
			self.addStringReplacement((InstallComponent.INSTALL_LOCATION_KEY, ""))
		if (not self.hasKey(InstallComponent.IS_ZIP_KEY)):
			self.addStringReplacement((InstallComponent.IS_ZIP_KEY, "false"))

		if (os.path.exists(self.componentDir)):
			print self.componentDir + " already exists, deleting it."
			shutil.rmtree(self.componentDir)
		else:
			FileUtilities.makeDirsForFile(self.componentDir)

		componentTemplate = InstallComponent.COMPONENT_TEMPLATE
		if (self.isLauncher):
			componentTemplate = InstallComponent.ALICE_COMPONENT_TEMPLATE
		elif (self.isCloser):
			componentTemplate = InstallComponent.ALICE_COMPONENT_CLOSER_TEMPLATE
		FileUtilities.copyDir(componentTemplate, self.componentDir)
		FileUtilities.renameDirs(self.replacementMap, self.componentDir)
		os.path.walk(self.componentDir, FileUtilities.replaceStrings, self.replacementMap)

		self.infraDir = dir + "/" + InstallComponent.INFRA_PATH + "/" +self.productDir
		print self.infraDir
		if (os.path.exists(self.infraDir)):
			print self.infraDir + " already exists, deleting it."
			shutil.rmtree(self.infraDir)
		else:
			FileUtilities.makeDirsForFile(self.infraDir)

		infraTemplate = InstallComponent.INFRA_TEMPLATE
		if (self.isLauncher):
			infraTemplate = InstallComponent.ALICE_INFRA_TEMPLATE
		elif (self.isCloser):
			infraTemplate = InstallComponent.ALICE_INFRA_CLOSER_TEMPLATE
		elif (self.isMultiPlatform):
			infraTemplate = InstallComponent.MULTI_PLATFORM_INFRA_TEMPLATE

		FileUtilities.copyDir(infraTemplate, self.infraDir)
		FileUtilities.renameDirs(self.replacementMap, self.infraDir)
		os.path.walk(self.infraDir, FileUtilities.replaceStrings, self.replacementMap)

class InstallerProject:
	BASE_INSTALLER_TEMPLATE = "C:/AliceBuild/InstallerTemplates/installerTemplate"
	BUILD_XML_FILE = "build/build.xml"
	BUILD_DIR = "build"
	CLEAN_KEY = "<!--CleanProducts-->"
	CLEAN_TEMPLATE = "C:/AliceBuild/InstallerTemplates/cleanTemplate.template"
	BUILD_KEY = "<!--BuildProduct-->"
	BUILD_TEMPLATE = "C:/AliceBuild/InstallerTemplates/buildTemplate.template"
	#BUNDLE_KEY = "<!--BundleComponent-->"
	MAC_BUNDLE_KEY = "<!--MacBundleComponent-->"
	WINDOWS_BUNDLE_KEY = "<!--WindowsBundleComponent-->"
	BUNDLE_TEMPLATE = "C:/AliceBuild/InstallerTemplates/bundleTemplate.template"
	#LIMITED_BUNDLE_KEY = "<!--LimitedBundleComponent-->"
	MAC_LIMITED_BUNDLE_KEY = "<!--MacLimitedBundleComponent-->"
	WINDOWS_LIMITED_BUNDLE_KEY = "<!--WindowsLimitedBundleComponent-->"

	def __init__(self, name, version):
		self.components = []
		self.launcherComponents = []
		self.name = name
		self.version = version

	def addComponent(self, component):
		if (component.isLauncher):
			self.launcherComponents.append(component)
		else:
			self.components.append(component)

	def writeToFile(self, baseDir):
		#copy the template to the new location
		self.projectDir = baseDir + "/" + self.name+"_"+str(self.version)
		print self.projectDir

		if (os.path.exists(self.projectDir)):
			print self.projectDir + " already exists, deleting it."
			shutil.rmtree(self.projectDir)
		else:
			FileUtilities.makeDirsForFile(self.projectDir)
		FileUtilities.copyDir(InstallerProject.BASE_INSTALLER_TEMPLATE, self.projectDir)

		cleanTemplateString = FileUtilities.getStringFromFile(InstallerProject.CLEAN_TEMPLATE)
		bundleTemplateString = FileUtilities.getStringFromFile(InstallerProject.BUNDLE_TEMPLATE)
		buildTemplateString = FileUtilities.getStringFromFile(InstallerProject.BUILD_TEMPLATE)

		cleanString = ""
		windowsBundleString = ""
		macBundleString = ""
		buildString = ""
		windowsLimitedBundleString = ""
		macLimitedBundleString = ""

		
		for component in self.components:
			component.makeNewInstaller(self.projectDir)
			cleanString += FileUtilities.replaceInString(cleanTemplateString, component.replacementMap) +"\n"

			componentBundleString = FileUtilities.replaceInString(bundleTemplateString, component.replacementMap) +"\n"
			if (component.platform == BuildObject.PlatformSpecificBuildObject.WINDOWS_PLATFORM):
				windowsBundleString += componentBundleString
			elif (component.platform == BuildObject.PlatformSpecificBuildObject.MAC_PLATFORM):
				macBundleString += componentBundleString
			else:
				macBundleString += componentBundleString
				windowsBundleString += componentBundleString
			buildString += FileUtilities.replaceInString(buildTemplateString, component.replacementMap) +"\n"
			if (component.version == self.version):
				if (component.platform == BuildObject.PlatformSpecificBuildObject.WINDOWS_PLATFORM):
					windowsLimitedBundleString += componentBundleString
				elif (component.platform == BuildObject.PlatformSpecificBuildObject.MAC_PLATFORM):
					macLimitedBundleString += componentBundleString
				else:
					macLimitedBundleString += componentBundleString
					windowsLimitedBundleString += componentBundleString
		for launcherComponent in self.launcherComponents:
			launcherComponent.makeNewInstaller(self.projectDir)
			cleanString += FileUtilities.replaceInString(cleanTemplateString, launcherComponent.replacementMap) +"\n"
			buildString += FileUtilities.replaceInString(buildTemplateString, launcherComponent.replacementMap) +"\n"

			launcherBundleString = FileUtilities.replaceInString(bundleTemplateString, launcherComponent.replacementMap) +"\n"

			if (launcherComponent.platform == BuildObject.PlatformSpecificBuildObject.WINDOWS_PLATFORM):
				windowsBundleString += launcherBundleString
				windowsLimitedBundleString += launcherBundleString
			elif (launcherComponent.platform == BuildObject.PlatformSpecificBuildObject.MAC_PLATFORM):
				macBundleString += launcherBundleString
				macLimitedBundleString += launcherBundleString
			else:
				macBundleString += launcherBundleString
				windowsBundleString += launcherBundleString
				macLimitedBundleString += launcherBundleString
				windowsLimitedBundleString += launcherBundleString

		replacementMap = [
			(InstallComponent.PRODUCT_NAME_KEY, self.name),
			(InstallComponent.VERSION_KEY, str(self.version)),
			(InstallerProject.CLEAN_KEY, cleanString),
			(InstallerProject.BUILD_KEY, buildString),
			(InstallerProject.WINDOWS_BUNDLE_KEY, windowsBundleString),
			(InstallerProject.WINDOWS_LIMITED_BUNDLE_KEY, windowsLimitedBundleString),
			(InstallerProject.MAC_BUNDLE_KEY, macBundleString),
			(InstallerProject.MAC_LIMITED_BUNDLE_KEY, macLimitedBundleString),
			]

		FileUtilities.replaceStringsInFile(replacementMap, self.projectDir + "/" + InstallerProject.BUILD_XML_FILE)
		projectDirJavaFile = java.io.File( self.projectDir, InstallerProject.BUILD_DIR  )
		BuildJava.buildProject(projectDirJavaFile)



