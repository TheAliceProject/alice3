/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.stageide;

import edu.cmu.cs.dennisc.crash.CrashDetector;
import edu.cmu.cs.dennisc.eula.EULAUtilities;
import edu.cmu.cs.dennisc.eula.LicenseRejectedException;
import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.icons.ColorIcon;
import edu.cmu.cs.dennisc.javax.swing.option.OkDialog;
import edu.cmu.cs.dennisc.pattern.Criterion;
import edu.cmu.cs.dennisc.render.RenderUtils;
import org.alice.ide.IDE;
import org.alice.ide.IdeApp;
import org.alice.ide.IdeConfiguration;
import org.alice.ide.ast.AstEventManager;
import org.alice.ide.cascade.ExpressionCascadeManager;
import org.alice.ide.declarationseditor.CodeComposite;
import org.alice.ide.declarationseditor.DeclarationComposite;
import org.alice.ide.declarationseditor.DeclarationTabState;
import org.alice.ide.declarationseditor.TypeComposite;
import org.alice.ide.frametitle.AliceIdeFrameTitleGenerator;
import org.alice.ide.frametitle.IdeFrameTitleGenerator;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.instancefactory.ThisFieldAccessFactory;
import org.alice.ide.instancefactory.ThisInstanceFactory;
import org.alice.ide.members.components.templates.ProcedureInvocationTemplate;
import org.alice.nonfree.NebulousIde;
import org.alice.stageide.ast.SceneAdapter;
import org.alice.stageide.ast.StoryApiSpecificAstUtilities;
import org.alice.stageide.croquet.models.run.PreviewMethodOperation;
import org.alice.stageide.icons.ColorIconFactory;
import org.alice.stageide.icons.IconFactoryManager;
import org.alice.stageide.icons.SceneIconFactory;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.alice.stageide.sceneeditor.ThumbnailGenerator;
import org.lgna.croquet.Operation;
import org.lgna.croquet.data.ListData;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.Label;
import org.lgna.project.License;
import org.lgna.project.Project;
import org.lgna.project.ast.AbstractArgument;
import org.lgna.project.ast.AbstractConstructor;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Accessible;
import org.lgna.project.ast.Declaration;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaField;
import org.lgna.project.ast.JavaType;
import org.lgna.project.ast.LambdaExpression;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.virtualmachine.VirtualMachine;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SCamera;
import org.lgna.story.SJointedModel;
import org.lgna.story.SScene;
import org.lgna.story.SThing;
import org.lgna.story.STurnable;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.ModelResource;
import org.lgna.story.resourceutilities.AbstractThumbnailMaker;

import javax.swing.Icon;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.util.List;

public abstract class StageIDE extends IDE {
	public static final String PERFORM_GENERATED_SET_UP_METHOD_NAME = "performGeneratedSetUp";
	public static final String INITIALIZE_EVENT_LISTENERS_METHOD_NAME = "initializeEventListeners";

	public static StageIDE getActiveInstance() {
		return ClassUtilities.getInstance( IDE.getActiveInstance(), StageIDE.class );
	}

	private ExpressionCascadeManager cascadeManager = NebulousIde.nonfree.newExpressionCascadeManager();

	public StageIDE( IdeConfiguration ideConfiguration, CrashDetector crashDetector ) {
		super( ideConfiguration, StoryApiConfigurationManager.getInstance(), crashDetector );
		this.getDocumentFrame().getFrame().addWindowStateListener( new WindowStateListener() {
			@Override
			public void windowStateChanged( WindowEvent e ) {
				int oldState = e.getOldState();
				int newState = e.getNewState();
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "windowStateChanged", oldState, newState, java.awt.Frame.ICONIFIED );
				if( ( oldState & Frame.ICONIFIED ) == Frame.ICONIFIED ) {
					RenderUtils.getDefaultRenderFactory().incrementAutomaticDisplayCount();
				}
				if( ( newState & Frame.ICONIFIED ) == Frame.ICONIFIED ) {
					RenderUtils.getDefaultRenderFactory().decrementAutomaticDisplayCount();
				}
			}
		} );
	}

	@Override
	protected String getInnerCommentForMethodName( String methodName ) {
		return StoryApiSpecificAstUtilities.getInnerCommentForMethodName( this.getSceneType(), methodName );
	}

	@Override
	protected IdeFrameTitleGenerator createFrameTitleGenerator() {
		return new AliceIdeFrameTitleGenerator();
	}

	@Deprecated
	public UserField getSceneField() {
		return StoryApiSpecificAstUtilities.getSceneFieldFromProgramType( this.getProgramType() );
	}

	@Deprecated
	public NamedUserType getSceneType() {
		return StoryApiSpecificAstUtilities.getSceneTypeFromProgramType( this.getProgramType() );
	}

	@Override
	public StorytellingSceneEditor getSceneEditor() {
		return StorytellingSceneEditor.getInstance();
	}

	private final Criterion<Declaration> declarationFilter = new Criterion<Declaration>() {
		@Override
		public boolean accept( Declaration declaration ) {
			return PERFORM_GENERATED_SET_UP_METHOD_NAME.equals( declaration.getName() ) == false;
		}
	};

	@Override
	protected Criterion<Declaration> getDeclarationFilter() {
		return this.declarationFilter;
	}

	@Override
	protected void registerAdaptersForSceneEditorVm( VirtualMachine vm ) {
		vm.registerAbstractClassAdapter( SScene.class, SceneAdapter.class );
		vm.registerProtectedMethodAdapter( ReflectionUtilities.getDeclaredMethod( SJointedModel.class, "setJointedModelResource", JointedModelResource.class ),
				ReflectionUtilities.getDeclaredMethod( EmployeesOnly.class, "invokeSetJointedModelResource", SJointedModel.class, JointedModelResource.class ) );
	}

	@Override
	public ExpressionCascadeManager getExpressionCascadeManager() {
		return this.cascadeManager;
	}

	@Override
	protected void promptForLicenseAgreements() {
		final String IS_LICENSE_ACCEPTED_PREFERENCE_KEY = "isLicenseAccepted";
		try {
			EULAUtilities.promptUserToAcceptEULAIfNecessary( License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 1 of 2): Alice 3", License.TEXT, "Alice" );
			NebulousIde.nonfree.promptForLicenseAgreements( IS_LICENSE_ACCEPTED_PREFERENCE_KEY );
		} catch( LicenseRejectedException lre ) {
			new OkDialog.Builder( "You must accept the license agreements in order to use Alice 3 and The Sims (TM) 2 Art Assets.  Exiting." ).buildAndShow();
			System.exit( -1 );
		}
	}

	private static final JavaType COLOR_TYPE = JavaType.getInstance( org.lgna.story.Color.class );
	private static final JavaType JOINTED_MODEL_RESOURCE_TYPE = JavaType.getInstance( JointedModelResource.class );

	private Icon getIconFor( AbstractField field ) {
		if( field == null ) {
			return null;
		}
		AbstractType<?, ?, ?> declaringType = field.getDeclaringType();
		AbstractType<?, ?, ?> valueType = field.getValueType();
		if( ( declaringType != null ) && ( valueType != null ) ) {
			if( ( declaringType == COLOR_TYPE ) && ( valueType == COLOR_TYPE ) ) {
				try {
					JavaField javaField = (JavaField)field;
					org.lgna.story.Color color = (org.lgna.story.Color)ReflectionUtilities.get( javaField.getFieldReflectionProxy().getReification(), null );
					Color awtColor = EmployeesOnly.getAwtColor( color );
					return new ColorIconFactory( awtColor ).getIcon( new Dimension( 15, 15 ) );
				} catch( RuntimeException re ) {
					//pass
					Logger.throwable( re, field );
					return null;
				}
			} else if( declaringType.isAssignableTo( JOINTED_MODEL_RESOURCE_TYPE ) && valueType.isAssignableTo( JOINTED_MODEL_RESOURCE_TYPE ) ) {
				if( field instanceof JavaField ) {
					JavaField javaField = (JavaField)field;
					try {
						ModelResource modelResource = (ModelResource)javaField.getFieldReflectionProxy().getReification().get( null );
						IconFactory iconFactory = IconFactoryManager.getIconFactoryForResourceInstance( modelResource );
						return iconFactory.getIcon( new Dimension( 20, 15 ) );
					} catch( Exception e ) {
						Logger.throwable( e, field );
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
	protected boolean isAccessibleDesired( Accessible accessible ) {
		if( super.isAccessibleDesired( accessible ) ) {
			//			if( accessible.getValueType().isAssignableTo( org.lookingglassandalice.storytelling.Marker.class) ) {
			//				return false;
			//			} else {
			return accessible.getValueType().isAssignableTo( SThing.class );
			//			}
		} else {
			return false;
		}
	}

	@Override
	public AwtComponentView<?> getPrefixPaneForFieldAccessIfAppropriate( FieldAccess fieldAccess ) {
		AbstractField field = fieldAccess.field.getValue();
		Icon icon = getIconFor( field );
		if( icon != null ) {
			Label rv = new Label( icon );
			//			rv.setVerticalAlignment( org.lgna.croquet.components.VerticalAlignment.CENTER );
			//			rv.setVerticalTextPosition( org.lgna.croquet.components.VerticalTextPosition.CENTER );
			rv.getAwtComponent().setAlignmentY( 0.5f );
			return rv;
		}
		return super.getPrefixPaneForFieldAccessIfAppropriate( fieldAccess );
	}

	@Override
	public AwtComponentView<?> getPrefixPaneForInstanceCreationIfAppropriate( InstanceCreation instanceCreation ) {
		AbstractConstructor constructor = instanceCreation.constructor.getValue();
		if( constructor != null ) {
			AbstractType<?, ?, ?> type = constructor.getDeclaringType();
			if( COLOR_TYPE.isAssignableFrom( type ) ) {
				Label rv = new Label();
				org.lgna.story.Color color = this.getSceneEditor().getInstanceInJavaVMForExpression( instanceCreation, org.lgna.story.Color.class );
				Color awtColor = EmployeesOnly.getAwtColor( color );
				rv.setIcon( new ColorIcon( awtColor ) );
				return rv;
			}
		}
		return super.getPrefixPaneForInstanceCreationIfAppropriate( instanceCreation );
	}

	@Override
	public boolean isDropDownDesiredFor( Expression expression ) {
		if( super.isDropDownDesiredFor( expression ) ) {
			if( expression != null ) {
				if( expression instanceof LambdaExpression ) {
					return false;
				} else {
					Node parent = expression.getParent();
					if( parent instanceof FieldAccess ) {
						FieldAccess fieldAccess = (FieldAccess)parent;
						AbstractField field = fieldAccess.field.getValue();
						assert field != null;
						AbstractType<?, ?, ?> declaringType = field.getDeclaringType();
						if( ( declaringType != null ) && declaringType.isAssignableTo( SScene.class ) ) {
							if( field.getValueType().isAssignableTo( STurnable.class ) ) {
								return false;
							}
						}
					} else if( parent instanceof AbstractArgument ) {
						AbstractArgument argument = (AbstractArgument)parent;
						Node grandparent = argument.getParent();
						if( grandparent instanceof InstanceCreation ) {
							InstanceCreation instanceCreation = (InstanceCreation)grandparent;
							AbstractConstructor constructor = instanceCreation.constructor.getValue();
							if( constructor != null ) {
								AbstractType<?, ?, ?> type = constructor.getDeclaringType();
								return ( COLOR_TYPE.isAssignableFrom( type ) || NebulousIde.nonfree.isPersonResourceTypeAssingleFrom( type ) ) == false;
							}
						}
					} else if( parent instanceof MethodInvocation ) {
						MethodInvocation methodInvocation = (MethodInvocation)parent;
						if( StoryApiConfigurationManager.getInstance().isBuildMethod( methodInvocation ) ) {
							return false;
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
	public Operation createPreviewOperation( ProcedureInvocationTemplate procedureInvocationTemplate ) {
		return new PreviewMethodOperation( procedureInvocationTemplate );
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
	public Operation getAboutOperation() {
		return IdeApp.INSTANCE.getAboutDialogLaunchOperation();
	}

	public List<UserMethod> getUserMethodsInvokedFromSceneActivationListeners() {
		return StoryApiSpecificAstUtilities.getUserMethodsInvokedSceneActivationListeners( this.getSceneType() );
	}

	private void setRootField( final UserField rootField ) {
		final NamedUserType type;
		if( rootField != null ) {
			type = (NamedUserType)rootField.getValueType();
		} else {
			type = null;
		}
		if( type != null ) {
			//org.alice.ide.declarationseditor.TypeState.getInstance().setValueTransactionlessly( type );
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					final int N = type.fields.size();
					int i = N;
					while( i > 0 ) {
						i--;
						UserField field = type.fields.get( i );
						if( field.managementLevel.getValue() == ManagementLevel.MANAGED ) {
							if( getApiConfigurationManager().isInstanceFactoryDesiredForType( field.getValueType() ) ) {
								getDocumentFrame().getInstanceFactoryState().setValueTransactionlessly( ThisFieldAccessFactory.getInstance( field ) );
								break;
							}
						}
					}
				}
			} );
		}
		AstEventManager.fireTypeHierarchyListeners();
	}

	@Override
	public void setProject( Project project ) {
		super.setProject( project );

		this.getDocumentFrame().getInstanceFactoryState().pushIgnoreAstChanges();
		try {
			this.setRootField( this.getSceneField() );
		} finally {
			this.getDocumentFrame().getInstanceFactoryState().popIgnoreAstChanges();
		}

		DeclarationTabState tabState = this.getDocumentFrame().getDeclarationsEditorComposite().getTabState();
		tabState.clear();
		if( project != null ) {
			NamedUserType programType = project.getProgramType();
			NamedUserType sceneType = StoryApiSpecificAstUtilities.getSceneTypeFromProgramType( programType );
			if( sceneType != null ) {
				ListData<DeclarationComposite<?, ?>> data = tabState.getData();

				data.internalAddItem( TypeComposite.getInstance( sceneType ) );

				List<AbstractMethod> methods = Lists.newLinkedList();
				methods.add( sceneType.findMethod( INITIALIZE_EVENT_LISTENERS_METHOD_NAME ) );
				methods.addAll( this.getUserMethodsInvokedFromSceneActivationListeners() );

				for( AbstractMethod method : methods ) {
					if( method != null ) {
						if( method.getDeclaringType() == sceneType ) {
							data.internalAddItem( CodeComposite.getInstance( method ) );
						}
					}
				}
				tabState.setValueTransactionlessly( data.getItemAt( data.getItemCount() - 1 ) );
			}
		}
		SceneIconFactory.getInstance().markAllIconsDirty();
	}

	@Override
	public boolean isInstanceCreationAllowableFor( NamedUserType userType ) {
		JavaType javaType = userType.getFirstEncounteredJavaType();
		return false == ClassUtilities.isAssignableToAtLeastOne( javaType.getClassReflectionProxy().getReification(), SScene.class, SCamera.class );
	}

	private ThumbnailGenerator thumbnailGenerator;

	@Override
	protected BufferedImage createThumbnail() throws Throwable {
		if( thumbnailGenerator != null ) {
			//pass
		} else {
			thumbnailGenerator = new ThumbnailGenerator( AbstractThumbnailMaker.DEFAULT_THUMBNAIL_WIDTH, AbstractThumbnailMaker.DEFAULT_THUMBNAIL_HEIGHT );
		}
		return this.thumbnailGenerator.createThumbnail();
	}

	@Override
	public UserMethod getPerformEditorGeneratedSetUpMethod() {
		NamedUserType sceneType = this.getSceneType();
		return StoryApiSpecificAstUtilities.getPerformEditorGeneratedSetUpMethod( sceneType );
	}

	private InstanceFactory getInstanceFactoryForSceneOrSceneField( UserField field ) {
		NamedUserType programType = this.getProgramType();
		if( programType != null ) {
			NamedUserType sceneType = StoryApiSpecificAstUtilities.getSceneTypeFromProgramType( programType );
			if( sceneType != null ) {
				NamedUserType scopeType = IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().getValue();
				if( scopeType == sceneType ) {
					if( field != null ) {
						return ThisFieldAccessFactory.getInstance( field );
					} else {
						return ThisInstanceFactory.getInstance();
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

	public InstanceFactory getInstanceFactoryForScene() {
		return this.getInstanceFactoryForSceneOrSceneField( null );
	}

	public InstanceFactory getInstanceFactoryForSceneField( UserField field ) {
		assert field != null : this;
		return this.getInstanceFactoryForSceneOrSceneField( field );
	}

}
