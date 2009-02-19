import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

import ecc

class CascadingUbiquitousStatementTemplatePane( alice.ide.editors.ubiquitous.ExpressionRequiringUbiquitousStatementTemplatePane ):
	def __init__( self, statementCls, blankType ):
		self._statementCls = statementCls
		self._blankType = blankType
		alice.ide.editors.ubiquitous.ExpressionRequiringUbiquitousStatementTemplatePane.__init__( self, apply( self._statementCls, self._createArgsForEmpty() ) )

	def _createArgs( self, expression ):
		raise "Override"
	def _createArgsForEmpty( self ):
		return self._createArgs( alice.ide.editors.code.EmptyExpression( self._blankType ) )

	def createUbiquitousStatement( self, expression ):
		return apply( self._statementCls, self._createArgs( expression ) )
	
	def promptUserForExpression( self, observer, dndEvent ):
		node = ecc.dennisc.alice.ide.cascade.ExpressionReceptorBlank( self._blankType )
		me = dndEvent.getEndingMouseEvent()
		node.showPopupMenu( me.getSource(), me.getX(), me.getY(), observer )		

