#print "-->", __name__

import java
import javax
import edu
import org

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import cascade
from edu.cmu.cs.dennisc import zoot

import ecc

from ecc.dennisc.alice.ide.cascade import SimpleExpressionFillIn, MethodInvocationFillIn

def _getConstants( type ):
	rv = []
	for field in type.getDeclaredFields():
		if field.isPublicAccess() and field.isStatic() and field.isFinal():
			rv.append( field )
	return rv

def _addConstantExpressionFillIns( parentNode, type ):
	for constant in _getConstants( type ):
		parentNode._addNodeChild( SimpleExpressionFillIn( alice.ast.FieldAccess( alice.ast.TypeExpression( type ), constant ) ) )

class MyFieldTile( alice.ide.editors.scene.FieldTile ):
#	def __init__(self, field):
#		alice.ide.editors.scene.FieldTile.__init__( self, field )
#		self.setBorder( javax.swing.BorderFactory.createEmptyBorder() )
#		self.setBackground(java.awt.Color.GREEN)
#		self.setOpaque( True )
	def calculateColor( self ):
		return self.getIDE().getColorForASTClass( edu.cmu.cs.dennisc.alice.ast.FieldAccess )
	def isActuallyPotentiallyActive( self ):
		return False
	def isInScope( self ):
		return True
	
class MySceneRunMethodPane( alice.ide.editors.common.StatementLikeSubstance ):
	def __init__( self, field, method ):
		alice.ide.editors.common.StatementLikeSubstance.__init__( self, alice.ast.ExpressionStatement )
		self.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) )
		self.add( MyFieldTile( field ) )
		self.add( alice.ide.editors.common.Label( method.getName() ) )
		self.setBackground( alice.ide.IDE.getColorForASTClass( alice.ast.ExpressionStatement ) )
#		self.setBackground(java.awt.Color.BLUE)
#		self.setOpaque( True )
	def isActuallyPotentiallyActive( self ):
		return False
	def isActuallyPotentiallySelectable( self ):
		return False
	def isActuallyPotentiallyDraggable( self ):
		return False
	def isKnurlDesired( self ):
		return False

def createBogusMain():
	rv = alice.ast.DoInOrder()
	rv.body.getValue().statements.add( alice.ast.MethodInvocation() )

from IDEListenerPane import IDEListenerPane
class SceneRunContainer( IDEListenerPane ):
	def projectOpened( self, e ):
		self.removeAll()
		field = self.getIDE().getSceneField()
		method = self.getIDE().getSceneType().getDeclaredMethod( "run", [] )
		self.add( MySceneRunMethodPane( field, method ) )
		self.revalidate()
		self.repaint()
	def getMaximumSize( self ):
		return self.getPreferredSize()

class RunPane( zoot.ZLineAxisPane ):
	def __init__( self, runOperation ):
		zoot.ZLineAxisPane.__init__( self )
		self.add( zoot.ZLabel( "invoke" ) )
		self.add( SceneRunContainer() )
		self.add( zoot.ZLabel( "when the program is" ) )
		strutWidth = 4
		self.add( javax.swing.Box.createHorizontalStrut( strutWidth ) )
		self.add( alice.ide.Button( runOperation ) )
		self.add( javax.swing.Box.createHorizontalGlue() )
		self._minWidth = 0
	def getPreferredSize( self ):
		rv = zoot.ZLineAxisPane.getPreferredSize( self )
		rv.width = max( rv.width, self._minWidth )
		return rv

class ToolBar( javax.swing.JPanel, java.awt.event.ComponentListener ):
	def __init__( self, runPane, ubiquitousTemplatesPane ):
		javax.swing.JPanel.__init__( self )
		self._runPane = runPane
		self._ubiquitousTemplatesPane = ubiquitousTemplatesPane
		
		self.setLayout( java.awt.GridLayout( 1, 2 ) )
		self.setLayout( java.awt.BorderLayout() )
		
		self.add( self._runPane, java.awt.BorderLayout.WEST )
		self.add( self._ubiquitousTemplatesPane, java.awt.BorderLayout.CENTER )
#		springLayout = javax.swing.SpringLayout()
#		self.setLayout( springLayout )
#				
#		import jarray
#		rows = java.util.ArrayList()
#		rows.add( jarray.array( ( self._runPane, self._ubiquitousTemplatesPane ), java.awt.Component ) )
#		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( self, rows, 0, 0 )
#		#self.add( self._runPane )
##		
##		
#		#springLayout.putConstraint( javax.swing.SpringLayout.NORTH, self._runPane, 0, javax.swing.SpringLayout.NORTH, self );
#		springLayout.putConstraint( javax.swing.SpringLayout.WEST, self._runPane, 0, javax.swing.SpringLayout.WEST, self );
#		springLayout.putConstraint( javax.swing.SpringLayout.EAST, self._runPane, 0, javax.swing.SpringLayout.WEST, self._ubiquitousTemplatesPane );
#		springLayout.putConstraint( javax.swing.SpringLayout.EAST, self._ubiquitousTemplatesPane, 0, javax.swing.SpringLayout.EAST, self );
#
##		constraints = springLayout.getConstraints( self._runPane )
##		constraints.setHeight( javax.swing.Spring.constant( 100 ) )
##		constraints = springLayout.getConstraints( self._ubiquitousTemplatesPane )
##		constraints.setHeight( javax.swing.Spring.constant( 100 ) )
##		
##		self.setMinimumSize( java.awt.Dimension( 100, 100 ) )
##		for pane in [self._runPane, self._ubiquitousTemplatesPane]:
##			springLayout.putConstraint( javax.swing.SpringLayout.NORTH, pane, 0, javax.swing.SpringLayout.NORTH, self );
##			springLayout.putConstraint( javax.swing.SpringLayout.SOUTH, pane, 0, javax.swing.SpringLayout.SOUTH, self );
#			
#		
##		self.setBackground(java.awt.Color.RED)

	def componentShown( self, e ):
		pass
	def componentHidden( self, e ):
		pass
	def componentMoved( self, e ):
		pass
	def componentResized( self, e ):
		self._runPane._minWidth = e.getSource().getWidth() + 16
		#constraints = self.getLayout().getConstraints( self._ubiquitousTemplatesPane );
		#constraints.setX( javax.swing.Spring.constant( e.getSource().getWidth() + 16 ) )
		self.revalidate()
		self.repaint()

class AbstractIDE( alice.ide.IDE ):
	def __init__( self ):
		alice.ide.IDE.__init__( self )
		
		alice.ui.BeveledShapeForType.addRoundType( org.alice.apis.moveandturn.AbstractTransformable )
		self._menuBar = self._createMenuBar()
		self.setJMenuBar( self._menuBar )

		self._scenePane = self._createScenePane()
		self._membersPane = self._createMembersPane()
		self._listenersPane = self._createListenersPane()
		self._codePane = self._createCodePane()

		self._toolBar = self._createToolBar()
		self._galleryBrowser = self._createGalleryBrowser()
		
		self._scenePane.addComponentListener( self._toolBar )
		
		self._initializeContentPane()
		
		self._fillerInners = []
		self.regiesterFillerInner( ecc.dennisc.alice.ide.cascade.fillerinners.BooleanFillerInner() )
		self.regiesterFillerInner( ecc.dennisc.alice.ide.cascade.fillerinners.NumberFillerInner() )
		self.regiesterFillerInner( ecc.dennisc.alice.ide.cascade.fillerinners.IntegerFillerInner() )
		self.regiesterFillerInner( ecc.dennisc.alice.ide.cascade.fillerinners.StringFillerInner() )
		self.regiesterFillerInner( ecc.dennisc.alice.ide.cascade.fillerinners.AngleFillerInner() )
		self.regiesterFillerInner( ecc.dennisc.alice.ide.cascade.fillerinners.PortionFillerInner() )
		self.regiesterFillerInner( ecc.dennisc.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner( ecc.dennisc.alice.ast.getType( org.alice.apis.moveandturn.TraditionalStyle ) ) )
		self.regiesterFillerInner( ecc.dennisc.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner( ecc.dennisc.alice.ast.getType( org.alice.apis.moveandturn.Color ) ) )
		#self.regiesterFillerInner( ecc.dennisc.alice.ide.cascade.ConstantsOwningFillerInner(ecc.dennisc.alice.ast.getType( edu.cmu.cs.dennisc.animation.Style )) )
		
		self._setUpMethodGenerator = self._createSceneSetUpCodeGenerator()

		applicationDirectory = self.getApplicationRootDirectory()
		classInfoDirectory = java.io.File( applicationDirectory, "classinfos" )
		alice.reflect.ClassInfoManager.setDirectory( classInfoDirectory )
	
	
	def handleWindowOpened(self, e):
		alice.ide.Launcher.hideSplashScreenIfNecessary()
	def setDragInProgress( self, isDragInProgress ):
		alice.ide.IDE.setDragInProgress( self, isDragInProgress )
		self._scenePane.setDragInProgress( isDragInProgress )
	def handleDelete( self, node ):
		self._scenePane.handleDelete( node )	
	def markChanged( self, description ):
		alice.ide.IDE.markChanged( self, description )
		self._toolBar._ubiquitousTemplatesPane.refreshVariableTemplates()

	def addToHistory( self, operation ):
		alice.ide.IDE.addToHistory( self, operation )
		self._toolBar._ubiquitousTemplatesPane.refreshVariableTemplates()
		
	def handleArgs( self, args ):
		if len( args ):
			file = java.io.File( args[ - 1 ] )
		else:
			file = java.io.File( self.getApplicationRootDirectory(), "projects/templates/GrassyProject.a3p" )
		self.loadProjectFrom( file )
		#newOperation = ecc.dennisc.alice.ide.operations.file.NewOperation()
		#self.performIfAppropriate( newOperation, java.util.EventObject( self ) )
		
		#print "todo: handleArgs:", args
		

	def _createSceneSetUpCodeGenerator( self ):
		raise "Override"

	def generateCodeForSceneSetUp( self ):
		self._scenePane.generateCodeForSceneSetUp( self._setUpMethodGenerator )
		
	def preserveProjectProperties( self ):
		self._scenePane.preserveProjectProperties()

	def getProgramType( self ):
		project = self.getProject()
		if project:
			return project.getProgramType()
		else:
			return None
	def getSceneField( self ):
		programType = self.getProgramType()
		if programType:
			return programType.fields.get( 0 )
		else:
			return None
	def getSceneType( self ):
		sceneField = self.getSceneField()
		if sceneField:
			return sceneField.getValueType()
		else:
			return None	

	def createCopy( self, original ):
		root = self.getProgramType()
		abstractDeclarations = root.createDeclarationSet()
		original.removeDeclarationsThatNeedToBeCopied( abstractDeclarations )
		map = alice.ast.Node.createMapOfDeclarationsThatShouldNotBeCopied( abstractDeclarations )
		xmlDocument = original.encode( abstractDeclarations )
		dst = alice.ast.Node.decode( xmlDocument, alice.Version.getCurrentVersionText(), map )
		if original == dst:
			return dst
		else:
			raise "copy not equivalent to original"
		
	def getCodeEditorInFocus( self ):
		return self._codePane.getCodeEditorInFocus()
	def regiesterFillerInner( self, fillerInner ):
		self._fillerInners.append( fillerInner )
	
	def _createExpressionReceptorBlank( self, type, prevExpression ):
		if prevExpression:
			self._prevExpression = prevExpression
		return ecc.dennisc.alice.ide.cascade.ExpressionReceptorBlank( type )

	def createProjectFromBootstrap(self):
		print "todo"
	
	def promptUserForMore( self, parameter, me, taskObserver ):
		class MoreMenuFillIn( cascade.MenuFillIn ):
			def __init__( self, ide, parameter ):
				self._ide = ide
				self._parameter = parameter
				title = self._parameter.getName()
				if title:
					pass
				else:
					title = "unknown parameter name"
				cascade.MenuFillIn.__init__( self, title )
			def addChildrenToBlank( self, blank ):
#				class MyFillIn(cascade.FillIn):
#					def __init__(self, ide, parameter):
#						self._ide = ide
#						self._parameter = parameter
#						print "__init__", self
#						cascade.FillIn.__init__( self )
#					def createMenuProxy(self):
#						return javax.swing.JLabel( "hello" )
#					def addChildren(self):
#						print "add", self
#						self.addChild(self._ide._createExpressionReceptorBlank(self._parameter.getValueType(), None))
#				blank.addChild(MyFillIn(self._ide, self._parameter))
				bogusBlank = self._ide._createExpressionReceptorBlank( self._parameter.getValueType(), None )
				for child in bogusBlank.getChildren():
					blank.addChild( child )
		#self.unsetPreviousExpression()
		
		class MoreBlank( cascade.Blank ):
			def __init__( self, ide, parameter ):
				self._ide = ide
				self._parameter = parameter
			def addChildren( self ):
				self.addChild( MoreMenuFillIn( self._ide, self._parameter ) )
		node = MoreBlank( self, parameter )
		#node = MoreMenuFillIn(self, parameter)
		node.showPopupMenu( me.getSource(), me.getX(), me.getY(), taskObserver )	

	def promptUserForExpression( self, type, prevExpression, me, taskObserver ):
		blank = self._createExpressionReceptorBlank( type, prevExpression )
		blank.showPopupMenu( me.getSource(), me.getX(), me.getY(), taskObserver )	

	def promptUserForStatement( self, me, taskObserver ):
		blank = ecc.dennisc.alice.ide.cascade.StatementBlank()
		blank.showPopupMenu( me.getSource(), me.getX(), me.getY(), taskObserver )

	def unsetPreviousExpression( self ):
		if self.__dict__.has_key( "_prevExpression" ):
			del self._prevExpression
	
	def addSeparatorIfNecessary( self, blank, text, isNecessary ):
		if isNecessary:
			blank.addSeparator( text )
		return False

	def addFillInAndPossiblyPartFills( self, blank, expression, type, type2 ):
		blank.addChild( SimpleExpressionFillIn( expression ) )
		if type.isAssignableTo( org.alice.apis.moveandturn.PolygonalModel ):
			if type2.isAssignableFrom( org.alice.apis.moveandturn.Model ):
				typeInJava = type.getFirstTypeEncounteredDeclaredInJava()
				clsInJava = typeInJava.getCls()
				try:
					paramType = clsInJava.Part
				except:
					paramType = None
			else:
				paramType = None
			if paramType:
				getPartMethod = typeInJava.getDeclaredMethod( "getPart", [ paramType ] )
				
				#todo
				
				blank.addChild( MethodInvocationFillIn( getPartMethod, expression ) )

	def _addExpressionBonusFillInsForType( self, blank, type ):
		codeInFocus = self.getFocusedCode()
		if codeInFocus:
			isNecessary = True
			selectedType = codeInFocus.getDeclaringType()
			if type.isAssignableFrom( selectedType ):
				isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
				#blank.addChild( SimpleExpressionFillIn( alice.ast.ThisExpression() ) )
				self.addFillInAndPossiblyPartFills( blank, alice.ast.ThisExpression(), selectedType, type )
			for field in selectedType.fields.iterator():
				fieldType = field.valueType.getValue()
				if type.isAssignableFrom( fieldType ):
					isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
					expression = alice.ast.FieldAccess( alice.ast.ThisExpression(), field )
					self.addFillInAndPossiblyPartFills( blank, expression, fieldType, type )
				if fieldType.isArray():
					fieldComponentType = fieldType.getComponentType()
					if type.isAssignableFrom( fieldComponentType ):
						isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
						expression = alice.ast.FieldAccess( alice.ast.ThisExpression(), field )
						blank.addChild( ecc.dennisc.alice.ide.cascade.ArrayAccessFillIn( fieldType, expression ) )
					if type.isAssignableFrom( alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ):
						isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
						fieldAccess = alice.ast.FieldAccess( alice.ast.ThisExpression(), field )
						arrayLength = alice.ast.ArrayLength( fieldAccess )
						blank.addChild( ecc.dennisc.alice.ide.cascade.SimpleExpressionFillIn( arrayLength ) )
					
			#acceptableParameters = []
			for parameter in codeInFocus.getParameters():
				if type.isAssignableFrom( parameter.getValueType() ):
					isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
					self.addFillInAndPossiblyPartFills( blank, alice.ast.ParameterAccess( parameter ), parameter.getValueType(), type )
			for variable in ecc.dennisc.alice.ast.getVariables( codeInFocus ):
				if type.isAssignableFrom( variable.valueType.getValue() ):
					isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
					self.addFillInAndPossiblyPartFills( blank, alice.ast.VariableAccess( variable ), variable.valueType.getValue(), type )
			for constant in ecc.dennisc.alice.ast.getConstants( codeInFocus ):
				if type.isAssignableFrom( constant.valueType.getValue() ):
					isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
					self.addFillInAndPossiblyPartFills( blank, alice.ast.ConstantAccess( constant ), constant.valueType.getValue(), type )
			if isNecessary:
				pass
			else:
				blank.addSeparator()

	def addSampleValueFillIns( self, blank, fillerInner ):
		fillerInner.addFillIns( blank )
		
	def addFillIns( self, blank, type ):
		if type is alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.AngleInRevolutions ):
			type = alice.ast.TypeDeclaredInJava.get( java.lang.Number )
			fillInType = alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.AngleInRevolutions )
		elif type is alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Portion ):
			type = alice.ast.TypeDeclaredInJava.get( java.lang.Number )
			fillInType = alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Portion )
		else:
			if type is alice.ast.TypeDeclaredInJava.get( java.lang.Object ):
				fillInType = alice.ast.TypeDeclaredInJava.get( java.lang.String )
			elif type is alice.ast.TypeDeclaredInJava.get( java.lang.Double ):
				type = alice.ast.TypeDeclaredInJava.get( java.lang.Number )
				fillInType = alice.ast.TypeDeclaredInJava.get( java.lang.Number )
			else:
				fillInType = type
		if self.__dict__.has_key( "_prevExpression" ):
			if self._prevExpression.getType().isAssignableTo( type ):
				blank.addChild( ecc.dennisc.alice.ide.cascade.PrevExpressionFillIn( self._prevExpression ) )
				blank.addSeparator()
				#self.addFillInAndPossiblyPartFills( blank, self._prevExpression, self._prevExpression.getType() )

		if type.isAssignableTo( java.lang.Enum ):
			self.addSampleValueFillIns( blank, ecc.dennisc.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner( type ) )
		else:
			for fillerInner in self._fillerInners:
				if fillerInner.isAssignableTo( fillInType ):
					self.addSampleValueFillIns( blank, fillerInner )
					break
#					if type is alice.ast.TypeDeclaredInJava.get( java.lang.Object ):
#						class MyMenuFillIn( edu.cmu.cs.dennisc.cascade.MenuFillIn ):
#							def __init__(self, fillerInner ):
#								edu.cmu.cs.dennisc.cascade.MenuFillIn.__init__( self, fillerInner._type.getName() )
#								self._fillerInner = fillerInner
#							def addChildrenToBlank(self, blank):
#								self._fillerInner.addFillIns( blank )
#						blank.addChild( MyMenuFillIn( fillerInner ) )
#					else:
#						self.addSampleValueFillIns( blank, fillerInner )
					

		self._addExpressionBonusFillInsForType( blank, type )

		if type.isArray():
			prevArray = None
			#todo
#			if self.__dict__.has_key( "_prevExpression" ):
#				if True: #todo? self._prevExpression.getType().isAssignableTo( type ):
#					prevArray = self._prevExpression
			blank.addChild( ecc.dennisc.alice.ide.cascade.CustomArrayFillIn( prevArray ) )

		if blank.getChildren().size():
			pass
		else:
			message = "sorry.  no fillins found for " + type.getName() + ". canceling."
			blank.addChild( edu.cmu.cs.dennisc.cascade.CancelFillIn( message ) )

	def _getDefaultApplicationRootDirectory( self ):
		return java.io.File( "/Program Files/Alice/" + edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText() + "/application" )

	def getApplicationRootDirectory( self ):
		if self.__dict__.has_key( "_applicationRootDirectory" ):
			pass
		else:
			testName = "projects"
			self._applicationRootDirectory = java.io.File( java.lang.System.getProperty( "user.dir" ), "application" )
			if java.io.File( self._applicationRootDirectory, testName ).exists():
				pass
			else:
				self._applicationRootDirectory = self._getDefaultApplicationRootDirectory()
				if java.io.File( self._applicationRootDirectory, testName ).exists():
					pass
				else:
					print "warning: cannot find ApplicationRootDirectory"
		return self._applicationRootDirectory

	def createExitOperation( self ):
		return ecc.dennisc.alice.ide.operations.file.ExitOperation()
	
	def _createToolBar( self ):
		self._runPane = RunPane( self.createRunOperation() )
		self._ubiquitousTemplatesPane = ecc.dennisc.alice.ide.ubiquitous.UbiquitousTemplatesPane()

#		self._runPane = javax.swing.JButton( "_runPane" )
#		self._ubiquitousTemplatesPane = javax.swing.JButton( "_ubiquitousTemplatesPane" )

		return ToolBar( self._runPane, self._ubiquitousTemplatesPane )
		
		#return javax.swing.JButton( "hello" ) 
	def _createGalleryBrowser( self ):
		return javax.swing.JLabel( "imagine a gallery browser here" )

	def _createScenePane( self ):
		#self._toolBar = self._createToolBar()
		return javax.swing.JLabel( "imagine a scene editor here" )
	def _createMembersPane( self ):
		return ecc.dennisc.alice.ide.memberspane.MembersRootPane()
	def _createListenersPane( self ):
		return None
	def _createCodePane( self ):
		return ecc.dennisc.alice.ide.codepane.CodeRootPane()

	def _initializeContentPane( self ):
		self._rootSplit = javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT )
		self._sceneMembersSplit = javax.swing.JSplitPane( javax.swing.JSplitPane.VERTICAL_SPLIT )
		self._listenersCodeSplit = javax.swing.JSplitPane( javax.swing.JSplitPane.VERTICAL_SPLIT )

		self._sceneMembersSplit.setTopComponent( self._scenePane )
		self._sceneMembersSplit.setBottomComponent( self._membersPane )

		#self._listenersCodeSplit.setTopComponent( self._listenersPane )
		#self._listenersCodeSplit.setBottomComponent( self._codePane )

		self._rootSplit.setLeftComponent( self._sceneMembersSplit )
		#self._rootSplit.setRightComponent( self._listenersCodeSplit )
		self._rootSplit.setRightComponent( self._codePane )
		
		self._leftRightDividerLocation = 400
		self._topBottomDividerLocationContracted = 300
		#self._topBottomDividerLocationExpanded = -256
		
		self._rootSplit.setDividerLocation( self._leftRightDividerLocation )
		self._sceneMembersSplit.setDividerLocation( self._topBottomDividerLocationContracted )

		self.getContentPane().add( self._toolBar, java.awt.BorderLayout.NORTH )
		self.getContentPane().add( self._rootSplit, java.awt.BorderLayout.CENTER )
	
	def _createMenuBar( self ):
		fileMenu = alice.ide.MenuUtilities.createJMenu( "File", [
			ecc.dennisc.alice.ide.operations.file.NewOperation(),
			ecc.dennisc.alice.ide.operations.file.OpenOperation(),
			ecc.dennisc.alice.ide.operations.file.SaveOperation(),
			ecc.dennisc.alice.ide.operations.file.SaveAsOperation(),
#			None,
#			ecc.dennisc.alice.ide.operations.file.RevertOperation(),
			None,
			self.getExitOperation(),
		] )
		editMenu = alice.ide.MenuUtilities.createJMenu( "Edit", [
			ecc.dennisc.alice.ide.operations.edit.UndoOperation(),
			ecc.dennisc.alice.ide.operations.edit.RedoOperation(),
			None,
			ecc.dennisc.alice.ide.operations.edit.CutOperation(),
			ecc.dennisc.alice.ide.operations.edit.CopyOperation(),
			ecc.dennisc.alice.ide.operations.edit.PasteOperation(),
		] )
		runMenu = alice.ide.MenuUtilities.createJMenu( "Run", [
			self.getRunOperation(),
		] )
		windowMenu = alice.ide.MenuUtilities.createJMenu( "Window", [
			ecc.dennisc.alice.ide.operations.window.ToggleExpandContractSceneEditorOperation(),
		] )
		windowMenu.add( 
			alice.ide.MenuUtilities.createJMenu( "Set Locale", [
				ecc.dennisc.alice.ide.operations.window.SetLocaleExceptionOperation( java.util.Locale( "en", "US" ) ),
				ecc.dennisc.alice.ide.operations.window.SetLocaleExceptionOperation( java.util.Locale( "en", "US", "complex" ) ),
				ecc.dennisc.alice.ide.operations.window.SetLocaleExceptionOperation( java.util.Locale( "en", "US", "java" ) ),
				#ecc.dennisc.alice.ide.operations.window.SetLocaleExceptionOperation( java.util.Locale( "tr" ) ),
				#ecc.dennisc.alice.ide.operations.window.SetLocaleExceptionOperation( java.util.Locale( "ar" ) ),
			] )
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																)
		windowMenu.add( 
			alice.ide.MenuUtilities.createJMenu( "Test", [
				ecc.dennisc.alice.ide.operations.window.RaiseExceptionOperation(),
			] )
																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																)
		helpMenu = alice.ide.MenuUtilities.createJMenu( "Help", [
			ecc.dennisc.alice.ide.operations.help.HelpOperation(),
			ecc.dennisc.alice.ide.operations.help.AboutOperation(),
		] )
		rv = javax.swing.JMenuBar()
		rv.add( fileMenu )
		rv.add( editMenu )
		rv.add( runMenu )
		rv.add( windowMenu )
		rv.add( helpMenu )
		return rv

	def isSceneEditorExpanded( self ):
		return self._rootSplit.getRightComponent() is None

	def toggleExpandContactOfSceneEditor( self ):
		if self.isSceneEditorExpanded():
			self._topBottomDividerLocationExpanded = self._sceneMembersSplit.getDividerLocation()
			
			#self._rootSplit.setRightComponent( self._listenersCodeSplit )
			self._rootSplit.setRightComponent( self._codePane )
			self._rootSplit.setDividerLocation( self._leftRightDividerLocation )
			print "TODO: animate scene editor contract"
			self._sceneMembersSplit.setBottomComponent( self._membersPane )
			self._sceneMembersSplit.setDividerLocation( self._topBottomDividerLocationContracted )
		else:
			self._leftRightDividerLocation = self._rootSplit.getDividerLocation()
			self._topBottomDividerLocationContracted = self._sceneMembersSplit.getDividerLocation()
	
			print "TODO: animate scene editor expand"
			self._rootSplit.setRightComponent( None )
			self._sceneMembersSplit.setBottomComponent( self._galleryBrowser )

			if self.__dict__.has_key( "_topBottomDividerLocationExpanded" ) :
				pass
			else:
				self._galleryBrowser.doLayout()
				preferredSize = self._galleryBrowser.getPreferredSize();
				self._topBottomDividerLocationExpanded = self._sceneMembersSplit.getHeight() - preferredSize.height
				self._topBottomDividerLocationExpanded -= 32
#			if self._topBottomDividerLocationExpanded < 0:
#				self._topBottomDividerLocationExpanded += self.getHeight()
			self._sceneMembersSplit.setDividerLocation( self._topBottomDividerLocationExpanded )
		self._scenePane.handleExpandContractChange( self.isSceneEditorExpanded() )

	def isClearedToProcedeWithChangedProject( self, e, operation ):
		option = javax.swing.JOptionPane.showConfirmDialog( self, "Your program has been modified.  Would you like to save it?", "Save changed project?", javax.swing.JOptionPane.YES_NO_CANCEL_OPTION )
		if option == javax.swing.JOptionPane.YES_OPTION:
			saveOperation = ecc.dennisc.alice.ide.operations.file.SaveOperation()
			result = saveOperation.prepare( e, operation )
			if result == alice.ide.Operation.PreparationResult.PERFORM:
				saveOperation.perform()
				return True
			else:
				return False
		elif option == javax.swing.JOptionPane.NO_OPTION:
			return True
		else:
			return False

	def maximize( self ):
		self.setExtendedState( self.getExtendedState() | java.awt.Frame.MAXIMIZED_BOTH )

#	def promptUserForParameterDeclaredInAlice(self, parametersProperty):
#		owner = self
#
#		model = parametersProperty.getOwner()
#		invocations = self.getInvocations(model)
#		
#		inputPane = ecc.dennisc.alice.ide.inputpanes.CreateParameterPane(parametersProperty.getValue(), invocations)
#		return inputPane.showInJDialog(owner, "Create Parameter", True)
		
	def getInvocations( self, method ):
		class Crawler( edu.cmu.cs.dennisc.pattern.Crawler ):
			def __init__( self, member ):
				self._member = member
				self._references = []
			def visit( self, visitable ):
				if isinstance( visitable, alice.ast.MethodInvocation ):
					if visitable.method.getValue() == self._member:
						self._references.append( visitable )
		programType = self.getProgramType()
		if programType:
			crawler = Crawler( method )
			programType.crawl( crawler, True )
			return crawler._references
		else:
			return []
	def getAccesses( self, field ):
		class Crawler( edu.cmu.cs.dennisc.pattern.Crawler ):
			def __init__( self, member ):
				self._member = member
				self._references = []
			def visit( self, visitable ):
				if isinstance( visitable, alice.ast.FieldAccess ):
					if visitable.field.getValue() == self._member:
						self._references.append( visitable )
		programType = self.getProgramType()
		if programType:
			crawler = Crawler( field )
			programType.crawl( crawler, True )
			return crawler._references
		else:
			return []
		
	def getReferences( self, member ):
		sceneType = self.getSceneType()

		automaticSetUpMethod = sceneType.getDeclaredMethod( "performSceneEditorGeneratedSetUp", [] )
		body = automaticSetUpMethod.body.getValue()
		body.statements.clear()

		if isinstance( member, alice.ast.MethodDeclaredInAlice ):
			return self.getInvocations( member )
		else:
			return self.getAccesses( member )

	def getAvailableTypes( self ):
		clses = [ java.lang.Double, java.lang.Integer, java.lang.Boolean, java.lang.String ]
		clses.append( org.alice.apis.moveandturn.Model )
		try:
			clses.append( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel )
			clses.append( edu.wustl.cse.lookingglass.apis.walkandtouch.Character )
			clses.append( edu.wustl.cse.lookingglass.apis.walkandtouch.Person )
		except:
			print "note: cannot find walkandtouch"
		try:
			clses.append( org.alice.apis.stage.Adult )
		except:
			print "note: cannot find stage"
		rv = []
		for cls in clses:
			rv.append( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls ) )
		
		sceneType = self.getSceneType()
		if sceneType:
			rv.append( sceneType )
			for field in sceneType.getDeclaredFields():
				valueType = field.getValueType()
				if valueType.isDeclaredInAlice():
					if valueType in rv:
						pass
					else:
						rv.append( valueType )
	
		return rv

	def getBonusTextForType( self, type ):
		#todo
		try:
			if type is edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Model ):
				return "(a.k.a. Generic Alice Model)"
			elif type is edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel ):
				return "(a.k.a. Looking Glass Scenery)"
			elif type is edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.Character ):
				return "(a.k.a. Looking Glass Character)"
			elif type is edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.Person ):
				return "(a.k.a. Looking Glass Person)"
			else:
				if type.isDeclaredInAlice():
					return "<is declared in alice>"
				else:
					return ""
		except:
			return ""

#print "<--", __name__