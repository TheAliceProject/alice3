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
package org.alice.apis.moveandturn.gallery.vehicles;
	
public class LifeBoat extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public LifeBoat() {
		super( "Vehicles/lifeBoat" );
	}
	public enum Part {
		MainBar_CrossBar_Bar2( "mainBar", "crossBar", "bar2" ),
		MainBar_CrossBar_Bar1( "mainBar", "crossBar", "bar1" ),
		MainBar_CrossBar_SpotLight( "mainBar", "crossBar", "spotLight" ),
		MainBar_CrossBar( "mainBar", "crossBar" ),
		MainBar( "mainBar" ),
		Lookout_Head2( "lookout", "head2" ),
		Lookout_LeftArm_LeftHand2( "lookout", "leftArm", "leftHand2" ),
		Lookout_LeftArm( "lookout", "leftArm" ),
		Lookout_Shoulder_Bicep_Forearm_Palm_MiddleF( "lookout", "Shoulder", "Bicep", "Forearm", "Palm", "MiddleF" ),
		Lookout_Shoulder_Bicep_Forearm_Palm_IndexF( "lookout", "Shoulder", "Bicep", "Forearm", "Palm", "IndexF" ),
		Lookout_Shoulder_Bicep_Forearm_Palm_Thumb( "lookout", "Shoulder", "Bicep", "Forearm", "Palm", "Thumb" ),
		Lookout_Shoulder_Bicep_Forearm_Palm_RingF( "lookout", "Shoulder", "Bicep", "Forearm", "Palm", "RingF" ),
		Lookout_Shoulder_Bicep_Forearm_Palm_PinkyF( "lookout", "Shoulder", "Bicep", "Forearm", "Palm", "PinkyF" ),
		Lookout_Shoulder_Bicep_Forearm_Palm( "lookout", "Shoulder", "Bicep", "Forearm", "Palm" ),
		Lookout_Shoulder_Bicep_Forearm( "lookout", "Shoulder", "Bicep", "Forearm" ),
		Lookout_Shoulder_Bicep( "lookout", "Shoulder", "Bicep" ),
		Lookout_Shoulder( "lookout", "Shoulder" ),
		Lookout( "lookout" ),
		Rower_Head( "rower", "head" ),
		Rower_RightBicep_RightForearm_RightHand_RightOar( "rower", "rightBicep", "rightForearm", "rightHand", "rightOar" ),
		Rower_RightBicep_RightForearm_RightHand( "rower", "rightBicep", "rightForearm", "rightHand" ),
		Rower_RightBicep_RightForearm( "rower", "rightBicep", "rightForearm" ),
		Rower_RightBicep( "rower", "rightBicep" ),
		Rower_LeftBicep_LeftForearm_LeftHand_LeftOar( "rower", "leftBicep", "leftForearm", "leftHand", "leftOar" ),
		Rower_LeftBicep_LeftForearm_LeftHand( "rower", "leftBicep", "leftForearm", "leftHand" ),
		Rower_LeftBicep_LeftForearm( "rower", "leftBicep", "leftForearm" ),
		Rower_LeftBicep( "rower", "leftBicep" ),
		Rower( "rower" );
		private String[] m_path;
		Part( String... path ) {
			m_path = path;
		}
		public String[] getPath() {
			return m_path;
		}
	}
	public org.alice.apis.moveandturn.Model getPart( Part part ) {
		return getDescendant( org.alice.apis.moveandturn.Model.class, part.getPath() );
	}

}
