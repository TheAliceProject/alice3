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

package org.alice.stageide.ast;

import org.lgna.story.EmployeesOnly;
import org.lgna.story.Orientation;
import org.lgna.story.PoseBuilder;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.implementation.PoseUtilities;

import edu.cmu.cs.dennisc.math.UnitQuaternion;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionCreator extends org.alice.ide.ast.ExpressionCreator {
	private org.lgna.project.ast.Expression createPositionExpression( org.lgna.story.Position position ) {
		if( position != null ) {
			Class<?> cls = org.lgna.story.Position.class;
			org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( cls, Number.class, Number.class, Number.class );
			return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, this.createDoubleExpression( position.getRight(), MILLI_DECIMAL_PLACES ), this.createDoubleExpression( position.getUp(), MILLI_DECIMAL_PLACES ), this.createDoubleExpression( position.getBackward(), MILLI_DECIMAL_PLACES ) );
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}

	private org.lgna.project.ast.Expression createOrientationExpression( org.lgna.story.Orientation orientation ) {
		if( orientation != null ) {
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 axes = org.lgna.story.EmployeesOnly.getOrthogonalMatrix3x3( orientation );
			edu.cmu.cs.dennisc.math.UnitQuaternion q = new edu.cmu.cs.dennisc.math.UnitQuaternion( axes );
			Class<?> cls = org.lgna.story.Orientation.class;
			org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( cls, Number.class, Number.class, Number.class, Number.class );
			return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, this.createDoubleExpression( q.x, MICRO_DECIMAL_PLACES ), this.createDoubleExpression( q.y, MICRO_DECIMAL_PLACES ), this.createDoubleExpression( q.z, MICRO_DECIMAL_PLACES ), this.createDoubleExpression( q.w, MICRO_DECIMAL_PLACES ) );
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}

	private org.lgna.project.ast.Expression createScaleExpression( org.lgna.story.Scale scale ) {
		if( scale != null ) {
			Class<?> cls = org.lgna.story.Scale.class;
			org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( cls, Number.class, Number.class, Number.class );
			return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, this.createDoubleExpression( scale.getLeftToRight(), MILLI_DECIMAL_PLACES ), this.createDoubleExpression( scale.getBottomToTop(), MILLI_DECIMAL_PLACES ), this.createDoubleExpression( scale.getFrontToBack(), MILLI_DECIMAL_PLACES ) );
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}

	private org.lgna.project.ast.Expression createSizeExpression( org.lgna.story.Size size ) {
		if( size != null ) {
			Class<?> cls = org.lgna.story.Size.class;
			org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( cls, Number.class, Number.class, Number.class );
			return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, this.createDoubleExpression( size.getLeftToRight(), MILLI_DECIMAL_PLACES ), this.createDoubleExpression( size.getBottomToTop(), MILLI_DECIMAL_PLACES ), this.createDoubleExpression( size.getFrontToBack(), MILLI_DECIMAL_PLACES ) );
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}

	private org.lgna.project.ast.Expression createFontExpression( org.lgna.story.Font font ) throws CannotCreateExpressionException {
		Class<?> cls = org.lgna.story.Font.class;
		org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( cls, org.lgna.story.fontattributes.Attribute[].class );
		return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, this.createExpression( font.getFamily() ), this.createExpression( font.getWeight() ), this.createExpression( font.getPosture() ) );
	}

	private org.lgna.project.ast.Expression createColorExpression( org.lgna.story.Color color ) {
		org.lgna.project.ast.Expression rv = null;
		Class<?> cls = org.lgna.story.Color.class;
		for( java.lang.reflect.Field fld : edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getPublicStaticFinalFields( cls, cls ) ) {
			if( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( fld, null ).equals( color ) ) {
				rv = this.createPublicStaticFieldAccess( fld );
				break;
			}
		}
		if( rv != null ) {
			//pass
		} else {
			org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( cls, Number.class, Number.class, Number.class );
			rv = org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, this.createDoubleExpression( color.getRed(), MILLI_DECIMAL_PLACES ), this.createDoubleExpression( color.getGreen(), MILLI_DECIMAL_PLACES ), this.createDoubleExpression( color.getBlue(), MILLI_DECIMAL_PLACES ) );
		}
		return rv;
	}

	private org.lgna.project.ast.Expression createImageSourceExpression( org.lgna.story.ImageSource imageSource ) {
		if( imageSource != null ) {
			org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance( org.lgna.story.ImageSource.class, org.lgna.common.resources.ImageResource.class );
			org.lgna.project.ast.Expression arg0Expression;
			org.lgna.common.resources.ImageResource imageResource = imageSource.getImageResource();
			if( imageResource != null ) {
				arg0Expression = new org.lgna.project.ast.ResourceExpression( org.lgna.common.resources.ImageResource.class, imageResource );
			} else {
				arg0Expression = new org.lgna.project.ast.NullLiteral();
			}
			return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, arg0Expression );
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}

	private org.lgna.project.ast.Expression createImagePaintExpression( org.lgna.story.ImagePaint imagePaint ) throws CannotCreateExpressionException {
		if( imagePaint != null ) {
			if( imagePaint instanceof Enum<?> ) {
				return this.createEnumExpression( (Enum<?>)imagePaint );
			} else {
				throw new CannotCreateExpressionException( imagePaint );
			}
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}

	private org.lgna.project.ast.Expression createPaintExpression( org.lgna.story.Paint paint ) throws CannotCreateExpressionException {
		if( paint != null ) {
			if( paint instanceof org.lgna.story.Color ) {
				org.lgna.story.Color color = (org.lgna.story.Color)paint;
				return createColorExpression( color );
			} else if( paint instanceof org.lgna.story.ImagePaint ) {
				org.lgna.story.ImagePaint imagePaint = (org.lgna.story.ImagePaint)paint;
				return this.createImagePaintExpression( imagePaint );
			} else if( paint instanceof org.lgna.story.ImageSource ) {
				org.lgna.story.ImageSource imageSource = (org.lgna.story.ImageSource)paint;
				return this.createImageSourceExpression( imageSource );
			} else {
				throw new CannotCreateExpressionException( paint );
			}
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}

	private org.lgna.project.ast.Expression createJointIdExpression( org.lgna.story.resources.JointId jointId ) throws CannotCreateExpressionException {
		java.lang.reflect.Field fld = jointId.getPublicStaticFinalFld();
		if( fld != null ) {
			return this.createPublicStaticFieldAccess( fld );
		} else {
			throw new CannotCreateExpressionException( jointId );
		}
	}

	//private static final org.lgna.project.ast.JavaMethod ADD_CUSTOM = org.lgna.project.ast.JavaMethod.getInstance( PoseBuilder.class, "arbitraryJoint", JointId.class, Orientation.class );
	private static final org.lgna.project.ast.JavaMethod BUILD = org.lgna.project.ast.JavaMethod.getInstance( PoseBuilder.class, "build" );

	private org.lgna.project.ast.Expression createPoseExpression( org.lgna.story.Pose pose ) throws CannotCreateExpressionException {
		if( ( pose != null ) && ( EmployeesOnly.getJointIdTransformationPairs( pose ).length > 0 ) ) {
			Class<? extends PoseBuilder> builderCls = PoseUtilities.getBuilderClassForPoseClass( pose.getClass() );
			org.lgna.project.ast.InstanceCreation builderExpression0 = org.lgna.project.ast.AstUtilities.createInstanceCreation( builderCls );
			org.lgna.project.ast.Expression prevExpression = null;
			for( JointIdTransformationPair jtPair : EmployeesOnly.getJointIdTransformationPairs( pose ) ) {

				//NOTE: this does not take into account that poses may affect translation as well.
				//TODO: check jtPair.affectsTranslation() to see if creating a different pose entry is necessary
				UnitQuaternion q = jtPair.getTransformation().orientation.createUnitQuaternion();
				Orientation orientation = new org.lgna.story.Orientation( q.x, q.y, q.z, q.w );

				org.lgna.project.ast.Expression callerExpression = prevExpression == null ? builderExpression0 : prevExpression;
				java.lang.reflect.Method jSpecificMethod = PoseUtilities.getSpecificPoseBuilderMethod( builderCls, jtPair.getJointId() );
				if( jSpecificMethod != null ) {
					prevExpression = org.lgna.project.ast.AstUtilities.createMethodInvocation( callerExpression, org.lgna.project.ast.JavaMethod.getInstance( jSpecificMethod ), this.createOrientationExpression( orientation ) );
				} else {
					java.lang.reflect.Method jCatchAllMethod = PoseUtilities.getCatchAllPoseBuilderMethod( builderCls );
					if( jCatchAllMethod != null ) {
						prevExpression = org.lgna.project.ast.AstUtilities.createMethodInvocation( callerExpression, org.lgna.project.ast.JavaMethod.getInstance( jCatchAllMethod ), this.createJointIdExpression( jtPair.getJointId() ), this.createOrientationExpression( orientation ) );
					} else {
						//should not happen
						//throw new CannotCreateExpressionException( pose );
						throw new Error( "cannot find catch all method for pose builder " + pose + " " + builderCls );
					}
				}
			}
			assert prevExpression != null;
			return org.lgna.project.ast.AstUtilities.createMethodInvocation( prevExpression, BUILD );
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}

	private org.lgna.project.ast.Expression createOutfitExpression( org.lgna.story.resources.sims2.Outfit outfit ) throws CannotCreateExpressionException {
		if( outfit != null ) {
			if( outfit instanceof org.lgna.story.resources.sims2.TopAndBottomOutfit<?, ?> ) {
				org.lgna.story.resources.sims2.TopAndBottomOutfit<?, ?> topAndBottomOutfit = (org.lgna.story.resources.sims2.TopAndBottomOutfit<?, ?>)outfit;
				org.lgna.story.resources.sims2.TopPiece topPiece = topAndBottomOutfit.getTopPiece();
				org.lgna.story.resources.sims2.BottomPiece bottomPiece = topAndBottomOutfit.getBottomPiece();

				org.lgna.project.ast.JavaType type = org.lgna.project.ast.JavaType.getInstance( outfit.getClass() );
				org.lgna.project.ast.JavaConstructor constructor = type.getDeclaredConstructors().get( 0 );
				java.util.List<org.lgna.project.ast.JavaConstructorParameter> parameters = constructor.getRequiredParameters();
				if( parameters.size() == 2 ) {
					if( parameters.get( 0 ).getValueType().isAssignableFrom( topPiece.getClass() ) ) {
						if( parameters.get( 1 ).getValueType().isAssignableFrom( bottomPiece.getClass() ) ) {
							org.lgna.project.ast.Expression topExpression = this.createExpression( topPiece );
							org.lgna.project.ast.Expression bottomExpression = this.createExpression( bottomPiece );
							return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, topExpression, bottomExpression );
						}
					}
				}
				throw new CannotCreateExpressionException( outfit );
			} else {
				if( outfit.getClass().isEnum() ) {
					return this.createEnumExpression( (Enum<? extends org.lgna.story.resources.sims2.Outfit>)outfit );
				} else {
					throw new CannotCreateExpressionException( outfit );
				}
			}
		} else {
			return new org.lgna.project.ast.NullLiteral();
		}
	}

	@Override
	protected org.lgna.project.ast.Expression createCustomExpression( Object value ) throws CannotCreateExpressionException {

		if( value instanceof org.lgna.story.Position ) {
			return this.createPositionExpression( (org.lgna.story.Position)value );
		} else if( value instanceof org.lgna.story.Orientation ) {
			return this.createOrientationExpression( (org.lgna.story.Orientation)value );
		} else if( value instanceof org.lgna.story.Scale ) {
			return this.createScaleExpression( (org.lgna.story.Scale)value );
		} else if( value instanceof org.lgna.story.Size ) {
			return this.createSizeExpression( (org.lgna.story.Size)value );
		} else if( value instanceof org.lgna.story.Paint ) {
			return this.createPaintExpression( (org.lgna.story.Paint)value );
		} else if( value instanceof org.lgna.story.Font ) {
			return this.createFontExpression( (org.lgna.story.Font)value );
			//		} else if( value instanceof org.lgna.ik.poser.Pose ) {
			//			return this.createPoseExpression( (org.lgna.ik.poser.Pose)value );
		} else if( value instanceof org.lgna.story.Pose<?> ) {
			return this.createPoseExpression( (org.lgna.story.Pose<?>)value );
		} else if( value instanceof org.lgna.story.resources.sims2.TopAndBottomOutfit<?, ?> ) {
			return this.createOutfitExpression( (org.lgna.story.resources.sims2.TopAndBottomOutfit<?, ?>)value );
		} else if( value instanceof org.lgna.story.resources.JointId ) {
			return this.createJointIdExpression( (org.lgna.story.resources.JointId)value );
		} else {
			throw new CannotCreateExpressionException( value );
		}

	}
}
