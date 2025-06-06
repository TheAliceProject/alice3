/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.stageide.personresource;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.IconUtilities;
import edu.cmu.cs.dennisc.map.MapToMap;
import org.alice.stageide.personresource.data.HairColorName;
import org.alice.stageide.personresource.data.HairColorNameHairCombo;
import org.alice.stageide.personresource.data.HairHatStyle;
import org.alice.stageide.personresource.data.HairHatStyleHairColorName;
import org.alice.stageide.personresource.data.HairUtilities;
import org.alice.stageide.personresource.edits.SetPersonResourceEdit;
import org.alice.stageide.personresource.views.IngredientsView;
import org.alice.stageide.personresource.views.OutfitTabView;
import org.alice.stageide.personresource.views.PersonViewer;
import org.lgna.croquet.BoundedDoubleState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.ImmutableDataSingleSelectListState;
import org.lgna.croquet.ImmutableDataTabState;
import org.lgna.croquet.Operation;
import org.lgna.croquet.RefreshableDataSingleSelectListState;
import org.lgna.croquet.SimpleComposite;
import org.lgna.croquet.SimpleTabComposite;
import org.lgna.croquet.State;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.history.UserActivity;
import org.lgna.story.Color;
import org.lgna.story.resources.sims2.BaseEyeColor;
import org.lgna.story.resources.sims2.BaseFace;
import org.lgna.story.resources.sims2.BaseSkinTone;
import org.lgna.story.resources.sims2.BottomPiece;
import org.lgna.story.resources.sims2.EyeColor;
import org.lgna.story.resources.sims2.FemaleAdultBottomPiece;
import org.lgna.story.resources.sims2.FemaleAdultTopAndBottomOutfit;
import org.lgna.story.resources.sims2.FemaleAdultTopPiece;
import org.lgna.story.resources.sims2.FemaleChildBottomPiece;
import org.lgna.story.resources.sims2.FemaleChildTopAndBottomOutfit;
import org.lgna.story.resources.sims2.FemaleChildTopPiece;
import org.lgna.story.resources.sims2.FemaleElderBottomPiece;
import org.lgna.story.resources.sims2.FemaleElderTopAndBottomOutfit;
import org.lgna.story.resources.sims2.FemaleElderTopPiece;
import org.lgna.story.resources.sims2.FemaleTeenBottomPiece;
import org.lgna.story.resources.sims2.FemaleTeenTopAndBottomOutfit;
import org.lgna.story.resources.sims2.FemaleTeenTopPiece;
import org.lgna.story.resources.sims2.FullBodyOutfit;
import org.lgna.story.resources.sims2.Gender;
import org.lgna.story.resources.sims2.Hair;
import org.lgna.story.resources.sims2.LifeStage;
import org.lgna.story.resources.sims2.MaleAdultBottomPiece;
import org.lgna.story.resources.sims2.MaleAdultTopAndBottomOutfit;
import org.lgna.story.resources.sims2.MaleAdultTopPiece;
import org.lgna.story.resources.sims2.MaleChildBottomPiece;
import org.lgna.story.resources.sims2.MaleChildTopAndBottomOutfit;
import org.lgna.story.resources.sims2.MaleChildTopPiece;
import org.lgna.story.resources.sims2.MaleElderBottomPiece;
import org.lgna.story.resources.sims2.MaleElderTopAndBottomOutfit;
import org.lgna.story.resources.sims2.MaleElderTopPiece;
import org.lgna.story.resources.sims2.MaleTeenBottomPiece;
import org.lgna.story.resources.sims2.MaleTeenTopAndBottomOutfit;
import org.lgna.story.resources.sims2.MaleTeenTopPiece;
import org.lgna.story.resources.sims2.Outfit;
import org.lgna.story.resources.sims2.PersonResource;
import org.lgna.story.resources.sims2.TopAndBottomOutfit;
import org.lgna.story.resources.sims2.TopPiece;

import javax.swing.Icon;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class IngredientsComposite extends SimpleComposite<IngredientsView> {
  private final Operation randomize = this.createActionOperation("randomize", new Action() {
    @Override
    public Edit perform(UserActivity userActivity, InternalActionOperation source) throws CancelException {
      return createRandomEdit(userActivity);
    }
  });

  private final BoundedDoubleState obesityLevelState = this.createBoundedDoubleState("obesityLevelState", new BoundedDoubleDetails());
  private final FullBodyOutfitTabComposite bodyTab = new FullBodyOutfitTabComposite();
  private final TopAndBottomOutfitTabComposite topAndBottomTab = new TopAndBottomOutfitTabComposite();
  private final HairTabComposite hairTab = new HairTabComposite();
  private final FaceTabComposite faceTab = new FaceTabComposite();
  private final ImmutableDataSingleSelectListState<LifeStage> lifeStageState = this.createImmutableListStateForEnum("lifeStageState", LifeStage.class, LifeStage.getRandom());
  private final ImmutableDataSingleSelectListState<Gender> genderState = this.createImmutableListStateForEnum("genderState", Gender.class, Gender.getRandom());
  private final SkinColorState skinColorState = new SkinColorState();
  private final ImmutableDataTabState<SimpleTabComposite<?>> bodyHeadHairTabState = this.createImmutableTabState("bodyHeadHairTabState", 0, this.bodyTab, this.topAndBottomTab, null, this.hairTab, null, this.faceTab);

  private OutfitTabComposite<? extends OutfitTabView> lastActiveOutfitTab = null;

  private final MapToMap<LifeStage, Gender, PersonResource> mapToMap = MapToMap.newInstance();
  private final State.ValueListener<LifeStage> lifeStageListener = new State.ValueListener<LifeStage>() {
    @Override
    public void changing(State<LifeStage> state, LifeStage prevValue, LifeStage nextValue) {
      pushAtomic();
    }

    @Override
    public void changed(State<LifeStage> state, LifeStage prevValue, LifeStage nextValue) {
      popAtomic();
      updateCameraPointOfView();
    }
  };
  private final State.ValueListener<Gender> genderListener = new State.ValueListener<Gender>() {
    @Override
    public void changing(State<Gender> state, Gender prevValue, Gender nextValue) {
      pushAtomic();
    }

    @Override
    public void changed(State<Gender> state, Gender prevValue, Gender nextValue) {
      popAtomic();
    }
  };
  private final State.ValueListener<java.awt.Color> skinColorListener = new State.ValueListener<java.awt.Color>() {
    @Override
    public void changing(State<java.awt.Color> state, java.awt.Color prevValue, java.awt.Color nextValue) {
      pushAtomic();
    }

    @Override
    public void changed(State<java.awt.Color> state, java.awt.Color prevValue, java.awt.Color nextValue) {
      popAtomic();
      handleSkinColorChange(nextValue);
    }
  };
  private final State.ValueListener<BaseFace> faceListener = new State.ValueListener<BaseFace>() {
    @Override
    public void changing(State<BaseFace> state, BaseFace prevValue, BaseFace nextValue) {
      pushAtomic();
    }

    @Override
    public void changed(State<BaseFace> state, BaseFace prevValue, BaseFace nextValue) {
      popAtomic();
    }
  };
  private final State.ValueListener<BaseEyeColor> baseEyeColorListener = new State.ValueListener<BaseEyeColor>() {
    @Override
    public void changing(State<BaseEyeColor> state, BaseEyeColor prevValue, BaseEyeColor nextValue) {
      pushAtomic();
    }

    @Override
    public void changed(State<BaseEyeColor> state, BaseEyeColor prevValue, BaseEyeColor nextValue) {
      popAtomic();
    }
  };
  private final State.ValueListener<HairColorName> hairColorNameListener = new State.ValueListener<HairColorName>() {
    @Override
    public void changing(State<HairColorName> state, HairColorName prevValue, HairColorName nextValue) {
      pushAtomic();
    }

    @Override
    public void changed(State<HairColorName> state, HairColorName prevValue, HairColorName nextValue) {
      addHairColorNameToFront(nextValue);
      popAtomic();
    }
  };
  private final State.ValueListener<HairHatStyle> hairListener = new State.ValueListener<HairHatStyle>() {
    @Override
    public void changing(State<HairHatStyle> state, HairHatStyle prevValue, HairHatStyle nextValue) {
      pushAtomic();
    }

    @Override
    public void changed(State<HairHatStyle> state, HairHatStyle prevValue, HairHatStyle nextValue) {
      popAtomic();
    }
  };
  private final State.ValueListener<FullBodyOutfit> fullBodyOutfitListener = new State.ValueListener<FullBodyOutfit>() {
    @Override
    public void changing(State<FullBodyOutfit> state, FullBodyOutfit prevValue, FullBodyOutfit nextValue) {
      pushAtomic();
    }

    @Override
    public void changed(State<FullBodyOutfit> state, FullBodyOutfit prevValue, FullBodyOutfit nextValue) {
      popAtomic();
    }
  };
  private final State.ValueListener<TopPiece> topPieceListener = new State.ValueListener<TopPiece>() {
    @Override
    public void changing(State<TopPiece> state, TopPiece prevValue, TopPiece nextValue) {
      pushAtomic();
    }

    @Override
    public void changed(State<TopPiece> state, TopPiece prevValue, TopPiece nextValue) {
      popAtomic();
    }
  };
  private final State.ValueListener<BottomPiece> bottomPieceListener = new State.ValueListener<BottomPiece>() {
    @Override
    public void changing(State<BottomPiece> state, BottomPiece prevValue, BottomPiece nextValue) {
      pushAtomic();
    }

    @Override
    public void changed(State<BottomPiece> state, BottomPiece prevValue, BottomPiece nextValue) {
      popAtomic();
    }
  };
  private final State.ValueListener<Double> obesityLevelListener = new State.ValueListener<Double>() {
    @Override
    public void changing(State<Double> state, Double prevValue, Double nextValue) {
      pushAtomic();
    }

    @Override
    public void changed(State<Double> state, Double prevValue, Double nextValue) {
      popAtomic();
    }
  };

  private final ValueListener<SimpleTabComposite<?>> tabListener = new ValueListener<SimpleTabComposite<?>>() {
    @Override
    public void valueChanged(ValueEvent<SimpleTabComposite<?>> e) {
      updateCameraPointOfView();
      updateLastActiveOutfitTab();
    }
  };

  private static final Icon RANDOM_ICON = IconUtilities.createImageIcon(IngredientsComposite.class.getResource("images/random.png"));

  private final List<HairColorName> hairColorNames = Lists.newLinkedList();

  public IngredientsComposite() {
    super(UUID.fromString("dd127381-09a8-4f78-bfd5-f3bffc1af98b"));
    this.randomize.setButtonIcon(RANDOM_ICON);
  }

  private void updateLastActiveOutfitTab() {
    SimpleTabComposite nextValue = this.bodyHeadHairTabState.getValue();
    if (nextValue == this.bodyTab) {
      this.lastActiveOutfitTab = this.bodyTab;
      syncPersonImpAndMaps();
    } else if (nextValue == this.topAndBottomTab) {
      this.lastActiveOutfitTab = this.topAndBottomTab;
      syncPersonImpAndMaps();
    }
  }

  private void updateCameraPointOfView() {
    SimpleTabComposite nextValue = this.bodyHeadHairTabState.getValue();
    PersonViewer personViewer = PersonResourceComposite.getInstance().getPreviewComposite().getView();
    LifeStage lifeStage = lifeStageState.getValue();
    if (lifeStage == null) {
      lifeStage = LifeStage.ADULT;
    }
    if ((nextValue == this.bodyTab) || (nextValue == this.topAndBottomTab)) {
      personViewer.setCameraToFullView(lifeStage);
    } else {
      personViewer.setCameraToCloseUp(lifeStage);
    }
  }

  private void addHairColorNameToFront(HairColorName hairColorName) {
    synchronized (this.hairColorNames) {
      int index = this.hairColorNames.indexOf(hairColorName);
      if (index != -1) {
        this.hairColorNames.remove(index);
      }
      this.hairColorNames.add(0, hairColorName);
    }
  }

  public Hair getHairForHairHatStyle(HairHatStyle hairHatStyle) {
    if (hairHatStyle != null) {
      synchronized (this.hairColorNames) {
        for (HairColorName hairColorName : this.hairColorNames) {
          Hair rv = hairHatStyle.getHair(hairColorName);
          if (rv != null) {
            return rv;
          }
        }
      }
      List<HairColorNameHairCombo> hairColorNameHairCombos = hairHatStyle.getHairColorNameHairCombos();
      if (hairColorNameHairCombos.size() > 0) {
        HairColorNameHairCombo hairColorNameHairCombo = hairColorNameHairCombos.get(0);
        if (hairColorNameHairCombo != null) {
          return hairColorNameHairCombo.getHair();
        }
      }
    }
    return null;
  }

  @Override
  protected IngredientsView createView() {
    return new IngredientsView(this);
  }

  public Operation getRandomize() {
    return this.randomize;
  }

  public ImmutableDataSingleSelectListState<LifeStage> getLifeStageState() {
    return this.lifeStageState;
  }

  public ImmutableDataSingleSelectListState<Gender> getGenderState() {
    return this.genderState;
  }

  public ImmutableDataSingleSelectListState<BaseFace> getBaseFaceState() {
    return this.faceTab.getBaseFaceState();
  }

  public SkinColorState getSkinColorState() {
    return this.skinColorState;
  }

  public RefreshableDataSingleSelectListState<HairHatStyle> getHairHatStyleState() {
    return this.hairTab.getHairHatStyleState();
  }

  public RefreshableDataSingleSelectListState<HairColorName> getHairColorNameState() {
    return this.hairTab.getHairColorNameState();
  }

  public ImmutableDataSingleSelectListState<BaseEyeColor> getBaseEyeColorState() {
    return this.faceTab.getBaseEyeColorState();
  }

  public RefreshableDataSingleSelectListState<FullBodyOutfit> getFullBodyOutfitState() {
    return this.bodyTab.getFullBodyOutfitState();
  }

  public RefreshableDataSingleSelectListState<TopPiece> getTopPieceState() {
    return this.topAndBottomTab.getTopPieceState();
  }

  public RefreshableDataSingleSelectListState<BottomPiece> getBottomPieceState() {
    return this.topAndBottomTab.getBottomPieceState();
  }

  public BoundedDoubleState getObesityLevelState() {
    return this.obesityLevelState;
  }

  public ImmutableDataTabState<SimpleTabComposite<?>> getBodyHeadHairTabState() {
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

  private Edit createRandomEdit(UserActivity userActivity) {
    LifeStage lifeStage;
    if (this.lifeStageState.isEnabled()) {
      lifeStage = null;
    } else {
      lifeStage = this.lifeStageState.getValue();
    }
    PersonResource nextPersonResource = RandomPersonUtilities.createRandomResource(lifeStage);
    return new SetPersonResourceEdit(userActivity, nextPersonResource);
  }

  private void addListeners() {
    this.getLifeStageState().addValueListener(this.lifeStageListener);
    this.getGenderState().addValueListener(this.genderListener);
    this.getSkinColorState().addValueListener(this.skinColorListener);
    this.getBaseFaceState().addValueListener(this.faceListener);
    this.getBaseEyeColorState().addValueListener(this.baseEyeColorListener);
    this.getHairColorNameState().addValueListener(this.hairColorNameListener);
    this.getHairHatStyleState().addValueListener(this.hairListener);
    this.getFullBodyOutfitState().addValueListener(this.fullBodyOutfitListener);
    this.getTopPieceState().addValueListener(this.topPieceListener);
    this.getBottomPieceState().addValueListener(this.bottomPieceListener);
    this.getObesityLevelState().addValueListener(this.obesityLevelListener);
  }

  private void removeListeners() {
    this.getLifeStageState().removeValueListener(this.lifeStageListener);
    this.getGenderState().removeValueListener(this.genderListener);
    this.getSkinColorState().removeValueListener(this.skinColorListener);
    this.getBaseFaceState().removeValueListener(this.faceListener);
    this.getBaseEyeColorState().removeValueListener(this.baseEyeColorListener);
    this.getHairColorNameState().removeValueListener(this.hairColorNameListener);
    this.getHairHatStyleState().removeValueListener(this.hairListener);
    this.getFullBodyOutfitState().removeValueListener(this.fullBodyOutfitListener);
    this.getTopPieceState().removeValueListener(this.topPieceListener);
    this.getBottomPieceState().removeValueListener(this.bottomPieceListener);
    this.getObesityLevelState().removeValueListener(this.obesityLevelListener);
  }

  private int activeCount = 0;

  private void addListenersIfAppropriate() {
    if (activeCount > 0) {
      this.addListeners();
    }
  }

  private void removeListenersIfAppropriate() {
    if (activeCount > 0) {
      this.removeListeners();
    }
  }

  @Override
  public void handlePreActivation() {
    super.handlePreActivation();
    if (activeCount == 0) {
      this.addListeners();

      this.bodyHeadHairTabState.addAndInvokeNewSchoolValueListener(this.tabListener);
    } else {
      Logger.severe(this, this.activeCount);
    }
    this.activeCount++;
  }

  @Override
  public void handlePostDeactivation() {
    this.activeCount--;
    if (activeCount == 0) {
      this.bodyHeadHairTabState.removeNewSchoolValueListener(this.tabListener);
      this.removeListeners();
    }
    if (activeCount != 0) {
      Logger.severe("todo");
    }
    super.handlePostDeactivation();
  }

  private static LifeStage getLifeStage(PersonResource personResource) {
    return personResource != null ? personResource.getLifeStage() : null;
  }

  private static Gender getGender(PersonResource personResource) {
    return personResource != null ? personResource.getGender() : null;
  }

  private static Hair getHair(PersonResource personResource) {
    return personResource != null ? personResource.getHair() : null;
  }

  private PersonResource prevPersonResource;
  private int atomicCount = 0;

  private void syncPersonImpAndMaps() {
    PersonImp personImp = PersonResourceComposite.getInstance().getPreviewComposite().getView().getPerson();
    if (personImp != null) {
      personImp.updateNebPerson();
    }

    this.prevPersonResource = this.createResourceFromStates();

    if (this.prevPersonResource != null) {
      this.mapToMap.put(this.prevPersonResource.getLifeStage(), this.prevPersonResource.getGender(), this.prevPersonResource);
    }
  }

  //  private void updateHairColorName( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender, org.lgna.story.resources.sims2.Hair hair, String hairColorName ) {
  //    if( hairColorName != null ) {
  //      //pass
  //    } else {
  //      hairColorName = this.getHairColorNameState().getValue();
  //    }
  //    this.getHairColorNameState().setValueTransactionlessly( null );
  //    org.alice.stageide.personresource.data.HairColorNameListData data = this.hairTab.getHairColorNameData();
  //    data.setHairHatStyle( hairHatStyle );
  //    if( hairColorName != null ) {
  //      if( data.contains( hairColorName ) ) {
  //        //pass
  //      } else {
  //        hairColorName = null;
  //      }
  //    }
  //    if( hairColorName != null ) {
  //      //pass
  //    } else {
  //      if( hair != null ) {
  //        hairColorName = hair.toString();
  //      }
  //      else {
  //        org.lgna.story.resources.sims2.PersonResource personResource = this.mapToMap.get( lifeStage, gender );
  //        if( personResource != null ) {
  //          org.lgna.story.resources.sims2.Hair personHair = personResource.getHair();
  //          if( personHair != null ) {
  //            hairColorName = personHair.toString();
  //          }
  //        }
  //      }
  //    }
  //    if( hairColorName != null ) {
  //      this.getHairColorNameState().setValueTransactionlessly( hairColorName );
  //    } else {
  //      this.getHairColorNameState().setRandomSelectedValue();
  //    }
  //  }
  //
  //  private void updateHair( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender, org.lgna.story.resources.sims2.Hair hair ) {
  //    this.getHairHatStyleState().setValueTransactionlessly( null );
  //
  //    String hairColorName;
  //    if( hair != null ) {
  //      hairColorName = hair.toString();
  //    } else {
  //      hairColorName = this.getHairColorNameState().getValue();
  //      if( hairColorName != null ) {
  //        org.lgna.story.resources.sims2.PersonResource previousPersonResource = this.mapToMap.get( lifeStage, gender );
  //        if( previousPersonResource != null ) {
  //          org.lgna.story.resources.sims2.Hair previousHairValue = previousPersonResource.getHair();
  //          if( previousHairValue != null ) {
  //            Class<?> cls = previousHairValue.getClass();
  //            if( cls.isEnum() ) {
  //              try {
  //                hair = (org.lgna.story.resources.sims2.Hair)cls.getField( hairColorName ).get( null );
  //              } catch( Exception e ) {
  //                edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( e, previousHairValue );
  //                hair = null;
  //              }
  //            }
  //          }
  //        }
  //      }
  //    }
  //    this.hairTab.getHairHatStyleListData().setLifeStageAndGender( lifeStage, gender );
  //
  //    if( hair != null ) {
  //      edu.cmu.cs.dennisc.java.util.logging.Logger.severe( hair );
  //      //this.getHairHatStyleState().setValueTransactionlessly( hair );
  //      this.getHairHatStyleState().setRandomSelectedValue();
  //    } else {
  //      this.getHairHatStyleState().setRandomSelectedValue();
  //    }
  //  }

  private BaseSkinTone closestBaseSkinTone = null;
  private final boolean FORCE_GRAY_SKIN_TONE = true;

  private void handleSkinColorChange(java.awt.Color color) {
    if (color != null) {
      BaseSkinTone nextBaseSkinTone = BaseSkinTone.getClosestToColor(color);
      if (this.closestBaseSkinTone != nextBaseSkinTone) {
        this.closestBaseSkinTone = nextBaseSkinTone;
        this.bodyHeadHairTabState.getValue().getView().repaint();
      }
    }
  }

  public BaseSkinTone getClosestBaseSkinTone() {
    return this.closestBaseSkinTone;
  }

  private void updateHairHatStyleHairColorName(LifeStage lifeStage, Gender gender, HairHatStyleHairColorName hairHatStyleHairColorName) {
    if (hairHatStyleHairColorName != null) {
      HairHatStyle hairHatStyle = hairHatStyleHairColorName.getHairHatStyle();
      HairColorName hairColorName = hairHatStyleHairColorName.getHairColorName();

      this.getHairTab().getHairHatStyleListData().setLifeStageAndGender(lifeStage, gender);
      this.getHairTab().getHairColorNameData().setHairHatStyle(hairHatStyle);
      this.getHairTab().getView().repaint();

      this.getHairHatStyleState().setValueTransactionlessly(hairHatStyle);
      this.getHairColorNameState().setValueTransactionlessly(hairColorName);
    } else {
      Logger.severe();
    }
  }

  private void updateFullBodyOutfit(LifeStage lifeStage, Gender gender, FullBodyOutfit fullBodyOutfit) {
    this.getFullBodyOutfitState().setValueTransactionlessly(null);
    this.bodyTab.getFullBodyOutfitData().setLifeStageAndGender(lifeStage, gender);
    if (fullBodyOutfit != null) {
      this.getFullBodyOutfitState().setValueTransactionlessly(fullBodyOutfit);
    } else {
      this.getFullBodyOutfitState().setRandomSelectedValue();
    }
  }

  private void updateTopAndBottomOutfit(LifeStage lifeStage, Gender gender, TopAndBottomOutfit<? extends TopPiece, ? extends BottomPiece> topAndBottomOutfit) {
    this.getTopPieceState().setValueTransactionlessly(null);
    this.topAndBottomTab.getTopPieceData().setLifeStageAndGender(lifeStage, gender);
    this.getBottomPieceState().setValueTransactionlessly(null);
    this.topAndBottomTab.getBottomPieceData().setLifeStageAndGender(lifeStage, gender);

    //If there are no top or bottom pieces, leave the data set to null
    if ((this.topAndBottomTab.getTopPieceData().getItemCount() == 0) || (this.topAndBottomTab.getBottomPieceData().getItemCount() == 0)) {
      return;
    }

    TopPiece topPiece = null;
    BottomPiece bottomPiece = null;
    if (topAndBottomOutfit != null) {
      topPiece = topAndBottomOutfit.getTopPiece();
      bottomPiece = topAndBottomOutfit.getBottomPiece();
    }

    if (topPiece != null) {
      this.getTopPieceState().setValueTransactionlessly(topPiece);
    } else {
      this.getTopPieceState().setRandomSelectedValue();
    }
    if (bottomPiece != null) {
      this.getBottomPieceState().setValueTransactionlessly(bottomPiece);
    } else {
      this.getBottomPieceState().setRandomSelectedValue();
    }
  }

  private void updateOutfit(LifeStage lifeStage, Gender gender, Outfit outfit) {
    TopAndBottomOutfit<? extends TopPiece, ? extends BottomPiece> topAndBottomOutfit = null;
    FullBodyOutfit fullBodyOutfit = null;
    if (outfit == null) {
      PersonResource previousPersonResource = this.mapToMap.get(lifeStage, gender);
      if (previousPersonResource != null) {
        outfit = previousPersonResource.getOutfit();
      }
    }
    if (outfit instanceof FullBodyOutfit) {
      fullBodyOutfit = (FullBodyOutfit) outfit;
    } else if (outfit instanceof TopAndBottomOutfit<?, ?>) {
      topAndBottomOutfit = (TopAndBottomOutfit<?, ?>) outfit;
    }
    updateFullBodyOutfit(lifeStage, gender, fullBodyOutfit);
    updateTopAndBottomOutfit(lifeStage, gender, topAndBottomOutfit);
  }

  public void pushAtomic() {
    if (this.atomicCount == 0) {
    }
    this.atomicCount++;
  }

  public void popAtomic() {
    this.atomicCount--;
    if (this.atomicCount == 0) {
      this.removeListenersIfAppropriate();
      try {
        LifeStage prevLifeStage = getLifeStage(this.prevPersonResource);
        LifeStage nextLifeStage = this.getLifeStageState().getValue();
        boolean isLifeStageChanged = prevLifeStage != nextLifeStage;

        Gender nextGender = this.getGenderState().getValue();
        Gender prevGender = getGender(this.prevPersonResource);
        boolean isGenderChanged = prevGender != nextGender;

        HairHatStyle hairHatStyle = this.getHairHatStyleState().getValue();
        Hair nextHair = this.getHairForHairHatStyle(hairHatStyle);

        Hair prevHair = getHair(this.prevPersonResource);
        boolean isHairChanged = nextHair != prevHair;

        if (isLifeStageChanged || isGenderChanged) {
          //Need a new hair style since we're on a new gender or lifestage
          //Pick a random one (we don't track previous values across lifestage or gender changes)
          this.hairTab.getHairHatStyleListData().setLifeStageAndGender(nextLifeStage, nextGender);
          this.hairTab.getHairHatStyleState().setRandomSelectedValue();
          //Given a new hair style, try to pick a color for it
          nextHair = this.getHairForHairHatStyle(this.hairTab.getHairHatStyleState().getValue());
          HairHatStyleHairColorName hairHatStyleHairColorName = HairUtilities.getHairHatStyleColorNameFromHair(nextLifeStage, nextGender, nextHair);
          if (hairHatStyleHairColorName != null) {
            this.updateHairHatStyleHairColorName(nextLifeStage, nextGender, hairHatStyleHairColorName);
          } else {
            this.hairTab.getHairColorNameState().setRandomSelectedValue();
          }
        } else {
          if (isHairChanged) {
            HairHatStyleHairColorName hairHatStyleHairColorName = HairUtilities.getHairHatStyleColorNameFromHair(nextLifeStage, nextGender, nextHair);
            this.updateHairHatStyleHairColorName(nextLifeStage, nextGender, hairHatStyleHairColorName);
          }
        }
        //        if( isLifeStageChanged || isHairChanged ) {
        //          String[] nextColors = org.alice.stageide.personresource.data.HairColorNameListData.getHairColors( nextHair );
        //          String[] prevColors = org.alice.stageide.personresource.data.HairColorNameListData.getHairColors( prevHair );
        //
        //          if( !Arrays.equals( nextColors, prevColors ) ) {
        //            this.updateHairColorName( nextLifeStage, nextGender, nextHair, null );
        //          }
        //        }
        //
        //        String prevHairColorName = getHairColorName( this.prevPersonResource );
        //        final String nextHairColorName = this.getHairColorNameState().getValue();
        //        boolean isHairColorChanged = edu.cmu.cs.dennisc.java.util.Objects.notEquals( prevHairColorName, nextHairColorName );
        //
        //        if( isLifeStageChanged || isGenderChanged ) {
        //          this.updateHair( nextLifeStage, nextGender, null );
        //        } else if( isHairColorChanged ) {
        //          if( isHairChanged ) {
        //            this.updateHair( nextLifeStage, nextGender, nextHair );
        //          }
        //          else {
        //            this.updateHair( nextLifeStage, nextGender, null );
        //          }
        //        }
        if (isLifeStageChanged || isGenderChanged) {
          this.updateOutfit(nextLifeStage, nextGender, null);
        }
      } finally {
        this.addListenersIfAppropriate();
      }
      this.syncPersonImpAndMaps();
    }
  }

  public PersonResource createResourceFromStates() {
    LifeStage lifeStage = this.getLifeStageState().getValue();
    Gender gender = this.getGenderState().getValue();

    java.awt.Color awtSkinColor = this.getSkinColorState().getValue();
    this.handleSkinColorChange(awtSkinColor);
    Color skinColor = Color.createInstance(awtSkinColor);

    EyeColor eyeColor = this.getBaseEyeColorState().getValue();
    Outfit outfit = getOutfit(lifeStage, gender);
    HairHatStyle hairHatStyle = this.getHairHatStyleState().getValue();
    HairColorName hairColorName = this.getHairColorNameState().getValue();
    Hair hair = hairHatStyle != null ? hairHatStyle.getHair(hairColorName) : null;
    BaseFace face = this.getBaseFaceState().getValue();
    double obesityLevel = this.getObesityLevelState().getValue();
    if (lifeStage != null) {
      return lifeStage.createResource(gender, skinColor, eyeColor, hair, obesityLevel, outfit, face);
    } else {
      return null;
    }
  }

  private Outfit getOutfit(LifeStage lifeStage, Gender gender) {
    boolean topsAndBottomsAvailable = (topAndBottomTab.getBottomPieceData().getItemCount() > 0)
                                   && (topAndBottomTab.getTopPieceData().getItemCount() > 0);
    Outfit fullbody = getFullBodyOutfitState().getValue();
    if ((!topsAndBottomsAvailable || lastActiveOutfitTab != topAndBottomTab) && fullbody != null) {
      return fullbody;
    }

    final TopPiece top = getTopPieceState().getValue();
    final BottomPiece bottom = getBottomPieceState().getValue();
    return switch (lifeStage) {
      // TODDLER does not have tops and bottom options so this should not be reached. Here to cover all cases.
      case TODDLER -> fullbody;
      case CHILD -> switch (gender) {
        case MALE -> new MaleChildTopAndBottomOutfit((MaleChildTopPiece) top, (MaleChildBottomPiece) bottom);
        case FEMALE -> new FemaleChildTopAndBottomOutfit((FemaleChildTopPiece) top, (FemaleChildBottomPiece) bottom);
      };
      case TEEN -> switch (gender) {
        case MALE -> new MaleTeenTopAndBottomOutfit((MaleTeenTopPiece) top, (MaleTeenBottomPiece) bottom);
        case FEMALE -> new FemaleTeenTopAndBottomOutfit((FemaleTeenTopPiece) top, (FemaleTeenBottomPiece) bottom);
      };
      case ADULT -> switch (gender) {
        case MALE -> new MaleAdultTopAndBottomOutfit((MaleAdultTopPiece) top, (MaleAdultBottomPiece) bottom);
        case FEMALE -> new FemaleAdultTopAndBottomOutfit((FemaleAdultTopPiece) top, (FemaleAdultBottomPiece) bottom);
      };
      case ELDER -> switch (gender) {
        case MALE -> new MaleElderTopAndBottomOutfit((MaleElderTopPiece) top, (MaleElderBottomPiece) bottom);
        case FEMALE -> new FemaleElderTopAndBottomOutfit((FemaleElderTopPiece) top, (FemaleElderBottomPiece) bottom);
      };
    };
  }

  public void setStates(PersonResource personResource) {
    this.removeListenersIfAppropriate();
    try {
      this.getLifeStageState().setValueTransactionlessly(personResource.getLifeStage());
      this.getGenderState().setValueTransactionlessly(personResource.getGender());
      this.getBaseEyeColorState().setValueTransactionlessly((BaseEyeColor) personResource.getEyeColor());
      this.getSkinColorState().setValueTransactionlessly(personResource.getSkinColor().toAwtColor());

      Hair hair = personResource.getHair();
      HairHatStyleHairColorName hairHatStyleHairColorName = HairUtilities.getHairHatStyleColorNameFromHair(personResource.getLifeStage(), personResource.getGender(), hair);

      this.addHairColorNameToFront(hairHatStyleHairColorName.getHairColorName());

      this.updateOutfit(personResource.getLifeStage(), personResource.getGender(), personResource.getOutfit());
      //this.updateHairColorName( personResource.getLifeStage(), personResource.getGender(), hair, hair != null ? hair.toString() : null );
      //this.updateHair( personResource.getLifeStage(), personResource.getGender(), hair );
      this.updateHairHatStyleHairColorName(personResource.getLifeStage(), personResource.getGender(), hairHatStyleHairColorName);
      this.getObesityLevelState().setValueTransactionlessly(personResource.getObesityLevel());
      this.getBaseFaceState().setValueTransactionlessly((BaseFace) personResource.getFace());
    } finally {
      this.addListenersIfAppropriate();
    }
    this.syncPersonImpAndMaps();
    updateCameraPointOfView();
  }
}
