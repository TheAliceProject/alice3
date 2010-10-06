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
package org.alice.apis.moveandturn.gallery.medieval;
	
public class Knight extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Knight() {
		super( "Medieval/Knight" );
	}
	public enum Part {
		Lowerback_Breastplate_LeftPauldron( "lowerback", "Breastplate", "LeftPauldron" ),
		Lowerback_Breastplate_RightPauldron( "lowerback", "Breastplate", "RightPauldron" ),
		Lowerback_Breastplate( "lowerback", "Breastplate" ),
		Lowerback_Rhumerus_Rradius_Rhand( "lowerback", "rhumerus", "rradius", "rhand" ),
		Lowerback_Rhumerus_Rradius_LeftElbowPad01( "lowerback", "rhumerus", "rradius", "LeftElbowPad01" ),
		Lowerback_Rhumerus_Rradius( "lowerback", "rhumerus", "rradius" ),
		Lowerback_Rhumerus( "lowerback", "rhumerus" ),
		Lowerback_Lhumerus_Lradius_Lhand( "lowerback", "lhumerus", "lradius", "lhand" ),
		Lowerback_Lhumerus_Lradius_LeftElbowPad( "lowerback", "lhumerus", "lradius", "LeftElbowPad" ),
		Lowerback_Lhumerus_Lradius( "lowerback", "lhumerus", "lradius" ),
		Lowerback_Lhumerus( "lowerback", "lhumerus" ),
		Lowerback_Lowerneck_Head_Helmet_FaceGuard( "lowerback", "lowerneck", "head", "Helmet", "FaceGuard" ),
		Lowerback_Lowerneck_Head_Helmet( "lowerback", "lowerneck", "head", "Helmet" ),
		Lowerback_Lowerneck_Head( "lowerback", "lowerneck", "head" ),
		Lowerback_Lowerneck( "lowerback", "lowerneck" ),
		Lowerback( "lowerback" ),
		Rfemur_Rtibia_Rfoot( "rfemur", "rtibia", "rfoot" ),
		Rfemur_Rtibia( "rfemur", "rtibia" ),
		Rfemur( "rfemur" ),
		Lfemur_Ltibia_Lfoot( "lfemur", "ltibia", "lfoot" ),
		Lfemur_Ltibia( "lfemur", "ltibia" ),
		Lfemur( "lfemur" ),
		LeftLegGuard( "LeftLegGuard" ),
		RightLegGuard( "RightLegGuard" );
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
