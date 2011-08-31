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

import org.lgna.story.resourceutilities.StorytellingResources;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Dimension3;


/**
 * @author Dennis Cosgrove
 */
public class SimsBipedImplementation extends org.lgna.story.implementation.BipedImplementation {
	
	private final edu.cmu.cs.dennisc.nebulous.Person nebPerson;
	private final org.lgna.story.resources.sims2.LifeStage lifeStage;
	private org.lgna.story.resources.sims2.Gender gender;
	private org.lgna.story.resources.sims2.Outfit outfit;
	private org.lgna.story.resources.sims2.SkinTone skinTone;
	private double obesityLevel;
	private org.lgna.story.resources.sims2.Hair hair;
	private org.lgna.story.resources.sims2.EyeColor eyeColor;
	
	private int atomicCount = 0;

	public SimsBipedImplementation( org.lgna.story.Biped abstraction, org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		super( abstraction, new edu.cmu.cs.dennisc.scenegraph.Visual() );
		this.lifeStage = lifeStage;
		try {
			switch( this.lifeStage ) {
			case ADULT:
				this.nebPerson = new edu.cmu.cs.dennisc.nebulous.Person( org.lgna.story.resources.sims2.LifeStage.ADULT );
				break;
			case CHILD:
				this.nebPerson = new edu.cmu.cs.dennisc.nebulous.Person( org.lgna.story.resources.sims2.LifeStage.CHILD );
				break;
			case ELDER:
				this.nebPerson = new edu.cmu.cs.dennisc.nebulous.Person( org.lgna.story.resources.sims2.LifeStage.ELDER );
				break;
			case TEEN:
				this.nebPerson = new edu.cmu.cs.dennisc.nebulous.Person( org.lgna.story.resources.sims2.LifeStage.TEEN );
				break;
			case TODDLER:
				this.nebPerson = new edu.cmu.cs.dennisc.nebulous.Person( org.lgna.story.resources.sims2.LifeStage.TODDLER );
				break;
			default:
				this.nebPerson = null;
				break;
			}
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			throw new RuntimeException( lre );
		}
		this.getSgVisuals()[ 0 ].geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { this.nebPerson } );
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
			//this.nebPerson.setAll( this.gender, this.skinTone, this.eyeColor, this.hair, this.outfit, this.obesityLevel );
		}
	}
	
	
	public org.lgna.story.resources.sims2.Gender getGender() {
		return this.gender;
	}
	public void setGender( org.lgna.story.resources.sims2.Gender gender ) {
		this.gender = gender;
		this.nebPerson.setGender( this.gender );
	}
	public org.lgna.story.resources.sims2.Outfit getOutfit() {
		return this.outfit;
	}
	public void setOutfit( org.lgna.story.resources.sims2.Outfit outfit ) {
		this.outfit = outfit;
		this.nebPerson.setOutfit( this.outfit );
	}
	public org.lgna.story.resources.sims2.SkinTone getSkinTone() {
		return this.skinTone;
	}
	public void setSkinTone( org.lgna.story.resources.sims2.SkinTone skinTone ) {
		this.skinTone = skinTone;
		this.nebPerson.setSkinTone( this.skinTone );
	}
	public double getObesityLevel() {
		return this.obesityLevel;
	}
	public void setObesityLevel( double obesityLevel ) {
		this.obesityLevel = obesityLevel;
		this.nebPerson.setFitnessLevel( 1.0-this.obesityLevel );
	}
	public org.lgna.story.resources.sims2.Hair getHair() {
		return this.hair;
	}
	public void setHair( org.lgna.story.resources.sims2.Hair hair ) {
		this.hair = hair;
		this.nebPerson.setHair( this.hair );
	}
	public org.lgna.story.resources.sims2.EyeColor getEyeColor() {
		return this.eyeColor;
	}
	public void setEyeColor( org.lgna.story.resources.sims2.EyeColor eyeColor ) {
		this.eyeColor = eyeColor;
		this.nebPerson.setEyeColor( this.eyeColor );
	}
	@Override
	public Dimension3 getSize() {
		AxisAlignedBox aabb = this.nebPerson.getAxisAlignedMinimumBoundingBox();
		if (!aabb.isNaN())
		{
			return new Dimension3(aabb.getWidth(), aabb.getHeight(), aabb.getDepth());
		}
		else
		{
			return new Dimension3(1, 2, 1);
		}
		
	}
	
	@Override
	protected JointImplementation createJointImplementation( org.lgna.story.resources.JointId jointId ) {
		return new JointImplementation( this, new NebulousJoint( this.nebPerson, jointId ) );
	}
}
