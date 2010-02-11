/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.apis.stage;

public class FullBodyOutfitUtilities extends IngredientUtilities< FullBodyOutfit > {
	private static FullBodyOutfitUtilities singleton = new FullBodyOutfitUtilities();

	public static FullBodyOutfitUtilities getSingleton() {
		return singleton;
	}

	private FullBodyOutfitUtilities() {
//		this.add( AdultFullBodyOutfit.class,
//							UnisexAdultFullBodyOutfitAstronaut.class ,
//							UnisexAdultFullBodyOutfitLlama.class ,
//							UnisexAdultFullBodyOutfiteton.class
//		);

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
							FemaleAdultFullBodyOutfitServer.class ,
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
							MaleAdultFullBodyOutfitOpenCoatJeans.class ,
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
	protected Class< Class< ? extends FullBodyOutfit > > getImplementingClassesReturnValueComponentType() {
		return (Class< Class< ? extends FullBodyOutfit > >)FullBodyOutfit.class.getClass();
	}
	@Override
	protected Class< ? extends FullBodyOutfit > getUnisexIntefaceClass( LifeStage lifeStage ) {
		return lifeStage.getUnisexFullBodyOutfitInterfaceClass();
	};
	@Override
	protected Class< ? extends FullBodyOutfit > getGenderedIntefaceClass( LifeStage lifeStage, Gender gender ) {
		return lifeStage.getGenderedFullBodyOutfitInterfaceClass( gender );
	}
}
