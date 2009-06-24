#print "-->", __name__

import java
import javax
import edu

import ecc

from ecc.dennisc.alice.ide.TypeComboBox import TypeComboBox

from CreateDeclarationPane import CreateDeclarationPane

class CreateTypedDeclarationPane( CreateDeclarationPane ):
	def __init__( self, siblings, typeText ):
		self._typeText = typeText
		CreateDeclarationPane.__init__( self, siblings )
		
	def _createComponentRows( self ):
		rv = CreateDeclarationPane._createComponentRows( self )
		self._typeVC = TypeComboBox()
		self._isArrayVC = javax.swing.JCheckBox( "is array" )
		pane = javax.swing.JPanel()
		pane.setLayout( java.awt.BorderLayout() )
		pane.add( self._typeVC, java.awt.BorderLayout.CENTER )
		pane.add( self._isArrayVC, java.awt.BorderLayout.EAST )
		rv.append( ( javax.swing.JLabel( self._typeText, javax.swing.JLabel.TRAILING ), pane ) )
		return rv

	def _getType( self ):
		rv = self._typeVC.getSelectedItem()
		if self._isArrayVC.isSelected():
			rv = rv.getArrayType()
		return rv
	
#print "<--", __name__
