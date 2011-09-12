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

package org.alice.stageide.person;

/**
 * @author Dennis Cosgrove
 */
public class PersonImp extends org.lgna.story.implementation.SingleVisualModelImp {
	public PersonImp() {
		super( new edu.cmu.cs.dennisc.scenegraph.Visual() );
	}
	@Override
	public org.lgna.story.Entity getAbstraction() {
		return null;
	}
	@Override
	protected double getBoundingSphereRadius() {
		return 1.0;
	}
	private final java.util.Map< org.lgna.story.resources.sims2.LifeStage, edu.cmu.cs.dennisc.nebulous.Person > mapLifeStageToNebPerson = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	
	private edu.cmu.cs.dennisc.scenegraph.Geometry getSgGeometry() {
		edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = this.getSgVisuals()[ 0 ];
		final int N = sgVisual.getGeometryCount();
		if( N > 0 ) {
			return sgVisual.getGeometryAt( 0 );
		} else {
			return null;
		}
	}
	private void setSgGeometry( edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry ) {
		edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = this.getSgVisuals()[ 0 ];
		sgVisual.setGeometry( sgGeometry );
	}
	
	/*package-private*/ void updateNebPerson() {
		org.lgna.story.resources.sims2.LifeStage lifeStage = PersonResourceManager.SINGLETON.getLifeStage();
		edu.cmu.cs.dennisc.nebulous.Person nebPerson = this.mapLifeStageToNebPerson.get( lifeStage );
		if( nebPerson != null ) {
			//pass
		} else {
			try {
				nebPerson = new edu.cmu.cs.dennisc.nebulous.Person( lifeStage );
				this.mapLifeStageToNebPerson.put( lifeStage, nebPerson );
			} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
				//todo
				throw new RuntimeException( lre );
			}
		}
		org.lgna.story.resources.sims2.Gender gender = PersonResourceManager.SINGLETON.getGender();
		org.lgna.story.resources.sims2.SkinTone skinTone = PersonResourceManager.SINGLETON.getSkinTone();
		org.lgna.story.resources.sims2.EyeColor eyeColor = PersonResourceManager.SINGLETON.getEyeColor();
		double fitnessLevel = PersonResourceManager.SINGLETON.getFitnessLevel();
		org.lgna.story.resources.sims2.Hair hair = PersonResourceManager.SINGLETON.getHair();
		org.lgna.story.resources.sims2.Outfit outfit = PersonResourceManager.SINGLETON.getOutfit();
		
		nebPerson.setGender( gender );
		nebPerson.setSkinTone( skinTone );
		nebPerson.setEyeColor( eyeColor );
		nebPerson.setFitnessLevel( fitnessLevel );
		nebPerson.setHair( hair );
		nebPerson.setOutfit( outfit );
		edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry = this.getSgGeometry();
		if( nebPerson != sgGeometry ) {
			this.setSgGeometry( nebPerson );
		}
	}
	
//	private org.lgna.story.resources.sims2.LifeStage prevLifeStage;
//	private org.lgna.story.resources.sims2.Gender prevGender;
//	private org.lgna.story.resources.sims2.EyeColor prevEyeColor;
//	private org.lgna.story.resources.sims2.Hair prevHair;
//	private org.lgna.story.resources.sims2.Outfit prevOutfit;

	private int atomicCount = 0;
	public void pushAtomic() {
		if( this.atomicCount == 0 ) {
//			this.prevLifeStage = this.getLifeStage();
//			this.prevGender = this.getGender();
//			this.prevEyeColor = this.getEyeColor();
//			this.prevHair = this.getHair();
//			this.prevOutfit = this.getOutfit();
		}
		this.atomicCount++;
		
	}
	public void popAtomic() {
		this.atomicCount--;
		if( this.atomicCount == 0 ) {
			this.updateNebPerson();
		}
	}
}
