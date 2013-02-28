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
public final class PersonResourceComposite extends org.lgna.croquet.ValueCreatorInputDialogCoreComposite<org.lgna.croquet.components.Panel, org.lgna.story.resources.sims2.PersonResource> {
	private static class SingletonHolder {
		private static PersonResourceComposite instance = new PersonResourceComposite();
	}

	public static PersonResourceComposite getInstance() {
		return SingletonHolder.instance;
	}

	private static final class PersonResourceToExpressionConverter extends org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.Expression> {
		public PersonResourceToExpressionConverter( ValueCreator<org.lgna.story.resources.sims2.PersonResource> valueCreator ) {
			super( java.util.UUID.fromString( "40894b2c-ebdc-4101-bc17-d920f3298d89" ), valueCreator );
		}

		@Override
		protected org.lgna.project.ast.Expression convert( org.lgna.story.resources.sims2.PersonResource value ) {
			try {
				org.lgna.project.ast.Expression expression = org.alice.stageide.sceneeditor.SetUpMethodGenerator.createSims2PersonRecourseInstanceCreation( value );
				return expression;
			} catch( org.alice.ide.ast.ExpressionCreator.CannotCreateExpressionException ccee ) {
				throw new RuntimeException( ccee );
			}
		}
	}

	private final org.lgna.croquet.ValueCreator<org.lgna.story.resources.sims2.PersonResource> adultValueCreator = new org.lgna.croquet.OwnedByCompositeValueCreator<org.lgna.story.resources.sims2.PersonResource>( this, new org.lgna.croquet.OwnedByCompositeValueCreator.Initializer() {
		public void initialize( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
			PersonResourceComposite.this.initializeAdult();
		}
	} );
	private final org.lgna.croquet.ValueCreator<org.lgna.story.resources.sims2.PersonResource> childValueCreator = new org.lgna.croquet.OwnedByCompositeValueCreator<org.lgna.story.resources.sims2.PersonResource>( this, new org.lgna.croquet.OwnedByCompositeValueCreator.Initializer() {
		public void initialize( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
			PersonResourceComposite.this.initializeChild();
		}
	} );

	private final org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.Expression> adultExpressionValueConverter = new PersonResourceToExpressionConverter( this.adultValueCreator );
	private final org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.Expression> childExpressionValueConverter = new PersonResourceToExpressionConverter( this.childValueCreator );

	private final PreviewComposite previewComposite = new PreviewComposite();
	private final IngredientsComposite ingredientsComposite = new IngredientsComposite();

	private final org.lgna.croquet.SplitComposite splitComposite = this.createHorizontalSplitComposite( this.previewComposite, this.ingredientsComposite, 0.0f );

	private PersonResourceComposite() {
		super( java.util.UUID.fromString( "a875beea-1feb-48a7-9fe0-5903af846d72" ) );
	}

	public IngredientsComposite getIngredientsComposite() {
		return this.ingredientsComposite;
	}

	public PreviewComposite getPreviewComposite() {
		return this.previewComposite;
	}

	private org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.Expression> getAdultExpressionValueConverter() {
		return this.adultExpressionValueConverter;
	}

	private org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.Expression> getChildExpressionValueConverter() {
		return this.childExpressionValueConverter;
	}

	public org.lgna.croquet.ValueConverter<org.lgna.story.resources.sims2.PersonResource, org.lgna.project.ast.Expression> getExpressionValueConverterForLifeStage( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		if( lifeStage == org.lgna.story.resources.sims2.LifeStage.ADULT ) {
			return this.getAdultExpressionValueConverter();
		} else {
			return this.getChildExpressionValueConverter();
		}
	}

	private org.lgna.croquet.ValueCreator<org.lgna.story.resources.sims2.PersonResource> getAdultValueCreator() {
		return this.adultValueCreator;
	}

	private org.lgna.croquet.ValueCreator<org.lgna.story.resources.sims2.PersonResource> getChildValueCreator() {
		return this.childValueCreator;
	}

	public org.lgna.croquet.ValueCreator<org.lgna.story.resources.sims2.PersonResource> getValueCreatorForLifeStage( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		if( lifeStage == org.lgna.story.resources.sims2.LifeStage.ADULT ) {
			return this.getAdultValueCreator();
		} else {
			return this.getChildValueCreator();
		}
	}

	private void initializeAdult() {
		this.ingredientsComposite.getLifeStageState().setEnabled( false );
	}

	private void initializeChild() {
		this.ingredientsComposite.getLifeStageState().setEnabled( true );
	}

	public org.lgna.croquet.SplitComposite getSplitComposite() {
		return this.splitComposite;
	}

	@Override
	protected org.lgna.croquet.components.Panel createView() {
		return new org.lgna.croquet.components.BorderPanel.Builder().center( this.splitComposite.getView() ).build();
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
		super.handlePreShowDialog( completionStep );
		org.alice.stageide.perspectives.PerspectiveState.getInstance().disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering.MODAL_DIALOG_WITH_RENDER_WINDOW_OF_ITS_OWN );
	}

	@Override
	protected void handleFinally( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog ) {
		super.handleFinally( step, dialog );
		org.alice.stageide.perspectives.PerspectiveState.getInstance().enableRendering();
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		return IS_GOOD_TO_GO_STATUS;
	}

	public static void main( String[] args ) throws Exception {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
		}
		new org.alice.stageide.StageIDE();
		try {
			org.alice.stageide.croquet.models.gallerybrowser.DeclareFieldFromPersonResourceIteratingOperation.getInstanceForLifeStage( org.lgna.story.resources.sims2.LifeStage.ADULT ).fire();
			//PersonResourceComposite.getInstance().getAdultValueCreator().fire();
		} catch( org.lgna.croquet.CancelException ce ) {
			//pass
		}
		System.exit( 0 );
	}
}
