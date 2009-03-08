#print "-->", __name__

import java
import javax
import edu

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import moot

import ecc

class ProcedureTemplatePanePotentiallyWithAltMenu( alice.ide.editors.type.ProcedureTemplatePane ):
	def __init__( self, method ):
		alice.ide.editors.type.ProcedureTemplatePane.__init__( self, method )
		self.addMouseListener( ecc.dennisc.alice.ide.operations.ast.MethodAltTriggerMouseAdapter( method ) )
	
	def promptUserForMethodInvocation( self, instanceExpression, observer, dndEvent ):
		if self.getMember().getParameters():
			fillIn = ecc.dennisc.alice.ide.cascade.MethodInvocationFillIn( self.getMember(), instanceExpression )
			me = dndEvent.getEndingMouseEvent()
			fillIn.showPopupMenu( me.getSource(), me.getX(), me.getY(), observer )
		else:
			javax.swing.SwingUtilities.invokeLater( ecc.dennisc.lang.ApplyRunnable( observer.handleCompletion, [alice.ast.MethodInvocation( instanceExpression, self.getMember(), [] )] ) )

class SetterTemplatePanePotentiallyWithAltMenu( alice.ide.editors.type.SetterTemplatePane ):
	def __init__( self, field ):
		alice.ide.editors.type.SetterTemplatePane.__init__( self, field )
		self.addMouseListener( ecc.dennisc.alice.ide.operations.ast.FieldAltTriggerMouseAdapter( field ) )
	
	def promptUserForExpression( self, type, observer, dndEvent ):
		node = ecc.dennisc.alice.ide.cascade.ExpressionReceptorBlank( type )
		me = dndEvent.getEndingMouseEvent()
		node.showPopupMenu( me.getSource(), me.getX(), me.getY(), observer )		

class SetArrayAtIndexTemplatePanePotentiallyWithAltMenu( alice.ide.editors.type.SetArrayAtIndexTemplatePane ):
	def __init__( self, field ):
		alice.ide.editors.type.SetArrayAtIndexTemplatePane.__init__( self, field )
		self.addMouseListener( ecc.dennisc.alice.ide.operations.ast.FieldAltTriggerMouseAdapter( field ) )
	
	def promptUserForExpression( self, type, observer, dndEvent ):
		node = ecc.dennisc.alice.ide.cascade.SetArrayFieldAtIndexFillIn( self.getIDE().createInstanceExpression(), self.getField() )
		me = dndEvent.getEndingMouseEvent()
		node.showPopupMenu( me.getSource(), me.getX(), me.getY(), observer )		

class FunctionTemplatePanePotentiallyWithAltMenu( alice.ide.editors.type.FunctionTemplatePane ):
	def __init__( self, method ):
		alice.ide.editors.type.FunctionTemplatePane.__init__( self, method )
		self.addMouseListener( ecc.dennisc.alice.ide.operations.ast.MethodAltTriggerMouseAdapter( method ) )
	
	def promptUserForMethodInvocation( self, instanceExpression, observer, dndEvent ):
		if self.getMember().getParameters():
			fillIn = ecc.dennisc.alice.ide.cascade.MethodInvocationFillIn( self.getMember(), instanceExpression )
			me = dndEvent.getEndingMouseEvent()
			fillIn.showPopupMenu( me.getSource(), me.getX(), me.getY(), observer )
		else:
			javax.swing.SwingUtilities.invokeLater( ecc.dennisc.lang.ApplyRunnable( observer.handleCompletion, [alice.ast.MethodInvocation( instanceExpression, self.getMember(), [] )] ) )

class GetterTemplatePanePotentiallyWithAltMenu( alice.ide.editors.type.GetterTemplatePane ):
	def __init__( self, field ):
		alice.ide.editors.type.GetterTemplatePane.__init__( self, field )
		self.addMouseListener( ecc.dennisc.alice.ide.operations.ast.FieldAltTriggerMouseAdapter( field ) )

#class XXXXAltTriggerMouseAdapter( edu.cmu.cs.dennisc.awt.event.AltTriggerMouseAdapter ):
class ConstructorTemplatePaneWithAltMenu( alice.ide.editors.type.InstanceCreationTemplatePane, java.awt.event.MouseListener ):
	def __init__( self, constructor ):
		alice.ide.editors.type.InstanceCreationTemplatePane.__init__( self, constructor )
		self.addMouseListener( ecc.dennisc.alice.ide.operations.ast.ConstructorAltTriggerMouseAdapter( constructor ) )
	def promptUserForInstanceCreation( self, observer, dndEvent ):
		observer.handleCompletion( alice.ast.InstanceCreation( self.getConstructor(), [] ) )


def createMethodTemplate( method ):
	if method.isProcedure():
		return ProcedureTemplatePanePotentiallyWithAltMenu( method )
	else:
		return FunctionTemplatePanePotentiallyWithAltMenu( method )

def _getValueFor_CreateIfNecessary( map, key, cls ):
	if map.has_key( key ):
		pass
	else:
		map[ key ] = cls( key )
	return map[ key ]

_mapMethodToTemplate = {}
_mapConstructorToTemplate = {}
_mapFieldToArrayAccessTemplate = {}
_mapFieldToArrayLengthTemplate = {}
_mapFieldToGetterTemplate = {}
_mapFieldToSetterTemplate = {}
_mapFieldToSetArrayAtIndexTemplate = {}
def _getMethodTemplateFor( method ):
	return _getValueFor_CreateIfNecessary( _mapMethodToTemplate, method, createMethodTemplate )
def _getCostructorTemplateFor( constructor ):
	return _getValueFor_CreateIfNecessary( _mapMethodToTemplate, constructor, ConstructorTemplatePaneWithAltMenu )
def _getArrayAccessTemplateFor( field ):
	return _getValueFor_CreateIfNecessary( _mapFieldToArrayAccessTemplate, field, alice.ide.editors.type.ArrayAccessTemplatePane )
def _getArrayLengthTemplateFor( field ):
	return _getValueFor_CreateIfNecessary( _mapFieldToArrayLengthTemplate, field, alice.ide.editors.type.ArrayLengthTemplatePane )
def _getGetterTemplateFor( field ):
	return _getValueFor_CreateIfNecessary( _mapFieldToGetterTemplate, field, GetterTemplatePanePotentiallyWithAltMenu )
def _getSetterTemplateFor( field ):
	return _getValueFor_CreateIfNecessary( _mapFieldToSetterTemplate, field, SetterTemplatePanePotentiallyWithAltMenu )
def _getSetArrayAtIndexTemplateFor( field ):
	return _getValueFor_CreateIfNecessary( _mapFieldToSetArrayAtIndexTemplate, field, SetArrayAtIndexTemplatePanePotentiallyWithAltMenu )

class FieldPane( moot.ZPageAxisPane ):
	def __init__( self, field ):
		moot.ZPageAxisPane.__init__( self )
		initializeClassLine = moot.ZLineAxisPane()
		nameLabel = alice.ide.editors.common.NodeNameLabel( field )
		nameLabel.scaleFont( 2.0 )
		if field.isDeclaredInAlice():
			if field.isFinal():
				initializeClassLine.add( moot.ZLabel( "permanently set " ) )
			else:
				initializeClassLine.add( moot.ZLabel( "initialize " ) )
			initializeClassLine.add( nameLabel )
			initializeClassLine.add( alice.ide.editors.code.GetsPane( True ) )
			initializeClassLine.add( alice.ide.editors.code.ExpressionPropertyPane( field.initializer, True ) )
		else:
			initializeClassLine.add( javax.swing.Box.createVerticalStrut( 8 ) )
		self.add( initializeClassLine )
#		if field.isFinal():
#			pass
#		else:
#			customizeInstanceLine = moot.ZLineAxisPane()
#			customizeInstanceLine.add( moot.ZLabel( "value customized to... (coming soon)", [ moot.font.ZTextPosture.OBLIQUE ] ) )
#			self.add( customizeInstanceLine )
		self.add( _getGetterTemplateFor( field ) )
		if field.getValueType().isArray():
			self.add( _getArrayAccessTemplateFor( field ) )
			self.add( _getArrayLengthTemplateFor( field ) )
		if field.isFinal():
			pass
		else:
			self.add( _getSetterTemplateFor( field ) )
			if field.getValueType().isArray():
				self.add( _getSetArrayAtIndexTemplateFor( field ) )

class AbstractTypeMembersPane( javax.swing.JPanel, edu.cmu.cs.dennisc.property.event.ListPropertyListener ):
	def __init__( self, type ):
		javax.swing.JPanel.__init__( self )
		self._type = type
		if type.isDeclaredInAlice():
			for membersProperty in self._getMemberListPropertiesToListenTo():
				membersProperty.addListPropertyListener( self )
		self._boxLayout = javax.swing.BoxLayout( self, javax.swing.BoxLayout.PAGE_AXIS )
		self.setLayout( self._boxLayout )
		self.setOpaque( False )
		self._handleChange()

	def _getMemberListPropertiesToListenTo( self ):
		raise "Override"

	def _update( self ):
		raise "Override"

	def _handleChange( self ):
		self._update()
		self.revalidate()

	def adding( self, e ):
		pass
	def added( self, e ):
		self._handleChange()
	def clearing( self, e ):
		pass
	def cleared( self, e ):
		self._handleChange()
	def removing( self, e ):
		pass
	def removed( self, e ):
		self._handleChange()
	def setting( self, e ):
		pass
	def set( self, e ):
		self._handleChange()

class AbstractTypeMethodsPane( AbstractTypeMembersPane ):
	def _isAcceptable( self, method ):
		raise "Override"
	def _getGetterTemplateOrSetterTemplate( self, field ):
		raise "Override"
	def _getMemberListPropertiesToListenTo( self ):
		if self._type.isDeclaredInAlice():
			if self._type.isArray():
				return []
			else:
				return [ self._type.methods, self._type.fields ]
		else:
			return []
	def _update( self ):
		self.removeAll()
		for member in self._type.getDeclaredMethods():
			if member.isStatic():
				pass
			else:
				if member.isAbstract():
					pass
				else:
					if member.isPublicAccess() or member.isDeclaredInAlice():
						visibility = member.getVisibility()
						#todo?
						#if ( visibility == None and not java.lang.Deprecated in m.getAnnotations() ) or visibility == alice.annotations.Visibility.PRIME_TIME:
						if visibility == None or visibility == alice.annotations.Visibility.PRIME_TIME:
							m = member
							while m.getNextShorterInChain():
								m = m.getNextShorterInChain()
		
							if self._isAcceptable( m ):
								vc = _getMethodTemplateFor( m )
								if m.isDeclaredInAlice():
									panel = moot.ZLineAxisPane()
									panel.add( alice.ide.Button( ecc.dennisc.alice.ide.operations.ast.EditMethodOperation( m ) ) )
									panel.add( javax.swing.Box.createHorizontalStrut( 4 ) )
									panel.add( vc )
									panel.add( javax.swing.Box.createHorizontalGlue() )
									self.add( panel )
								else:
									self.add( vc )
#		for member in self._type.getDeclaredFields():
#			if member.isStatic():
#				pass
#			else:
#				if member.isPublicAccess() or member.isDeclaredInAlice():
#					visibility = member.getVisibility()
#					if visibility == None or visibility == alice.annotations.Visibility.PRIME_TIME:
#						template = self._getGetterTemplateOrSetterTemplate( member )
#						if template:
#							self.add( template )

class TypeProceduresPane( AbstractTypeMethodsPane ):
	def _isAcceptable( self, method ):
		return method.isProcedure()
	def _getGetterTemplateOrSetterTemplate( self, field ):
		if field.isFinal():
			return None
		else:
			return _getSetterTemplateFor( field )
class TypeFunctionsPane( AbstractTypeMethodsPane ):
	def _isAcceptable( self, method ):
		return method.isFunction()
	def _getGetterTemplateOrSetterTemplate( self, field ):
		return _getGetterTemplateFor( field )

class TypeFieldsPane( AbstractTypeMethodsPane ):
	def _getMemberListPropertiesToListenTo( self ):
		if self._type.isDeclaredInAlice():
			return [ self._type.fields ]
		else:
			return []
	def _update( self ):
		self.removeAll()
		for member in self._type.getDeclaredFields():
			if member.isStatic():
				pass
			else:
				if member.isPublicAccess() or member.isDeclaredInAlice():
					visibility = member.getVisibility()
					if visibility == None or visibility == alice.annotations.Visibility.PRIME_TIME:
						self.add( FieldPane( member ) )

_mapTypeToProceduresPane = {}
_mapTypeToFunctionsPane = {}
_mapTypeToFieldsPane = {}
def _getTypeProceduresPaneFor( type ):
	return _getValueFor_CreateIfNecessary( _mapTypeToProceduresPane, type, TypeProceduresPane )
def _getTypeFunctionsPaneFor( type ):
	return _getValueFor_CreateIfNecessary( _mapTypeToFunctionsPane, type, TypeFunctionsPane )
def _getTypeFieldsPaneFor( type ):
	return _getValueFor_CreateIfNecessary( _mapTypeToFieldsPane, type, TypeFieldsPane )


class AbstractCreateMethodOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self, type ):
		self._type = type

	def _createInputPane( self ):
		raise "Override"
	def _getDialogTitle( self ):
		raise "Override"
	
	def prepare( self, e, observer ):
		inputPane = self._createInputPane()
		self._method = inputPane.showInJDialog( title=self._getDialogTitle() )
		if self._method:
			return alice.ide.Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY
		else:
			return alice.ide.Operation.PreparationResult.CANCEL

	def perform( self ):
		self.redo()
		#todo?
		alice.ide.IDE.getSingleton().setFocusedCode( self._method )
	def undo( self ):
		self._type.methods.remove( [self._method] )
	def redo( self ):
		self._type.methods.add( [self._method] )


class CreateProcedureOperation( AbstractCreateMethodOperation ):
	def __init__( self, type ):
		AbstractCreateMethodOperation.__init__( self, type )
		self.putValue( javax.swing.Action.NAME, "create new procedure..." )
	def _createInputPane( self ):
		return ecc.dennisc.alice.ide.inputpanes.CreateProcedurePane( self._type )
	def _getDialogTitle( self ):
		return "Create Procedure"

class CreateFunctionOperation( AbstractCreateMethodOperation ):
	def __init__( self, type ):
		AbstractCreateMethodOperation.__init__( self, type )
		self.putValue( javax.swing.Action.NAME, "create new function..." )
	def _createInputPane( self ):
		return ecc.dennisc.alice.ide.inputpanes.CreateFunctionPane( self._type )
	def _getDialogTitle( self ):
		return "Create Function"

class CreateFieldOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self, type ):
		self._type = type
		self.putValue( javax.swing.Action.NAME, "create new property..." )
	def prepare( self, e, observer ):
		inputPane = ecc.dennisc.alice.ide.inputpanes.CreateFieldPane( self._type )
		self._field = inputPane.showInJDialog( title="Create Property" )
		if self._field:
			return alice.ide.Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY
		else:
			return alice.ide.Operation.PreparationResult.CANCEL
	def perform( self ):
		self.redo()
	def undo( self ):
		self._type.fields.remove( [self._field] )
	def redo( self ):
		self._type.fields.add( [self._field] )

class CreateProcedureButton( alice.ide.Button ):
	def __init__( self, type ):
		alice.ide.Button.__init__( self, CreateProcedureOperation( type ) )
class CreateFuntionButton( alice.ide.Button ):
	def __init__( self, type ):
		alice.ide.Button.__init__( self, CreateFunctionOperation( type ) )
class CreateFieldButton( alice.ide.Button ):
	def __init__( self, type ):
		alice.ide.Button.__init__( self, CreateFieldOperation( type ) )

_mapTypeToCreateProcedureButton = {}
_mapTypeToCreateFuntionButton = {}
_mapTypeToCreateFieldButton = {}
def _getCreateProcedureButtonFor( type ):
	return _getValueFor_CreateIfNecessary( _mapTypeToCreateProcedureButton, type, CreateProcedureButton )
def _getCreateFunctionButtonFor( type ):
	return _getValueFor_CreateIfNecessary( _mapTypeToCreateFuntionButton, type, CreateFuntionButton )
def _getCreateFieldButtonFor( type ):
	return _getValueFor_CreateIfNecessary( _mapTypeToCreateFieldButton, type, CreateFieldButton )

class MembersTabComponent( javax.swing.JPanel ):
	def __init__( self ):
		javax.swing.JPanel.__init__( self )
		self.setLayout( java.awt.GridBagLayout() )
		i = 4
		self.setBorder( javax.swing.BorderFactory.createEmptyBorder( i, i, i, i ) )

	def _getConstructorComponents( self, type ):
		raise "Override"

	def _getTypeMembersPane( self, type ):
		raise "Override"

	def _getCreateMemberButton( self, type ):
		raise "Override"
	
	def _update( self ):
		self.removeAll()
		
		currentlySelectedField = alice.ide.IDE.getSingleton().getFieldSelection()
		currentlyBeingEditedCode = alice.ide.IDE.getSingleton().getFocusedCode()
		if currentlySelectedField and currentlyBeingEditedCode:
			gbc = java.awt.GridBagConstraints()
			gbc.anchor = java.awt.GridBagConstraints.NORTHWEST
			gbc.fill = java.awt.GridBagConstraints.BOTH
			gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER
			if currentlySelectedField.getValueType().isArray():
				label = moot.ZLabel( "<html><b>todo:</b><br><i>handle selection of a field whose type is an array type</i></html>" )
				label.setFontToScaledFont( 1.2 )
				self.add( label, gbc )
			else:
				gbc.insets.bottom = 2
				for type in _getTypes( currentlySelectedField ):
					linePane = moot.ZLineAxisPane()
					linePane.add( alice.ide.editors.common.TypePane( type ) )
					linePane.add( javax.swing.Box.createHorizontalGlue() )
					gbc.insets.left = 2
					self.add( linePane, gbc )
					gbc.insets.left = 24
					#classLabel = javax.swing.JLabel( "class " + type.getName() )
					#classLabel.setFont( java.awt.Font( None, java.awt.Font.ITALIC, 12 ) )
					#self.add( classLabel, gbc )
					if type.isDeclaredInAlice():
						editConstructorButton, classInstanceCreationTemplate = self._getConstructorComponents( type )
						if editConstructorButton:
							panel = javax.swing.JPanel()
							panel.setOpaque( False )
							panel.setLayout( javax.swing.BoxLayout( panel, javax.swing.BoxLayout.LINE_AXIS ) )
							panel.add( editConstructorButton )
							#panel.add( classInstanceCreationTemplate )
							panel.add( javax.swing.Box.createHorizontalGlue() )
							self.add( panel, gbc )
	#						gbc.fill = java.awt.GridBagConstraints.NONE
	#						self.add( editConstructorButton, gbc )
	#						self.add( classInstanceCreationTemplate, gbc )
	#						gbc.fill = java.awt.GridBagConstraints.BOTH
					self.add( self._getTypeMembersPane( type ), gbc )
					if type.isDeclaredInAlice():
						#linePane.add( javax.swing.Box.createHorizontalStrut( 32 ) )
						linePane = moot.ZLineAxisPane()
						linePane.add( self._getCreateMemberButton( type ) )
						linePane.add( javax.swing.Box.createHorizontalGlue() )
						self.add( linePane, gbc )
						self.add( javax.swing.Box.createVerticalStrut( 24 ), gbc )
				
			gbc.weightx = 1.0
			gbc.weighty = 1.0
			self.add( javax.swing.JLabel(), gbc )
		self.revalidate()

def _getTypes( field ):
	rv = []
	type = field.valueType.getValue()
	while type:
		rv.append( type )
		if type.isFollowToSuperClassDesired():
			type = type.getSuperType()
		else:
			break
	return rv

class AbstractMethodsTabComponent( MembersTabComponent ):
	pass

class ProceduresTabComponent( AbstractMethodsTabComponent ):
	def _getConstructorComponents( self, type ):
		return None, None
	def _getTypeMembersPane( self, type ):
		return _getTypeProceduresPaneFor( type )
	def _getCreateMemberButton( self, type ):
		return _getCreateProcedureButtonFor( type )

class FunctionsTabComponent( AbstractMethodsTabComponent ):
	def _getConstructorComponents( self, type ):
		constructor = type.getDeclaredConstructor( [] )
		return alice.ide.Button( ecc.dennisc.alice.ide.operations.ast.EditConstructorOperation( constructor ) ), _getCostructorTemplateFor( constructor )
	def _getTypeMembersPane( self, type ):
		return _getTypeFunctionsPaneFor( type )
	def _getCreateMemberButton( self, type ):
		return _getCreateFunctionButtonFor( type )

class FieldsTabComponent( MembersTabComponent ):
	def _getConstructorComponents( self, type ):
		return None, None
	def _getTypeMembersPane( self, type ):
		return _getTypeFieldsPaneFor( type )
	def _getCreateMemberButton( self, type ):
		return _getCreateFieldButtonFor( type )

class MembersTab( edu.cmu.cs.dennisc.swing.Tab ):
	def __init__( self, titleText, component, color ):
		title = javax.swing.JLabel( titleText )
		
		self._component = component
		self._component.setBackground( color )

		scrollPane = javax.swing.JScrollPane( self._component )
		scrollPane.setBackground( color )
		scrollPane.setBorder( None )
		scrollPane.getVerticalScrollBar().setUnitIncrement( 12 )
		
		edu.cmu.cs.dennisc.swing.Tab.__init__( self, title, scrollPane )
		self.setBackground( color )
				
	def isClosable( self ):
		return False

	def _update( self ):
		self._component._update()

class AbstractMethodsTab( MembersTab ):
	pass

class ProceduresTab( AbstractMethodsTab ):
	def __init__( self ):
		AbstractMethodsTab.__init__( self, "Procedures", ProceduresTabComponent(), alice.ide.IDE.getSingleton().getProcedureColor() )
		self.setToolTipText( "methods that do not return a value" )

class FunctionsTab( AbstractMethodsTab ):
	def __init__( self ):
		AbstractMethodsTab.__init__( self, "Functions", FunctionsTabComponent(), alice.ide.IDE.getSingleton().getFunctionColor() )
		self.setToolTipText( "methods that return a value" )

class FieldsTab( MembersTab ):
	def __init__( self ):
		MembersTab.__init__( self, "Properties", FieldsTabComponent(), alice.ide.IDE.getSingleton().getFieldColor() )
#print "<--", __name__
