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

public class HairUtilities extends IngredientUtilities< Hair > {
	private static HairUtilities singleton = new HairUtilities();

	public static HairUtilities getSingleton() {
		return singleton;
	}

	private HairUtilities() {
		this.add( MaleAdultHair.class,
						//MaleAdultHairBeanie.class ,
						MaleAdultHairCloseCrop.class ,
						MaleAdultHairCornRows.class ,
						MaleAdultHairCrewCut.class ,
						//MaleAdultHairCurlyWild.class ,
						MaleAdultHairDreadlockLong.class ,
						MaleAdultHairGibs.class ,
						//MaleAdultHairHatFedora.class ,
						MaleAdultHairHatFedoraCasual.class ,
						MaleAdultHairMulletLong.class ,
						MaleAdultHairPeak.class ,
						MaleAdultHairPompodore.class ,
						MaleAdultHairSemiBald.class ,
						//MaleAdultHairShocked.class ,
						MaleAdultHairShortCombed.class ,
						MaleAdultHairShortSpikey.class ,
						MaleAdultHairTopHat.class
						//MaleAdultHairBald.class
		);
		this.add( FemaleAdultHair.class,
	//					FemaleAdultHairBeanie.class ,
						FemaleAdultHairBraids.class ,
						FemaleAdultHairCornRowsLong.class ,
						FemaleAdultHairDreadlockShort.class ,
						FemaleAdultHairFeather.class ,
						FemaleAdultHairGetFabulous.class ,
						FemaleAdultHairPoofs.class ,
						FemaleAdultHairRosettes.class ,
						FemaleAdultHairShocked.class ,
						FemaleAdultHairShortSlick.class 
	//					FemaleAdultHairBald.class
		);
	}
	
	@Override
	protected Class< Class< ? extends Hair > > getImplementingClassesReturnValueComponentType() {
		return (Class< Class< ? extends Hair > >)Hair.class.getClass();
	}
	@Override
	protected Class< ? extends Hair > getUnisexIntefaceClass( LifeStage lifeStage ) {
		return lifeStage.getUnisexHairInterfaceClass();
	};
	@Override
	protected Class< ? extends Hair > getGenderedIntefaceClass( LifeStage lifeStage, Gender gender ) {
		return lifeStage.getGenderedHairInterfaceClass( gender );
	}
}
