from org.alice import apis

import java
import javax
import edu
import org

import ecc

class IngredientListCellRenderer( ecc.dennisc.swing.AbstractIconListCellRenderer ):
	def _getSubPath( self ):
		raise "Override"
	def _getIngredientPath( self, skinTone, clsName, enumConstantName ):
		ROOT = edu.cmu.cs.dennisc.alice.ide.IDE.getSingleton()._getResourcesRootDirectory()
		rv = ROOT.getAbsolutePath()
		rv += "/personbuilder/"
		rv += self._getSubPath()
		rv += "/"
		rv += skinTone
		rv += "/"
		rv += clsName
		rv += "."
		rv += enumConstantName
		rv += ".png"
		return rv
	def _getTextForLabelAndIcon( self, label, list, value, index, isSelected, isFocus ):
		clsName = value.getClass().getSimpleName()
		enumConstantName = value.toString()
		#textForLabel = clsName + "." + enumConstantName
		textForLabel = ""
		pathForIcon = self._getIngredientPath( list._baseSkinTone.toString(), clsName, enumConstantName )
		label.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER )
		label.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM )
		return textForLabel, javax.swing.ImageIcon( pathForIcon )

class FullBodyOutfitListCellRenderer( IngredientListCellRenderer ):
	def _getSubPath( self ):
		return "fullbodyoutfit_pictures"

class HairListCellRenderer( IngredientListCellRenderer ):
	def _getSubPath( self ):
		return "hair_pictures"

class IngedientListVC( ecc.dennisc.swing.ListVC ):
	def __init__( self, lifeStage=None, gender=None, baseSkinTone=None, listSelectionListener=None ):
		ecc.dennisc.swing.ListVC.__init__( self, listSelectionListener )
		self.setLayoutOrientation( javax.swing.JList.HORIZONTAL_WRAP )
		self.setVisibleRowCount( - 1 )
		self._lifeStage = None
		self._gender = None
		self._baseSkinTone = None
		self.setLifeStage( lifeStage )
		self.setGender( gender )
		self.setBaseSkinTone( baseSkinTone )
		self.setCellRenderer( self._createCellRenderer() )

	def _createCellRenderer( self ):
		raise "Override"
	
	def _getIngredientInterface( self ):
		raise "Override"

	def _selectAppropriateIndex(self, prevSelectedIndex):
		if prevSelectedIndex >= 0:
			if prevSelectedIndex < 10:
				n = prevSelectedIndex
			else:
				import random
				n = random.randint( 0, 9 )
			self.setSelectedIndex( n )
	def _update( self ):
		prevSelectedIndex = self.getSelectedIndex()
		if self._lifeStage and self._gender:
			#prevValue = self.getSelectedValue()
			cls = self._getIngredientInterface()
			enumClses = apis.stage.IngredientUtilities.get( cls )
			data = []
			if enumClses:
				for enumCls in enumClses:
					values = enumCls.values()
					if values:
						data += values
			self.setSelectedIndex( -1 )
			self.setListData( data )
			self._selectAppropriateIndex( prevSelectedIndex )

	def setLifeStage( self, lifeStage ):
		if self._lifeStage == lifeStage:
			pass
		else:
			self._lifeStage = lifeStage
			self._update()
	def setGender( self, gender ):
		if self._gender == gender:
			pass
		else:
			self._gender = gender
			self._update()
	def setBaseSkinTone( self, baseSkinTone ):
		if self._baseSkinTone == baseSkinTone:
			pass
		else:
			self._baseSkinTone = baseSkinTone
			self._update()
			
class FullBodyOutfitListVC( IngedientListVC ):
	def _createCellRenderer( self ):
		return FullBodyOutfitListCellRenderer()
	
	def _getIngredientInterface( self ):
		return self._lifeStage.getFullBodyOutfitInterface( self._gender )

class HairListVC( IngedientListVC ):
	def _createCellRenderer( self ):
		return HairListCellRenderer()
	
	def _getIngredientInterface( self ):
		return self._lifeStage.getHairInterface( self._gender )


#def randomMaleOutfitIndex():
#	import random
#	return 

class IngredientsVC( javax.swing.JPanel ):
	def __init__( self ):
		javax.swing.JPanel.__init__( self )
		
		self._lifeStageVC = ecc.dennisc.swing.EnumConstantListVC( apis.stage.LifeStage, ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleLifeStageSelection ) )
		self._genderVC = ecc.dennisc.swing.EnumConstantListVC( apis.stage.Gender, ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleGenderSelection ) )
		self._baseSkinToneVC = ecc.dennisc.swing.EnumConstantListVC( apis.stage.BaseSkinTone, ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleBaseSkinToneSelection ) )
		self._baseEyeColorVC = ecc.dennisc.swing.EnumConstantListVC( apis.stage.BaseEyeColor, ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleBaseEyeColorSelection ) )
		self._fullBodyOutfitVC = FullBodyOutfitListVC( None, None, None, ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleFullBodyOutfitSelection ) )
		self._hairVC = HairListVC( None, None, None, ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleHairSelection ) )
		
		self.setLayout( java.awt.GridBagLayout() )
		gbc = java.awt.GridBagConstraints()
		gbc.fill = java.awt.GridBagConstraints.BOTH
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER
		gbc.weightx = 1.0
		
		INSET_TOP = 8
		INSET_LEFT = 0

		#self.add( javax.swing.JLabel( "life stage" ), gbc )
		#gbc.insets.left = INSET_LEFT
		#self.add( self._lifeStageVC, gbc )
		#gbc.insets.left = 0
		
		
		gbc.insets.top = INSET_TOP
		self.add( javax.swing.JLabel( "gender" ), gbc )
		gbc.insets.top = 0
		gbc.insets.left = INSET_LEFT
		self.add( self._genderVC, gbc )
		gbc.insets.left = 0

		gbc.insets.top = INSET_TOP
		self.add( javax.swing.JLabel( "skin tone" ), gbc )
		gbc.insets.top = 0
		gbc.insets.left = INSET_LEFT
		self.add( self._baseSkinToneVC, gbc )
		gbc.insets.left = 0

		gbc.insets.top = INSET_TOP
		gbc.gridwidth = java.awt.GridBagConstraints.RELATIVE
		gbc.weightx = 0.0
		self.add( javax.swing.JLabel( "eye color" ), gbc )
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER
		gbc.weightx = 1.0
		self.add( javax.swing.JLabel( "hair" ), gbc )

		
		gbc.weighty = 1.0
		gbc.insets.top = 0
		gbc.insets.left = INSET_LEFT
		gbc.gridwidth = java.awt.GridBagConstraints.RELATIVE
		gbc.weightx = 0.0
		self.add( self._baseEyeColorVC, gbc )
		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER
		gbc.weightx = 0.0
		self.add( javax.swing.JScrollPane( self._hairVC ), gbc ) 
		gbc.insets.left = 0

		gbc.weighty = 0.0
		gbc.insets.top = INSET_TOP
		self.add( javax.swing.JLabel( "full body outfit" ), gbc )
		gbc.insets.top = 0
		gbc.weighty = 4.0
		gbc.insets.left = INSET_LEFT
		self.add( javax.swing.JScrollPane( self._fullBodyOutfitVC ), gbc ) 
		gbc.insets.left = 0

	def selectRandomOutfit(self):
		if self._genderVC.getSelectedIndex() == 0:
			N = 162
		else:
			N = 249
		#i = N-1
		import random
		i = random.randint( 0, N-1 )
		self._fullBodyOutfitVC.setSelectedIndex( i )
		
	def _handleLifeStageSelection( self, lifeStage ):
		if lifeStage:
			self._fullBodyOutfitVC.setLifeStage( lifeStage )
			self._hairVC.setLifeStage( lifeStage )
			
	def _handleGenderSelection( self, gender ):
		if gender:
			self._fullBodyOutfitVC.setGender( gender )
			self._hairVC.setGender( gender )

	def _handleBaseSkinToneSelection( self, baseSkinTone ):
		if baseSkinTone:
			self._fullBodyOutfitVC.setBaseSkinTone( baseSkinTone )
			self._hairVC.setBaseSkinTone( baseSkinTone )

	def _handleBaseEyeColorSelection( self, baseEyeColor ):
		pass

	def _handleFullBodyOutfitSelection( self, fullBodyOutfit ):
		pass
	def _handleHairSelection( self, hair ):
		pass

class PersonViewer( ecc.dennisc.alice.ide.moveandturn.editors.ModelViewer ):
	def __init__( self ):
		ecc.dennisc.alice.ide.moveandturn.editors.ModelViewer.__init__( self )
		self._dragAdapter = org.alice.interact.CreateASimDragAdapter()
		self._mapToMap = {}
		self._mapToMap[ apis.stage.LifeStage.ADULT ] = {}
		self._mapToMap[ apis.stage.LifeStage.ADULT ][ apis.stage.Gender.FEMALE ] = apis.stage.FemaleAdult()
		self._mapToMap[ apis.stage.LifeStage.ADULT ][ apis.stage.Gender.MALE ] = apis.stage.MaleAdult()
		self._lifeStage = None
		self._gender = None
		self._baseSkinTone = None
		self._baseEyeColor = None
		self._fullBodyOutfit = None
		self._hair = None
		self._fitnessLevel = apis.stage.FitnessLevel.NORMAL
		
	def initialize( self ):
		ecc.dennisc.alice.ide.moveandturn.editors.ModelViewer.initialize( self )
		#self._scene.setAmbientLightBrightness( 1.0 )
		self._camera.moveTo( self._scene.createOffsetStandIn( -1, 2, -7 ), 0.0 )
		self._camera.pointAt( self._scene.createOffsetStandIn( 0, 1.5, 0 ), 0.0 )
		self._sunLight.turn( apis.moveandturn.TurnDirection.FORWARD, org.alice.apis.moveandturn.AngleInRevolutions( 0.125 ) )

		self._dragAdapter.setOnscreenLookingGlass( self.getOnscreenLookingGlass() )
		#self._dragAdapter.setSGCamera( self._camera.getSGCamera() )

	def _update( self ):
		if self._lifeStage and self._gender:
			person = self._mapToMap[ self._lifeStage ][ self._gender ]
			model = None
			if self._baseSkinTone:
				person.setSkinTone( self._baseSkinTone )
				if self._fitnessLevel:
					person.setFitnessLevel( self._fitnessLevel )
					if self._fullBodyOutfit:
						person.setOutfit( self._fullBodyOutfit )
						model = person
			if self._baseEyeColor:
				person.setEyeColor( self._baseEyeColor )
			if self._hair:
				person.setHair( self._hair )
			self.setModel( model )

	def setLifeStage( self, lifeStage ):
		if self._lifeStage == lifeStage:
			pass
		else:
			self._lifeStage = lifeStage
			self._fullBodyOutfit = None
			self._hair = None
			self._update()
	def setGender( self, gender ):
		if self._gender == gender:
			pass
		else:
			self._gender = gender
			self._fullBodyOutfit = None
			self._hair = None
			self._update()
			person = self._mapToMap[ apis.stage.LifeStage.ADULT ][ gender ]
			if person:
				self._dragAdapter.setSelectedObject( person.getSGTransformable() )
	def setBaseSkinTone( self, baseSkinTone ):
		if self._baseSkinTone == baseSkinTone:
			pass
		else:
			self._baseSkinTone = baseSkinTone
			self._update()
	def setBaseEyeColor( self, baseEyeColor ):
		if self._baseEyeColor == baseEyeColor:
			pass
		else:
			self._baseEyeColor = baseEyeColor
			self._update()
	def setFullBodyOutfit( self, fullBodyOutfit ):
		if self._fullBodyOutfit == fullBodyOutfit:
			pass
		else:
			self._fullBodyOutfit = fullBodyOutfit
			self._update()

	def setHair( self, hair ):
		if self._hair == hair:
			pass
		else:
			self._hair = hair
			self._update()

class LabelPane( javax.swing.JPanel ):
	def __init__( self, labelText, vc ):
		javax.swing.JPanel.__init__( self )
		self.setLayout( java.awt.BorderLayout() )
		self.add( javax.swing.JLabel( labelText ), java.awt.BorderLayout.WEST );
		self.add( vc, java.awt.BorderLayout.CENTER );
		
		#vc.setFont( java.awt.Font( None, 0, 18 ) )

from ecc.dennisc.alice.ide.IdentifierPane import IdentifierPane
from ecc.dennisc.alice.ide.PaneWithValidation import PaneWithValidation

class InstanceAndClassNamesPane( PaneWithValidation ):
	def __init__( self ):
		PaneWithValidation.__init__( self )

		#todo
		self._identifierPane = IdentifierPane( None, [] )
		self._identifierPane._textVC.setText( "" )
		self._identifierPane._textVC.selectAll()

		#self._instanceNameVC = ecc.dennisc.swing.InputTextFieldWithValidationCallback( ecc.dennisc.swing.event.ChangeAdapter( self._handleInstanceNameChange ), self._isInstanceNameValid )
		#self._instanceNameVC.setText( "unnamed" )
		#self._instanceNameVC.selectAll()
		
		self._classNameVC = javax.swing.JTextField()
		self._classNameVC.setEditable( False )

		#self._superClassNameVC = javax.swing.JTextField()
		#self._superClassNameVC.setEditable( False )
		#self._superClassNameVC.setText( "FemaleAdult" )
		
		self.setLayout( java.awt.GridLayout( 1, 3 ) )
		self.add( LabelPane( "instance name:", self._identifierPane._textVC ) )
		self.add( LabelPane( "class name:", self._classNameVC ) )
		#self.add( LabelPane( "extends", self._superClassNameVC ) )

	def getInstanceName(self):
		return self._identifierPane._textVC.getText()

	def isValid( self ):
		self._classNameVC.setText( ecc.dennisc.alice.vm.getConventionalClassName( self.getInstanceName() ) )
		return self._identifierPane.isValid()

from ecc.dennisc.alice.ide.inputpanes.AbstractInputPane import AbstractInputPane

def createComponentArray( *args ):
	import jarray
	return jarray.array( args, java.awt.Component )

def _createLabel( text ):
	rv = javax.swing.JLabel( text )
	rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING )
	return rv

class NewInstancePane( AbstractInputPane ):
	def __init__( self, siblings ):
		AbstractInputPane.__init__( self )

		inset = 16
		self.setBorder( javax.swing.border.EmptyBorder( inset, inset, inset, inset ) )

		self._textVC = javax.swing.JTextField()
		self._textVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( self._handleTextChange ) )
		self._instanceNameVC = javax.swing.JTextField()
		self._instanceNameVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( self._handleInstanceNameChange ) )
		self._classNameVC = javax.swing.JTextField()
		self._classNameVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( self._handleClassNameChange ) )

		self._constrainInstanceNameToTextVC = javax.swing.JCheckBox( "generate from text above" )
		self._constrainInstanceNameToTextVC.addItemListener( ecc.dennisc.swing.event.ItemAdapter( self._handleInstanceNameContraintChange ) )
		self._constrainInstanceNameToTextVC.setSelected( True )
		self._constrainClassNameToInstanceNameVC = javax.swing.JCheckBox( "generate from instance name" )
		self._constrainClassNameToInstanceNameVC.addItemListener( ecc.dennisc.swing.event.ItemAdapter( self._handleClassNameContraintChange ) )
		self._constrainClassNameToInstanceNameVC.setSelected( True )

		rows = java.util.ArrayList()
		rows.add( createComponentArray( _createLabel( "" ), self._textVC,  javax.swing.JLabel() ) )
		rows.add( createComponentArray( _createLabel( "instance name:" ), self._instanceNameVC,  self._constrainInstanceNameToTextVC ) )
		#rows.add( createComponentArray( _createLabel( "class name:" ), self._classNameVC, self._constrainClassNameToInstanceNameVC ) )
		edu.cmu.cs.dennisc.swing.SpringUtilities.springItUpANotch( self, rows, 8, 4 )
		
	def isValid( self ):
		instanceName = self._instanceNameVC.getText()
		className = self._classNameVC.getText()
		#todo: check siblings
		return ecc.dennisc.alice.vm.isValidIdentifier( instanceName ) and ecc.dennisc.alice.vm.isValidIdentifier( className )
	
	def _handleInstanceNameContraintChange( self, e ):
		self._instanceNameVC.setEditable( not e.getSource().isSelected() )

	def _handleClassNameContraintChange( self, e ):
		self._classNameVC.setEditable( not e.getSource().isSelected() )

	def _handleTextChange( self, text ):
		text = self._textVC.getText()
		if self._constrainInstanceNameToTextVC.isSelected():
			instanceName = ecc.dennisc.alice.vm.getConventionalInstanceName( text )
			self._instanceNameVC.setText( instanceName )
		self.updateOKButton()
	def _handleInstanceNameChange( self, text ):
		if self._constrainClassNameToInstanceNameVC.isSelected():
			instanceName = self._instanceNameVC.getText()
			className = "My" + ecc.dennisc.alice.vm.getConventionalClassName( instanceName )
			self._classNameVC.setText( className )
		self.updateOKButton()
	def _handleClassNameChange( self, text ):
		self.updateOKButton()

	def getActualInputValue( self ):
		return self._instanceNameVC.getText()
		
class PersonEditor( edu.cmu.cs.dennisc.swing.InputPane ):
	def __init__( self, sceneEditor ):
		edu.cmu.cs.dennisc.swing.InputPane.__init__( self )
		
		self._sceneEditor = sceneEditor
		
		self._personViewerParent = javax.swing.JPanel()
		self._personViewer = PersonViewer()
		self._ingredients = IngredientsVC()
		
		self._ingredients._lifeStageVC.addListSelectionListener( ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleLifeStageSelection ) )
		self._ingredients._genderVC.addListSelectionListener( ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleGenderSelection ) )
		self._ingredients._baseSkinToneVC.addListSelectionListener( ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleBaseSkinToneSelection ) )
		self._ingredients._baseEyeColorVC.addListSelectionListener( ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleBaseEyeColorSelection ) )
		self._ingredients._fullBodyOutfitVC.addListSelectionListener( ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleFullBodyOutfitSelection ) )
		self._ingredients._hairVC.addListSelectionListener( ecc.dennisc.swing.event.FilteredListSelectionAdapter( self._handleHairSelection ) )

		#self._lifeStageVC.selectRandom()
		self._ingredients._lifeStageVC.setSelectedIndex( 3 )
		self._ingredients._genderVC.selectRandom()
		#self._ingredients._genderVC.setSelectedIndex( 0 )
		self._ingredients._baseSkinToneVC.selectRandom()
		self._ingredients._baseEyeColorVC.selectRandom()
		#self._ingredients._fullBodyOutfitVC.selectRandom()
		#self._ingredients._fullBodyOutfitVC.setSelectedIndex( 0 )
		self._ingredients.selectRandomOutfit()
		self._ingredients._hairVC.selectRandom()

		#self._instanceAndClassNamesPane = InstanceAndClassNamesPane()
		#self._instanceAndClassNamesPane.setInputPane( self )
		#self.addOKButtonValidator( self._instanceAndClassNamesPane )

		pane = javax.swing.JPanel()
		pane.setLayout( java.awt.BorderLayout() )
		pane.add( self._personViewerParent, java.awt.BorderLayout.CENTER )
		#pane.add( self._instanceAndClassNamesPane, java.awt.BorderLayout.SOUTH )
		
		self._splitPane = javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT, pane, self._ingredients )
		#self._splitPane.setDividerLocation( 0.75 )
		self._splitPane.setDividerLocation( 400 )
		
		self.setLayout( java.awt.BorderLayout() )
		self.add( self._splitPane, java.awt.BorderLayout.CENTER )

		self._personViewer.showInAWTContainer( self._personViewerParent, [] ) 
		
	def _handleLifeStageSelection( self, lifeStage ):
		self._personViewer.setLifeStage( lifeStage )
			
	def _handleGenderSelection( self, gender ):
		self._personViewer.setGender( gender )

	def _handleBaseSkinToneSelection( self, baseSkinTone ):
		self._personViewer.setBaseSkinTone( baseSkinTone )

	def _handleBaseEyeColorSelection( self, baseEyeColor ):
		self._personViewer.setBaseEyeColor( baseEyeColor )

	def _handleFullBodyOutfitSelection( self, fullBodyOutfit ):
		self._personViewer.setFullBodyOutfit( fullBodyOutfit )

	def _handleHairSelection( self, hair ):
		self._personViewer.setHair( hair )

	def _isInstanceNameValid( self, text ):
		return self._instanceAndClassNamesPane._isInstanceNameValid( text )
	
	def getActualInputValue( self ):
		model = self._personViewer.getModel()
		typeDeclaredInJava = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( model.__class__ )
		type, isAlreadyReferenced = ecc.dennisc.alice.ide.moveandturn.editors.gallery._getTypeForTypeDeclaredInJava( typeDeclaredInJava )
		
		newInstancePane = NewInstancePane( [] )
		instanceName = newInstancePane.showInJDialog( self, "Name Person", True )

		if instanceName:
			pass
		else:
			instanceName = "unnamed"

		rv = self._sceneEditor.createInstance( type )
		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( rv )
		instanceInJava.setName( instanceName )
		instanceInJava.setSkinTone( model.getSkinTone() )
		instanceInJava.setFitnessLevel( model.getFitnessLevel() )
		instanceInJava.setOutfit( model.getOutfit() )
		instanceInJava.setEyeColor( model.getEyeColor() )
		instanceInJava.setHair( model.getHair() )
		return rv

#	def setDialog( self, dialog ):
#		ecc.dennisc.swing.InputPane.setDialog( self, dialog )
#		self._instanceAndClassNamesPane._instanceNameVC.setDialog( dialog )
