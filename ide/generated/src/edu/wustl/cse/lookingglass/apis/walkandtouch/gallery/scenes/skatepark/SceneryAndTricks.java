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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.skatepark;

public class SceneryAndTricks extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryModel { 
	public SceneryAndTricks() {
		super( "skate park/Scenery and Tricks" );
}
	public enum Part {
		Ground_Steps_Rail( "Ground", "Steps", "Rail" ),
		Ground_Steps_Rail01( "Ground", "Steps", "Rail01" ),
		Ground_Steps( "Ground", "Steps" ),
		Ground_OuterWall_Rail03( "Ground", "OuterWall", "Rail03" ),
		Ground_OuterWall_OuterWall03_OuterWall01( "Ground", "OuterWall", "OuterWall03", "OuterWall01" ),
		Ground_OuterWall_OuterWall03_OuterWall02( "Ground", "OuterWall", "OuterWall03", "OuterWall02" ),
		Ground_OuterWall_OuterWall03( "Ground", "OuterWall", "OuterWall03" ),
		Ground_OuterWall( "Ground", "OuterWall" ),
		Ground_Steps01_Rail02( "Ground", "Steps01", "Rail02" ),
		Ground_Steps01( "Ground", "Steps01" ),
		Ground( "Ground" ),
		TmpMarker( "TmpMarker" ),
		RailCamMarker( "RailCamMarker" ),
		OverStepsCamMarker( "OverStepsCamMarker" ),
		AriealCam( "Arieal Cam" ),
		Start( "start" ),
		RampRot( "rampRot" ),
		CornerGrind( "cornerGrind" ),
		CornerGrindRot( "cornerGrindRot" ),
		RailGrindStart( "railGrindStart" ),
		RailGrindRail01( "railGrindRail01" ),
		RailGrindRail02( "railGrindRail02" ),
		StairJumpStart( "stairJumpStart" ),
		Railend( "railend" );
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
