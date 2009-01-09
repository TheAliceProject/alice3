#print "-->", __name__

import java
import javax
import edu

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import awt
from org.alice import apis
from edu.cmu.cs.dennisc import lookingglass

import ecc

class RuntimeProgramMixin:
	def __init__( self, vm, sceneType ):
		self._sceneType = sceneType
		self._vm = vm

	def setUp( self ):
		#print "pre constructor"
		self._scene = self._vm.createInstanceEntryPoint( self._sceneType.getDeclaredConstructor( [] ), [] )
		#print "post constructor"
#		self._vm.invokeEntryPoint( self._sceneType.getDeclaredMethod( "performSetUp", [] ), self._scene, [] )

	def run( self ):
		self._vm.invokeEntryPoint( self._sceneType.getDeclaredMethod( "run", [] ), self._scene, [] )

class MoveAndTurnRuntimeProgram( apis.moveandturn.Program, RuntimeProgramMixin ):
	def __init__( self, vm, sceneType ):
		apis.moveandturn.Program.__init__( self )
		RuntimeProgramMixin.__init__( self, vm, sceneType )
	def initialize( self ):
		self.setUp()
		self.setScene( self._scene.getInstanceInJava() )

class RunOperation( ecc.dennisc.alice.ide.operations.run.AbstractRunOperation ):
	def perform(self):
		ide = self.getIDE()
		ide.generateCodeForSceneSetUp()
		program = MoveAndTurnRuntimeProgram( ide.createVirtualMachineForRuntimeProgram(), ide.getSceneType() )
		program.showInJDialog( ide, True, [] )

#print "<--", __name__
