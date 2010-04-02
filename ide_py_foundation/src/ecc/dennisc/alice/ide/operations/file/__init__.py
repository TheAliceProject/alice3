import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

import ecc

class AbstractOpenOperation( alice.ide.AbstractOperation ):
	def _isNew( self ):
		raise "Override"
	def prepare( self, e, observer ):
		if self.getIDE().isProjectUpToDateWithFile():
			pass
		else:
			if self.getIDE().isClearedToProcedeWithChangedProject( e, observer ):
				pass
			else:
				return alice.ide.Operation.PreparationResult.CANCEL

		#todo: support bootstrap
		self._file = ecc.dennisc.alice.ide.SelectProjectDialog.showSelectProjectDialog( e.getSource(), isNew=self._isNew() )
		if self._file:
			return alice.ide.Operation.PreparationResult.PERFORM
		else:
			return alice.ide.Operation.PreparationResult.CANCEL

	def perform( self ):
		if self._file:
			self.getIDE().loadProjectFrom( self._file )
		else:
			self.getIDE().createProjectFromBootstrap()

class NewOperation( AbstractOpenOperation ):
	def __init__( self ):
		AbstractOpenOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "New..." )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_N, edu.cmu.cs.dennisc.awt.event.InputEventUtilities.getAcceleratorMask() ) )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_N )
	def _isNew( self ):
		return True

class OpenOperation( AbstractOpenOperation ):
	def __init__( self ):
		AbstractOpenOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Open..." )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_O, edu.cmu.cs.dennisc.awt.event.InputEventUtilities.getAcceleratorMask() ) )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_O )

	def _isNew( self ):
		return False


class AbstractSaveOperation( alice.ide.AbstractOperation ):
	def _isPromptNecessary( self ):
		raise "Override"

	def _getDefaultDirectory( self ):
		return alice.io.FileUtilities.getMyProjectsDirectory()
	def _getExtension( self ):
		return alice.io.FileUtilities.PROJECT_EXTENSION
	
	def prepare( self, e, observer ):
		self._file = self.getIDE().getFile()
		if self._isPromptNecessary():
			#owner = e.getSource()
			owner = self.getIDE()
			#self._file = ecc.dennisc.swing.showFileSaveAsDialog( owner, self._getDefaultDirectory(), self._getExtension() )
			self._file = edu.cmu.cs.dennisc.awt.FileDialogUtilities.showSaveFileDialog( owner, self._getDefaultDirectory(), self._getExtension(), True )
			if self._file:
				return alice.ide.Operation.PreparationResult.PERFORM
			else:
				return alice.ide.Operation.PreparationResult.CANCEL
		else:
			return alice.ide.Operation.PreparationResult.PERFORM
	def perform( self ):
		self.getIDE().saveProjectTo( self._file )
	
class SaveOperation( AbstractSaveOperation ):
	def __init__( self ):
		AbstractSaveOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Save" )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_S, edu.cmu.cs.dennisc.awt.event.InputEventUtilities.getAcceleratorMask() ) )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_S )
	def _isPromptNecessary( self ):
		return ( self._file and self._file.canWrite() ) == False

class SaveAsOperation( AbstractSaveOperation ):
	def __init__( self ):
		AbstractSaveOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Save As..." )
		self.putValue( javax.swing.Action.ACCELERATOR_KEY, javax.swing.KeyStroke.getKeyStroke( java.awt.event.KeyEvent.VK_S, edu.cmu.cs.dennisc.awt.event.InputEventUtilities.getAcceleratorMask() | java.awt.event.InputEvent.SHIFT_MASK ) )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_A )
	def _isPromptNecessary( self ):
		return True

class RevertOperation( alice.ide.AbstractOperation ):
	def __init__( self ):
		alice.ide.AbstractOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Revert..." )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_R )
	def prepare( self, e, observer ):
		if javax.swing.JOptionPane.YES_OPTION == javax.swing.JOptionPane.showConfirmDialog( self, "WARNING: revert restores your project to the last saved version.\nWould you like to continue with revert?", "Revert?", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE ):
			return alice.ide.Operation.PreparationResult.PERFORM
		else:
			return alice.ide.Operation.PreparationResult.CANCEL
	def perform( self ):
		ide = self.getIDE()
		ide.loadProjectFrom( ide.getFile() )

class ExitOperation( alice.ide.AbstractOperation ):
	def __init__( self ):
		alice.ide.AbstractOperation.__init__( self )
		self.putValue( javax.swing.Action.NAME, "Exit" )
		self.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_X )
	def prepare( self, e, observer ):
		if self.getIDE().isProjectUpToDateWithFile():
			return alice.ide.Operation.PreparationResult.PERFORM
		else:
			if self.getIDE().isClearedToProcedeWithChangedProject( e, observer ):
				return alice.ide.Operation.PreparationResult.PERFORM
			else:
				return alice.ide.Operation.PreparationResult.CANCEL
	def perform( self ):
		java.lang.System.exit( 0 )