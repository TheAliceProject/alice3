import os.path

import os
import java

__author__="Dave Culyba"
__date__ ="$May 5, 2009 5:23:18 PM$"

VERSION_FILE_HEADER = "AliceVersion"
VERSION_SEPARATOR = "_"
VERSION_EXTENSION = ".txt"

class VersionNum:
	def __init__(self):
		self.vals = [0,0,0,0,0]
		self.major = self.vals[0]
		self.minor = self.vals[4]

	def initFromString(self, versionString):
		versionStrings = versionString.split('.')
		self.vals = []
		for versionPart in versionStrings:
			self.vals.append(int(versionPart))
		self.major = self.vals[0]
		self.minor = self.vals[4]

	def setVals(self, vals):
		self.vals = vals[:]
		self.major = self.vals[0]
		self.minor = self.vals[4]

	def __repr__(self):
		return str(self.vals[0])+"."+str(self.vals[1])+"."+str(self.vals[2])+"."+str(self.vals[3])+"."+str(self.vals[4])

	def __lt__(self, other):
		for i in range(len(self.vals)):
			if (self.vals[i] < other.vals[i]):
				return True
			elif (self.vals[i] > other.vals[i]):
				return False
		return False #equals case

	def __le__(self, other):
		for i in range(len(self.vals)):
			if (self.vals[i] < other.vals[i]):
				return True
			elif (self.vals[i] > other.vals[i]):
				return False
		return True #equals case

	def __eq__(self, other):
		for i in range(len(self.vals)):
			if (self.vals[i] != other.vals[i]):
				return False
		return True

	def __ne__(self, other):
		for i in range(len(self.vals)):
			if (self.vals[i] != other.vals[i]):
				return True
		return False

	def __gt__(self, other):
		for i in range(len(self.vals)):
			if (self.vals[i] > other.vals[i]):
				return True
			elif (self.vals[i] < other.vals[i]):
				return False
		return False #equals case

	def __ge__(self, other):
		for i in range(len(self.vals)):
			if (self.vals[i] > other.vals[i]):
				return True
			elif (self.vals[i] < other.vals[i]):
				return False
		return True #equals case

class ComponentVersion:
	def __init__(self, name, version):
		self.name = name
		self.version = version

	def initFromString(self, componentString):
		componentString = componentString.strip()
		data = componentString.split(' ')
		if (len(data) == 2):
			self.name = data[0]
			self.version = VersionNum()
			self.version.initFromString(data[1])
		else:
			print "ERROR GETTING COMPONENT INFO FROM "+componentString

	def __str__(self):
		return self.name + " " + str(self.version)

class SingleVersion:

	def __lt__(self, other):
		return self.version < other.version

	def __le__(self, other):
		return self.version <= other.version

	def __eq__(self, other):
		return self.version == other.version

	def __ne__(self, other):
		return self.version != other.version

	def __gt__(self, other):
		return self.version > other.version

	def __ge__(self, other):
		return self.version >= other.version

	def initFromFile(self, versionFile):
		splitStrings = versionFile.split(VERSION_SEPARATOR)
		if (len(splitStrings) != 3):
			print "FAILED TO GET VALID VERSION FILE WITH '"+versionFile+"'"
			return
		versionString = splitStrings[1]
		self.version = VersionNum()
		self.version.initFromString(versionString)
		self.components = []
		infile = open(versionFile, "r")
		while infile:
			line = infile.readline()
			if not line:
				break;
			component = ComponentVersion("", None)
			component.initFromString(line)
			self.components.append(component)
		infile.close()

	def getComponentNamed(self, componentName):
		for component in self.components:
			if (component.name == componentName):
				return component
		return None

	def addComponent(self, component):
		self.components.append(component)

	def writeToFile(self, directory):
		versionText = str(self.version)
		versionFileName = VERSION_FILE_HEADER + VERSION_SEPARATOR + versionText + VERSION_SEPARATOR + VERSION_EXTENSION
		versionFile =  directory + "/" + str(self.version) + "/" + versionFileName
		outfile = open(versionFile, "w")
		for component in self.components:
			outfile.write(str(component)+"\n")
		outfile.close()

	def __init__(self, version):
		self.version = version
		self.components = []

	def __str__(self):
		toReturn = str(self.version) +" : ["
		for i in range(len(self.components)):
			toReturn += str(self.components[i])
			if (i < len(self.components) - 1):
				toReturn += ", "
		toReturn += "]"
		return toReturn

class VersionInfo:

	def __init__(self, rootDir):
		self.rootDir = rootDir
		dirs = os.listdir(rootDir)
		self.versions = []
		for dir in dirs:
			dirPath = rootDir +"/"+ dir
			if (os.path.isdir(dirPath)):
				files = os.listdir(dirPath)
				for file in files:
					if (file.startswith(VERSION_FILE_HEADER)):
						component = SingleVersion(None)
						component.initFromFile(dirPath + "/" + file)
						self.versions.append(component)
						break

		self.versions.sort()

		print "Found old versions:"
		for version in self.versions:
			print version

	def getLatestVersion(self):
		if (len(self.versions) > 0):
			return self.versions[len(self.versions) - 1]
		else:
			return None

	def getNewVersionNum(self):
		if (len(self.versions) > 0):
			lastVersion = self.versions[len(self.versions) - 1]
			newVersion = VersionNum()
			newVals = lastVersion.version.vals[:]
			newVals[4] += 1
			newVersion.setVals(newVals)
			return newVersion
		else:
			return None
