#print "-->", __name__

import java
import javax
import edu

import ecc

from edu.cmu.cs.dennisc import cascade
from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import zoot

def getIDE():
	return alice.ide.IDE.getSingleton()

class SimpleExpressionFillIn( cascade.SimpleFillIn ):
	def createMenuProxy(self):
		return alice.ide.editors.code.ExpressionPane( self.getModel() )

class PrevExpressionFillIn( SimpleExpressionFillIn ):
	def createMenuProxy(self):
		rv = zoot.ZLineAxisPane()
		rv.add( SimpleExpressionFillIn.createMenuProxy( self ) )
		rv.add( alice.ide.editors.common.Label( " (current value)" ) )
		return rv
	def getValue(self):
		src = SimpleExpressionFillIn.getValue( self )
		return getIDE().createCopy( src )

class ValueBlank( cascade.Blank ):
	def __init__( self, type ):
		cascade.Blank.__init__( self )
		self._type = type
	def addChildren(self):
		getIDE().addFillIns( self, self._type )

class ExpressionPropertyBlank( cascade.Blank ):
	def __init__( self, expressionProperty ):
		cascade.Blank.__init__( self )
		self._expressionProperty = expressionProperty
	def addChildren(self):
		getIDE().addFillIns( self, self._expressionProperty.getExpressionType() )

class ExpressionBlank( cascade.Blank ):
	def __init__( self, type ):
		self._type = type
	def addChildren( self ):
		getIDE().addFillIns( self, self._type )

class ParameterBlank( ExpressionBlank ):
	def __init__(self, parameter):
		self._parameter = parameter
		ExpressionBlank.__init__( self, parameter.getDesiredValueType() )

class ExpressionReceptorBlank( ExpressionBlank ):
	def __init__( self, type, previousValue=None ):
		ExpressionBlank.__init__( self, type )
		self._previousValue = previousValue
#	def _addNodeChildren(self):
#		alice.ide.IDE.getSingleton()._getExpressionFillInsForType( self, self.getModel(), self._previousValue )
#	def _getInputValue( self ):
#		return self._selectedFillIn.getExpression()

class MethodInvocationFillIn( cascade.FillIn ):
	def __init__( self, method, instanceOrTypeExpression ):
		self._method = method
		self._instanceOrTypeExpression = instanceOrTypeExpression
	def addChildren( self ):
#		if self._instanceOrTypeExpression:
#			pass
#		else:
#			self.addChild( ExpressionBlank( self._method.getDeclaringType() ) )
		for parameter in self._method.getParameters():
			self.addChild( ParameterBlank( parameter ) )
	def getValue(self):
		arguments = []
		for parameterBlank in self.getChildren():
			expression = parameterBlank.getSelectedFillIn().getValue()
			arguments.append( alice.ast.Argument( parameterBlank._parameter, expression ) )
		return alice.ast.MethodInvocation( self._instanceOrTypeExpression, self._method, arguments )
	
	def createMenuProxy(self):
		class MyFunctionTemplatePane( alice.ide.editors.type.FunctionTemplatePane ):
			def promptUserForMethodInvocation( self, instanceExpression, taskObserver, dndEvent ):
				dndEvent.handleCancelation()
		return MyFunctionTemplatePane( self._method, self._instanceOrTypeExpression )


class ArrayAccessFillIn( cascade.FillIn ):
	def __init__( self, arrayType, arrayExpression ):
		self._arrayType = arrayType
		self._arrayExpression = arrayExpression
	def addChildren( self ):
		self.addChild( ExpressionReceptorBlank( alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) )
	def getValue(self):
		blank = self.getChildren()[ 0 ]
		return alice.ast.ArrayAccess( self._arrayType, self._arrayExpression, blank.getSelectedFillIn().getValue() )
	def createMenuProxy(self):
		expression = alice.ast.ArrayAccess( self._arrayType, self._arrayExpression, alice.ide.editors.code.EmptyExpression( alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) )
		return alice.ide.editors.code.ExpressionPane( expression )

class InfixExpressionOperatorFillIn( SimpleExpressionFillIn ):
	def __init__(self, expression, operandType):
		SimpleExpressionFillIn.__init__( self, expression )
		self._operandType = operandType
	def getModel(self):
		rv = SimpleExpressionFillIn.getModel( self )
		leftValue = self.getParent().getParent().getChildren()[ 0 ].getSelectedFillIn().getTransientValue()
		if leftValue:
			pass
		else:
			leftValue = alice.ide.editors.code.EmptyExpression( self._operandType )
		rv.leftOperand.setValue( leftValue )
		return rv
	def isMenuItemIconUpToDate(self):
		return False
	def getMenuProxy(self):
		rv = self.createMenuProxy()
		return rv

class InfixExpressionOperatorBlank(cascade.Blank ):
	def _createInfixExpressionFillIn( self, operator, expressionInstantiator, operandCls ):
		operandType = ecc.dennisc.alice.ast.getType( operandCls )
		leftValue = alice.ide.editors.code.EmptyExpression( operandType )
		rightValue = alice.ide.editors.code.EmptyExpression( operandType )
		return InfixExpressionOperatorFillIn( expressionInstantiator( leftValue, operator, rightValue ), operandType )

class ConditionalInfixExpressionOperatorBlank( InfixExpressionOperatorBlank ):
	def addChildren( self ):
		self.addChild( self._createConditionalInfixExpressionFillIn( alice.ast.ConditionalInfixExpression.Operator.AND ) )
		self.addChild( self._createConditionalInfixExpressionFillIn( alice.ast.ConditionalInfixExpression.Operator.OR ) )
	def _createConditionalInfixExpressionFillIn( self, operator ):
		return self._createInfixExpressionFillIn( operator, alice.ast.ConditionalInfixExpression, java.lang.Boolean )

class RelationalInfixExpressionOperatorBlank( InfixExpressionOperatorBlank ):
	def __init__( self, expressionInstantiator, operandCls ):
		self._expressionInstantiator = expressionInstantiator
		self._operandCls = operandCls
	def addChildren( self ):
		self.addChild( self._createRelationalInfixExpressionFillIn( alice.ast.RelationalInfixExpression.Operator.EQUALS ) )
		self.addChild( self._createRelationalInfixExpressionFillIn( alice.ast.RelationalInfixExpression.Operator.NOT_EQUALS ) )
		self.addChild( self._createRelationalInfixExpressionFillIn( alice.ast.RelationalInfixExpression.Operator.LESS ) )
		self.addChild( self._createRelationalInfixExpressionFillIn( alice.ast.RelationalInfixExpression.Operator.LESS_EQUALS ) )
		self.addChild( self._createRelationalInfixExpressionFillIn( alice.ast.RelationalInfixExpression.Operator.GREATER ) )
		self.addChild( self._createRelationalInfixExpressionFillIn( alice.ast.RelationalInfixExpression.Operator.GREATER_EQUALS ) )
	def _createRelationalInfixExpressionFillIn( self, operator ):
		return self._createInfixExpressionFillIn( operator, self._expressionInstantiator, self._operandCls )

class ArithmeticInfixExpressionOperatorBlank( InfixExpressionOperatorBlank ):
	def __init__( self, expressionInstantiator, operandCls ):
		self._expressionInstantiator = expressionInstantiator
		self._operandCls = operandCls
	def addChildren( self ):
		self.addChild( self._createArithmeticInfixExpressionFillIn( alice.ast.ArithmeticInfixExpression.Operator.PLUS ) )
		self.addChild( self._createArithmeticInfixExpressionFillIn( alice.ast.ArithmeticInfixExpression.Operator.MINUS ) )
		self.addChild( self._createArithmeticInfixExpressionFillIn( alice.ast.ArithmeticInfixExpression.Operator.TIMES ) )
		self.addChild( self._createArithmeticInfixExpressionFillIn( alice.ast.ArithmeticInfixExpression.Operator.REAL_DIVIDE ) )
		self.addChild( self._createArithmeticInfixExpressionFillIn( alice.ast.ArithmeticInfixExpression.Operator.INTEGER_DIVIDE ) )
		self.addChild( self._createArithmeticInfixExpressionFillIn( alice.ast.ArithmeticInfixExpression.Operator.REAL_REMAINDER ) )
		self.addChild( self._createArithmeticInfixExpressionFillIn( alice.ast.ArithmeticInfixExpression.Operator.INTEGER_REMAINDER ) )
	def _createArithmeticInfixExpressionFillIn( self, operator ):
		return self._createInfixExpressionFillIn( operator, self._expressionInstantiator, self._operandCls )


class InfixExpressionFillIn( cascade.FillIn ):
	def __init__( self, expressionInstantiator, operatorBlankInstantiator, text ):
		edu.cmu.cs.dennisc.cascade.FillIn.__init__( self )
		self._expressionInstantiator = expressionInstantiator
		self._operatorBlankInstantiator = operatorBlankInstantiator
		self._text = text
	def addChildren( self ):
		instance = self._expressionInstantiator()
		self.addChild( ExpressionPropertyBlank( instance.leftOperand ) )
		self.addChild( self._operatorBlankInstantiator() )
		self.addChild( ExpressionPropertyBlank( instance.rightOperand ) )
	def getValue( self ):
		rv = self._expressionInstantiator()
		rv.leftOperand.setValue( self.getChildren()[ 0 ].getSelectedFillIn().getValue() )
		rv.operator.setValue( self.getChildren()[ 1 ].getSelectedFillIn().getValue().operator.getValue() )
		rv.rightOperand.setValue( self.getChildren()[ 2 ].getSelectedFillIn().getValue() )
		return rv
	def createMenuProxy( self ):
		return javax.swing.JLabel( self._text )

class ConditionalInfixExpressionFillIn( InfixExpressionFillIn ):
	def __init__( self ):
		InfixExpressionFillIn.__init__( self, alice.ast.ConditionalInfixExpression, ConditionalInfixExpressionOperatorBlank, "conditional { and, or }" )

class RelationalInfixExpressionFillIn( InfixExpressionFillIn ):
	def __init__( self, text, expressionAndOperandCls ):
		InfixExpressionFillIn.__init__( self, self._instantiateRelationalInfixExpression, self._instantiateRelationalInfixExpressionOperatorBlank, text )
		self._expressionAndOperandCls = expressionAndOperandCls
	def _instantiateRelationalInfixExpression( self, left=None, operator=None, right=None ):
		return alice.ast.RelationalInfixExpression( ecc.dennisc.alice.ast.getType( self._expressionAndOperandCls ), left, operator, right )
	def _instantiateRelationalInfixExpressionOperatorBlank( self ):
		return RelationalInfixExpressionOperatorBlank( self._instantiateRelationalInfixExpression, self._expressionAndOperandCls )


class ArithmeticInfixExpressionFillIn( InfixExpressionFillIn ):
	def __init__( self, expressionCls, operandCls ):
		InfixExpressionFillIn.__init__( self, self._instantiateArithmeticInfixExpression, self._instantiateArithmeticInfixExpressionOperatorBlank, "arithmetic { +, -, *, /, remainder }" )
		self._expressionCls = expressionCls
		self._operandCls = operandCls
	def _instantiateArithmeticInfixExpression( self, left=None, operator=None, right=None ):
		return alice.ast.ArithmeticInfixExpression( ecc.dennisc.alice.ast.getType( self._expressionCls ), left, operator, right )
	def _instantiateArithmeticInfixExpressionOperatorBlank( self ):
		return ArithmeticInfixExpressionOperatorBlank( self._instantiateArithmeticInfixExpression, self._operandCls )

class StringConcatenationFillIn( cascade.FillIn ):
	def addChildren( self ):
		instance = alice.ast.StringConcatenation()
		self.addChild( ExpressionPropertyBlank( instance.leftOperand ) )
		self.addChild( ExpressionPropertyBlank( instance.rightOperand ) )
	def getValue( self ):
		rv = alice.ast.StringConcatenation()
		rv.leftOperand.setValue( self.getChildren()[ 0 ].getSelectedFillIn().getValue() )
		rv.rightOperand.setValue( self.getChildren()[ 1 ].getSelectedFillIn().getValue() )
		return rv
	def createMenuProxy( self ):
		operandType = ecc.dennisc.alice.ast.getType( java.lang.Object )
		leftValue = alice.ide.editors.code.EmptyExpression( operandType )
		rightValue = alice.ide.editors.code.EmptyExpression( operandType )
		return alice.ide.editors.code.ExpressionPane( alice.ast.StringConcatenation( leftValue, rightValue ) )

class SimpleStatementFillIn( cascade.SimpleFillIn ):
	def createMenuProxy(self):
		return alice.ide.editors.code.AbstractStatementPane.createPane( self.getModel(), None )

class IncompleteStatementFillIn( cascade.FillIn ):
	def __init__(self, incomplete):
		cascade.FillIn.__init__( self )
		self._incomplete = incomplete
	def createMenuProxy( self ):
		return alice.ide.ast.NodeUtilities.createPane( self._incomplete )
	
class CountLoopFillIn( IncompleteStatementFillIn ):
	def __init__(self):
		IncompleteStatementFillIn.__init__( self, alice.ide.ast.NodeUtilities.createIncompleteCountLoop() )
	def addChildren( self ):
		self.addChild( ExpressionPropertyBlank( self._incomplete.count ) )
	def getValue( self ):
		return alice.ide.ast.NodeUtilities.createCountLoop( self.getChildren()[ 0 ].getSelectedFillIn().getValue() )

class WhileLoopFillIn( IncompleteStatementFillIn ):
	def __init__(self):
		IncompleteStatementFillIn.__init__( self, alice.ide.ast.NodeUtilities.createIncompleteWhileLoop() )
	def addChildren( self ):
		self.addChild( ExpressionPropertyBlank( self._incomplete.conditional ) )
	def getValue( self ):
		return alice.ide.ast.NodeUtilities.createWhileLoop( self.getChildren()[ 0 ].getSelectedFillIn().getValue() )

class StatementBlank( cascade.Blank ):
	def addChildren( self ):
		self.addChild( SimpleStatementFillIn( alice.ide.ast.NodeUtilities.createDoInOrder() ) )
		self.addChild( CountLoopFillIn() )
		self.addChild( WhileLoopFillIn() )
		self.addChild( SimpleStatementFillIn( alice.ide.ast.NodeUtilities.createDoTogether() ) )
		self.addChild( SimpleStatementFillIn( alice.ide.ast.NodeUtilities.createComment() ) )

#class ArithmeticMenuFillIn( edu.cmu.cs.dennisc.cascade.MenuFillIn ):
#	def __init__(self, type):
#		edu.cmu.cs.dennisc.cascade.MenuFillIn.__init__( self, "arithmetic { +, -, *, /, remainder }" )
#		self._type = type
#	def addChildrenToBlank(self, blank):
#		blank.addChild( self._createArithmeticInfixExpressionFillIn( alice.ast.ArithmeticInfixExpression.Operator.PLUS ) )
#		blank.addChild( self._createArithmeticInfixExpressionFillIn( alice.ast.ArithmeticInfixExpression.Operator.MINUS ) )
#		blank.addChild( self._createArithmeticInfixExpressionFillIn( alice.ast.ArithmeticInfixExpression.Operator.TIMES ) )
#		blank.addChild( self._createArithmeticInfixExpressionFillIn( alice.ast.ArithmeticInfixExpression.Operator.DIVIDE ) )
#		blank.addChild( self._createArithmeticInfixExpressionFillIn( alice.ast.ArithmeticInfixExpression.Operator.REMAINDER ) )
#	def _createArithmeticInfixExpressionFillIn(self, operator):
#		return ecc.dennisc.alice.ide.cascade.InfixExpressionFillIn( alice.ast.ArithmeticInfixExpression( self._type, alice.ide.editors.code.EmptyExpression( self._type ), operator, alice.ide.editors.code.EmptyExpression( self._type ) ) )
#
#class RelationalMenuFillIn(cascade.MenuFillIn ):
#	def __init__( self, type, text ):
#		edu.cmu.cs.dennisc.cascade.MenuFillIn.__init__( self, text )
#		self._type = type
#	def addChildrenToBlank( self, blank ):
#		blank.addChild( self._createRelationalInfixExpressionFillIn( alice.ast.RelationalInfixExpression.Operator.EQUALS ) )
#		blank.addChild( self._createRelationalInfixExpressionFillIn( alice.ast.RelationalInfixExpression.Operator.NOT_EQUALS ) )
#		blank.addChild( self._createRelationalInfixExpressionFillIn( alice.ast.RelationalInfixExpression.Operator.LESS ) )
#		blank.addChild( self._createRelationalInfixExpressionFillIn( alice.ast.RelationalInfixExpression.Operator.LESS_EQUALS ) )
#		blank.addChild( self._createRelationalInfixExpressionFillIn( alice.ast.RelationalInfixExpression.Operator.GREATER ) )
#		blank.addChild( self._createRelationalInfixExpressionFillIn( alice.ast.RelationalInfixExpression.Operator.GREATER_EQUALS ) )
#
#	def _createRelationalInfixExpressionFillIn( self, operator ):
#		return InfixExpressionFillIn( alice.ast.RelationalInfixExpression( self._type, alice.ide.editors.code.EmptyExpression( self._type ), operator, alice.ide.editors.code.EmptyExpression( self._type ) ) )

class SetArrayAtIndexFillIn( cascade.FillIn ):
	def __init__( self, rightExpressionType ):
		cascade.FillIn.__init__( self )
		self._rightExpressionType = rightExpressionType
	def addChildren( self ):
		self.addChild( ecc.dennisc.alice.ide.cascade.ExpressionReceptorBlank( alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) )
		self.addChild( ecc.dennisc.alice.ide.cascade.ExpressionReceptorBlank( self._rightExpressionType ) )
	def _createExpression(self, indexExpression, rightExpression ):
		raise "Override"
	def getValue(self):
		blank0 = self.getChildren()[ 0 ]
		blank1 = self.getChildren()[ 1 ]
		indexExpression = blank0.getSelectedFillIn().getValue()
		rightExpression = blank1.getSelectedFillIn().getValue()
		return self._createExpression( indexExpression, rightExpression )

class SetArrayVariableAtIndexFillIn( SetArrayAtIndexFillIn ):
	def __init__( self, variable ):
		self._variable = variable
		SetArrayAtIndexFillIn.__init__( self, self._variable.valueType.getValue().getComponentType() )
	def _createExpression(self, indexExpression, rightExpression ):
		arrayType = self._variable.valueType.getValue()
		accessExpression = alice.ast.ArrayAccess( arrayType, alice.ast.VariableAccess( self._variable ), indexExpression )
		return alice.ast.AssignmentExpression( arrayType.getComponentType(), accessExpression, alice.ast.AssignmentExpression.Operator.ASSIGN, rightExpression )

class SetArrayParameterAtIndexFillIn( SetArrayAtIndexFillIn ):
	def __init__( self, parameter ):
		self._parameter = parameter
		SetArrayAtIndexFillIn.__init__( self, self._parameter.valueType.getValue().getComponentType() )
	def _createExpression(self, indexExpression, rightExpression ):
		arrayType = self._parameter.valueType.getValue()
		accessExpression = alice.ast.ArrayAccess( arrayType, alice.ast.ParameterAccess( self._parameter ), indexExpression )
		return alice.ast.AssignmentExpression( arrayType.getComponentType(), accessExpression, alice.ast.AssignmentExpression.Operator.ASSIGN, rightExpression )

class SetArrayFieldAtIndexFillIn( SetArrayAtIndexFillIn ):
	def __init__( self, instanceExpression, field ):
		self._instanceExpression = instanceExpression
		self._field = field
		SetArrayAtIndexFillIn.__init__( self, self._field.valueType.getValue().getComponentType() )
	def _createExpression(self, indexExpression, rightExpression ):
		arrayType = self._field.valueType.getValue()
		accessExpression = alice.ast.ArrayAccess( arrayType, alice.ast.FieldAccess( self._instanceExpression, self._field ), indexExpression )
		return alice.ast.AssignmentExpression( arrayType.getComponentType(), accessExpression, alice.ast.AssignmentExpression.Operator.ASSIGN, rightExpression )

from CustomFillIns import *

#print "<--", __name__
