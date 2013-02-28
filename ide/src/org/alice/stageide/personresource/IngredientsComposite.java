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

/**
 * @author Dennis Cosgrove
 */
public class IngredientsComposite extends org.lgna.croquet.SimpleComposite<org.alice.stageide.personresource.views.IngredientsView> {
	private final org.lgna.croquet.Operation randomize = this.createActionOperation( this.createKey( "randomize" ), new Action() {
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			return createRandomEdit( step );
		}
	} );

	private final BodyTabComposite bodyTab = new BodyTabComposite();
	private final HeadTabComposite headTab = new HeadTabComposite();
	private final org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.LifeStage> lifeStageState = this.createListSelectionState( this.createKey( "lifeStageState" ), org.lgna.story.resources.sims2.LifeStage.class, edu.cmu.cs.dennisc.toolkit.croquet.codecs.EnumCodec.getInstance( org.lgna.story.resources.sims2.LifeStage.class ), 0, org.lgna.story.resources.sims2.LifeStage.ADULT, org.lgna.story.resources.sims2.LifeStage.CHILD );
	private final org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.Gender> genderState = this.createListSelectionStateForEnum( this.createKey( "genderState" ), org.lgna.story.resources.sims2.Gender.class, org.lgna.story.resources.sims2.Gender.getRandom() );
	private final org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.BaseSkinTone> baseSkinToneState = this.createListSelectionStateForEnum( this.createKey( "baseSkinToneState" ), org.lgna.story.resources.sims2.BaseSkinTone.class, org.lgna.story.resources.sims2.BaseSkinTone.getRandom() );
	private final org.lgna.croquet.TabSelectionState<org.lgna.croquet.SimpleTabComposite> bodyHeadTabState = this.createTabSelectionState( this.createKey( "bodyHeadTabState" ), 0, this.bodyTab, this.headTab );

	private final edu.cmu.cs.dennisc.map.MapToMap<org.lgna.story.resources.sims2.LifeStage, org.lgna.story.resources.sims2.Gender, org.lgna.story.resources.sims2.PersonResource> mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.LifeStage> lifeStageListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.LifeStage>() {
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.sims2.LifeStage> state, org.lgna.story.resources.sims2.LifeStage prevValue, org.lgna.story.resources.sims2.LifeStage nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		public void changed( org.lgna.croquet.State<org.lgna.story.resources.sims2.LifeStage> state, org.lgna.story.resources.sims2.LifeStage prevValue, org.lgna.story.resources.sims2.LifeStage nextValue, boolean isAdjusting ) {
			//			handleCataclysm( true, false, false );
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.Gender> genderListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.Gender>() {
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.sims2.Gender> state, org.lgna.story.resources.sims2.Gender prevValue, org.lgna.story.resources.sims2.Gender nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		public void changed( org.lgna.croquet.State<org.lgna.story.resources.sims2.Gender> state, org.lgna.story.resources.sims2.Gender prevValue, org.lgna.story.resources.sims2.Gender nextValue, boolean isAdjusting ) {
			//			handleCataclysm( false, true, false );
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.BaseSkinTone> baseSkinToneListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.BaseSkinTone>() {
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.sims2.BaseSkinTone> state, org.lgna.story.resources.sims2.BaseSkinTone prevValue, org.lgna.story.resources.sims2.BaseSkinTone nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		public void changed( org.lgna.croquet.State<org.lgna.story.resources.sims2.BaseSkinTone> state, org.lgna.story.resources.sims2.BaseSkinTone prevValue, org.lgna.story.resources.sims2.BaseSkinTone nextValue, boolean isAdjusting ) {
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.BaseEyeColor> baseEyeColorListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.BaseEyeColor>() {
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.sims2.BaseEyeColor> state, org.lgna.story.resources.sims2.BaseEyeColor prevValue, org.lgna.story.resources.sims2.BaseEyeColor nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		public void changed( org.lgna.croquet.State<org.lgna.story.resources.sims2.BaseEyeColor> state, org.lgna.story.resources.sims2.BaseEyeColor prevValue, org.lgna.story.resources.sims2.BaseEyeColor nextValue, boolean isAdjusting ) {
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<String> hairColorNameListener = new org.lgna.croquet.State.ValueListener<String>() {
		public void changing( org.lgna.croquet.State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		public void changed( org.lgna.croquet.State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
			//			handleCataclysm( false, false, true );
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.Hair> hairListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.Hair>() {
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.sims2.Hair> state, org.lgna.story.resources.sims2.Hair prevValue, org.lgna.story.resources.sims2.Hair nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		public void changed( org.lgna.croquet.State<org.lgna.story.resources.sims2.Hair> state, org.lgna.story.resources.sims2.Hair prevValue, org.lgna.story.resources.sims2.Hair nextValue, boolean isAdjusting ) {
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.FullBodyOutfit> fullBodyOutfitListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.FullBodyOutfit>() {
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.sims2.FullBodyOutfit> state, org.lgna.story.resources.sims2.FullBodyOutfit prevValue, org.lgna.story.resources.sims2.FullBodyOutfit nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		public void changed( org.lgna.croquet.State<org.lgna.story.resources.sims2.FullBodyOutfit> state, org.lgna.story.resources.sims2.FullBodyOutfit prevValue, org.lgna.story.resources.sims2.FullBodyOutfit nextValue, boolean isAdjusting ) {
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<Double> obesityLevelListener = new org.lgna.croquet.State.ValueListener<Double>() {
		public void changing( org.lgna.croquet.State<Double> state, Double prevValue, Double nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		public void changed( org.lgna.croquet.State<Double> state, Double prevValue, Double nextValue, boolean isAdjusting ) {
			popAtomic();
		}
	};

	private static final javax.swing.Icon RANDOM_ICON = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( IngredientsComposite.class.getResource( "images/random.png" ) );

	public IngredientsComposite() {
		super( java.util.UUID.fromString( "dd127381-09a8-4f78-bfd5-f3bffc1af98b" ) );
		this.randomize.setButtonIcon( RANDOM_ICON );
	}

	@Override
	protected org.alice.stageide.personresource.views.IngredientsView createView() {
		return new org.alice.stageide.personresource.views.IngredientsView( this );
	}

	public org.lgna.croquet.Operation getRandomize() {
		return this.randomize;
	}

	public org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.LifeStage> getLifeStageState() {
		return this.lifeStageState;
	}

	public org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.Gender> getGenderState() {
		return this.genderState;
	}

	public org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.BaseSkinTone> getBaseSkinToneState() {
		return this.baseSkinToneState;
	}

	public org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.Hair> getHairState() {
		return this.headTab.getHairState();
	}

	public org.lgna.croquet.ListSelectionState<String> getHairColorNameState() {
		return this.headTab.getHairColorNameState();
	}

	public org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.BaseEyeColor> getBaseEyeColorState() {
		return this.headTab.getBaseEyeColorState();
	}

	public org.lgna.croquet.ListSelectionState<org.lgna.story.resources.sims2.FullBodyOutfit> getFullBodyOutfitState() {
		return this.bodyTab.getFullBodyOutfitState();
	}

	public org.lgna.croquet.BoundedDoubleState getObesityLevelState() {
		return this.bodyTab.getObesityLevelState();
	}

	public org.lgna.croquet.TabSelectionState<org.lgna.croquet.SimpleTabComposite> getBodyHeadTabState() {
		return this.bodyHeadTabState;
	}

	//	private void handleCataclysm( boolean isLifeStage, boolean isGender, boolean isHairColor ) {
	//	}

	private org.lgna.croquet.edits.Edit createRandomEdit( org.lgna.croquet.history.CompletionStep<?> step ) {
		org.lgna.story.resources.sims2.LifeStage lifeStage;
		if( this.lifeStageState.isEnabled() ) {
			lifeStage = null;
		} else {
			lifeStage = this.lifeStageState.getValue();
		}
		org.lgna.story.resources.sims2.PersonResource nextPersonResource = org.alice.stageide.personresource.RandomPersonUtilities.createRandomResource( lifeStage );
		return new org.alice.stageide.personresource.edits.SetPersonResourceEdit( step, nextPersonResource );
	}

	private void addListeners() {
		this.getLifeStageState().addValueListener( this.lifeStageListener );
		this.getGenderState().addValueListener( this.genderListener );
		this.getBaseSkinToneState().addValueListener( this.baseSkinToneListener );
		this.getBaseEyeColorState().addValueListener( this.baseEyeColorListener );
		this.getHairColorNameState().addValueListener( this.hairColorNameListener );
		this.getHairState().addValueListener( this.hairListener );
		this.getFullBodyOutfitState().addValueListener( this.fullBodyOutfitListener );
		this.getObesityLevelState().addValueListener( this.obesityLevelListener );
	}

	private void removeListeners() {
		this.getLifeStageState().removeValueListener( this.lifeStageListener );
		this.getGenderState().removeValueListener( this.genderListener );
		this.getBaseSkinToneState().removeValueListener( this.baseSkinToneListener );
		this.getBaseEyeColorState().removeValueListener( this.baseEyeColorListener );
		this.getHairColorNameState().removeValueListener( this.hairColorNameListener );
		this.getHairState().removeValueListener( this.hairListener );
		this.getFullBodyOutfitState().removeValueListener( this.fullBodyOutfitListener );
		this.getObesityLevelState().removeValueListener( this.obesityLevelListener );
	}

	private int activeCount = 0;

	private void addListenersIfAppropriate() {
		if( activeCount > 0 ) {
			this.addListeners();
		}
	}

	private void removeListenersIfAppropriate() {
		if( activeCount > 0 ) {
			this.removeListeners();
		}
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		this.pushAtomic();
		//		this.setStates( personResource );
		if( activeCount == 0 ) {
			this.addListeners();
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, this.activeCount );
		}
		this.activeCount++;
		this.popAtomic();
	}

	@Override
	public void handlePostDeactivation() {
		this.activeCount--;
		if( activeCount == 0 ) {
			this.removeListeners();
		}
		//personViewer.setPerson( null );
		if( activeCount != 0 ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "todo" );
		}
		super.handlePostDeactivation();
	}

	//	public org.alice.stageide.person.components.PersonViewer allocatePersonViewer( org.lgna.story.resources.sims2.PersonResource personResource ) {
	//		assert activeCount == 0; //todo
	//		this.pushAtomic();
	//		this.setStates( personResource );
	//		if( activeCount == 0 ) {
	//			this.addListeners();
	//		}
	//		this.activeCount++;
	//		this.popAtomic();
	//		//return org.alice.stageide.person.components.PersonViewer.getInstance( this.personImp );
	//		return null;
	//	}
	//
	//	public void releasePersonViewer( org.alice.stageide.person.components.PersonViewer personViewer ) {
	//		this.activeCount--;
	//		if( activeCount == 0 ) {
	//			this.removeListeners();
	//		}
	//		//personViewer.setPerson( null );
	//		assert activeCount == 0; //todo
	//	}

	private static org.lgna.story.resources.sims2.LifeStage getLifeStage( org.lgna.story.resources.sims2.PersonResource personResource ) {
		return personResource != null ? personResource.getLifeStage() : null;
	}

	private static org.lgna.story.resources.sims2.Gender getGender( org.lgna.story.resources.sims2.PersonResource personResource ) {
		return personResource != null ? personResource.getGender() : null;
	}

	private static String getHairColorName( org.lgna.story.resources.sims2.PersonResource personResource ) {
		if( personResource != null ) {
			org.lgna.story.resources.sims2.Hair hair = personResource.getHair();
			return hair != null ? hair.toString() : null;
		} else {
			return null;
		}
	}

	private static final String[] getHairColors( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		return lifeStage != null ? lifeStage.getHairColors() : null;
	}

	private org.lgna.story.resources.sims2.PersonResource prevPersonResource;
	private int atomicCount = 0;

	public void pushAtomic() {
		if( this.atomicCount == 0 ) {
		}
		this.atomicCount++;
	}

	public void popAtomic() {
		this.atomicCount--;
		if( this.atomicCount == 0 ) {
			this.removeListenersIfAppropriate();
			try {
				org.lgna.story.resources.sims2.LifeStage prevLifeStage = getLifeStage( this.prevPersonResource );
				org.lgna.story.resources.sims2.LifeStage nextLifeStage = this.getLifeStageState().getValue();
				boolean isLifeStageChanged = prevLifeStage != nextLifeStage;
				if( isLifeStageChanged ) {
					String[] hairColors = getHairColors( nextLifeStage );
					if( hairColors != getHairColors( getLifeStage( this.prevPersonResource ) ) ) {
						String hairColor = this.getHairColorNameState().getValue();
						int index = java.util.Arrays.asList( hairColors ).indexOf( hairColor );
						if( index != -1 ) {
							//pass
						} else {
							index = org.lgna.common.RandomUtilities.nextIntegerFrom0ToNExclusive( hairColors.length );
						}
						this.getHairColorNameState().setListData( index, hairColors );
					}
				}
				org.lgna.story.resources.sims2.Gender nextGender = this.getGenderState().getValue();
				org.lgna.story.resources.sims2.Gender prevGender = getGender( this.prevPersonResource );
				boolean isGenderChanged = prevGender != nextGender;

				String prevHairColorName = getHairColorName( this.prevPersonResource );
				final String nextHairColorName = this.getHairColorNameState().getValue();
				boolean isHairColorChanged = edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areNotEquivalent( prevHairColorName, nextHairColorName );
				if( isLifeStageChanged || isGenderChanged || isHairColorChanged ) {
					java.util.List<org.lgna.story.resources.sims2.Hair> list = edu.cmu.cs.dennisc.java.lang.EnumUtilities.getEnumConstants(
							org.lgna.story.resources.sims2.HairManager.getSingleton().getImplementingClasses( nextLifeStage, nextGender ),
							new edu.cmu.cs.dennisc.pattern.Criterion<org.lgna.story.resources.sims2.Hair>() {
								public boolean accept( org.lgna.story.resources.sims2.Hair hair ) {
									return hair.toString().equals( nextHairColorName );
								}
							}
							);
					int index;
					org.lgna.story.resources.sims2.Hair hair = this.getHairState().getValue();
					if( hair != null ) {
						index = list.indexOf( hair );
					} else {
						index = -1;
					}
					index = 0;
					this.getHairState().setListData(
							index,
							edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray(
									list,
									org.lgna.story.resources.sims2.Hair.class
									)
							);
				}
				if( isLifeStageChanged || isGenderChanged ) {
					java.util.List<org.lgna.story.resources.sims2.FullBodyOutfit> list = edu.cmu.cs.dennisc.java.lang.EnumUtilities.getEnumConstants(
							org.lgna.story.resources.sims2.FullBodyOutfitManager.getSingleton().getImplementingClasses( nextLifeStage, nextGender ),
							null
							);
					int index = 0;
					this.getFullBodyOutfitState().setListData( index, edu.cmu.cs.dennisc.java.lang.ArrayUtilities.createArray(
							list,
							org.lgna.story.resources.sims2.FullBodyOutfit.class
							) );
				}
			} finally {
				this.addListenersIfAppropriate();
			}

			PersonResourceComposite.getInstance().getPreviewComposite().getView().getPerson().updateNebPerson();
			this.getView().repaint();

			this.prevPersonResource = this.createResourceFromStates();
			if( this.prevPersonResource != null ) {
				this.mapToMap.put( this.prevPersonResource.getLifeStage(), this.prevPersonResource.getGender(), this.prevPersonResource );
			}
		}
	}

	public org.lgna.story.resources.sims2.PersonResource createResourceFromStates() {
		org.lgna.story.resources.sims2.LifeStage lifeStage = this.getLifeStageState().getValue();
		org.lgna.story.resources.sims2.Gender gender = this.getGenderState().getValue();
		org.lgna.story.resources.sims2.SkinTone skinTone = this.getBaseSkinToneState().getValue();
		org.lgna.story.resources.sims2.EyeColor eyeColor = this.getBaseEyeColorState().getValue();
		org.lgna.story.resources.sims2.Outfit outfit = this.getFullBodyOutfitState().getValue();
		org.lgna.story.resources.sims2.Hair hair = this.getHairState().getValue();
		double obesityLevel = this.getObesityLevelState().getValue();
		if( lifeStage != null ) {
			return lifeStage.createResource( gender, skinTone, eyeColor, hair, obesityLevel, outfit );
		} else {
			return null;
		}
	}

	public void setStates( org.lgna.story.resources.sims2.PersonResource personResource ) {
		this.removeListenersIfAppropriate();
		try {
			this.getLifeStageState().setValueTransactionlessly( personResource.getLifeStage() );
			this.getGenderState().setValueTransactionlessly( personResource.getGender() );
			this.getBaseEyeColorState().setValueTransactionlessly( (org.lgna.story.resources.sims2.BaseEyeColor)personResource.getEyeColor() );
			this.getBaseSkinToneState().setValueTransactionlessly( (org.lgna.story.resources.sims2.BaseSkinTone)personResource.getSkinTone() );
			this.getFullBodyOutfitState().setValueTransactionlessly( (org.lgna.story.resources.sims2.FullBodyOutfit)personResource.getOutfit() );

			org.lgna.story.resources.sims2.Hair hair = personResource.getHair();
			this.getHairState().setValueTransactionlessly( hair );
			this.getHairColorNameState().setValueTransactionlessly( hair != null ? hair.toString() : null );
			this.getObesityLevelState().setValueTransactionlessly( personResource.getObesityLevel() );
		} finally {
			this.addListenersIfAppropriate();
		}
	}

}
