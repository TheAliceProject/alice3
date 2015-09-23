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

import org.alice.nonfree.NebulousIde;

public abstract class StageIDE extends org.alice.ide.IDE {
	static {
		org.alice.stageide.apis.org.lgna.story.ClassInfoUtilities.loadClassInfos();
	}

	public static final String PERFORM_GENERATED_SET_UP_METHOD_NAME = "performGeneratedSetUp";
	public static final String INITIALIZE_EVENT_LISTENERS_METHOD_NAME = "initializeEventListeners";

	public static StageIDE getActiveInstance() {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( org.alice.ide.IDE.getActiveInstance(), StageIDE.class );
	}

	private org.alice.ide.cascade.ExpressionCascadeManager cascadeManager = NebulousIde.nonfree.newExpressionCascadeManager();

	public StageIDE( org.alice.ide.IdeConfiguration ideConfiguration, edu.cmu.cs.dennisc.crash.CrashDetector crashDetector ) {
		super( ideConfiguration, StoryApiConfigurationManager.getInstance(), crashDetector );
		this.getDocumentFrame().getFrame().addWindowStateListener( new java.awt.event.WindowStateListener() {
			@Override
			public void windowStateChanged( java.awt.event.WindowEvent e ) {
				int oldState = e.getOldState();
				int newState = e.getNewState();
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "windowStateChanged", oldState, newState, java.awt.Frame.ICONIFIED );
				if( ( oldState & java.awt.Frame.ICONIFIED ) == java.awt.Frame.ICONIFIED ) {
					edu.cmu.cs.dennisc.render.RenderUtils.getDefaultRenderFactory().incrementAutomaticDisplayCount();
				}
				if( ( newState & java.awt.Frame.ICONIFIED ) == java.awt.Frame.ICONIFIED ) {
					edu.cmu.cs.dennisc.render.RenderUtils.getDefaultRenderFactory().decrementAutomaticDisplayCount();
				}
			}
		} );
	}

	@Override
	protected String getInnerCommentForMethodName( String methodName ) {
		return org.alice.stageide.ast.StoryApiSpecificAstUtilities.getInnerCommentForMethodName( this.getSceneType(), methodName );
	}

	@Override
	protected org.alice.ide.frametitle.IdeFrameTitleGenerator createFrameTitleGenerator() {
		return new org.alice.ide.frametitle.AliceIdeFrameTitleGenerator();
	}

	@Deprecated
	public org.lgna.project.ast.UserField getSceneField() {
		return org.alice.stageide.ast.StoryApiSpecificAstUtilities.getSceneFieldFromProgramType( this.getProgramType() );
	}

	@Deprecated
	public org.lgna.project.ast.NamedUserType getSceneType() {
		return org.alice.stageide.ast.StoryApiSpecificAstUtilities.getSceneTypeFromProgramType( this.getProgramType() );
	}

	@Override
	public org.alice.stageide.sceneeditor.StorytellingSceneEditor getSceneEditor() {
		return org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance();
	}

	private final edu.cmu.cs.dennisc.pattern.Criterion<org.lgna.project.ast.Declaration> declarationFilter = new edu.cmu.cs.dennisc.pattern.Criterion<org.lgna.project.ast.Declaration>() {
		@Override
		public boolean accept( org.lgna.project.ast.Declaration declaration ) {
			return PERFORM_GENERATED_SET_UP_METHOD_NAME.equals( declaration.getName() ) == false;
		}
	};

	@Override
	protected edu.cmu.cs.dennisc.pattern.Criterion<org.lgna.project.ast.Declaration> getDeclarationFilter() {
		return this.declarationFilter;
	}

	@Override
	protected void registerAdaptersForSceneEditorVm( org.lgna.project.virtualmachine.VirtualMachine vm ) {
		vm.registerAbstractClassAdapter( org.lgna.story.SScene.class, org.alice.stageide.ast.SceneAdapter.class );
		vm.registerProtectedMethodAdapter( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getDeclaredMethod( org.lgna.story.SJointedModel.class, "setJointedModelResource", org.lgna.story.resources.JointedModelResource.class ),
				edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getDeclaredMethod( org.lgna.story.EmployeesOnly.class, "invokeSetJointedModelResource", org.lgna.story.SJointedModel.class, org.lgna.story.resources.JointedModelResource.class ) );
	}

	@Override
	public org.alice.ide.cascade.ExpressionCascadeManager getExpressionCascadeManager() {
		return this.cascadeManager;
	}

	@Override
	protected void promptForLicenseAgreements() {
		final String IS_LICENSE_ACCEPTED_PREFERENCE_KEY = "isLicenseAccepted";
		try {
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( org.lgna.project.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 1 of 2): Alice 3", org.lgna.project.License.TEXT, "Alice" );
			NebulousIde.nonfree.promptForLicenseAgreements( IS_LICENSE_ACCEPTED_PREFERENCE_KEY );
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( "You must accept the license agreements in order to use Alice 3 and The Sims (TM) 2 Art Assets.  Exiting." ).buildAndShow();
			System.exit( -1 );
		}
	}

	private static final org.lgna.project.ast.JavaType COLOR_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Color.class );
	private static final org.lgna.project.ast.JavaType JOINTED_MODEL_RESOURCE_TYPE = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.resources.JointedModelResource.class );

	private javax.swing.Icon getIconFor( org.lgna.project.ast.AbstractField field ) {
		if( field == null ) {
			return null;
		}
		org.lgna.project.ast.AbstractType<?, ?, ?> declaringType = field.getDeclaringType();
		org.lgna.project.ast.AbstractType<?, ?, ?> valueType = field.getValueType();
		if( ( declaringType != null ) && ( valueType != null ) ) {
			if( ( declaringType == COLOR_TYPE ) && ( valueType == COLOR_TYPE ) ) {
				try {
					org.lgna.project.ast.JavaField javaField = (org.lgna.project.ast.JavaField)field;
					org.lgna.story.Color color = (org.lgna.story.Color)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( javaField.getFieldReflectionProxy().getReification(), null );
					java.awt.Color awtColor = org.lgna.story.EmployeesOnly.getAwtColor( color );
					return new org.alice.stageide.icons.ColorIconFactory( awtColor ).getIcon( new java.awt.Dimension( 15, 15 ) );
				} catch( RuntimeException re ) {
					//pass
					edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( re, field );
					return null;
				}
			} else if( declaringType.isAssignableTo( JOINTED_MODEL_RESOURCE_TYPE ) && valueType.isAssignableTo( JOINTED_MODEL_RESOURCE_TYPE ) ) {
				if( field instanceof org.lgna.project.ast.JavaField ) {
					org.lgna.project.ast.JavaField javaField = (org.lgna.project.ast.JavaField)field;
					try {
						org.lgna.story.resources.ModelResource modelResource = (org.lgna.story.resources.ModelResource)javaField.getFieldReflectionProxy().getReification().get( null );
						org.lgna.croquet.icon.IconFactory iconFactory = org.alice.stageide.icons.IconFactoryManager.getIconFactoryForResourceInstance( modelResource );
						return iconFactory.getIcon( new java.awt.Dimension( 20, 15 ) );
					} catch( Exception e ) {
						edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( e, field );
						return null;
					}
				} else {
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
			return accessible.getValueType().isAssignableTo( org.lgna.story.SThing.class );
			//			}
		} else {
			return false;
		}
	}

	@Override
	public org.lgna.croquet.views.AwtComponentView<?> getPrefixPaneForFieldAccessIfAppropriate( org.lgna.project.ast.FieldAccess fieldAccess ) {
		org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
		javax.swing.Icon icon = getIconFor( field );
		if( icon != null ) {
			org.lgna.croquet.views.Label rv = new org.lgna.croquet.views.Label( icon );
			//			rv.setVerticalAlignment( org.lgna.croquet.components.VerticalAlignment.CENTER );
			//			rv.setVerticalTextPosition( org.lgna.croquet.components.VerticalTextPosition.CENTER );
			rv.getAwtComponent().setAlignmentY( 0.5f );
			return rv;
		}
		return super.getPrefixPaneForFieldAccessIfAppropriate( fieldAccess );
	}

	@Override
	public org.lgna.croquet.views.AwtComponentView<?> getPrefixPaneForInstanceCreationIfAppropriate( org.lgna.project.ast.InstanceCreation instanceCreation ) {
		org.lgna.project.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
		if( constructor != null ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> type = constructor.getDeclaringType();
			if( COLOR_TYPE.isAssignableFrom( type ) ) {
				org.lgna.croquet.views.Label rv = new org.lgna.croquet.views.Label();
				org.lgna.story.Color color = this.getSceneEditor().getInstanceInJavaVMForExpression( instanceCreation, org.lgna.story.Color.class );
				java.awt.Color awtColor = org.lgna.story.EmployeesOnly.getAwtColor( color );
				rv.setIcon( new edu.cmu.cs.dennisc.javax.swing.icons.ColorIcon( awtColor ) );
				return rv;
			}
		}
		return super.getPrefixPaneForInstanceCreationIfAppropriate( instanceCreation );
	}

	@Override
	public boolean isDropDownDesiredFor( org.lgna.project.ast.Expression expression ) {
		if( super.isDropDownDesiredFor( expression ) ) {
			if( expression != null ) {
				if( expression instanceof org.lgna.project.ast.LambdaExpression ) {
					return false;
				} else {
					org.lgna.project.ast.Node parent = expression.getParent();
					if( parent instanceof org.lgna.project.ast.FieldAccess ) {
						org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)parent;
						org.lgna.project.ast.AbstractField field = fieldAccess.field.getValue();
						assert field != null;
						org.lgna.project.ast.AbstractType<?, ?, ?> declaringType = field.getDeclaringType();
						if( ( declaringType != null ) && declaringType.isAssignableTo( org.lgna.story.SScene.class ) ) {
							if( field.getValueType().isAssignableTo( org.lgna.story.STurnable.class ) ) {
								return false;
							}
						}
					} else if( parent instanceof org.lgna.project.ast.AbstractArgument ) {
						org.lgna.project.ast.AbstractArgument argument = (org.lgna.project.ast.AbstractArgument)parent;
						org.lgna.project.ast.Node grandparent = argument.getParent();
						if( grandparent instanceof org.lgna.project.ast.InstanceCreation ) {
							org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)grandparent;
							org.lgna.project.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
							if( constructor != null ) {
								org.lgna.project.ast.AbstractType<?, ?, ?> type = constructor.getDeclaringType();
								return ( COLOR_TYPE.isAssignableFrom( type ) || NebulousIde.nonfree.isPersonResourceTypeAssingleFrom( type ) ) == false;
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
	public org.lgna.croquet.Operation createPreviewOperation( org.alice.ide.members.components.templates.ProcedureInvocationTemplate procedureInvocationTemplate ) {
		return new org.alice.stageide.croquet.models.run.PreviewMethodOperation( procedureInvocationTemplate );
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
	public org.lgna.croquet.Operation getAboutOperation() {
		return org.alice.ide.IdeApp.INSTANCE.getAboutDialogLaunchOperation();
	}

	public java.util.List<org.lgna.project.ast.UserMethod> getUserMethodsInvokedFromSceneActivationListeners() {
		return org.alice.stageide.ast.StoryApiSpecificAstUtilities.getUserMethodsInvokedSceneActivationListeners( this.getSceneType() );
	}

	private void setRootField( final org.lgna.project.ast.UserField rootField ) {
		final org.lgna.project.ast.NamedUserType type;
		if( rootField != null ) {
			type = (org.lgna.project.ast.NamedUserType)rootField.getValueType();
		} else {
			type = null;
		}
		if( type != null ) {
			//org.alice.ide.declarationseditor.TypeState.getInstance().setValueTransactionlessly( type );
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					final int N = type.fields.size();
					int i = N;
					while( i > 0 ) {
						i--;
						org.lgna.project.ast.UserField field = type.fields.get( i );
						if( field.managementLevel.getValue() == org.lgna.project.ast.ManagementLevel.MANAGED ) {
							if( getApiConfigurationManager().isInstanceFactoryDesiredForType( field.getValueType() ) ) {
								getDocumentFrame().getInstanceFactoryState().setValueTransactionlessly( org.alice.ide.instancefactory.ThisFieldAccessFactory.getInstance( field ) );
								break;
							}
						}
					}
				}
			} );
		}
		org.alice.ide.ast.AstEventManager.fireTypeHierarchyListeners();
	}

	@Override
	public void setProject( org.lgna.project.Project project ) {
		super.setProject( project );

		this.getDocumentFrame().getInstanceFactoryState().pushIgnoreAstChanges();
		try {
			this.setRootField( this.getSceneField() );
		} finally {
			this.getDocumentFrame().getInstanceFactoryState().popIgnoreAstChanges();
		}

		org.alice.ide.declarationseditor.DeclarationTabState tabState = this.getDocumentFrame().getDeclarationsEditorComposite().getTabState();
		tabState.clear();
		if( project != null ) {
			org.lgna.project.ast.NamedUserType programType = project.getProgramType();
			org.lgna.project.ast.NamedUserType sceneType = org.alice.stageide.ast.StoryApiSpecificAstUtilities.getSceneTypeFromProgramType( programType );
			if( sceneType != null ) {
				org.lgna.croquet.data.ListData<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>> data = tabState.getData();

				data.internalAddItem( org.alice.ide.declarationseditor.TypeComposite.getInstance( sceneType ) );

				java.util.List<org.lgna.project.ast.AbstractMethod> methods = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				methods.add( sceneType.findMethod( INITIALIZE_EVENT_LISTENERS_METHOD_NAME ) );
				methods.addAll( this.getUserMethodsInvokedFromSceneActivationListeners() );

				for( org.lgna.project.ast.AbstractMethod method : methods ) {
					if( method != null ) {
						if( method.getDeclaringType() == sceneType ) {
							data.internalAddItem( org.alice.ide.declarationseditor.CodeComposite.getInstance( method ) );
						}
					}
				}
				tabState.setValueTransactionlessly( data.getItemAt( data.getItemCount() - 1 ) );
			}
		}
		org.alice.stageide.icons.SceneIconFactory.getInstance().markAllIconsDirty();
	}

	@Override
	public boolean isInstanceCreationAllowableFor( org.lgna.project.ast.NamedUserType userType ) {
		org.lgna.project.ast.JavaType javaType = userType.getFirstEncounteredJavaType();
		return false == edu.cmu.cs.dennisc.java.lang.ClassUtilities.isAssignableToAtLeastOne( javaType.getClassReflectionProxy().getReification(), org.lgna.story.SScene.class, org.lgna.story.SCamera.class );
	}

	private org.alice.stageide.sceneeditor.ThumbnailGenerator thumbnailGenerator;

	@Override
	protected java.awt.image.BufferedImage createThumbnail() throws Throwable {
		if( thumbnailGenerator != null ) {
			//pass
		} else {
			thumbnailGenerator = new org.alice.stageide.sceneeditor.ThumbnailGenerator( org.lgna.story.resourceutilities.AbstractThumbnailMaker.DEFAULT_THUMBNAIL_WIDTH, org.lgna.story.resourceutilities.AbstractThumbnailMaker.DEFAULT_THUMBNAIL_HEIGHT );
		}
		return this.thumbnailGenerator.createThumbnail();
	}

	@Override
	public org.lgna.project.ast.UserMethod getPerformEditorGeneratedSetUpMethod() {
		org.lgna.project.ast.NamedUserType sceneType = this.getSceneType();
		return org.alice.stageide.ast.StoryApiSpecificAstUtilities.getPerformEditorGeneratedSetUpMethod( sceneType );
	}

	private org.alice.ide.instancefactory.InstanceFactory getInstanceFactoryForSceneOrSceneField( org.lgna.project.ast.UserField field ) {
		org.lgna.project.ast.NamedUserType programType = this.getProgramType();
		if( programType != null ) {
			org.lgna.project.ast.NamedUserType sceneType = org.alice.stageide.ast.StoryApiSpecificAstUtilities.getSceneTypeFromProgramType( programType );
			if( sceneType != null ) {
				org.lgna.project.ast.NamedUserType scopeType = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().getValue();
				if( scopeType == sceneType ) {
					if( field != null ) {
						return org.alice.ide.instancefactory.ThisFieldAccessFactory.getInstance( field );
					} else {
						return org.alice.ide.instancefactory.ThisInstanceFactory.getInstance();
					}
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public org.alice.ide.instancefactory.InstanceFactory getInstanceFactoryForScene() {
		return this.getInstanceFactoryForSceneOrSceneField( null );
	}

	public org.alice.ide.instancefactory.InstanceFactory getInstanceFactoryForSceneField( org.lgna.project.ast.UserField field ) {
		assert field != null : this;
		return this.getInstanceFactoryForSceneOrSceneField( field );
	}

}
