#print "-->", __name__

from AbstractNumberFillerInner import *

class IntegerFillerInner( AbstractNumberFillerInner ):
	def __init__( self ):
		AbstractNumberFillerInner.__init__( self, ecc.dennisc.alice.ast.getType( java.lang.Integer ), alice.ast.IntegerLiteral )
	def addFillIns( self, blank ):
		self._addExpressionFillIn( blank, 0 )
		self._addExpressionFillIn( blank, 1 )
		self._addExpressionFillIn( blank, 2 )
		self._addExpressionFillIn( blank, 3 )
		self._addExpressionFillIn( blank, 4 )
		self._addExpressionFillIn( blank, 5 )
		self._addExpressionFillIn( blank, 6 )
		self._addExpressionFillIn( blank, 7 )
		self._addExpressionFillIn( blank, 8 )
		self._addExpressionFillIn( blank, 9 )
		blank.addChild( ecc.dennisc.alice.ide.cascade.CustomIntegerFillIn() )
		blank.addSeparator()
		self._addArithmeticFillIns( blank, self._type, self._type )

#print "<--", __name__
