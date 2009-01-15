from javax.swing import SwingUtilities
import java
import javax
import edu

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import awt
from org.alice import apis
from edu.cmu.cs.dennisc import lookingglass

import ecc

from ProgramAdapter import ProgramAdapter

class LookingGlassAdapter( edu.cmu.cs.dennisc.lookingglass.event.LookingGlassListener ):
	def __init__( self, editor ):
		self._editor = editor
	def displayChanged( self, e ):
		pass
	def initialized( self, e ):
		pass
	def resized( self, e ):
		pass
	def cleared( self, e ):
		pass
	def rendered( self, e ):
		self._editor._awtContainerProxy.paint( e.getGraphics2D() )

#class MouseAdapter( java.awt.event.MouseListener ):
#	def __init__( self, editor ):
#		self._editor = editor
#	def mouseEntered( self, e ):
#		self._editor.handleMouseEntered( e )
#	def mouseExited( self, e ):
#		self._editor.handleMouseExited( e )
#	def mousePressed( self, e ):
#		pass
#	def mouseReleased( self, e ):
#		pass
#	def mouseClicked( self, e ):
#		pass

class ComponentAdapter( java.awt.event.ComponentListener ):
	def __init__( self, editor ):
		self._editor = editor
	def componentShown( self, e ):
		pass
	def componentHidden( self, e ):
		pass
	def componentMoved( self, e ):
		pass
	def componentResized( self, e ):
		#self._editor._forceAtLeastOneLiveFrame()
		self._editor._updateLookingGlassSize()

class MoveAndTurnSceneEditor( alice.ide.editors.scene.AbstractSceneEditor ):
	def __init__( self ):
		alice.ide.editors.scene.AbstractSceneEditor.__init__( self )
		ide = self.getIDE()

		self._controlsForOverlayPane = ecc.dennisc.alice.ide.barebones.editors.scene.ControlsForOverlayPane()
		
		self._program = ProgramAdapter( ide.getVirtualMachineForSceneEditor(), ide.getRunOperation(), self._controlsForOverlayPane )

		#todo: replace with showInAWTContainer()
		self._program.setArgs( [] )
		self._program.init()
		while self._program.m_isInitializationSuccessful == False:
			java.lang.Thread.currentThread().sleep( 100 )
		self._program.start()

		onscreenLookingGlass = self._program.getOnscreenLookingGlass()
		#onscreenLookingGlass.getAWTComponent().setSize( 640, 480 )

		pane = javax.swing.JPanel()
		#pane.setBackground( java.awt.Color.RED )
		pane.setOpaque( False )
		pane.setLayout( java.awt.BorderLayout() )
		pane.add( onscreenLookingGlass.getAWTComponent(), java.awt.BorderLayout.CENTER )
		pane.add( self._controlsForOverlayPane, java.awt.BorderLayout.CENTER )
		
		#edu.cmu.cs.dennisc.swing.SpringUtilities.expandToBounds( self._controlsForOverlayPane )
		#edu.cmu.cs.dennisc.swing.SpringUtilities.expandToBounds( onscreenLookingGlass.getAWTComponent() )

		self._cardPane = edu.cmu.cs.dennisc.lookingglass.util.CardPane( onscreenLookingGlass, pane )

		self.setLayout( java.awt.BorderLayout() )
		self.add( self._cardPane, java.awt.BorderLayout.CENTER )
		
		
		#edu.cmu.cs.dennisc.swing.SpringUtilities.expandToBounds( self._controlsForOverlayPane )
		#edu.cmu.cs.dennisc.swing.SpringUtilities.expandToBounds( self._cardPane )
		
		#self.add( self._cardPane, java.awt.BorderLayout.CENTER )
		#self.add( onscreenLookingGlass.getAWTComponent() )
		
		redirectingEventQueue = edu.cmu.cs.dennisc.awt.RedirectingEventQueue( onscreenLookingGlass.getAWTComponent(), self._controlsForOverlayPane )
		java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue().push( redirectingEventQueue )
		
		self._awtContainerProxy = edu.cmu.cs.dennisc.lookingglass.overlay.AWTContainerProxy( self._controlsForOverlayPane )
		onscreenLookingGlass.addLookingGlassListener( LookingGlassAdapter( self ) )
		
#		mouseAdapter = MouseAdapter( self )
#		self._controlsForOverlayPane.addMouseListener( mouseAdapter )
#		self._cardPane.addMouseListener( mouseAdapter )
		componentAdapter = ComponentAdapter( self )
		self._controlsForOverlayPane.addComponentListener( componentAdapter )
		
		#todo?
		# 
		#Toolkit.getDefaultToolkit().addAWTEventListener(adapter, AWTEvent.MOUSE_EVENT_MASK);
		#
		 
	def setDragInProgress(self, isDragInProgress):
		self._program.setDragInProgress( isDragInProgress ) 

	def _updateLookingGlassSize(self):
		onscreenLookingGlass = self._program.getOnscreenLookingGlass()
		onscreenLookingGlass.getAWTComponent().setSize( self.getWidth(), self.getHeight() )
	
	def handleDelete(self, node):
		self._program.handleDelete( node )
	def handleMouseEntered( self, e ):
		if self.getIDE().isDragInProgress():
			pass
		else:
			self._cardPane.showLive()
	def handleMouseExited( self, e ):
		self._showSnapshotIfAppropriateFromEvent( e )

#	def isSwappingOutLookingGlassDesired(self):
#		return False

#	def _showSnapshotIfAppropriate( self, p ):
#		#return
#		if self.isSwappingOutLookingGlassDesired():
#			if p is None:
#				pass
#			else:
#				if self.contains( p ):
#					pass
#				else:
#					if self.getIDE().isSceneEditorExpanded():
#						pass
#					else:
#						#todo: how does one query the state of the buttons?
#						#button = 0
#						if self._program.isDragInProgress():
#							pass
#						else:
#							self._cardPane.showSnapshot()
#
#	def _showSnapshotIfAppropriateFromEvent( self, e ):
#		self._showSnapshotIfAppropriate( e.getPoint() )
#	def _showSnapshotIfAppropriateFromRunnable( self ):
#		p = java.awt.MouseInfo.getPointerInfo().getLocation()
#		p = javax.swing.SwingUtilities.convertPointFromScreen( p, self )
#		self._showSnapshotIfAppropriate( p )
#
#	def _forceAtLeastOneLiveFrame( self ):
#		self._cardPane.showLive()
#		javax.swing.SwingUtilities.invokeLater( ecc.dennisc.lang.ApplyRunnable( self._showSnapshotIfAppropriateFromRunnable ) )

	def setDragInProgress(self, isDragInProgress):
		#if edu.cmu.cs.dennisc.lang.SystemUtilities.isMac():
		if True:
			if isDragInProgress:
				self._cardPane.showSnapshot()
			else:
				self._cardPane.showLive()
			
	def handleExpandContractChange( self, isExpanded ):
		self._program._expandContractToggleButton.getRenderer().setExpanded( isExpanded )

	def _isFieldNameFree( self, name ):
		return self._program._isFieldNameFree( name )
	def _getAvailableFieldName( self, superClassBaseName ):
		return self._program._getAvailableFieldName( superClassBaseName )
	def getSceneType( self ):
		return self._program.getSceneType()
	def setProgramType( self, programType ):
		alice.ide.editors.scene.AbstractSceneEditor.setProgramType( self, programType )
		self._program.setProgramType( programType )
		
	def setSceneField( self, sceneField ):
		alice.ide.editors.scene.AbstractSceneEditor.setSceneField( self, sceneField )
		self._controlsForOverlayPane.setSceneField( sceneField )
#		if self._cardPane.isLive():
#			pass
#		else:
#			self._forceAtLeastOneLiveFrame()

	def handleExpandContractChange( self, isExpanded ):
		self._controlsForOverlayPane.handleExpandContractChange( isExpanded )

	def createInstance( self, type ):
		return self._program.createInstance( type )

			

	def _handleCreateInstance( self, type ):
		#todo
		t = type.getFirstTypeEncounteredDeclaredInJava()
		cls = t.getCls()
		clsName = cls.__name__
		thumbnailsRoot = self._api._getGalleryThumbnailsRoot()
		
		prefixList = []
		prefixList.append( "org.alice.apis.moveandturn.gallery" )
		prefixList.append( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery" )
		for prefix in prefixList:
			if clsName.startswith( prefix ):
				file = java.io.File( thumbnailsRoot, prefix + clsName[ len( prefix ): ].replace( ".", "/" ) + ".png" )

		inputPane = ecc.dennisc.alice.ide.moveandturn.editors.gallery.CreateInstancePane( self, file, thumbnailsRoot, type )
		owner = alice.ide.IDE.getSingleton()
		instance = inputPane.showInJDialog( owner, "Create Instance", True )
		if instance:
			self.addInstance( instance )

	def addInstance( self, instance ):
		self._program.addInstance( instance )


	def generateCodeForSceneSetUp( self, setUpMethodGenerator ):
		self._program.generateCodeForSceneSetUp( setUpMethodGenerator )

	def getRunMethod( self ):
		return self.getSceneType().getDeclaredMethod( "run", [] )

