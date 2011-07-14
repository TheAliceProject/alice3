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
package org.alice.apis.moveandturn.gallery.japan;
	
public class Dojo2 extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Dojo2() {
		super( "Japan/Dojo2" );
	}
	public enum Part {
		SideRoof( "sideRoof" ),
		SideRoof01( "sideRoof01" ),
		Plane03( "Plane03" ),
		Plane04( "Plane04" ),
		BeamBottom( "beamBottom" ),
		BeamMiddle( "beamMiddle" ),
		BeamBottom01( "beamBottom01" ),
		BeamMiddle01( "beamMiddle01" ),
		BeamBottom02( "beamBottom02" ),
		BeamMiddle02( "beamMiddle02" ),
		BeamBottom03( "beamBottom03" ),
		BeamMiddle03( "beamMiddle03" ),
		BeamBottom04( "beamBottom04" ),
		BeamMiddle04( "beamMiddle04" ),
		BeamBottom05( "beamBottom05" ),
		BeamMiddle05( "beamMiddle05" ),
		BeamBottom06( "beamBottom06" ),
		BeamMiddle06( "beamMiddle06" ),
		BeamBottom07( "beamBottom07" ),
		BeamMiddle07( "beamMiddle07" ),
		BeamBottom08( "beamBottom08" ),
		BeamMiddle08( "beamMiddle08" ),
		BeamBottom09( "beamBottom09" ),
		BeamMiddle09( "beamMiddle09" ),
		Box04( "Box04" ),
		Box07( "Box07" ),
		Box08( "Box08" ),
		Box10( "Box10" ),
		Box11( "Box11" ),
		Box12( "Box12" ),
		Box13( "Box13" ),
		Box14( "Box14" ),
		Box15( "Box15" ),
		Box16( "Box16" ),
		Box17( "Box17" ),
		Box18( "Box18" ),
		Box19( "Box19" ),
		Box20( "Box20" ),
		Box21( "Box21" ),
		Box22( "Box22" ),
		Box23( "Box23" ),
		Box01( "Box01" ),
		Box24( "Box24" ),
		Box25( "Box25" ),
		Box26( "Box26" ),
		Box27( "Box27" ),
		Box28( "Box28" ),
		Box29( "Box29" ),
		Box30( "Box30" ),
		Box31( "Box31" ),
		Box32( "Box32" ),
		Box33( "Box33" ),
		Box34( "Box34" ),
		Box35( "Box35" ),
		Box36( "Box36" ),
		Box37( "Box37" ),
		Box38( "Box38" ),
		Box39( "Box39" ),
		Box40( "Box40" ),
		Box41( "Box41" ),
		Box42( "Box42" ),
		Box43( "Box43" ),
		Box44( "Box44" ),
		Box45( "Box45" ),
		BeamTop( "beamTop" ),
		Box48( "Box48" ),
		Box05( "Box05" ),
		Box50( "Box50" ),
		Box51( "Box51" ),
		Box52( "Box52" ),
		Box53( "Box53" ),
		Box54( "Box54" ),
		Box55( "Box55" ),
		Box56( "Box56" ),
		Box57( "Box57" ),
		Box58( "Box58" ),
		Box59( "Box59" ),
		Box60( "Box60" ),
		Box61( "Box61" ),
		Box63( "Box63" ),
		Box64( "Box64" ),
		Box65( "Box65" ),
		Box66( "Box66" ),
		Box67( "Box67" ),
		Box68( "Box68" ),
		Box69( "Box69" ),
		Box72( "Box72" ),
		Box73( "Box73" ),
		Box74( "Box74" ),
		Box75( "Box75" ),
		Box76( "Box76" ),
		Box77( "Box77" ),
		Box78( "Box78" ),
		Box79( "Box79" ),
		Box80( "Box80" ),
		Box81( "Box81" ),
		Box84( "Box84" ),
		Box85( "Box85" ),
		Box86( "Box86" ),
		Box87( "Box87" ),
		Box88( "Box88" ),
		Box89( "Box89" ),
		Box90( "Box90" ),
		Box91( "Box91" ),
		Box92( "Box92" ),
		Box93( "Box93" ),
		Box94( "Box94" ),
		Box95( "Box95" ),
		Box97( "Box97" ),
		Box98( "Box98" ),
		Box99( "Box99" ),
		Box100( "Box100" ),
		Box102( "Box102" ),
		Box103( "Box103" ),
		Box104( "Box104" ),
		Box105( "Box105" ),
		Ceiling( "ceiling" ),
		Box107( "Box107" ),
		Box108( "Box108" ),
		Box109( "Box109" ),
		Box110( "Box110" ),
		Box111( "Box111" ),
		Box112( "Box112" ),
		Box113( "Box113" ),
		Box114( "Box114" ),
		Box06( "Box06" ),
		Box115( "Box115" ),
		Box116( "Box116" ),
		Box117( "Box117" ),
		Box118( "Box118" ),
		Box119( "Box119" ),
		Box120( "Box120" ),
		Box121( "Box121" ),
		Box122( "Box122" ),
		BeamTop01( "beamTop01" ),
		BeamTop02( "beamTop02" ),
		BeamTop03( "beamTop03" ),
		BeamTop04( "beamTop04" ),
		BeamTop05( "beamTop05" ),
		BeamTop06( "beamTop06" ),
		BeamTop07( "beamTop07" ),
		BeamTop08( "beamTop08" ),
		BeamTop09( "beamTop09" ),
		Box127( "Box127" ),
		Box128( "Box128" ),
		Box129( "Box129" ),
		Box130( "Box130" );
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
