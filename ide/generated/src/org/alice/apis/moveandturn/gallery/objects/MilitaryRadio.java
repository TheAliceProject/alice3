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
package org.alice.apis.moveandturn.gallery.objects;
	
public class MilitaryRadio extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public MilitaryRadio() {
		super( "Objects/MilitaryRadio" );
	}
	public enum Part {
		RadioBox_Dial1_Box08( "RadioBox", "Dial1", "Box08" ),
		RadioBox_Dial1( "RadioBox", "Dial1" ),
		RadioBox_Dial2_Box09( "RadioBox", "Dial2", "Box09" ),
		RadioBox_Dial2( "RadioBox", "Dial2" ),
		RadioBox_Dial3_Box07( "RadioBox", "Dial3", "Box07" ),
		RadioBox_Dial3( "RadioBox", "Dial3" ),
		RadioBox_Dial4_Box05( "RadioBox", "Dial4", "Box05" ),
		RadioBox_Dial4( "RadioBox", "Dial4" ),
		RadioBox_Dial5_Box04( "RadioBox", "Dial5", "Box04" ),
		RadioBox_Dial5( "RadioBox", "Dial5" ),
		RadioBox_Dial6_Box06( "RadioBox", "Dial6", "Box06" ),
		RadioBox_Dial6( "RadioBox", "Dial6" ),
		RadioBox_Readout( "RadioBox", "Readout" ),
		RadioBox_Switch4_Box11( "RadioBox", "Switch4", "Box11" ),
		RadioBox_Switch4( "RadioBox", "Switch4" ),
		RadioBox_Switch2_Box12( "RadioBox", "Switch2", "Box12" ),
		RadioBox_Switch2( "RadioBox", "Switch2" ),
		RadioBox_Switch1_Box03( "RadioBox", "Switch1", "Box03" ),
		RadioBox_Switch1( "RadioBox", "Switch1" ),
		RadioBox_Switch3_Box10( "RadioBox", "Switch3", "Box10" ),
		RadioBox_Switch3( "RadioBox", "Switch3" ),
		RadioBox_Osscilisco( "RadioBox", "Osscilisco" ),
		RadioBox( "RadioBox" ),
		Speaker( "Speaker" ),
		Cord( "Cord" );
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
