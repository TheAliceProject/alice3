import java
import os.path
import os
import shutil
import edu
import zipfile
JAR_CMD = "C:/Java/jdk1.5.0_18/bin/jar"
EXEC_METHOD = eval( "edu.cmu.cs.dennisc.lang.RuntimeUtilities.exec" )

__author__="Dave Culyba"
__date__ ="$May 6, 2009 5:36:17 PM$"

def makeDir(path):
	javaFile = java.io.File(path)
	javaFile.mkdirs()

def makeDirsForFile(file):
	javaFile = java.io.File(file)
	javaFile.getParentFile().mkdirs()

def getStringFromFile(fileName):
	toReturn = ""
	input = open(fileName)
	for s in input:
		toReturn += s
	input.close()
	return toReturn

def deleteDir(dir):
	javaDir = java.io.File(dir)
	if (javaDir.exists() and javaDir.isDirectory()):
		shutil.rmtree(dir)

def replaceInString(string, replacementMap):
	toReturn = string[:]
	for findStr, replaceStr in replacementMap:
		toReturn = toReturn.replace(findStr, replaceStr)
	return toReturn

def replaceStringsInFile(replacementMap,filePath):
	tempName=filePath+'~~~'
	input = open(filePath)
	output = open(tempName,'w')

	for s in input:
		for findStr, replaceStr in replacementMap:
			s = s.replace(findStr,replaceStr)
		output.write(s)
	output.close()
	input.close()
	os.remove(filePath)
	os.rename(tempName,filePath)

def ignoreSVN(dir, dirContents):
	toCopy = []
	for content in dirContents:
		print content, content.type
		if (content != ".svn"):
			toCopy.append(content)
	return toCopy

def copyDirIgnoreSVN(srcDir, newDir):
	copyDirIgnoreFolders(srcDir, newDir, [".svn"])

def copyDirIgnoreFolders(srcDir, newDir, foldersToIgnore):
	makeDirsForFile(newDir)
	copyDirIgnoreIgnoreFolders_Helper(srcDir, newDir, foldersToIgnore)

def copyDirIgnoreIgnoreFolders_Helper(srcDir, newDir, foldersToIgnore):
	isValidDir = True
	for f in foldersToIgnore:
		if (srcDir.endswith(f)):
			isValidDir = False
			break
	if (isValidDir):
		names = os.listdir(srcDir)
		makeDir(newDir)
		for name in names:
			srcname = os.path.join(srcDir, name)
			dstname = os.path.join(newDir, name)
			try:
				if os.path.isdir(srcname):
					copyDirIgnoreIgnoreFolders_Helper(srcname, dstname, foldersToIgnore)
				else:
					shutil.copy2(srcname, dstname)
			except (IOError, os.error), why:
				print "Can't copy %s to %s: %s" % (`srcname`, `dstname`, str(why))

def copyDir(srcDir, newDir):
	#do we ever not want to skip the .svn dirs?
	copyDirIgnoreSVN(srcDir, newDir)

def zipDir_Helper(srcDir, zipFile, basePath):
	(unwantedDir, baseName) = os.path.split(basePath)
#	print "base path: "+basePath
	cropPathSeparator = 1
	if (basePath.endswith("/") or basePath.endswith("\\")):
		cropPathSeparator = 0

	zipPath = srcDir[len(basePath)+cropPathSeparator:]
	if (zipPath != ""):
		zipPath += "/"
#	print "zip path: "+zipPath
	names = os.listdir(srcDir)
	for name in names:
		srcname = os.path.join(srcDir, name)
		try:
			if os.path.isdir(srcname):
				zipDir_Helper(srcname, zipFile, basePath)
			else:
				(filepath, filename) = os.path.split(srcname)
#				print "adding "+srcname+" to zip as "+zipPath+filename
				zipFile.write( srcname, zipPath+filename, zipfile.ZIP_DEFLATED )

		except (IOError, os.error), why:
			print "Can't write %s to %s: %s" % (`srcname`, `zipFile`, str(why))

def zipDir(srcDir, zipName):
	print "zip src dir: "+srcDir+", zip name: "+zipName
	srcJavaFile = java.io.File(srcDir)
	parentDir = srcJavaFile.getParentFile()
	zipFileName = os.path.join(parentDir.getAbsolutePath(), zipName)
	print "zip file: "+zipFileName
	zipFile = zipfile.ZipFile(zipFileName, "w")
	zipDir_Helper(srcDir, zipFile, srcDir)
	zipFile.close()
	zipFileName = zipFileName.replace("\\", "/")
	return zipFileName

def replaceStrings(replacementMap, dirr, filess):
	for child in filess:
		if os.path.isfile(dirr+'/'+child):
			replaceStringsInFile(replacementMap,dirr+'/'+child)

def replaceStringsInFilesMatching((replacementMap, fileNames), dirr, filess):
	for child in filess:
		if os.path.isfile(dirr+'/'+child):
			for fileName in fileNames:
				if (fileName == child):
					replaceStringsInFile(replacementMap,dirr+'/'+child)
					continue

def replaceStringsInDir(replacementMap, path):
	os.path.walk(path, replaceStrings, replacementMap)

def replaceStringsInFiles(replacementMap, path, fileNames):
	os.path.walk(path, replaceStringsInFilesMatching, (replacementMap, fileNames))

def renameDirs(replacementMap, rootDir):
	if (os.path.isdir(rootDir)):
		dirs = os.listdir(rootDir)
		for dir in dirs:
			if os.path.isdir(rootDir+'/'+dir):
				renameDirs(replacementMap, rootDir+'/'+dir )
		newName = rootDir
		for findStr, replaceStr in replacementMap:
			if (newName.endswith(findStr)):
				index = newName.rfind(findStr)
				newName = newName[0:index]
				newName += replaceStr
		if (newName != rootDir):
			os.rename(rootDir, newName)