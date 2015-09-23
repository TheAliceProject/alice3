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

package org.alice.stageide.personresource;

/**
 * @author Dennis Cosgrove
 */
public class PersonImp extends org.lgna.story.implementation.SingleVisualModelImp {
	public PersonImp() {
		super( new edu.cmu.cs.dennisc.scenegraph.Visual() );
	}

	@Override
	public org.lgna.story.SThing getAbstraction() {
		return null;
	}

	private final java.util.Map<org.lgna.story.resources.sims2.LifeStage, edu.cmu.cs.dennisc.nebulous.Person> mapLifeStageToNebPerson = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private edu.cmu.cs.dennisc.scenegraph.Geometry getSgGeometry() {
		edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = this.getSgVisuals()[ 0 ];
		final int N = sgVisual.getGeometryCount();
		if( N > 0 ) {
			return sgVisual.getGeometryAt( 0 );
		} else {
			return null;
		}
	}

	public void unload() {
		for( java.util.Map.Entry<org.lgna.story.resources.sims2.LifeStage, edu.cmu.cs.dennisc.nebulous.Person> entry : this.mapLifeStageToNebPerson.entrySet() ) {
			entry.getValue().synchronizedUnload();
		}
		this.mapLifeStageToNebPerson.clear();
		this.setSgGeometry( null );
	}

	private void setSgGeometry( edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry ) {
		edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = this.getSgVisuals()[ 0 ];
		sgVisual.setGeometry( sgGeometry );
	}

	/* package-private */void updateNebPerson() {
		IngredientsComposite composite = PersonResourceComposite.getInstance().getIngredientsComposite();
		org.lgna.story.resources.sims2.LifeStage lifeStage = composite.getLifeStageState().getValue();
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
		org.lgna.story.resources.sims2.PersonResource personResource = composite.createResourceFromStates();
		if( personResource == null ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "NOT SETTNG ATTRIBUTES ON PERSON: null resource." );
		}
		else {

		}
		org.lgna.story.resources.sims2.Gender gender = personResource.getGender();
		java.awt.Color awtSkinColor = new java.awt.Color( personResource.getSkinColor().getRed().floatValue(), personResource.getSkinColor().getGreen().floatValue(), personResource.getSkinColor().getBlue().floatValue() );
		org.lgna.story.resources.sims2.EyeColor eyeColor = personResource.getEyeColor();
		double obesityLevel = personResource.getObesityLevel();
		org.lgna.story.resources.sims2.Hair hair = personResource.getHair();
		org.lgna.story.resources.sims2.Outfit outfit = personResource.getOutfit();
		org.lgna.story.resources.sims2.Face face = personResource.getFace();

		if( ( gender == null ) || ( outfit == null ) || ( awtSkinColor == null ) || ( eyeColor == null ) || ( hair == null ) || ( face == null ) ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "NOT SETTNG ATTRIBUTES ON PERSON: gender=" + gender + ", outfit=" + outfit + ", skinColor" + awtSkinColor + ", eyeColor=" + eyeColor + ", obesityLevel=" + obesityLevel + ", hair=" + hair + ", face=" + face );
		} else {
			if( lifeStage.getGenderedHairInterfaceClass( gender ).isAssignableFrom( hair.getClass() ) ) {
				if( ( ( ( outfit instanceof org.lgna.story.resources.sims2.FullBodyOutfit ) && lifeStage.getGenderedFullBodyOutfitInterfaceClass( gender ).isAssignableFrom( outfit.getClass() ) ) || ( ( outfit instanceof org.lgna.story.resources.sims2.TopAndBottomOutfit<?, ?> ) && lifeStage.getGenderedTopPieceInterfaceClass( gender ).isAssignableFrom( ( (org.lgna.story.resources.sims2.TopAndBottomOutfit)outfit ).getTopPiece().getClass() ) && lifeStage.getGenderedBottomPieceInterfaceClass( gender ).isAssignableFrom( ( (org.lgna.story.resources.sims2.TopAndBottomOutfit)outfit ).getBottomPiece().getClass() ) ) ) ) {
					nebPerson.synchronizedSetAll( gender, outfit, awtSkinColor.getRGB(), obesityLevel, eyeColor, hair, face );
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( outfit, lifeStage, gender );
				}
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( hair, lifeStage, gender );
			}
		}
		edu.cmu.cs.dennisc.scenegraph.Geometry sgGeometry = this.getSgGeometry();
		if( nebPerson != sgGeometry ) {
			this.setSgGeometry( nebPerson );
		}
	}

	@Override
	public void setSize( edu.cmu.cs.dennisc.math.Dimension3 size ) {
		this.setScale( getScaleForSize( size ) );
	}
}
