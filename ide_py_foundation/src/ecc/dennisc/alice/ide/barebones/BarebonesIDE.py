#print "-->", __name__

import ecc

class BarebonesIDE( ecc.dennisc.alice.ide.AbstractIDE ):
	def _createScenePane( self ):
		return ecc.dennisc.alice.ide.barebones.editors.scene.BarebonesScenePane()
	def createRunOperation( self ):
		return ecc.dennisc.alice.ide.barebones.runtime.RunOperation()
	def _createSceneSetUpCodeGenerator( self ):
		return None

#print "<--", __name__
	