#print "-->", __name__

from AbstractNumberFillerInner import *

class NumberFillerInner( AbstractNumberFillerInner ):
	def __init__( self ):
		AbstractNumberFillerInner.__init__( self, ecc.dennisc.alice.ast.getType( java.lang.Double ), alice.ast.DoubleLiteral )
	def addFillIns( self, blank ):
		self._addExpressionFillIn( blank, 0.0 )
		self._addExpressionFillIn( blank, 0.25 )
		self._addExpressionFillIn( blank, 0.5 )
		self._addExpressionFillIn( blank, 1.0 )
		self._addExpressionFillIn( blank, 2.0 )
		self._addExpressionFillIn( blank, 5.0 )
		self._addExpressionFillIn( blank, 10.0 )
		self._addExpressionFillIn( blank, 100.0 )
		blank.addChild( ecc.dennisc.alice.ide.cascade.CustomDoubleFillIn() )
		blank.addSeparator()
		self._addArithmeticFillIns( blank, ecc.dennisc.alice.ast.getType( java.lang.Double ), ecc.dennisc.alice.ast.getType( java.lang.Number ) )

#print "<--", __name__
