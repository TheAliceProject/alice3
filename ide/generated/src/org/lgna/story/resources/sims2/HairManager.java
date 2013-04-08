/*
 * Alice 3 End User License Agreement
 * 
 * Copyright (c) 2006-2013, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice", nor may "Alice" appear in their name, without prior written permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software must display the following acknowledgement: "This product includes software developed by Carnegie Mellon University"
 * 
 * 5. The gallery of art assets and animations provided with this software is contributed by Electronic Arts Inc. and may be used for personal, non-commercial, and academic use only. Redistributions of any program source code that utilizes The Sims 2 Assets must also retain the copyright notice, list of conditions and the disclaimer contained in The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package org.lgna.story.resources.sims2;

public class HairManager extends IngredientManager<Hair> {
	private static HairManager singleton = new HairManager();

	public static HairManager getSingleton() {
		return singleton;
	}

	private HairManager() {
		this.add( MaleAdultHair.class,
				MaleAdultHairBald.class,
				MaleAdultHairBeanie.class,
				MaleAdultHairCloseCrop.class,
				MaleAdultHairCombOver.class,
				MaleAdultHairCornRows.class,
				MaleAdultHairCrewCut.class,
				MaleAdultHairDreadlockLong.class,
				MaleAdultHairFrontalWave.class,
				MaleAdultHairGibs.class,
				MaleAdultHairHatBallcapNPC.class,
				MaleAdultHairHatBallcapUp.class,
				MaleAdultHairHatBeret.class,
				MaleAdultHairHatBucket.class,
				MaleAdultHairHatCap.class,
				MaleAdultHairHatCapUp.class,
				MaleAdultHairHatCowboyFlat.class,
				MaleAdultHairHatFargo.class,
				MaleAdultHairHatFedora.class,
				MaleAdultHairHatFedoraCasual.class,
				MaleAdultHairHatKilt.class,
				MaleAdultHairHatViking.class,
				MaleAdultHairMessy.class,
				MaleAdultHairmohawk.class,
				MaleAdultHairMulletLong.class,
				MaleAdultHairPeak.class,
				MaleAdultHairPGSkater.class,
				MaleAdultHairPompodore.class,
				MaleAdultHairPonyTail.class,
				MaleAdultHairRocker.class,
				MaleAdultHairRound.class,
				MaleAdultHairSemiBald.class,
				MaleAdultHairShocked.class,
				MaleAdultHairShort.class,
				MaleAdultHairShortCenterSpike.class,
				MaleAdultHairShortCombed.class,
				MaleAdultHairShortGel.class,
				MaleAdultHairShortMop.class,
				MaleAdulthairshortsimple.class,
				MaleAdultHairShortSpikey.class,
				MaleAdultHairSixty.class,
				MaleAdultHairTopHat.class,
				MaleAdultHairWill.class,
				MaleAdultHairHatFirefighterRed.class,
				MaleAdultHairHatFirefighterYellow.class
				);
		this.add( FemaleAdultHair.class,
				FemaleAdultHairAcornTuck.class,
				FemaleAdultHairBald.class,
				FemaleAdultHairBarrette.class,
				FemaleAdultHairBeanie.class,
				FemaleAdultHairBowlTwist.class,
				FemaleAdultHairBraids.class,
				FemaleAdultHairBraidsUp.class,
				FemaleAdultHairBun.class,
				FemaleAdultHairCornRowsLong.class,
				FemaleAdultHairCrown.class,
				FemaleAdultHairDreadlockShort.class,
				FemaleAdultHairFeather.class,
				FemaleAdultHairFrenchBraid.class,
				FemaleAdultHairGetFabulous.class,
				FemaleAdultHairHalo.class,
				FemaleAdultHairHatAngora.class,
				FemaleAdultHairHatBallcapUp.class,
				FemaleAdultHairHatBucket.class,
				FemaleAdultHairHatCap.class,
				FemaleAdultHairHatCapUp.class,
				FemaleAdultHairHatClassicStrawUp.class,
				FemaleAdultHairHatCowboyUpDome.class,
				FemaleAdultHairHatCowboyUpFlat.class,
				FemaleAdultHairHatFargo.class,
				FemaleAdultHairHatHip.class,
				FemaleAdultHairLong.class,
				FemaleAdultHairLongSimple.class,
				FemaleAdultHairLowBun.class,
				FemaleAdultHairMediumCenterPart.class,
				FemaleAdultHairMessy.class,
				FemaleAdultHairMohawk.class,
				FemaleAdultHairPagePunk.class,
				FemaleAdultHairPGChoppy.class,
				FemaleAdultHairPonyTail.class,
				FemaleAdultHairPonyTailHigh.class,
				FemaleAdultHairPoofs.class,
				FemaleAdulthairPunkFlip.class,
				FemaleAdultHairRosettes.class,
				FemaleAdultHairRound.class,
				FemaleAdultHairShocked.class,
				FemaleAdultHairShortCute.class,
				FemaleAdultHairShortSlick.class,
				FemaleAdultHairShortTuckin.class,
				FemaleAdultHairHatFirefighterRed.class,
				FemaleAdultHairHatFirefighterYellow.class
				);
		this.add( FemaleChildHair.class,
				FemaleChildHairAcornTuck.class,
				FemaleChildHairBarrette.class,
				FemaleChildHairBraids.class,
				FemaleChildHairBraidsUp.class,
				FemaleChildHairBun.class,
				FemaleChildHairFormal.class,
				FemaleChildHairMediumCenterPart.class,
				FemaleChildHairPoofs.class,
				FemaleChildHairRosettes.class
				);
		this.add( ChildHair.class,
				ChildHairBald.class,
				ChildHairDreadlockShort.class,
				ChildHairHatTricorn.class,
				ChildHairRocker.class,
				ChildHairShocked.class
				);
		this.add( MaleChildHair.class,
				MaleChildHairCloseCrop.class,
				MaleChildHairGibs.class,
				MaleChildHairPompodore.class,
				MaleChildHairShort.class,
				MaleChildHairShortCombed.class,
				MaleChildHairShortGel.class,
				MaleChildHairShortMop.class
				);
	}

	@Override
	protected Class<Class<? extends Hair>> getImplementingClassesComponentType() {
		return (Class<Class<? extends Hair>>)Hair.class.getClass();
	}

	@Override
	protected Class<? extends Hair> getUnisexInterfaceClass( LifeStage lifeStage ) {
		return lifeStage.getUnisexHairInterfaceClass();
	}

	@Override
	protected Class<? extends Hair> getGenderedInterfaceClass( LifeStage lifeStage, Gender gender ) {
		return lifeStage.getGenderedHairInterfaceClass( gender );
	}
}
