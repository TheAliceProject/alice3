#print "-->", __name__

from ExpressionFillerInner import *

class BooleanFillerInner( ExpressionFillerInner ):
	def __init__( self ):
		ExpressionFillerInner.__init__( self, ecc.dennisc.alice.ast.getType( java.lang.Boolean ), alice.ast.BooleanLiteral )
	def addFillIns( self, blank ):
		self._addExpressionFillIn( blank, True )
		self._addExpressionFillIn( blank, False )
		blank.addSeparator()
		blank.addChild( ecc.dennisc.alice.ide.cascade.ConditionalInfixExpressionFillIn() )
		blank.addChild( ecc.dennisc.alice.ide.cascade.RelationalInfixExpressionFillIn( "relational expressions { ==, !=, <, <=, >=, > } (Real Number)", java.lang.Double ) )
		blank.addChild( ecc.dennisc.alice.ide.cascade.RelationalInfixExpressionFillIn( "relational expressions { ==, !=, <, <=, >=, > } (Integer)", java.lang.Integer ) )


#class ConditionalMenuFillIn( edu.cmu.cs.dennisc.cascade.MenuFillIn ):
#	def __init__(self):
#		edu.cmu.cs.dennisc.cascade.MenuFillIn.__init__( self, "conditional { and, or }" )
#	def addChildrenToBlank(self, blank):
#		blank.addChild( self._createConditionalInfixExpressionFillIn( alice.ast.ConditionalInfixExpression.Operator.AND ) )
#		blank.addChild( self._createConditionalInfixExpressionFillIn( alice.ast.ConditionalInfixExpression.Operator.OR ) )
#	def _createConditionalInfixExpressionFillIn( self, operator ):
#		type = ecc.dennisc.alice.ast.getType( java.lang.Boolean )
#		return ecc.dennisc.alice.ide.cascade.InfixExpressionFillIn( alice.ast.ConditionalInfixExpression( alice.ide.editors.code.EmptyExpression( type ), operator, alice.ide.editors.code.EmptyExpression( type ) ) )


#class InfixExpressionFillIn2( edu.cmu.cs.dennisc.cascade.SimpleFillIn ):
#	def __init__( self, infixExpression, cls ):
#		edu.cmu.cs.dennisc.cascade.SimpleFillIn.__init__( self, infixExpression )
#		self._type = ecc.dennisc.alice.ast.getType( cls )
#
#	def createMenuProxy(self):
#		return alice.ide.editors.code.ExpressionPane( self.getModel() )

#class ConditionalMenuFillIn( edu.cmu.cs.dennisc.cascade.FillIn ):
#	def __init__(self):
#		edu.cmu.cs.dennisc.cascade.FillIn.__init__( self )
#		self._expression = alice.ast.ConditionalInfixExpression()
#	def addChildren(self):
#		self.addChild( ecc.dennisc.alice.ide.cascade.ExpressionPropertyBlank( self._expression.leftOperand ) )
#		self.addChild( OperatorBlank() )
#		self.addChild( ecc.dennisc.alice.ide.cascade.ExpressionPropertyBlank( self._expression.rightOperand ) )
#	def getValue(self):
#		rv = alice.ast.ConditionalInfixExpression()
#		rv.leftOperand.setValue( self.getChildren()[ 0 ].getSelectedFillIn().getValue() )
#		rv.operator.setValue( self.getChildren()[ 1 ].getSelectedFillIn().getValue().operator.getValue() )
#		rv.rightOperand.setValue( self.getChildren()[ 2 ].getSelectedFillIn().getValue() )
#		return rv
#	def createMenuProxy(self):
#		return javax.swing.JLabel( "conditional { and, or }" )

#print "<--", __name__
