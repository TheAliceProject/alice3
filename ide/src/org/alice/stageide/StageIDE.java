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

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.alice.stageide.ast.SceneAdapter;
import org.lookingglassandalice.storytelling.Scene;
import org.lookingglassandalice.storytelling.resourceutilities.ModelResourceTreeNode;
import org.lookingglassandalice.storytelling.resourceutilities.ModelResourceUtilities;

import edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice;
import edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine;
import edu.cmu.cs.dennisc.java.io.FileUtilities;

public class StageIDE extends org.alice.ide.IDE {
	public static StageIDE getActiveInstance() {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance(  org.alice.ide.IDE.getActiveInstance(), StageIDE.class );
	}
	private org.alice.ide.cascade.CascadeManager cascadeManager = new org.alice.stageide.cascade.CascadeManager();
	public StageIDE() {
		org.alice.ide.common.BeveledShapeForType.addRoundType( org.lookingglassandalice.storytelling.Entity.class );
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
		org.alice.stageide.gallerybrowser.ResourceManager.registerSmallIcon( org.lookingglassandalice.storytelling.Sun.class, new javax.swing.Icon() {

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
		org.alice.stageide.gallerybrowser.ResourceManager.registerSmallIcon( org.lookingglassandalice.storytelling.Camera.class, new javax.swing.ImageIcon( org.alice.stageide.gallerybrowser.ResourceManager.class.getResource( "images/SymmetricPerspectiveCamera.png" ) ) );
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
	public org.alice.ide.ApiConfigurationManager getApiConfigurationManager() {
		return StoryApiConfigurationManager.SINGLETON;
	}
	@Override
	protected void registerAdapters(VirtualMachine vm) {
		vm.registerAnonymousAdapter( Scene.class, SceneAdapter.class );
	}
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava MOUSE_BUTTON_LISTENER_TYPE = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.event.MouseButtonListener.class );
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava KEY_LISTENER_TYPE = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.event.KeyListener.class );
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createPredeterminedExpressionIfAppropriate( edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > desiredValueType ) {
		if( desiredValueType == MOUSE_BUTTON_LISTENER_TYPE ) {
			edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice[] parameters = new edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice[] {
					new edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice( "e", org.lookingglassandalice.storytelling.event.MouseButtonEvent.class )
			};
			edu.cmu.cs.dennisc.alice.ast.BlockStatement body = new edu.cmu.cs.dennisc.alice.ast.BlockStatement();
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = new edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice( "mouseButtonClicked", Void.TYPE, parameters, body );
			method.isSignatureLocked.setValue( true );
			edu.cmu.cs.dennisc.alice.ast.AnonymousInnerTypeDeclaredInAlice type = new edu.cmu.cs.dennisc.alice.ast.AnonymousInnerTypeDeclaredInAlice();
			type.superType.setValue( desiredValueType );
			type.methods.add( method );
			edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor constructor = edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor.get( type );
			return new edu.cmu.cs.dennisc.alice.ast.InstanceCreation( constructor );
		} else if( desiredValueType == KEY_LISTENER_TYPE ) {
			edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice[] parameters = new edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice[] {
					new edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice( "e", org.lookingglassandalice.storytelling.event.KeyEvent.class )
			};
			edu.cmu.cs.dennisc.alice.ast.BlockStatement body = new edu.cmu.cs.dennisc.alice.ast.BlockStatement();
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = new edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice( "keyPressed", Void.TYPE, parameters, body );
			method.isSignatureLocked.setValue( true );
			edu.cmu.cs.dennisc.alice.ast.AnonymousInnerTypeDeclaredInAlice type = new edu.cmu.cs.dennisc.alice.ast.AnonymousInnerTypeDeclaredInAlice();
			type.superType.setValue( desiredValueType );
			type.methods.add( method );
			edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor constructor = edu.cmu.cs.dennisc.alice.ast.AnonymousConstructor.get( type );
			return new edu.cmu.cs.dennisc.alice.ast.InstanceCreation( constructor );
		} else {
			return super.createPredeterminedExpressionIfAppropriate( desiredValueType );
		}
	}
	@Override
	public org.alice.ide.cascade.CascadeManager getCascadeManager() {
		return this.cascadeManager;
	}
	
	@Override
	protected void promptForLicenseAgreements() {
		final String IS_LICENSE_ACCEPTED_PREFERENCE_KEY = "isLicenseAccepted";
		try {
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.cmu.cs.dennisc.alice.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 1 of 3): Alice 3", edu.cmu.cs.dennisc.alice.License.TEXT, "Alice" );
//			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.wustl.cse.lookingglass.apis.walkandtouch.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 2 of 3): Looking Glass Walk & Touch API",
//					edu.wustl.cse.lookingglass.apis.walkandtouch.License.TEXT_FOR_USE_IN_ALICE, "the Looking Glass Walk & Touch API" );
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.cmu.cs.dennisc.nebulous.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 3 of 3): The Sims (TM) 2 Art Assets",
					edu.cmu.cs.dennisc.nebulous.License.TEXT, "The Sims (TM) 2 Art Assets" );
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			this.showMessageDialog( "You must accept the license agreements in order to use Alice 3, the Looking Glass Walk & Touch API, and The Sims (TM) 2 Art Assets.  Exiting." );
			System.exit( -1 );
		}
	}

	private static final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava COLOR_TYPE = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.Color.class );

	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractField, org.alice.ide.swing.icons.ColorIcon > mapFieldToIcon = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractField, org.alice.ide.swing.icons.ColorIcon >();

	private javax.swing.Icon getIconFor( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		if( field.getDeclaringType() == COLOR_TYPE && field.getValueType() == COLOR_TYPE ) {
			org.alice.ide.swing.icons.ColorIcon rv = this.mapFieldToIcon.get( field );
			if( rv != null ) {
				//pass
			} else {
				try {
					edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField fieldInJava = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField)field;
					org.lookingglassandalice.storytelling.Color color = (org.lookingglassandalice.storytelling.Color)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( fieldInJava.getFieldReflectionProxy().getReification(), null );
					rv = new org.alice.ide.swing.icons.ColorIcon( org.lookingglassandalice.storytelling.ImplementationAccessor.getColor4f( color ).getAsAWTColor() );
					this.mapFieldToIcon.put( field, rv );
				} catch( RuntimeException re ) {
					//pass
				}
			}
			return rv;
		}
		return null;
	}
	
//	@Override
//	protected boolean isInclusionOfTypeDesired(edu.cmu.cs.dennisc.alice.ast.AbstractTypeDeclaredInAlice<?> valueTypeInAlice) {
//		return super.isInclusionOfTypeDesired(valueTypeInAlice) || valueTypeInAlice.isAssignableTo( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.Camera.class ) );
//	}

	@Override
	protected boolean isAccessibleDesired( edu.cmu.cs.dennisc.alice.ast.Accessible accessible ) {
		if( super.isAccessibleDesired( accessible ) ) {
//			if( accessible.getValueType().isAssignableTo( org.lookingglassandalice.storytelling.Marker.class) ) {
//				return false;
//			} else {
				return accessible.getValueType().isAssignableTo( org.lookingglassandalice.storytelling.Entity.class );
//			}
		} else {
			return false;
		}
	}
	@Override
	public org.lgna.croquet.components.Component< ? > getPrefixPaneForFieldAccessIfAppropriate( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = fieldAccess.field.getValue();
		javax.swing.Icon icon = getIconFor( field );
		if( icon != null ) {
			return new org.lgna.croquet.components.Label( icon );
		}
		return super.getPrefixPaneForFieldAccessIfAppropriate( fieldAccess );
	}

	private static final edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava REVOLUTIONS_CONSTRUCTOR = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( org.lookingglassandalice.storytelling.AngleInRevolutions.class, Number.class );
	private static final edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava PORTION_CONSTRUCTOR = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( org.lookingglassandalice.storytelling.Portion.class, Number.class );

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
	public org.lgna.croquet.components.JComponent< ? > getOverrideComponent( org.alice.ide.common.Factory factory, edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
			edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess)expression;
			edu.cmu.cs.dennisc.alice.ast.Expression fieldExpression = fieldAccess.expression.getValue();
			if( fieldExpression instanceof edu.cmu.cs.dennisc.alice.ast.ThisExpression ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractField field = fieldAccess.field.getValue();
				edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > declaringType = field.getDeclaringType();
				if( declaringType != null && declaringType.isAssignableTo( org.lookingglassandalice.storytelling.Scene.class ) ) {
					if( field.getValueType().isAssignableTo( org.lookingglassandalice.storytelling.Entity.class ) ) {
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
						return new org.lgna.croquet.components.LineAxisPanel( factory.createExpressionPane( instanceCreation.arguments.get( 0 ).expression.getValue() ), new org.lgna.croquet.components.Label( " revolutions" ) );
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
			if( declaringType.isAssignableTo( org.lookingglassandalice.storytelling.Scene.class ) ) {
				edu.cmu.cs.dennisc.alice.ast.Expression initializer = field.initializer.getValue();
				if( initializer instanceof edu.cmu.cs.dennisc.alice.ast.InstanceCreation ) {
					edu.cmu.cs.dennisc.alice.ast.InstanceCreation instanceCreation = (edu.cmu.cs.dennisc.alice.ast.InstanceCreation)initializer;
					edu.cmu.cs.dennisc.alice.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
					if( constructor != null ) {
						edu.cmu.cs.dennisc.alice.ast.AbstractType type = constructor.getDeclaringType();
						if( type != null ) {
							if( type.isAssignableTo( org.lookingglassandalice.storytelling.Turnable.class ) ) {
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
						if( type.isAssignableTo( org.lookingglassandalice.storytelling.event.KeyListener.class ) || type.isAssignableTo( org.lookingglassandalice.storytelling.event.MouseButtonListener.class ) ) {
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
						if( declaringType != null && declaringType.isAssignableTo( org.lookingglassandalice.storytelling.Scene.class ) ) {
							if( field.getValueType().isAssignableTo( org.lookingglassandalice.storytelling.Turnable.class ) ) {
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
	@Override
	public org.lgna.croquet.Operation<?> getRunOperation() {
		return EPIC_HACK_getRunDialogOperation();
	}
	
	public org.lgna.croquet.PlainDialogOperation EPIC_HACK_getRunDialogOperation() {
		return org.alice.stageide.croquet.models.run.RunOperation.getInstance();
	}
	
	
	
	@Override
	public org.lgna.croquet.Operation< ? > getRestartOperation() {
		return org.alice.stageide.croquet.models.run.RestartOperation.getInstance();
	}
	@Override
	public org.lgna.croquet.Operation<?> createPreviewOperation( org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate procedureInvocationTemplate ) {
		return new org.alice.stageide.croquet.models.run.PreviewMethodOperation( procedureInvocationTemplate );
	}
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
	public org.alice.stageide.sceneeditor.StorytellingSceneEditor getSceneEditor() {
		return org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance();
	}
	
	@Override
	public org.lgna.croquet.Operation< ? > getAboutOperation() {
		return org.alice.stageide.croquet.models.help.AboutOperation.getInstance();
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
		super.setProject( project );
		if( project != null ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programType = project.getProgramType();
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = getSceneTypeFromProgramType( programType );
			if( sceneType != null ) {
				org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().edit( sceneType.findMethod( "myFirstMethod" ), false );
			}
		}
	}

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
		}
		return mapTypeToText.get( type );
	}

	@Override
	protected java.util.List< ? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > addPrimeTimeJavaTypes( java.util.List< ? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava > rv ) {
		rv = super.addPrimeTimeJavaTypes( rv );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.Biped.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.Model.class ) );
		return rv;
	}

	@Override
	protected java.util.List<? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava> addSecondaryJavaTypes(java.util.List<? super edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava> rv) {
		super.addSecondaryJavaTypes(rv);
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.Color.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.MoveDirection.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.TurnDirection.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.RollDirection.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.Model.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.Marker.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.ObjectMarker.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.lookingglassandalice.storytelling.CameraMarker.class ) );
		return rv;
	}
	@Override
	protected org.lgna.croquet.components.JComponent<?> createClassGalleryBrowser( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> root ) {
		return new org.alice.stageide.gallerybrowser.ClassBasedGalleryBrowser( root );
	}

	@Override
	public edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> getClassGalleryRoot() {
		try {
			String rootGalleryPath = this.getApplicationRootDirectory() + "/assets/newAPI";

			File[] jarFiles = FileUtilities.listDescendants(rootGalleryPath, "jar");
			List<Class<?>> galleryClasses = new LinkedList<Class<?>>();
			for (File f : jarFiles)
			{
				galleryClasses.addAll( ModelResourceUtilities.loadResourceJarFile(f) );
			}
			
			ModelResourceTreeNode galleryTree = ModelResourceUtilities.createClassTree(galleryClasses);
			return galleryTree;
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
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
		return false == edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( typeInJava.getClassReflectionProxy().getReification(), org.lookingglassandalice.storytelling.Scene.class, org.lookingglassandalice.storytelling.Camera.class );
	}
	@Override
	public edu.cmu.cs.dennisc.animation.Program createRuntimeProgramForMovieEncoding( edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice programType, int frameRate ) {
		throw new RuntimeException( "todo" );
//		return new MoveAndTurnRuntimeProgram( sceneType, vm ) {
//			@Override
//			protected java.awt.Component createSpeedMultiplierControlPanel() {
//				return null;
//			}
//			@Override
//			protected edu.cmu.cs.dennisc.animation.Animator createAnimator() {
//				return new edu.cmu.cs.dennisc.animation.FrameBasedAnimator( frameRate );
//			}
//
//			@Override
//			protected void postRun() {
//				super.postRun();
//				this.setMovieEncoder( null );
//			}
//		};
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
		org.alice.stageide.sceneeditor.StorytellingSceneEditor sceneEditor = this.getSceneEditor();
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = sceneEditor.getSGCameraForCreatingThumbnails();
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
		//rv.add( new edu.cmu.cs.dennisc.pattern.Tuple2< String, Class< ? > >( "Key", org.lookingglassandalice.storytelling.Key.class ) );
		return rv;
	}
}
