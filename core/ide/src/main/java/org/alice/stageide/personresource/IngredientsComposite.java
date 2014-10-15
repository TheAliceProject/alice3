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
	private final org.lgna.croquet.Operation randomize = this.createActionOperation( "randomize", new Action() {
		@Override
		public org.lgna.croquet.edits.AbstractEdit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			return createRandomEdit( step );
		}
	} );

	private final org.lgna.croquet.BoundedDoubleState obesityLevelState = this.createBoundedDoubleState( "obesityLevelState", new BoundedDoubleDetails() );
	private final FullBodyOutfitTabComposite bodyTab = new FullBodyOutfitTabComposite();
	private final TopAndBottomOutfitTabComposite topAndBottomTab = new TopAndBottomOutfitTabComposite();
	private final HairTabComposite hairTab = new HairTabComposite();
	private final FaceTabComposite faceTab = new FaceTabComposite();
	private final org.lgna.croquet.ImmutableDataSingleSelectListState<org.lgna.story.resources.sims2.LifeStage> lifeStageState = this.createImmutableListStateForEnum( "lifeStageState", org.lgna.story.resources.sims2.LifeStage.class, org.lgna.story.resources.sims2.LifeStage.getRandom() );
	private final org.lgna.croquet.ImmutableDataSingleSelectListState<org.lgna.story.resources.sims2.Gender> genderState = this.createImmutableListStateForEnum( "genderState", org.lgna.story.resources.sims2.Gender.class, org.lgna.story.resources.sims2.Gender.getRandom() );
	private final SkinColorState skinColorState = new SkinColorState();
	private final org.lgna.croquet.ImmutableDataTabState<org.lgna.croquet.SimpleTabComposite<?>> bodyHeadHairTabState = this.createImmutableTabState( "bodyHeadHairTabState", 0, this.bodyTab, this.topAndBottomTab, null, this.hairTab, null, this.faceTab );

	private org.alice.stageide.personresource.OutfitTabComposite<? extends org.alice.stageide.personresource.views.OutfitTabView> lastActiveOutfitTab = null;

	private final edu.cmu.cs.dennisc.map.MapToMap<org.lgna.story.resources.sims2.LifeStage, org.lgna.story.resources.sims2.Gender, org.lgna.story.resources.sims2.PersonResource> mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.LifeStage> lifeStageListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.LifeStage>() {
		@Override
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.sims2.LifeStage> state, org.lgna.story.resources.sims2.LifeStage prevValue, org.lgna.story.resources.sims2.LifeStage nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		@Override
		public void changed( org.lgna.croquet.State<org.lgna.story.resources.sims2.LifeStage> state, org.lgna.story.resources.sims2.LifeStage prevValue, org.lgna.story.resources.sims2.LifeStage nextValue, boolean isAdjusting ) {
			popAtomic();
			updateCameraPointOfView();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.Gender> genderListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.Gender>() {
		@Override
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.sims2.Gender> state, org.lgna.story.resources.sims2.Gender prevValue, org.lgna.story.resources.sims2.Gender nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		@Override
		public void changed( org.lgna.croquet.State<org.lgna.story.resources.sims2.Gender> state, org.lgna.story.resources.sims2.Gender prevValue, org.lgna.story.resources.sims2.Gender nextValue, boolean isAdjusting ) {
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<java.awt.Color> skinColorListener = new org.lgna.croquet.State.ValueListener<java.awt.Color>() {
		@Override
		public void changing( org.lgna.croquet.State<java.awt.Color> state, java.awt.Color prevValue, java.awt.Color nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		@Override
		public void changed( org.lgna.croquet.State<java.awt.Color> state, java.awt.Color prevValue, java.awt.Color nextValue, boolean isAdjusting ) {
			popAtomic();
			handleSkinColorChange( nextValue );
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.BaseFace> faceListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.BaseFace>() {
		@Override
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.sims2.BaseFace> state, org.lgna.story.resources.sims2.BaseFace prevValue, org.lgna.story.resources.sims2.BaseFace nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		@Override
		public void changed( org.lgna.croquet.State<org.lgna.story.resources.sims2.BaseFace> state, org.lgna.story.resources.sims2.BaseFace prevValue, org.lgna.story.resources.sims2.BaseFace nextValue, boolean isAdjusting ) {
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.BaseEyeColor> baseEyeColorListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.BaseEyeColor>() {
		@Override
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.sims2.BaseEyeColor> state, org.lgna.story.resources.sims2.BaseEyeColor prevValue, org.lgna.story.resources.sims2.BaseEyeColor nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		@Override
		public void changed( org.lgna.croquet.State<org.lgna.story.resources.sims2.BaseEyeColor> state, org.lgna.story.resources.sims2.BaseEyeColor prevValue, org.lgna.story.resources.sims2.BaseEyeColor nextValue, boolean isAdjusting ) {
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.alice.stageide.personresource.data.HairColorName> hairColorNameListener = new org.lgna.croquet.State.ValueListener<org.alice.stageide.personresource.data.HairColorName>() {
		@Override
		public void changing( org.lgna.croquet.State<org.alice.stageide.personresource.data.HairColorName> state, org.alice.stageide.personresource.data.HairColorName prevValue, org.alice.stageide.personresource.data.HairColorName nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		@Override
		public void changed( org.lgna.croquet.State<org.alice.stageide.personresource.data.HairColorName> state, org.alice.stageide.personresource.data.HairColorName prevValue, org.alice.stageide.personresource.data.HairColorName nextValue, boolean isAdjusting ) {
			addHairColorNameToFront( nextValue );
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.alice.stageide.personresource.data.HairHatStyle> hairListener = new org.lgna.croquet.State.ValueListener<org.alice.stageide.personresource.data.HairHatStyle>() {
		@Override
		public void changing( org.lgna.croquet.State<org.alice.stageide.personresource.data.HairHatStyle> state, org.alice.stageide.personresource.data.HairHatStyle prevValue, org.alice.stageide.personresource.data.HairHatStyle nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		@Override
		public void changed( org.lgna.croquet.State<org.alice.stageide.personresource.data.HairHatStyle> state, org.alice.stageide.personresource.data.HairHatStyle prevValue, org.alice.stageide.personresource.data.HairHatStyle nextValue, boolean isAdjusting ) {
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.FullBodyOutfit> fullBodyOutfitListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.FullBodyOutfit>() {
		@Override
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.sims2.FullBodyOutfit> state, org.lgna.story.resources.sims2.FullBodyOutfit prevValue, org.lgna.story.resources.sims2.FullBodyOutfit nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		@Override
		public void changed( org.lgna.croquet.State<org.lgna.story.resources.sims2.FullBodyOutfit> state, org.lgna.story.resources.sims2.FullBodyOutfit prevValue, org.lgna.story.resources.sims2.FullBodyOutfit nextValue, boolean isAdjusting ) {
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.TopPiece> topPieceListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.TopPiece>() {
		@Override
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.sims2.TopPiece> state, org.lgna.story.resources.sims2.TopPiece prevValue, org.lgna.story.resources.sims2.TopPiece nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		@Override
		public void changed( org.lgna.croquet.State<org.lgna.story.resources.sims2.TopPiece> state, org.lgna.story.resources.sims2.TopPiece prevValue, org.lgna.story.resources.sims2.TopPiece nextValue, boolean isAdjusting ) {
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.BottomPiece> bottomPieceListener = new org.lgna.croquet.State.ValueListener<org.lgna.story.resources.sims2.BottomPiece>() {
		@Override
		public void changing( org.lgna.croquet.State<org.lgna.story.resources.sims2.BottomPiece> state, org.lgna.story.resources.sims2.BottomPiece prevValue, org.lgna.story.resources.sims2.BottomPiece nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		@Override
		public void changed( org.lgna.croquet.State<org.lgna.story.resources.sims2.BottomPiece> state, org.lgna.story.resources.sims2.BottomPiece prevValue, org.lgna.story.resources.sims2.BottomPiece nextValue, boolean isAdjusting ) {
			popAtomic();
		}
	};
	private final org.lgna.croquet.State.ValueListener<Double> obesityLevelListener = new org.lgna.croquet.State.ValueListener<Double>() {
		@Override
		public void changing( org.lgna.croquet.State<Double> state, Double prevValue, Double nextValue, boolean isAdjusting ) {
			pushAtomic();
		}

		@Override
		public void changed( org.lgna.croquet.State<Double> state, Double prevValue, Double nextValue, boolean isAdjusting ) {
			popAtomic();
		}
	};

	private final org.lgna.croquet.event.ValueListener<org.lgna.croquet.SimpleTabComposite<?>> tabListener = new org.lgna.croquet.event.ValueListener<org.lgna.croquet.SimpleTabComposite<?>>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.lgna.croquet.SimpleTabComposite<?>> e ) {
			updateCameraPointOfView();
			updateLastActiveOutfitTab();
		}
	};

	private static final javax.swing.Icon RANDOM_ICON = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( IngredientsComposite.class.getResource( "images/random.png" ) );

	private final java.util.List<org.alice.stageide.personresource.data.HairColorName> hairColorNames = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

	public IngredientsComposite() {
		super( java.util.UUID.fromString( "dd127381-09a8-4f78-bfd5-f3bffc1af98b" ) );
		this.randomize.setButtonIcon( RANDOM_ICON );
	}

	private void updateLastActiveOutfitTab() {
		org.lgna.croquet.SimpleTabComposite nextValue = this.bodyHeadHairTabState.getValue();
		if( nextValue == this.bodyTab ) {
			this.lastActiveOutfitTab = this.bodyTab;
			syncPersonImpAndMaps();
		}
		else if( nextValue == this.topAndBottomTab ) {
			this.lastActiveOutfitTab = this.topAndBottomTab;
			syncPersonImpAndMaps();
		}
	}

	private void updateCameraPointOfView() {
		org.lgna.croquet.SimpleTabComposite nextValue = this.bodyHeadHairTabState.getValue();
		org.alice.stageide.personresource.views.PersonViewer personViewer = PersonResourceComposite.getInstance().getPreviewComposite().getView();
		org.lgna.story.resources.sims2.LifeStage lifeStage = lifeStageState.getValue();
		if( lifeStage != null ) {
			//pass
		} else {
			lifeStage = org.lgna.story.resources.sims2.LifeStage.ADULT;
		}
		if( ( nextValue == this.bodyTab ) || ( nextValue == this.topAndBottomTab ) ) {
			personViewer.setCameraToFullView( lifeStage );
		} else {
			personViewer.setCameraToCloseUp( lifeStage );
		}
	}

	private void addHairColorNameToFront( org.alice.stageide.personresource.data.HairColorName hairColorName ) {
		synchronized( this.hairColorNames ) {
			int index = this.hairColorNames.indexOf( hairColorName );
			if( index != -1 ) {
				this.hairColorNames.remove( index );
			}
			this.hairColorNames.add( 0, hairColorName );
		}
	}

	public org.lgna.story.resources.sims2.Hair getHairForHairHatStyle( org.alice.stageide.personresource.data.HairHatStyle hairHatStyle ) {
		if( hairHatStyle != null ) {
			synchronized( this.hairColorNames ) {
				for( org.alice.stageide.personresource.data.HairColorName hairColorName : this.hairColorNames ) {
					org.lgna.story.resources.sims2.Hair rv = hairHatStyle.getHair( hairColorName );
					if( rv != null ) {
						return rv;
					}
				}
			}
			java.util.List<org.alice.stageide.personresource.data.HairColorNameHairCombo> hairColorNameHairCombos = hairHatStyle.getHairColorNameHairCombos();
			if( hairColorNameHairCombos.size() > 0 ) {
				org.alice.stageide.personresource.data.HairColorNameHairCombo hairColorNameHairCombo = hairColorNameHairCombos.get( 0 );
				if( hairColorNameHairCombo != null ) {
					return hairColorNameHairCombo.getHair();
				}
			}
		}
		return null;
	}

	@Override
	protected org.alice.stageide.personresource.views.IngredientsView createView() {
		return new org.alice.stageide.personresource.views.IngredientsView( this );
	}

	public org.lgna.croquet.Operation getRandomize() {
		return this.randomize;
	}

	public org.lgna.croquet.ImmutableDataSingleSelectListState<org.lgna.story.resources.sims2.LifeStage> getLifeStageState() {
		return this.lifeStageState;
	}

	public org.lgna.croquet.ImmutableDataSingleSelectListState<org.lgna.story.resources.sims2.Gender> getGenderState() {
		return this.genderState;
	}

	public org.lgna.croquet.ImmutableDataSingleSelectListState<org.lgna.story.resources.sims2.BaseFace> getBaseFaceState() {
		return this.faceTab.getBaseFaceState();
	}

	public SkinColorState getSkinColorState() {
		return this.skinColorState;
	}

	public org.lgna.croquet.RefreshableDataSingleSelectListState<org.alice.stageide.personresource.data.HairHatStyle> getHairHatStyleState() {
		return this.hairTab.getHairHatStyleState();
	}

	public org.lgna.croquet.RefreshableDataSingleSelectListState<org.alice.stageide.personresource.data.HairColorName> getHairColorNameState() {
		return this.hairTab.getHairColorNameState();
	}

	public org.lgna.croquet.ImmutableDataSingleSelectListState<org.lgna.story.resources.sims2.BaseEyeColor> getBaseEyeColorState() {
		return this.faceTab.getBaseEyeColorState();
	}

	public org.lgna.croquet.RefreshableDataSingleSelectListState<org.lgna.story.resources.sims2.FullBodyOutfit> getFullBodyOutfitState() {
		return this.bodyTab.getFullBodyOutfitState();
	}

	public org.lgna.croquet.RefreshableDataSingleSelectListState<org.lgna.story.resources.sims2.TopPiece> getTopPieceState() {
		return this.topAndBottomTab.getTopPieceState();
	}

	public org.lgna.croquet.RefreshableDataSingleSelectListState<org.lgna.story.resources.sims2.BottomPiece> getBottomPieceState() {
		return this.topAndBottomTab.getBottomPieceState();
	}

	public org.lgna.croquet.BoundedDoubleState getObesityLevelState() {
		return this.obesityLevelState;
	}

	public org.lgna.croquet.ImmutableDataTabState<org.lgna.croquet.SimpleTabComposite<?>> getBodyHeadHairTabState() {
		return this.bodyHeadHairTabState;
	}

	public FullBodyOutfitTabComposite getBodyTab() {
		return this.bodyTab;
	}

	public HairTabComposite getHairTab() {
		return this.hairTab;
	}

	public FaceTabComposite getFaceTab() {
		return this.faceTab;
	}

	private org.lgna.croquet.edits.AbstractEdit createRandomEdit( org.lgna.croquet.history.CompletionStep<?> step ) {
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
		this.getSkinColorState().addValueListener( this.skinColorListener );
		this.getBaseFaceState().addValueListener( this.faceListener );
		this.getBaseEyeColorState().addValueListener( this.baseEyeColorListener );
		this.getHairColorNameState().addValueListener( this.hairColorNameListener );
		this.getHairHatStyleState().addValueListener( this.hairListener );
		this.getFullBodyOutfitState().addValueListener( this.fullBodyOutfitListener );
		this.getTopPieceState().addValueListener( this.topPieceListener );
		this.getBottomPieceState().addValueListener( this.bottomPieceListener );
		this.getObesityLevelState().addValueListener( this.obesityLevelListener );
	}

	private void removeListeners() {
		this.getLifeStageState().removeValueListener( this.lifeStageListener );
		this.getGenderState().removeValueListener( this.genderListener );
		this.getSkinColorState().removeValueListener( this.skinColorListener );
		this.getBaseFaceState().removeValueListener( this.faceListener );
		this.getBaseEyeColorState().removeValueListener( this.baseEyeColorListener );
		this.getHairColorNameState().removeValueListener( this.hairColorNameListener );
		this.getHairHatStyleState().removeValueListener( this.hairListener );
		this.getFullBodyOutfitState().removeValueListener( this.fullBodyOutfitListener );
		this.getTopPieceState().removeValueListener( this.topPieceListener );
		this.getBottomPieceState().removeValueListener( this.bottomPieceListener );
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
		if( activeCount == 0 ) {
			this.addListeners();

			this.bodyHeadHairTabState.addAndInvokeNewSchoolValueListener( this.tabListener );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, this.activeCount );
		}
		this.activeCount++;
	}

	@Override
	public void handlePostDeactivation() {
		this.activeCount--;
		if( activeCount == 0 ) {
			this.bodyHeadHairTabState.removeNewSchoolValueListener( this.tabListener );
			this.removeListeners();
		}
		if( activeCount != 0 ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "todo" );
		}
		super.handlePostDeactivation();
	}

	private static org.lgna.story.resources.sims2.LifeStage getLifeStage( org.lgna.story.resources.sims2.PersonResource personResource ) {
		return personResource != null ? personResource.getLifeStage() : null;
	}

	private static org.lgna.story.resources.sims2.Gender getGender( org.lgna.story.resources.sims2.PersonResource personResource ) {
		return personResource != null ? personResource.getGender() : null;
	}

	private static org.lgna.story.resources.sims2.Hair getHair( org.lgna.story.resources.sims2.PersonResource personResource ) {
		return personResource != null ? personResource.getHair() : null;
	}

	private org.lgna.story.resources.sims2.PersonResource prevPersonResource;
	private int atomicCount = 0;

	private void syncPersonImpAndMaps() {
		PersonImp personImp = PersonResourceComposite.getInstance().getPreviewComposite().getView().getPerson();
		if( personImp != null ) {
			personImp.updateNebPerson();
		}

		this.prevPersonResource = this.createResourceFromStates();

		if( this.prevPersonResource != null ) {
			this.mapToMap.put( this.prevPersonResource.getLifeStage(), this.prevPersonResource.getGender(), this.prevPersonResource );
		}
	}

	//	private void updateHairColorName( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender, org.lgna.story.resources.sims2.Hair hair, String hairColorName ) {
	//		if( hairColorName != null ) {
	//			//pass
	//		} else {
	//			hairColorName = this.getHairColorNameState().getValue();
	//		}
	//		this.getHairColorNameState().setValueTransactionlessly( null );
	//		org.alice.stageide.personresource.data.HairColorNameListData data = this.hairTab.getHairColorNameData();
	//		data.setHairHatStyle( hairHatStyle );
	//		if( hairColorName != null ) {
	//			if( data.contains( hairColorName ) ) {
	//				//pass
	//			} else {
	//				hairColorName = null;
	//			}
	//		}
	//		if( hairColorName != null ) {
	//			//pass
	//		} else {
	//			if( hair != null ) {
	//				hairColorName = hair.toString();
	//			}
	//			else {
	//				org.lgna.story.resources.sims2.PersonResource personResource = this.mapToMap.get( lifeStage, gender );
	//				if( personResource != null ) {
	//					org.lgna.story.resources.sims2.Hair personHair = personResource.getHair();
	//					if( personHair != null ) {
	//						hairColorName = personHair.toString();
	//					}
	//				}
	//			}
	//		}
	//		if( hairColorName != null ) {
	//			this.getHairColorNameState().setValueTransactionlessly( hairColorName );
	//		} else {
	//			this.getHairColorNameState().setRandomSelectedValue();
	//		}
	//	}
	//
	//	private void updateHair( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender, org.lgna.story.resources.sims2.Hair hair ) {
	//		this.getHairHatStyleState().setValueTransactionlessly( null );
	//
	//		String hairColorName;
	//		if( hair != null ) {
	//			hairColorName = hair.toString();
	//		} else {
	//			hairColorName = this.getHairColorNameState().getValue();
	//			if( hairColorName != null ) {
	//				org.lgna.story.resources.sims2.PersonResource previousPersonResource = this.mapToMap.get( lifeStage, gender );
	//				if( previousPersonResource != null ) {
	//					org.lgna.story.resources.sims2.Hair previousHairValue = previousPersonResource.getHair();
	//					if( previousHairValue != null ) {
	//						Class<?> cls = previousHairValue.getClass();
	//						if( cls.isEnum() ) {
	//							try {
	//								hair = (org.lgna.story.resources.sims2.Hair)cls.getField( hairColorName ).get( null );
	//							} catch( Exception e ) {
	//								edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( e, previousHairValue );
	//								hair = null;
	//							}
	//						}
	//					}
	//				}
	//			}
	//		}
	//		this.hairTab.getHairHatStyleListData().setLifeStageAndGender( lifeStage, gender );
	//
	//		if( hair != null ) {
	//			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( hair );
	//			//this.getHairHatStyleState().setValueTransactionlessly( hair );
	//			this.getHairHatStyleState().setRandomSelectedValue();
	//		} else {
	//			this.getHairHatStyleState().setRandomSelectedValue();
	//		}
	//	}

	private org.lgna.story.resources.sims2.BaseSkinTone closestBaseSkinTone = null;
	private final boolean FORCE_GRAY_SKIN_TONE = true;

	private void handleSkinColorChange( java.awt.Color color ) {
		if( color != null ) {
			org.lgna.story.resources.sims2.BaseSkinTone nextBaseSkinTone = org.lgna.story.resources.sims2.BaseSkinTone.getClosestToColor( color );
			if( this.closestBaseSkinTone != nextBaseSkinTone ) {
				this.closestBaseSkinTone = nextBaseSkinTone;
				this.bodyHeadHairTabState.getValue().getView().repaint();
			}
		}
	}

	public org.lgna.story.resources.sims2.BaseSkinTone getClosestBaseSkinTone() {
		return this.closestBaseSkinTone;
	}

	private void updateHairHatStyleHairColorName( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender, org.alice.stageide.personresource.data.HairHatStyleHairColorName hairHatStyleHairColorName ) {
		if( hairHatStyleHairColorName != null ) {
			org.alice.stageide.personresource.data.HairHatStyle hairHatStyle = hairHatStyleHairColorName.getHairHatStyle();
			org.alice.stageide.personresource.data.HairColorName hairColorName = hairHatStyleHairColorName.getHairColorName();

			this.getHairTab().getHairHatStyleListData().setLifeStageAndGender( lifeStage, gender );
			this.getHairTab().getHairColorNameData().setHairHatStyle( hairHatStyle );
			this.getHairTab().getView().repaint();

			this.getHairHatStyleState().setValueTransactionlessly( hairHatStyle );
			this.getHairColorNameState().setValueTransactionlessly( hairColorName );
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe();
		}
	}

	private void updateFullBodyOutfit( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender, org.lgna.story.resources.sims2.FullBodyOutfit fullBodyOutfit ) {
		this.getFullBodyOutfitState().setValueTransactionlessly( null );
		this.bodyTab.getFullBodyOutfitData().setLifeStageAndGender( lifeStage, gender );
		if( fullBodyOutfit != null ) {
			this.getFullBodyOutfitState().setValueTransactionlessly( fullBodyOutfit );
		} else {
			this.getFullBodyOutfitState().setRandomSelectedValue();
		}
	}

	private void updateTopAndBottomOutfit( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender, org.lgna.story.resources.sims2.TopAndBottomOutfit<? extends org.lgna.story.resources.sims2.TopPiece, ? extends org.lgna.story.resources.sims2.BottomPiece> topAndBottomOutfit ) {
		this.getTopPieceState().setValueTransactionlessly( null );
		this.topAndBottomTab.getTopPieceData().setLifeStageAndGender( lifeStage, gender );
		this.getBottomPieceState().setValueTransactionlessly( null );
		this.topAndBottomTab.getBottomPieceData().setLifeStageAndGender( lifeStage, gender );

		//If there are no top or bottom pieces, leave the data set to null
		if( ( this.topAndBottomTab.getTopPieceData().getItemCount() == 0 ) || ( this.topAndBottomTab.getBottomPieceData().getItemCount() == 0 ) ) {
			return;
		}

		org.lgna.story.resources.sims2.TopPiece topPiece = null;
		org.lgna.story.resources.sims2.BottomPiece bottomPiece = null;
		if( topAndBottomOutfit != null ) {
			topPiece = topAndBottomOutfit.getTopPiece();
			bottomPiece = topAndBottomOutfit.getBottomPiece();
		}

		if( topPiece != null ) {
			this.getTopPieceState().setValueTransactionlessly( topPiece );
		}
		else {
			this.getTopPieceState().setRandomSelectedValue();
		}
		if( bottomPiece != null ) {
			this.getBottomPieceState().setValueTransactionlessly( bottomPiece );
		}
		else {
			this.getBottomPieceState().setRandomSelectedValue();
		}
	}

	private void updateOutfit( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender, org.lgna.story.resources.sims2.Outfit outfit ) {
		org.lgna.story.resources.sims2.TopAndBottomOutfit<? extends org.lgna.story.resources.sims2.TopPiece, ? extends org.lgna.story.resources.sims2.BottomPiece> topAndBottomOutfit = null;
		org.lgna.story.resources.sims2.FullBodyOutfit fullBodyOutfit = null;
		if( outfit != null ) {
			//pass
		} else {
			org.lgna.story.resources.sims2.PersonResource previousPersonResource = this.mapToMap.get( lifeStage, gender );
			if( previousPersonResource != null ) {
				outfit = previousPersonResource.getOutfit();
			}
		}
		if( outfit instanceof org.lgna.story.resources.sims2.FullBodyOutfit )
		{
			fullBodyOutfit = (org.lgna.story.resources.sims2.FullBodyOutfit)outfit;
		}
		else if( outfit instanceof org.lgna.story.resources.sims2.TopAndBottomOutfit<?, ?> )
		{
			topAndBottomOutfit = (org.lgna.story.resources.sims2.TopAndBottomOutfit<?, ?>)outfit;
		}
		updateFullBodyOutfit( lifeStage, gender, fullBodyOutfit );
		updateTopAndBottomOutfit( lifeStage, gender, topAndBottomOutfit );
	}

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

				org.lgna.story.resources.sims2.Gender nextGender = this.getGenderState().getValue();
				org.lgna.story.resources.sims2.Gender prevGender = getGender( this.prevPersonResource );
				boolean isGenderChanged = prevGender != nextGender;

				org.alice.stageide.personresource.data.HairHatStyle hairHatStyle = this.getHairHatStyleState().getValue();
				org.lgna.story.resources.sims2.Hair nextHair = this.getHairForHairHatStyle( hairHatStyle );

				org.lgna.story.resources.sims2.Hair prevHair = getHair( this.prevPersonResource );
				boolean isHairChanged = nextHair != prevHair;

				if( isLifeStageChanged || isGenderChanged ) {
					//Need a new hair style since we're on a new gender or lifestage
					//Pick a random one (we don't track previous values across lifestage or gender changes)
					this.hairTab.getHairHatStyleListData().setLifeStageAndGender( nextLifeStage, nextGender );
					this.hairTab.getHairHatStyleState().setRandomSelectedValue();
					//Given a new hair style, try to pick a color for it
					nextHair = this.getHairForHairHatStyle( this.hairTab.getHairHatStyleState().getValue() );
					org.alice.stageide.personresource.data.HairHatStyleHairColorName hairHatStyleHairColorName = org.alice.stageide.personresource.data.HairUtilities.getHairHatStyleColorNameFromHair( nextLifeStage, nextGender, nextHair );
					if( hairHatStyleHairColorName != null ) {
						this.updateHairHatStyleHairColorName( nextLifeStage, nextGender, hairHatStyleHairColorName );
					}
					else {
						this.hairTab.getHairColorNameState().setRandomSelectedValue();
					}
				} else {
					if( isHairChanged ) {
						org.alice.stageide.personresource.data.HairHatStyleHairColorName hairHatStyleHairColorName = org.alice.stageide.personresource.data.HairUtilities.getHairHatStyleColorNameFromHair( nextLifeStage, nextGender, nextHair );
						this.updateHairHatStyleHairColorName( nextLifeStage, nextGender, hairHatStyleHairColorName );
					}
				}
				//				if( isLifeStageChanged || isHairChanged ) {
				//					String[] nextColors = org.alice.stageide.personresource.data.HairColorNameListData.getHairColors( nextHair );
				//					String[] prevColors = org.alice.stageide.personresource.data.HairColorNameListData.getHairColors( prevHair );
				//
				//					if( !Arrays.equals( nextColors, prevColors ) ) {
				//						this.updateHairColorName( nextLifeStage, nextGender, nextHair, null );
				//					}
				//				}
				//
				//				String prevHairColorName = getHairColorName( this.prevPersonResource );
				//				final String nextHairColorName = this.getHairColorNameState().getValue();
				//				boolean isHairColorChanged = edu.cmu.cs.dennisc.java.util.Objects.notEquals( prevHairColorName, nextHairColorName );
				//
				//				if( isLifeStageChanged || isGenderChanged ) {
				//					this.updateHair( nextLifeStage, nextGender, null );
				//				} else if( isHairColorChanged ) {
				//					if( isHairChanged ) {
				//						this.updateHair( nextLifeStage, nextGender, nextHair );
				//					}
				//					else {
				//						this.updateHair( nextLifeStage, nextGender, null );
				//					}
				//				}
				if( isLifeStageChanged || isGenderChanged ) {
					this.updateOutfit( nextLifeStage, nextGender, null );
				}
			} finally {
				this.addListenersIfAppropriate();
			}
			this.syncPersonImpAndMaps();
		}
	}

	public org.lgna.story.resources.sims2.PersonResource createResourceFromStates() {
		org.lgna.story.resources.sims2.LifeStage lifeStage = this.getLifeStageState().getValue();
		org.lgna.story.resources.sims2.Gender gender = this.getGenderState().getValue();

		java.awt.Color awtSkinColor = this.getSkinColorState().getValue();
		this.handleSkinColorChange( awtSkinColor );
		org.lgna.story.Color skinColor = org.lgna.story.EmployeesOnly.createColor( awtSkinColor );

		org.lgna.story.resources.sims2.EyeColor eyeColor = this.getBaseEyeColorState().getValue();
		org.lgna.story.resources.sims2.Outfit outfit = null;
		boolean topsAndBottomsAvailable = ( this.topAndBottomTab.getBottomPieceData().getItemCount() > 0 ) && ( this.topAndBottomTab.getTopPieceData().getItemCount() > 0 );
		if( !topsAndBottomsAvailable || ( this.lastActiveOutfitTab == this.bodyTab ) ) {
			outfit = this.getFullBodyOutfitState().getValue();
		}
		else if( this.lastActiveOutfitTab == this.topAndBottomTab ) {
			if( lifeStage == org.lgna.story.resources.sims2.LifeStage.CHILD ) {
				if( gender == org.lgna.story.resources.sims2.Gender.MALE ) {
					outfit = new org.lgna.story.resources.sims2.MaleChildTopAndBottomOutfit( (org.lgna.story.resources.sims2.MaleChildTopPiece)this.getTopPieceState().getValue(), (org.lgna.story.resources.sims2.MaleChildBottomPiece)this.getBottomPieceState().getValue() );
				}
				else if( gender == org.lgna.story.resources.sims2.Gender.FEMALE ) {
					outfit = new org.lgna.story.resources.sims2.FemaleChildTopAndBottomOutfit( (org.lgna.story.resources.sims2.FemaleChildTopPiece)this.getTopPieceState().getValue(), (org.lgna.story.resources.sims2.FemaleChildBottomPiece)this.getBottomPieceState().getValue() );
				}
			}
			else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.TEEN ) {
				if( gender == org.lgna.story.resources.sims2.Gender.MALE ) {
					outfit = new org.lgna.story.resources.sims2.MaleTeenTopAndBottomOutfit( (org.lgna.story.resources.sims2.MaleTeenTopPiece)this.getTopPieceState().getValue(), (org.lgna.story.resources.sims2.MaleTeenBottomPiece)this.getBottomPieceState().getValue() );
				}
				else if( gender == org.lgna.story.resources.sims2.Gender.FEMALE ) {
					outfit = new org.lgna.story.resources.sims2.FemaleTeenTopAndBottomOutfit( (org.lgna.story.resources.sims2.FemaleTeenTopPiece)this.getTopPieceState().getValue(), (org.lgna.story.resources.sims2.FemaleTeenBottomPiece)this.getBottomPieceState().getValue() );
				}
			}
			else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.ADULT ) {
				if( gender == org.lgna.story.resources.sims2.Gender.MALE ) {
					outfit = new org.lgna.story.resources.sims2.MaleAdultTopAndBottomOutfit( (org.lgna.story.resources.sims2.MaleAdultTopPiece)this.getTopPieceState().getValue(), (org.lgna.story.resources.sims2.MaleAdultBottomPiece)this.getBottomPieceState().getValue() );
				}
				else if( gender == org.lgna.story.resources.sims2.Gender.FEMALE ) {
					outfit = new org.lgna.story.resources.sims2.FemaleAdultTopAndBottomOutfit( (org.lgna.story.resources.sims2.FemaleAdultTopPiece)this.getTopPieceState().getValue(), (org.lgna.story.resources.sims2.FemaleAdultBottomPiece)this.getBottomPieceState().getValue() );
				}
			}
			else if( lifeStage == org.lgna.story.resources.sims2.LifeStage.ELDER ) {
				if( gender == org.lgna.story.resources.sims2.Gender.MALE ) {
					outfit = new org.lgna.story.resources.sims2.MaleElderTopAndBottomOutfit( (org.lgna.story.resources.sims2.MaleElderTopPiece)this.getTopPieceState().getValue(), (org.lgna.story.resources.sims2.MaleElderBottomPiece)this.getBottomPieceState().getValue() );
				}
				else if( gender == org.lgna.story.resources.sims2.Gender.FEMALE ) {
					outfit = new org.lgna.story.resources.sims2.FemaleElderTopAndBottomOutfit( (org.lgna.story.resources.sims2.FemaleElderTopPiece)this.getTopPieceState().getValue(), (org.lgna.story.resources.sims2.FemaleElderBottomPiece)this.getBottomPieceState().getValue() );
				}
			}
		}
		else {
			outfit = this.getFullBodyOutfitState().getValue();
		}
		org.alice.stageide.personresource.data.HairHatStyle hairHatStyle = this.getHairHatStyleState().getValue();
		org.alice.stageide.personresource.data.HairColorName hairColorName = this.getHairColorNameState().getValue();
		org.lgna.story.resources.sims2.Hair hair = hairHatStyle != null ? hairHatStyle.getHair( hairColorName ) : null;
		org.lgna.story.resources.sims2.BaseFace face = this.getBaseFaceState().getValue();
		double obesityLevel = this.getObesityLevelState().getValue();
		if( lifeStage != null ) {
			return lifeStage.createResource( gender, skinColor, eyeColor, hair, obesityLevel, outfit, face );
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
			this.getSkinColorState().setValueTransactionlessly( org.lgna.story.EmployeesOnly.getAwtColor( personResource.getSkinColor() ) );

			org.lgna.story.resources.sims2.Hair hair = personResource.getHair();
			org.alice.stageide.personresource.data.HairHatStyleHairColorName hairHatStyleHairColorName = org.alice.stageide.personresource.data.HairUtilities.getHairHatStyleColorNameFromHair( personResource.getLifeStage(), personResource.getGender(), hair );

			this.addHairColorNameToFront( hairHatStyleHairColorName.getHairColorName() );

			this.updateOutfit( personResource.getLifeStage(), personResource.getGender(), personResource.getOutfit() );
			//this.updateHairColorName( personResource.getLifeStage(), personResource.getGender(), hair, hair != null ? hair.toString() : null );
			//this.updateHair( personResource.getLifeStage(), personResource.getGender(), hair );
			this.updateHairHatStyleHairColorName( personResource.getLifeStage(), personResource.getGender(), hairHatStyleHairColorName );
			this.getObesityLevelState().setValueTransactionlessly( personResource.getObesityLevel() );
			this.getBaseFaceState().setValueTransactionlessly( (org.lgna.story.resources.sims2.BaseFace)personResource.getFace() );
		} finally {
			this.addListenersIfAppropriate();
		}
		this.syncPersonImpAndMaps();
	}
}
