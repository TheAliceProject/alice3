import java
import SVNUtilities

__author__="Dave Culyba"
__date__ ="$May 4, 2009 4:43:15 PM$"









jarMap = {
	"foundation" : [
		[
			"rt_foundation",
			[ "edu" ]
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
			"x_zoot",
			[ "cascade", "swing", "zoot" ]
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

httpString = "https://alma.andrew.ad.cmu.edu:8443/svn/alice3/trunk/"
svnString = "svn+ssh://culybad@invent.cse.wustl.edu/project/invent/lookingglass/svn/"
username = "dculyba"
password = "bkmdlmc"
svnUsername = "culybad"
svnPassword = "bkmDlmc00"
projectRoot = "C:/AliceSource/"

httpRepository = Repository(httpString, username, password, projectRoot)
svnRespository = Repository(svnString, svnUsername, svnPassword, projectRoot)

packages = []

for jarName, value in jarMap.items():
	for packageName, root in value:
		currentPackage = SVNPackage(packageName)
		repository = httpRepository
		if (packageName.startswith("lg")):
			repository = svnRespository
		currentPackage.previousRevision = repository.getLocalCommittedRevision(packageName)
		currentPackage.currentRevision = repository.getRemoteRevision(packageName)
		if (currentPackage.previousRevision != currentPackage.currentRevision):
				print packageName + " is at revision "+str(currentPackage.previousRevision) + " and is being updated to "+str(currentPackage.currentRevision)
				repository.updatePath(packageName)
				print "done updating "+packageName
		packages.append(currentPackage)


for package in packages:
	print package.name + ": disk revision: "+str(package.previousRevision) + ", server revision: "+str(package.currentRevision)
	
		



