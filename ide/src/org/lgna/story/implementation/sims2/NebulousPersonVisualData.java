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

package org.lgna.story.implementation.sims2;

/**
 * @author Dennis Cosgrove
 */
public class NebulousPersonVisualData extends NebulousVisualData< edu.cmu.cs.dennisc.nebulous.Person > {
	private final org.lgna.story.resources.sims2.LifeStage lifeStage;
	private org.lgna.story.resources.sims2.Gender gender;
	private org.lgna.story.resources.sims2.Outfit outfit;
	private org.lgna.story.resources.sims2.SkinTone skinTone;
	private double obesityLevel;
	private org.lgna.story.resources.sims2.Hair hair;
	private org.lgna.story.resources.sims2.EyeColor eyeColor;
	
	private int atomicCount = 0;
	
	public static NebulousPersonVisualData createInstance( org.lgna.story.resources.sims2.PersonResource personResource ) throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
		NebulousPersonVisualData rv = new NebulousPersonVisualData( personResource.getLifeStage() );
		rv.pushAtomic();
		try {
			rv.setGender( personResource.getGender() );
			rv.setOutfit( personResource.getOutfit() );
			rv.setSkinTone( personResource.getSkinTone() );
			rv.setObesityLevel( personResource.getObesityLevel() );
			rv.setHair( personResource.getHair() );
			rv.setEyeColor( personResource.getEyeColor() );
		} finally {
			rv.popAtomic();
		}
		return rv;
	}

	private NebulousPersonVisualData( org.lgna.story.resources.sims2.LifeStage lifeStage ) throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
		super( new edu.cmu.cs.dennisc.nebulous.Person( lifeStage ) );
		this.lifeStage = lifeStage;
	}
	public org.lgna.story.resources.sims2.LifeStage getLifeStage() {
		return this.lifeStage;
	}
	
	public void pushAtomic() {
		if( this.atomicCount == 0 ) {
			
		}
		this.atomicCount++;
	}
	public void popAtomic() {
		this.atomicCount--;
		if( this.atomicCount == 0 ) {
			this.getNebModel().setAll( this.gender, this.skinTone, this.eyeColor, this.hair, this.outfit, this.obesityLevel );
		}
	}
	
	
	public org.lgna.story.resources.sims2.Gender getGender() {
		return this.gender;
	}
	public void setGender( org.lgna.story.resources.sims2.Gender gender ) {
		this.gender = gender;
		if( this.atomicCount == 0 ) {
			this.getNebModel().setGender( this.gender );
		}
	}
	public org.lgna.story.resources.sims2.Outfit getOutfit() {
		return this.outfit;
	}
	public void setOutfit( org.lgna.story.resources.sims2.Outfit outfit ) {
		this.outfit = outfit;
		if( this.atomicCount == 0 ) {
			this.getNebModel().setOutfit( this.outfit );
		}
	}
	public org.lgna.story.resources.sims2.SkinTone getSkinTone() {
		return this.skinTone;
	}
	public void setSkinTone( org.lgna.story.resources.sims2.SkinTone skinTone ) {
		this.skinTone = skinTone;
		if( this.atomicCount == 0 ) {
			this.getNebModel().setSkinTone( this.skinTone );
		}
	}
	public double getObesityLevel() {
		return this.obesityLevel;
	}
	public void setObesityLevel( double obesityLevel ) {
		this.obesityLevel = obesityLevel;
		if( this.atomicCount == 0 ) {
			this.getNebModel().setObesityLevel( this.obesityLevel );
		}
	}
	public org.lgna.story.resources.sims2.Hair getHair() {
		return this.hair;
	}
	public void setHair( org.lgna.story.resources.sims2.Hair hair ) {
		this.hair = hair;
		if( this.atomicCount == 0 ) {
			this.getNebModel().setHair( this.hair );
		}
	}
	public org.lgna.story.resources.sims2.EyeColor getEyeColor() {
		return this.eyeColor;
	}
	public void setEyeColor( org.lgna.story.resources.sims2.EyeColor eyeColor ) {
		this.eyeColor = eyeColor;
		if( this.atomicCount == 0 ) {
			this.getNebModel().setEyeColor( this.eyeColor );
		}
	}

//	@Override
//	public Dimension3 getSize() {
//		AxisAlignedBox aabb = this.nebPerson.getAxisAlignedMinimumBoundingBox();
//		if (!aabb.isNaN())
//		{
//			return new Dimension3(aabb.getWidth(), aabb.getHeight(), aabb.getDepth());
//		}
//		else
//		{
//			return new Dimension3(1, 2, 1);
//		}
//		
//	}

}
