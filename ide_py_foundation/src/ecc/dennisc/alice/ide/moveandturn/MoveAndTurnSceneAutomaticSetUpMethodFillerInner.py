#print "-->", __name__

import java
import javax
import edu

from edu.cmu.cs.dennisc import alice
from org.alice import apis

import ecc

class MoveAndTurnSceneAutomaticSetUpMethodFillerInner( ecc.dennisc.alice.ide.SceneAutomaticSetUpMethodFillerInner ):
	def _fillInSceneAutomaticSetUpMethodForElement( self, astStatements, astField, instance ):
		setNameMethod = ecc.dennisc.alice.ast.lookupMethod( edu.cmu.cs.dennisc.pattern.AbstractElement, "setName", [ java.lang.String ] )
		if isinstance( instance, apis.moveandturn.Transformable ):
			setNameExpression = alice.ast.FieldAccess( alice.ast.ThisExpression(), astField )

#			setLocalTransformationMethod = ecc.dennisc.alice.ast.lookupMethod( apis.moveandturn.AbstractTransformable, "setLocalTransformation", [ edu.cmu.cs.dennisc.math.AffineMatrix4x4 ] )
#			astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), setLocalTransformationMethod, [ ecc.dennisc.alice.ast.createAffineMatrix4x4( instance.getLocalTransformation() ) ] ) ] )

			setLocalPointOfViewMethod = ecc.dennisc.alice.ast.lookupMethod( apis.moveandturn.AbstractTransformable, "setLocalPointOfView", [ apis.moveandturn.PointOfView ] )
			astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), setLocalPointOfViewMethod, [ ecc.dennisc.alice.ast.createPointOfView( instance.getLocalTransformation() ) ] ) ] )

			addComponentMethod = ecc.dennisc.alice.ast.lookupMethod( apis.moveandturn.Composite, "addComponent", [ apis.moveandturn.Transformable ] )
			astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.ThisExpression(), addComponentMethod, [alice.ast.FieldAccess( alice.ast.ThisExpression(), astField )] ) ] )
			if isinstance( instance, apis.moveandturn.Model ):
				widthFactor = instance.getResizeWidthAmount()
				if edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( widthFactor, 1 ):
					pass
				else:
					resizeWidthMethod = ecc.dennisc.alice.ast.lookupMethod( apis.moveandturn.Transformable, "resizeWidth", [ java.lang.Number, java.lang.Number, apis.moveandturn.ResizePolicy ] )
					astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), resizeWidthMethod, [ alice.ast.DoubleLiteral( widthFactor ), alice.ast.DoubleLiteral( 0.0 ), ecc.dennisc.alice.ast.createEnumConstant(apis.moveandturn.ResizePolicy.PRESERVE_NOTHING) ] ) ] )
				heightFactor = instance.getResizeHeightAmount()
				if edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( heightFactor, 1 ):
					pass
				else:
					resizeHeightMethod = ecc.dennisc.alice.ast.lookupMethod( apis.moveandturn.Transformable, "resizeHeight", [ java.lang.Number, java.lang.Number, apis.moveandturn.ResizePolicy ] )
					astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), resizeHeightMethod, [ alice.ast.DoubleLiteral( heightFactor ), alice.ast.DoubleLiteral( 0.0 ), ecc.dennisc.alice.ast.createEnumConstant(apis.moveandturn.ResizePolicy.PRESERVE_NOTHING) ] ) ] )
				depthFactor = instance.getResizeDepthAmount()
				if edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( depthFactor, 1 ):
					pass
				else:
					resizeDepthMethod = ecc.dennisc.alice.ast.lookupMethod( apis.moveandturn.Transformable, "resizeDepth", [ java.lang.Number, java.lang.Number, apis.moveandturn.ResizePolicy ] )
					astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), resizeDepthMethod, [ alice.ast.DoubleLiteral( depthFactor ), alice.ast.DoubleLiteral( 0.0 ), ecc.dennisc.alice.ast.createEnumConstant(apis.moveandturn.ResizePolicy.PRESERVE_NOTHING) ] ) ] )
					
#				setColorMethod = ecc.dennisc.alice.ast.lookupMethod( apis.moveandturn.Model, "setColor", [ edu.cmu.cs.dennisc.color.Color4f ] )
#				astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), setColorMethod, [ ecc.dennisc.alice.ast.createColor4f( instance.getColor() ) ] ) ] )
				if isinstance( instance, apis.moveandturn.Text ):
					setValueMethod = ecc.dennisc.alice.ast.lookupMethod( apis.moveandturn.Text, "setValue", [ java.lang.String ] )
					setFontMethod = ecc.dennisc.alice.ast.lookupMethod( apis.moveandturn.Text, "setFont", [ apis.moveandturn.Font ] )
					setLetterHeightMethod = ecc.dennisc.alice.ast.lookupMethod( apis.moveandturn.Text, "setLetterHeight", [ java.lang.Number ] )
					astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), setValueMethod, [alice.ast.StringLiteral( instance.getValue() ) ] ) ] )
					astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), setFontMethod, [ecc.dennisc.alice.ast.createFont(instance.getFont()) ] ) ] )
					astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), setLetterHeightMethod, [alice.ast.DoubleLiteral( instance.getLetterHeight() ) ] ) ] )
#				elif isinstance( instance, apis.moveandturn.Cone ):
#					setLengthMethod = ecc.dennisc.alice.ast.lookupMethod( apis.moveandturn.Cone, "setLength", [ java.lang.Double ] )
#					setBaseRadiusMethod = ecc.dennisc.alice.ast.lookupMethod( apis.moveandturn.Cone, "setBaseRadius", [ java.lang.Double ] )
#					astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), setLengthMethod, [alice.ast.DoubleLiteral( instance.getLength() ) ] ) ] )
#					astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( alice.ast.FieldAccess( alice.ast.ThisExpression(), astField ), setBaseRadiusMethod, [alice.ast.DoubleLiteral( instance.getBaseRadius() ) ] ) ] )
		elif isinstance( instance, apis.moveandturn.Scene ):
			setNameExpression = alice.ast.ThisExpression()
			setAtmosphereColorMethod = ecc.dennisc.alice.ast.lookupMethod( apis.moveandturn.Scene, "setAtmosphereColor", [ apis.moveandturn.Color ] )
			astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( edu.cmu.cs.dennisc.alice.ast.ThisExpression(), setAtmosphereColorMethod, [ ecc.dennisc.alice.ast.createColor( instance.getAtmosphereColor() ) ] ) ] )
		astStatements.add( [ ecc.dennisc.alice.ast.createMethodInvocationStatement( setNameExpression, setNameMethod, [ alice.ast.StringLiteral( instance.getName() ) ] ) ] )

	def _fillInSceneAutomaticSetUpMethodForInstance( self, astStatements, astField, instance ):
		if isinstance( instance, apis.moveandturn.Element ):
			self._fillInSceneAutomaticSetUpMethodForElement( astStatements, astField, instance )

#print "<--", __name__
