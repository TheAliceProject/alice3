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
package org.alice.stageide.personeditor;

/**
 * @author Dennis Cosgrove
 */
public class PersonInfo {
	public static PersonInfo createFromPerson( org.alice.apis.stage.Person person ) {
		return new PersonInfo( 
				person.getLifeStage(), 
				person.getGender(), 
				(org.alice.apis.stage.BaseSkinTone)person.getSkinTone(), 
				(org.alice.apis.stage.BaseEyeColor)person.getEyeColor(), 
				(org.alice.apis.stage.FullBodyOutfit)person.getOutfit(), 
				person.getHair(), 
				person.getFitnessLevel()
		);
	}
	public static PersonInfo createRandom() {
		org.alice.apis.stage.LifeStage[] potentialLifeStages = { org.alice.apis.stage.LifeStage.ADULT, org.alice.apis.stage.LifeStage.CHILD };
		org.alice.apis.stage.LifeStage lifeStage = edu.cmu.cs.dennisc.random.RandomUtilities.getRandomValueFrom( potentialLifeStages );
		org.alice.apis.stage.Gender gender = org.alice.apis.stage.Gender.getRandom();
		return new PersonInfo( 
				lifeStage, 
				gender,
				org.alice.apis.stage.BaseSkinTone.getRandom(),
				org.alice.apis.stage.BaseEyeColor.getRandom(),
				org.alice.apis.stage.FullBodyOutfitManager.getSingleton().getRandomEnumConstant( lifeStage, gender ),
				org.alice.apis.stage.HairManager.getSingleton().getRandomEnumConstant( lifeStage, gender ),
				edu.cmu.cs.dennisc.random.RandomUtilities.nextDouble()
		);
	}

	private org.alice.apis.stage.LifeStage lifeStage;
	private org.alice.apis.stage.Gender gender;
	private org.alice.apis.stage.BaseSkinTone baseSkinTone;
	private org.alice.apis.stage.BaseEyeColor baseEyeColor;
	private org.alice.apis.stage.FullBodyOutfit fullBodyOutfit;
	private org.alice.apis.stage.Hair hair;
	private double fitnessLevel;

	private PersonInfo( org.alice.apis.stage.LifeStage lifeStage, org.alice.apis.stage.Gender gender, org.alice.apis.stage.BaseSkinTone baseSkinTone, org.alice.apis.stage.BaseEyeColor baseEyeColor, org.alice.apis.stage.FullBodyOutfit fullBodyOutfit, org.alice.apis.stage.Hair hair, double fitnessLevel ) {
		this.lifeStage = lifeStage;
		this.gender = gender;
		this.baseSkinTone = baseSkinTone;
		this.baseEyeColor = baseEyeColor;
		this.fullBodyOutfit = fullBodyOutfit;
		this.hair = hair;
		this.fitnessLevel = fitnessLevel;
	}
	public org.alice.apis.stage.LifeStage getLifeStage() {
		return this.lifeStage;
	}
	public org.alice.apis.stage.Gender getGender() {
		return this.gender;
	}
	public org.alice.apis.stage.BaseSkinTone getBaseSkinTone() {
		return this.baseSkinTone;
	}
	public org.alice.apis.stage.BaseEyeColor getBaseEyeColor() {
		return this.baseEyeColor;
	}
	public org.alice.apis.stage.FullBodyOutfit getFullBodyOutfit() {
		return this.fullBodyOutfit;
	}
	public org.alice.apis.stage.Hair getHair() {
		return this.hair;
	}
	public double getFitnessLevel() {
		return this.fitnessLevel;
	}
	
	public org.alice.apis.stage.Person createPerson() {
		org.alice.apis.stage.Person rv;
		if( this.lifeStage != null ) {
			rv = this.lifeStage.createInstance();
			update( rv );
		} else {
			rv = null;
		}
		return rv;
	}
	public org.alice.apis.stage.Person update( org.alice.apis.stage.Person rv ) {
		assert rv.getLifeStage() == this.lifeStage;
		rv.setGender( this.gender );
		rv.setSkinTone( this.baseSkinTone );
		rv.setEyeColor( this.baseEyeColor );
		rv.setOutfit( this.fullBodyOutfit );
		rv.setHair( this.hair );
		rv.setFitnessLevel( this.fitnessLevel, org.alice.apis.stage.Person.RIGHT_NOW );
		return rv;
	}
}
