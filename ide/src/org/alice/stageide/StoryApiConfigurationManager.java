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

package org.alice.stageide;

/**
 * @author Dennis Cosgrove
 */
public class StoryApiConfigurationManager extends org.alice.ide.ApiConfigurationManager {
	private static class SingletonHolder {
		private static StoryApiConfigurationManager instance = new StoryApiConfigurationManager();
	}
	public static StoryApiConfigurationManager getInstance() {
		return SingletonHolder.instance;
	}
	
	@Override
	protected boolean isNamedUserTypesAcceptableForGallery( org.lgna.project.ast.NamedUserType type ) {
		return type.isAssignableTo( org.lgna.story.Model.class );
	}
	@Override
	protected boolean isNamedUserTypesAcceptableForSelection( org.lgna.project.ast.NamedUserType type ) {
		return type.isAssignableTo( org.lgna.story.Program.class ) == false || org.alice.ide.croquet.models.ui.preferences.IsIncludingProgramType.getInstance().getValue();
	}
	private final org.alice.stageide.ast.ExpressionCreator expressionCreator = new org.alice.stageide.ast.ExpressionCreator();
	private StoryApiConfigurationManager() {
		org.alice.ide.common.BeveledShapeForType.addRoundType( org.lgna.story.Entity.class );
		final int SMALL_ICON_SIZE = 24;
		org.alice.stageide.gallerybrowser.ResourceManager.registerSmallIcon( org.lgna.story.Sun.class, new javax.swing.Icon() {

			public int getIconWidth() {
				return SMALL_ICON_SIZE;
			}
			public int getIconHeight() {
				return SMALL_ICON_SIZE;
			}
			
			private java.awt.Shape createArc( float size ) {
				java.awt.geom.GeneralPath rv = new java.awt.geom.GeneralPath();
				rv.moveTo( 0.0f, 0.0f );
				rv.lineTo( size, 0.0f );
				rv.quadTo( size, size, 0.0f, size );
				rv.closePath();
				return rv;
			}
			public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				java.awt.geom.AffineTransform m = g2.getTransform();
				Object prevAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
				try {
					java.awt.Shape innerArc = this.createArc( 16.0f );
					java.awt.Shape outerArc = this.createArc( 18.0f );
					
					g2.translate( 4.0f, 4.0f );
					java.awt.geom.GeneralPath pathRays = new java.awt.geom.GeneralPath();
					double thetaN = Math.PI/2.0;
					double thetaDelta = thetaN/8.0;
					g2.setColor( new java.awt.Color( 255, 210, 0 ) );
					for( double theta = 0.0; theta<=thetaN; theta += thetaDelta ) {
						pathRays.moveTo( 0.0f, 0.0f );
						pathRays.lineTo( (float)( Math.cos( theta ) * 20.0 ), (float)( Math.sin( theta ) * 20.0 ) ); 
					}
					g2.draw( pathRays );
					g2.fill( outerArc );

					g2.setColor( new java.awt.Color( 230, 230, 0 ) );
					g2.fill( innerArc );
				} finally {
					g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
					g2.setTransform( m );
				}
			}
		} );
		
		org.alice.stageide.gallerybrowser.ResourceManager.registerSmallIcon( org.lgna.story.Ground.class, new javax.swing.Icon() {
			public int getIconWidth() {
				return SMALL_ICON_SIZE;
			}
			public int getIconHeight() {
				return SMALL_ICON_SIZE;
			}
			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
				g.setColor( java.awt.Color.DARK_GRAY );
				g.drawRect( x+2, y+2, SMALL_ICON_SIZE-5, SMALL_ICON_SIZE-5 );
			}
		} );

		final java.awt.Stroke BONE_STROKE = new java.awt.BasicStroke( 3.0f );
		final java.awt.Stroke JOINT_OUTLINE_STROKE = new java.awt.BasicStroke( 1.0f );
		final int INSET_X = 6;
		final int INSET_Y = 6;
		final int JOINT_A_X = INSET_X;
		final int JOINT_A_Y = INSET_Y;
		final int JOINT_B_X = 3*SMALL_ICON_SIZE/4;
		final int JOINT_B_Y = 2*SMALL_ICON_SIZE/5;
		final int JOINT_C_X = SMALL_ICON_SIZE/2;
		final int JOINT_C_Y = SMALL_ICON_SIZE-INSET_Y;
		final int JOINT_WIDTH = 6;
		final int JOINT_HEIGHT = 6;
		org.alice.stageide.gallerybrowser.ResourceManager.registerSmallIcon( org.lgna.story.Joint.class, new javax.swing.Icon() {
			public int getIconWidth() {
				return SMALL_ICON_SIZE;
			}
			public int getIconHeight() {
				return SMALL_ICON_SIZE;
			}
			private void drawJoint( java.awt.Graphics2D g2, int x, int y, java.awt.Paint fillPaint, java.awt.Paint outlinePaint ) {
				if( fillPaint != null ) {
					g2.setPaint( fillPaint );
					g2.fillOval( x-4, y-4, JOINT_WIDTH, JOINT_HEIGHT );
				}
				if( outlinePaint != null ) {
					g2.setPaint( outlinePaint );
					g2.drawOval( x-4, y-4, JOINT_WIDTH, JOINT_HEIGHT );
				}

			}
			public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				Object prevAntialiasing = g2.getRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING );
				java.awt.Stroke prevStroke = g2.getStroke();
				g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
				try {
					g2.setStroke( BONE_STROKE );
					g2.drawLine( JOINT_A_X, JOINT_A_Y, JOINT_B_X, JOINT_B_Y );
					g2.drawLine( JOINT_B_X, JOINT_B_Y, JOINT_C_X, JOINT_C_Y );
					g2.setStroke( JOINT_OUTLINE_STROKE );
					this.drawJoint( g2, JOINT_B_X, JOINT_B_Y, java.awt.Color.RED, java.awt.Color.BLACK );
					this.drawJoint( g2, JOINT_A_X, JOINT_A_Y, java.awt.Color.LIGHT_GRAY, java.awt.Color.BLACK );
					this.drawJoint( g2, JOINT_C_X, JOINT_C_Y, java.awt.Color.LIGHT_GRAY, java.awt.Color.BLACK );
				} finally {
					g2.setStroke( prevStroke );
					g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, prevAntialiasing );
				}
			}
		} );
		org.alice.stageide.gallerybrowser.ResourceManager.registerSmallIcon( org.lgna.story.Camera.class, edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( org.alice.stageide.gallerybrowser.ResourceManager.class.getResource( "images/camera-video.png" ) ) );
//		org.alice.stageide.gallerybrowser.ResourceManager.registerSmallIcon( org.lookingglassandalice.storytelling.Camera.class, new javax.swing.Icon() {
//			public int getIconWidth() {
//				return SMALL_ICON_SIZE;
//			}
//			public int getIconHeight() {
//				return SMALL_ICON_SIZE;
//			}
//			public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
//				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//				java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
//				path.moveTo( 4,4 );
//				path.lineTo( 20, 4 );
//				path.lineTo( 20, 12 );
//				path.lineTo( 28, 8 );
//				path.lineTo( 28, 20 );
//				path.lineTo( 20, 16 );
//				path.lineTo( 20, 24 );
//				path.lineTo( 4, 24 );
//				path.closePath();
//				g2.setColor( java.awt.Color.GRAY );
//				g2.fill( path );
//				g2.setColor( java.awt.Color.BLACK );
//				g2.draw( path );
//			}
//		} );
	}
	@Override
	public boolean isDeclaringTypeForManagedFields( org.lgna.project.ast.UserType< ? > type ) {
		return type.isAssignableTo( org.lgna.story.Scene.class );
	}
	@Override
	public boolean isInstanceFactoryDesiredForType( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		return type.isAssignableTo( org.lgna.story.Entity.class );
	}
	@Override
	public java.util.List< org.lgna.project.ast.JavaType > getTopLevelGalleryTypes() {
		return org.lgna.story.resourceutilities.StorytellingResources.getInstance().getTopLevelGalleryTypes();
	}
	private static final org.lgna.project.ast.JavaType BIPED_RESOURCE_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.resources.BipedResource.class );
	private static final org.lgna.project.ast.JavaType PERSON_RESOURCE_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.resources.sims2.PersonResource.class );
	@Override
	public org.lgna.project.ast.AbstractType< ?, ?, ? > getGalleryResourceParentFor( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		if( type == PERSON_RESOURCE_TYPE ) {
			return BIPED_RESOURCE_TYPE;
		} else {
			return org.lgna.story.resourceutilities.StorytellingResources.getInstance().getGalleryResourceParentFor( type );
		}
	}
	
	@Override
	public java.util.List< org.lgna.project.ast.AbstractDeclaration > getGalleryResourceChildrenFor( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		java.util.List< org.lgna.project.ast.AbstractDeclaration > rv = org.lgna.story.resourceutilities.StorytellingResources.getInstance().getGalleryResourceChildrenFor(type);
		if( type == BIPED_RESOURCE_TYPE ) {
			rv.add( 0, PERSON_RESOURCE_TYPE );
		}
		return rv;
	}
	@Override
	public org.lgna.croquet.CascadeMenuModel< org.alice.ide.instancefactory.InstanceFactory > getInstanceFactorySubMenuForThis( org.lgna.project.ast.AbstractType< ?,?,? > type ) {
		if( org.alice.stageide.ast.JointedTypeInfo.isJointed( type ) ) {
			return org.alice.stageide.instancefactory.croquet.joint.all.ThisJointedTypeMenuModel.getInstance( type );
		} else {
			return null;
		}
	}
	
	@Override
	public org.lgna.croquet.CascadeMenuModel< org.alice.ide.instancefactory.InstanceFactory > getInstanceFactorySubMenuForThisFieldAccess( org.lgna.project.ast.UserField field ) {
		org.lgna.project.ast.AbstractType< ?,?,? > type = field.getValueType();
		if( org.alice.stageide.ast.JointedTypeInfo.isJointed( type ) ) {
			return org.alice.stageide.instancefactory.croquet.joint.all.ThisFieldAccessJointedTypeMenuModel.getInstance( field );
		} else {
			return null;
		}
//		return org.alice.stageide.instancefactory.croquet.joint.declaration.ThisFieldAccessJointedTypeMenuModel.getMenuModel( field );
	}
	@Override
	public org.lgna.croquet.CascadeMenuModel< org.alice.ide.instancefactory.InstanceFactory > getInstanceFactorySubMenuForParameterAccess( org.lgna.project.ast.UserParameter parameter ) {
		org.lgna.project.ast.AbstractType< ?,?,? > type = parameter.getValueType();
		if( org.alice.stageide.ast.JointedTypeInfo.isJointed( type ) ) {
			return org.alice.stageide.instancefactory.croquet.joint.all.ParameterAccessJointedTypeMenuModel.getInstance( parameter );
		} else {
			return null;
		}
	}
	@Override
	public org.lgna.croquet.CascadeMenuModel< org.alice.ide.instancefactory.InstanceFactory > getInstanceFactorySubMenuForLocalAccess( org.lgna.project.ast.UserLocal local ) {
		org.lgna.project.ast.AbstractType< ?,?,? > type = local.getValueType();
		if( org.alice.stageide.ast.JointedTypeInfo.isJointed( type ) ) {
			return org.alice.stageide.instancefactory.croquet.joint.all.LocalAccessJointedTypeMenuModel.getInstance( local );
		} else {
			return null;
		}
	}

	@Override
	public org.lgna.project.ast.AbstractConstructor getGalleryResourceConstructorFor( org.lgna.project.ast.AbstractType< ?, ?, ? > argumentType ) {
		java.util.List< org.lgna.project.ast.NamedUserType > types = org.alice.ide.typemanager.TypeManager.getNamedUserTypesFromSuperTypes( getTopLevelGalleryTypes() );
		for( org.lgna.project.ast.AbstractType< ?, ?, ? > type : types ) {
			org.lgna.project.ast.AbstractConstructor constructor = type.getDeclaredConstructors().get( 0 );
			java.util.ArrayList< ? extends org.lgna.project.ast.AbstractParameter > parameters = constructor.getRequiredParameters();
			if( parameters.size() == 1 ) {
				if( parameters.get( 0 ).getValueType().isAssignableFrom( argumentType ) ) {
					return constructor;
				}
			}
		}
		return null;
	}

	protected org.alice.ide.ast.components.DeclarationNameLabel createDeclarationNameLabel( org.lgna.project.ast.AbstractField field ) {
		//todo: better name
		class ThisFieldAccessNameLabel extends org.alice.ide.ast.components.DeclarationNameLabel {
			public ThisFieldAccessNameLabel( org.lgna.project.ast.AbstractField field ) {
				super( field );
			}
			@Override
			protected String getNameText() {
				if( org.alice.ide.croquet.models.ui.preferences.IsIncludingThisForFieldAccessesState.getInstance().getValue() ) {
					return "this." + super.getNameText();
				} else {
					return super.getNameText();
				}
			}
		}
		return new ThisFieldAccessNameLabel( field );
	}

	@Override
	public org.lgna.croquet.components.JComponent< ? > createReplacementForFieldAccessIfAppropriate( org.lgna.project.ast.FieldAccess fieldAccess ) {
		org.lgna.project.ast.Expression fieldExpression = fieldAccess.expression.getValue();
		if( fieldExpression instanceof org.lgna.project.ast.ThisExpression || fieldExpression instanceof org.alice.ide.ast.CurrentThisExpression ) {
			org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
			org.lgna.project.ast.AbstractType< ?,?,? > declaringType = field.getDeclaringType();
			if( declaringType != null && declaringType.isAssignableTo( org.lgna.story.Scene.class ) ) {
				if( field.getValueType().isAssignableTo( org.lgna.story.Entity.class ) ) {
					return this.createDeclarationNameLabel( field );
				}
			}
		}
		return null;
	}
	@Override
	public org.lgna.croquet.CascadeItem< ?, ? > getCustomFillInFor( org.lgna.project.annotations.ValueDetails< ? > valueDetails ) {
		if( valueDetails instanceof org.lgna.story.annotation.PortionDetails ) {
			return org.alice.stageide.croquet.models.custom.CustomPortionInputDialogOperation.getInstance().getFillIn();
		} else {
			return null;
		}
	}
	@Override
	public org.alice.ide.ast.ExpressionCreator getExpressionCreator() {
		return this.expressionCreator;
	}

	
	@Override
	public boolean isSignatureLocked( org.lgna.project.ast.AbstractCode code ) {
		//todo: check to see if only referenced from Program and Program type is hidden
		return super.isSignatureLocked( code ) || "myFirstMethod".equalsIgnoreCase( code.getName() );
	}
	
	@Override
	protected java.util.List< ? super org.lgna.project.ast.JavaType > addPrimeTimeJavaTypes( java.util.List< ? super org.lgna.project.ast.JavaType > rv ) {
		rv = super.addPrimeTimeJavaTypes( rv );
//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Model.class ) );
//		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Biped.class ) );
		return rv;
	}

	@Override
	protected java.util.List<? super org.lgna.project.ast.JavaType> addSecondaryJavaTypes(java.util.List<? super org.lgna.project.ast.JavaType> rv) {
		super.addSecondaryJavaTypes(rv);
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Color.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.MoveDirection.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.TurnDirection.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.RollDirection.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Joint.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Marker.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.ObjectMarker.class ) );
		rv.add( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.CameraMarker.class ) );
		return rv;
	}
	
	private static final org.lgna.project.ast.JavaType JOINTED_MODEL_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.JointedModel.class );
	@Override
	public org.lgna.project.ast.UserType< ? > augmentTypeIfNecessary( org.lgna.project.ast.UserType< ? > rv ) {
		if( JOINTED_MODEL_TYPE.isAssignableFrom( rv ) ) {
			org.lgna.project.ast.AbstractConstructor constructor0 = org.alice.ide.typemanager.ConstructorArgumentUtilities.getContructor0( rv );
			org.lgna.project.ast.AbstractType< ?,?,? > resourceType = org.alice.ide.typemanager.ConstructorArgumentUtilities.getParameter0Type( constructor0 );
			if( resourceType != null ) {
				//pass
			} else {
				org.lgna.project.ast.JavaField field = org.alice.ide.typemanager.ConstructorArgumentUtilities.getArgumentField( constructor0 );
				if( field != null ) {
					resourceType = field.getValueType();
				}
			}
			if( resourceType != null ) {
				org.lgna.project.ast.JavaType ancestorType = rv.getFirstEncounteredJavaType();
				if( resourceType == org.alice.ide.typemanager.ConstructorArgumentUtilities.getContructor0Parameter0Type( ancestorType ) ) {
					//skip
				} else {
					org.lgna.project.ast.JavaMethod getJointMethod = JOINTED_MODEL_TYPE.getDeclaredMethod( "getJoint", org.lgna.story.resources.JointId.class );
					for( org.lgna.project.ast.AbstractField field : resourceType.getDeclaredFields() ) {
						if( field.isStatic() ) {
							if( field.getValueType().isAssignableTo( org.lgna.story.resources.JointId.class ) ) {
								org.lgna.project.ast.UserMethod method = org.lgna.project.ast.AstUtilities.createFunction( org.alice.ide.identifier.IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName( field.getName(), "get" ), org.lgna.story.Joint.class );
								method.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.GENERATED );
								org.lgna.project.ast.BlockStatement body = method.body.getValue();
								org.lgna.project.ast.Expression expression = org.lgna.project.ast.AstUtilities.createMethodInvocation( 
										new org.lgna.project.ast.ThisExpression(), 
										getJointMethod, 
										org.lgna.project.ast.AstUtilities.createStaticFieldAccess( field )
								);
								body.statements.add( org.lgna.project.ast.AstUtilities.createReturnStatement( org.lgna.story.Joint.class, expression ) );
								rv.methods.add( method );
							}
						}
					}
				}
			}
		}
		return rv;
	}
}
