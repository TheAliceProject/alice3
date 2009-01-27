import java
import javax
import edu

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import zoot

import ecc

from CascadingUbiquitousStatementTemplatePane import CascadingUbiquitousStatementTemplatePane

class ReturnStatementTemplatePane( CascadingUbiquitousStatementTemplatePane ):
	def __init__( self ):
		CascadingUbiquitousStatementTemplatePane.__init__( self, alice.ast.ReturnStatement, None )
	def setReturnType( self, type ):
		self._blankType = type
	def _createArgs( self, expression ):
		return [ self._blankType, expression ]

class ReturnStatementOwnerPane( zoot.ZPane ):
	def __init__( self ):
		zoot.ZPane.__init__( self )
		self.setLayout( java.awt.GridLayout( 1, 1 ) )
		self._returnStatementPane = ReturnStatementTemplatePane()
	def handleMethodChange( self, code ):
		if code:
			if isinstance( code, alice.ast.ConstructorDeclaredInAlice ) or code.isProcedure():
				self.removeAll()
			else:
				returnType = code.getReturnType()
				self._returnStatementPane.setReturnType( returnType )
				if self._returnStatementPane.getParent() == self:
					pass
				else:
					self.add( self._returnStatementPane )
