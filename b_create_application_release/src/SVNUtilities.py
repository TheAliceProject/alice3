
from org.tmatesoft.svn import core
import java


IS_CONNECTED = True

__author__="Dave Culyba"
__date__ ="$May 5, 2009 2:24:57 PM$"

class SVNPackage:
	def __init__(self, path, dirs):
		self.name = path
		self.dirs = dirs
		self.previousRevision = 0
		self.newRevision = 0

	def isNew(self):
		return self.previousRevision != self.newRevision

class SVNConnection:
	def __init__(self, url, username, password, keyFile = None):
		self.url = url
		self.svnUrl = core.SVNURL.parseURIDecoded( self.url );
		if (self.url.startswith("http")):
			self.type = "http"
		elif (self.url.startswith("svn")):
			self.type = "svn"
		self.username = username
		self.password = password
		self.keyFile = keyFile
		self.clientManager = self.getClientManager()
		self.repository = self.connectToRepository()

	def getAuthManager(self):
		if (self.type == "http"):
			return core.auth.BasicAuthenticationManager(self.username, self.password)
		elif (self.type == "svn"):
			return core.auth.BasicAuthenticationManager(self.username, self.keyFile, self.password, self.svnUrl.getPort())

	def getClientManager(self):
		try:
			if (self.type == "http"):
				core.internal.io.dav.DAVRepositoryFactory.setup()
			elif (self.type == "svn"):
				core.internal.io.svn.SVNRepositoryFactoryImpl.setup()
		except core.SVNException, e:
			print "error setting up repository", e

		authManager = self.getAuthManager()
		options = core.wc.SVNWCUtil.createDefaultOptions( True )
		return core.wc.SVNClientManager.newInstance(options, authManager)

	def connectToRepository(self):
		try:
			if (self.type == "http"):
				core.internal.io.dav.DAVRepositoryFactory.setup()
			elif (self.type == "svn"):
				core.internal.io.svn.SVNRepositoryFactoryImpl.setup()
		except core.SVNException, e:
			print "error setting up repository", e

		repository = None
		try:
			urlObj = core.SVNURL.parseURIDecoded( self.url );
#			protocol = urlObj.getProtocol()
#			userInfo = urlObj.getUserInfo()
#			host = urlObj.getHost()
#			port = urlObj.getPort()
#			path = urlObj.getPath()
#			uriEncoded = True
#			urlObj = core.SVNURL.create(protocol, userInfo, host, 15217, path, uriEncoded)
			repository = core.io.SVNRepositoryFactory.create( urlObj )
			authManager = self.getAuthManager()
			repository.setAuthenticationManager( authManager )
			if (IS_CONNECTED):
			    try:
				    print "Repository Root: ", repository.getRepositoryRoot( True )
				    print "Repository UUID: " + repository.getRepositoryUUID( True )
			    except (Exception), e:
				    print e
			    nodeKind = repository.checkPath( "" ,  -1 )
			    if ( nodeKind == core.SVNNodeKind.NONE ):
				    print "There is no entry at '" + self.url + "'."
				    return None
			    elif ( nodeKind == core.SVNNodeKind.FILE ):
				    print "The entry at '" + self.url + "' is a file while a directory was expected."
				    return None
		except core.SVNException, e:
			print "error connecting to repository", e
			return None

		return repository

	def getFullURL(self, path):
		return core.SVNURL.parseURIDecoded( self.url + path );

	def relocateWorkingCopy(self, workingCopyDir, newURL):
	    if (IS_CONNECTED):
		updateClient = self.clientManager.getUpdateClient()
		updateClient.setIgnoreExternals( False )
		localFile = java.io.File( workingCopyDir )
		print "switching "+str(localFile)+ " to url: "+str(newURL)
		try:
		    updateClient.doSwitch(localFile, newURL, core.wc.SVNRevision.HEAD, True)
		except:
		    print "Failed to relocate copy. I hope you know what you're doing."

	def getRemoteRevision( self, path):
		try:
			entries = self.repository.getDir( path, -1 , None, None)
		except:
			return 0 #if we can't find the remote dir, return a version of 0
		iterator = entries.iterator()
		latestRevision = 0
		while (iterator.hasNext()):
			entry = iterator.next()
			newRevision = entry.getRevision()
			if (newRevision > latestRevision):
				latestRevision = newRevision
		return latestRevision

	def listEntries( self, path ):
		entries = self.repository.getDir( path, -1 , None, None)
		iterator = entries.iterator()
		while (iterator.hasNext()):
			entry = iterator.next()
			dirSep = ""
			if (path != "" ):
				dirSep = "/"
			print "/" + dirSep + entry.getName() + " ( author: '" + entry.getAuthor() + "'; revision: " + str(entry.getRevision()) + "; date: " + str(entry.getDate()) + ")"

			if (entry.getKind() == core.SVNNodeKind.DIR):
				newPath = path + "/" + entry.getName()
				if ( path == "" ):
					newPath = entry.getName()
				self.listEntries( newPath )

	def updatePathToRevision( self, localDir, revision):
	    if (IS_CONNECTED):
		localFile = java.io.File( localDir )
		updateClient = self.clientManager.getUpdateClient()
		updateClient.setIgnoreExternals( False )
		print "updating "+localFile.getAbsolutePath()
		try:
		    return updateClient.doUpdate( localFile, revision, True )
		except:
		    print "Failed to update directory."

	def commitChanges(self, dir):
	    if (IS_CONNECTED):
		commitClient = self.clientManager.getCommitClient()
		javaFile = java.io.File(dir)
		files = [javaFile]
		try:
		    commitClient.doCommit(files, False, "Automated commit", None, None, False, True, core.SVNDepth.INFINITY)
		except:
		    print "Failed to commit files"



class Repository:
	def __init__(self, previousBranch, currentBranch, relativePath, localDir, shouldRelocate = True):
		self.previousBranch = previousBranch
		self.currentBranch = currentBranch
		self.relativePath = relativePath
		self.localDir = localDir
		self.shouldRelocate = shouldRelocate
		if (self.currentBranch != None and self.shouldRelocate):
		    if (self.previousBranch != None):
			print "trying to relocate "+self.localDir+" from "+str(self.previousBranch.getFullURL(self.relativePath)) + " to "+str(self.currentBranch.getFullURL(self.relativePath))
			self.previousBranch.relocateWorkingCopy(self.localDir, self.currentBranch.getFullURL(self.relativePath))
			print "relocated!"
		    else:
			print "trying to relocate "+self.localDir+" from "+str(self.currentBranch.getFullURL(self.relativePath)) + " to "+str(self.currentBranch.getFullURL(self.relativePath))
			self.currentBranch.relocateWorkingCopy(self.localDir, self.currentBranch.getFullURL(self.relativePath))
			print "relocated!"
		else:
			print "previous branch: "+str(self.previousBranch) + " and current branch: "+str(self.currentBranch)

	def getPreviousRevision( self ):
		if (self.previousBranch != None):
			return self.previousBranch.getRemoteRevision(self.relativePath)
		else:
			return -1

	def getCurrentRevision( self ):
		if (self.currentBranch != None):
			return self.currentBranch.getRemoteRevision(self.relativePath)
		else:
			return -1

#	def updateCode_CmdLine(self, path):
#		cmdarray = [
#					"C:/subversion/svn",
#					"update",
#					self.localRoot + path,
#				]
#		binDir = java.io.File( "C:/" )
#		result = EXEC_METHOD( binDir, cmdarray )
#		print result

	def isNew(self):
	    if (IS_CONNECTED):
		oldRevision = self.getPreviousRevision()
		newRevision = self.getCurrentRevision()
		return (newRevision > oldRevision)
	    else:
		return False;

	def syncCodeToHead( self ):
		return self.currentBranch.updatePathToRevision( self.localDir, core.wc.SVNRevision.HEAD)

	def syncCodeToRevision( self, revision ):
		return self.currentBranch.updatePathToRevision( self.localDir, revision)

	def commitChanges(self):
	    self.currentBranch.commitChanges(self.localDir)


