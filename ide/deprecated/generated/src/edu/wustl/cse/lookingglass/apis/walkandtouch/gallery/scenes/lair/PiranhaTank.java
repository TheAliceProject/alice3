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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.lair;

public class PiranhaTank extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryModel { 
	edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double> State= new edu.cmu.cs.dennisc.alice.virtualmachine.Variable<Double>( 0.0 );
	public PiranhaTank() {
		super( "lair/Piranha Tank" );
}
	public enum Part {
		Tankfloor_TankWater( "tankfloor(", "tank water" ),
		Tankfloor_Tankwalls( "tankfloor(", "tankwalls(" ),
		Tankfloor( "tankfloor(" ),
		CraneBase_CraneArm_CraneRope1_CraneRope2_CraneRope3_CraneRope4_CraneClaw2( "craneBase", "craneArm", "craneRope1", "craneRope2", "craneRope3", "craneRope4", "craneClaw2" ),
		CraneBase_CraneArm_CraneRope1_CraneRope2_CraneRope3_CraneRope4_CraneClaw0( "craneBase", "craneArm", "craneRope1", "craneRope2", "craneRope3", "craneRope4", "craneClaw0" ),
		CraneBase_CraneArm_CraneRope1_CraneRope2_CraneRope3_CraneRope4( "craneBase", "craneArm", "craneRope1", "craneRope2", "craneRope3", "craneRope4" ),
		CraneBase_CraneArm_CraneRope1_CraneRope2_CraneRope3( "craneBase", "craneArm", "craneRope1", "craneRope2", "craneRope3" ),
		CraneBase_CraneArm_CraneRope1_CraneRope2( "craneBase", "craneArm", "craneRope1", "craneRope2" ),
		CraneBase_CraneArm_CraneRope1( "craneBase", "craneArm", "craneRope1" ),
		CraneBase_CraneArm( "craneBase", "craneArm" ),
		CraneBase( "craneBase" ),
		CamMarker( "camMarker" );
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

	public void CraneOut( ) {
		if (true ) {			this.getPart(PiranhaTank.Part.CraneBase_CraneArm_CraneRope1).resizeHeight( 0.5, 4.0, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
			this.getPart(PiranhaTank.Part.CraneBase_CraneArm).turn( org.alice.apis.moveandturn.TurnDirection.LEFT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 2.0 );
			this.getPart(PiranhaTank.Part.CraneBase_CraneArm_CraneRope1).resizeHeight( 2.0, 4.0, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
			State.value = 1.0;
		} else { 
			this.say( "Error: Crane not in Right State" );
		}

	}

	public void CraneOver( ) {
		if (false ) {			this.getPart(PiranhaTank.Part.CraneBase_CraneArm_CraneRope1).resizeHeight( 0.5, 4.0, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
			this.getPart(PiranhaTank.Part.CraneBase_CraneArm).turn( org.alice.apis.moveandturn.TurnDirection.RIGHT, new org.alice.apis.moveandturn.AngleInRevolutions(0.25), 2.0 );
			State.value = 2.0;
		} else { 
			this.say( "Error: Crane not in Right State" );
		}

	}

	public void CraneDrop( ) {
		if (false ) {			this.getPart(PiranhaTank.Part.CraneBase_CraneArm_CraneRope1).resizeHeight( 2.0, 4.0, org.alice.apis.moveandturn.ResizePolicy.PRESERVE_NOTHING  );
			State.value = 3.0;
		} else { 
			this.say( "Error: Crane not in Right State" );
		}

	}
}
