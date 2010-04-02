import java
import javax
import edu

import ecc

class ListVC( javax.swing.JList ):
	def __init__( self, listSelectionListener=None ):
		javax.swing.JList.__init__( self )
		self._mapListSelectionAdapter = {}
		if listSelectionListener:
			self.addListSelectionListener( listSelectionListener )
			
	def selectRandom( self ):
		import random
		n = self.getModel().getSize()
		if n:
			i = random.randint( 0, n-1 )
			self.setSelectedIndex( i )
		else:
			print "warning selectRandom"
		
	def addListSelectionListener( self, listSelectionListener ):
		if isinstance( listSelectionListener, javax.swing.event.ListSelectionListener ):
			pass
		else:
			listSelectionAdapter = ListSelectionAdapter( listSelectionListener )
			self._mapListSelectionAdapter[ listSelectionListener ] = listSelectionAdapter
			listSelectionListener = listSelectionAdapter
		javax.swing.JList.addListSelectionListener( self, listSelectionListener )
	def removeListSelectionListener( self, listSelectionListener ):
		listSelectionListener = self._mapListSelectionAdapter.pop( listSelectionListener, listSelectionListener )
		javax.swing.JList.removeListSelectionListener( self, listSelectionListener )

class EnumConstantListVC( ListVC ):
	def __init__( self, enumCls=None, listSelectionListener=None ):
		ListVC.__init__( self, listSelectionListener )
		self.setEnumCls( enumCls )
	def setEnumCls( self, enumCls ):
		self.setListData( enumCls.values() )
		
#class AbstractIconListCellRenderer( javax.swing.JPanel, javax.swing.ListCellRenderer ):
#	def __init__( self ):
#		javax.swing.JPanel.__init__( self )
#		self.setLayout( java.awt.BorderLayout() )
#		self._label = javax.swing.JLabel()
#		self.add( self._label, java.awt.BorderLayout.CENTER )
#		
#	def getListCellRendererComponent( self, list, value, index, isSelected, isFocus ):
#		rv = self._label
#		textForLabel, icon = self._getTextForLabelAndIcon( rv, list, value, index, isSelected, isFocus )
#		rv.setText( textForLabel )
#		rv.setIcon( icon )
#		return rv
class AbstractIconListCellRenderer( javax.swing.DefaultListCellRenderer ):
	def __init__( self, inset=2 ):
		self._border = javax.swing.border.EmptyBorder( inset, inset, inset, inset )
		javax.swing.DefaultListCellRenderer.__init__( self )
	
	def _getTextForLabelAndIcon( self, component, list, value, index, isSelected, isFocus ):
		raise "Override"
	
	def getListCellRendererComponent( self, list, value, index, isSelected, isFocus ):
		rv = javax.swing.DefaultListCellRenderer.getListCellRendererComponent( self, list, value, index, isSelected, isFocus )
		textForLabel, icon = self._getTextForLabelAndIcon( rv, list, value, index, isSelected, isFocus )
		rv.setText( textForLabel )
		rv.setIcon( icon )
		rv.setBorder( self._border )
		return rv
		
#class InputPane( javax.swing.JPanel ):
#	def __init__( self, inset=16 ):
#		self._dialog = None
#		self.setBorder( javax.swing.border.EmptyBorder( inset, inset, inset, inset ) )
#
#	def getInputValue( self ):
#		raise "Override"
#
#	def _getValidators( self ):
#		raise "Override"
#
#	def isValid( self ):
#		for validator in self._getValidators():
#			if validator.isValid():
#				pass
#			else:
#				return False
#		return False
#
#	def updateIsValid( self ):
#		if self._dialog:
#			isValid = self._isValidCallback( self.getText() )
#			if isValid:
#				self.setForeground( java.awt.Color.BLACK )
#			else:
#				self.setForeground( java.awt.Color.RED )
#			self._dialog.setOKEnabled( isValid )
#
#	def setDialog( self, dialog ):
#		self._dialog = dialog
#		self.updateIsValid()
#		
##	def setOKEnabled( self, isOKEnabled ):
##		if self._dialog:
##			self._dialog.setOKEnabled( isOKEnabled )
#	
#	def _fireOKIfPossible( self ):
#		if self._dialog:
#			self._dialog._fireOKIfPossible()
#
#class AbstractInputTextField( javax.swing.JTextField ):
#	def __init__( self, changeListener ):
#		javax.swing.JTextField.__init__( self )
#		self._changeListener = changeListener
#		self.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( self._handleChanged ) )
#		self.addActionListener( ecc.dennisc.swing.event.ActionAdapter( self._handleActionPerformed ) )
#	def isValid( self ):
#		raise "Override"
#	def _handleActionPerformed( self, e ):
#		self._inputPane.fireOKIfPossible()
#	def _handleChanged( self, text ):
#		if self.isValid():
#			self.setForeground( java.awt.Color.BLACK )
#		else:
#			self.setForeground( java.awt.Color.RED )
#		e = javax.swing.event.ChangeEvent( self )
#		self._changeListener.stateChanged( e )
#
#class InputTextFieldWithValidationCallback( AbstractInputTextField ):
#	def __init__( self, changeListener, isValidCallback ):
#		AbstractInputTextField.__init__( self, changeListener )
#		self._isValidCallback = isValidCallback
#	def isValid( self ):
#		return self._isValidCallback( self.getText() )
#
#class InputDialog( javax.swing.JDialog ):
#	def __init__( self, owner, component, title=None, isModal=True ):
#		actualOwner = owner
#		if actualOwner:
#			if isinstance( actualOwner, java.awt.Dialog ):
#				pass
#			elif isinstance( actualOwner, java.awt.Frame ):
#				pass
#			else:
#				actualOwner = javax.swing.SwingUtilities.getRoot( actualOwner )
#
#		javax.swing.JDialog.__init__( self, actualOwner, title, isModal )
#		
#		if owner:
#			#self.setLocationRelativeTo( owner )
#			#self.setLocationRelativeTo( None )
#			self.setLocation( 64, 64 )
#		
#		self._component = component
#		
#		self._okButton = javax.swing.JButton( "OK" )
#		self._okButton.addActionListener( ecc.dennisc.swing.event.ActionAdapter( self._handleOK ) )
#		self._cancelButton = javax.swing.JButton( "Cancel" )
#		self._cancelButton.addActionListener( ecc.dennisc.swing.event.ActionAdapter( self._handleCancel ) )
#
#		panel = javax.swing.JPanel()
#		panel.add( self._okButton )
#		panel.add( self._cancelButton )
#		
#		self.getContentPane().add( self._component, java.awt.BorderLayout.CENTER )
#		self.getContentPane().add( panel, java.awt.BorderLayout.SOUTH )
#		self.pack()
#
#		self._component.setDialog( self )
#		self._value = None
#		
#	def _handleOK( self, e ):
#		self._value = self._component.getInputValue()
#		self.setVisible( False )
#	def _handleCancel( self, e ):
#		self._value = None
#		self.setVisible( False )
#
#	def _fireOKIfPossible( self ):
#		if self._okButton.isEnabled():
#			self._okButton.doClick()
#
#	def setOKEnabled( self, isEnabled ):
#		self._okButton.setEnabled( isEnabled )
#
#	def getInputValue( self ):
#		return self._value
#	
#def showInputDialog( owner, component, title=None ):
#	dialog = ecc.dennisc.swing.InputDialog( owner, component, title )
#	dialog.setVisible( True )
#	return dialog.getInputValue()

class _FileNameFilter( java.io.FilenameFilter ):
	def __init__( self, extension ):
		self._extension = extension
	def accept( self, dir, name ):
		return name.endswith( self._extension )

def _showFileDialog( owner, title, mode, directory, extension ):
	frameOwner = edu.cmu.cs.dennisc.swing.SwingUtilities.getRootFrame( owner )
	if frameOwner:
		owner = frameOwner
	else:
		owner = edu.cmu.cs.dennisc.swing.SwingUtilities.getRootDialog( owner )

	dialog = java.awt.FileDialog( owner, title, mode )	
		
	if directory:
		dialog.setDirectory( directory.getAbsolutePath() )
	if extension:
		dialog.setFilenameFilter( _FileNameFilter( extension ) )
	dialog.show()
	fileName = dialog.getFile()
	if fileName:
		directoryName = dialog.getDirectory()
		directory = java.io.File( directoryName )
		
		if fileName.endswith( "." + extension ):
			pass
		else:
			fileName = fileName + "." + extension
		rv = java.io.File( directory, fileName )
	else:
		rv = None
	return rv
def showFileOpenDialog( owner, directory, extension ):
	return _showFileDialog( owner, "Open...", java.awt.FileDialog.LOAD, directory, extension )
def showFileSaveAsDialog( owner, directory, extension ):
	return _showFileDialog( owner, "Save...", java.awt.FileDialog.SAVE, directory, extension )

class MenuItem( javax.swing.JMenuItem, java.awt.event.ActionListener ):
	def __init__( self, model, index, root ):
		javax.swing.JMenuItem.__init__( self )
		self._model = model
		self._index = index
		self._root = root
		self.addActionListener( self )
	def actionPerformed( self, e ):
		self._root._handleActionPerformed( e )

class Menu( javax.swing.JMenu, javax.swing.event.MenuListener ):
	def __init__( self, model, index, root ):
		javax.swing.JMenu.__init__( self )
		self._model = model
		self._index = index
		self._root = root
		self.addMenuListener( self )
	def menuSelected( self, e ):
		self._root._handleMenuSelected( e )
	def menuDeselected( self, e ):
		self._root._handleMenuDeselected( e )
	def menuCanceled( self, e ):
		self._root._handleMenuCancelled( e )

class PopupMenu( javax.swing.JPopupMenu, javax.swing.event.PopupMenuListener ):
	def __init__( self, valueLists ):
		javax.swing.JPopupMenu.__init__( self )
		i = 0
		n = len( valueLists )
		self._componentLists = []
		while i<n:
			self._componentLists.append( [] )
			valueList = valueLists[ i ]
			for value in valueList:
				if i < n-1:
					component = Menu( value, i, self )
				else: 
					component = MenuItem( value, i, self )
				component.setText( self._createText( value ) )
				component.setIcon( self._createIcon( value ) )
				self._componentLists[ i ].append( component )
			i += 1
			
		for component in self._componentLists[ 0 ]:
			self.add( component )
			
		self._srcs = [ None ] * n
		
		self.addPopupMenuListener( self )

	def popupMenuWillBecomeVisible( self, e ):
		print "popupMenuWillBecomeVisible", e
	def popupMenuWillBecomeInvisible( self, e ):
		print "popupMenuWillBecomeInvisible", e
	def popupMenuCanceled( self, e ):
		print "popupMenuCanceled", e

	def _createText( self, model ):
		return str( model )
	def _createIcon( self, model ):
		return None

	def _handleSelection( self ):
		print "todo: override;",
		for srcI in self._srcs:
			print srcI._model,
		print

	def _handleMenuCancelled( self, e ):
		print "menuCanceled", e.getSource()._index
		
	def _handleMenuDeselected( self, e ):
		src = e.getSource()
		src.removeAll()
		#print "menuDeselected", e.getSource()._index
		
	def _handleMenuSelected( self, e ):
		src = e.getSource()
		i = src._index
		self._srcs[ i ] = src
		if src._index < len( self._componentLists ) - 1:
			for component in self._componentLists[ src._index + 1 ]:
				src.add( component )
		#print "menuSelected", e.getSource()._index

	def _handleActionPerformed( self, e ):
		src = e.getSource()
		i = src._index
		self._srcs[ i ] = src
		self._handleSelection()


def _getColumnCount( matrix ):
	columnCount = - 1
	for row in matrix:
		n = len( row )
		if columnCount != - 1:
			assert n == columnCount
		columnCount = n
	return columnCount

def springItUpANotch( pane, componentRows, x0, y0, xPad, yPad ):
	columnCount = _getColumnCount( componentRows )

	layout = javax.swing.SpringLayout()
	pane.setLayout( layout )

	for row in componentRows:
		for component in row:
			pane.add( component )

	colIndices = range( columnCount )
	rowIndices = range( len( componentRows ) )

	x = javax.swing.Spring.constant( x0 )	
	xPad = javax.swing.Spring.constant( xPad )	
	for c in colIndices:
		width = javax.swing.Spring.constant( 0 )
		for r in rowIndices:
			constraints = layout.getConstraints( componentRows[ r ][ c ] )
			width = javax.swing.Spring.max( width, constraints.getWidth() )

		for r in rowIndices:
			constraints = layout.getConstraints( componentRows[ r ][ c ] )
			constraints.setX( x )
			constraints.setWidth( width )
		x = javax.swing.Spring.sum( x, width )
		x = javax.swing.Spring.sum( x, xPad )

	y = javax.swing.Spring.constant( y0 )	
	yPad = javax.swing.Spring.constant( yPad )	
	for r in rowIndices:
		height = javax.swing.Spring.constant( 0 )
		for c in colIndices:
			constraints = layout.getConstraints( componentRows[ r ][ c ] )
			height = javax.swing.Spring.max( height, constraints.getHeight() )

		for c in colIndices:
			constraints = layout.getConstraints( componentRows[ r ][ c ] )
			constraints.setY( y )
			constraints.setHeight( height )
		y = javax.swing.Spring.sum( y, height )
		y = javax.swing.Spring.sum( y, yPad )

	constraints = layout.getConstraints( pane )
	constraints.setConstraint( javax.swing.SpringLayout.EAST, x )
	constraints.setConstraint( javax.swing.SpringLayout.SOUTH, y )

