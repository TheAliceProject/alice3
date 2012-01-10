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
	public static final String PERFORM_GENERATED_SET_UP_METHOD_NAME = "performGeneratedSetUp";
	public static final String INITIALIZE_EVENT_LISTENERS_METHOD_NAME = "initializeEventListeners";

	public static StageIDE getActiveInstance() {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance(  org.alice.ide.IDE.getActiveInstance(), StageIDE.class );
	}
	private org.alice.ide.cascade.CascadeManager cascadeManager = new org.alice.stageide.cascade.CascadeManager();
	public StageIDE() {
		this.getFrame().addWindowStateListener( new java.awt.event.WindowStateListener() {
			public void windowStateChanged( java.awt.event.WindowEvent e ) {
				int oldState = e.getOldState();
				int newState = e.getNewState();
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "windowStateChanged", oldState, newState, java.awt.Frame.ICONIFIED );
				if( (oldState & java.awt.Frame.ICONIFIED) == java.awt.Frame.ICONIFIED ) {
					edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().incrementAutomaticDisplayCount();
				}
				if( (newState & java.awt.Frame.ICONIFIED) == java.awt.Frame.ICONIFIED ) {
					edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getInstance().decrementAutomaticDisplayCount();
				}
			}
		} );
	}
	@Override
	public org.alice.stageide.sceneeditor.StorytellingSceneEditor getSceneEditor() {
		return org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance();
	}
	@Override
	public org.alice.ide.ApiConfigurationManager getApiConfigurationManager() {
		return StoryApiConfigurationManager.getInstance();
	}
	@Override
	protected void registerAdapters(org.lgna.project.virtualmachine.VirtualMachine vm) {
		vm.registerAnonymousAdapter( org.lgna.story.Scene.class, org.alice.stageide.ast.SceneAdapter.class );
		vm.registerAnonymousAdapter( org.lgna.story.event.SceneActivationListener.class, org.alice.stageide.apis.story.event.SceneActivationAdapter.class );
	}
	@Override
	public org.alice.ide.cascade.CascadeManager getCascadeManager() {
		return this.cascadeManager;
	}
	
	@Override
	protected void promptForLicenseAgreements() {
		final String IS_LICENSE_ACCEPTED_PREFERENCE_KEY = "isLicenseAccepted";
		try {
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( org.lgna.project.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 1 of 3): Alice 3", org.lgna.project.License.TEXT, "Alice" );
//			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.wustl.cse.lookingglass.apis.walkandtouch.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 2 of 3): Looking Glass Walk & Touch API",
//					edu.wustl.cse.lookingglass.apis.walkandtouch.License.TEXT_FOR_USE_IN_ALICE, "the Looking Glass Walk & Touch API" );
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.cmu.cs.dennisc.nebulous.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 3 of 3): The Sims (TM) 2 Art Assets",
					edu.cmu.cs.dennisc.nebulous.License.TEXT, "The Sims (TM) 2 Art Assets" );
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			this.showMessageDialog( "You must accept the license agreements in order to use Alice 3, the Looking Glass Walk & Touch API, and The Sims (TM) 2 Art Assets.  Exiting." );
			System.exit( -1 );
		}
	}

	private static final org.lgna.project.ast.JavaType COLOR_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Color.class );
	private static final org.lgna.project.ast.JavaType JOINTED_MODEL_RESOURCE_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.resources.JointedModelResource.class );

	private java.util.Map< org.lgna.project.ast.AbstractField, org.alice.ide.swing.icons.ColorIcon > mapFieldToIcon = new java.util.HashMap< org.lgna.project.ast.AbstractField, org.alice.ide.swing.icons.ColorIcon >();

	private javax.swing.Icon getIconFor( org.lgna.project.ast.AbstractField field ) {
		org.lgna.project.ast.AbstractType< ?,?,? > declaringType = field.getDeclaringType();
		org.lgna.project.ast.AbstractType< ?,?,? > valueType = field.getValueType();
		if( declaringType != null && valueType != null ) {
			if( declaringType == COLOR_TYPE && valueType == COLOR_TYPE ) {
				org.alice.ide.swing.icons.ColorIcon rv = this.mapFieldToIcon.get( field );
				if( rv != null ) {
					//pass
				} else {
					try {
						org.lgna.project.ast.JavaField javaField = (org.lgna.project.ast.JavaField)field;
						org.lgna.story.Color color = (org.lgna.story.Color)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( javaField.getFieldReflectionProxy().getReification(), null );
						rv = new org.alice.ide.swing.icons.ColorIcon( org.lgna.story.ImplementationAccessor.getColor4f( color ).getAsAWTColor() );
						this.mapFieldToIcon.put( field, rv );
					} catch( RuntimeException re ) {
						//pass
					}
				}
				return rv;
			} else if( declaringType.isAssignableTo( JOINTED_MODEL_RESOURCE_TYPE ) && valueType.isAssignableTo( JOINTED_MODEL_RESOURCE_TYPE ) ) {
				Class<?> resourceClass = ((org.lgna.project.ast.JavaType)field.getValueType()).getClassReflectionProxy().getReification();
				java.awt.image.BufferedImage thumbnail = org.lgna.story.implementation.alice.AliceResourceUtilties.getThumbnail(resourceClass, field.getName());
				if( thumbnail != null ) {
					return new edu.cmu.cs.dennisc.javax.swing.icons.ScaledImageIcon( thumbnail, 20, 20 );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( resourceClass, field.getName() );
					return null;
				}
			} else {
				return null;
			}
		}
		return null;
	}

	@Override
	protected boolean isAccessibleDesired( org.lgna.project.ast.Accessible accessible ) {
		if( super.isAccessibleDesired( accessible ) ) {
//			if( accessible.getValueType().isAssignableTo( org.lookingglassandalice.storytelling.Marker.class) ) {
//				return false;
//			} else {
				return accessible.getValueType().isAssignableTo( org.lgna.story.Entity.class );
//			}
		} else {
			return false;
		}
	}
	@Override
	public org.lgna.croquet.components.Component< ? > getPrefixPaneForFieldAccessIfAppropriate( org.lgna.project.ast.FieldAccess fieldAccess ) {
		org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
		javax.swing.Icon icon = getIconFor( field );
		if( icon != null ) {
			org.lgna.croquet.components.Label rv = new org.lgna.croquet.components.Label( icon );
//			rv.setVerticalAlignment( org.lgna.croquet.components.VerticalAlignment.CENTER );
//			rv.setVerticalTextPosition( org.lgna.croquet.components.VerticalTextPosition.CENTER );
			rv.getAwtComponent().setAlignmentY( 0.5f );
			return rv;
		}
		return super.getPrefixPaneForFieldAccessIfAppropriate( fieldAccess );
	}

	@Override
	public boolean isDropDownDesiredFor( org.lgna.project.ast.Expression expression ) {
		if( super.isDropDownDesiredFor( expression ) ) {
			if( expression != null ) {
				if (expression instanceof org.lgna.project.ast.InstanceCreation) {
					org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation) expression;
					org.lgna.project.ast.AbstractType<?,?,?> type = instanceCreation.getType();
					if( type instanceof org.lgna.project.ast.AnonymousUserType ) {
						if( type.isAssignableTo( org.lgna.story.event.KeyListener.class ) || type.isAssignableTo( org.lgna.story.event.MouseButtonListener.class ) ) {
							return false;
						}
					}
				} else if( expression instanceof org.lgna.project.ast.LambdaExpression ) {
					return false;
				} else {
					org.lgna.project.ast.Node parent = expression.getParent();
					if( parent instanceof org.lgna.project.ast.FieldAccess ) {
						org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)parent;
						org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
						assert field != null;
						org.lgna.project.ast.AbstractType< ?,?,? > declaringType = field.getDeclaringType();
						if( declaringType != null && declaringType.isAssignableTo( org.lgna.story.Scene.class ) ) {
							if( field.getValueType().isAssignableTo( org.lgna.story.Turnable.class ) ) {
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
		return org.alice.stageide.croquet.models.run.RunOperation.getInstance();
	}
	@Override
	public org.lgna.croquet.Operation< ? > getRestartOperation() {
		return org.alice.stageide.croquet.models.run.RestartOperation.getInstance();
	}
	@Override
	public org.lgna.croquet.Operation<?> createPreviewOperation( org.alice.ide.members.components.templates.ProcedureInvocationTemplate procedureInvocationTemplate ) {
		return new org.alice.stageide.croquet.models.run.PreviewMethodOperation( procedureInvocationTemplate );
	}

	@Override
	public org.lgna.croquet.ListSelectionState< org.alice.ide.perspectives.IdePerspective > getPerspectiveState() {
		return org.alice.stageide.perspectives.PerspectiveState.getInstance();
	}

//	@Override
//	public void handlePreviewMethod( edu.cmu.cs.dennisc.croquet.ModelContext context, org.lgna.project.ast.MethodInvocation emptyExpressionMethodInvocation ) {
//		this.ensureProjectCodeUpToDate();
//		org.lgna.project.ast.AbstractField field = this.getFieldSelectionState().getValue();
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
	public org.lgna.croquet.Operation< ? > getAboutOperation() {
		return org.alice.stageide.croquet.models.help.AboutOperation.getInstance();
	}
	@Override
	public void setProject( org.lgna.project.Project project ) {
		super.setProject( project );
		if( project != null ) {
			org.lgna.project.ast.NamedUserType programType = project.getProgramType();
			org.lgna.project.ast.NamedUserType sceneType = getSceneTypeFromProgramType( programType );
			if( sceneType != null ) {
				this.setFocusedCode( sceneType.findMethod( "myFirstMethod" ) );
			}
		}
	}
	@Override
	public boolean isInstanceCreationAllowableFor( org.lgna.project.ast.NamedUserType userType ) {
		org.lgna.project.ast.JavaType javaType = userType.getFirstEncounteredJavaType();
		return false == edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( javaType.getClassReflectionProxy().getReification(), org.lgna.story.Scene.class, org.lgna.story.Camera.class );
	}
	@Override
	public edu.cmu.cs.dennisc.animation.Program createRuntimeProgramForMovieEncoding( org.lgna.project.virtualmachine.VirtualMachine vm, org.lgna.project.ast.NamedUserType programType, int frameRate ) {
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
	@Override
	protected java.awt.image.BufferedImage createThumbnail() throws Throwable {
		return org.alice.stageide.sceneeditor.ThumbnailGenerator.createThumbnail( THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT );
	}
	@Override
	public org.lgna.project.ast.UserMethod getPerformEditorGeneratedSetUpMethod() {
		org.lgna.project.ast.NamedUserType sceneType = this.getSceneType();
		if( sceneType != null ) {
			for( org.lgna.project.ast.UserMethod method : sceneType.methods ) {
				if( PERFORM_GENERATED_SET_UP_METHOD_NAME.equals( method.name.getValue() ) ) {
					return method;
				}
			}
		}
		return null;
	}
}
