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
class FitnessLevelActionOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	private edu.cmu.cs.dennisc.croquet.BoundedRangeIntegerState fitnessState;
	private int value;
	public FitnessLevelActionOperation( edu.cmu.cs.dennisc.croquet.BoundedRangeIntegerState fitnessState, int value, String name ) {
		super( java.util.UUID.fromString( "979d9be8-c24c-4921-93d4-23747bdf079d" ) );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: FitnessLevelActionOperation" );
		this.fitnessState = fitnessState;
		this.value = value;
		this.setName( name );
	}
	@Override
	protected void performInternal( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		this.fitnessState.setValue( this.value );
	}
}

class PersonInfo {
	public static PersonInfo createFromPerson( org.alice.apis.stage.Person person ) {
		PersonInfo rv = new PersonInfo();
		rv.lifeStage = person.getLifeStage();
		rv.gender = person.getGender();
		return rv;
	}
	public static PersonInfo createRandom() {
		PersonInfo rv = new PersonInfo();
		rv.lifeStage = org.alice.apis.stage.LifeStage.ADULT;
		rv.gender = org.alice.apis.stage.Gender.getRandom();
		return rv;
	}
	private org.alice.apis.stage.LifeStage lifeStage;
	private org.alice.apis.stage.Gender gender;
	private PersonInfo() {
	}
}

/**
 * @author Dennis Cosgrove
 */
public class PersonEditor extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	public static final edu.cmu.cs.dennisc.croquet.Group GROUP = new edu.cmu.cs.dennisc.croquet.Group( java.util.UUID.fromString( "2d7d725d-1806-40d1-ac2b-d9cd48cb0abb" ), "PersonEditor.GROUP" );

	///*package-private*/ static final java.awt.Color BACKGROUND_COLOR = new java.awt.Color( 220, 220, 255 );
	/*package-private*/ static final java.awt.Color SELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( java.awt.Color.YELLOW, 1.0, 0.3, 1.0 );
	/*package-private*/ static final java.awt.Color UNSELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( edu.cmu.cs.dennisc.croquet.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR, 1.0, 0.9, 0.8 );



	private RandomPersonActionOperation randomPersonActionOperation = new RandomPersonActionOperation();

	private LifeStageSelectionState lifeStageSelection = new LifeStageSelectionState();
	private GenderSelectionState genderSelection = new GenderSelectionState();
	
	private BaseSkinToneSelectionState baseSkinToneSelection = new BaseSkinToneSelectionState();
	private BaseEyeColorSelectionState baseEyeColorSelection = new BaseEyeColorSelectionState();
	
	private FullBodyOutfitSelectionState fullBodyOutfitSelection = new FullBodyOutfitSelectionState();
	private HairColorSelectionState hairColorSelection = new HairColorSelectionState();
	private HairSelectionState hairSelection = new HairSelectionState();

	private edu.cmu.cs.dennisc.croquet.BoundedRangeIntegerState fitnessState = new edu.cmu.cs.dennisc.croquet.BoundedRangeIntegerState( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "8e172c61-c2b6-43e4-9777-e9d8fd2b0d65" ), 0, 50, 100 );
	private FitnessLevelActionOperation softOperation = new FitnessLevelActionOperation( this.fitnessState, this.fitnessState.getMinimum(), "out of shape" );
	private FitnessLevelActionOperation cutOperation = new FitnessLevelActionOperation( this.fitnessState, this.fitnessState.getMaximum(), "in shape" );

	private static abstract class ContentTabStateOperation extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
		public ContentTabStateOperation(java.util.UUID individualId, String title) {
			super(individualId, title);
		}
		@Override
		public edu.cmu.cs.dennisc.croquet.ScrollPane createScrollPane() {
			return null;
		}
	}

	private ContentTabStateOperation bodyTabState = new ContentTabStateOperation(java.util.UUID.fromString( "10c0d057-a5d7-4a36-8cd7-c30f46f5aac2" ), "Body") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent<?> createMainComponent() {
			edu.cmu.cs.dennisc.croquet.List< ? > list = fullBodyOutfitSelection.createList();
			edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( list );
			scrollPane.getAwtComponent().getVerticalScrollBar().setUnitIncrement( 66 );
			scrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );

			edu.cmu.cs.dennisc.croquet.Slider slider = fitnessState.createSlider();
			slider.setBackgroundColor( edu.cmu.cs.dennisc.croquet.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
			
			edu.cmu.cs.dennisc.croquet.BorderPanel fitnessLevelPane = new edu.cmu.cs.dennisc.croquet.BorderPanel();
			fitnessLevelPane.addComponent( softOperation.createButton(), Constraint.WEST );
			fitnessLevelPane.addComponent( slider, Constraint.CENTER );
			fitnessLevelPane.addComponent( cutOperation.createButton(), Constraint.EAST );

			edu.cmu.cs.dennisc.croquet.BorderPanel rv = new edu.cmu.cs.dennisc.croquet.BorderPanel( 8, 8 );
			rv.addComponent( scrollPane, Constraint.CENTER );
			rv.addComponent( fitnessLevelPane, Constraint.SOUTH );
			rv.setBackgroundColor( edu.cmu.cs.dennisc.croquet.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
			rv.getAwtComponent().setOpaque( true );
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8 ) );
			return rv;
		}
	};
	private ContentTabStateOperation headTabState = new ContentTabStateOperation(java.util.UUID.fromString( "1e1d604d-974f-4666-91e0-ccf5adec0e4d" ), "Head") {
		@Override
		protected edu.cmu.cs.dennisc.croquet.JComponent<?> createMainComponent() {
			edu.cmu.cs.dennisc.croquet.RowsSpringPanel rv = new edu.cmu.cs.dennisc.croquet.RowsSpringPanel( 8, 8 ) {
				@Override
				protected java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > updateComponentRows( java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > rv ) {
					rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createLabeledRow( "hair:", hairColorSelection.createList() ) );
					rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( null, hairSelection.createList() ) );
					rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createLabeledRow( "eye color:", baseEyeColorSelection.createList() ) );
					rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( null, edu.cmu.cs.dennisc.croquet.BoxUtilities.createGlue() ) );
					return rv;
				}
			};
			rv.setBackgroundColor( edu.cmu.cs.dennisc.croquet.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8 ) );
			return rv;
		}
	};

	private edu.cmu.cs.dennisc.croquet.TabSelectionOperation tabbedPaneSelection = new edu.cmu.cs.dennisc.croquet.TabSelectionOperation( 
			PersonEditor.GROUP, 
			java.util.UUID.fromString( "d525f0c5-9f39-4807-a9d3-f66775f9eb2d" ), 
			bodyTabState, headTabState );
	

	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.croquet.PredeterminedTab> tabChangeAdapter = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.croquet.PredeterminedTab>() {
		public void changed(edu.cmu.cs.dennisc.croquet.PredeterminedTab nextValue) {
		}
	};
	
//	private IngredientsPane ingredientsPane = new IngredientsPane() {
//		@Override
//		protected void handleTabSelection( int tabIndex ) {
//			personViewer.handleTabSelection( tabIndex, 0.5 );
//		}
//		@Override
//		protected void handleLifeStageSelection( int tabIndex ) {
//			personViewer.handleTabSelection( tabIndex, 0.0 );
//		}
//		@Override
//		protected void handleGenderSelection( int tabIndex ) {
//			personViewer.handleTabSelection( tabIndex, 0.0 );
//		}
//	};
//	
	
	private void handleCataclysm( boolean isLifeStageChange, boolean isGenderChange, boolean isHairColorChange ) {
		if( isLifeStageChange || isGenderChange || isHairColorChange ) {
			this.hairSelection.handleCataclysmicChange( this.lifeStageSelection.getValue(), this.genderSelection.getValue(), this.hairColorSelection.getValue() );
		}
		if( isLifeStageChange || isGenderChange ) {
			this.fullBodyOutfitSelection.handleCataclysmicChange( this.lifeStageSelection.getValue(), this.genderSelection.getValue() );
		}
		if( isLifeStageChange ) {
			this.hairColorSelection.handleCataclysmicChange( this.lifeStageSelection.getValue() );
		}
	}
	
	public PersonEditor( org.alice.apis.stage.Person person ) {
		if( person != null ) {
			this.lifeStageSelection.setValue( person.getLifeStage() );
			this.genderSelection.setValue( person.getGender() );
			this.hairColorSelection.setValue( person.getHair().toString() );
		} else {
			this.lifeStageSelection.setValue( org.alice.apis.stage.LifeStage.ADULT );
			this.genderSelection.setToRandomValue();
			this.hairColorSelection.setToRandomValue();
		}
		
		lifeStageSelection.addValueObserver( new LifeStageSelectionState.ValueObserver<org.alice.apis.stage.LifeStage>() {
			public void changed(org.alice.apis.stage.LifeStage nextValue) {
				handleCataclysm( true, false, false );
			}
		} );
		genderSelection.addValueObserver( new LifeStageSelectionState.ValueObserver<org.alice.apis.stage.Gender>() {
			public void changed(org.alice.apis.stage.Gender nextValue) {
				handleCataclysm( false, true, false );
			}
		} );
		hairColorSelection.addValueObserver( new LifeStageSelectionState.ValueObserver<String>() {
			public void changed(String nextValue) {
				handleCataclysm( false, false, true );
			}
		} );
		
		this.handleCataclysm( true, true, true );

		edu.cmu.cs.dennisc.croquet.BorderPanel northPane = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		northPane.addComponent(  this.randomPersonActionOperation.createButton(), Constraint.NORTH );
		edu.cmu.cs.dennisc.croquet.RowsSpringPanel ubiquitousPane = new edu.cmu.cs.dennisc.croquet.RowsSpringPanel( 8, 8 ) {
			@Override
			protected java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > updateComponentRows( java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > rv ) {
				rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createLabeledRow( "life stage:", lifeStageSelection.createList() ) );
				rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createLabeledRow( "gender:", genderSelection.createList() ) );
				rv.add( edu.cmu.cs.dennisc.croquet.SpringUtilities.createLabeledRow( "skin tone:", baseSkinToneSelection.createList() ) );
				return rv;
			}
		};
		ubiquitousPane.setBackgroundColor( edu.cmu.cs.dennisc.croquet.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
		northPane.addComponent( ubiquitousPane, Constraint.CENTER );

		this.tabbedPaneSelection.setValue( bodyTabState );
		final edu.cmu.cs.dennisc.croquet.FolderTabbedPane<?> tabbedPane = this.tabbedPaneSelection.createDefaultFolderTabbedPane();
		tabbedPane.scaleFont( 1.5f );

		this.baseSkinToneSelection.addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<org.alice.apis.stage.BaseSkinTone>() {
			public void changed(org.alice.apis.stage.BaseSkinTone nextValue) {
				tabbedPane.repaint();
			}
		} );
		
		edu.cmu.cs.dennisc.croquet.BorderPanel ingredientsPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		ingredientsPanel.addComponent( northPane, Constraint.NORTH );
		ingredientsPanel.addComponent( tabbedPane, Constraint.CENTER );
		ingredientsPanel.setBackgroundColor( edu.cmu.cs.dennisc.croquet.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );

		//		this.personViewer.initializeValues( this.person );
		edu.cmu.cs.dennisc.croquet.HorizontalSplitPane splitPane = new edu.cmu.cs.dennisc.croquet.HorizontalSplitPane( PersonViewer.getSingleton(), ingredientsPanel );
		splitPane.setDividerLocation( 400 );
		this.addComponent( splitPane, Constraint.CENTER );

//		this.fitnessState.addValueObserver( new edu.cmu.cs.dennisc.croquet.BoundedRangeIntegerState.ValueObserver() {
//			public void changed(int nextValue) {
//				PersonViewer.getSingleton().setFitnessLevel( nextValue*0.01 );
//			}
//		} );
	}

//	public org.alice.apis.stage.Person createInstance() {
//		PersonViewer personViewer = PersonViewer.getSingleton();
//		org.alice.apis.stage.Person rv;
//		
//		if( personViewer.getLifeStage() == org.alice.apis.stage.LifeStage.ADULT ) {
//			rv = new org.alice.apis.stage.Adult();
//		} else if( personViewer.getLifeStage() == org.alice.apis.stage.LifeStage.CHILD ) {
//			rv = new org.alice.apis.stage.Child();
//		} else {
//			rv = null;
//		}
//		if( rv != null ) {
//			rv.setGender( personViewer.getGender() );
//			rv.setHair( personViewer.getHair() );
//			rv.setSkinTone( personViewer.getBaseSkinTone() );
//			rv.setEyeColor( personViewer.getBaseEyeColor() );
//			rv.setFitnessLevel( personViewer.getFitnessLevel() );
//			rv.setOutfit( personViewer.getFullBodyOutfit() );
//		}
//		
//		return rv;
//	}
	
	public org.alice.apis.stage.Person getPerson() {
		return null;
	}
//	@Override
//	public java.awt.Dimension getPreferredSize() {
//		return new java.awt.Dimension( 1024, 700 );
//	}
	
	public static void main( String[] args ) {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			try {
				edu.cmu.cs.dennisc.javax.swing.plaf.nimbus.NimbusUtilities.installModifiedNimbus( lookAndFeelInfo );
			} catch( Throwable t ) {
				t.printStackTrace();
			}
		}

		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
		edu.cmu.cs.dennisc.javax.swing.ApplicationFrame frame = new edu.cmu.cs.dennisc.javax.swing.ApplicationFrame() {
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}
			@Override
			protected void handleAbout( java.util.EventObject e ) {
			}
			@Override
			protected void handlePreferences( java.util.EventObject e ) {
			}

			@Override
			protected void handleQuit( java.util.EventObject e ) {
				System.exit( 0 );
			}
		};
		frame.setSize( new java.awt.Dimension( 1024, 768 ) );
		frame.getContentPane().add( new PersonEditor( null ).getAwtComponent() );
		frame.setVisible( true );
	}
}
