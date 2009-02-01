import java
import javax
import edu 

import ecc

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import zoot

def getSceneEditor():
	return alice.ide.IDE.getSingleton()._scenePane

class FileView( javax.swing.JLabel ):
	def __init__( self, file ):
		javax.swing.JLabel.__init__( self )
		self._file = file
		self.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER )
		self.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM )
		self.setText( "Create an instance of class " + edu.cmu.cs.dennisc.io.FileUtilities.getBaseName( self._file ) + "?" )
		self.setIcon( javax.swing.ImageIcon( self._file.getAbsolutePath() ) )
	def getClassName( self, imageRoot ):
		prefix = imageRoot.getAbsolutePath()
		path = self._file.getAbsolutePath()
		path = path[ len( prefix ) + 1 : - 4]
		path = path.replace( "\\", "/" )
		return path.replace( "/", "." )


def _getProgramType():
	return alice.ide.IDE.getSingleton().getProject().getProgramType()

def _buildTypeSet( set, type ):
	if type.isDeclaredInAlice():
		if set.contains( type ):
			pass
		else:
			set.add( type )
			for field in type.getDeclaredFields():
				_buildTypeSet( set, field.getValueType() )
			#TODO
			#for method in type.getDeclaredMethods():
			#	_buildTypeSet( set, method.getReturnType() )
			#	for parameter in method.getParameters():
			#		_buildTypeSet( set, parameter.getValueType() )

def _getReferencedTypesDeclaredInAlice():
	programType = _getProgramType()
	rv = java.util.HashSet()
	_buildTypeSet( rv, programType )
	return rv

def _getUniqueTypeName( typeDeclaredInJava, referencedTypes ):
	baseName = "My" + typeDeclaredInJava.getName()
	i = 1
	while True:
		rv = baseName
		if i > 1:
			rv += str( i )
		isUsed = False
		for referencedType in referencedTypes:
			if referencedType.getName() == rv:
				isUsed = True
				break
		if isUsed:
			i += 1
		else:
			break
	return rv

def _getTypeForTypeDeclaredInJava( typeDeclaredInJava ):
	referencedTypes = _getReferencedTypesDeclaredInAlice()
	
	rv = None
	for referencedType in referencedTypes:
		if referencedType.getFirstTypeEncounteredDeclaredInJava() is typeDeclaredInJava:
			rv = ( referencedType, True )
			break
		
	if rv:
		pass
	else:
		name = _getUniqueTypeName( typeDeclaredInJava, referencedTypes )
		constructor = alice.ast.ConstructorDeclaredInAlice( [], alice.ast.BlockStatement( [] ) )
		
		constructors = [ constructor ]
		methods = []
		fields = []
	
		rv = ( alice.ast.TypeDeclaredInAlice( name, None, typeDeclaredInJava, constructors, methods, fields ), False )
	
	return rv

from ecc.dennisc.alice.ide.IdentifierPane import IdentifierPane

class CreateInstancePane( edu.cmu.cs.dennisc.swing.InputPane ):
	def __init__( self, file, thumbnailsRoot, type ):
		edu.cmu.cs.dennisc.swing.InputPane.__init__( self )
		self._file = file
		self._thumbnailsRoot = thumbnailsRoot
		
		if type:
			superType = type.getFirstTypeEncounteredDeclaredInJava()
		else:
			superClsName = self._getSuperClassName()
			#superClsName = "org.alice.apis.moveandturn.gallery.sports.BowlingPin"
			cls = java.lang.Class.forName( superClsName )
			superType = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( cls )
		
		
		_type, isAlreadyReferenced = _getTypeForTypeDeclaredInJava( superType )
		if isAlreadyReferenced:
			self._type = _type
		else:
			if type:
				self._type = type
			else:
				self._type = _type

		self._label = javax.swing.JLabel()
		self._label.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER )
		self._label.setVerticalTextPosition( javax.swing.SwingConstants.TOP )
		
		if isAlreadyReferenced:
			if type:
				isAlreadyReferencedText = "Create a new instance of already existing (ignoring previously saved file) class "
			else:
				isAlreadyReferencedText = "Create a new instance of already existing class "
		else:
			if type:
				isAlreadyReferencedText = "Create a new instance of (from previously saved file) class "
			else:
				isAlreadyReferencedText = "Create a new instance of (brand-new) class "
				
		
		print self._file
		#self._label.setText( "Create an instance of class " + self._getClassName() + "?" )
		self._label.setText( isAlreadyReferencedText + self._type.getName() + "?" )
		self._label.setIcon( javax.swing.ImageIcon( self._file.getAbsolutePath() ) )
		
		self._identifierPane = IdentifierPane( None, getSceneEditor().getSceneType().fields )
		self._identifierPane._textVC.setText( self._getAvailableFieldName() )
		self._identifierPane._textVC.selectAll()
		
		self._identifierPane.setInputPane( self )
		self.addOKButtonValidator( self._identifierPane )
#		self._nameVC = ecc.dennisc.swing.InputTextFieldWithValidationCallback( ecc.dennisc.swing.event.ChangeAdapter( self._handleNameChange ), self._isValidName )
#		self._nameVC.setText( self._getAvailableFieldName() )
#		self._nameVC.selectAll()

#		pane = javax.swing.JPanel()
#		pane.setLayout( java.awt.BorderLayout() )
#		pane.add( javax.swing.JLabel( "name:" ), java.awt.BorderLayout.WEST )
#		pane.add( self._nameVC, java.awt.BorderLayout.CENTER )
		
		self.setLayout( java.awt.BorderLayout() )
		self.add( self._label, java.awt.BorderLayout.CENTER )
		self.add( self._identifierPane, java.awt.BorderLayout.SOUTH )
		
		
	def _getAvailableFieldName( self ):
		return getSceneEditor()._getAvailableFieldName( self._getSuperClassBaseName() )
	
	def _getSuperClassName( self ):
		prefix = self._thumbnailsRoot.getAbsolutePath()
		path = self._file.getAbsolutePath()
		path = path[ len( prefix ) + 1 : - 4]
		path = path.replace( "\\", "/" )
		return path.replace( "/", "." )
	def _getSuperClassBaseName( self ):
		return edu.cmu.cs.dennisc.io.FileUtilities.getBaseName( self._file )
#	def _getClassName( self ):
#		if self._clsName:
#			return self._clsName
#		else:
#			return "My" + self._getSuperClassBaseName()
	
	def getActualInputValue( self ):
		rv = getSceneEditor().createInstance( self._type )
		#rv = getSceneEditor().createInstance( self._getClassName(), self._getSuperClassName() )
		ecc.dennisc.alice.vm.getInstanceInJava( rv ).setName( self._identifierPane.getText() )
		return rv

class CreateInstanceOfTypeButton( javax.swing.JButton ):
	def __init__( self, text, callback, directory ):
		javax.swing.JButton.__init__( self, text )
		self.addActionListener( ecc.dennisc.swing.event.ActionAdapter( callback, directory ) )
	
def _getTextbookTypesDirectory():
	rv = java.io.File( alice.ide.IDE.getSingleton().getApplicationRootDirectory(), "classes/textbook" )
	rv.mkdirs()
	return rv

from CreateTextButton import CreateTextButton

class GalleryBrowser( alice.ide.gallerypane.GalleryPane ):
	def __init__( self, thumbnailsRoot, subRoot, map ):
		self._thumbnailsRoot = thumbnailsRoot
		self._thumbnailsSubRoot = java.io.File( thumbnailsRoot, subRoot )
		self._map = map
		alice.ide.gallerypane.GalleryPane.__init__( self, self._thumbnailsSubRoot )

		westComponent = self.createWestComponent()
		if westComponent:
			self.add( westComponent, java.awt.BorderLayout.WEST )
		eastComponent = self.createEastComponent()
		if eastComponent:
			self.add( eastComponent, java.awt.BorderLayout.EAST )

	def createWestComponent( self ):
		return None

	def createEastComponent( self ):
		pane = zoot.ZPane()
		pane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 16, 0, 0 ) )
		pane.setLayout( java.awt.BorderLayout() )

		fromFilePane = zoot.ZPane()
		fromFilePane.setLayout( java.awt.GridLayout( 2, 1, 0, 4 ) )
		fromFilePane.add( CreateInstanceOfTypeButton( "My Classes...", self._handleCreateInstanceOfTypeButton, edu.cmu.cs.dennisc.alice.io.FileUtilities.getMyTypesDirectory() ) )
		fromFilePane.add( CreateInstanceOfTypeButton( "Textbook Classes...", self._handleCreateInstanceOfTypeButton, _getTextbookTypesDirectory() ) )

		pane.add( fromFilePane, java.awt.BorderLayout.NORTH )
		pane.add( CreateTextButton(), java.awt.BorderLayout.SOUTH )
		return pane

	def getAdornedTextFor( self, name, isDirectory, isRequestedByPath ):
		if self._map.has_key( name ):
			name = self._map[ name ]
		return alice.ide.gallerypane.GalleryPane.getAdornedTextFor( self, name, isDirectory, isRequestedByPath )
	
	def _handleCreateInstanceOfTypeButton( self, e, directory ):
		owner = alice.ide.IDE.getSingleton()
		file = ecc.dennisc.swing.showFileOpenDialog( owner, directory, edu.cmu.cs.dennisc.alice.io.FileUtilities.TYPE_EXTENSION )
		if file:
			type = edu.cmu.cs.dennisc.alice.io.FileUtilities.readType( file )
			
			superType = type.getFirstTypeEncounteredDeclaredInJava()
			
			cls = superType.m_cls
			clsFullName = cls.__name__
			
			prefixList = []
			prefixList.append( "org.alice.apis.moveandturn.gallery" )
			prefixList.append( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters" )
			prefixList.append( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes" )
			for prefix in prefixList:
				if clsFullName.startswith( prefix ):
					pre = prefix
					post = clsFullName[len( prefix ):]
					break
			
			
			path = pre + post.replace( ".", "/" ) + ".png"
			
			file = java.io.File( self._thumbnailsRoot, path )

			inputPane = CreateInstancePane( file, self._thumbnailsRoot, type )
			owner = alice.ide.IDE.getSingleton()
			instance = inputPane.showInJDialog( owner, "Create Instance", True )
			if instance:
				getSceneEditor().addInstance( instance )

	def handleFileActivation( self, file ):
		self._handleFileSelection( file )
	#todo
	def _handleFileSelection( self, file ):
		if file:
			if file.isDirectory():
				self._directoryFilesVC.setDirectory( file )
				self._ancestorsVC.setDirectory( file )
				self.revalidate()
			else:
				inputPane = CreateInstancePane( file, self._thumbnailsRoot, None )
				owner = alice.ide.IDE.getSingleton()
				instance = inputPane.showInJDialog( owner, "Create Instance", True )
				if instance:
					getSceneEditor().addInstance( instance )
