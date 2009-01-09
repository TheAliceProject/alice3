#print "-->", __name__

import java
import javax
import edu

import ecc

from AbstractInputPane import AbstractInputPane

class CreateDeclarationPane( AbstractInputPane ):
	def __init__( self, siblings ):
		AbstractInputPane.__init__( self )
		
		pane = javax.swing.JPanel()
		self._siblings = siblings
		ecc.dennisc.swing.springItUpANotch( pane, self._createComponentRows(), 0, 0, 12, 12 )
		
		self.setLayout( java.awt.BorderLayout() )
		self.add( pane, java.awt.BorderLayout.NORTH )
		self.add( javax.swing.JPanel(), java.awt.BorderLayout.CENTER )
		
		inset = 16
		self.setBorder( javax.swing.border.EmptyBorder( inset, inset, inset, inset ) )
		
	def _createComponentRows( self ):
		from ecc.dennisc.alice.ide.IdentifierPane import IdentifierPane
		self._identifierPane = IdentifierPane( None, self._siblings )
		self._identifierPane.setInputPane( self )
		self.addOKButtonValidator( self._identifierPane )
		rv = []
		rv.append( self._identifierPane.getComponents() )
		return rv

	def _getName( self ):
		return self._identifierPane.getText()

	def getActualInputValue( self ):
		raise "Override"

#print "<--", __name__
