import edu

if edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux():
	ZIP_CMD = "7za"
else:
	ZIP_CMD = "C:/Program Files/7-Zip/7z.exe"

CMD_WINDOW_CMD = "C:/Windows/System32/cmd"
EXEC_METHOD = eval( "edu.cmu.cs.dennisc.java.lang.RuntimeUtilities.exec" )
JAR_METHOD = eval( "edu.cmu.cs.dennisc.java.lang.RuntimeUtilities.exec" )
