import java
import javax
import edu

import ecc

class Button( javax.swing.JButton ):
	def __init__( self, text, actionListener ):
		javax.swing.JButton.__init__( self, text )
		self.addActionListener( actionListener )

class ListPropertyModel( javax.swing.AbstractListModel ):
	def __init__( self, value ):
		self._value = value
	def getSize( self ):
		return self._value.size()
	def getElementAt( self, index ):
		return self._value.get( index )

	def handleAdd( self, value ):
		index = self._value.size()
		self._value.add( [ value ] )
		self.fireIntervalAdded( self, index, index + 1 )

	def handleSet( self, index, value ):
		self._value.set( index, [ value ] )
		self.fireContentsChanged( self, index, index + 1 )
	
	def handleRemove( self, index ):
		self._value.remove( index )
		self.fireIntervalRemoved( self, index, index + 1 )

	def handleMoveUp( self, index ):
		a = self._value.get( index - 1 )
		b = self._value.get( index )
		self._value.set( index - 1, [b, a] )
		self.fireContentsChanged( self, index - 1, index )

	def handleMoveDown( self, index ):
		a = self._value.get( index )
		b = self._value.get( index + 1 )
		self._value.set( index, [b, a] )
		self.fireContentsChanged( self, index, index + 1 )

	def handleRename( self, index, name ):
		self._value.get( index ).name.setValue( name )
		self.fireContentsChanged( self, index, index + 1 )

class ListPropertyVC( ecc.dennisc.swing.ListVC ):
	def __init__( self, *args ):
		self.setCellRenderer( apply( self._createListCellRenderer, args, {} ) )
	def _createListCellRenderer( self, *args ):
		raise "Override"
	def setListProperty( self, listProperty ):
		self.setModel( ListPropertyModel( listProperty ) )

class NodeListPropertyPane( javax.swing.JPanel ):
	def __init__( self, *args ):
		javax.swing.JPanel.__init__( self )
		self._listVC = apply( self._createListVC, args, {} )
		self._listVC.addListSelectionListener( ecc.dennisc.swing.event.ListSelectionAdapter( self._handleNodeSelection ) )
		addText = "add"
		if self._isDialogTriggeredOnAdd():
			addText += "..."
		
		self._addButton = Button( addText, ecc.dennisc.swing.event.ActionAdapter( self._handleAdd ) )
		self._removeButton = Button( "remove", ecc.dennisc.swing.event.ActionAdapter( self._handleRemove ) )
		self._moveUpButton = Button( "move up", ecc.dennisc.swing.event.ActionAdapter( self._handleMoveUp ) )
		self._moveDownButton = Button( "move down", ecc.dennisc.swing.event.ActionAdapter( self._handleMoveDown ) )
		
		self._renameButton = Button( "rename...", ecc.dennisc.swing.event.ActionAdapter( self._handleRename ) )
		
		buttonPane = javax.swing.JPanel()
		buttonPane.setLayout( java.awt.GridBagLayout() )
		gbc = java.awt.GridBagConstraints()
		gbc.fill = java.awt.GridBagConstraints.BOTH
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER
		buttonPane.add( self._addButton, gbc )
		buttonPane.add( self._removeButton, gbc )
		gbc.insets.top = 8
		buttonPane.add( self._moveUpButton, gbc )
		gbc.insets.top = 0
		buttonPane.add( self._moveDownButton, gbc )
		if self._isRenameDesired():
			gbc.insets.top = 16
			buttonPane.add( self._renameButton, gbc )
			gbc.insets.top = 0
		gbc.weighty = 1.0
		buttonPane.add( javax.swing.JLabel(), gbc )
		
		self._updateEnabledStatesOfButtons()

		self.setLayout( java.awt.BorderLayout( 8, 8 ) )
		self.add( javax.swing.JScrollPane( self._listVC ), java.awt.BorderLayout.CENTER )
		self.add( buttonPane, java.awt.BorderLayout.EAST )

	def _createListVC( self, listSelectionListener ):
		raise "Override"

	def _isDialogTriggeredOnAdd(self):
		raise "Override"
	def _isRenameDesired( self ):
		raise "Override"
		
	def _updateEnabledStatesOfButtons( self ):
		isSelected = False
		isMovableUp = False
		isMovableDown = False
		i = self._listVC.getSelectedIndex()
		if i != - 1:
			isSelected = True
			if i > 0:
				isMovableUp = True
			if i < ( self._listVC.getModel().getSize() - 1 ):
				isMovableDown = True
		self._removeButton.setEnabled( isSelected )
		self._moveUpButton.setEnabled( isMovableUp )
		self._moveDownButton.setEnabled( isMovableDown )
		self._renameButton.setEnabled( isSelected )

	def _handleNodeSelection( self, e ):
		self._updateEnabledStatesOfButtons()

	def _handleAdd( self, e ):
		raise "Override"

	def _handleRemove( self, e ):
		i = self._listVC.getSelectedIndex()
		self._listVC.getModel().handleRemove( i )
		self._listVC.setSelectedIndex( java.lang.Math.min( i, self._listVC.getModel().getSize() ) )
	def _handleMoveUp( self, e ):
		i = self._listVC.getSelectedIndex()
		self._listVC.getModel().handleMoveUp( i )
		self._listVC.setSelectedIndex( i - 1 )
	def _handleMoveDown( self, e ):
		i = self._listVC.getSelectedIndex()
		self._listVC.getModel().handleMoveDown( i )
		self._listVC.setSelectedIndex( i + 1 )
	
	def _handleRename( self, e ):
		print "todo: handle rename parameter"
#		model = self._listVC.getModel()
#		i = self._listVC.getSelectedIndex()
#		component = ecc.dennisc.alice.ide.RenameInputPane.RenameParameterPane( model._value, model._value.get( i ) )
#		owner = ecc.dennisc.alice.ide.IDE._singleton
#		name = ecc.dennisc.swing.showInputDialog( owner, component, "Rename" )
#		if name:
#			model.handleRename( i, name )
