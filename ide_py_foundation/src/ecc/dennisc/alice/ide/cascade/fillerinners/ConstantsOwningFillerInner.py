#print "-->", __name__

from ExpressionFillerInner import *

class ConstantsOwningFillerInner( ExpressionFillerInner ):
	def __init__( self, type ):
		ExpressionFillerInner.__init__( self, type, alice.ast.FieldAccess )
	def addFillIns( self, blank ):
		constants = []
		for field in self._type.getDeclaredFields():
			if field.isPublicAccess() and field.isStatic() and field.isFinal():
				constants.append( field )
		for constant in constants:
			self._addExpressionFillIn( blank, alice.ast.TypeExpression( self._type ), constant )

#print "<--", __name__
