#print "-->", __name__

import java
import javax
import edu

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import zoot

import ecc

class AddParameterOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self, code ):
		self._code = code
		self.putValue( javax.swing.Action.NAME, "add parameter..." )

	def prepare( self, e, observer ):
		ide = self.getIDE()
		invocations = ide.getInvocations( self._code )
		inputPane = ecc.dennisc.alice.ide.inputpanes.CreateParameterPane( self._code, invocations )
		self._param = inputPane.showInJDialog( ide, "Create Parameter", True )
		if self._param:
			return alice.ide.Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY
		else:
			return alice.ide.Operation.PreparationResult.CANCEL

	def perform( self ):
		self._index = self._code.parameters.size()
		self._invocations = self.getIDE().getInvocations( self._code )
		self.redo()
	def undo( self ):
		self._code.parameters.remove( self._index )
		for invocation in self._invocations:
			invocation.arguments.remove( self._index )
	def redo( self ):
		self._code.parameters.add( [ self._param ] )
		for invocation in self._invocations:
			argument = alice.ast.Argument( self._param, alice.ast.NullLiteral() )
			invocation.arguments.add( [ argument ] )
			

class DeleteParameterOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self, paramteter, code ):
		self._paramteter = paramteter
		self._code = code
		self.putValue( javax.swing.Action.NAME, "delete" )

	def prepare( self, e, observer ):
		invocations = self.getIDE().getInvocations( self._code )
		if len( invocations ):
			if self._code.isProcedure():
				methodText = "procedure"
			else:
				methodText = "function"
			message = "There are invocations to this %s.\nDeleting this parameter will also delete the arguments from those invocations.\nWould you like to continue with the deletion?" % methodText
			result = javax.swing.JOptionPane.showConfirmDialog(self.getIDE(), message, "Delete Parameter", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION )
			if result == javax.swing.JOptionPane.YES_OPTION:
				pass
			else:
				return alice.ide.Operation.PreparationResult.CANCEL
		return alice.ide.Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY

	def perform( self ):
		self._index = self._code.parameters.indexOf( self._paramteter )
		invocations = self.getIDE().getInvocations( self._code )
		self._map = {}
		for invocation in invocations:
			self._map[ invocation ] = invocation.arguments.get( self._index )
		self.redo()
	def redo( self ):
		self._code.parameters.remove( self._index )
		for invocation in self._map.keys():
			invocation.arguments.remove( self._index )
	def undo( self ):
		self._code.parameters.add( self._index, [ self._paramteter ] )
		for invocation, argument in self._map.items():
			invocation.arguments.add( self._index, [ argument ] )

class AbstractShiftParameterOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self, paramteter, code ):
		self._paramteter = paramteter
		self._code = code
	def prepare( self, e, observer ):
		return alice.ide.Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY
	def perform( self ):
		self._index = self._code.parameters.indexOf( self._paramteter )
		self._invocations = self.getIDE().getInvocations( self._code )
		self.redo()

	def _shift( self, aIndex ):
		bIndex = aIndex + 1
		aParam = self._code.parameters.get( aIndex )
		bParam = self._code.parameters.get( bIndex )
		self._code.parameters.set( aIndex, [bParam, aParam] )
		for invocation in self._invocations:
			aArg = invocation.arguments.get( aIndex )
			bArg = invocation.arguments.get( bIndex )
			assert aArg.parameter.getValue() == aParam 
			assert bArg.parameter.getValue() == bParam 
			invocation.arguments.set( aIndex, [bArg, aArg] )

class ShiftForwardParameterOperation( AbstractShiftParameterOperation ):
	def __init__( self, paramteter, code ):
		AbstractShiftParameterOperation.__init__(self, paramteter, code)
		self.putValue( javax.swing.Action.NAME, "shift forward" )
		
	def redo( self ):
		self._shift( self._index-1 )
	def undo( self ):
		self._shift( self._index-1 )

class ShiftBackwardParameterOperation( AbstractShiftParameterOperation ):
	def __init__( self, paramteter, code ):
		AbstractShiftParameterOperation.__init__(self, paramteter, code)
		self.putValue( javax.swing.Action.NAME, "shift backward" )
	def redo( self ):
		self._shift( self._index )
	def undo( self ):
		self._shift( self._index )

#class ParameterAltMenuMouseAdapter( java.awt.event.MouseListener ):
#	def __init__( self, parameter, code ):
#		self._parameter = parameter
#		self._code = code
#	def mouseEntered( self, e ):
#		pass
#	def mouseExited( self, e ):
#		pass
#	def mousePressed( self, e ):
#		pass
#	def mouseReleased( self, e ):
#		if javax.swing.SwingUtilities.isRightMouseButton( e ):
#			N = self._code.parameters.size()
#			index = self._code.parameters.indexOf( self._parameter )
#			operations = []
#			operations.append( ecc.dennisc.alice.ide.operations.ast.RenameParameterOperation( self._parameter, self._code ) )
#			if index > 0:
#				operations.append( ShiftForwardParameterOperation( self._parameter, self._code ) )
#			if index < N-1:
#				operations.append( ShiftBackwardParameterOperation( self._parameter, self._code ) )
#			operations.append( DeleteParameterOperation( self._parameter, self._code ) )
#			popup = alice.ide.MenuUtilities.createJPopupMenu( operations )
#			popup.show( e.getSource(), e.getX(), e.getY() )
#	def mouseClicked( self, e ):
#		pass

class ParameterAltTriggerMouseAdapter( edu.cmu.cs.dennisc.awt.event.AltTriggerMouseAdapter ):
	def __init__( self, parameter, code ):
		self._parameter = parameter
		self._code = code
	def altTriggered(self, e ):
		N = self._code.parameters.size()
		index = self._code.parameters.indexOf( self._parameter )
		operations = []
		operations.append( ecc.dennisc.alice.ide.operations.ast.RenameParameterOperation( self._parameter, self._code ) )
		if index > 0:
			operations.append( ShiftForwardParameterOperation( self._parameter, self._code ) )
		if index < N-1:
			operations.append( ShiftBackwardParameterOperation( self._parameter, self._code ) )
		operations.append( DeleteParameterOperation( self._parameter, self._code ) )
		popup = alice.ide.MenuUtilities.createJPopupMenu( operations )
		popup.show( e.getSource(), e.getX(), e.getY() )

#class ParameterPane( zoot.ZLineAxisPane ):
#	def __init__(self, parameter, code):
#		zoot.ZLineAxisPane.__init__( self )
#		self.add( alice.ide.editors.common.NodeNameLabel( parameter ) )
#		self.add( alice.ide.editors.common.Label( ": " ) )
#		self.add( alice.ide.editors.code.EmptyExpressionPane( parameter.getValueType() ) )
#		#self.setBackground( java.awt.Color.LIGHT_GRAY )
#		self.setOpaque( True )
#		#self.setBorder( javax.swing.BorderFactory.createBevelBorder( javax.swing.border.BevelBorder.RAISED ) )
#		#self.setBorder( javax.swing.BorderFactory.createEtchedBorder() )
#		self.setBorder( javax.swing.BorderFactory.createLineBorder( java.awt.Color.LIGHT_GRAY ) )

class ParameterPane( alice.ide.editors.code.ParameterPane ):
	def __init__(self, parameter, code):
		alice.ide.editors.code.ParameterPane.__init__( self, parameter )
		self.addMouseListener( ParameterAltTriggerMouseAdapter( parameter, code ) )

class ParametersPane( alice.ide.editors.code.AbstractListPropertyPane ):
	def __init__(self, code ):
		self._code = code
		alice.ide.editors.code.AbstractListPropertyPane.__init__( self, javax.swing.BoxLayout.LINE_AXIS, code.parameters )
	def addPrefixComponents(self):
		if "java" == edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton().getLocale().getVariant():
			self.add( zoot.ZLabel( "( " ) )
		else:
			n = self.getProperty().size()
			if n == 0:
				#text = " with no parameters"
				text = " "
			elif n == 1:
				text = " with parameter: "
			else:
				text = " with parameters: "
			self.add( alice.ide.editors.common.Label( text ) )
	def createInterstitial(self, i, N ):
		if i<N-1:
			return zoot.ZLabel( ", " )
		else:
			return None
	def createComponent(self, parameter):
		pane = zoot.ZLineAxisPane()
		pane.add( edu.cmu.cs.dennisc.alice.ide.editors.common.TypePane( parameter.getValueType() ) )
		#pane.add( javax.swing.Box.createHorizontalStrut( 4 ) )
		pane.add( ParameterPane( parameter, self._code ) )
		return pane
	def addPostfixComponents(self):
		if isinstance( self._code, alice.ast.ConstructorDeclaredInAlice ):
			pass
		else:
			if self._code.isSignatureLocked.getValue():
				pass
			else:
				self.add( alice.ide.Button( AddParameterOperation( self._code ) ) )
		if "java" == edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton().getLocale().getVariant():
			self.add( zoot.ZLabel( " )" ) )

class CodePane( alice.ide.editors.code.CodeEditor ):
	def createParametersPane(self, code):
		return ParametersPane( code )

#print "<--", __name__
