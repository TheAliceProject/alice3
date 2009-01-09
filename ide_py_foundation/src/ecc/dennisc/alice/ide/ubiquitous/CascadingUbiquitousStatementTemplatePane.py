import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

import ecc

class CascadingUbiquitousStatementTemplatePane( alice.ide.editors.ubiquitous.ExpressionRequiringUbiquitousStatementTemplatePane ):
	def __init__( self, cls, blankType ):
		self._blankType = blankType
		alice.ide.editors.ubiquitous.ExpressionRequiringUbiquitousStatementTemplatePane.__init__( self, cls, apply( cls, self._createArgsForEmpty() ) )
	def _createArgs( self, expression ):
		raise "Override"
	def _createArgsForEmpty( self ):
		return self._createArgs( alice.ide.editors.code.EmptyExpression( self._blankType ) )

	def createUbiquitousStatement( self, expression ):
		return apply( self.cls, self._createArgs( expression ) )
	
	def promptUserForExpression( self, observer, dndEvent ):
		fillIn = ecc.dennisc.alice.ide.cascade.ExpressionReceptorBlank( self._blankType )
		me = dndEvent.getEndingMouseEvent()
		fillIn.showPopupMenu( me.getSource(), me.getX(), me.getY(), observer )		

