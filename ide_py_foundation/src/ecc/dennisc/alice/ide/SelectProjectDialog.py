#print "-->", __name__

import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

import ecc

class ProjectTabPane( javax.swing.JPanel ):
	def __init__( self, owner, tabTitle, tabIcon, tabTip ):
		javax.swing.JPanel.__init__( self )
		self._owner = owner
		self._tabTitle = tabTitle
		self._tabIcon = tabIcon
		self._tabTip = tabTip
		self._inputPane = None
	def getTabTitle( self ):
		return self._tabTitle
	def getTabIcon( self ):
		return self._tabIcon
	def getTabTip( self ):
		return self._tabTip
	def setInputPane( self, inputPane ):
		self._inputPane = inputPane
		self.updateOKButton()
		
	def isValid(self):
		raise "Override"
	
	def updateOKButton( self ):
		if self._inputPane:
			self._inputPane.updateOKButton()
	def fireOKButtonIfPossible( self ):
		if self._inputPane:
			self._inputPane.fireOKButtonIfPossible()

class ComingSoonTabPane( ProjectTabPane ):
	def __init__( self, owner, tabTitle ):
		ProjectTabPane.__init__( self, owner, tabTitle, None, "coming soon" )
		self.add( javax.swing.JLabel( "coming soon" ) )
		self.setEnabled( False )
	def isValid(self):
		return False

class TutorialPane( ComingSoonTabPane ):
	def __init__( self, owner ):
		ComingSoonTabPane.__init__( self, owner, "Tutorial" )
class RecentPane( ComingSoonTabPane ):
	def __init__( self, owner ):
		ComingSoonTabPane.__init__( self, owner, "Recent" )


class NotAvailableIcon( javax.swing.Icon ):
	def getIconWidth( self ):
		return 160
	def getIconHeight( self ):
		return 120
	def paintIcon( self, c, g, x, y ):
		g.setColor( java.awt.Color.DARK_GRAY )
		g.fillRect( x, y, 160, 120 )
		g.setColor( java.awt.Color.LIGHT_GRAY )
		g.drawString( "snapshot not available", x + 15, y + 60 )

NOT_AVAILABLE_ICON = NotAvailableIcon()

class FileListCellRenderer( ecc.dennisc.swing.AbstractIconListCellRenderer ):
	def __init__( self ):
		ecc.dennisc.swing.AbstractIconListCellRenderer.__init__( self, 8 )
	def _getTextForLabelAndIcon( self, label, list, value, index, isSelected, isFocus ):
		textForLabel = value.getName()
		pathForIcon = value.getAbsolutePath()[:-3] + "png"
		if edu.cmu.cs.dennisc.io.FileUtilities.existsAndHasLengthGreaterThanZero( pathForIcon ):
			icon = javax.swing.ImageIcon( pathForIcon )
		else:
			icon = NOT_AVAILABLE_ICON
		label.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER )
		label.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM )
		return textForLabel, icon

class DirectoryListModel( javax.swing.ListModel ):
	def __init__( self, directoryListPane ):
		self._directoryListPane = directoryListPane
	def addListDataListener( self, l ):
		pass 
	def removeListDataListener( self, l ):
		pass 
	def getSize( self ):
		self._files = self._directoryListPane._listProjectFiles()
		if self._files:
			return len( self._files )
		else:
			return 0
	def getElementAt( self, i ):
		return self._files[ i ]

class DirectoryListPane( ProjectTabPane, java.awt.event.MouseListener ):
	def __init__( self, owner, tabTitle, tabIcon, tabTip ):
		ProjectTabPane.__init__( self, owner, tabTitle, tabIcon, tabTip )
		self._listVC = ecc.dennisc.swing.ListVC()
		self._listVC.setModel( DirectoryListModel( self ) )
		self._listVC.setCellRenderer( FileListCellRenderer() )
		self._listVC.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP )
		self._listVC.setVisibleRowCount( -1 )
		
		self._listVC.addListSelectionListener( ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleListSelection ) )
		self._listVC.addMouseListener( self )
		self.setLayout( java.awt.BorderLayout() )
		self.add( javax.swing.JScrollPane( self._listVC ), java.awt.BorderLayout.CENTER )
		
		self._files = None

	def _getDirectory( self ):
		raise "Override"
	def _listProjectFiles( self ):
		if self._files != None:
			pass
		else:
			directory = self._getDirectory()
			print directory
			self._files = edu.cmu.cs.dennisc.alice.io.FileUtilities.listProjectFiles( directory )
		return self._files

	def mouseClicked( self, e ):
		if e.getClickCount() == 2:
			self.fireOKButtonIfPossible()
	def mouseEntered( self, e ):
		pass
	def mouseExited( self, e ):
		pass
	def mousePressed( self, e ):
		pass
	def mouseReleased( self, e ):
		pass

	def isValid( self ):
		return self._listVC.getSelectedValue() != None
		
	def _handleListSelection( self, value ):
		self.updateOKButton()

	def getSelectedFile( self ):
		return self._listVC.getSelectedValue()
	
	def getApplicationRootDirectory(self):
		return alice.ide.IDE.getSingleton().getApplicationRootDirectory()

class MyProjectsPane( DirectoryListPane ):
	def __init__( self, owner ):
		DirectoryListPane.__init__( self, owner, "My Projects", None, None )
	def _getDirectory( self ):
		return alice.io.FileUtilities.getMyProjectsDirectory()

class TemplatePane( DirectoryListPane ):
	def __init__( self, owner ):
		DirectoryListPane.__init__( self, owner, "Templates", None, None )
	def _getDirectory( self ):
		return java.io.File( self.getApplicationRootDirectory(), "projects/templates" )

class TextbookPane( DirectoryListPane ):
	def __init__( self, owner ):
		DirectoryListPane.__init__( self, owner, "Textbook", None, None )
	def _getDirectory( self ):
		return java.io.File( self.getApplicationRootDirectory(), "projects/textbook" )

class ExamplePane( DirectoryListPane ):
	def __init__( self, owner ):
		DirectoryListPane.__init__( self, owner, "Examples", None, None )
	def _getDirectory( self ):
		return java.io.File( self.getApplicationRootDirectory(), "projects/examples" )


class OpenPane( ProjectTabPane ):
	def __init__( self, owner ):
		ProjectTabPane.__init__( self, owner, "File System", None, None )
		self.setLayout( java.awt.BorderLayout() )
	
	def isValid( self ):
		selectedFile = self.getSelectedFile()
		return selectedFile and selectedFile.exists()

class SwingOpenPane( OpenPane ):
	def __init__( self, owner ):
		OpenPane.__init__( self, owner )
		self._fileChooser = javax.swing.JFileChooser()
		self._fileChooser.setControlButtonsAreShown( False )

		self.add( self._fileChooser, java.awt.BorderLayout.CENTER )

	def getSelectedFile( self ):
		return self._fileChooser.getSelectedFile()

class AWTOpenPane( OpenPane ):
	def __init__( self, owner ):
		OpenPane.__init__( self, owner )
		self._pathTextField = javax.swing.JTextField()
		self._pathTextField.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( self._handlePathChange ) )
		
		self._browseButton = javax.swing.JButton( "Browse..." )
		self._browseButton.addActionListener( ecc.dennisc.swing.event.ActionAdapter( self._handleBrowse ) )

		panel = javax.swing.JPanel()
		panel.setLayout( java.awt.BorderLayout() )
		panel.add( javax.swing.JLabel( "file:" ), java.awt.BorderLayout.WEST )
		panel.add( self._pathTextField, java.awt.BorderLayout.CENTER )
		panel.add( self._browseButton, java.awt.BorderLayout.EAST )

		self.add( panel, java.awt.BorderLayout.NORTH )

		#self.add( javax.swing.JLabel( "waiting on javax.swing.JFileChooser to be fixed on Windows platform" ), java.awt.BorderLayout.SOUTH )

	def _handleBrowse( self, e ):
		file = ecc.dennisc.swing.showFileOpenDialog( self, alice.io.FileUtilities.getMyProjectsDirectory(), alice.io.FileUtilities.PROJECT_EXTENSION )
		if file:
			self._pathTextField.setText( file.getAbsolutePath() )
	
	def _handlePathChange( self, text ):
		self.updateOKButton()

	def getSelectedFile( self ):
		path = self._pathTextField.getText()
		file = java.io.File( path )
		if file.exists():
			return file
		else:
			return None

class TabbedPane( javax.swing.JTabbedPane, edu.cmu.cs.dennisc.pattern.Validator ):
	def isValid( self ):
		selectedComponent = self.getSelectedComponent()
		if selectedComponent:
			return selectedComponent.isValid()
		else:
			return False

class ProjectInputPane( edu.cmu.cs.dennisc.swing.InputPane ):
	def __init__( self, owner ):
		edu.cmu.cs.dennisc.swing.InputPane.__init__( self )
		self._tabbedPane = TabbedPane()
		self._myProjectsPane = MyProjectsPane( owner ) 
		self._fileSystemPane = AWTOpenPane( owner ) #SwingOpenPane() 
		#self._tutorialPane = TutorialPane(owner)
		#self._recentPane = RecentPane(owner)
		self._templatePane = TemplatePane( owner )
		self._examplePane = ExamplePane( owner )
		self._textbookPane = TextbookPane( owner )
		self._tabbedPane.addChangeListener( ecc.dennisc.swing.event.ChangeAdapter( self._handleTabChange ) )
		self._selectedComponent = None
		for pane in ( self._myProjectsPane, self._fileSystemPane, self._templatePane, self._examplePane, self._textbookPane ):
			i = self._tabbedPane.getTabCount()
			self._tabbedPane.addTab( pane.getTabTitle(), pane.getTabIcon(), pane, pane.getTabTip() )
			self._tabbedPane.setEnabledAt( i, pane.isEnabled() )

		self.setLayout( java.awt.BorderLayout() )
		self.add( self._tabbedPane, java.awt.BorderLayout.CENTER )
		self.setPreferredSize( java.awt.Dimension( 640, 480 ) )

		self.addOKButtonValidator( self._tabbedPane )
		
		
	def selectAppropriateTab( self, isNew ):
		if isNew:
			pane = self._templatePane
		else:
			pane = self._myProjectsPane
			
		self._tabbedPane.setSelectedComponent( self._templatePane )
	def _handleTabChange( self, e ):
		if self._selectedComponent:
			self._selectedComponent.setInputPane( None )
		self._selectedComponent = self._tabbedPane.getSelectedComponent()
		if self._selectedComponent:
			self._selectedComponent.setInputPane( self )

	def getActualInputValue( self ):
		return self._tabbedPane.getSelectedComponent().getSelectedFile()
	

_projectInputPane = None

def showSelectProjectDialog( owner, isNew ):
	owner = javax.swing.SwingUtilities.getRoot( owner )
	global _projectInputPane
	if _projectInputPane:
		pass
	else:
		_projectInputPane = ProjectInputPane( owner )		

	_projectInputPane.selectAppropriateTab( isNew )

#	dialog = ecc.dennisc.swing.InputDialog( owner, _projectInputPane )
#	dialog.setOKEnabled( False )
#	dialog.setVisible( True )
#	file = dialog.getInputValue()
#	return file

	return _projectInputPane.showInJDialog( owner, "Open Project", True )

#print "<--", __name__
