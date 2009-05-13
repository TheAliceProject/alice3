import ShellUtilities
import java
import edu

JAR_CMD = "C:/Program Files/Java/jdk1.5.0_17/bin/jar"
EXEC_METHOD = eval( "edu.cmu.cs.dennisc.lang.RuntimeUtilities.exec" )

#def removeJar( jarName ):
#	dstJar = java.io.File( dstDir, jarName + ".jar" )
#	dstJar.delete()

def buildJar( jarName, value ):
	dstFile = java.io.File( dstDir, jarName + ".jar" )
	option = "cvf"
	for name, directories in value:
		if name:
			pass
		else:
			name = jarName
		binDir = java.io.File( srcDir, name + "/bin" ) 
		print binDir
		for directory in directories:
			cmdarray = [
				JAR_CMD,
				option,
				dstFile.getAbsolutePath(),
				directory
			]
			result = EXEC_METHOD( binDir, cmdarray )
			#result = edu.cmu.cs.dennisc.lang.RuntimeUtilities.exec( binDir, cmdarray )
			option = "uvf"
	print result

USE_KEY = None	
jarMap = {
	"foundation" : [
		[ 
			"rt_foundation", 
			[ "edu" ] 
		],
		[ 
			"x_zoot", 
			[ "cascade", "swing", "zoot" ] 
		],
		[ 
			"rt_foundation", 
			[ "org" ] 
		]
	], 
	"moveandturn" : [ 
		[ 
			"rt_moveandturn", 
			[ "org" ] 
		],
		[ 
			"rt_moveandturn_generated", 
			[ "org" ] 
		]
	], 
	"lg_walkandtouch" : [
		[ 
			"lg_walkandtouch", 
			[ "edu" ] 
		],
		[ 
			"lg_walkandtouch_gallery_generated", 
			[ "edu" ] 
		],
	], 
	"stage" : [
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
	"ide" : [
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

versionText = edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText()

defaultDirectory = edu.cmu.cs.dennisc.io.FileUtilities.getDefaultDirectory()
srcDir = java.io.File( defaultDirectory, "Eclipse Workspaces/Alice3/" )
dstDir = java.io.File( "/Program Files/Alice/" + versionText + "/application" )

if False:
	ShellUtilities.removeIfNecessary( dstDir )

if True:
	for jarName, value in jarMap.items():
		buildJar( jarName, value )

if False:
	ShellUtilities.copyTree( java.io.File( "/Alice/BatchOutput/projects" ), java.io.File( "/Alice/application/projects" ), "a3p", isOverwriteDesired=True )
	ShellUtilities.copyTree( java.io.File( "/Alice/application" ), dstDir )

if False:
	for file in edu.cmu.cs.dennisc.io.FileUtilities.listDescendants( java.io.File( dstDir, "projects" ), "a3p" ):
		if file.setReadOnly():
			print "success setReadOnly:", file 
		else:
			print "FAILURE setReadOnly:", file 

#cmdarray = [
#	JAR_CMD,
#	"cmf",
#	"/Alice/Manifest/Manifest.txt",
#	"run_alice.jar"
#]
#result = EXEC_METHOD( dstDir, cmdarray )

print "done"