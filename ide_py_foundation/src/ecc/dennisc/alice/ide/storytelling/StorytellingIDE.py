import ecc

import java
import javax
import edu

from edu.cmu.cs.dennisc import alice
from edu.cmu.cs.dennisc import awt
from org.alice import apis
from edu.cmu.cs.dennisc import lookingglass

class StorytellingSceneAutomaticSetUpMethodFillerInner( ecc.dennisc.alice.ide.moveandturn.MoveAndTurnSceneAutomaticSetUpMethodFillerInner ):
	def _fillInSceneAutomaticSetUpMethodForElement( self, astStatements, astField, instance ):
		ecc.dennisc.alice.ide.moveandturn.MoveAndTurnSceneAutomaticSetUpMethodFillerInner._fillInSceneAutomaticSetUpMethodForElement( self, astStatements, astField, instance )
		if isinstance( instance, apis.storytelling.Person ):
			setSkinToneMethod = ecc.dennisc.alice.ast.lookupMethod( apis.storytelling.Person, "setSkinTone", [ apis.storytelling.SkinTone ] )
			astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), setSkinToneMethod, [ ecc.dennisc.alice.ast.createEnumConstant( instance.getSkinTone() ) ] ) ] )
			setFitnessLevelMethod = ecc.dennisc.alice.ast.lookupMethod( apis.storytelling.Person, "setFitnessLevel", [ apis.storytelling.FitnessLevel ] )
			astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), setFitnessLevelMethod, [ecc.dennisc.alice.ast.createEnumConstant( instance.getFitnessLevel() ) ] ) ] )
			if isinstance( instance, apis.storytelling.FemaleAdult ):
				setOutfitMethod = ecc.dennisc.alice.ast.lookupMethod( apis.storytelling.FemaleAdult, "setOutfit", [ apis.storytelling.FemaleAdultFullBodyOutfit ] )
			elif isinstance( instance, apis.storytelling.MaleAdult ):
				setOutfitMethod = ecc.dennisc.alice.ast.lookupMethod( apis.storytelling.MaleAdult, "setOutfit", [ apis.storytelling.MaleAdultFullBodyOutfit ] )
			else:
				raise "todo"
			astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), setOutfitMethod, [ecc.dennisc.alice.ast.createEnumConstant( instance.getOutfit() ) ] ) ] )
			setEyeColorMethod = ecc.dennisc.alice.ast.lookupMethod( apis.storytelling.Person, "setEyeColor", [ apis.storytelling.EyeColor ] )
			astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), setEyeColorMethod, [ecc.dennisc.alice.ast.createEnumConstant( instance.getEyeColor() ) ] ) ] )
			setHairMethod = ecc.dennisc.alice.ast.lookupMethod( apis.storytelling.Person, "setHair", [ apis.storytelling.Hair ] )
			astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), setHairMethod, [ecc.dennisc.alice.ast.createEnumConstant( instance.getHair() ) ] ) ] )

class PersonEditorButton( javax.swing.JButton, java.awt.event.ActionListener ):
	def __init__( self, ide ):
		javax.swing.JButton.__init__( self )
		self._ide = ide
		self.setText( "Create Person..." )
		ROOT = self._ide._getResourcesRootDirectory()
		CREATE_PERSON_FILE = java.io.File( ROOT, "personbuilder/create_person.png" )
		self.setIcon( javax.swing.ImageIcon( CREATE_PERSON_FILE.getAbsolutePath() ) )
		self.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER )
		self.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM )
		self.addActionListener( self )

		self._personEditor = None
		
	def actionPerformed(self, e):
		if self._personEditor:
			pass
		else:
			self._personEditor = ecc.dennisc.alice.ide.storytelling.editors.person.PersonEditor( self._ide._scenePane )
			self._personEditor.setPreferredSize( java.awt.Dimension( 1200, 740 ) )
		
		instance = self._personEditor.showInJDialog( self, "Create Person", True )
		if instance:
			self._ide._scenePane.addInstance( instance )

class StorytellingIDE( ecc.dennisc.alice.ide.moveandturn.MoveAndTurnIDE ):
	def __init__( self ):
		ecc.dennisc.alice.ide.moveandturn.MoveAndTurnIDE.__init__( self )

	def _createGalleryBrowser(self):
		personEditorButton = PersonEditorButton( self )
		moveandturnGalleryBrowser = ecc.dennisc.alice.ide.moveandturn.MoveAndTurnIDE._createGalleryBrowser( self )
		moveandturnGalleryBrowser.add( personEditorButton, java.awt.BorderLayout.WEST )
		return moveandturnGalleryBrowser
#		panel = javax.swing.JPanel()
#		panel.setLayout( java.awt.BorderLayout() )
#		panel.add( personEditorButton, java.awt.BorderLayout.WEST )
#		panel.add( moveandturnGalleryBrowser, java.awt.BorderLayout.CENTER )
#		return panel

	def _createSceneSetUpCodeGenerator( self ):
		return StorytellingSceneAutomaticSetUpMethodFillerInner()

