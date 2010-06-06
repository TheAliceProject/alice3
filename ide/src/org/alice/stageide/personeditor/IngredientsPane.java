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
package org.alice.stageide.personeditor;

/**
 * @author Dennis Cosgrove
 */
abstract class IngredientsPane extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private static final java.awt.Color BACKGROUND_COLOR = new java.awt.Color( 220, 220, 255 );
	/*package private*/ static final java.awt.Color SELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( java.awt.Color.YELLOW, 1.0, 0.3, 1.0 );
	/*package private*/ static final java.awt.Color UNSELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( BACKGROUND_COLOR, 1.0, 0.9, 0.8 );

	private RandomPersonActionOperation randomPersonActionOperation = new RandomPersonActionOperation();
	private LifeStageSelectionOperation lifeStageSelection = new LifeStageSelectionOperation();
	private GenderSelectionOperation genderSelection = new GenderSelectionOperation();
	private BaseSkinToneSelectionOperation baseSkinToneSelection = new BaseSkinToneSelectionOperation();
	private BaseEyeColorSelectionOperation baseEyeColorSelection = new BaseEyeColorSelectionOperation();
	private HairCardPanel hairCardPanel = new HairCardPanel();
	private HairColorCardPanel hairColorCardPanel = new HairColorCardPanel();
	private FullBodyOutfitCardPanel fullBodyOutfitCardPanel = new FullBodyOutfitCardPanel();
	private FitnessLevelPane fitnessLevelPane = new FitnessLevelPane();

	private static abstract class ContentTabStateOperation extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
		public ContentTabStateOperation(java.util.UUID individualId, String title) {
			super(individualId, title);
		}
		@Override
		public edu.cmu.cs.dennisc.croquet.ScrollPane createScrollPane() {
			edu.cmu.cs.dennisc.croquet.ScrollPane rv = super.createScrollPane();
			rv.setVerticalScrollbarPolicy( edu.cmu.cs.dennisc.croquet.ScrollPane.VerticalScrollbarPolicy.NEVER );
			rv.setHorizontalScrollbarPolicy( edu.cmu.cs.dennisc.croquet.ScrollPane.HorizontalScrollbarPolicy.NEVER );
			rv.setBackgroundColor( null );
			return rv;
		}
	}

	private ContentTabStateOperation bodyTabState = new ContentTabStateOperation(java.util.UUID.fromString( "10c0d057-a5d7-4a36-8cd7-c30f46f5aac2" ), "Body") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent<?> createMainComponent() {
			edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( IngredientsPane.this.fullBodyOutfitCardPanel );
			scrollPane.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 66 );
			//scrollPane.getVerticalScrollBar().setBlockIncrement( 10 );
			
			scrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
			scrollPane.setBackgroundColor( null );
			scrollPane.getAwtComponent().getViewport().setOpaque( false );

			edu.cmu.cs.dennisc.croquet.BorderPanel bodyPane = new edu.cmu.cs.dennisc.croquet.BorderPanel( 8, 8 );
			bodyPane.addComponent( scrollPane, Constraint.CENTER );
			bodyPane.addComponent( IngredientsPane.this.fitnessLevelPane, Constraint.SOUTH );
			bodyPane.setBackgroundColor( BACKGROUND_COLOR );
			bodyPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8  ) );
			return bodyPane;
		}
	};
	private ContentTabStateOperation headTabState = new ContentTabStateOperation(java.util.UUID.fromString( "1e1d604d-974f-4666-91e0-ccf5adec0e4d" ), "Head") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent<?> createMainComponent() {
			edu.cmu.cs.dennisc.croquet.RowsSpringPanel rv = new edu.cmu.cs.dennisc.croquet.RowsSpringPanel( 8, 8 ) {
				@Override
				protected java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > updateComponentRows( java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > rv ) {
					rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createLabeledRow( "hair:", IngredientsPane.this.hairColorCardPanel ) );
					rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( null, hairCardPanel ) );
					rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createLabeledRow( "eye color:", IngredientsPane.this.baseEyeColorSelection.createList() ) );
					rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( null, edu.cmu.cs.dennisc.croquet.BoxUtilities.createGlue() ) );
					return rv;
				}
			};
			rv.setBackgroundColor( BACKGROUND_COLOR );
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8  ) );
			return rv;
		}
	};

	private edu.cmu.cs.dennisc.croquet.TabSelectionOperation tabbedPaneSelection = new edu.cmu.cs.dennisc.croquet.TabSelectionOperation( 
			PersonEditor.GROUP, 
			java.util.UUID.fromString( "d525f0c5-9f39-4807-a9d3-f66775f9eb2d" ), 
			bodyTabState, headTabState );
	

	private edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver<edu.cmu.cs.dennisc.croquet.PredeterminedTab> tabChangeAdapter = new edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver<edu.cmu.cs.dennisc.croquet.PredeterminedTab>() {
		public void changed(edu.cmu.cs.dennisc.croquet.PredeterminedTab nextValue) {
			int index;
			if( nextValue == IngredientsPane.this.bodyTabState ) {
				index = 0;
			} else {
				index = 1;
			}
			IngredientsPane.this.handleTabSelection( index );
		}
	};
	
	public IngredientsPane() {
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setBackgroundColor( BACKGROUND_COLOR );
		this.lifeStageSelection.addValueObserver( new edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver< org.alice.apis.stage.LifeStage >() { 
			public void changed( org.alice.apis.stage.LifeStage nextValue ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handleLifeStageSelection" );
				IngredientsPane.this.handleLifeStageSelection( 0 );
			}
		} );
		this.genderSelection.addValueObserver( new edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver< org.alice.apis.stage.Gender >() { 
			public void changed( org.alice.apis.stage.Gender nextValue ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: handleGenderSelection" );
				IngredientsPane.this.handleGenderSelection( 0 );
			}
		} );

		edu.cmu.cs.dennisc.croquet.BorderPanel northPane = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		northPane.addComponent(  this.randomPersonActionOperation.createButton(), Constraint.NORTH );
		
		edu.cmu.cs.dennisc.croquet.RowsSpringPanel ubiquitousPane = new edu.cmu.cs.dennisc.croquet.RowsSpringPanel( 8, 8 ) {
			@Override
			protected java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > updateComponentRows( java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > rv ) {
				rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createLabeledRow( "life stage:", IngredientsPane.this.lifeStageSelection.createList() ) );
				rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createLabeledRow( "gender:", IngredientsPane.this.genderSelection.createList() ) );
				rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createLabeledRow( "skin tone:", IngredientsPane.this.baseSkinToneSelection.createList() ) );
				return rv;
			}
		};
		northPane.addComponent( ubiquitousPane, Constraint.CENTER );
				
		edu.cmu.cs.dennisc.croquet.FolderTabbedPane tabbedPane = this.tabbedPaneSelection.createDefaultFolderTabbedPane();
		tabbedPane.scaleFont( 1.5f );

		this.addComponent( northPane, Constraint.NORTH );
		this.addComponent( tabbedPane, Constraint.CENTER );
		
	}
	
	protected abstract void handleTabSelection( int tabIndex );
	protected abstract void handleLifeStageSelection( int tabIndex );
	protected abstract void handleGenderSelection( int tabIndex );

	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo( parent );
		this.tabbedPaneSelection.addValueObserver( this.tabChangeAdapter );
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		this.tabbedPaneSelection.removeValueObserver( this.tabChangeAdapter );
		super.handleRemovedFrom( parent );
	}

	public void refresh() {
		final PersonViewer personViewer = PersonViewer.getSingleton();
		org.alice.apis.stage.LifeStage lifeStage = personViewer.getLifeStage();
		org.alice.apis.stage.Gender gender = personViewer.getGender();
		org.alice.apis.stage.Hair hair = personViewer.getHair();
		if( hair != null ) {
			String hairColor = hair.toString();
			this.lifeStageSelection.setValue( lifeStage );
			this.genderSelection.setValue( gender );
			
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: IngredientsPane investigate ordering" );
			this.fullBodyOutfitCardPanel.handleEpicChange( lifeStage, gender, hairColor );
			this.hairCardPanel.handleEpicChange( lifeStage, gender, hairColor );
			
			this.hairCardPanel.setValue( hair );
			
			this.hairColorCardPanel.handleEpicChange( lifeStage, gender, hairColor );
			
			this.hairColorCardPanel.setValue( hairColor );
			this.fullBodyOutfitCardPanel.setValue( personViewer.getFullBodyOutfit() );
			
			this.baseSkinToneSelection.setValue( personViewer.getBaseSkinTone() );
			this.baseEyeColorSelection.setValue( personViewer.getBaseEyeColor() );
			this.fitnessLevelPane.setFitnessLevel( personViewer.getFitnessLevel() );
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "hair is null" );
		}
	}
}
