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
package org.alice.apis.stage;

public class FullBodyOutfitManager extends IngredientManager<FullBodyOutfit> {
	private static FullBodyOutfitManager singleton = new FullBodyOutfitManager();

	public static FullBodyOutfitManager getSingleton() {
		return singleton;
	}

	private FullBodyOutfitManager() {
//		this.add( AdultFullBodyOutfit.class,
//							UnisexAdultFullBodyOutfitAstronaut.class ,
//							UnisexAdultFullBodyOutfitLlama.class ,
//							UnisexAdultFullBodyOutfiteton.class
//		);

		this.add( FemaleChildFullBodyOutfit.class,

				FemaleChildFullBodyOutfitBlazerPleats.class,
				FemaleChildFullBodyOutfitCowGirlSkirt.class,
				FemaleChildFullBodyOutfitDressAboveKneeCollar.class,
				FemaleChildFullBodyOutfitDressFormal.class,
				FemaleChildFullBodyOutfitDressShortBell.class,
				FemaleChildFullBodyOutfitJacketDressFlare.class,
				FemaleChildFullBodyOutfitTShirtPants.class
			);
			this.add( ChildFullBodyOutfit.class,

				ChildFullBodyOutfitJumper.class,
				ChildFullBodyOutfitNaked.class,
				ChildFullBodyOutfitPirate.class,
				ChildFullBodyOutfitPuffyPJ.class
			);
			this.add( MaleChildFullBodyOutfit.class,

				MaleChildFullBodyOutfitBigShorts.class,
				MaleChildFullBodyOutfitLongSweaterPants.class,
				MaleChildFullBodyOutfitOverShirtShorts.class,
				MaleChildFullBodyOutfitPulloverShirtPants.class,
				MaleChildFullBodyOutfitShirtOverPants.class,
				MaleChildFullBodyOutfitSportif.class
			);
		this.add( FemaleAdultFullBodyOutfit.class,
							FemaleAdultFullBodyOutfitAmbulanceDriver.class ,
							FemaleAdultFullBodyOutfitApron.class ,
							FemaleAdultFullBodyOutfitBartender.class ,
							FemaleAdultFullBodyOutfitBurglar.class ,
							FemaleAdultFullBodyOutfitBusDriver.class ,
							FemaleAdultFullBodyOutfitChef.class ,
							FemaleAdultFullBodyOutfitClerk.class ,
							FemaleAdultFullBodyOutfitCoach.class ,
							FemaleAdultFullBodyOutfitCop.class ,
							FemaleAdultFullBodyOutfitDeliveryPerson.class ,
							FemaleAdultFullBodyOutfitDress.class ,
							FemaleAdultFullBodyOutfitDressAboveKnee.class ,
							FemaleAdultFullBodyOutfitDressAboveKneeHooded.class ,
							FemaleAdultFullBodyOutfitDressAboveKneeSuit.class ,
							FemaleAdultFullBodyOutfitDressChina.class ,
							FemaleAdultFullBodyOutfitDressFormalLong.class ,
							FemaleAdultFullBodyOutfitDressGothic.class ,
							FemaleAdultFullBodyOutfitDressKorean.class ,
							FemaleAdultFullBodyOutfitDressLongHug.class ,
							FemaleAdultFullBodyOutfitDressLongLoose.class ,
							////FemaleAdultFullBodyOutfitDressLongTwo.class ,
							//FemaleAdultFullBodyOutfitExterminator.class ,
							FemaleAdultFullBodyOutfitFastFood.class ,
							FemaleAdultFullBodyOutfitFurCoatShortDressShoes.class ,
							FemaleAdultFullBodyOutfitGardener.class ,
							FemaleAdultFullBodyOutfitJacketHighCollar.class ,
							FemaleAdultFullBodyOutfitJacketShortDressBoots.class ,
							FemaleAdultFullBodyOutfitJacketShortDressShoes.class ,
							FemaleAdultFullBodyOutfitJacketShortDressSlitShoes.class ,
							FemaleAdultFullBodyOutfitJacketTurtleSweaterDressBoots.class ,
							FemaleAdultFullBodyOutfitKTDream.class ,
							FemaleAdultFullBodyOutfitLabcoat.class ,
							FemaleAdultFullBodyOutfitLeatherJacket.class ,
							FemaleAdultFullBodyOutfitMailDelivery.class ,
							FemaleAdultFullBodyOutfitMaternityShirtPants.class ,
							FemaleAdultFullBodyOutfitMechanic.class ,
							FemaleAdultFullBodyOutfitMilitaryOfficer.class ,
							FemaleAdultFullBodyOutfitMRacer.class ,
							//FemaleAdultFullBodyOutfitNaked.class ,
							FemaleAdultFullBodyOutfitNightgown.class ,
							FemaleAdultFullBodyOutfitOrderly.class ,
							FemaleAdultFullBodyOutfitOverShirtPantsSandals.class ,
							FemaleAdultFullBodyOutfitPajamasClassic.class ,
							FemaleAdultFullBodyOutfitPirate.class ,
							FemaleAdultFullBodyOutfitPizzaDelivery.class ,
							FemaleAdultFullBodyOutfitPowerSuit.class ,
							FemaleAdultFullBodyOutfitScrubs.class ,
							//FemaleAdultFullBodyOutfitServer.class ,
							FemaleAdultFullBodyOutfitShirtUntuckedOxford.class ,
							////FemaleAdultFullBodyOutfitShortDressBoots.class ,
							//FemaleAdultFullBodyOutfitShortDressShoes.class ,
							FemaleAdultFullBodyOutfitSlickSuit.class ,
							FemaleAdultFullBodyOutfitSocialWorker.class ,
							FemaleAdultFullBodyOutfitSoldier.class ,
							FemaleAdultFullBodyOutfitSuit.class ,
							FemaleAdultFullBodyOutfitSundress.class ,
							////FemaleAdultFullBodyOutfitSuspendersTights.class ,
							FemaleAdultFullBodyOutfitSwat.class ,
							FemaleAdultFullBodyOutfitSweats.class ,
							////FemaleAdultFullBodyOutfitSwimwear.class ,
							////FemaleAdultFullBodyOutfitSwimwearCleavage.class ,
							////FemaleAdultFullBodyOutfitSwimwearSporty.class ,
							FemaleAdultFullBodyOutfitTracksuit.class ,
							FemaleAdultFullBodyOutfitTurtleSweaterDressShoes.class ,
							FemaleAdultFullBodyOutfitTweedJacket.class
							////FemaleAdultFullBodyOutfitUnderwear.class ,
							////FemaleAdultFullBodyOutfitWarrior.class ,
		);
		this.add( MaleAdultFullBodyOutfit.class,
							MaleAdultFullBodyOutfitAmbulanceDriver.class ,
							MaleAdultFullBodyOutfitApron.class ,
							MaleAdultFullBodyOutfitBartender.class ,
							MaleAdultFullBodyOutfitBurglar.class ,
							MaleAdultFullBodyOutfitBusDriver.class ,
							MaleAdultFullBodyOutfitChef.class ,
							MaleAdultFullBodyOutfitClerk.class ,
							MaleAdultFullBodyOutfitClosedCoatLongPants.class ,
							MaleAdultFullBodyOutfitCoach.class ,
							MaleAdultFullBodyOutfitCop.class ,
							MaleAdultFullBodyOutfitCouture.class ,
							MaleAdultFullBodyOutfitDeliveryPerson.class ,
							MaleAdultFullBodyOutfitDressKorean.class ,
							MaleAdultFullBodyOutfitExterminator.class ,
							MaleAdultFullBodyOutfitFancySuit.class ,
							MaleAdultFullBodyOutfitFastFood.class ,
							MaleAdultFullBodyOutfitGardener.class ,
							MaleAdultFullBodyOutfitGothTeeShirt.class ,
							MaleAdultFullBodyOutfitHiphopHood.class ,
							MaleAdultFullBodyOutfitHoodedSweatShirtBoardShorts.class ,
							MaleAdultFullBodyOutfitHoodedSweatShirtPants.class ,
							MaleAdultFullBodyOutfitKilt.class ,
							MaleAdultFullBodyOutfitLabcoat.class ,
							MaleAdultFullBodyOutfitLongCoat.class ,
							MaleAdultFullBodyOutfitLooseOpenCoatPants.class ,
							MaleAdultFullBodyOutfitMailDelivery.class ,
							MaleAdultFullBodyOutfitManMaid.class ,
							MaleAdultFullBodyOutfitMaternityComfy.class ,
							MaleAdultFullBodyOutfitMechanic.class ,
							MaleAdultFullBodyOutfitMRacer.class ,
							//MaleAdultFullBodyOutfitNaked.class ,
							//MaleAdultFullBodyOutfitOpenCoatJeans.class ,
							MaleAdultFullBodyOutfitOpenCoatLongPants.class ,
							MaleAdultFullBodyOutfitOpenShirtPants.class ,
							MaleAdultFullBodyOutfitOpenSportcoatLongPants.class ,
							MaleAdultFullBodyOutfitOrderly.class ,
							MaleAdultFullBodyOutfitOveralls.class ,
							MaleAdultFullBodyOutfitOverhangTshirt.class ,
							//MaleAdultFullBodyOutfitOverhangTshirtGamespot.class ,
							//MaleAdultFullBodyOutfitOverhangTshirtGamespy.class ,
							MaleAdultFullBodyOutfitOverhangTshirtLongShorts.class ,
							MaleAdultFullBodyOutfitOverShirtPantsShoes.class ,
							MaleAdultFullBodyOutfitPajamas.class ,
							MaleAdultFullBodyOutfitPajamasBoxers.class ,
							MaleAdultFullBodyOutfitPajamasDrawstring.class ,
							MaleAdultFullBodyOutfitPirate.class ,
							MaleAdultFullBodyOutfitPizzaDelivery.class ,
							MaleAdultFullBodyOutfitPowerSuit.class ,
							MaleAdultFullBodyOutfitPunk.class ,
							MaleAdultFullBodyOutfitScrubs.class ,
							MaleAdultFullBodyOutfitServer.class ,
							MaleAdultFullBodyOutfitShirtFlares.class ,
							MaleAdultFullBodyOutfitShirtUntuckedSaddle.class ,
							MaleAdultFullBodyOutfitShorts.class ,
				//			MaleAdultFullBodyOutfitShrink.class ,
							MaleAdultFullBodyOutfitSlickSuit.class ,
							MaleAdultFullBodyOutfitSloppySuit.class ,
							MaleAdultFullBodyOutfitSoldier.class ,
							MaleAdultFullBodyOutfitSuit.class ,
							MaleAdultFullBodyOutfitSwat.class ,
							MaleAdultFullBodyOutfitSweats.class ,
							MaleAdultFullBodyOutfitTrackSuit.class ,
							MaleAdultFullBodyOutfitTrenchCoatPantsBoots.class ,
							MaleAdultFullBodyOutfitTweedJacket.class
							////MaleAdultFullBodyOutfitUnderwear.class ,
		);
	}

	@Override
	protected Class<Class<? extends FullBodyOutfit>> getImplementingClassesComponentType() {
		return (Class<Class<? extends FullBodyOutfit>>)FullBodyOutfit.class.getClass();
	}
	@Override
	protected Class<? extends FullBodyOutfit> getUnisexIntefaceClass( LifeStage lifeStage ) {
		return lifeStage.getUnisexFullBodyOutfitInterfaceClass();
	};
	@Override
	protected Class<? extends FullBodyOutfit> getGenderedIntefaceClass( LifeStage lifeStage, Gender gender ) {
		return lifeStage.getGenderedFullBodyOutfitInterfaceClass( gender );
	}
}
