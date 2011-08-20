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
public class PersonEditor extends org.lgna.croquet.components.BorderPanel {
	private static class SingletonHolder {
		private static PersonEditor instance = new PersonEditor();
	}
	public static PersonEditor getInstance() {
		return SingletonHolder.instance;
	}
	private java.util.Map<org.lgna.story.resources.sims2.LifeStage, org.lgna.story.implementation.sims2.SimsBipedImplementation> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private PersonEditor() {
		org.lgna.story.resources.sims2.LifeStage[] lifeStages = {  org.lgna.story.resources.sims2.LifeStage.ADULT, org.lgna.story.resources.sims2.LifeStage.CHILD };
		for( org.lgna.story.resources.sims2.LifeStage lifeStage : lifeStages ) {
			map.put( lifeStage, new org.lgna.story.implementation.sims2.SimsBipedImplementation( null, lifeStage ) );
		}

		org.alice.stageide.person.models.LifeStageState.getInstance().addValueObserver( new org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.LifeStage>() {
			public void changing( org.lgna.croquet.State< org.lgna.story.resources.sims2.LifeStage > state, org.lgna.story.resources.sims2.LifeStage prevValue, org.lgna.story.resources.sims2.LifeStage nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< org.lgna.story.resources.sims2.LifeStage > state, org.lgna.story.resources.sims2.LifeStage prevValue, org.lgna.story.resources.sims2.LifeStage nextValue, boolean isAdjusting ) {
				handleCataclysm( true, false, false );
			}
		} );
		org.alice.stageide.person.models.GenderState.getInstance().addValueObserver( new org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.Gender>() {
			public void changing( org.lgna.croquet.State< org.lgna.story.resources.sims2.Gender > state, org.lgna.story.resources.sims2.Gender prevValue, org.lgna.story.resources.sims2.Gender nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< org.lgna.story.resources.sims2.Gender > state, org.lgna.story.resources.sims2.Gender prevValue, org.lgna.story.resources.sims2.Gender nextValue, boolean isAdjusting ) {
				handleCataclysm( false, true, false );
			}
		} );
		org.alice.stageide.person.models.HairColorState.getInstance().addValueObserver( new org.lgna.croquet.State.ValueObserver<String>() {
			public void changing( org.lgna.croquet.State< String > state, String prevValue, String nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< String > state, String prevValue, String nextValue, boolean isAdjusting ) {
				handleCataclysm( false, false, true );
			}
		} );
		
		org.alice.stageide.person.models.FullBodyOutfitState.getInstance().addValueObserver( new org.lgna.croquet.State.ValueObserver<org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfit>() {
			public void changing( org.lgna.croquet.State< org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfit > state, org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfit prevValue, org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfit nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfit > state, org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfit prevValue, org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfit nextValue, boolean isAdjusting ) {
				updatePerson();
			}
		} );
		org.alice.stageide.person.models.HairState.getInstance().addValueObserver( new org.lgna.croquet.State.ValueObserver<org.lookingglassandalice.storytelling.resources.sims2.Hair>() {
			public void changing( org.lgna.croquet.State< org.lookingglassandalice.storytelling.resources.sims2.Hair > state, org.lookingglassandalice.storytelling.resources.sims2.Hair prevValue, org.lookingglassandalice.storytelling.resources.sims2.Hair nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< org.lookingglassandalice.storytelling.resources.sims2.Hair > state, org.lookingglassandalice.storytelling.resources.sims2.Hair prevValue, org.lookingglassandalice.storytelling.resources.sims2.Hair nextValue, boolean isAdjusting ) {
				updatePerson();
			}
		} );
		org.alice.stageide.person.models.ObesityState.getInstance().addValueObserver( new org.lgna.croquet.State.ValueObserver<Integer>() {
			public void changing( org.lgna.croquet.State< Integer > state, Integer prevValue, Integer nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< Integer > state, Integer prevValue, Integer nextValue, boolean isAdjusting ) {
				updatePerson();
			}
		} );
		org.alice.stageide.person.models.BaseEyeColorState.getInstance().addValueObserver( new org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.BaseEyeColor>() {
			public void changing( org.lgna.croquet.State< org.lgna.story.resources.sims2.BaseEyeColor > state, org.lgna.story.resources.sims2.BaseEyeColor prevValue, org.lgna.story.resources.sims2.BaseEyeColor nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< org.lgna.story.resources.sims2.BaseEyeColor > state, org.lgna.story.resources.sims2.BaseEyeColor prevValue, org.lgna.story.resources.sims2.BaseEyeColor nextValue, boolean isAdjusting ) {
				updatePerson();
			}
		} );


		final org.lgna.croquet.components.FolderTabbedPane<?> tabbedPane = org.alice.stageide.person.models.BodyHeadTabSelectionModel.getInstance().createDefaultFolderTabbedPane();
		tabbedPane.scaleFont( 1.5f );

		org.alice.stageide.person.models.BaseSkinToneState.getInstance().addValueObserver( new org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.BaseSkinTone>() {
			public void changing( org.lgna.croquet.State< org.lgna.story.resources.sims2.BaseSkinTone > state, org.lgna.story.resources.sims2.BaseSkinTone prevValue, org.lgna.story.resources.sims2.BaseSkinTone nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< org.lgna.story.resources.sims2.BaseSkinTone > state, org.lgna.story.resources.sims2.BaseSkinTone prevValue, org.lgna.story.resources.sims2.BaseSkinTone nextValue, boolean isAdjusting ) {
				updatePerson();
				tabbedPane.repaint();
			}
		} );
		

		org.lgna.croquet.components.BorderPanel northPane = new org.lgna.croquet.components.BorderPanel();
		northPane.addComponent( org.alice.stageide.person.models.RandomizeOperation.getInstance().createButton(), Constraint.PAGE_START );
		org.lgna.croquet.components.RowsSpringPanel ubiquitousPane = new org.lgna.croquet.components.RowsSpringPanel( 8, 8 ) {
			@Override
			protected java.util.List< org.lgna.croquet.components.Component< ? >[] > updateComponentRows( java.util.List< org.lgna.croquet.components.Component< ? >[] > rv ) {
				rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( "life stage:", new org.alice.stageide.person.components.LifeStageList() ) );
				rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( "gender:", new org.alice.stageide.person.components.GenderList() ) );
				rv.add( org.lgna.croquet.components.SpringUtilities.createLabeledRow( "skin tone:", new org.alice.stageide.person.components.BaseSkinToneList() ) );
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

		org.alice.stageide.person.models.ObesityState.getInstance().addValueObserver( new org.lgna.croquet.State.ValueObserver<Integer>() {
			public void changing( org.lgna.croquet.State< Integer > state, Integer prevValue, Integer nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< Integer > state, Integer prevValue, Integer nextValue, boolean isAdjusting ) {
				PersonViewer.getSingleton().getPerson().setObesityLevel( nextValue*0.01 );
			}
		} );

//		this.fitnessState.addValueObserver( new org.lgna.croquet.BoundedRangeIntegerState.ValueObserver< Integer >() {
//			public void changed(int nextValue) {
//				PersonViewer.getSingleton().setFitnessLevel( nextValue*0.01 );
//			}
//		} );
	}

	private org.lgna.croquet.State.ValueObserver<org.lgna.croquet.PredeterminedTab> tabChangeAdapter = new org.lgna.croquet.State.ValueObserver<org.lgna.croquet.PredeterminedTab>() {
		public void changing( org.lgna.croquet.State< org.lgna.croquet.PredeterminedTab > state, org.lgna.croquet.PredeterminedTab prevValue, org.lgna.croquet.PredeterminedTab nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.croquet.PredeterminedTab > state, org.lgna.croquet.PredeterminedTab prevValue, org.lgna.croquet.PredeterminedTab nextValue, boolean isAdjusting ) {
			//todo
		}
	};
	
	public void initialize( PersonInfo personInfo ) {
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
				org.lgna.story.resources.sims2.LifeStage lifeStage = org.alice.stageide.person.models.LifeStageState.getInstance().getSelectedItem();
				org.lgna.story.resources.sims2.Gender gender = org.alice.stageide.person.models.GenderState.getInstance().getSelectedItem();
				String hairColor = org.alice.stageide.person.models.HairColorState.getInstance().getSelectedItem();
				if( isLifeStageChange || isGenderChange || isHairColorChange ) {
					org.alice.stageide.person.models.HairState.getInstance().handleCataclysmicChange( lifeStage, gender, hairColor );
				}
				if( isLifeStageChange || isGenderChange ) {
					org.alice.stageide.person.models.FullBodyOutfitState.getInstance().handleCataclysmicChange( lifeStage, gender );
				}
				if( isLifeStageChange ) {
					org.alice.stageide.person.models.HairColorState.getInstance().handleCataclysmicChange( lifeStage );
				}
				this.updatePerson();
				org.lgna.story.implementation.sims2.SimsBipedImplementation person = PersonViewer.getSingleton().getPerson();
				org.lookingglassandalice.storytelling.resources.sims2.Hair hair = person.getHair();
				if( isLifeStageChange || isGenderChange || isHairColorChange ) {
					org.alice.stageide.person.models.HairState.getInstance().setSelectedItem( hair );
				}
				if( isLifeStageChange || isGenderChange ) {
					org.alice.stageide.person.models.FullBodyOutfitState.getInstance().setSelectedItem( (org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfit)person.getOutfit() );
				}
				if( isLifeStageChange ) {
					org.alice.stageide.person.models.HairColorState.getInstance().setSelectedItem( hair != null ? hair.toString() : null );
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
				org.lgna.story.resources.sims2.LifeStage lifeStage = org.alice.stageide.person.models.LifeStageState.getInstance().getSelectedItem();
				org.lgna.story.resources.sims2.Gender gender = org.alice.stageide.person.models.GenderState.getInstance().getSelectedItem();
				String hairColor = org.alice.stageide.person.models.HairColorState.getInstance().getSelectedItem();

				org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfit fullBodyOutfit = org.alice.stageide.person.models.FullBodyOutfitState.getInstance().getSelectedItem();
				org.lgna.story.resources.sims2.BaseEyeColor baseEyeColor = org.alice.stageide.person.models.BaseEyeColorState.getInstance().getSelectedItem();
				org.lgna.story.resources.sims2.BaseSkinTone baseSkinTone = org.alice.stageide.person.models.BaseSkinToneState.getInstance().getSelectedItem();
				org.lookingglassandalice.storytelling.resources.sims2.Hair hair = org.alice.stageide.person.models.HairState.getInstance().getSelectedItem();
				double fitnessLevel = org.alice.stageide.person.models.ObesityState.getInstance().getValue()*0.01;
				
				assert lifeStage != null;
				org.lgna.story.implementation.sims2.SimsBipedImplementation person = this.map.get( lifeStage );
				if( person != null ) {
					if( gender != null ) {
						person.setGender( gender );
					}
					if( baseSkinTone != null ) {
						person.setSkinTone( baseSkinTone );
						person.setObesityLevel( fitnessLevel );
						if( fullBodyOutfit != null && org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfitManager.getSingleton().isApplicable( fullBodyOutfit, lifeStage, gender ) ) {
							//pass
						} else {
//							org.lookingglassandalice.storytelling.Outfit outfit = person.getOutfit();
//							if( outfit instanceof org.lookingglassandalice.storytelling.FullBodyOutfit ) {
//								fullBodyOutfit = ( org.lookingglassandalice.storytelling.FullBodyOutfit )outfit;
//							} else {
								fullBodyOutfit = org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfitManager.getSingleton().getRandomEnumConstant( lifeStage, gender );
//							}
						}
						person.setOutfit( fullBodyOutfit );
					}
					if( baseEyeColor != null ) {
						person.setEyeColor( baseEyeColor );
					}
					if( gender != null ) {
						if( hair != null && org.lookingglassandalice.storytelling.resources.sims2.HairManager.getSingleton().isApplicable( hair, lifeStage, gender ) ) {
							//pass
						} else {
							try {
								Class<? extends org.lookingglassandalice.storytelling.resources.sims2.Hair> cls = org.lookingglassandalice.storytelling.resources.sims2.HairManager.getSingleton().getRandomClass(lifeStage, gender);
								java.lang.reflect.Field field = cls.getField( hairColor );
								hair = (org.lookingglassandalice.storytelling.resources.sims2.Hair)field.get( null );
							} catch( Exception e ) {
								hair = org.lookingglassandalice.storytelling.resources.sims2.HairManager.getSingleton().getRandomEnumConstant(lifeStage, gender);
							}
						}
						person.setHair( hair );
					}
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
		org.alice.stageide.person.models.LifeStageState.getInstance().setSelectedItem( personInfo.getLifeStage() );
		org.alice.stageide.person.models.GenderState.getInstance().setSelectedItem( personInfo.getGender() );
		org.alice.stageide.person.models.BaseEyeColorState.getInstance().setSelectedItem( personInfo.getBaseEyeColor() );
		org.alice.stageide.person.models.BaseSkinToneState.getInstance().setSelectedItem( personInfo.getBaseSkinTone() );
		org.alice.stageide.person.models.FullBodyOutfitState.getInstance().setSelectedItem( personInfo.getFullBodyOutfit() );
		
		org.lookingglassandalice.storytelling.resources.sims2.Hair hair = personInfo.getHair();
		org.alice.stageide.person.models.HairState.getInstance().setSelectedItem( hair );
		org.alice.stageide.person.models.HairColorState.getInstance().setSelectedItem( hair.toString() );
		org.alice.stageide.person.models.ObesityState.getInstance().setValue( (int)(personInfo.getFitnessLevel()*100) );
	}
}
