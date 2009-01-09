import ecc

import edu

class SceneAutomaticSetUpMethodFillerInner:
	def _fillInSceneAutomaticSetUpMethodForInstance( self, astStatements, astField, instance ):
		raise "Override"

	def fillInSceneAutomaticSetUpMethod( self, method, sceneField, map ):
		astStatements = method.body.getValue().statements
		astStatements.clear()
		astStatements.add( [ edu.cmu.cs.dennisc.alice.ast.Comment( "DO NOT EDIT.  This code is automatically generated." ) ] )
		self._fillInSceneAutomaticSetUpMethodForInstance( astStatements, sceneField, ecc.dennisc.alice.vm.getInstanceInJava( map[ sceneField ] ) )
		for astField in sceneField.getValueType().fields.iterator():
			self._fillInSceneAutomaticSetUpMethodForInstance( astStatements, astField, ecc.dennisc.alice.vm.getInstanceInJava( map[ astField ] ) )
