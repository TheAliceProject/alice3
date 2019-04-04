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

package org.lgna.story.resources.sims2;

public class BottomPieceManager extends IngredientManager<BottomPiece> {
  private static BottomPieceManager singleton = new BottomPieceManager();

  public static BottomPieceManager getSingleton() {
    return singleton;
  }

  private BottomPieceManager() {
    this.add(FemaleElderBottomPiece.class, FemaleElderBottomPiecePantsCowboyBoots.class, FemaleElderBottomPieceSlacks.class, FemaleElderBottomPieceSlacksHeels.class, FemaleElderBottomPieceSlinkySkirtBoots.class);
    this.add(MaleElderBottomPiece.class, MaleElderBottomPieceCowboyJeans.class, MaleElderBottomPieceLongShorts.class, MaleElderBottomPiecePantsSocksShoes.class, MaleElderBottomPieceShorts.class);
    this.add(FemaleTeenBottomPiece.class, FemaleTeenBottomPieceBaggyPants.class, FemaleTeenBottomPieceCargoPants.class, FemaleTeenBottomPieceFlairedPantsSandals.class, FemaleTeenBottomPieceHipFlarePantsBoots.class, FemaleTeenBottomPieceHipMicroMiniSkirt.class, FemaleTeenBottomPieceMiniShorts.class, FemaleTeenBottomPiecePantsCowboyBoots.class, FemaleTeenBottomPieceShorts.class, FemaleTeenBottomPieceShortShorts.class, FemaleTeenBottomPieceSkirt.class, FemaleTeenBottomPieceSkirtUBoots.class, FemaleTeenBottomPieceSlacksHeels.class);
    this.add(MaleTeenBottomPiece.class, MaleTeenBottomPieceBoardShorts.class, MaleTeenBottomPieceBoxers.class, MaleTeenBottomPieceCargoPants.class, MaleTeenBottomPieceKneeShorts.class, MaleTeenBottomPiecePantsCuffsBoots.class, MaleTeenBottomPieceSwimShort.class, MaleTeenBottomPieceTightPantsPointyToe.class);
    this.add(MaleChildBottomPiece.class, MaleChildBottomPieceBigShorts.class, MaleChildBottomPieceCowboyJeans.class, MaleChildBottomPieceShorts.class, ChildBottomPieceBaggyPants.class, ChildBottomPiecePuffyLeg.class);
    this.add(FemaleChildBottomPiece.class, FemaleChildBottomPieceMiniSkirt.class, FemaleChildBottomPiecePleatSkirt.class, FemaleChildBottomPieceShorts.class, ChildBottomPieceBaggyPants.class, ChildBottomPiecePuffyLeg.class);
    this.add(MaleAdultBottomPiece.class, MaleAdultBottomPieceCargoPants.class, MaleAdultBottomPieceCowboyJeans.class, MaleAdultBottomPieceLongShorts.class, MaleAdultBottomPiecePants.class, MaleAdultBottomPieceWillPants.class);
    this.add(FemaleAdultBottomPiece.class, FemaleAdultBottomPieceBaggyPants.class, FemaleAdultBottomPieceCapriSneaks.class, FemaleAdultBottomPieceCargoPants.class, FemaleAdultBottomPieceFlairedPantsSandals.class, FemaleAdultBottomPieceMomTailored.class, FemaleAdultBottomPiecePantsCowboyBoots.class, FemaleAdultBottomPieceShorts.class, FemaleAdultBottomPieceShortShorts.class, FemaleAdultBottomPieceShortSkirtBoots.class, FemaleAdultBottomPieceShortSkirtShoes.class, FemaleAdultBottomPieceSkirtTight.class, FemaleAdultBottomPieceSlacksHeels.class, FemaleAdultBottomPieceSlinkySkirtBoots.class);
  }

  @Override
  protected Class<Class<? extends BottomPiece>> getImplementingClassesComponentType() {
    return (Class<Class<? extends BottomPiece>>) BottomPiece.class.getClass();
  }

  @Override
  protected Class<? extends BottomPiece> getGenderedInterfaceClass(LifeStage lifeStage, Gender gender) {
    return lifeStage.getGenderedBottomPieceInterfaceClass(gender);
  }
}
