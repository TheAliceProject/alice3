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
	private RandomPersonActionOperation randomPersonActionOperation = new RandomPersonActionOperation();
	private LifeStageSelectionOperation lifeStageSelection = new LifeStageSelectionOperation();
	private GenderSelectionOperation genderSelection = new GenderSelectionOperation();
	private BaseSkinToneSelectionOperation baseSkinToneSelection = new BaseSkinToneSelectionOperation();
	private BaseEyeColorSelectionOperation baseEyeColorSelection = new BaseEyeColorSelectionOperation();
	private HairColorCardPanel hairColorCardPanel = new HairColorCardPanel();
	private HairCardPanel hairCardPanel = new HairCardPanel();
	private FullBodyOutfitCardPanel fullBodyOutfitCardPanel = new FullBodyOutfitCardPanel();
	private FitnessLevelPane fitnessLevelPane = new FitnessLevelPane();

	private static final java.awt.Color BACKGROUND_COLOR = new java.awt.Color( 220, 220, 255 );
	/*package private*/ static final java.awt.Color SELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( java.awt.Color.YELLOW, 1.0, 0.3, 1.0 );
	/*package private*/ static final java.awt.Color UNSELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( BACKGROUND_COLOR, 1.0, 0.9, 0.8 );
	private edu.cmu.cs.dennisc.zoot.ZTabbedPane tabbedPane = new edu.cmu.cs.dennisc.zoot.ZTabbedPane() {
		@Override
		public java.awt.Color getContentAreaColor() {
			return BACKGROUND_COLOR;
		}
	};
	private javax.swing.event.ChangeListener tabChangeAdapter = new javax.swing.event.ChangeListener() {
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			int index = tabbedPane.getSelectedIndex();
			handleTabSelection( index );
		}
	};
	
	public IngredientsPane() {
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setBackgroundColor( BACKGROUND_COLOR );
		this.setOpaque( true );

		this.lifeStageSelection.addValueObserver( new edu.cmu.cs.dennisc.croquet.ItemSelectionOperation.ValueObserver< org.alice.apis.stage.LifeStage >() { 
			public void changed( org.alice.apis.stage.LifeStage nextValue ) {
//				if( e.getValueIsAdjusting() ) {
//					//pass
//				} else {
					if( IngredientsPane.this.tabbedPane != null ) {
						javax.swing.SwingUtilities.invokeLater( new Runnable() {
							public void run() {
								IngredientsPane.this.handleLifeStageSelection( IngredientsPane.this.tabbedPane.getSelectedIndex() );
							}
						} );
					}
//				}
			}
		} );
		this.genderSelection.addValueObserver( new edu.cmu.cs.dennisc.croquet.ItemSelectionOperation.ValueObserver< org.alice.apis.stage.Gender >() { 
			public void changed( org.alice.apis.stage.Gender nextValue ) {
//				if( e.getValueIsAdjusting() ) {
//					//pass
//				} else {
					if( IngredientsPane.this.tabbedPane != null ) {
						javax.swing.SwingUtilities.invokeLater( new Runnable() {
							public void run() {
								IngredientsPane.this.handleGenderSelection( IngredientsPane.this.tabbedPane.getSelectedIndex() );
							}
						} );
					}
//				}
			}
		} );
//
//		this.lifeStageSelection.addListSelectionListener( new javax.swing.event.ListSelectionListener() {
//			public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
//				if( e.getValueIsAdjusting() ) {
//					//pass
//				} else {
//					if( IngredientsPane.this.tabbedPane != null ) {
//						javax.swing.SwingUtilities.invokeLater( new Runnable() {
//							public void run() {
//								IngredientsPane.this.handleLifeStageSelection( IngredientsPane.this.tabbedPane.getSelectedIndex() );
//							}
//						} );
//					}
//				}
//			}
//		} );
//		
//		this.genderSelection.addListSelectionListener( new javax.swing.event.ListSelectionListener() {
//			public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
//				if( e.getValueIsAdjusting() ) {
//					//pass
//				} else {
//					if( IngredientsPane.this.tabbedPane != null ) {
//						javax.swing.SwingUtilities.invokeLater( new Runnable() {
//							public void run() {
//								IngredientsPane.this.handleGenderSelection( IngredientsPane.this.tabbedPane.getSelectedIndex() );
//							}
//						} );
//					}
//				}
//			}
//		} );
//		
		
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
		
//		final swing.BorderPane hairPane = new swing.BorderPane();
//		hairPane.add( this.hairColorList, java.awt.BorderLayout.NORTH );
//		hairPane.add( this.hairList, java.awt.BorderLayout.CENTER );
		
		edu.cmu.cs.dennisc.croquet.RowsSpringPanel headPane = new edu.cmu.cs.dennisc.croquet.RowsSpringPanel( 8, 8 ) {
			@Override
			protected java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > updateComponentRows( java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > rv ) {
				rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createLabeledRow( "hair:", hairColorCardPanel ) );
				rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( null, hairCardPanel ) );
				rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createLabeledRow( "eye color:", baseEyeColorSelection.createList() ) );
				rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( null, edu.cmu.cs.dennisc.croquet.BoxUtilities.createGlue() ) );
				return rv;
			}
		};
		

		edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( this.fullBodyOutfitCardPanel );
		scrollPane.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 66 );
		//scrollPane.getVerticalScrollBar().setBlockIncrement( 10 );
		
		scrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		scrollPane.setOpaque( false );
		scrollPane.getAwtComponent().getViewport().setOpaque( false );

		edu.cmu.cs.dennisc.croquet.BorderPanel bodyPane = new edu.cmu.cs.dennisc.croquet.BorderPanel( 8, 8 );
		bodyPane.addComponent( scrollPane, Constraint.CENTER );
		bodyPane.addComponent( this.fitnessLevelPane, Constraint.SOUTH );
		
		//java.awt.Color color = edu.cmu.cs.dennisc.awt.ColorUtilities.scaleHSB( BACKGROUND_COLOR, 1.0, 0.9, 0.8 );
		java.awt.Color color = BACKGROUND_COLOR;
		
		bodyPane.setBackgroundColor( color );
		headPane.setBackgroundColor( color );
		bodyPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8  ) );
		headPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8  ) );
		
		this.tabbedPane.add( "Body", bodyPane.getAwtComponent() );
		this.tabbedPane.add( "Head", headPane.getAwtComponent() );
		this.tabbedPane.setOpaque( true );

		java.awt.Font font = tabbedPane.getFont();
		this.tabbedPane.setFont( font.deriveFont( font.getSize2D() * 1.5f ) );

		this.addComponent( northPane, Constraint.NORTH );
		this.addComponent( new edu.cmu.cs.dennisc.croquet.SwingAdapter( tabbedPane ), Constraint.CENTER );
		
	}
	
	protected abstract void handleTabSelection( int tabIndex );
	protected abstract void handleLifeStageSelection( int tabIndex );
	protected abstract void handleGenderSelection( int tabIndex );

	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo( parent );
		tabbedPane.addChangeListener( this.tabChangeAdapter );
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		tabbedPane.removeChangeListener( this.tabChangeAdapter );
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
