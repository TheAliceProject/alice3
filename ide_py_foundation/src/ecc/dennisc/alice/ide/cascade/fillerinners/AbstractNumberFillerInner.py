#print "-->", __name__

from ExpressionFillerInner import *

class AbstractNumberFillerInner( ExpressionFillerInner ):
	def __init__( self, type, expressionCls ):
		ExpressionFillerInner.__init__( self, type, expressionCls )
	def _addArithmeticFillIns( self, blank, valueType, operandType ):
		blank.addChild( ecc.dennisc.alice.ide.cascade.ArithmeticInfixExpressionFillIn( valueType, operandType ) )

#print "<--", __name__
