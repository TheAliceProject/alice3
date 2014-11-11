import java
import os.path
import os
import shutil
import edu
import zipfile
import jarray
import re
import Commands

__author__="Dave Culyba"
__date__ ="$May 6, 2009 5:36:17 PM$"

USER_DIRECTORY = edu.cmu.cs.dennisc.java.io.FileUtilities.getUserDirectory()
DEFAULT_DIRECTORY = edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory()

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

def clearDir(dir):
	javaDir = java.io.File(dir)
	print "Cleaning "+dir
	if (javaDir.exists() and javaDir.isDirectory()):
		contents = os.listdir(dir)
		for item in contents:
			print "Looking to delete "+str(item)
			if (os.path.isdir(item)):
				if (not item.endswith(".svn")):
					print "Deleting "+str(os.path.join(dir, item))
                                        edu.cmu.cs.dennisc.java.io.FileUtilities.delete(os.path.join(dir, item))
					#shutil.rmtree(os.path.join(dir, item), ignore_errors=True)
				else:
					print "Skipping .svn"
			else:
				print "Deleting "+str(item)
                                edu.cmu.cs.dennisc.java.io.FileUtilities.delete(os.path.join(dir, item))

def deleteFilesInDir(dir):
	javaDir = java.io.File(dir)
	if (javaDir.exists() and javaDir.isDirectory()):
		contents = os.listdir(dir)
		for item in contents:
                        filename = os.path.join(dir, item)
			if (not os.path.isdir(filename)):
				#print "Deleting "+str(filename)
				os.remove(filename)

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

def regexReplaceInString(string, replacementMap):
	toReturn = string[:]
	for findStr, replaceStr in replacementMap:
		print "trying to replace "+findStr+" with "+replaceStr
		regex = re.compile(findStr)
		result = regex.findall(toReturn)
		if (result != None):
		    for exMatch in result:
			print " found: "+exMatch
		toReturn = regex.sub(replaceStr, toReturn)
		toReturn = toReturn.replace(findStr, replaceStr)
	return toReturn
    
def regexReplaceStringsInFile(replacementMap,filePath):
	tempName=filePath+'~~~'

	print "Searching "+filePath
	input = open(filePath)
	output = open(tempName,'w')
	for s in input:
		for findStr, replaceStr in replacementMap:
			#print "trying to replace "+findStr+" with "+replaceStr
			regex = re.compile(findStr)
			result = regex.findall(s)
			if (result != None):
			    for exMatch in result:
				print " found: "+exMatch
			s = regex.sub(replaceStr, s)
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

def copyFilesInDir(srcDir, newDir):
	names = os.listdir(srcDir)
	makeDir(newDir)
	for name in names:
		srcname = os.path.join(srcDir, name)
		dstname = os.path.join(newDir, name)
		try:
			if not os.path.isdir(srcname):
				print "Copying "+srcname + " to "+dstname
				shutil.copy2(srcname, dstname)
		except (IOError, os.error), why:
			print "Can't copy %s to %s: %s" % (`srcname`, `dstname`, str(why))
def copyFileInDir(srcDir, name, newDir):
	srcname = os.path.join(srcDir, name)
	dstname = os.path.join(newDir, name)
	try:
                print "Copying "+srcname + " to "+name
                shutil.copy2(srcname, dstname)
	except (IOError, os.error), why:
		print "Can't copy %s to %s: %s" % (`srcname`, `dstname`, str(why))


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
		if not os.path.isdir(srcDir):
			makeDirsForFile(newDir)
			shutil.copy2(srcDir, newDir)
		else:
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
#	if (isinstance(srcDir, str)):
#		srcDir = java.io.File(srcDir)
#	if (isinstance(newDir, str)):
#		newDir = java.io.File(newDir)
	copyDirIgnoreSVN(srcDir, newDir)


def getDirsIgnoreFolders_Helper(srcDir, foldersToIgnore):
    isValidDir = True
    dirNames = []
    for f in foldersToIgnore:
        if (srcDir.endswith(f)):
            isValidDir = False
            break
    if (isValidDir):
        dirNames.append(srcDir)
        if os.path.isdir(srcDir):
            names = os.listdir(srcDir)
            for name in names:
                srcname = os.path.join(srcDir, name)
                if os.path.isdir(srcname):
                    dirNames.extend(getDirsIgnoreFolders_Helper(srcname, foldersToIgnore))     
    return dirNames

def getDirsIgnoreSVN(srcDir):
    return getDirsIgnoreFolders_Helper(srcDir, [".svn"])
        
def zipDir_Helper(srcDir, dirsToSkip, extensionsToKeep, zipFile, zipRelativePath):
	if (not zipRelativePath.endswith("/")):
		zipRelativePath += "/"
	if (zipRelativePath.startswith("/")):
		zipRelativePath = zipRelativePath[1:len(zipRelativePath)]
	if (os.path.isdir(srcDir)):
		names = os.listdir(srcDir)
		for name in names:
                        if (dirsToSkip.count(name) == 0):
                            srcname = os.path.join(srcDir, name)
                            try:
                                    if not srcname.endswith(".svn"):
                                            if os.path.isdir(srcname):
                                                    zipDir_Helper(srcname, dirsToSkip, extensionsToKeep, zipFile, zipRelativePath + name)
                                            else:
                                                    (filepath, filename) = os.path.split(srcname)
    #						print "  writing "+srcname+" to "+zipRelativePath+filename
                                                    zipFile.write( srcname, str(zipRelativePath+filename), zipfile.ZIP_DEFLATED )

                            except (IOError, os.error), why:
                                    print "Can't write %s to %s: %s" % (`srcname`, `zipFile`, str(why))
                        else:
                            print "#################################### skipping "+name
	else:
		(filepath, filename) = os.path.split(srcDir)
                keepFile = False
                if (extensionsToKeep == None or len(extensionsToKeep) == 0):
                    keepFile = True
                else:
                    for extension in extensionsToKeep:
                        if (filename.endswith(extension)):
                            keepFile = True
                            break
                if (keepFile):
                    print "  writing "+srcDir+" to "+zipRelativePath+filename
                    zipFile.write( srcDir, zipRelativePath+filename, zipfile.ZIP_DEFLATED )

def extractZip(zipPath, outputPath):
    print "Trying to extract "+str(zipPath)+" to "+str(outputPath)
    zipFile = java.util.zip.ZipFile(zipPath)
    entries  = zipFile.entries()
    while (entries.hasMoreElements()):
	entry = entries.nextElement()
	entryPath = outputPath +"/"+entry.getName()
	if (entry.isDirectory()):
	    makeDir(entryPath)
	else:
	    makeDirsForFile(entryPath)
	    inStream = zipFile.getInputStream(entry)
	    outStream = java.io.BufferedOutputStream( java.io.FileOutputStream(entryPath) )
	    buffer = jarray.zeros(1024, 'b')
	    len = inStream.read(buffer)
	    while ( len  >= 0):
		outStream.write(buffer, 0, len)
		len = inStream.read(buffer)
	    inStream.close()
	    outStream.close()
    zipFile.close()

def extractZipToDir(zipPath, outputPath):
	zipFile = zipfile.ZipFile(zipPath, "r", allowZip64=True)
	for zipName in zipFile.namelist():
		fileData = zipFile.read(zipName)
		outFileName = outputPath + "/" + zipName
		makeDirsForFile(outFileName)
		outFile = open(outFileName, "w")
		outFile.write(fileData)
		outFile.close()

def zipDirs(srcDirs, dirsToSkip, zipFileName):
#	print "zip name: "+zipFileName+", zip src dirs: "
#	for (dir, zipPath) in srcDirs:
#		print "  "+dir+", "+zipPath
	print "Making zip file "+zipFileName+"..."
	makeDirsForFile(zipFileName)
	zipFile = zipfile.ZipFile(zipFileName, "w", allowZip64=True)
	for (srcDir, zipRelativePath) in srcDirs:
		zipDir_Helper(srcDir, dirsToSkip, [], zipFile, zipRelativePath)
	zipFile.close()
	zipFileName = zipFileName.replace("\\", "/")
	print "Done making zip file "+zipFileName+"."
	return zipFileName

def zipFilesInDirs(rootDir, dirs, extensionsToKeep, zipFileName):
#	print "zip name: "+zipFileName+", zip src dirs: "
#	for (dir, zipPath) in srcDirs:
#		print "  "+dir+", "+zipPath
	print "Making zip file "+zipFileName+"..."
	makeDirsForFile(zipFileName)
	zipFile = zipfile.ZipFile(zipFileName, "w")
        if (len(dirs) == 0):
            zipDir_Helper(rootDir, [], extensionsToKeep, zipFile, "")
        else:
            for dir in dirs:
                zipDir_Helper(rootDir+"/"+dir, [], extensionsToKeep, zipFile, dir)
	zipFile.close()
	zipFileName = zipFileName.replace("\\", "/")
	print "Done making zip file "+zipFileName+"."
	return zipFileName

def zipFilesInDir(srcDirs, extensionsToKeep, zipFileName):
    print "Making zip file "+zipFileName+"..."
    makeDirsForFile(zipFileName)
    zipFile = zipfile.ZipFile(zipFileName, "w")
    for (srcDir, zipRelativePath) in srcDirs:
            zipDir_Helper(srcDir, [], extensionsToKeep, zipFile, zipRelativePath)
    zipFile.close()
    zipFileName = zipFileName.replace("\\", "/")
    print "Done making zip file "+zipFileName+"."
    return zipFileName

def zipDir(srcDir, zipName):
        cmdarray = [
				Commands.ZIP_CMD,
				"a",
				"..\\"+zipName
				]
        srcDirFile = java.io.File(srcDir)
        print "zipping "+str(srcDirFile)+" to "+zipName
        print "Command: "+cmdarray[0]+" "+cmdarray[1]+" "+cmdarray[2]
	return Commands.EXEC_METHOD( srcDirFile, cmdarray )

#	print "zip src dir: "+srcDir+", zip name: "+zipName
#	srcJavaFile = java.io.File(srcDir)
#	parentDir = srcJavaFile.getParentFile()
#	zipFileName = os.path.join(parentDir.getAbsolutePath(), zipName)
#	zipFileName = zipDirs([(srcDir, "")], zipFileName)
#	return zipFileName

def jarDir(srcDir, jarName):
    binDir = java.io.File( srcDir )
    cmdarray = [
        Commands.JAR_CMD,
        "cf",
        jarName,
        "*"
    ]
    result = Commands.EXEC_METHOD( binDir, cmdarray )

def jarDirSpecificFiles(srcDir, jarName, filesString):
    binDir = java.io.File( srcDir )
    cmdarray = [
        Commands.JAR_CMD,
        "cf",
        jarName,
        filesString
    ]
    result = Commands.EXEC_METHOD( binDir, cmdarray )

def findFilesInDir(dir, searchPattern):
	foundFiles = []
	for root, dirs, files in os.walk(dir):
		for file in files:
			if (file.count(searchPattern) > 0):
				print os.path.join(root, file)
				foundFiles.append(os.path.join(root, file))

	return foundFiles

def getFirstFileFound(dir, searchPattern):
	foundFiles = findFilesInDir(dir, searchPattern)
	if (len(foundFiles) > 0):
		return foundFiles[0]
	return None

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

def regexReplaceStringsInFilesMatching((replacementMap, fileNames), dirr, filess):
	for child in filess:
		if os.path.isfile(dirr+'/'+child):
			for fileName in fileNames:
				if (fileName == child):
					regexReplaceStringsInFile(replacementMap,dirr+'/'+child)
					continue

def replaceStringsInDir(replacementMap, path):
	os.path.walk(path, replaceStrings, replacementMap)

def replaceStringsInFiles(replacementMap, path, fileNames):
	os.path.walk(path, replaceStringsInFilesMatching, (replacementMap, fileNames))

def regexReplaceStringsInFiles(replacementMap, path, fileNames):
	os.path.walk(path, regexReplaceStringsInFilesMatching, (replacementMap, fileNames))

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

def getRelativePath(rootDir, fullPath):
	if (fullPath.find(rootDir) == 0):
		relativePath = fullPath[len(rootDir):len(fullPath)]
		return relativePath
	else:
		print "failed to find "+rootDir+" in "+fullPath
	return None

def copyMatchingFolders(templateDir, srcDir):
    templateDirs = getDirsIgnoreSVN(templateDir)
    dirsToCopy = []
    for dir in templateDirs:
        files = os.listdir(dir)
        for file in files:
            fileName = os.path.join(dir, file)
            if not os.path.isdir(fileName):
                dirWithFiles = getRelativePath(templateDir, dir)
                #print dirWithFiles
                dirsToCopy.append(dirWithFiles)
                break;
    for dirToCopy in dirsToCopy:
        dirToCopy = dirToCopy.replace("/", "\\")
        src = srcDir + dirToCopy
        dst = templateDir + dirToCopy
        if (os.path.isdir(src)):
            print "copying "+src+" to "+dst
            deleteFilesInDir(dst)
            copyFilesInDir(src, dst)
        else:
            print "Skipping "+dirToCopy

def copySrcForPlugin(rootDir, srcDir, dirsToSkip, doDelete):
    rootDir = rootDir.replace("/", "\\")
    srcDir = srcDir.replace("/", "\\")
    if not rootDir.endswith("\\"):
        rootDir = rootDir + "\\"
    if not srcDir.endswith("\\"):
        srcDir = srcDir + "\\"
    dirsToCopy = getDirsIgnoreSVN(srcDir)
    filesToCopy = []
    dirsToDelete = []
    for dir in dirsToCopy:
        relativeDir = getRelativePath(srcDir, dir)
        shouldSkip = False
        for toSkip in dirsToSkip:
            if relativeDir.startswith(toSkip):
                print "SKIPPING "+relativeDir
                shouldSkip = True
                break
        if shouldSkip:
            continue
        files = os.listdir(dir)
        hasFiles = False
        for file in files:
            fileName = os.path.join(dir, file)
            if not os.path.isdir(fileName) and fileName.endswith(".java"):
                #print "Adding file to copy: "+fileName
                filesToCopy.append(fileName)
                if (not hasFiles) :
                    hasFiles = True
                    dirToDelete = os.path.join(rootDir, relativeDir)
                    dirsToDelete.append(dirToDelete)
    if (doDelete):
        for dirToDelete in dirsToDelete:
            deleteFilesInDir(dirToDelete)
    for fileToCopy in filesToCopy:
         relativeFile = getRelativePath(srcDir, fileToCopy)
         dstFile = os.path.join(rootDir, relativeFile)
         makeDirsForFile(dstFile)
         shutil.copy2(fileToCopy, dstFile)

def copyMatchingFoldersForPlugin(templateDir, srcDir, toSaveString, doDelete):
    templateDirs = getDirsIgnoreSVN(templateDir)
    dirsToCopy = []
    dirsToDelete = []
    for dir in templateDirs:
        files = os.listdir(dir)
        for file in files:
            fileName = os.path.join(dir, file)
            if not os.path.isdir(fileName):
                dirWithFiles = getRelativePath(templateDir, dir)
                #print dirWithFiles
                dirsToCopy.append(dirWithFiles)
                break;
    for dirToCopy in dirsToCopy:
        dirToCopy = dirToCopy.replace("/", "\\")
        src = srcDir + dirToCopy
        dst = templateDir + dirToCopy
        if (os.path.isdir(src)):
            print "copying "+src+" to "+dst
            oldFiles = os.listdir(dst)
            newFiles = os.listdir(src)
            for oldFile in oldFiles:
                if (oldFile.find(".svn")==-1):
                    try:
                        newFiles.index(oldFile)
                    except:
                        print "Deleting since no matching file for "+src+"/"+oldFile
            for newFile in newFiles:
                if (newFile.find(".svn")==-1):
                    try:
                        oldFiles.index(newFile)
                    except:
                        print "Adding since no matching file for "+newFile
            if (doDelete):
                deleteFilesInDir(dst)
            copyFilesInDir(src, dst)
        elif (src.find(toSaveString) == -1 and doDelete):
            dirsToDelete.append(dst)
            print "planning on deleting "+dst
        else:
            print "Skipping "+dirToCopy
    