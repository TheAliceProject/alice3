import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

import ecc

class MathFunctionsBlank( edu.cmu.cs.dennisc.cascade.Blank ):
	def __init__( self ):
		edu.cmu.cs.dennisc.cascade.Blank.__init__( self )
		self._type = alice.ast.TypeDeclaredInJava.get( java.lang.Math )
		self._expression = alice.ast.TypeExpression( self._type )

	def _addNodeChildForMethod( self, methodName, parameterTypes ):
		self.addChild( ecc.dennisc.alice.ide.cascade.MethodInvocationFillIn( self._type.getDeclaredMethod( methodName, parameterTypes ), self._expression ) )
		
	def addChildren( self ):
		self._addNodeChildForMethod( "random", [] )
		self._addNodeChildForMethod( "abs", [ java.lang.Double.TYPE ] )
		self._addNodeChildForMethod( "ceil", [ java.lang.Double.TYPE ] )
		self._addNodeChildForMethod( "floor", [ java.lang.Double.TYPE ] )
		self._addNodeChildForMethod( "rint", [ java.lang.Double.TYPE ] )
		self._addNodeChildForMethod( "min", [ java.lang.Double.TYPE, java.lang.Double.TYPE ] )
		self._addNodeChildForMethod( "max", [ java.lang.Double.TYPE, java.lang.Double.TYPE ] )
		self._addNodeChildForMethod( "sqrt", [ java.lang.Double.TYPE ] )
		self._addNodeChildForMethod( "pow", [ java.lang.Double.TYPE, java.lang.Double.TYPE ] )

#		self._addNodeChildForMethod( "acos", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "asin", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "atan", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "atan2", [ java.lang.Double.TYPE, java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "cbrt", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "cos", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "cosh", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "exp", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "expm1", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "hypot", [ java.lang.Double.TYPE, java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "IEEEremainder", [ java.lang.Double.TYPE, java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "log", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "log10", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "log1p", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "signum", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "sin", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "sinh", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "tan", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "tanh", [ java.lang.Double.TYPE ] )
#		self._addNodeChildForMethod( "ulp", [ java.lang.Double.TYPE ] )
		#type = self.getModel().expression.getExpressionType()
	

class MathFunctionsTemplatePane( alice.ide.editors.code.AccessiblePane ):
	def __init__( self ):
		alice.ide.editors.code.AccessiblePane.__init__( self )
		self.add( alice.ide.editors.common.Label( "Math" ) )
		self.setBackground( alice.ide.IDE.getSingleton().getFunctionColor() )

	def getExpressionType( self ):
		return alice.ast.TypeDeclaredInJava.DOUBLE_PRIMITIVE_TYPE
	
	def isActuallyPotentiallyActive( self ):
		return True
	def isActuallyPotentiallyDraggable( self ):
		return True
	
	def _createExpression( self, dndEvent, observer ):
		me = dndEvent.getEndingMouseEvent()
		blank = MathFunctionsBlank()
		blank.showPopupMenu( me.getSource(), me.getX(), me.getY(), observer )	
	
	def createExpression( self, dndEvent ):
		class MyBlockingTaskObserver( edu.cmu.cs.dennisc.task.BlockingTaskObserver ):
			def run(self):
				me = dndEvent.getEndingMouseEvent()
				blank = MathFunctionsBlank()
				blank.showPopupMenu( me.getSource(), me.getX(), me.getY(), self )	
		observer = MyBlockingTaskObserver()
		return observer.getResult()

