/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.apis.moveandturn.gallery.space;
	
public class HyperionRobot extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public HyperionRobot() {
		super( "Space/hyperion_robot" );
	}
	public enum Part {
		Middle_FrontAxil_FrontLefttWheel( "middle", "front_axil", "FrontLefttWheel" ),
		Middle_FrontAxil_FrontRightWheel( "middle", "front_axil", "FrontRightWheel" ),
		Middle_FrontAxil( "middle", "front_axil" ),
		Middle_BackAxil_BackRightWheel( "middle", "back_axil", "BackRightWheel" ),
		Middle_BackAxil_BackLefttWheel( "middle", "back_axil", "BackLefttWheel" ),
		Middle_BackAxil( "middle", "back_axil" ),
		Middle_Backpole_Radar1( "middle", "backpole", "radar1" ),
		Middle_Backpole_Radar2( "middle", "backpole", "radar2" ),
		Middle_Backpole( "middle", "backpole" ),
		Middle_Motor( "middle", "motor" ),
		Middle_Front_TopCamera( "middle", "front", "TopCamera" ),
		Middle_Front_BottomCamera( "middle", "front", "BottomCamera" ),
		Middle_Front_RightSensor( "middle", "front", "RightSensor" ),
		Middle_Front_LeftSensor( "middle", "front", "LeftSensor" ),
		Middle_Front( "middle", "front" ),
		Middle( "middle" ),
		PanelAttachment_PanelFACE1_PanelBottom1( "panel_attachment", "panel-FACE1", "panel_bottom1" ),
		PanelAttachment_PanelFACE1( "panel_attachment", "panel-FACE1" ),
		PanelAttachment_PanelFACE3_PanelBottom3( "panel_attachment", "panel-FACE3", "panel_bottom3" ),
		PanelAttachment_PanelFACE3( "panel_attachment", "panel-FACE3" ),
		PanelAttachment_PanelFACE2_PanelBottom2( "panel_attachment", "panel-FACE2", "panel_bottom2" ),
		PanelAttachment_PanelFACE2( "panel_attachment", "panel-FACE2" ),
		PanelAttachment_PanelFACE4_PanelBottom4( "panel_attachment", "panel-FACE4", "panel_bottom4" ),
		PanelAttachment_PanelFACE4( "panel_attachment", "panel-FACE4" ),
		PanelAttachment( "panel_attachment" );
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
