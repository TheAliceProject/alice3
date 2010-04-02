import FileUtilities
import edu
import java

__author__="Dave Culyba"
__date__ ="$May 25, 2009 4:03:00 PM$"

EXEC_METHOD = eval( "edu.cmu.cs.dennisc.lang.RuntimeUtilities.exec" )

OBFUSCATION_DIR = java.io.File( "C:/Program Files/PCS/Mangle-It C++/" )
OBFUSCATION_COMMAND = "MangleItCpp.exe"
POST_OBFUSCATION_DIR = "Y:/Alice_Private_Source/post_obfuscated/jni_nebulous"
OBFUSCATION_OUTPUT_DIR = "Y:/Alice_Private_Source/obfuscation_output"
OBFUSCATION_PROJECT_NAME = "AliceObfuscation"


def copyOutputToPostDir():
	foldersToIgnore = [".svn", "Release"]
	FileUtilities.copyDirIgnoreFolders(OBFUSCATION_OUTPUT_DIR, POST_OBFUSCATION_DIR, foldersToIgnore)

def obfuscateProject(projectName):
	cmdarray = [
		OBFUSCATION_COMMAND,
		projectName,
	]
	print "exec'ing '"+OBFUSCATION_COMMAND+" "+projectName+"' in "+str(OBFUSCATION_DIR)
	result = EXEC_METHOD( OBFUSCATION_DIR, cmdarray )
	print result

def doObfuscation():
	obfuscateProject(OBFUSCATION_PROJECT_NAME)
	copyOutputToPostDir()