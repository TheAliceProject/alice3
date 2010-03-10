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
package edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes.garden;

public class Garden extends edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.GalleryModel { 
	public Garden() {
		super( "garden/garden" );
}
	public enum Part {
		Brick_circle_Brick_trans_right( "brick-circle", "brick-trans-right" ),
		Brick_circle_Brick_trans_left_Brick_oval_outer_Brick_entry_right( "brick-circle", "brick-trans-left", "brick-oval-outer", "brick-entry-right" ),
		Brick_circle_Brick_trans_left_Brick_oval_outer_Brick_entry_left( "brick-circle", "brick-trans-left", "brick-oval-outer", "brick-entry-left" ),
		Brick_circle_Brick_trans_left_Brick_oval_outer_Brick_oval_inner( "brick-circle", "brick-trans-left", "brick-oval-outer", "brick-oval-inner" ),
		Brick_circle_Brick_trans_left_Brick_oval_outer( "brick-circle", "brick-trans-left", "brick-oval-outer" ),
		Brick_circle_Brick_trans_left( "brick-circle", "brick-trans-left" ),
		Brick_circle_Brick_exit_left( "brick-circle", "brick-exit-left" ),
		Brick_circle_Brick_exit_right( "brick-circle", "brick-exit-right" ),
		Brick_circle( "brick-circle" ),
		Wall_front_Wall_front_top( "wall-front", "wall-front-top" ),
		Wall_front( "wall-front" ),
		Wall_left_Wall_left_top( "wall-left", "wall-left-top" ),
		Wall_left( "wall-left" ),
		Wall_rear_Wall_rear_top( "wall-rear", "wall-rear-top" ),
		Wall_rear( "wall-rear" ),
		Wall_right_Wall_right_top( "wall-right", "wall-right-top" ),
		Wall_right( "wall-right" );
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
