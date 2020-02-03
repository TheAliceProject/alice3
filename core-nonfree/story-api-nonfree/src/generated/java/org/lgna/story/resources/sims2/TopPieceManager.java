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

public class TopPieceManager extends IngredientManager<TopPiece> {
  private static TopPieceManager singleton = new TopPieceManager();

  public static TopPieceManager getSingleton() {
    return singleton;
  }

  private TopPieceManager() {
    this.add(MaleElderTopPiece.class, MaleElderTopPieceDressShirt.class, MaleElderTopPieceNaked.class, MaleElderTopPieceShortSleeveCollar.class, MaleElderTopPieceSweaterVestCollarShirt.class);
    this.add(FemaleElderTopPiece.class, FemaleElderTopPieceBlouse.class, FemaleElderTopPieceCowgirlShirt.class, FemaleElderTopPieceFlaredWaist.class, FemaleElderTopPiecePoloShirt.class, FemaleElderTopPieceSweaterSet.class, FemaleElderTopPieceTuckedWaist.class);
    this.add(MaleTeenTopPiece.class, MaleTeenTopPieceCrewneckShirtOver.class, MaleTeenTopPieceHipJacket.class, MaleTeenTopPieceJacketTshirtHang.class, MaleTeenTopPieceMockTurtle.class, MaleTeenTopPieceNaked.class, MaleTeenTopPieceShortSleeveCollar.class, MaleTeenTopPieceShortSleevedTie.class, MaleTeenTopPieceSweaterVestCollarShirt.class, MaleTeenTopPieceVNeckShortSleeve.class);
    this.add(FemaleTeenTopPiece.class, FemaleTeenTopPieceBlousedWaist.class, FemaleTeenTopPieceCowgirlShirt.class, FemaleTeenTopPieceCrewneckShirtOver.class, FemaleTeenTopPieceFlaredMidriff.class, FemaleTeenTopPieceHalter.class, FemaleTeenTopPieceJeansJacket.class, FemaleTeenTopPieceMidSleeveTshirt.class, FemaleTeenTopPiecePoloShirt.class, FemaleTeenTopPieceTankTop.class, FemaleTeenTopPieceTShirt.class, FemaleTeenTopPieceVNeckLongSleeve.class);
    this.add(MaleChildTopPiece.class, MaleChildTopPieceCowboyShirt.class, MaleChildTopPieceVNeckShortSleeve.class, MaleChildTopPieceNaked.class, ChildTopPieceShortSleeve.class, ChildTopPieceShortSleeveCollar.class);
    this.add(FemaleChildTopPiece.class, FemaleChildTopPieceCamisole.class, FemaleChildTopPieceHalter.class, FemaleChildTopPieceTShirt.class, ChildTopPieceShortSleeve.class, ChildTopPieceShortSleeveCollar.class);
    this.add(FemaleAdultTopPiece.class, FemaleAdultTopPieceBlousedWaist.class, FemaleAdultTopPieceCowgirlShirt.class, FemaleAdultTopPieceCrewneckCollarShirt.class, FemaleAdultTopPieceHalter.class, FemaleAdultTopPieceMidSleeveTshirt.class, FemaleAdultTopPieceMomShirt.class, FemaleAdultTopPiecePoloShirt.class, FemaleAdultTopPieceSweaterSet.class, FemaleAdultTopPieceTShirt.class, FemaleAdultTopPieceTuckedWaist.class, FemaleAdultTopPieceVNeckLongSleeve.class);
    this.add(MaleAdultTopPiece.class, MaleAdultTopPieceCowboyShirt.class, MaleAdultTopPieceJacketTshirtHang.class, MaleAdultTopPieceNaked.class, MaleAdultTopPieceShirtPolo.class, MaleAdultTopPieceShortSleeveCollar.class, MaleAdultTopPieceSweaterHang.class, MaleAdultTopPieceTankTop.class, MaleAdultTopPieceVNeckShortSleeve.class);
  }

  @Override
  protected Class<Class<? extends TopPiece>> getImplementingClassesComponentType() {
    return (Class<Class<? extends TopPiece>>) TopPiece.class.getClass();
  }

  @Override
  protected Class<? extends TopPiece> getGenderedInterfaceClass(LifeStage lifeStage, Gender gender) {
    return lifeStage.getGenderedTopPieceInterfaceClass(gender);
  }
}
