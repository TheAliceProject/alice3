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

package org.alice.stageide.personresource;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities;
import org.alice.ide.IDE;
import org.alice.ide.ReasonToDisableSomeAmountOfRendering;
import org.alice.ide.ast.ExpressionCreator;
import org.alice.ide.cascade.ExpressionCascadeManager;
import org.alice.stageide.StageIDE;
import org.alice.stageide.croquet.models.gallerybrowser.DeclareFieldFromPersonResourceIteratingOperation;
import org.alice.stageide.sceneeditor.SimsSetUpMethodGenerator;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.OwnedByCompositeValueCreator;
import org.lgna.croquet.SplitComposite;
import org.lgna.croquet.ValueConverter;
import org.lgna.croquet.ValueCreator;
import org.lgna.croquet.ValueCreatorInputDialogCoreComposite;
import org.lgna.croquet.simple.SimpleApplication;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.Dialog;
import org.lgna.croquet.views.Panel;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaType;
import org.lgna.project.virtualmachine.ReleaseVirtualMachine;
import org.lgna.story.resources.sims2.LifeStage;
import org.lgna.story.resources.sims2.PersonResource;

import javax.swing.SwingUtilities;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class PersonResourceComposite extends ValueCreatorInputDialogCoreComposite<Panel, PersonResource> {
	private static class SingletonHolder {
		private static PersonResourceComposite instance = new PersonResourceComposite();
	}

	public static PersonResourceComposite getInstance() {
		return SingletonHolder.instance;
	}

	private static final class PersonResourceToExpressionConverter extends ValueConverter<PersonResource, InstanceCreation> {
		public PersonResourceToExpressionConverter( ValueCreator<PersonResource> valueCreator ) {
			super( UUID.fromString( "40894b2c-ebdc-4101-bc17-d920f3298d89" ), valueCreator );
		}

		@Override
		protected InstanceCreation convert( PersonResource value ) {
			try {
				InstanceCreation instanceCreation = SimsSetUpMethodGenerator.createSims2PersonRecourseInstanceCreation( value );
				return instanceCreation;
			} catch( ExpressionCreator.CannotCreateExpressionException ccee ) {
				throw new RuntimeException( ccee );
			}
		}
	}

	private final ValueCreator<PersonResource> randomElderValueCreator =
			new OwnedByCompositeValueCreator<PersonResource>( this, new OwnedByCompositeValueCreator.Initializer() {
				@Override
				public void initialize() {
					PersonResourceComposite.this.initializeRandom( LifeStage.ELDER );
				}
			}, "randomElder" );
	private final ValueCreator<PersonResource> randomAdultValueCreator =
			new OwnedByCompositeValueCreator<PersonResource>( this, new OwnedByCompositeValueCreator.Initializer() {
				@Override
				public void initialize() {
					PersonResourceComposite.this.initializeRandom( LifeStage.ADULT );
				}
			}, "randomAdult" );
	private final ValueCreator<PersonResource> randomTeenValueCreator =
			new OwnedByCompositeValueCreator<PersonResource>( this, new OwnedByCompositeValueCreator.Initializer() {
				@Override
				public void initialize() {
					PersonResourceComposite.this.initializeRandom( LifeStage.TEEN );
				}
			}, "randomTeen" );
	private final ValueCreator<PersonResource> randomChildValueCreator =
			new OwnedByCompositeValueCreator<PersonResource>( this, new OwnedByCompositeValueCreator.Initializer() {
				@Override
				public void initialize() {
					PersonResourceComposite.this.initializeRandom( LifeStage.CHILD );
				}
			}, "randomChild" );
	private final ValueCreator<PersonResource> randomToddlerValueCreator =
			new OwnedByCompositeValueCreator<PersonResource>( this, new OwnedByCompositeValueCreator.Initializer() {
				@Override
				public void initialize() {
					PersonResourceComposite.this.initializeRandom( LifeStage.TODDLER );
				}
			}, "randomToddler" );
	private final ValueCreator<PersonResource> previousResourceExpressionValueCreator =
			new OwnedByCompositeValueCreator<PersonResource>( this, new OwnedByCompositeValueCreator.Initializer() {
				@Override
				public void initialize() {
					PersonResourceComposite.this.initializePreviousExpression();
				}
			}, "edit" );

	private final ValueConverter<PersonResource, InstanceCreation> randomElderExpressionValueConverter = new PersonResourceToExpressionConverter( this.randomElderValueCreator );
	private final ValueConverter<PersonResource, InstanceCreation> randomAdultExpressionValueConverter = new PersonResourceToExpressionConverter( this.randomAdultValueCreator );
	private final ValueConverter<PersonResource, InstanceCreation> randomTeenExpressionValueConverter = new PersonResourceToExpressionConverter( this.randomTeenValueCreator );
	private final ValueConverter<PersonResource, InstanceCreation> randomChildExpressionValueConverter = new PersonResourceToExpressionConverter( this.randomChildValueCreator );
	private final ValueConverter<PersonResource, InstanceCreation> randomToddlerExpressionValueConverter = new PersonResourceToExpressionConverter( this.randomToddlerValueCreator );
	private final ValueConverter<PersonResource, InstanceCreation> previousResourceExpressionValueConverter =
			new PersonResourceToExpressionConverter( this.previousResourceExpressionValueCreator );

	private final PreviewComposite previewComposite = new PreviewComposite();
	private final IngredientsComposite ingredientsComposite = new IngredientsComposite();

	private final SplitComposite splitComposite = this.createHorizontalSplitComposite( this.previewComposite, this.ingredientsComposite, 0.25f );

	private PersonResourceComposite() {
		super( UUID.fromString( "a875beea-1feb-48a7-9fe0-5903af846d72" ) );
	}

	public IngredientsComposite getIngredientsComposite() {
		return this.ingredientsComposite;
	}

	public PreviewComposite getPreviewComposite() {
		return this.previewComposite;
	}

	public ValueCreator<PersonResource> getRandomPersonValueCreator( LifeStage lifeStage ) {
		if( lifeStage == LifeStage.ADULT ) {
			return this.randomAdultValueCreator;
		} else if( lifeStage == LifeStage.CHILD ) {
			return this.randomChildValueCreator;
		} else if( lifeStage == LifeStage.TEEN ) {
			return this.randomTeenValueCreator;
		} else if( lifeStage == LifeStage.TODDLER ) {
			return this.randomToddlerValueCreator;
		} else if( lifeStage == LifeStage.ELDER ) {
			return this.randomElderValueCreator;
		} else {
			return this.randomAdultValueCreator;
		}
	}

	public ValueConverter<PersonResource, InstanceCreation> getRandomPersonExpressionValueConverter( LifeStage lifeStage ) {
		if( lifeStage == LifeStage.ADULT ) {
			return this.randomAdultExpressionValueConverter;
		} else if( lifeStage == LifeStage.CHILD ) {
			return this.randomChildExpressionValueConverter;
		} else if( lifeStage == LifeStage.TEEN ) {
			return this.randomTeenExpressionValueConverter;
		} else if( lifeStage == LifeStage.TODDLER ) {
			return this.randomToddlerExpressionValueConverter;
		} else if( lifeStage == LifeStage.ELDER ) {
			return this.randomElderExpressionValueConverter;
		} else {
			return this.randomAdultExpressionValueConverter;
		}
	}

	public ValueConverter<PersonResource, InstanceCreation> getPreviousResourceExpressionValueConverter() {
		return this.previousResourceExpressionValueConverter;
	}

	private void initializeRandom( LifeStage lifeStage ) {
		boolean isLifeStageStateEnabled = true;
		if( EPIC_HACK_disableLifeStageStateOneTime ) {
			isLifeStageStateEnabled = false;
			EPIC_HACK_disableLifeStageStateOneTime = false;
		}
		this.ingredientsComposite.getLifeStageState().setEnabled( isLifeStageStateEnabled );
		PersonResource personResource = RandomPersonUtilities.createRandomResource( lifeStage );
		this.ingredientsComposite.setStates( personResource );
	}

	private static final class InstanceCreatingVirtualMachine extends ReleaseVirtualMachine {
		public Object ENTRY_POINT_createInstance( InstanceCreation instanceCreation ) {
			return this.evaluate( instanceCreation );
		}
	};

	private final InstanceCreatingVirtualMachine vm = new InstanceCreatingVirtualMachine();

	private void initializePreviousExpression() {
		ExpressionCascadeManager expressionCascadeManager = StageIDE.getActiveInstance().getExpressionCascadeManager();
		Expression expression = expressionCascadeManager.getPreviousExpression();
		boolean isLifeStageStateEnabled = true;
		if( expression instanceof InstanceCreation ) {
			InstanceCreation instanceCreation = (InstanceCreation)expression;
			AbstractType<?, ?, ?> type = instanceCreation.getType();
			if( type instanceof JavaType ) {
				JavaType javaType = (JavaType)type;
				if( javaType.isAssignableTo( PersonResource.class ) ) {
					//note: duplicated below
					isLifeStageStateEnabled = false;

					Object instance = vm.ENTRY_POINT_createInstance( instanceCreation );

					if( instance instanceof PersonResource ) {
						PersonResource personResource = (PersonResource)instance;
						this.ingredientsComposite.setStates( personResource );
						//note: duplicated above
						isLifeStageStateEnabled = false;
					} else {
						Logger.severe( instance );
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

	public SplitComposite getSplitComposite() {
		return this.splitComposite;
	}

	@Override
	protected Panel createView() {
		return new BorderPanel.Builder().center( this.splitComposite.getView() ).build();
	}

	@Override
	protected PersonResource createValue() {
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
	protected void handlePreShowDialog( Dialog dialog ) {
		IDE ide = IDE.getActiveInstance();
		if( ide != null ) {
			ide.getDocumentFrame().disableRendering( ReasonToDisableSomeAmountOfRendering.MODAL_DIALOG_WITH_RENDER_WINDOW_OF_ITS_OWN );
		}
		super.handlePreShowDialog( dialog );
	}

	@Override
	protected void handleFinally( Dialog dialog ) {
		super.handleFinally( dialog );
		IDE ide = IDE.getActiveInstance();
		if( ide != null ) {
			ide.getDocumentFrame().enableRendering();
		}
	}

	@Override
	protected Status getStatusPreRejectorCheck() {
		return IS_GOOD_TO_GO_STATUS;
	}

	public static void main( String[] args ) throws Exception {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				UIManagerUtilities.setLookAndFeel( "Nimbus" );

				//new org.alice.stageide.StageIDE();
				new SimpleApplication();

				try {
					DeclareFieldFromPersonResourceIteratingOperation.getInstanceForLifeStage( LifeStage.ADULT ).fire();
				} catch( CancelException ce ) {
					//pass
				}
				System.exit( 0 );
			}
		} );
	}
}
