import java
import javax

class ActionAdapter( java.awt.event.ActionListener ):
	def __init__( self, callback, *args ):
		self._callback = callback
		self._args = [None,] + list( args )
	def actionPerformed( self, e ):
		self._args[ 0 ] = e
		apply( self._callback, tuple( self._args ) )

class ChangeAdapter( javax.swing.event.ChangeListener ):
	def __init__( self, callback ):
		self._callback = callback
	def stateChanged( self, e ):
		self._callback( e )

class ListSelectionAdapter( javax.swing.event.ListSelectionListener ):
	def __init__( self, callback ):
		self._callback = callback
	def valueChanged( self, e ):
		self._callback( e )

class FilteredListSelectionAdapter( javax.swing.event.ListSelectionListener ):
	def __init__( self, callback ):
		self._callback = callback
	def valueChanged( self, e ):
		if e.getValueIsAdjusting():
			pass
		else:
			#todo: handle multiple selection
			self._callback( e.getSource().getSelectedValue() )

class ItemAdapter( java.awt.event.ItemListener ):
	def __init__( self, callback ):
		self._callback = callback
	def itemStateChanged(self, e):
		self._callback( e )

class FilteredItemAdapter( java.awt.event.ItemListener ):
	def __init__( self, callback ):
		self._callback = callback
	def itemStateChanged(self, e):
		if e.getStateChange() == java.awt.event.ItemEvent.SELECTED:
			self._callback( e.getItem() )

class DocumentAdapter( javax.swing.event.DocumentListener ):
	def __init__( self, changedCallback, insertCallback, removeCallback ):
		self._changedCallback = changedCallback
		self._insertCallback = insertCallback
		self._removeCallback = removeCallback
	def changedUpdate( self, e ):
		if self._changedCallback:
			self._changedCallback( e )
	def insertUpdate( self, e ):
		if self._insertCallback:
			self._insertCallback( e )
	def removeUpdate( self, e ):
		if self._removeCallback:
			self._removeCallback( e )

def _getDocumentText(document):
	return document.getText( 0, document.getLength() )

class FilteredDocumentAdapter( javax.swing.event.DocumentListener ):
	def __init__( self, callback ):
		self._callback = callback
	def changedUpdate( self, e ):
		self._callback( _getDocumentText( e.getDocument() ) )
	def insertUpdate( self, e ):
		self._callback( _getDocumentText( e.getDocument() ) )
	def removeUpdate( self, e ):
		self._callback( _getDocumentText( e.getDocument() ) )
