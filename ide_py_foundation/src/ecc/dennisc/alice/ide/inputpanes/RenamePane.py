#print "-->", __name__

import java
import javax
import edu

from AbstractInputPane import AbstractInputPane

class RenamePane( AbstractInputPane ):
	def __init__( self, selected, siblings ):
		AbstractInputPane.__init__( self )
		
		from ecc.dennisc.alice.ide.IdentifierPane import IdentifierPane
		self._identifierPane = IdentifierPane( selected, siblings )
		self._identifierPane.setInputPane( self )
		
		self.addOKButtonValidator( self._identifierPane )
		self.setLayout( java.awt.BorderLayout() )
		self.add( self._identifierPane, java.awt.BorderLayout.NORTH )

	def getActualInputValue( self ):
		return self._identifierPane.getText()

#print "<--", __name__
