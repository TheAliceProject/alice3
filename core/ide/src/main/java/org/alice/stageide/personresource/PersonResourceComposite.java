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

package org.alice.stageide.personresource;

import org.lgna.croquet.ValueCreator;

/**
 * @author Dennis Cosgrove
 */
public final class PersonResourceComposite extends org.lgna.croquet.ValueCreatorInputDialogCoreComposite<org.lgna.croquet.views.Panel, org.lgna.story.resources.sims2.PersonResource> {
	private static class SingletonHolder {
		private static PersonResourceComposite instance = new PersonResourceComposite();
	}

	public static PersonResourceComposite getInstance() {
		return SingletonHolder.instance;
	}

	private static final class PersonResourceToExpressionConverter extends org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.InstanceCreation> {
		public PersonResourceToExpressionConverter( ValueCreator<org.lgna.story.resources.sims2.PersonResource> valueCreator ) {
			super( java.util.UUID.fromString( "40894b2c-ebdc-4101-bc17-d920f3298d89" ), valueCreator );
		}

		@Override
		protected org.lgna.project.ast.InstanceCreation convert( org.lgna.story.resources.sims2.PersonResource value ) {
			try {
				org.lgna.project.ast.InstanceCreation instanceCreation = org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSims2PersonRecourseInstanceCreation( value );
				return instanceCreation;
			} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
				throw new RuntimeException( ccee );
			}
		}
	}

	private final org.lgna.croquet.ValueCreator<org.lgna.story.resources.sims2.PersonResource> randomElderValueCreator = new org.lgna.croquet.OwnedByCompositeValueCreator<org.lgna.story.resources.sims2.PersonResource>( this, new org.lgna.croquet.OwnedByCompositeValueCreator.Initializer() {
		@Override
		public void initialize( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
			PersonResourceComposite.this.initializeRandom( org.lgna.story.resources.sims2.LifeStage.ELDER );
		}
	}, "randomElder" );
	private final org.lgna.croquet.ValueCreator<org.lgna.story.resources.sims2.PersonResource> randomAdultValueCreator = new org.lgna.croquet.OwnedByCompositeValueCreator<org.lgna.story.resources.sims2.PersonResource>( this, new org.lgna.croquet.OwnedByCompositeValueCreator.Initializer() {
		@Override
		public void initialize( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
			PersonResourceComposite.this.initializeRandom( org.lgna.story.resources.sims2.LifeStage.ADULT );
		}
	}, "randomAdult" );
	private final org.lgna.croquet.ValueCreator<org.lgna.story.resources.sims2.PersonResource> randomTeenValueCreator = new org.lgna.croquet.OwnedByCompositeValueCreator<org.lgna.story.resources.sims2.PersonResource>( this, new org.lgna.croquet.OwnedByCompositeValueCreator.Initializer() {
		@Override
		public void initialize( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
			PersonResourceComposite.this.initializeRandom( org.lgna.story.resources.sims2.LifeStage.TEEN );
		}
	}, "randomTeen" );
	private final org.lgna.croquet.ValueCreator<org.lgna.story.resources.sims2.PersonResource> randomChildValueCreator = new org.lgna.croquet.OwnedByCompositeValueCreator<org.lgna.story.resources.sims2.PersonResource>( this, new org.lgna.croquet.OwnedByCompositeValueCreator.Initializer() {
		@Override
		public void initialize( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
			PersonResourceComposite.this.initializeRandom( org.lgna.story.resources.sims2.LifeStage.CHILD );
		}
	}, "randomChild" );
	private final org.lgna.croquet.ValueCreator<org.lgna.story.resources.sims2.PersonResource> randomToddlerValueCreator = new org.lgna.croquet.OwnedByCompositeValueCreator<org.lgna.story.resources.sims2.PersonResource>( this, new org.lgna.croquet.OwnedByCompositeValueCreator.Initializer() {
		@Override
		public void initialize( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
			PersonResourceComposite.this.initializeRandom( org.lgna.story.resources.sims2.LifeStage.TODDLER );
		}
	}, "randomToddler" );
	private final org.lgna.croquet.ValueCreator<org.lgna.story.resources.sims2.PersonResource> previousResourceExpressionValueCreator = new org.lgna.croquet.OwnedByCompositeValueCreator<org.lgna.story.resources.sims2.PersonResource>( this, new org.lgna.croquet.OwnedByCompositeValueCreator.Initializer() {
		@Override
		public void initialize( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
			PersonResourceComposite.this.initializePreviousExpression();
		}
	}, "edit" );

	private final org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.InstanceCreation> randomElderExpressionValueConverter = new PersonResourceToExpressionConverter( this.randomElderValueCreator );
	private final org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.InstanceCreation> randomAdultExpressionValueConverter = new PersonResourceToExpressionConverter( this.randomAdultValueCreator );
	private final org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.InstanceCreation> randomTeenExpressionValueConverter = new PersonResourceToExpressionConverter( this.randomTeenValueCreator );
	private final org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.InstanceCreation> randomChildExpressionValueConverter = new PersonResourceToExpressionConverter( this.randomChildValueCreator );
	private final org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.InstanceCreation> randomToddlerExpressionValueConverter = new PersonResourceToExpressionConverter( this.randomToddlerValueCreator );
	private final org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.InstanceCreation> previousResourceExpressionValueConverter = new PersonResourceToExpressionConverter( this.previousResourceExpressionValueCreator );

	private final PreviewComposite previewComposite = new PreviewComposite();
	private final IngredientsComposite ingredientsComposite = new IngredientsComposite();

	private final org.lgna.croquet.SplitComposite splitComposite = this.createHorizontalSplitComposite( this.previewComposite, this.ingredientsComposite, 0.25f );

	private PersonResourceComposite() {
		super( java.util.UUID.fromString( "a875beea-1feb-48a7-9fe0-5903af846d72" ) );
	}

	public IngredientsComposite getIngredientsComposite() {
		return this.ingredientsComposite;
	}

	public PreviewComposite getPreviewComposite() {
		return this.previewComposite;
	}

	public org.lgna.croquet.ValueCreator<org.lgna.story.resources.sims2.PersonResource> getRandomPersonValueCreator( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		if( lifeStage == org.lgna.story.resources.sims2.LifeStage.ADULT ) {
			return this.randomAdultValueCreator;
		} else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.CHILD ) {
			return this.randomChildValueCreator;
		} else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TEEN ) {
			return this.randomTeenValueCreator;
		} else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TODDLER ) {
			return this.randomToddlerValueCreator;
		} else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.ELDER ) {
			return this.randomElderValueCreator;
		} else {
			return this.randomAdultValueCreator;
		}
	}

	public org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.InstanceCreation> getRandomPersonExpressionValueConverter( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		if( lifeStage == org.lgna.story.resources.sims2.LifeStage.ADULT ) {
			return this.randomAdultExpressionValueConverter;
		} else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.CHILD ) {
			return this.randomChildExpressionValueConverter;
		} else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TEEN ) {
			return this.randomTeenExpressionValueConverter;
		} else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TODDLER ) {
			return this.randomToddlerExpressionValueConverter;
		} else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.ELDER ) {
			return this.randomElderExpressionValueConverter;
		} else {
			return this.randomAdultExpressionValueConverter;
		}
	}

	public org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.InstanceCreation> getPreviousResourceExpressionValueConverter() {
		return this.previousResourceExpressionValueConverter;
	}

	private void initializeRandom( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		boolean isLifeStageStateEnabled = true;
		if( EPIC_HACK_disableLifeStageStateOneTime ) {
			isLifeStageStateEnabled = false;
			EPIC_HACK_disableLifeStageStateOneTime = false;
		}
		this.ingredientsComposite.getLifeStageState().setEnabled( isLifeStageStateEnabled );
		org.lgna.story.resources.sims2.PersonResource personResource = RandomPersonUtilities.createRandomResource( lifeStage );
		this.ingredientsComposite.setStates( personResource );
	}

	private static final class InstanceCreatingVirtualMachine extends org.lgna.project.virtualmachine.ReleaseVirtualMachine {
		public Object ENTRY_POINT_createInstance( org.lgna.project.ast.InstanceCreation instanceCreation ) {
			return this.evaluate( instanceCreation );
		}
	};

	private final InstanceCreatingVirtualMachine vm = new InstanceCreatingVirtualMachine();

	private void initializePreviousExpression() {
		org.alice.ide.cascade.ExpressionCascadeManager expressionCascadeManager = org.alice.stageide.StageIDE.getActiveInstance().getExpressionCascadeManager();
		org.lgna.project.ast.Expression expression = expressionCascadeManager.getPreviousExpression();
		boolean isLifeStageStateEnabled = true;
		if( expression instanceof org.lgna.project.ast.InstanceCreation ) {
			org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)expression;
			org.lgna.project.ast.AbstractType<?, ?, ?> type = instanceCreation.getType();
			if( type instanceof org.lgna.project.ast.JavaType ) {
				org.lgna.project.ast.JavaType javaType = (org.lgna.project.ast.JavaType)type;
				if( javaType.isAssignableTo( org.lgna.story.resources.sims2.PersonResource.class ) ) {
					//note: duplicated below
					isLifeStageStateEnabled = false;

					Object instance = vm.ENTRY_POINT_createInstance( instanceCreation );

					if( instance instanceof org.lgna.story.resources.sims2.PersonResource ) {
						org.lgna.story.resources.sims2.PersonResource personResource = (org.lgna.story.resources.sims2.PersonResource)instance;
						this.ingredientsComposite.setStates( personResource );
						//note: duplicated above
						isLifeStageStateEnabled = false;
					} else {
						edu.cmu.cs.dennisc.java.util.logging.Logger.severe( instance );
					}
				}
			}
		}
		this.ingredientsComposite.getLifeStageState().setEnabled( isLifeStageStateEnabled );
	}

	private boolean EPIC_HACK_disableLifeStageStateOneTime;

	public void EPIC_HACK_disableLifeStageStateOneTime() {
		EPIC_HACK_disableLifeStageStateOneTime = true;
	}

	public org.lgna.croquet.SplitComposite getSplitComposite() {
		return this.splitComposite;
	}

	@Override
	protected org.lgna.croquet.views.Panel createView() {
		return new org.lgna.croquet.views.BorderPanel.Builder().center( this.splitComposite.getView() ).build();
	}

	@Override
	protected org.lgna.story.resources.sims2.PersonResource createValue() {
		return this.ingredientsComposite.createResourceFromStates();
	}

	@Override
	protected Integer getWiderGoldenRatioSizeFromWidth() {
		return 1000;
	}

	@Override
	public boolean isStatusLineDesired() {
		return false;
	}

	@Override
	protected void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		if( ide != null ) {
			ide.getDocumentFrame().disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering.MODAL_DIALOG_WITH_RENDER_WINDOW_OF_ITS_OWN );
		}
		super.handlePreShowDialog( completionStep );
	}

	@Override
	protected void handleFinally( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.views.Dialog dialog ) {
		super.handleFinally( step, dialog );
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		if( ide != null ) {
			ide.getDocumentFrame().enableRendering();
		}
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}

	public static void main( String[] args ) throws Exception {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );

				//new org.alice.stageide.StageIDE();
				new org.lgna.croquet.simple.SimpleApplication();

				try {
					org.alice.stageide.croquet.models.gallerybrowser.DeclareFieldFromPersonResourceIteratingOperation.getInstanceForLifeStage( org.lgna.story.resources.sims2.LifeStage.ADULT ).fire();
				} catch( org.lgna.croquet.CancelException ce ) {
					//pass
				}
				System.exit( 0 );
			}
		} );
	}
}
