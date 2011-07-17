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
package org.lookingglassandalice.storytelling.resources.sims2;

import org.lookingglassandalice.storytelling.Gender;
import org.lookingglassandalice.storytelling.IngredientManager;
import org.lookingglassandalice.storytelling.LifeStage;

public class HairManager extends IngredientManager< Hair > {
	private static HairManager singleton = new HairManager();

	public static HairManager getSingleton() {
		return singleton;
	}

	private HairManager() {
		this.add( FemaleChildHair.class, FemaleChildHairBraids.class, FemaleChildHairRosettes.class, FemaleChildHairPoofs.class );
		this.add( ChildHair.class, 
				ChildHairStraightWithTricornHat.class, 
//				ChildHairBald.class, 
//				ChildHairShocked.class, 
				ChildHairDreadlockShort.class 
		);
		this.add( MaleChildHair.class, MaleChildHairGibs.class, MaleChildHairCloseCrop.class, MaleChildHairPompodore.class );
		this.add( MaleAdultHair.class,
		//MaleAdultHairBeanie.class ,
				MaleAdultHairCloseCrop.class, MaleAdultHairCornRows.class, MaleAdultHairCrewCut.class,
				//MaleAdultHairCurlyWild.class ,
				MaleAdultHairDreadlockLong.class, MaleAdultHairGibs.class,
				//MaleAdultHairHatFedora.class ,
				MaleAdultHairHatFedoraCasual.class, MaleAdultHairMulletLong.class, MaleAdultHairPeak.class, MaleAdultHairPompodore.class, MaleAdultHairSemiBald.class,
				//MaleAdultHairShocked.class ,
				MaleAdultHairShortCombed.class, 
				//MaleAdultHairShortSpikey.class, 
				MaleAdultHairTopHat.class
		//MaleAdultHairBald.class
				);
		this.add( FemaleAdultHair.class,
				//					FemaleAdultHairBeanie.class ,
				FemaleAdultHairBraids.class, FemaleAdultHairCornRowsLong.class, FemaleAdultHairDreadlockShort.class, FemaleAdultHairFeather.class, FemaleAdultHairGetFabulous.class, FemaleAdultHairPoofs.class, FemaleAdultHairRosettes.class,
				FemaleAdultHairShocked.class, FemaleAdultHairShortSlick.class
		//					FemaleAdultHairBald.class
				);
	}

	@Override
	protected Class< Class< ? extends Hair >> getImplementingClassesComponentType() {
		return (Class< Class< ? extends Hair >>)Hair.class.getClass();
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
