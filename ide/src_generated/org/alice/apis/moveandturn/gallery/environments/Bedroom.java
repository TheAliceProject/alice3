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
package org.alice.apis.moveandturn.gallery.environments;
	
public class Bedroom extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Bedroom() {
		super( "Environments/bedroom" );
	}
	public enum Part {
		WindowPane_WindowHorizBar_WindowVertBar1( "WindowPane", "WindowHorizBar", "WindowVertBar1" ),
		WindowPane_WindowHorizBar_WindowVertBar2( "WindowPane", "WindowHorizBar", "WindowVertBar2" ),
		WindowPane_WindowHorizBar( "WindowPane", "WindowHorizBar" ),
		WindowPane_WindowSill( "WindowPane", "WindowSill" ),
		WindowPane_RightShutter( "WindowPane", "RightShutter" ),
		WindowPane_LeftShutter( "WindowPane", "LeftShutter" ),
		WindowPane( "WindowPane" ),
		BedroomDoorSill_BreakableDoor1( "BedroomDoorSill", "BreakableDoor1" ),
		BedroomDoorSill_BreakableDoor2( "BedroomDoorSill", "BreakableDoor2" ),
		BedroomDoorSill_BreakableDoor3_BedroomKnobGuard_BedroomDoorKnob( "BedroomDoorSill", "BreakableDoor3", "BedroomKnobGuard", "BedroomDoorKnob" ),
		BedroomDoorSill_BreakableDoor3_BedroomKnobGuard( "BedroomDoorSill", "BreakableDoor3", "BedroomKnobGuard" ),
		BedroomDoorSill_BreakableDoor3( "BedroomDoorSill", "BreakableDoor3" ),
		BedroomDoorSill( "BedroomDoorSill" ),
		BedroomLightBase_ChainA1_ChainA2( "BedroomLightBase", "ChainA1", "ChainA2" ),
		BedroomLightBase_ChainA1_ChainB1_ChainB2( "BedroomLightBase", "ChainA1", "ChainB1", "ChainB2" ),
		BedroomLightBase_ChainA1_ChainB1_ChainC1_ChainC2( "BedroomLightBase", "ChainA1", "ChainB1", "ChainC1", "ChainC2" ),
		BedroomLightBase_ChainA1_ChainB1_ChainC1_BedroomLightbulb( "BedroomLightBase", "ChainA1", "ChainB1", "ChainC1", "BedroomLightbulb" ),
		BedroomLightBase_ChainA1_ChainB1_ChainC1( "BedroomLightBase", "ChainA1", "ChainB1", "ChainC1" ),
		BedroomLightBase_ChainA1_ChainB1( "BedroomLightBase", "ChainA1", "ChainB1" ),
		BedroomLightBase_ChainA1( "BedroomLightBase", "ChainA1" ),
		BedroomLightBase( "BedroomLightBase" );
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
