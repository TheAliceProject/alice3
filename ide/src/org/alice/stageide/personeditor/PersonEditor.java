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
		this.fitnessState = fitnessState;
		this.value = value;
		this.setName( name );
	}
	@Override
	protected void performInternal( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		this.fitnessState.setValue( this.value );
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



	private RandomPersonActionOperation randomPersonActionOperation = new RandomPersonActionOperation( this );

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

	private java.util.Map<org.alice.apis.stage.LifeStage, org.alice.apis.stage.Person> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private static abstract class ContentTabStateOperation extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
		public ContentTabStateOperation(java.util.UUID individualId, String title) {
			super(individualId, title, null);
		}
		@Override
		public edu.cmu.cs.dennisc.croquet.ScrollPane createScrollPane() {
			return null;
		}
	}

	private class BodyTab extends ContentTabStateOperation {
		public BodyTab() {
			super( java.util.UUID.fromString( "10c0d057-a5d7-4a36-8cd7-c30f46f5aac2" ), "Body" );
		}
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
	}
	private class HeadTab extends ContentTabStateOperation {
		public HeadTab() {
			super( java.util.UUID.fromString( "1e1d604d-974f-4666-91e0-ccf5adec0e4d" ), "Head" );
		}
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

	private edu.cmu.cs.dennisc.croquet.TabSelectionOperation tabbedPaneSelection;
	

	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.croquet.PredeterminedTab> tabChangeAdapter = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.croquet.PredeterminedTab>() {
		public void changed(edu.cmu.cs.dennisc.croquet.PredeterminedTab nextValue) {
		}
	};
	public PersonEditor( org.alice.stageide.personeditor.PersonInfo personInfo ) {
		org.alice.apis.stage.LifeStage[] lifeStages = {  org.alice.apis.stage.LifeStage.ADULT, org.alice.apis.stage.LifeStage.CHILD };
		for( org.alice.apis.stage.LifeStage lifeStage : lifeStages ) {
			map.put( lifeStage, lifeStage.createInstance() );
		}
		for( org.alice.apis.stage.Person person : map.values() ) {
			person.getSGTransformable().putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.MOVEABLE_OBJECTS );
		}

		this.setPersonInfo( personInfo );
		this.handleCataclysm( true, true, true );

		this.lifeStageSelection.addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<org.alice.apis.stage.LifeStage>() {
			public void changed(org.alice.apis.stage.LifeStage nextValue) {
				handleCataclysm( true, false, false );
			}
		} );
		this.genderSelection.addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<org.alice.apis.stage.Gender>() {
			public void changed(org.alice.apis.stage.Gender nextValue) {
				handleCataclysm( false, true, false );
			}
		} );
		this.hairColorSelection.addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<String>() {
			public void changed(String nextValue) {
				handleCataclysm( false, false, true );
			}
		} );
		
		this.fullBodyOutfitSelection.addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<org.alice.apis.stage.FullBodyOutfit>() {
			public void changed(org.alice.apis.stage.FullBodyOutfit nextValue) {
				updatePerson();
			}
		} );
		this.hairSelection.addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<org.alice.apis.stage.Hair>() {
			public void changed(org.alice.apis.stage.Hair nextValue) {
				updatePerson();
			}
		} );
		this.fitnessState.addValueObserver( new edu.cmu.cs.dennisc.croquet.BoundedRangeIntegerState.ValueObserver() {
			public void changed(int nextValue) {
				updatePerson();
			}
		} );
		this.baseEyeColorSelection.addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<org.alice.apis.stage.BaseEyeColor>() {
			public void changed(org.alice.apis.stage.BaseEyeColor nextValue) {
				updatePerson();
			}
		} );


		this.tabbedPaneSelection = new edu.cmu.cs.dennisc.croquet.TabSelectionOperation( PersonEditor.GROUP, java.util.UUID.fromString( "d525f0c5-9f39-4807-a9d3-f66775f9eb2d" ), 0, new BodyTab(), new HeadTab() );
		final edu.cmu.cs.dennisc.croquet.FolderTabbedPane<?> tabbedPane = this.tabbedPaneSelection.createDefaultFolderTabbedPane();
		tabbedPane.scaleFont( 1.5f );

		this.baseSkinToneSelection.addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<org.alice.apis.stage.BaseSkinTone>() {
			public void changed(org.alice.apis.stage.BaseSkinTone nextValue) {
				updatePerson();
				tabbedPane.repaint();
			}
		} );
		

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
	
	private boolean isAlreadyHandlingCataclysm = false;
	private void handleCataclysm( boolean isLifeStageChange, boolean isGenderChange, boolean isHairColorChange ) {
		if( this.isAlreadyHandlingCataclysm ) {
			//pass
		} else {
			this.isAlreadyHandlingCataclysm = true;
			try {
				org.alice.apis.stage.LifeStage lifeStage = this.lifeStageSelection.getSelectedItem();
				org.alice.apis.stage.Gender gender = this.genderSelection.getSelectedItem();
				String hairColor = this.hairColorSelection.getSelectedItem();
				if( isLifeStageChange || isGenderChange || isHairColorChange ) {
					this.hairSelection.handleCataclysmicChange( lifeStage, gender, hairColor );
				}
				if( isLifeStageChange || isGenderChange ) {
					this.fullBodyOutfitSelection.handleCataclysmicChange( lifeStage, gender );
				}
				if( isLifeStageChange ) {
					this.hairColorSelection.handleCataclysmicChange( lifeStage );
				}
				this.updatePerson();
				org.alice.apis.stage.Person person = PersonViewer.getSingleton().getPerson();
				org.alice.apis.stage.Hair hair = person.getHair();
				if( isLifeStageChange || isGenderChange || isHairColorChange ) {
					this.hairSelection.setSelectedItem( hair );
				}
				if( isLifeStageChange || isGenderChange ) {
					this.fullBodyOutfitSelection.setSelectedItem( (org.alice.apis.stage.FullBodyOutfit)person.getOutfit() );
				}
				if( isLifeStageChange ) {
					this.hairColorSelection.setSelectedItem( hair.toString() );
				}
			} finally {
				this.isAlreadyHandlingCataclysm = false;
			}
		}
	}
	
	private boolean isAlreadyUpdating = false;
	private void updatePerson() {
		if( this.isAlreadyUpdating ) {
			//pass
		} else {
			this.isAlreadyUpdating = true;
			//edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().acquireRenderingLock();
			try {
				org.alice.apis.stage.LifeStage lifeStage = this.lifeStageSelection.getSelectedItem();
				org.alice.apis.stage.Gender gender = this.genderSelection.getSelectedItem();
				String hairColor = this.hairColorSelection.getSelectedItem();

				org.alice.apis.stage.FullBodyOutfit fullBodyOutfit = this.fullBodyOutfitSelection.getSelectedItem();
				org.alice.apis.stage.BaseEyeColor baseEyeColor = this.baseEyeColorSelection.getSelectedItem();
				org.alice.apis.stage.BaseSkinTone baseSkinTone = this.baseSkinToneSelection.getSelectedItem();
				org.alice.apis.stage.Hair hair = this.hairSelection.getSelectedItem();
				double fitnessLevel = this.fitnessState.getValue()*0.01;
				
				assert lifeStage != null;
				org.alice.apis.stage.Person person = this.map.get( lifeStage );
				if( person != null ) {
					if( gender != null ) {
						person.setGender( gender );
					}
					if( baseSkinTone != null ) {
						person.setSkinTone( baseSkinTone );
						person.setFitnessLevel( fitnessLevel, org.alice.apis.stage.Person.RIGHT_NOW );
						if( fullBodyOutfit != null && org.alice.apis.stage.FullBodyOutfitManager.getSingleton().isApplicable( fullBodyOutfit, lifeStage, gender ) ) {
							//pass
						} else {
//							org.alice.apis.stage.Outfit outfit = person.getOutfit();
//							if( outfit instanceof org.alice.apis.stage.FullBodyOutfit ) {
//								fullBodyOutfit = ( org.alice.apis.stage.FullBodyOutfit )outfit;
//							} else {
								fullBodyOutfit = org.alice.apis.stage.FullBodyOutfitManager.getSingleton().getRandomEnumConstant( lifeStage, gender );
//							}
						}
						person.setOutfit( fullBodyOutfit );
					}
					if( baseEyeColor != null ) {
						person.setEyeColor( baseEyeColor );
					}
					
					if( hair != null && org.alice.apis.stage.HairManager.getSingleton().isApplicable( hair, lifeStage, gender ) ) {
						//pass
					} else {
						try {
							Class<? extends org.alice.apis.stage.Hair> cls = org.alice.apis.stage.HairManager.getSingleton().getRandomClass(lifeStage, gender);
							java.lang.reflect.Field field = cls.getField( hairColor );
							hair = (org.alice.apis.stage.Hair)field.get( null );
						} catch( Exception e ) {
							hair = org.alice.apis.stage.HairManager.getSingleton().getRandomEnumConstant(lifeStage, gender);
						}
					}
					person.setHair( hair );
					PersonViewer.getSingleton().setPerson( person );
				}
			} finally {
				//edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().releaseRenderingLock();
				this.isAlreadyUpdating = false;
			}
		}
	}
	public PersonInfo getPersonInfo() {
		return PersonInfo.createFromPerson( PersonViewer.getSingleton().getPerson() );
	}
	public void setPersonInfo( PersonInfo personInfo ) {
		this.lifeStageSelection.setSelectedItem( personInfo.getLifeStage() );
		this.genderSelection.setSelectedItem( personInfo.getGender() );
		this.baseEyeColorSelection.setSelectedItem( personInfo.getBaseEyeColor() );
		this.baseSkinToneSelection.setSelectedItem( personInfo.getBaseSkinTone() );
		this.fullBodyOutfitSelection.setSelectedItem( personInfo.getFullBodyOutfit() );
		
		org.alice.apis.stage.Hair hair = personInfo.getHair();
		this.hairSelection.setSelectedItem( hair );
		this.hairColorSelection.setSelectedItem( hair.toString() );
		this.fitnessState.setValue( (int)(personInfo.getFitnessLevel()*100) );
	}
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
		ide.initialize( args );
		org.alice.stageide.operations.gallery.CreatePersonFieldOperation operation = new org.alice.stageide.operations.gallery.CreatePersonFieldOperation();
		operation.fire();
	}
}
