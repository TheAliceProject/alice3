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

package org.alice.stageide.person;

/**
 * @author Dennis Cosgrove
 */
public enum PersonResourceManager {
	SINGLETON;
	private final org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.LifeStage> lifeStageObserver = new org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.LifeStage>() {
		public void changing( org.lgna.croquet.State< org.lgna.story.resources.sims2.LifeStage > state, org.lgna.story.resources.sims2.LifeStage prevValue, org.lgna.story.resources.sims2.LifeStage nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.story.resources.sims2.LifeStage > state, org.lgna.story.resources.sims2.LifeStage prevValue, org.lgna.story.resources.sims2.LifeStage nextValue, boolean isAdjusting ) {
			handleCataclysm( true, false, false );
		}
	};
	private final org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.Gender> genderObserver = new org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.Gender>() {
		public void changing( org.lgna.croquet.State< org.lgna.story.resources.sims2.Gender > state, org.lgna.story.resources.sims2.Gender prevValue, org.lgna.story.resources.sims2.Gender nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.story.resources.sims2.Gender > state, org.lgna.story.resources.sims2.Gender prevValue, org.lgna.story.resources.sims2.Gender nextValue, boolean isAdjusting ) {
			handleCataclysm( false, true, false );
		}
	};
	private final org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.BaseSkinTone> baseSkinToneObserver = new org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.BaseSkinTone>() {
		public void changing( org.lgna.croquet.State< org.lgna.story.resources.sims2.BaseSkinTone > state, org.lgna.story.resources.sims2.BaseSkinTone prevValue, org.lgna.story.resources.sims2.BaseSkinTone nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.story.resources.sims2.BaseSkinTone > state, org.lgna.story.resources.sims2.BaseSkinTone prevValue, org.lgna.story.resources.sims2.BaseSkinTone nextValue, boolean isAdjusting ) {
			updatePerson();
		}
	};
	private final org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.BaseEyeColor> baseEyeColorObserver = new org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.BaseEyeColor>() {
		public void changing( org.lgna.croquet.State< org.lgna.story.resources.sims2.BaseEyeColor > state, org.lgna.story.resources.sims2.BaseEyeColor prevValue, org.lgna.story.resources.sims2.BaseEyeColor nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.story.resources.sims2.BaseEyeColor > state, org.lgna.story.resources.sims2.BaseEyeColor prevValue, org.lgna.story.resources.sims2.BaseEyeColor nextValue, boolean isAdjusting ) {
			updatePerson();
		}
	};
	private final org.lgna.croquet.State.ValueObserver<String> hairColorNameObserver = new org.lgna.croquet.State.ValueObserver<String>() {
		public void changing( org.lgna.croquet.State< String > state, String prevValue, String nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< String > state, String prevValue, String nextValue, boolean isAdjusting ) {
			handleCataclysm( false, false, true );
		}
	};
	private final org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.Hair> hairObserver = new org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.Hair>() {
		public void changing( org.lgna.croquet.State< org.lgna.story.resources.sims2.Hair > state, org.lgna.story.resources.sims2.Hair prevValue, org.lgna.story.resources.sims2.Hair nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.story.resources.sims2.Hair > state, org.lgna.story.resources.sims2.Hair prevValue, org.lgna.story.resources.sims2.Hair nextValue, boolean isAdjusting ) {
			updatePerson();
		}
	};
	private final org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.FullBodyOutfit> fullBodyOutfitObserver = new org.lgna.croquet.State.ValueObserver<org.lgna.story.resources.sims2.FullBodyOutfit>() {
		public void changing( org.lgna.croquet.State< org.lgna.story.resources.sims2.FullBodyOutfit > state, org.lgna.story.resources.sims2.FullBodyOutfit prevValue, org.lgna.story.resources.sims2.FullBodyOutfit nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.story.resources.sims2.FullBodyOutfit > state, org.lgna.story.resources.sims2.FullBodyOutfit prevValue, org.lgna.story.resources.sims2.FullBodyOutfit nextValue, boolean isAdjusting ) {
			updatePerson();
		}
	};
	private final org.lgna.croquet.State.ValueObserver<Integer> obesityPercentStateObserver = new org.lgna.croquet.State.ValueObserver<Integer>() {
		public void changing( org.lgna.croquet.State< Integer > state, Integer prevValue, Integer nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Integer > state, Integer prevValue, Integer nextValue, boolean isAdjusting ) {
			updatePerson();
		}
	};
	private java.util.Map<org.lgna.story.resources.sims2.LifeStage, org.lgna.story.implementation.sims2.SimsBipedImplementation> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private int count = 0;
	private PersonResourceManager() {
		org.lgna.story.resources.sims2.LifeStage[] lifeStages = {  org.lgna.story.resources.sims2.LifeStage.ADULT, org.lgna.story.resources.sims2.LifeStage.CHILD };
		for( org.lgna.story.resources.sims2.LifeStage lifeStage : lifeStages ) {
			map.put( lifeStage, new org.lgna.story.implementation.sims2.SimsBipedImplementation( null, lifeStage ) );
		}
	}	
	public void push() {
		if( count == 0 ) {
			org.alice.stageide.person.models.LifeStageState.getInstance().addValueObserver( this.lifeStageObserver );
			org.alice.stageide.person.models.GenderState.getInstance().addValueObserver( this.genderObserver );
			org.alice.stageide.person.models.BaseSkinToneState.getInstance().addValueObserver( this.baseSkinToneObserver );
			org.alice.stageide.person.models.BaseEyeColorState.getInstance().addValueObserver( this.baseEyeColorObserver );
			org.alice.stageide.person.models.HairColorNameState.getInstance().addValueObserver( this.hairColorNameObserver );
			org.alice.stageide.person.models.HairState.getInstance().addValueObserver( this.hairObserver );
			org.alice.stageide.person.models.FullBodyOutfitState.getInstance().addValueObserver( this.fullBodyOutfitObserver );
			org.alice.stageide.person.models.ObesityPercentState.getInstance().addValueObserver( this.obesityPercentStateObserver );
			this.handleCataclysm( true, true, true );
		}
		this.count++;
	}
	public void pop() {
		this.count--;
		if( count == 0 ) {
			org.alice.stageide.person.models.LifeStageState.getInstance().removeValueObserver( this.lifeStageObserver );
			org.alice.stageide.person.models.GenderState.getInstance().removeValueObserver( this.genderObserver );
			org.alice.stageide.person.models.BaseSkinToneState.getInstance().removeValueObserver( this.baseSkinToneObserver );
			org.alice.stageide.person.models.BaseEyeColorState.getInstance().removeValueObserver( this.baseEyeColorObserver );
			org.alice.stageide.person.models.HairColorNameState.getInstance().removeValueObserver( this.hairColorNameObserver );
			org.alice.stageide.person.models.HairState.getInstance().removeValueObserver( this.hairObserver );
			org.alice.stageide.person.models.FullBodyOutfitState.getInstance().removeValueObserver( this.fullBodyOutfitObserver );
			org.alice.stageide.person.models.ObesityPercentState.getInstance().removeValueObserver( this.obesityPercentStateObserver );
		}
	}
	
	public void setStates( org.lgna.story.resources.sims2.PersonResource personResource ) {
		org.alice.stageide.person.models.LifeStageState.getInstance().setSelectedItem( personResource.getLifeStage() );
		org.alice.stageide.person.models.GenderState.getInstance().setSelectedItem( personResource.getGender() );
		org.alice.stageide.person.models.BaseEyeColorState.getInstance().setSelectedItem( (org.lgna.story.resources.sims2.BaseEyeColor)personResource.getEyeColor() );
		org.alice.stageide.person.models.BaseSkinToneState.getInstance().setSelectedItem( (org.lgna.story.resources.sims2.BaseSkinTone)personResource.getSkinTone() );
		org.alice.stageide.person.models.FullBodyOutfitState.getInstance().setSelectedItem( (org.lgna.story.resources.sims2.FullBodyOutfit)personResource.getOutfit() );
		
		org.lgna.story.resources.sims2.Hair hair = personResource.getHair();
		org.alice.stageide.person.models.HairState.getInstance().setSelectedItem( hair );
		org.alice.stageide.person.models.HairColorNameState.getInstance().setSelectedItem( hair != null ? hair.toString() : null );
		org.alice.stageide.person.models.ObesityPercentState.getInstance().setValue( (int)(personResource.getObesityLevel()*100) );
	}
	public org.lgna.story.resources.sims2.PersonResource createResourceFromStates() {
		org.lgna.story.resources.sims2.LifeStage lifeStage = org.alice.stageide.person.models.LifeStageState.getInstance().getValue();
		org.lgna.story.resources.sims2.Gender gender = org.alice.stageide.person.models.GenderState.getInstance().getValue();
		org.lgna.story.resources.sims2.SkinTone skinTone = org.alice.stageide.person.models.BaseSkinToneState.getInstance().getValue();
		org.lgna.story.resources.sims2.EyeColor eyeColor = org.alice.stageide.person.models.BaseEyeColorState.getInstance().getValue();
		org.lgna.story.resources.sims2.Outfit outfit = org.alice.stageide.person.models.FullBodyOutfitState.getInstance().getValue();
		org.lgna.story.resources.sims2.Hair hair = org.alice.stageide.person.models.HairState.getInstance().getValue();
		double obesityLevel = org.alice.stageide.person.models.ObesityPercentState.getInstance().getValue() * 0.01;
		return lifeStage.createResource( gender, skinTone, eyeColor, hair, obesityLevel, outfit );
	}
	public org.lgna.story.resources.sims2.PersonResource createRandomResource() {
		org.lgna.story.resources.sims2.LifeStage[] potentialLifeStages = { org.lgna.story.resources.sims2.LifeStage.ADULT, org.lgna.story.resources.sims2.LifeStage.CHILD };
		org.lgna.story.resources.sims2.LifeStage lifeStage = edu.cmu.cs.dennisc.random.RandomUtilities.getRandomValueFrom( potentialLifeStages );
		org.lgna.story.resources.sims2.Gender gender = org.lgna.story.resources.sims2.Gender.getRandom();
		org.lgna.story.resources.sims2.SkinTone skinTone = org.lgna.story.resources.sims2.BaseSkinTone.getRandom();
		org.lgna.story.resources.sims2.EyeColor eyeColor = org.lgna.story.resources.sims2.BaseEyeColor.getRandom();
		org.lgna.story.resources.sims2.Outfit outfit = org.lgna.story.resources.sims2.FullBodyOutfitManager.getSingleton().getRandomEnumConstant( lifeStage, gender );
		org.lgna.story.resources.sims2.Hair hair = org.lgna.story.resources.sims2.HairManager.getSingleton().getRandomEnumConstant( lifeStage, gender );
		double obesityLevel = org.alice.random.RandomUtilities.nextDouble();
		return lifeStage.createResource( gender, skinTone, eyeColor, hair, obesityLevel, outfit );
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
				String hairColor = org.alice.stageide.person.models.HairColorNameState.getInstance().getSelectedItem();
				if( isLifeStageChange || isGenderChange || isHairColorChange ) {
					org.alice.stageide.person.models.HairState.getInstance().handleCataclysmicChange( lifeStage, gender, hairColor );
				}
				if( isLifeStageChange || isGenderChange ) {
					org.alice.stageide.person.models.FullBodyOutfitState.getInstance().handleCataclysmicChange( lifeStage, gender );
				}
				if( isLifeStageChange ) {
					org.alice.stageide.person.models.HairColorNameState.getInstance().handleCataclysmicChange( lifeStage );
				}
				this.updatePerson();
				org.lgna.story.implementation.sims2.SimsBipedImplementation person = org.alice.stageide.person.components.PersonViewer.getSingleton().getPerson();
				org.lgna.story.resources.sims2.Hair hair = person.getHair();
				if( isLifeStageChange || isGenderChange || isHairColorChange ) {
					org.alice.stageide.person.models.HairState.getInstance().setSelectedItem( hair );
				}
				if( isLifeStageChange || isGenderChange ) {
					org.alice.stageide.person.models.FullBodyOutfitState.getInstance().setSelectedItem( (org.lgna.story.resources.sims2.FullBodyOutfit)person.getOutfit() );
				}
				if( isLifeStageChange ) {
					org.alice.stageide.person.models.HairColorNameState.getInstance().setSelectedItem( hair != null ? hair.toString() : null );
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
				String hairColor = org.alice.stageide.person.models.HairColorNameState.getInstance().getSelectedItem();

				org.lgna.story.resources.sims2.FullBodyOutfit fullBodyOutfit = org.alice.stageide.person.models.FullBodyOutfitState.getInstance().getSelectedItem();
				org.lgna.story.resources.sims2.BaseEyeColor baseEyeColor = org.alice.stageide.person.models.BaseEyeColorState.getInstance().getSelectedItem();
				org.lgna.story.resources.sims2.BaseSkinTone baseSkinTone = org.alice.stageide.person.models.BaseSkinToneState.getInstance().getSelectedItem();
				org.lgna.story.resources.sims2.Hair hair = org.alice.stageide.person.models.HairState.getInstance().getSelectedItem();
				double fitnessLevel = org.alice.stageide.person.models.ObesityPercentState.getInstance().getValue()*0.01;
				
				assert lifeStage != null;
				org.lgna.story.implementation.sims2.SimsBipedImplementation person = this.map.get( lifeStage );
				if( person != null ) {
					if( gender != null ) {
						person.setGender( gender );
					}
					if( baseSkinTone != null ) {
						person.setSkinTone( baseSkinTone );
						person.setObesityLevel( fitnessLevel );
						if( fullBodyOutfit != null && org.lgna.story.resources.sims2.FullBodyOutfitManager.getSingleton().isApplicable( fullBodyOutfit, lifeStage, gender ) ) {
							//pass
						} else {
//							org.lookingglassandalice.storytelling.Outfit outfit = person.getOutfit();
//							if( outfit instanceof org.lookingglassandalice.storytelling.FullBodyOutfit ) {
//								fullBodyOutfit = ( org.lookingglassandalice.storytelling.FullBodyOutfit )outfit;
//							} else {
								fullBodyOutfit = org.lgna.story.resources.sims2.FullBodyOutfitManager.getSingleton().getRandomEnumConstant( lifeStage, gender );
//							}
						}
						person.setOutfit( fullBodyOutfit );
					}
					if( baseEyeColor != null ) {
						person.setEyeColor( baseEyeColor );
					}
					if( gender != null ) {
						if( hair != null && org.lgna.story.resources.sims2.HairManager.getSingleton().isApplicable( hair, lifeStage, gender ) ) {
							//pass
						} else {
							try {
								Class<? extends org.lgna.story.resources.sims2.Hair> cls = org.lgna.story.resources.sims2.HairManager.getSingleton().getRandomClass(lifeStage, gender);
								java.lang.reflect.Field field = cls.getField( hairColor );
								hair = (org.lgna.story.resources.sims2.Hair)field.get( null );
							} catch( Exception e ) {
								hair = org.lgna.story.resources.sims2.HairManager.getSingleton().getRandomEnumConstant(lifeStage, gender);
							}
						}
						person.setHair( hair );
					}
					org.alice.stageide.person.components.PersonViewer.getSingleton().setPerson( person );
				}
			} finally {
				//edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().releaseRenderingLock();
				this.isAlreadyUpdating = false;
			}
		}
	}
	
}
