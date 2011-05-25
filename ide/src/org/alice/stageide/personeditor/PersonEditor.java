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

import org.alice.stageide.croquet.models.personeditor.PersonInfo;

/**
 * @author Dennis Cosgrove
 */
public class PersonEditor extends org.lgna.croquet.components.BorderPanel {
	private static class SingletonHolder {
		private static PersonEditor instance = new PersonEditor();
	}
	public static PersonEditor getInstance() {
		return SingletonHolder.instance;
	}
	private PersonEditor() {
		org.alice.apis.stage.LifeStage[] lifeStages = {  org.alice.apis.stage.LifeStage.ADULT, org.alice.apis.stage.LifeStage.CHILD };
		for( org.alice.apis.stage.LifeStage lifeStage : lifeStages ) {
			map.put( lifeStage, lifeStage.createInstance() );
		}
		for( org.alice.apis.stage.Person person : map.values() ) {
			person.getSGTransformable().putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.MOVEABLE_OBJECTS );
		}

		org.alice.stageide.croquet.models.personeditor.LifeStageSelectionState.getInstance().addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<org.alice.apis.stage.LifeStage>() {
			public void changed(org.alice.apis.stage.LifeStage nextValue) {
				handleCataclysm( true, false, false );
			}
		} );
		org.alice.stageide.croquet.models.personeditor.GenderSelectionState.getInstance().addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<org.alice.apis.stage.Gender>() {
			public void changed(org.alice.apis.stage.Gender nextValue) {
				handleCataclysm( false, true, false );
			}
		} );
		org.alice.stageide.croquet.models.personeditor.HairColorSelectionState.getInstance().addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<String>() {
			public void changed(String nextValue) {
				handleCataclysm( false, false, true );
			}
		} );
		
		org.alice.stageide.croquet.models.personeditor.FullBodyOutfitSelectionState.getInstance().addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<org.alice.apis.stage.FullBodyOutfit>() {
			public void changed(org.alice.apis.stage.FullBodyOutfit nextValue) {
				updatePerson();
			}
		} );
		org.alice.stageide.croquet.models.personeditor.HairSelectionState.getInstance().addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<org.alice.apis.stage.Hair>() {
			public void changed(org.alice.apis.stage.Hair nextValue) {
				updatePerson();
			}
		} );
		org.alice.stageide.croquet.models.personeditor.FitnessModel.getInstance().addValueObserver( new edu.cmu.cs.dennisc.croquet.BoundedRangeIntegerState.ValueObserver() {
			public void changed(int nextValue) {
				updatePerson();
			}
		} );
		org.alice.stageide.croquet.models.personeditor.BaseEyeColorSelectionState.getInstance().addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<org.alice.apis.stage.BaseEyeColor>() {
			public void changed(org.alice.apis.stage.BaseEyeColor nextValue) {
				updatePerson();
			}
		} );


		final org.lgna.croquet.components.FolderTabbedPane<?> tabbedPane = org.alice.stageide.croquet.models.personeditor.BodyHeadTabSelectionModel.getInstance().createDefaultFolderTabbedPane();
		tabbedPane.scaleFont( 1.5f );

		org.alice.stageide.croquet.models.personeditor.BaseSkinToneSelectionState.getInstance().addValueObserver( new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<org.alice.apis.stage.BaseSkinTone>() {
			public void changed(org.alice.apis.stage.BaseSkinTone nextValue) {
				updatePerson();
				tabbedPane.repaint();
			}
		} );
		

		org.lgna.croquet.components.BorderPanel northPane = new org.lgna.croquet.components.BorderPanel();
		northPane.addComponent( org.alice.stageide.croquet.models.personeditor.RandomPersonActionOperation.getInstance().createButton(), Constraint.PAGE_START );
		org.lgna.croquet.components.RowsSpringPanel ubiquitousPane = new org.lgna.croquet.components.RowsSpringPanel( 8, 8 ) {
			@Override
			protected java.util.List< org.lgna.croquet.components.Component< ? >[] > updateComponentRows( java.util.List< org.lgna.croquet.components.Component< ? >[] > rv ) {
				rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( "life stage:", org.alice.stageide.croquet.models.personeditor.LifeStageSelectionState.getInstance().createList() ) );
				rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( "gender:", org.alice.stageide.croquet.models.personeditor.GenderSelectionState.getInstance().createList() ) );
				rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( "skin tone:", org.alice.stageide.croquet.models.personeditor.BaseSkinToneSelectionState.getInstance().createList() ) );
				return rv;
			}
		};
		ubiquitousPane.setBackgroundColor( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
		northPane.addComponent( ubiquitousPane, Constraint.CENTER );

		org.lgna.croquet.components.BorderPanel ingredientsPanel = new org.lgna.croquet.components.BorderPanel();
		ingredientsPanel.addComponent( northPane, Constraint.PAGE_START );
		ingredientsPanel.addComponent( tabbedPane, Constraint.CENTER );
		ingredientsPanel.setBackgroundColor( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );

		//		this.personViewer.initializeValues( this.person );
		org.lgna.croquet.components.HorizontalSplitPane splitPane = new org.lgna.croquet.components.HorizontalSplitPane( PersonViewer.getSingleton(), ingredientsPanel );
		splitPane.setDividerLocation( 400 );
		this.addComponent( splitPane, Constraint.CENTER );

//		this.fitnessState.addValueObserver( new edu.cmu.cs.dennisc.croquet.BoundedRangeIntegerState.ValueObserver() {
//			public void changed(int nextValue) {
//				PersonViewer.getSingleton().setFitnessLevel( nextValue*0.01 );
//			}
//		} );
	}

	private java.util.Map<org.alice.apis.stage.LifeStage, org.alice.apis.stage.Person> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.croquet.PredeterminedTab> tabChangeAdapter = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.croquet.PredeterminedTab>() {
		public void changed(edu.cmu.cs.dennisc.croquet.PredeterminedTab nextValue) {
		}
	};
	
	public void initialize( org.alice.stageide.croquet.models.personeditor.PersonInfo personInfo ) {
		this.setPersonInfo( personInfo );
		this.handleCataclysm( true, true, true );
	}
	
	private boolean isAlreadyHandlingCataclysm = false;
	private void handleCataclysm( boolean isLifeStageChange, boolean isGenderChange, boolean isHairColorChange ) {
		if( this.isAlreadyHandlingCataclysm ) {
			//pass
		} else {
			this.isAlreadyHandlingCataclysm = true;
			try {
				org.alice.apis.stage.LifeStage lifeStage = org.alice.stageide.croquet.models.personeditor.LifeStageSelectionState.getInstance().getSelectedItem();
				org.alice.apis.stage.Gender gender = org.alice.stageide.croquet.models.personeditor.GenderSelectionState.getInstance().getSelectedItem();
				String hairColor = org.alice.stageide.croquet.models.personeditor.HairColorSelectionState.getInstance().getSelectedItem();
				if( isLifeStageChange || isGenderChange || isHairColorChange ) {
					org.alice.stageide.croquet.models.personeditor.HairSelectionState.getInstance().handleCataclysmicChange( lifeStage, gender, hairColor );
				}
				if( isLifeStageChange || isGenderChange ) {
					org.alice.stageide.croquet.models.personeditor.FullBodyOutfitSelectionState.getInstance().handleCataclysmicChange( lifeStage, gender );
				}
				if( isLifeStageChange ) {
					org.alice.stageide.croquet.models.personeditor.HairColorSelectionState.getInstance().handleCataclysmicChange( lifeStage );
				}
				this.updatePerson();
				org.alice.apis.stage.Person person = PersonViewer.getSingleton().getPerson();
				org.alice.apis.stage.Hair hair = person.getHair();
				if( isLifeStageChange || isGenderChange || isHairColorChange ) {
					org.alice.stageide.croquet.models.personeditor.HairSelectionState.getInstance().setSelectedItem( hair );
				}
				if( isLifeStageChange || isGenderChange ) {
					org.alice.stageide.croquet.models.personeditor.FullBodyOutfitSelectionState.getInstance().setSelectedItem( (org.alice.apis.stage.FullBodyOutfit)person.getOutfit() );
				}
				if( isLifeStageChange ) {
					org.alice.stageide.croquet.models.personeditor.HairColorSelectionState.getInstance().setSelectedItem( hair.toString() );
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
				org.alice.apis.stage.LifeStage lifeStage = org.alice.stageide.croquet.models.personeditor.LifeStageSelectionState.getInstance().getSelectedItem();
				org.alice.apis.stage.Gender gender = org.alice.stageide.croquet.models.personeditor.GenderSelectionState.getInstance().getSelectedItem();
				String hairColor = org.alice.stageide.croquet.models.personeditor.HairColorSelectionState.getInstance().getSelectedItem();

				org.alice.apis.stage.FullBodyOutfit fullBodyOutfit = org.alice.stageide.croquet.models.personeditor.FullBodyOutfitSelectionState.getInstance().getSelectedItem();
				org.alice.apis.stage.BaseEyeColor baseEyeColor = org.alice.stageide.croquet.models.personeditor.BaseEyeColorSelectionState.getInstance().getSelectedItem();
				org.alice.apis.stage.BaseSkinTone baseSkinTone = org.alice.stageide.croquet.models.personeditor.BaseSkinToneSelectionState.getInstance().getSelectedItem();
				org.alice.apis.stage.Hair hair = org.alice.stageide.croquet.models.personeditor.HairSelectionState.getInstance().getSelectedItem();
				double fitnessLevel = org.alice.stageide.croquet.models.personeditor.FitnessModel.getInstance().getValue()*0.01;
				
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
		org.alice.stageide.croquet.models.personeditor.LifeStageSelectionState.getInstance().setSelectedItem( personInfo.getLifeStage() );
		org.alice.stageide.croquet.models.personeditor.GenderSelectionState.getInstance().setSelectedItem( personInfo.getGender() );
		org.alice.stageide.croquet.models.personeditor.BaseEyeColorSelectionState.getInstance().setSelectedItem( personInfo.getBaseEyeColor() );
		org.alice.stageide.croquet.models.personeditor.BaseSkinToneSelectionState.getInstance().setSelectedItem( personInfo.getBaseSkinTone() );
		org.alice.stageide.croquet.models.personeditor.FullBodyOutfitSelectionState.getInstance().setSelectedItem( personInfo.getFullBodyOutfit() );
		
		org.alice.apis.stage.Hair hair = personInfo.getHair();
		org.alice.stageide.croquet.models.personeditor.HairSelectionState.getInstance().setSelectedItem( hair );
		org.alice.stageide.croquet.models.personeditor.HairColorSelectionState.getInstance().setSelectedItem( hair.toString() );
		org.alice.stageide.croquet.models.personeditor.FitnessModel.getInstance().setValue( (int)(personInfo.getFitnessLevel()*100) );
	}
}
