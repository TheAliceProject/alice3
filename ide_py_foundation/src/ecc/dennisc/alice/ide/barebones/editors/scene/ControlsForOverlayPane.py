import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

import ecc

class MyFieldTile( alice.ide.editors.scene.FieldTile ):
	def __init__(self, field):
		self._adapter = self.createAltTriggerMouseAdapter( field )
		alice.ide.editors.scene.FieldTile.__init__( self, field )
		self.addMouseListener( self._adapter )
	def setField(self, field):
		alice.ide.editors.scene.FieldTile.setField( self, field )
		self._adapter._field = field
	def createAltTriggerMouseAdapter(self, field):
		return ecc.dennisc.alice.ide.operations.ast.FieldAltTriggerMouseAdapter( field )

class ControlsForOverlayPane( edu.cmu.cs.dennisc.alice.ide.editors.scene.ControlsForOverlayPane ):
	def __init__( self ):
		edu.cmu.cs.dennisc.alice.ide.editors.scene.ControlsForOverlayPane.__init__( self )
		ide = alice.ide.IDE.getSingleton()
		self._expandContractButton = alice.ide.Button( ecc.dennisc.alice.ide.operations.window.ToggleExpandContractSceneEditorOperation() )
		self.setSouthEastComponent( self._expandContractButton )

	def createFieldTile(self, field ):
		return MyFieldTile( field )

	def setSceneField( self, sceneField ):
		self.setRootField( sceneField )

	#todo: remove
	def handleExpandContractChange( self, isExpanded ):
		pass
