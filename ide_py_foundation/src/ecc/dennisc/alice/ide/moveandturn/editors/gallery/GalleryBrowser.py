import java
import javax
import edu 

import ecc

from edu.cmu.cs.dennisc import alice

def getSceneEditor():
	return alice.ide.IDE.getSingleton()._scenePane

def _getFilteredFileName( file, galleryBrowser ):
	rv = file.getName()
	if galleryBrowser._map.has_key( rv ):
		rv = galleryBrowser._map[ rv ]
	return rv

class FileListCellRenderer( ecc.dennisc.swing.AbstractIconListCellRenderer ):
	def __init__( self, galleryBrowser ):
		ecc.dennisc.swing.AbstractIconListCellRenderer.__init__( self, 8 )
		self._galleryBrowser = galleryBrowser
		self._image = None
		
	def _getTextForLabelAndIcon( self, label, list, value, index, isSelected, isFocus ):
		textForLabel = _getFilteredFileName( value, self._galleryBrowser )
		if textForLabel.endswith( ".png" ):
			textForLabel = textForLabel[:-4]
		if value.isDirectory():
			directoryThumbnailFile = java.io.File( value, "directoryThumbnail.png" )
			if directoryThumbnailFile.exists():
				imageBackground = javax.swing.ImageIcon( self._galleryBrowser.getDefaultFolderPath() ).getImage()
				if self._image:
					pass
				else:
					self._image = java.awt.image.BufferedImage( imageBackground.getWidth(), imageBackground.getHeight(), java.awt.image.BufferedImage.TYPE_4BYTE_ABGR )
				imageForeground = javax.swing.ImageIcon( directoryThumbnailFile.getAbsolutePath() ).getImage()
				g = self._image.getGraphics()
				
				composite = g.getComposite()
				g.setComposite( java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.CLEAR, 0.0 ) );
				g.fillRect( 0, 0, self._image.getWidth(), self._image.getHeight() )
				g.setComposite( composite )
				g.drawImage( imageBackground, 0, 0, None )
				g.drawImage( imageForeground, 0, 0, None )
				g.dispose()
				icon = javax.swing.ImageIcon( self._image )
			else:
				icon = javax.swing.ImageIcon( self._galleryBrowser.getDefaultFolderPath() )
		else:
			icon = javax.swing.ImageIcon( value.getAbsolutePath() )
		label.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER )
		label.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM )
		return textForLabel, icon

class ThumbnailFilter( java.io.FilenameFilter ):
	def accept( self, directory, filename ):
		return filename != "directoryThumbnail.png"

class DirectoryFilesListVC( ecc.dennisc.swing.ListVC ):
	def __init__( self, galleryBrowser, listSelectionListener=None ):
		ecc.dennisc.swing.ListVC.__init__( self, listSelectionListener )
		self._directory = None
		self.setCellRenderer( FileListCellRenderer( galleryBrowser ) )
		self.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP )
		self.setVisibleRowCount( 1 )

	def _update( self ):
		files = []
		if self._directory:
			if self._directory.exists():
				if self._directory.isDirectory():
					files = self._directory.listFiles( ThumbnailFilter() )
		self.setListData( files )

	def setDirectory( self, directory ):
		if self._directory == directory:
			pass
		else:
			self._directory = directory
			self._update()


class AncestorButton( javax.swing.JButton, java.awt.event.ActionListener ):
	def __init__( self, file, galleryBrowser ):
		javax.swing.JButton.__init__( self )
		self._file = file
		self._galleryBrowser = galleryBrowser
		self.setFont( java.awt.Font( None, 0, 24 ) )
		self.setText( "<html><u>" + _getFilteredFileName( file, self._galleryBrowser ) + "</u></html>" )
		self.addActionListener( self )
	def actionPerformed( self, e ):
		self._galleryBrowser._handleFileSelection( self._file )

class CurrentLabel( javax.swing.JLabel ):
	def __init__( self, file, galleryBrowser ):
		javax.swing.JLabel.__init__( self )
		self._file = file
		self.setFont( java.awt.Font( None, java.awt.Font.ITALIC, 32 ) )
		self.setText( _getFilteredFileName( file, galleryBrowser ) )
		
class AncestorsVC( javax.swing.JPanel ):
	def __init__( self, galleryBrowser ):
		javax.swing.JPanel.__init__( self )
		self._galleryBrowser = galleryBrowser
		self.setLayout( java.awt.GridBagLayout() )
		self._directory = None
		self._root = galleryBrowser._thumbnailsSubRoot
		self.setDirectory( self._root )

	def _update( self ):
		if self._directory:
			ancestors = [ CurrentLabel( self._directory, self._galleryBrowser ) ]
			dir = self._directory
			while dir != self._root:
				dir = dir.getParentFile()
				ancestors.append( AncestorButton( dir, self._galleryBrowser ) )
			self.removeAll()
			gbc = java.awt.GridBagConstraints()
			gbc.insets.right = 16
			gbc.anchor = java.awt.GridBagConstraints.WEST
			gbc.fill = java.awt.GridBagConstraints.BOTH
			while ancestors:
				self.add( ancestors.pop(), gbc )
			gbc.weightx = 1.0
			gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER
			self.add( javax.swing.JLabel(), gbc )
			self.revalidate()
			self.repaint()

	def setDirectory( self, directory ):
		if self._directory == directory:
			pass
		else:
			self._directory = directory
			self._update()
			
	def getDirectoryUpOneLevel( self ):
		if self._directory:
			if self._directory != self._root:
				return self._directory.getParentFile()
		return None

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
	
class GalleryBrowser( javax.swing.JPanel, java.awt.event.KeyListener ):
	def __init__( self, thumbnailsRoot, subRoot, map ):
		javax.swing.JPanel.__init__( self )
		self._thumbnailsRoot = thumbnailsRoot
		self._thumbnailsSubRoot = java.io.File( thumbnailsRoot, subRoot )
		self._map = map
		self._ancestorsVC = AncestorsVC( self )
		self._directoryFilesVC = DirectoryFilesListVC( self, listSelectionListener=ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleFileSelection ) )
		self._directoryFilesVC.setDirectory( self._ancestorsVC._root )
		
		panel = javax.swing.JPanel()
		panel.setLayout( java.awt.BorderLayout() )
		panel.add( self._ancestorsVC, java.awt.BorderLayout.NORTH )
		panel.add( javax.swing.JScrollPane( self._directoryFilesVC ), java.awt.BorderLayout.CENTER )

		fromFilePane = javax.swing.JPanel()
		fromFilePane.setLayout( java.awt.GridLayout( 2, 1, 0, 4 ) )
		fromFilePane.add( CreateInstanceOfTypeButton( "My Classes...", self._handleCreateInstanceOfTypeButton, edu.cmu.cs.dennisc.alice.io.FileUtilities.getMyTypesDirectory() ) )
		fromFilePane.add( CreateInstanceOfTypeButton( "Textbook Classes...", self._handleCreateInstanceOfTypeButton, _getTextbookTypesDirectory() ) )
		
		self.setLayout( java.awt.GridBagLayout() )
		gbc = java.awt.GridBagConstraints()
		gbc.fill = java.awt.GridBagConstraints.BOTH
		gbc.weightx = 0.0
		gbc.weighty = 1.0
#		gbc.insets.left = INSET
#		gbc.insets.top = INSET
#		gbc.insets.bottom = INSET
		gbc.weightx = 1.0
		self.add( panel, gbc )
		gbc.weightx = 0.0
		self.add( CreateTextButton(), gbc )
		gbc.insets.left = 12
		self.add( fromFilePane, gbc )

		#self._ancestorsVC.addKeyListener( self )
		self._directoryFilesVC.addKeyListener( self )

	def _handleCreateInstanceOfTypeButton(self, e, directory ):
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

	def keyPressed( self, e ):
		pass
	def keyReleased( self, e ):
		if e.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE:
			upOneLevelDir = self._ancestorsVC.getDirectoryUpOneLevel()
			if upOneLevelDir:
				self._handleFileSelection( upOneLevelDir )
	def keyTyped( self, e ):
		pass

	def getDefaultFolderPath(self):
		root = alice.ide.IDE.getSingleton()._getResourcesRootDirectory() 
		rv = java.io.File( root, "folder.png" ).getAbsolutePath()
		return rv

	def _handleFileSelection( self, file ):
		if file:
			if file.isDirectory():
				self._directoryFilesVC.setDirectory( file )
				self._ancestorsVC.setDirectory( file )
				self.revalidate()
			else:
				#fileView = FileView( file )
#				result = javax.swing.JOptionPane.showConfirmDialog(self, fileView, "Create Instance", javax.swing.JOptionPane.YES_NO_OPTION )
#				if result == javax.swing.JOptionPane.YES_OPTION:
#					if self._scenePane:
#						self._scenePane.addInstanceFromClassName( fileView.getClassName( self._thumbnailsRoot ) )
				inputPane = CreateInstancePane( file, self._thumbnailsRoot, None )
				owner = alice.ide.IDE.getSingleton()
				instance = inputPane.showInJDialog( owner, "Create Instance", True )
				if instance:
					getSceneEditor().addInstance( instance )

				self._directoryFilesVC.clearSelection()
