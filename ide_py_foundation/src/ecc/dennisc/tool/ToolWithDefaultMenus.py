import java
import javax

from Actions import *
from Menu import MenuBar, Menu, MenuSeparator
from Tool import Tool 

#def _getFile( fileDialog ):
#	fileName = fileDialog.getFile()
#	if fileName:
#		directoryName = fileDialog.getDirectory()
#		directory = java.io.File( directoryName )
#		rv = java.io.File( directory, fileName )
#	else:
#		rv = None
#	return rv
	

class ToolWithDefaultMenus( Tool ):
	def __init__( self, pane, args, argDefaults ):
		if argDefaults.has_key( "path" ):
			pass
		else:
			argDefaults[ "path" ] = None

		self._file = None
		self._isDirty = False
		Tool.__init__( self, pane, args, argDefaults )

		self._updateTitle()
		#self._actionHistory = []

		self._newAction = NewAction( self )
		self._openAction = OpenAction( self )
		self._saveAction = SaveAction( self )
		self._saveAsAction = SaveAsAction( self )
		self._revertAction = RevertAction( self )
		self._exitAction = ExitAction( self )

		self._fileMenu = Menu( "File", java.awt.event.KeyEvent.VK_F, self._newAction, self._openAction, self._saveAction, self._saveAsAction, MenuSeparator, self._revertAction, MenuSeparator, self._exitAction )
		self._menuBar = MenuBar()
		self._menuBar.add( self._fileMenu )
		
		self.setJMenuBar( self._menuBar )

	def _handleArg( self, key, value ):
		if key=="path":
			if value:
				file = java.io.File( value )
				self._open( file )
				self._setFile( file )
			else:
				self.handleNew()
		else:
			Tool._handleArg( self, key, value )

	def _getTitlePrefix(self):
		return ""
	
	def _updateTitle( self ):
		rootPane = self.getRootPane()
		if rootPane:
			frame = rootPane.getParent()
			if frame:
				title = self._getTitlePrefix()
				if self._file:
					title += self._file.getAbsolutePath()
				if self.isDirty():
					title += "*"
				frame.setTitle( title )

	def _setFile( self, file ):
		self._file = file
		self._setDirty( False )
		self._updateTitle()

	def _setDirty( self, isDirty ):
		if self._isDirty != isDirty:
			self._isDirty = isDirty
			self._updateTitle()
			
	def isDirty( self ):
		return self._isDirty

	def _check( self ):
		if self.isDirty():
			if self._file:
				name = self._file.getName()
			else:
				name = "Your program"
			option = javax.swing.JOptionPane.showConfirmDialog( self, name + " has been modified.  Would you like to save it?" )
			if option == javax.swing.JOptionPane.YES_OPTION:
				return self.handleSave()
			elif option == javax.swing.JOptionPane.NO_OPTION:
				return True
			else:
				return False
		else:
			return True
	
	#WARNING: swing file dialogs can be painfully slow
	def _isSwingFileDialogDesired(self):
		return False
	
	def handleNew( self ):
		if self._check():
			return self._new()
		else:
			return False

	def _getDefaultDirectory(self):
		return None
	
	def _getExtension(self):
		return None

	def _promptUserForInputFile(self ):
		defaultDirectory = self._getDefaultDirectory()
		if self._isSwingFileDialogDesired():
			chooser = javax.swing.JFileChooser()
			if defaultDirectory:
				chooser.setCurrentDirectory( defaultDirectory )
			if chooser.showOpenDialog( self ) == javax.swing.JFileChooser.APPROVE_OPTION:
				rv = chooser.getSelectedFile()
			else:
				rv = None
		else:
#			fileDialog = java.awt.FileDialog( self, "Open...", java.awt.FileDialog.LOAD )
#			if defaultDirectory:
#				fileDialog.setDirectory( defaultDirectory.getAbsolutePath() )
#			fileDialog.show()
#			rv = _getFile( fileDialog )
			import ecc
			rv = ecc.dennisc.swing.showFileOpenDialog( self, defaultDirectory, self._getExtension() )
		return rv
		
	def handleOpen( self ):
		if self._check():
			file = self._promptUserForInputFile()
			if file:
				return self._open( file )
			else:
				return False
		else:
			return False
			
			

	def handleSave( self ):
		if self._file:
			return self._save( self._file )
		else:
			return self.handleSaveAs()
	
	def handleSaveAs( self ):
		defaultDirectory = self._getDefaultDirectory()
		if self._isSwingFileDialogDesired():
			chooser = javax.swing.JFileChooser()
			if defaultDirectory:
				chooser.setCurrentDirectory( defaultDirectory )
			if chooser.showSaveDialog( self ) == javax.swing.JFileChooser.APPROVE_OPTION:
				file = chooser.getSelectedFile()
			else:
				file = None
		else:
			import ecc
			file = ecc.dennisc.swing.showFileSaveAsDialog( self, defaultDirectory, self._getExtension() )
			#fileDialog = java.awt.FileDialog( self, "Save...", java.awt.FileDialog.SAVE )
			#fileDialog.show()
			#file = _getFile( fileDialog )
		if file:
			return self._save( file )
		else:
			return False

	def handleRevert( self ):
		if javax.swing.JOptionPane.YES_OPTION == javax.swing.JOptionPane.showConfirmDialog( self, "WARNING: revert restores your project to the last saved version.\nWould you like to continue with revert?", "Revert?", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE ):
			if self._file:
				self._open( self._file )
			else:
				self.handleNew()
#			if self._check():
#				if self._file:
#					self._open( self._file )
#				else:
#					self.handleNew()

	def handleExit( self ):
		if self._check():
			self._exit()
		

	def handleActionPerformed( self, action, e ):
		#self._actionHistory.append( ( action, e ) )
		if action == self._newAction:
			self.handleNew()
		elif action == self._openAction:
			self.handleOpen()
		elif action == self._saveAction:
			self.handleSave()
		elif action == self._saveAsAction:
			self.handleSaveAs()
		elif action == self._revertAction:
			self.handleRevert()
		elif action == self._exitAction:
			self.handleExit()

		else:
			print "TODO: ", action

	def windowClosing( self, e ):
		self.handleExit()
		#if self._check():
		#	Tool.windowClosing(self, e)
