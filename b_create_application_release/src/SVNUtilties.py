
from org.tmatesoft.svn import core
import java


__author__="Dave Culyba"
__date__ ="$May 5, 2009 2:24:57 PM$"

class SVNPackage:
	def __init__(self, path):
		self.name = path
		self.previousRevision = 0
		self.currentRevision = 0

class Repository:
	def __init__(self, url, username, password, localRoot):
		self.url = url
		if (self.url.startswith("http")):
			self.type = "http"
		elif (self.url.startswith("svn")):
			self.type = "svn"
		self.username = username
		self.password = password
		self.localRoot = localRoot
		self.clientManager = self.getClientManager()
		self.repository = self.connectToRepository()

	def getClientManager(self):
		try:
			if (self.type == "http"):
				core.internal.io.dav.DAVRepositoryFactory.setup()
			elif (self.type == "svn"):
				core.internal.io.svn.SVNRepositoryFactoryImpl.setup()
		except core.SVNException, e:
			print "error setting up repository", e
		authManager = core.wc.SVNWCUtil.createDefaultAuthenticationManager(self.username, self.password)
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
			repository = core.io.SVNRepositoryFactory.create( urlObj )
			authManager = core.wc.SVNWCUtil.createDefaultAuthenticationManager(self.username, self.password)
			repository.setAuthenticationManager( authManager )
			print "Repository Root: ", repository.getRepositoryRoot( True )
			print "Repository UUID: " + repository.getRepositoryUUID( True )
			nodeKind = repository.checkPath( "" ,  -1 )
			if ( nodeKind == core.SVNNodeKind.NONE ):
				print "There is no entry at '" + url + "'."
				return None
			elif ( nodeKind == core.SVNNodeKind.FILE ):
				print "The entry at '" + url + "' is a file while a directory was expected."
				return None
		except core.SVNException, e:
			print "error connecting to repository", e
			return None

		return repository


	def getRemoteRevision( self, path):
		entries = self.repository.getDir( path, -1 , None, None)
		iterator = entries.iterator()
		latestRevision = 0
		while (iterator.hasNext()):
			entry = iterator.next()
			currentRevision = entry.getRevision()
			if (currentRevision > latestRevision):
				latestRevision = currentRevision
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

	def updateCode_CmdLine(self, path):
		cmdarray = [
					"C:/subversion/svn",
					"update",
					self.localRoot + path,
				]
		binDir = java.io.File( "C:/" )
		result = EXEC_METHOD( binDir, cmdarray )
		print result

	def updatePath( self, path):
		localFile = java.io.File( self.localRoot + path )
		updateClient = self.clientManager.getUpdateClient()
		updateClient.setIgnoreExternals( False )
		return updateClient.doUpdate( localFile, core.wc.SVNRevision.HEAD, True )

	def getLocalCommittedRevision(self, path):
		localFile = java.io.File( self.localRoot + path )
		statusClient = self.clientManager.getStatusClient()
		localStatus = statusClient.doStatus(localFile, True)
		return localStatus.getCommittedRevision().getNumber()