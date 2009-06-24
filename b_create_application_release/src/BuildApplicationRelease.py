import ShellUtilities
import JarUtilities
import java
import edu
import os

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
		JarUtilities.buildJar( jarName, value )

if False:
	ShellUtilities.copyTree( java.io.File( "/Alice/BatchOutput/projects" ), java.io.File( "/Alice/application/projects" ), "a3p", isOverwriteDesired=True )
	ShellUtilities.copyTree( java.io.File( "/Alice/application" ), dstDir )

if False:
	for file in edu.cmu.cs.dennisc.io.FileUtilities.listDescendants( java.io.File( dstDir, "projects" ), "a3p" ):
		if file.setReadOnly():
			print "success setReadOnly:", file 
		else:
			print "FAILURE setReadOnly:", file 

print "done"