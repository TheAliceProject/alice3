#print "-->", __name__

import java
import javax
import edu
import org

import ecc

from edu.cmu.cs.dennisc import alice

class ExpressionFillerInner:
	def __init__( self, type, expressionCls ):
		self._type = type
		self._expressionCls = expressionCls
	def isAssignableTo( self, type ):
		return self._type.isAssignableTo( type )
	def _addExpressionFillIn( self, blank, *args ):
		blank.addChild( ecc.dennisc.alice.ide.cascade.SimpleExpressionFillIn( apply( self._expressionCls, args ) ) )

#print "<--", __name__
