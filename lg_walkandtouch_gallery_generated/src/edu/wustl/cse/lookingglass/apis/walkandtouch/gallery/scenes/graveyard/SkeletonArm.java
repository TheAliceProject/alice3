/*
 * Copyright (c) 2008-2010, Washington University in St. Louis. All rights reserved.
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
 * 3. Products derived from the software may not be called "Looking Glass", nor 
 *    may "Looking Glass" appear in their name, without prior written permission
 *    of Washington University in St. Louis.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Washington University in St. Louis"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL 
 * EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, 
 * TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, 
 * COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.graveyard;

public class SkeletonArm extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryModel { 
	public SkeletonArm() {
		super( "graveyard/skeleton arm" );
}
	public enum Part {
		Fore_Arm_Hand_Pinky_One( "Fore_Arm", "Hand", "Pinky", "One" ),
		Fore_Arm_Hand_Pinky( "Fore_Arm", "Hand", "Pinky" ),
		Fore_Arm_Hand_Ring_One( "Fore_Arm", "Hand", "Ring", "One" ),
		Fore_Arm_Hand_Ring( "Fore_Arm", "Hand", "Ring" ),
		Fore_Arm_Hand_Middle_One( "Fore_Arm", "Hand", "Middle", "One" ),
		Fore_Arm_Hand_Middle( "Fore_Arm", "Hand", "Middle" ),
		Fore_Arm_Hand_Pointer_One( "Fore_Arm", "Hand", "Pointer", "One" ),
		Fore_Arm_Hand_Pointer( "Fore_Arm", "Hand", "Pointer" ),
		Fore_Arm_Hand_Thumb_One_FingTwo( "Fore_Arm", "Hand", "Thumb", "One", "fingTwo" ),
		Fore_Arm_Hand_Thumb_One_Fintwo( "Fore_Arm", "Hand", "Thumb", "One", "fintwo" ),
		Fore_Arm_Hand_Thumb_One_Fingrtwo( "Fore_Arm", "Hand", "Thumb", "One", "fingrtwo" ),
		Fore_Arm_Hand_Thumb_One_FingerTwo( "Fore_Arm", "Hand", "Thumb", "One", "fingerTwo" ),
		Fore_Arm_Hand_Thumb_One( "Fore_Arm", "Hand", "Thumb", "One" ),
		Fore_Arm_Hand_Thumb( "Fore_Arm", "Hand", "Thumb" ),
		Fore_Arm_Hand( "Fore_Arm", "Hand" ),
		Fore_Arm( "Fore_Arm" );
		private String[] m_path;
		Part( String... path ) {
			m_path = path;
		}
		public String[] getPath() {
			return m_path;
		}
	}
	public edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel getPart( Part part ) {
		return getDescendant( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class, part.getPath() );
	}
}
