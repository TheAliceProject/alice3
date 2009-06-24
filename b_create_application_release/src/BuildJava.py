import edu

__author__="Dave Culyba"
__date__ ="$May 5, 2009 6:50:42 PM$"

CMD_WINDOW_CMD = "C:/Windows/System32/cmd"
ANT_CMD = "C:/apache-ant-1.7.1/bin/ant"
EXEC_METHOD = eval( "edu.cmu.cs.dennisc.lang.RuntimeUtilities.exec" )

def buildProject(projectDir):
	cmdarray = [
				CMD_WINDOW_CMD,
				"/c",
				ANT_CMD
				]
	print "trying to exec in "+str(projectDir)
	return EXEC_METHOD( projectDir, cmdarray )

