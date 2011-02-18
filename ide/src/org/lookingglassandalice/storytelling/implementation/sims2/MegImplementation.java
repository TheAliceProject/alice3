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

package org.lookingglassandalice.storytelling.implementation.sims2;

/**
 * @author Dennis Cosgrove
 */
public class MegImplementation extends org.lookingglassandalice.storytelling.implementation.PersonImplementation {
	private final edu.cmu.cs.dennisc.nebulous.Person nebPerson;
	public MegImplementation( org.lookingglassandalice.storytelling.Person abstraction, org.lookingglassandalice.storytelling.resources.PersonResource resource) {
		super( new edu.cmu.cs.dennisc.scenegraph.Visual(), abstraction, resource );
		try {
			org.alice.apis.stage.Adult temp = new org.alice.apis.stage.Adult();
			this.nebPerson = new edu.cmu.cs.dennisc.nebulous.Person( temp );
			this.nebPerson.setGender( org.alice.apis.stage.Gender.FEMALE );
			this.nebPerson.setOutfit( org.alice.apis.stage.FemaleAdultFullBodyOutfitAmbulanceDriver.BLUE );
			this.nebPerson.setSkinTone( org.alice.apis.stage.BaseSkinTone.getRandom() );
			this.nebPerson.setFitnessLevel( 0.5 );
			this.nebPerson.setHair( org.alice.apis.stage.FemaleAdultHairBraids.BLACK );
			this.nebPerson.setEyeColor( org.alice.apis.stage.BaseEyeColor.getRandom() );
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			throw new RuntimeException( lre );
		}
		this.getSgVisual().geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { this.nebPerson } );
	}
	@Override
	protected JointImplementation createJointImplementation( org.lookingglassandalice.storytelling.resources.JointId jointId ) {
		return new JointImplementation( this, new NativeJoint( this.nebPerson, jointId ) );
	}
}
