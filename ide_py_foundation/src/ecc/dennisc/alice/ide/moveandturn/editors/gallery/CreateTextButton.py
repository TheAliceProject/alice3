import java
import javax
import edu 
import org

import ecc

from edu.cmu.cs.dennisc import alice

def getSceneEditor():
	return alice.ide.IDE.getSingleton()._scenePane

class SelectFontPane( javax.swing.JPanel ):
	def __init__( self ):
		javax.swing.JPanel.__init__( self )
		
	def setValue(self, value):
		pass
	def getValue(self):
		rv = org.alice.apis.moveandturn.Font()
		return rv

from ecc.dennisc.alice.ide.IdentifierPane import IdentifierPane
from ecc.dennisc.alice.ide.PaneWithValidation import PaneWithValidation
from ecc.dennisc.alice.ide.CustomChoosers import DoubleChooser

class CreateTextPane( edu.cmu.cs.dennisc.swing.InputPane, edu.cmu.cs.dennisc.pattern.Validator ):
	def __init__( self, selected, siblings ):
		edu.cmu.cs.dennisc.swing.InputPane.__init__( self )

		inset = 16
		self.setBorder( javax.swing.border.EmptyBorder( inset, inset, inset, inset ) )

		self._textVC = javax.swing.JTextField()
		self._textVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( self._handleTextChange ) )
		self._instanceNameVC = javax.swing.JTextField()
		self._instanceNameVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( self._handleInstanceNameChange ) )
		self._classNameVC = javax.swing.JTextField()
		self._classNameVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( self._handleClassNameChange ) )

		self._constrainInstanceNameToTextVC = javax.swing.JCheckBox( "constrain to text" )
		self._constrainInstanceNameToTextVC.addItemListener( ecc.dennisc.swing.event.ItemAdapter( self._handleInstanceNameContraintChange ) )
		self._constrainInstanceNameToTextVC.setSelected( True )
		self._constrainClassNameToInstanceNameVC = javax.swing.JCheckBox( "constrain to instance" )
		self._constrainClassNameToInstanceNameVC.addItemListener( ecc.dennisc.swing.event.ItemAdapter( self._handleClassNameContraintChange ) )
		self._constrainClassNameToInstanceNameVC.setSelected( True )

		self._letterHeightChooser = DoubleChooser( 1.0, "letter height (in meters):" )
		self._letterHeightChooser.setInputPane( self )
		self.addOKButtonValidator( self._letterHeightChooser )

		self._fontChooser = org.alice.apis.moveandturn.ui.FontChooser()

		fontPane = javax.swing.JPanel()
		etchedBorder = javax.swing.border.EtchedBorder()
		titledBorder = javax.swing.border.TitledBorder( etchedBorder, "Font" )
		fontPane.setBorder( titledBorder )
		fontPane.setLayout( java.awt.BorderLayout() )
		fontPane.add( self._letterHeightChooser, java.awt.BorderLayout.NORTH )
		fontPane.add( self._fontChooser, java.awt.BorderLayout.CENTER )

#		self._textVC.setText( "dude" )
#		self._textVC.selectAll()

		self.setLayout( java.awt.GridBagLayout() )
		gbc = java.awt.GridBagConstraints()
		gbc.fill = java.awt.GridBagConstraints.BOTH
		gbc.insets.right = 16
		
		gbc.gridy = 0
		gbc.weightx = 0.0
		self.add( javax.swing.JLabel( "text:" ), gbc )
		gbc.weightx = 1.0
		self.add( self._textVC, gbc )
		gbc.weightx = 0.0
		self.add( javax.swing.JPanel(), gbc )
		
		
		gbc.gridy = 1
		gbc.weightx = 0.0
		self.add( javax.swing.JLabel( "instance:" ), gbc )
		gbc.weightx = 1.0
		self.add( self._instanceNameVC, gbc )
		gbc.weightx = 0.0
		self.add( self._constrainInstanceNameToTextVC, gbc )


		gbc.gridy = 2
		gbc.weightx = 0.0
		self.add( javax.swing.JLabel( "class:" ), gbc )
		gbc.weightx = 1.0
		self.add( self._classNameVC, gbc )
		gbc.weightx = 0.0
		self.add( self._constrainClassNameToInstanceNameVC, gbc )

		gbc.weighty = 1.0
		gbc.insets.top = 32
		gbc.gridy = 3
		gbc.gridwidth = 3
		self.add( fontPane, gbc )

		gbc.insets.top = 0
		gbc.gridy = 4
		self.add( javax.swing.JPanel(), gbc )

		self.addOKButtonValidator( self )

	def isValid( self ):
		instanceName = self._instanceNameVC.getText()
		className = self._classNameVC.getText()
		#todo: check siblings
		return ecc.dennisc.alice.vm.isValidIdentifier( instanceName ) and ecc.dennisc.alice.vm.isValidIdentifier( className )
	
	def _handleInstanceNameContraintChange( self, e ):
		self._instanceNameVC.setEditable( not e.getSource().isSelected() )

	def _handleClassNameContraintChange( self, e ):
		self._classNameVC.setEditable( not e.getSource().isSelected() )

	def _handleTextChange( self, text ):
		text = self._textVC.getText()
		if self._constrainInstanceNameToTextVC.isSelected():
			instanceName = ecc.dennisc.alice.vm.getConventionalInstanceName( text )
			self._instanceNameVC.setText( instanceName )
		self._fontChooser.setSampleText( text )
		self.updateOKButton()
	def _handleInstanceNameChange( self, text ):
		if self._constrainClassNameToInstanceNameVC.isSelected():
			instanceName = self._instanceNameVC.getText()
			className = ecc.dennisc.alice.vm.getConventionalClassName( instanceName )
			self._classNameVC.setText( className )
		self.updateOKButton()
	def _handleClassNameChange( self, text ):
		self.updateOKButton()
		
	def getActualInputValue( self ):
		typeDeclaredInJava = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Text )
		className = self._classNameVC.getText();
		constructors = [ alice.ast.ConstructorDeclaredInAlice( [], alice.ast.BlockStatement( [] ) ) ]
		type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice( className, None, typeDeclaredInJava, constructors, [], [] )
		rv = getSceneEditor().createInstance( type )
		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( rv )
		instanceInJava.setName( self._instanceNameVC.getText() )
		instanceInJava.setValue( self._textVC.getText() )
		instanceInJava.setFont( org.alice.apis.moveandturn.Font( self._fontChooser.getFont() ) )
		instanceInJava.setLetterHeight( self._letterHeightChooser.getActualInputValue() )
		return rv

class CreateTextOperation(alice.ide.AbstractOperation):
	def __init__(self):
		alice.ide.AbstractOperation.__init__(self)
		self.putValue( javax.swing.Action.NAME, "Create Text..." )
	def prepare(self, e, observer):
		owner = alice.ide.IDE.getSingleton()
		inputPane = CreateTextPane( None, [] )
		self._instance = inputPane.showInJDialog( owner, "Create Text", True )
		if self._instance:
			return alice.ide.Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY
		else:
			return alice.ide.Operation.PreparationResult.CANCEL
	def perform(self):
		getSceneEditor().addInstance( self._instance )

class CreateTextButton( alice.ide.Button ):
	def __init__( self ):
		alice.ide.Button.__init__( self, CreateTextOperation() )
