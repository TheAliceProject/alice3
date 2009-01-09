import java
import javax
import edu

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import cascade

import ecc

from ecc.dennisc.alice.ide.CustomChoosers import *


class CustomChooserInputPane( edu.cmu.cs.dennisc.swing.InputPane ):
	def __init__( self, chooser ):
		edu.cmu.cs.dennisc.swing.InputPane.__init__( self )
		self._chooser = chooser
		self.setLayout( java.awt.BorderLayout() )
		self.add( self._chooser, java.awt.BorderLayout.CENTER )
		self._chooser.setInputPane( self )
		self.addOKButtonValidator( self._chooser )
	def getActualInputValue( self ):
		return self._chooser.getActualInputValue()

class CustomFillIn( cascade.SimpleFillIn ):
	def __init__( self, model=None ):
		cascade.SimpleFillIn.__init__( self, model )
		try:
			self._previousValue = ecc.dennisc.alice.ide.getIDE()._prevExpresssion.value.getValue()
		except:
			self._previousValue = None
	def _getMenuItemText(self):
		raise "Override"
	def createMenuProxy(self):
		return javax.swing.JLabel( self._getMenuItemText() )
	def _createView( self ):
		return CustomView()
	def getTransientValue(self):
		return None
	def getValue( self ):
		owner = alice.ide.IDE.getSingleton()
		chooser = self._createChooser()
		inputPane = CustomChooserInputPane( chooser )
		inputValue = inputPane.showInJDialog( owner, self._getMessage(), True )

#		if self._previousValue is None:
#			pass
#		else:
#			component.setInitialValue( self._previousValue )
#
#		inputValue = ecc.dennisc.swing.showInputDialog( owner, component, "Custom" )
		if inputValue != None:
			return self._createExpression( inputValue ) 
		else:
			raise cascade.CancelException( "cancel custom input" )

class CustomStringFillIn( CustomFillIn ):
	def _createChooser( self ):
		return StringChooser( self._previousValue )
	def _getMenuItemText(self):
		return "Custom String..."
	def _getMessage( self ):
		return "Enter a String"
	def _createExpression( self, inputValue ):
		return alice.ast.StringLiteral( inputValue )

class CustomIntegerFillIn( CustomFillIn ):
	def _createExpression( self, inputValue ):
		return alice.ast.IntegerLiteral( inputValue ) 
	def _createChooser( self ):
		return IntegerChooser( self._previousValue )
	def _getMenuItemText(self):
		return "Custom Integer..."
	def _getMessage( self ):
		return "Enter an Integer"

class CustomRealNumberFillIn( CustomFillIn ):
	def _getMessage( self ):
		return "Enter a Real Number"
	def _getMenuItemText(self):
		return "Custom Real Number..."
class CustomFloatFillIn( CustomRealNumberFillIn ):
	def _createExpression( self, inputValue ):
		return alice.ast.FloatLiteral( inputValue ) 
	def _createChooser( self ):
		return FloatChooser( self._previousValue )
class CustomDoubleFillIn( CustomRealNumberFillIn ):
	def _createExpression( self, inputValue ):
		return alice.ast.DoubleLiteral( inputValue ) 
	def _createChooser( self ):
		return DoubleChooser( self._previousValue )

class CustomAngleFillIn( CustomFillIn ):
	def _createChooser( self ):
		return AngleChooser( self._previousValue )
	def _getMenuItemText(self):
		return "Custom Angle..."
	def _getMessage( self ):
		return "Select an Angle"
	def _createExpression( self, inputValue ):
		return ecc.dennisc.alice.ast.createAngleInRevolutions( inputValue ) 

class CustomPortionFillIn( CustomFillIn ):
	def _createChooser( self ):
		return PortionChooser( self._previousValue )
	def _getMenuItemText(self):
		return "Custom Portion from 0 to 1..."
	def _getMessage( self ):
		return "Select a Portion from 0 to 1"
	def _createExpression( self, inputValue ):
		return ecc.dennisc.alice.ast.createPortion( inputValue ) 

class CustomFontFillIn( CustomFillIn ):
	def _createChooser( self ):
		return FontChooser( self._previousValue )
	def _getMenuItemText(self):
		return "Custom Font..."
	def _getMessage( self ):
		return "Select a Font"
	def _createExpression( self, inputValue ):
		return ecc.dennisc.alice.ast.createFont( inputValue ) 

class CustomColorFillIn( CustomFillIn ):
	def _createChooser( self ):
		return ColorChooser( self._previousValue )
	def _getMenuItemText(self):
		return "Custom Color..."
	def _getMessage( self ):
		return "Select a Color"
	def _createExpression( self, inputValue ):
		return ecc.dennisc.alice.ast.createColor( inputValue ) 

class CustomArrayFillIn( CustomFillIn ):
	def _createChooser( self ):
		return ArrayChooser( self._previousValue )
	def _getMenuItemText(self):
		return "Custom Array..."
	def _getMessage( self ):
		return "Create New Array Instance"
	def _createExpression( self, inputValue ):
		return inputValue 
