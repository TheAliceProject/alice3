/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.stageide.sceneeditor;

import org.alice.apis.moveandturn.AsSeenBy;
import org.alice.apis.moveandturn.CameraMarker;
import org.alice.apis.moveandturn.gallery.environments.Ground;
import org.alice.apis.moveandturn.gallery.environments.grounds.DirtGround;
import org.alice.apis.moveandturn.gallery.environments.grounds.GrassyGround;
import org.alice.apis.moveandturn.gallery.environments.grounds.MoonSurface;
import org.alice.apis.moveandturn.gallery.environments.grounds.SandyGround;
import org.alice.apis.moveandturn.gallery.environments.grounds.SeaSurface;
import org.alice.apis.moveandturn.gallery.environments.grounds.SnowyGround;
import org.alice.interact.AbstractDragAdapter;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;

/**
 * @author Dennis Cosgrove
 */
public class SetUpMethodGenerator {
	private static edu.cmu.cs.dennisc.alice.ast.Expression createInstanceExpression( boolean isThis, edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		edu.cmu.cs.dennisc.alice.ast.Expression thisExpression = new edu.cmu.cs.dennisc.alice.ast.ThisExpression();
		if( isThis ) {
			return thisExpression;
		} else {
			return new edu.cmu.cs.dennisc.alice.ast.FieldAccess( thisExpression, field );
		}
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( Double d ) {
		return new edu.cmu.cs.dennisc.alice.ast.DoubleLiteral( d );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( String s ) {
		return new edu.cmu.cs.dennisc.alice.ast.StringLiteral( s );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( Enum e ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( e.getClass() );
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = type.getDeclaredField( type, e.name() );
		return new edu.cmu.cs.dennisc.alice.ast.FieldAccess( new edu.cmu.cs.dennisc.alice.ast.TypeExpression( type ), field );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.font.Attribute attribute ) {
		if( attribute instanceof Enum ) {
			return createExpression( (Enum)attribute );
		} else {
			throw new RuntimeException( "todo handle org.alice.apis.moveandturn.font.Attribute that is not an Enum" );
		}
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.ImageSource imageSource ) {
		if( imageSource != null ) {
			edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( org.alice.apis.moveandturn.ImageSource.class, org.alice.virtualmachine.resources.ImageResource.class );
			edu.cmu.cs.dennisc.alice.ast.Expression arg0Expression;
			org.alice.virtualmachine.resources.ImageResource imageResource = imageSource.getImageResource();
			if( imageResource != null ) {
				arg0Expression = new edu.cmu.cs.dennisc.alice.ast.ResourceExpression( org.alice.virtualmachine.resources.ImageResource.class, imageResource );
			} else {
				arg0Expression = new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
			}
			return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, arg0Expression );
		} else {
			return new edu.cmu.cs.dennisc.alice.ast.NullLiteral();
		}
	}

	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.Color color ) {
		edu.cmu.cs.dennisc.alice.ast.Expression rv = null;
		Class< ? > cls = org.alice.apis.moveandturn.Color.class;
		for( java.lang.reflect.Field fld : edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getPublicStaticFinalFields( cls, cls ) ) {
			if( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( fld, null ).equals( color ) ) {
				edu.cmu.cs.dennisc.alice.ast.TypeExpression typeExpression = new edu.cmu.cs.dennisc.alice.ast.TypeExpression( cls );
				edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField field = edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField.get( fld );
				rv = new edu.cmu.cs.dennisc.alice.ast.FieldAccess( typeExpression, field );
				break;
			}
		}
		if( rv != null ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( cls, Number.class, Number.class, Number.class );
			rv = org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createExpression( color.getRed() ), createExpression( color.getGreen() ), createExpression( color.getBlue() ) );
		}
		return rv;
	}

	private static edu.cmu.cs.dennisc.alice.ast.Expression createPositionExpression( edu.cmu.cs.dennisc.math.Point3 translation ) {
		Class< ? > cls = org.alice.apis.moveandturn.Position.class;
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( cls, Number.class, Number.class, Number.class );
		return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createExpression( translation.x ), createExpression( translation.y ), createExpression( translation.z ) );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createQuaternionExpression( edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 orientation ) {
		edu.cmu.cs.dennisc.math.UnitQuaternion q = new edu.cmu.cs.dennisc.math.UnitQuaternion( orientation );
		Class< ? > cls = org.alice.apis.moveandturn.Quaternion.class;
		java.lang.reflect.Constructor< ? > cnstrctr = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getConstructor( cls, Number.class, Number.class, Number.class, Number.class );
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( cnstrctr );
		return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createExpression( q.x ), createExpression( q.y ), createExpression( q.z ), createExpression( q.w ) );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.PointOfView pointOfView ) {
		Class< ? > cls = org.alice.apis.moveandturn.PointOfView.class;
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( cls, org.alice.apis.moveandturn.Orientation.class, org.alice.apis.moveandturn.Position.class );
		return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createQuaternionExpression( pointOfView.getInternal().orientation ), createPositionExpression( pointOfView.getInternal().translation ) );
	}
	private static edu.cmu.cs.dennisc.alice.ast.Expression createExpression( org.alice.apis.moveandturn.Font font ) {
		Class< ? > cls = org.alice.apis.moveandturn.Font.class;
		edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( cls, org.alice.apis.moveandturn.font.Attribute[].class );
		return org.alice.ide.ast.NodeUtilities.createInstanceCreation( constructor, createExpression( font.getFamily() ), createExpression( font.getWeight() ), createExpression( font.getPosture() ) );
	}

	private static edu.cmu.cs.dennisc.alice.ast.ExpressionStatement createStatement( Class< ? > declarationCls, String methodName, Class< ? > parameterCls, edu.cmu.cs.dennisc.alice.ast.Expression instanceExpression, edu.cmu.cs.dennisc.alice.ast.Expression argumentExpression ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = org.alice.ide.ast.NodeUtilities.lookupMethod( declarationCls, methodName, parameterCls );
		return org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpression );
	}
	private static edu.cmu.cs.dennisc.alice.ast.ExpressionStatement createStatement( Class< ? > declarationCls, String methodName, Class< ? >[] parameterClses, edu.cmu.cs.dennisc.alice.ast.Expression instanceExpression, edu.cmu.cs.dennisc.alice.ast.Expression... argumentExpressions ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = org.alice.ide.ast.NodeUtilities.lookupMethod( declarationCls, methodName, parameterClses );
		return org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement( instanceExpression, method, argumentExpressions );
	}

	public static void fillInAutomaticPointOfViewAssignment(edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty, edu.cmu.cs.dennisc.alice.ast.AbstractField field, edu.cmu.cs.dennisc.alice.ast.AbstractField pointOfViewField)
	{
		if (pointOfViewField != null)
		{
			bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.AbstractTransformable.class, "moveAndOrientTo", new Class< ? >[] {org.alice.apis.moveandturn.ReferenceFrame.class, Number.class}, SetUpMethodGenerator.createInstanceExpression( false, field ), SetUpMethodGenerator.createInstanceExpression( false, pointOfViewField ), createExpression( 0.0 ) ) );
		}
	}
	
	public static void fillInAutomaticSetUpMethod( edu.cmu.cs.dennisc.alice.ast.StatementListProperty bodyStatementsProperty, boolean isThis, edu.cmu.cs.dennisc.alice.ast.AbstractField field, Object instance ) {
		if( instance instanceof org.alice.apis.moveandturn.Element ) {
			org.alice.apis.moveandturn.Element element = (org.alice.apis.moveandturn.Element)instance;
			bodyStatementsProperty.add( createStatement( edu.cmu.cs.dennisc.pattern.AbstractElement.class, "setName", String.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( field.getName() ) ) );
			if( instance instanceof org.alice.apis.moveandturn.Transformable ) {
				org.alice.apis.moveandturn.Transformable transformable = (org.alice.apis.moveandturn.Transformable)element;
				if (transformable instanceof CameraMarker)
				{
					//CameraMarkers are all fields of the Scene but may be parented to the camera while being edited in the scene editor. Because of this, make sure to get their LocalPointOfView as seen by the Scene
					bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.AbstractTransformable.class, "setLocalPointOfView", org.alice.apis.moveandturn.PointOfView.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( transformable.getPointOfView(AsSeenBy.SCENE) ) ) );
				}
				else
				{
					bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.AbstractTransformable.class, "setLocalPointOfView", org.alice.apis.moveandturn.PointOfView.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( transformable.getLocalPointOfView() ) ) );
				}
				if( instance instanceof org.alice.apis.moveandturn.AbstractModel ) {
					org.alice.apis.moveandturn.AbstractModel abstractModel = (org.alice.apis.moveandturn.AbstractModel)transformable;

					double widthFactor = abstractModel.getResizeWidthAmount();
					if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( widthFactor, 1 ) ) {
						//pass
					} else {
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Transformable.class, "resizeWidth", new Class< ? >[] { Number.class, Number.class, org.alice.apis.moveandturn.ResizePolicy.class }, SetUpMethodGenerator.createInstanceExpression( isThis, field ), createExpression( widthFactor ), createExpression( 0.0 ), createExpression( org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING ) ) );
					}
					double heightFactor = abstractModel.getResizeHeightAmount();
					if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( heightFactor, 1 ) ) {
						//pass
					} else {
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Transformable.class, "resizeHeight", new Class< ? >[] { Number.class, Number.class, org.alice.apis.moveandturn.ResizePolicy.class }, SetUpMethodGenerator.createInstanceExpression( isThis, field ), createExpression( heightFactor ), createExpression( 0.0 ), createExpression( org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING ) ) );
					}
					double depthFactor = abstractModel.getResizeDepthAmount();
					if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( depthFactor, 1 ) ) {
						//pass
					} else {
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Transformable.class, "resizeDepth", new Class< ? >[] { Number.class, Number.class, org.alice.apis.moveandturn.ResizePolicy.class }, SetUpMethodGenerator.createInstanceExpression( isThis, field ), createExpression( depthFactor ), createExpression( 0.0 ), createExpression( org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING ) ) );
					}
					if( instance instanceof org.alice.apis.moveandturn.Model ) {
						org.alice.apis.moveandturn.Model model = (org.alice.apis.moveandturn.Model)transformable;

						if( model instanceof org.alice.apis.moveandturn.Text ) {
							org.alice.apis.moveandturn.Text text = (org.alice.apis.moveandturn.Text)model;
							bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Text.class, "setValue", String.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( text.getValue() ) ) );
							bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Text.class, "setFont", org.alice.apis.moveandturn.Font.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( text.getFont() ) ) );
							bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Text.class, "setLetterHeight", Number.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( text.getLetterHeight() ) ) );
						} else if( model instanceof org.alice.apis.stage.Person ) {
							org.alice.apis.stage.Person person = (org.alice.apis.stage.Person)model;
							if( person instanceof org.alice.apis.stage.MaleAdult || person instanceof org.alice.apis.stage.FemaleAdult ) {
								//pass
							} else {
								bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setGender", org.alice.apis.stage.Gender.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( person.getGender() ) ) );
							}

							bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setHair", org.alice.apis.stage.Hair.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( (Enum)person.getHair() ) ) );
							bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setEyeColor", org.alice.apis.stage.EyeColor.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( (Enum)person.getEyeColor() ) ) );
							bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setSkinTone", org.alice.apis.stage.SkinTone.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( (Enum)person.getSkinTone() ) ) );
							bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setOutfit", org.alice.apis.stage.Outfit.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( (Enum)person.getOutfit() ) ) );
							bodyStatementsProperty.add( createStatement( org.alice.apis.stage.Person.class, "setFitnessLevel", Number.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( person.getFitnessLevel() ) ) );

						}
					} else if( instance instanceof org.alice.apis.moveandturn.Billboard ) {
						org.alice.apis.moveandturn.Billboard billboard = (org.alice.apis.moveandturn.Billboard)transformable;
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Billboard.class, "setFrontImageSource", org.alice.apis.moveandturn.ImageSource.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( billboard.getFrontImageSource() ) ) );
						bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Billboard.class, "setBackImageSource", org.alice.apis.moveandturn.ImageSource.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( billboard.getBackImageSource() ) ) );
					}
				}
				bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Composite.class, "addComponent", org.alice.apis.moveandturn.Transformable.class, new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), SetUpMethodGenerator.createInstanceExpression( isThis, field ) ) );
			} else if( instance instanceof org.alice.apis.moveandturn.Scene ) {
				org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)element;
				bodyStatementsProperty.add( createStatement( org.alice.apis.moveandturn.Scene.class, "setAtmosphereColor", org.alice.apis.moveandturn.Color.class, SetUpMethodGenerator.createInstanceExpression( isThis, field ), SetUpMethodGenerator.createExpression( scene.getAtmosphereColor() ) ) );
			}
		}
	}
}
