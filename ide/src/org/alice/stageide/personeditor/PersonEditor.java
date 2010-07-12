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

import org.alice.apis.stage.Adult;
import org.alice.apis.stage.BaseEyeColor;
import org.alice.apis.stage.BaseSkinTone;
import org.alice.apis.stage.Child;
import org.alice.apis.stage.FullBodyOutfit;
import org.alice.apis.stage.FullBodyOutfitManager;
import org.alice.apis.stage.Gender;
import org.alice.apis.stage.Hair;
import org.alice.apis.stage.HairManager;
import org.alice.apis.stage.LifeStage;
import org.alice.apis.stage.Outfit;
import org.alice.apis.stage.Person;

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

	private Adult adult = new Adult();
	private Child child = new Child();
	//private edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Person > mapLifeStageGenderToPerson = new edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Person >();

	private static abstract class ContentTabStateOperation extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
		public ContentTabStateOperation(java.util.UUID individualId, String title) {
			super(individualId, title, null);
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
	public PersonEditor( org.alice.stageide.personeditor.PersonInfo personInfo ) {
		Person[] people = { adult, child };
		for( Person person : people ) {
			person.getSGTransformable().putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.MOVEABLE_OBJECTS );
		}


		lifeStageSelection.setSelectedItem( personInfo.getLifeStage() );
		genderSelection.setSelectedItem( personInfo.getGender() );
		
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

		this.tabbedPaneSelection.setSelectedItem( bodyTabState );
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
		Person person;
		LifeStage lifeStage = personInfo.getLifeStage();
		if( lifeStage == LifeStage.ADULT ) {
			person = adult;
		} else {
			person = child;
		}
		PersonViewer.getSingleton().setPerson( person );
	}
	
	private void handleCataclysm( boolean isLifeStageChange, boolean isGenderChange, boolean isHairColorChange ) {
		if( isLifeStageChange || isGenderChange || isHairColorChange ) {
			this.hairSelection.handleCataclysmicChange( this.lifeStageSelection.getSelectedItem(), this.genderSelection.getSelectedItem(), this.hairColorSelection.getSelectedItem() );
		}
		if( isLifeStageChange || isGenderChange ) {
			this.fullBodyOutfitSelection.handleCataclysmicChange( this.lifeStageSelection.getSelectedItem(), this.genderSelection.getSelectedItem() );
		}
		if( isLifeStageChange ) {
			this.hairColorSelection.handleCataclysmicChange( this.lifeStageSelection.getSelectedItem() );
		}
	}
	
////	private boolean isAlreadyUpdating = false;
//	private void updatePerson() {
////		if( this.isAlreadyUpdating ) {
////			//pass
////		} else {
////			this.isAlreadyUpdating = true;
////			//edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().acquireRenderingLock();
////			try {
//				if( this.lifeStage != null && this.gender != null ) {
//					Person person = this.mapLifeStageGenderToPerson.get( this.lifeStage, this.gender );
//					if( person != null ) {
//						this.dragAdapter.setSelectedObject( person.getSGTransformable() );
//						if( this.baseSkinTone != null ) {
//							person.setSkinTone( this.baseSkinTone );
//							if( this.fitnessLevel != null ) {
//								person.setFitnessLevel( this.fitnessLevel, org.alice.apis.stage.Person.RIGHT_NOW );
//								if( this.fullBodyOutfit != null && FullBodyOutfitManager.getSingleton().isApplicable( this.fullBodyOutfit, this.lifeStage, this.gender ) ) {
//									//pass
//								} else {
//									Outfit outfit = person.getOutfit();
//									if( outfit instanceof FullBodyOutfit ) {
//										this.fullBodyOutfit = ( FullBodyOutfit )outfit;
//									} else {
//										this.fullBodyOutfit = FullBodyOutfitManager.getSingleton().getRandomEnumConstant( this.lifeStage, this.gender );
//									}
//								}
//								person.setOutfit( this.fullBodyOutfit );
//							}
//						}
//						if( this.baseEyeColor != null ) {
//							person.setEyeColor( this.baseEyeColor );
//						}
//						if( this.hair != null && HairManager.getSingleton().isApplicable( this.hair, this.lifeStage, this.gender ) ) {
//							//pass
//						} else {
//							Hair hair = person.getHair();
//							if( hair != null ) {
//								this.hair = hair;
//							} else {
//								this.hair = HairManager.getSingleton().getRandomEnumConstant( this.lifeStage, this.gender );
//							}
//						}
//						person.setHair( this.hair );
//						this.setModel( person );
//					} else {
//						edu.cmu.cs.dennisc.print.PrintUtilities.println( "updatePerson person is null:", this.lifeStage, this.gender );
//					}
////					if( this.ingredientsPane != null ) {
////						this.ingredientsPane.refresh();
////					}
//				}
////			} finally {
////				//edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().releaseRenderingLock();
////				this.isAlreadyUpdating = false;
////			}
////		}
//	}
	public PersonInfo getPersonInfo() {
		return PersonInfo.createFromPerson( null );
	}
	public void setPersonInfo( PersonInfo personInfo ) {
		
		//todo
	}
//	public LifeStage getLifeStage() {
//		return this.lifeStage;
//	}
//	public void setLifeStage( LifeStage lifeStage ) {
//		if( this.lifeStage == lifeStage ) {
//			//pass
//		} else {
//			this.lifeStage = lifeStage;
//			this.updatePerson();
//		}
//	}
//
//	public Gender getGender() {
//		return this.gender;
//	}
//	public void setGender( Gender gender ) {
//		if( this.gender == gender ) {
//			//pass
//		} else {
//			this.gender = gender;
//			this.updatePerson();
//		}
//	}
//	public BaseEyeColor getBaseEyeColor() {
//		return this.baseEyeColor;
//	}
//	public void setBaseEyeColor( BaseEyeColor baseEyeColor ) {
//		if( this.baseEyeColor == baseEyeColor ) {
//			//pass
//		} else {
//			this.baseEyeColor = baseEyeColor;
//			this.updatePerson();
//		}
//	}
//	public BaseSkinTone getBaseSkinTone() {
//		return this.baseSkinTone;
//	}
//	public void setBaseSkinTone( BaseSkinTone baseSkinTone ) {
//		if( this.baseSkinTone == baseSkinTone ) {
//			//pass
//		} else {
//			this.baseSkinTone = baseSkinTone;
//			this.updatePerson();
//		}
//	}
//	public FullBodyOutfit getFullBodyOutfit() {
//		return this.fullBodyOutfit;
//	}
//	public void setFullBodyOutfit( FullBodyOutfit fullBodyOutfit ) {
//		if( this.fullBodyOutfit == fullBodyOutfit ) {
//			//pass
//		} else {
//			this.fullBodyOutfit = fullBodyOutfit;
//			this.updatePerson();
//		}
//	}
//	
//	
//	
//	public Hair getHair() {
//		return this.hair;
//	}
//	public void setHair( Hair hair ) {
//		if( this.hair == hair ) {
//			//pass
//		} else {
//			this.hair = hair;
//			this.updatePerson();
//		}
//	}
//	
//	public String getHairColor() {
//		return this.hair.toString();
//	}
//	public void setHairColor( String hairColor ) {
//		if( this.hair != null ) {
//			Class< ? extends Enum > cls = (Class<? extends Enum>)this.hair.getClass();
//			for( Enum e : cls.getEnumConstants() ) {
//				if( e.name().equals( hairColor ) ) {
//					this.setHair( (Hair)e );
//					break;
//				}
//			}
//		}
//	}
//	
//	public Double getFitnessLevel() {
//		return this.fitnessLevel;
//	}
//	public void setFitnessLevel( Double fitnessLevel ) {
//		if( this.fitnessLevel == fitnessLevel ) {
//			//pass
//		} else {
//			this.fitnessLevel = fitnessLevel;
//			this.updatePerson();
//		}
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
		ide.initialize( args );
		org.alice.stageide.operations.gallery.CreatePersonFieldOperation operation = new org.alice.stageide.operations.gallery.CreatePersonFieldOperation();
		operation.fire();
	}
}
