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

public class StageIDE extends org.alice.ide.IDE {
	public StageIDE() {
		//a very short window...
//		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "resize", org.alice.apis.moveandturn.Transformable.class );
//		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "resizeWidth", org.alice.apis.moveandturn.Transformable.class );
//		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "resizeHeight", org.alice.apis.moveandturn.Transformable.class );
//		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "resizeDepth", org.alice.apis.moveandturn.Transformable.class );
//		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "setWidth", org.alice.apis.moveandturn.Transformable.class );
//		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "setHeight", org.alice.apis.moveandturn.Transformable.class );
//		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "setDepth", org.alice.apis.moveandturn.Transformable.class );
		//

		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, "moveTo", "placeRelativeTo" );
		
		org.alice.ide.common.BeveledShapeForType.addRoundType( org.alice.apis.moveandturn.Transformable.class );
		this.getFrame().addWindowStateListener( new java.awt.event.WindowStateListener() {
			public void windowStateChanged( java.awt.event.WindowEvent e ) {
				int oldState = e.getOldState();
				int newState = e.getNewState();
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "windowStateChanged", oldState, newState, java.awt.Frame.ICONIFIED );
				if( (oldState & java.awt.Frame.ICONIFIED) == java.awt.Frame.ICONIFIED ) {
					edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().incrementAutomaticDisplayCount();
				}
				if( (newState & java.awt.Frame.ICONIFIED) == java.awt.Frame.ICONIFIED ) {
					edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().decrementAutomaticDisplayCount();
				}
			}
		} );

		final int SMALL_ICON_SIZE = 32;
		org.alice.stageide.gallerybrowser.ResourceManager.registerSmallIcon( org.alice.apis.moveandturn.DirectionalLight.class, new javax.swing.Icon() {

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
					java.awt.Shape innerArc = this.createArc( 20.0f );
					java.awt.Shape outerArc = this.createArc( 22.0f );
					
					g2.translate( 4.0f, 4.0f );
					java.awt.geom.GeneralPath pathRays = new java.awt.geom.GeneralPath();
					double thetaN = Math.PI/2.0;
					double thetaDelta = thetaN/8.0;
					g2.setColor( new java.awt.Color( 255, 210, 0 ) );
					for( double theta = 0.0; theta<=thetaN; theta += thetaDelta ) {
						pathRays.moveTo( 0.0f, 0.0f );
						pathRays.lineTo( (float)( Math.cos( theta ) * 26.0 ), (float)( Math.sin( theta ) * 26.0 ) ); 
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
		org.alice.stageide.gallerybrowser.ResourceManager.registerSmallIcon( org.alice.apis.moveandturn.SymmetricPerspectiveCamera.class, new javax.swing.ImageIcon( org.alice.stageide.gallerybrowser.ResourceManager.class.getResource( "images/SymmetricPerspectiveCamera.png" ) ) );
//		org.alice.stageide.gallerybrowser.ResourceManager.registerSmallIcon( org.alice.apis.moveandturn.SymmetricPerspectiveCamera.class, new javax.swing.Icon() {
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
	protected void promptForLicenseAgreements() {
		final String IS_LICENSE_ACCEPTED_PREFERENCE_KEY = "isLicenseAccepted";
		try {
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.cmu.cs.dennisc.alice.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 1 of 3): Alice 3", edu.cmu.cs.dennisc.alice.License.TEXT, "Alice" );
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.wustl.cse.lookingglass.apis.walkandtouch.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 2 of 3): Looking Glass Walk & Touch API",
					edu.wustl.cse.lookingglass.apis.walkandtouch.License.TEXT_FOR_USE_IN_ALICE, "the Looking Glass Walk & Touch API" );
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.cmu.cs.dennisc.nebulous.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 3 of 3): The Sims (TM) 2 Art Assets",
					edu.cmu.cs.dennisc.nebulous.License.TEXT, "The Sims (TM) 2 Art Assets" );
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			this.showMessageDialog( "You must accept the license agreements in order to use Alice 3, the Looking Glass Walk & Touch API, and The Sims (TM) 2 Art Assets.  Exiting." );
			System.exit( -1 );
		}
	}

	private static final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava COLOR_TYPE = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Color.class );

	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractField, ColorIcon > mapFieldToIcon = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractField, ColorIcon >();

	class ColorIcon implements javax.swing.Icon {
		private static final int SIZE = 15;
		private java.awt.Color fillColor;
		private java.awt.Color outlineColor;

		public ColorIcon( java.awt.Color color ) {
			this.fillColor = color;
			float[] hsb = new float[ 3 ];
			//todo
			java.awt.Color.RGBtoHSB( this.fillColor.getRed(), this.fillColor.getGreen(), this.fillColor.getBlue(), hsb );
			if( hsb[ 2 ] > 0.9f ) {
				this.outlineColor = java.awt.Color.GRAY;
			} else {
				this.outlineColor = null;
			}
		}
		public int getIconWidth() {
			return SIZE + 3 + 2;
		}
		public int getIconHeight() {
			return SIZE + 3;
		}
		public void paintIcon( java.awt.Component arg0, java.awt.Graphics g, int x, int y ) {
			g.setColor( this.fillColor );
			g.fillRect( x + 1 + 2, y + 1, SIZE, SIZE );
			if( this.outlineColor != null ) {
				g.setColor( this.outlineColor );
				g.drawRect( x + 1 + 2, y + 1, SIZE, SIZE );
			}
		}
	}

	private javax.swing.Icon getIconFor( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		if( field.getDeclaringType() == COLOR_TYPE && field.getValueType() == COLOR_TYPE ) {
			ColorIcon rv = this.mapFieldToIcon.get( field );
			if( rv != null ) {
				//pass
			} else {
				try {
					edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField fieldInJava = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField)field;
					org.alice.apis.moveandturn.Color color = (org.alice.apis.moveandturn.Color)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( fieldInJava.getFieldReflectionProxy().getReification(), null );
					rv = new ColorIcon( color.getInternal().getAsAWTColor() );
					this.mapFieldToIcon.put( field, rv );
				} catch( RuntimeException re ) {
					//pass
				}
			}
			return rv;
		}
		return null;
	}
	@Override
	protected boolean areEnumConstantsDesired( edu.cmu.cs.dennisc.alice.ast.AbstractType enumType ) {
		if( enumType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Key.class ) ) {
			return false;
		} else {
			return super.areEnumConstantsDesired( enumType );
		}
	}
	@Override
	protected void addCustomFillIns( edu.cmu.cs.dennisc.cascade.Blank blank, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		super.addCustomFillIns( blank, type );
		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = this.getPreviousExpression();
		if( previousExpression != null ) {
			if( type.isAssignableFrom( org.alice.apis.moveandturn.Model.class ) ) {
				edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice enclosingMethod = previousExpression.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class );
				if( enclosingMethod != null ) {
					for( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter : enclosingMethod.parameters ) {
						edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeMouseButtonEvent = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.event.MouseButtonEvent.class );
						if( parameter.getValueType() == typeMouseButtonEvent ) {
							String[] methodNames = new String[] { "getModelAtMouseLocation", "getPartAtMouseLocation" };
							for( String methodName : methodNames ) {
								edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = typeMouseButtonEvent.getDeclaredMethod( methodName );
								edu.cmu.cs.dennisc.alice.ast.Expression expression = new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( parameter );
								edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = new edu.cmu.cs.dennisc.alice.ast.MethodInvocation( expression, method );
								blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( methodInvocation ) );
							}
						}
					}
				}
			} else if( type.isAssignableFrom( Boolean.class ) ) {
				edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice enclosingMethod = previousExpression.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class );
				if( enclosingMethod != null ) {
					for( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter : enclosingMethod.parameters ) {
						edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeKeyEvent = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.event.KeyEvent.class );
						if( parameter.getValueType() == typeKeyEvent ) {
							//							String[] methodNames = new String[] { "isKey", "isLetter", "isDigit" };
							//							Class<?>[][] parameterClses = new Class<?>[][] { new Class<?>[]{ org.alice.apis.moveandturn.Key.class }, new Class<?>[]{}, new Class<?>[]{} };
							//							for( int i=0; i<methodNames.length; i++ ) {
							//								edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = typeKeyEvent.getDeclaredMethod( methodNames[ i ], parameterClses[ i ] );
							//								edu.cmu.cs.dennisc.alice.ast.Expression expression = new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( parameter );
							//								edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = new edu.cmu.cs.dennisc.alice.ast.MethodInvocation( expression, method );
							//								blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( methodInvocation ) );
							//							}

							{
								edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = typeKeyEvent.getDeclaredMethod( "isKey", org.alice.apis.moveandturn.Key.class );
								edu.cmu.cs.dennisc.alice.ast.Expression expression = new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( parameter );
								blank.addFillIn( new org.alice.ide.cascade.IncompleteMethodInvocationFillIn( expression, method ) );
							}
							String[] methodNames = new String[] { "isLetter", "isDigit" };
							for( String methodName : methodNames ) {
								edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = typeKeyEvent.getDeclaredMethod( methodName );
								edu.cmu.cs.dennisc.alice.ast.Expression expression = new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( parameter );
								edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = new edu.cmu.cs.dennisc.alice.ast.MethodInvocation( expression, method );
								blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( methodInvocation ) );
							}
						}
					}
				}
			}
		}
	}
	
//	@Override
//	protected boolean isInclusionOfTypeDesired(edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> valueTypeInAlice) {
//		return super.isInclusionOfTypeDesired(valueTypeInAlice) || valueTypeInAlice.isAssignableTo( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.AbstractCamera.class ) );
//	}

	@Override
	protected boolean isAccessibleDesired( edu.cmu.cs.dennisc.alice.ast.Accessible accessible ) {
		if( super.isAccessibleDesired( accessible ) ) {
			if( accessible.getValueType().isAssignableTo( org.alice.apis.moveandturn.Marker.class) ) {
				return false;
			} else {
				return accessible.getValueType().isAssignableTo( org.alice.apis.moveandturn.AbstractTransformable.class );
			}
		} else {
			return false;
		}
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.Component< ? > getPrefixPaneForFieldAccessIfAppropriate( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = fieldAccess.field.getValue();
		javax.swing.Icon icon = getIconFor( field );
		if( icon != null ) {
			return new edu.cmu.cs.dennisc.croquet.Label( icon );
		}
		return super.getPrefixPaneForFieldAccessIfAppropriate( fieldAccess );
	}

	private static final edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava REVOLUTIONS_CONSTRUCTOR = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( org.alice.apis.moveandturn.AngleInRevolutions.class, Number.class );
	private static final edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava PORTION_CONSTRUCTOR = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( org.alice.apis.moveandturn.Portion.class, Number.class );

	protected org.alice.ide.common.DeclarationNameLabel createDeclarationNameLabel( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		//todo: better name
		class ThisFieldAccessNameLabel extends org.alice.ide.common.DeclarationNameLabel {
			public ThisFieldAccessNameLabel( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
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
	public edu.cmu.cs.dennisc.croquet.JComponent< ? > getOverrideComponent( org.alice.ide.common.Factory factory, edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
			edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess)expression;
			edu.cmu.cs.dennisc.alice.ast.Expression fieldExpression = fieldAccess.expression.getValue();
			if( fieldExpression instanceof edu.cmu.cs.dennisc.alice.ast.ThisExpression ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractField field = fieldAccess.field.getValue();
				edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > declaringType = field.getDeclaringType();
				if( declaringType != null && declaringType.isAssignableTo( org.alice.apis.moveandturn.Scene.class ) ) {
					if( field.getValueType().isAssignableTo( org.alice.apis.moveandturn.Transformable.class ) ) {
						return new org.alice.ide.common.ExpressionPane( expression, this.createDeclarationNameLabel( field ) ) {
							@Override
							protected boolean isExpressionTypeFeedbackDesired() {
								return true;
							}
						};
					}
				}
			}
		} else {
			if( this.isJava() ) {
				//pass
			} else {
				if( expression instanceof edu.cmu.cs.dennisc.alice.ast.InstanceCreation ) {
					edu.cmu.cs.dennisc.alice.ast.InstanceCreation instanceCreation = (edu.cmu.cs.dennisc.alice.ast.InstanceCreation)expression;
					edu.cmu.cs.dennisc.alice.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
					if( constructor == REVOLUTIONS_CONSTRUCTOR ) {
						return new edu.cmu.cs.dennisc.croquet.LineAxisPanel( factory.createExpressionPane( instanceCreation.arguments.get( 0 ).expression.getValue() ), new edu.cmu.cs.dennisc.croquet.Label( " revolutions" ) );
					} else if( constructor == PORTION_CONSTRUCTOR ) {
						return factory.createExpressionPane( instanceCreation.arguments.get( 0 ).expression.getValue() );
					}
				}
			}
		}
		return super.getOverrideComponent( factory, expression );
	}
	@Override
	public boolean isDropDownDesiredForFieldInitializer( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractType declaringType = field.getDeclaringType();
		if( declaringType != null ) {
			if( declaringType.isAssignableTo( org.alice.apis.moveandturn.Scene.class ) ) {
				edu.cmu.cs.dennisc.alice.ast.Expression initializer = field.initializer.getValue();
				if( initializer instanceof edu.cmu.cs.dennisc.alice.ast.InstanceCreation ) {
					edu.cmu.cs.dennisc.alice.ast.InstanceCreation instanceCreation = (edu.cmu.cs.dennisc.alice.ast.InstanceCreation)initializer;
					edu.cmu.cs.dennisc.alice.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
					if( constructor != null ) {
						edu.cmu.cs.dennisc.alice.ast.AbstractType type = constructor.getDeclaringType();
						if( type != null ) {
							if( type.isAssignableTo( org.alice.apis.moveandturn.AbstractTransformable.class ) ) {
								return false;
							}
						}
					}
				}
			}
		}
		return super.isDropDownDesiredForFieldInitializer( field );
	}
	@Override
	public boolean isDropDownDesiredFor( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		if( super.isDropDownDesiredFor( expression ) ) {
			if( expression != null ) {
				if (expression instanceof edu.cmu.cs.dennisc.alice.ast.InstanceCreation) {
					edu.cmu.cs.dennisc.alice.ast.InstanceCreation instanceCreation = (edu.cmu.cs.dennisc.alice.ast.InstanceCreation) expression;
					edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = instanceCreation.getType();
					if( type instanceof edu.cmu.cs.dennisc.alice.ast.AnonymousInnerTypeDeclaredInAlice ) {
						if( type.isAssignableTo( org.alice.apis.moveandturn.event.KeyListener.class ) || type.isAssignableTo( org.alice.apis.moveandturn.event.MouseButtonListener.class ) ) {
							return false;
						}
					}
				} else {
					edu.cmu.cs.dennisc.alice.ast.Node parent = expression.getParent();
					if( parent instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
						edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess)parent;
						edu.cmu.cs.dennisc.alice.ast.AbstractField field = fieldAccess.field.getValue();
						assert field != null;
						edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > declaringType = field.getDeclaringType();
						if( declaringType != null && declaringType.isAssignableTo( org.alice.apis.moveandturn.Scene.class ) ) {
							if( field.getValueType().isAssignableTo( org.alice.apis.moveandturn.Transformable.class ) ) {
								return false;
							}
						}
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}
//	public MoveAndTurnRuntimeProgram createRuntimeProgram( edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType, edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm ) {
//		return new MoveAndTurnRuntimeProgram( sceneType, vm );
//	}
//	private int xLocation = 100;
//	private int yLocation = 100;
//
//	protected void showInJDialog( MoveAndTurnRuntimeProgram rtProgram ) {
//		this.disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM );
//		try {
//			rtProgram.showInJDialog( this.getFrame().getAwtWindow(), true, new String[] { "X_LOCATION=" + xLocation, "Y_LOCATION=" + yLocation } );
//		} finally {
//			this.enableRendering();
//			try {
//				this.xLocation = Integer.parseInt( rtProgram.getParameter( "X_LOCATION" ) );
//			} catch( Throwable t ) {
//				this.xLocation = 0;
//			}
//			try {
//				this.yLocation = Integer.parseInt( rtProgram.getParameter( "Y_LOCATION" ) );
//			} catch( Throwable t ) {
//				this.yLocation = 0;
//			}
//		}
//	}

//	public void showInContainer( MoveAndTurnRuntimeProgram rtProgram, edu.cmu.cs.dennisc.croquet.Container< ? > container ) {
//		String[] args = {};
//		rtProgram.showInAWTContainer( container.getAwtComponent(), args );
//	}
	@Override
	public edu.cmu.cs.dennisc.croquet.DialogOperation getRunOperation() {
		return org.alice.stageide.croquet.models.run.RunOperation.getInstance();
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Operation<?> createRestartOperation() {
		return new org.alice.stageide.croquet.models.run.RestartOperation();
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.Operation<?> createPreviewOperation( org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate procedureInvocationTemplate ) {
		return new org.alice.stageide.croquet.models.run.PreviewMethodOperation( procedureInvocationTemplate );
	}

//	public final void handleRun( edu.cmu.cs.dennisc.croquet.ModelContext context ) {
//		if( this.getProject() != null ) {
//			this.ensureProjectCodeUpToDate();
//			this.handleRun( context, this.getSceneType() );
//		} else {
//			this.showMessageDialog( "Please open a project first." );
//		}
//	}
//	@Override
//	public void handleRun( edu.cmu.cs.dennisc.croquet.ModelContext context, edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType ) {
//		edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm = this.createVirtualMachineForRuntimeProgram();
//		vm.registerAnonymousAdapter( org.alice.apis.moveandturn.event.MouseButtonListener.class, org.alice.stageide.apis.moveandturn.event.MouseButtonAdapter.class );
//		vm.registerAnonymousAdapter( org.alice.apis.moveandturn.event.KeyListener.class, org.alice.stageide.apis.moveandturn.event.KeyAdapter.class );
//		vm.setEntryPointType( this.getProgramType() );
//		MoveAndTurnRuntimeProgram rtProgram = this.createRuntimeProgram( sceneType, vm );
//		showInJDialog( rtProgram );
//	}
//	@Override
//	public void handleRestart( final edu.cmu.cs.dennisc.croquet.ModelContext context ) {
//		javax.swing.SwingUtilities.invokeLater( new Runnable() {
//			public void run() {
//				handleRun( context );
//			}
//		} );
//	}
//
//	@Override
//	public void handlePreviewMethod( edu.cmu.cs.dennisc.croquet.ModelContext context, edu.cmu.cs.dennisc.alice.ast.MethodInvocation emptyExpressionMethodInvocation ) {
//		this.ensureProjectCodeUpToDate();
//		edu.cmu.cs.dennisc.alice.ast.AbstractField field = this.getFieldSelectionState().getValue();
//		if( field == this.getSceneField() ) {
//			field = null;
//		}
//		TestMethodProgram testProgram = new TestMethodProgram( this.getSceneType(), field, emptyExpressionMethodInvocation );
//		this.disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering.RUN_PROGRAM );
//		try {
//			testProgram.showInJDialog( this.getFrame().getAwtWindow(), true, new String[] { "X_LOCATION=" + xLocation, "Y_LOCATION=" + yLocation } );
//		} finally {
//			this.enableRendering();
//			try {
//				this.xLocation = Integer.parseInt( testProgram.getParameter( "X_LOCATION" ) );
//			} catch( Throwable t ) {
//				this.xLocation = 0;
//			}
//			try {
//				this.yLocation = Integer.parseInt( testProgram.getParameter( "Y_LOCATION" ) );
//			} catch( Throwable t ) {
//				this.yLocation = 0;
//			}
//		}
//	}

	@Override
	protected org.alice.ide.sceneeditor.AbstractSceneEditor createSceneEditor() {
		return new org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor();
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Operation createAboutOperation() {
		return new org.alice.stageide.croquet.models.help.AboutOperation();
	}

	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractType, String > mapTypeToText;

	private static edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice getDeclaredMethod( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type, String name, Class< ? >... paramClses ) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( type.getDeclaredMethod( name, paramClses ), edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class );
	}
	private static edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice getDeclaredConstructor( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type, Class< ? >... paramClses ) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( type.getDeclaredConstructor( paramClses ), edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice.class );
	}

	@Override
	public void setProject( edu.cmu.cs.dennisc.alice.Project project ) {
		if( project != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType programType = project.getProgramType();
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = getSceneTypeFromProgramType( programType );
			if( sceneType != null ) {

				//edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice cameraType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)sceneType.fields.get( 2 ).valueType.getValue();
				//cameraType.superType.setValue( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.FrustumPerspectiveCamera.class ) );
				//cameraType.superType.setValue( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.SymmetricPerspectiveCamera.class ) );

				final String CUSTOM_SET_UP_METHOD_NAME = "performMySetUp";
				boolean isSetUpMethodReplacementDesired = false;
				java.util.Map< String, String > map = new java.util.HashMap< String, String >();
				map.put( "performSceneEditorGeneratedSetUp", GENERATED_SET_UP_METHOD_NAME );
				map.put( "performCustomPropertySetUp", CUSTOM_SET_UP_METHOD_NAME );
				for( String key : map.keySet() ) {
					edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = sceneType.getDeclaredMethod( key );
					if( method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
						edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodInAlice = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)method;
						String value = map.get( key );
						methodInAlice.name.setValue( value );
						isSetUpMethodReplacementDesired = true;
					}
				}
				if( isSetUpMethodReplacementDesired ) {
					edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInAlice constructor = getDeclaredConstructor( sceneType );
					final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice setUpMethod = getDeclaredMethod( sceneType, "performSetUp" );
					edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice setUpGeneratedMethod = getDeclaredMethod( sceneType, GENERATED_SET_UP_METHOD_NAME );
					edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice setUpCustomMethod = getDeclaredMethod( sceneType, CUSTOM_SET_UP_METHOD_NAME );
					if( constructor != null && setUpMethod != null && setUpGeneratedMethod != null && setUpCustomMethod != null ) {
						edu.cmu.cs.dennisc.alice.ast.BlockStatement constructorBody = constructor.body.getValue();
						if( constructorBody != null && constructorBody.statements.size() == 1 ) {
							edu.cmu.cs.dennisc.alice.ast.BlockStatement setUpBody = setUpMethod.body.getValue();
							if( setUpBody != null && setUpBody.statements.size() == 2 ) {
								int index = sceneType.methods.indexOf( setUpMethod );
								if( index >= 0 ) {
									edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice >(
											edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class ) {
										@Override
										protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice ) {
											return methodDeclaredInAlice == setUpMethod;
										}
									};
									programType.crawl( crawler, true );
									if( crawler.getList().size() == 1 ) {
										edu.cmu.cs.dennisc.alice.ast.Statement statement = constructorBody.statements.get( 0 );
										if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ExpressionStatement ) {
											edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement = (edu.cmu.cs.dennisc.alice.ast.ExpressionStatement)statement;
											edu.cmu.cs.dennisc.alice.ast.Expression expression = expressionStatement.expression.getValue();
											if( expression instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation ) {
												edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expression;
												if( methodInvocation.method.getValue() == setUpMethod ) {
													constructorBody.statements.clear();
													constructorBody.statements.add( org.alice.ide.ast.NodeUtilities.createMethodInvocationStatement( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), setUpGeneratedMethod ), org.alice.ide.ast.NodeUtilities
															.createMethodInvocationStatement( new edu.cmu.cs.dennisc.alice.ast.ThisExpression(), setUpCustomMethod ) );
													sceneType.methods.remove( index );
												}
											}
										}
									}
								}
							}

						}
					}
				}
			}
		}
		super.setProject( project );
	}

	private static final boolean IS_LIMITED_TO_LOOKING_GLASS_TYPES = true;
	private static String createExampleText( String examples ) {
		return "<html><em>examples:</em> " + examples + "</html>";
	}
	@Override
	public String getTextFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		if( mapTypeToText != null ) {
			//pass
		} else {
			mapTypeToText = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractType, String >();
			mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.DOUBLE_OBJECT_TYPE, createExampleText( "0.25, 1.0, 3.14, 98.6" ) );
			mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE, createExampleText( "1, 2, 42, 100" ) );
			mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.BOOLEAN_OBJECT_TYPE, createExampleText( "true, false" ) );
			mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( String.class ), createExampleText( "\"hello\", \"goodbye\"" ) );
			
//			if( IS_LIMITED_TO_LOOKING_GLASS_TYPES ) {
//				
//			} else {
//				mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Model.class ), "(a.k.a. Generic Alice Model)" );
//				mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class ), "(a.k.a. Looking Glass Scenery)" );
//				mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.Character.class ), "(a.k.a. Looking Glass Character)" );
//				mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.Person.class ), "(a.k.a. Looking Glass Person)" );
//				mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.Adult.class ), "(a.k.a. Stage Adult)" );
//				mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.Child.class ), "(a.k.a. Stage Child)" );
//			}
		}
		return mapTypeToText.get( type );
	}

	@Override
	protected java.util.List< ? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > addPrimeTimeJavaTypes( java.util.List< ? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > rv ) {
		rv = super.addPrimeTimeJavaTypes( rv );
//		if( IS_LIMITED_TO_LOOKING_GLASS_TYPES ) {
//			//pass
//		} else {
//			rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Model.class ) );
//		}
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class ) );
//		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.Character.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.Person.class ) );
		return rv;
	}

	@Override
	protected java.util.List<? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava> addSecondaryJavaTypes(java.util.List<? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava> rv) {
		super.addSecondaryJavaTypes(rv);
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Color.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.MoveDirection.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.TurnDirection.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.RollDirection.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Model.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.CameraMarker.class ) );
		if( IS_LIMITED_TO_LOOKING_GLASS_TYPES ) {
			//pass
		} else {
			rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.Adult.class ) );
			rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.Child.class ) );
		}
		return rv;
	}
	@Override
	protected void addFillInAndPossiblyPartFills( edu.cmu.cs.dennisc.cascade.Blank blank, edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.AbstractType type2 ) {
		super.addFillInAndPossiblyPartFills( blank, expression, type, type2 );
		if( type.isAssignableTo( org.alice.apis.moveandturn.PolygonalModel.class ) ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeInJava = null;
			Class< ? > paramCls = null;
			if( type2.isAssignableFrom( org.alice.apis.moveandturn.Model.class ) ) {
				typeInJava = type.getFirstTypeEncounteredDeclaredInJava();
				Class< ? > cls = typeInJava.getClassReflectionProxy().getReification();
				for( Class innerCls : cls.getDeclaredClasses() ) {
					if( innerCls.getSimpleName().equals( "Part" ) ) {
						paramCls = innerCls;
					}
				}
			}
			if( paramCls != null ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractMethod getPartMethod = typeInJava.getDeclaredMethod( "getPart", paramCls );
				if( getPartMethod != null ) {
					blank.addFillIn( new org.alice.ide.cascade.IncompleteMethodInvocationFillIn( expression, getPartMethod ) );
				}
			}
		}
	}
	@Override
	public edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> getGalleryRoot() {
		try {
			String jarPath = org.alice.apis.moveandturn.gallery.GalleryModel.getGalleryRootDirectory() + "/mtwtGalleryLargeIcons.jar";
			edu.cmu.cs.dennisc.zip.DirectoryZipTreeNode jarRoot = edu.cmu.cs.dennisc.zip.ZipUtilities.createTreeNode( jarPath, false );
			String[] paths = {
					 "org/alice/stageide/gallerybrowser/images/edu/wustl/cse/lookingglass/apis/walkandtouch/gallery/characters", 
					 "org/alice/stageide/gallerybrowser/images/edu/wustl/cse/lookingglass/apis/walkandtouch/gallery/scenes", 
					 "org/alice/stageide/gallerybrowser/images/org/alice/apis/moveandturn/gallery", 
			};
			final int N = paths.length;
			final java.util.ArrayList< edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> > children = edu.cmu.cs.dennisc.java.util.Collections.newArrayList();
			children.ensureCapacity( N );
			edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> rv = new edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>() {
				public java.util.Enumeration< edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> > children() {
					return java.util.Collections.enumeration( children );
				}
				public java.util.Iterator<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>> iterator() {
					return children.iterator();
				}
				public edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> getChildAt(int childIndex) {
					return children.get( childIndex );
				}
				public int getChildCount() {
					return children.size();
				}
				public int getIndex(javax.swing.tree.TreeNode node) {
					return children.indexOf( node );
				}
				public edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> getParent() {
					return null;
				}
				public boolean getAllowsChildren() {
					return true;
				}
				public boolean isLeaf() {
					return false;
				}
				public String getValue() {
					return null;
				}
				@Override
				public String toString() {
					return "thumbnails";
				}
			};
			for( String path : paths ) {
				edu.cmu.cs.dennisc.zip.ZipTreeNode zipTreeNode = jarRoot.getDescendant( path );
				zipTreeNode.setParent( rv );
				children.add( zipTreeNode );
			}
			return rv;
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.JComponent<?> createGalleryBrowser( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> root ) {
		return new org.alice.stageide.gallerybrowser.GalleryBrowser( root );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getEnumTypeForInterfaceType( edu.cmu.cs.dennisc.alice.ast.AbstractType interfaceType ) {
		if( interfaceType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Style.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.TraditionalStyle.class );
		} else if( interfaceType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.EyeColor.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.BaseEyeColor.class );
		} else if( interfaceType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.SkinTone.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.BaseSkinTone.class );
		} else {
			return super.getEnumTypeForInterfaceType( interfaceType );
		}
	}

	//	@Override
	//	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getTypeFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
	//		if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.EyeColor.class ) ) {
	//			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.BaseEyeColor.class );
	//		} else if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.SkinTone.class ) ) {
	//			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.BaseSkinTone.class );
	//		} else {
	//			return super.getTypeFor( type );
	//		}
	//	}

	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getActualTypeForDesiredParameterType( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		if( type.isAssignableTo( org.alice.apis.moveandturn.Angle.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.NUMBER_OBJECT_TYPE;
		} else if( type.isAssignableTo( org.alice.apis.moveandturn.Portion.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.NUMBER_OBJECT_TYPE;
		} else if( type.isAssignableTo( org.alice.apis.moveandturn.VolumeLevel.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.NUMBER_OBJECT_TYPE;
		} else {
			return super.getActualTypeForDesiredParameterType( type );
		}
	}

	@Override
	protected java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > addExpressionFillerInners( java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > rv ) {
		super.addExpressionFillerInners( rv );
		rv.add( new org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner( org.alice.apis.moveandturn.Color.class ) );
		rv.add( new org.alice.stageide.cascade.fillerinners.KeyFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.AngleFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.PortionFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.AudioSourceFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.VolumeLevelFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.ImageSourceFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.MouseButtonListenerFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.KeyListenerFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.OutfitFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.HairFillerInner() );
		return rv;
	}

//	@Override
//	public boolean isDeclareFieldOfPredeterminedTypeSupported( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice valueType ) {
//		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeInJava = valueType.getFirstTypeEncounteredDeclaredInJava();
//		if( typeInJava == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.Adult.class ) ) {
//			this.showMessageDialog( "todo: isDeclareFieldOfPredeterminedTypeSupported" );
//			return false;
//		} else {
//			return super.isDeclareFieldOfPredeterminedTypeSupported( valueType );
//		}
//	}
	@Override
	public boolean isInstanceCreationAllowableFor( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice typeInAlice ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeInJava = typeInAlice.getFirstTypeEncounteredDeclaredInJava();
		return false == edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( typeInJava.getClassReflectionProxy().getReification(), org.alice.apis.moveandturn.Scene.class, org.alice.apis.moveandturn.AbstractCamera.class, org.alice.apis.stage.Person.class );
	}
	@Override
	public edu.cmu.cs.dennisc.animation.Program createRuntimeProgram( edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType, final int frameRate ) {
		return new MoveAndTurnRuntimeProgram( sceneType, vm ) {
			@Override
			protected java.awt.Component createSpeedMultiplierControlPanel() {
				return null;
			}
			@Override
			protected edu.cmu.cs.dennisc.animation.Animator createAnimator() {
				return new edu.cmu.cs.dennisc.animation.FrameBasedAnimator( frameRate );
			}

			@Override
			protected void postRun() {
				super.postRun();
				this.setMovieEncoder( null );
			}
		};
	}

	private static final int THUMBNAIL_WIDTH = 160;
	private static final int THUMBNAIL_HEIGHT = THUMBNAIL_WIDTH * 3 / 4;
	private edu.cmu.cs.dennisc.lookingglass.OffscreenLookingGlass offscreenLookingGlass;

	@Override
	protected java.awt.image.BufferedImage createThumbnail() throws Throwable {
		if( offscreenLookingGlass != null ) {
			//pass
		} else {
			offscreenLookingGlass = edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().createOffscreenLookingGlass( null );
			offscreenLookingGlass.setSize( THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT );
		}
		org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor moveAndTurnSceneEditor = (org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor)this.getSceneEditor();
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = moveAndTurnSceneEditor.getSGCameraForCreatingThumbnails();
		boolean isClearingAndAddingRequired;
		if( offscreenLookingGlass.getCameraCount() == 1 ) {
			if( offscreenLookingGlass.getCameraAt( 0 ) == sgCamera ) {
				isClearingAndAddingRequired = false;
			} else {
				isClearingAndAddingRequired = true;
			}
		} else {
			isClearingAndAddingRequired = true;
		}
		if( isClearingAndAddingRequired ) {
			offscreenLookingGlass.clearCameras();
			offscreenLookingGlass.addCamera( sgCamera );
		}
		java.awt.image.BufferedImage rv = offscreenLookingGlass.getColorBuffer();
		return rv;
	}

	@Override
	protected org.alice.ide.openprojectpane.TabContentPanel createTemplatesTabContentPane() {
		return new org.alice.stageide.openprojectpane.templates.TemplatesTabContentPane();
	}

	@Override
	public java.util.List< edu.cmu.cs.dennisc.pattern.Tuple2< String, Class< ? >>> updateNameClsPairsForRelationalFillIns( java.util.List< edu.cmu.cs.dennisc.pattern.Tuple2< String, Class< ? >>> rv ) {
		super.updateNameClsPairsForRelationalFillIns( rv );
		//rv.add( new edu.cmu.cs.dennisc.pattern.Tuple2< String, Class< ? > >( "Key", org.alice.apis.moveandturn.Key.class ) );
		return rv;
	}
}
