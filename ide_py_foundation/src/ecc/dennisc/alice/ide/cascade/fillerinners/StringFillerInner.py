#print "-->", __name__

from ExpressionFillerInner import *

class StringFillerInner( ExpressionFillerInner ):
	def __init__( self ):
		ExpressionFillerInner.__init__( self, ecc.dennisc.alice.ast.getType( java.lang.String ), alice.ast.StringLiteral )
	def addFillIns( self, blank ):
		self._addExpressionFillIn( blank, "hello" )
		blank.addSeparator()
		blank.addChild( ecc.dennisc.alice.ide.cascade.CustomStringFillIn() )
		blank.addSeparator()
		blank.addChild( ecc.dennisc.alice.ide.cascade.StringConcatenationFillIn() )

#print "<--", __name__
