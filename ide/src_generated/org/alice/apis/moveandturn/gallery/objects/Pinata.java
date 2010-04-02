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
	
public class Pinata extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Pinata() {
		super( "Objects/Pinata" );
	}
	public enum Part {
		Head2_Head1_Ear( "Head2", "Head1", "Ear" ),
		Head2_Head1_Ear2( "Head2", "Head1", "Ear2" ),
		Head2_Head1_Bigheadchunk15( "Head2", "Head1", "bigheadchunk15" ),
		Head2_Head1_Bigheadchunk16( "Head2", "Head1", "bigheadchunk16" ),
		Head2_Head1_Bigheadchunk13( "Head2", "Head1", "bigheadchunk13" ),
		Head2_Head1_Bigheadchunk17( "Head2", "Head1", "bigheadchunk17" ),
		Head2_Head1_Bigheadchunk03( "Head2", "Head1", "bigheadchunk03" ),
		Head2_Head1_Bigheadchunk18( "Head2", "Head1", "bigheadchunk18" ),
		Head2_Head1_Bigheadchunk12( "Head2", "Head1", "bigheadchunk12" ),
		Head2_Head1_Bigheadchunk11( "Head2", "Head1", "bigheadchunk11" ),
		Head2_Head1_Bigheadchunk14( "Head2", "Head1", "bigheadchunk14" ),
		Head2_Head1_Bigheadchunk10( "Head2", "Head1", "bigheadchunk10" ),
		Head2_Head1_Bigheadchunk06( "Head2", "Head1", "bigheadchunk06" ),
		Head2_Head1_Bigheadchunk05( "Head2", "Head1", "bigheadchunk05" ),
		Head2_Head1_Bigheadchunk1( "Head2", "Head1", "bigheadchunk1" ),
		Head2_Head1_Bigheadchunk09( "Head2", "Head1", "bigheadchunk09" ),
		Head2_Head1_Bigheadchunk08( "Head2", "Head1", "bigheadchunk08" ),
		Head2_Head1_Bigheadchunk02( "Head2", "Head1", "bigheadchunk02" ),
		Head2_Head1_Bigheadchunk07( "Head2", "Head1", "bigheadchunk07" ),
		Head2_Head1_Bigheadchunk04( "Head2", "Head1", "bigheadchunk04" ),
		Head2_Head1_Bod6chunk45_Bod6chunk46( "Head2", "Head1", "bod6chunk45", "bod6chunk46" ),
		Head2_Head1_Bod6chunk45_Bod6chunk48( "Head2", "Head1", "bod6chunk45", "bod6chunk48" ),
		Head2_Head1_Bod6chunk45_Bod6chunk47( "Head2", "Head1", "bod6chunk45", "bod6chunk47" ),
		Head2_Head1_Bod6chunk45_Bod6chunk44( "Head2", "Head1", "bod6chunk45", "bod6chunk44" ),
		Head2_Head1_Bod6chunk45_Bod6chunk43( "Head2", "Head1", "bod6chunk45", "bod6chunk43" ),
		Head2_Head1_Bod6chunk45_Bod6chunk42( "Head2", "Head1", "bod6chunk45", "bod6chunk42" ),
		Head2_Head1_Bod6chunk45_Bod6chunk41( "Head2", "Head1", "bod6chunk45", "bod6chunk41" ),
		Head2_Head1_Bod6chunk45_Bod6chunk40( "Head2", "Head1", "bod6chunk45", "bod6chunk40" ),
		Head2_Head1_Bod6chunk45_Bod6chunk39( "Head2", "Head1", "bod6chunk45", "bod6chunk39" ),
		Head2_Head1_Bod6chunk45_Bod6chunk38( "Head2", "Head1", "bod6chunk45", "bod6chunk38" ),
		Head2_Head1_Bod6chunk45_Bod6chunk37( "Head2", "Head1", "bod6chunk45", "bod6chunk37" ),
		Head2_Head1_Bod6chunk45_Bod6chunk36( "Head2", "Head1", "bod6chunk45", "bod6chunk36" ),
		Head2_Head1_Bod6chunk45( "Head2", "Head1", "bod6chunk45" ),
		Head2_Head1( "Head2", "Head1" ),
		Head2_Headchunk04( "Head2", "headchunk04" ),
		Head2_Headchunk06( "Head2", "headchunk06" ),
		Head2_Headchunk05( "Head2", "headchunk05" ),
		Head2_Headchunk07( "Head2", "headchunk07" ),
		Head2_Headchunk08( "Head2", "headchunk08" ),
		Head2_Headchunk03( "Head2", "headchunk03" ),
		Head2_Headchunk02( "Head2", "headchunk02" ),
		Head2_Headchunk1( "Head2", "headchunk1" ),
		Head2_Headchunk20( "Head2", "headchunk20" ),
		Head2_Headchunk15( "Head2", "headchunk15" ),
		Head2_Headchunk16( "Head2", "headchunk16" ),
		Head2_Headchunk17( "Head2", "headchunk17" ),
		Head2_Headchunk18( "Head2", "headchunk18" ),
		Head2_Headchunk19( "Head2", "headchunk19" ),
		Head2_Headchunk14( "Head2", "headchunk14" ),
		Head2_Headchunk13( "Head2", "headchunk13" ),
		Head2_Headchunk12( "Head2", "headchunk12" ),
		Head2_Headchunk09( "Head2", "headchunk09" ),
		Head2_Headchunk10( "Head2", "headchunk10" ),
		Head2_Headchunk11( "Head2", "headchunk11" ),
		Head2_Bod6chunk05_Bod6chunk04( "Head2", "bod6chunk05", "bod6chunk04" ),
		Head2_Bod6chunk05_Bod6chunk25( "Head2", "bod6chunk05", "bod6chunk25" ),
		Head2_Bod6chunk05_Bod6chunk34( "Head2", "bod6chunk05", "bod6chunk34" ),
		Head2_Bod6chunk05_Bod6chunk33( "Head2", "bod6chunk05", "bod6chunk33" ),
		Head2_Bod6chunk05_Bod6chunk24( "Head2", "bod6chunk05", "bod6chunk24" ),
		Head2_Bod6chunk05_Bod6chunk23( "Head2", "bod6chunk05", "bod6chunk23" ),
		Head2_Bod6chunk05_Bod6chunk22( "Head2", "bod6chunk05", "bod6chunk22" ),
		Head2_Bod6chunk05_Bod6chunk16( "Head2", "bod6chunk05", "bod6chunk16" ),
		Head2_Bod6chunk05_Bod6chunk18( "Head2", "bod6chunk05", "bod6chunk18" ),
		Head2_Bod6chunk05_Bod6chunk31( "Head2", "bod6chunk05", "bod6chunk31" ),
		Head2_Bod6chunk05_Bod6chunk1( "Head2", "bod6chunk05", "bod6chunk1" ),
		Head2_Bod6chunk05_Bod6chunk32( "Head2", "bod6chunk05", "bod6chunk32" ),
		Head2_Bod6chunk05_Bod6chunk30( "Head2", "bod6chunk05", "bod6chunk30" ),
		Head2_Bod6chunk05_Bod6chunk02( "Head2", "bod6chunk05", "bod6chunk02" ),
		Head2_Bod6chunk05_Bod6chunk28( "Head2", "bod6chunk05", "bod6chunk28" ),
		Head2_Bod6chunk05_Bod6chunk35( "Head2", "bod6chunk05", "bod6chunk35" ),
		Head2_Bod6chunk05_Bod6chunk21( "Head2", "bod6chunk05", "bod6chunk21" ),
		Head2_Bod6chunk05_Bod6chunk17( "Head2", "bod6chunk05", "bod6chunk17" ),
		Head2_Bod6chunk05_Bod6chunk19( "Head2", "bod6chunk05", "bod6chunk19" ),
		Head2_Bod6chunk05_Bod6chunk17a( "Head2", "bod6chunk05", "bod6chunk17a" ),
		Head2_Bod6chunk05_Bod6chunk29( "Head2", "bod6chunk05", "bod6chunk29" ),
		Head2_Bod6chunk05_Bod6chunk13( "Head2", "bod6chunk05", "bod6chunk13" ),
		Head2_Bod6chunk05_Bod6chunk12( "Head2", "bod6chunk05", "bod6chunk12" ),
		Head2_Bod6chunk05_Bod6chunk20( "Head2", "bod6chunk05", "bod6chunk20" ),
		Head2_Bod6chunk05_Bod6chunk10( "Head2", "bod6chunk05", "bod6chunk10" ),
		Head2_Bod6chunk05_Bod6chunk15( "Head2", "bod6chunk05", "bod6chunk15" ),
		Head2_Bod6chunk05_Bod6chunk07( "Head2", "bod6chunk05", "bod6chunk07" ),
		Head2_Bod6chunk05_Bod6chunk09( "Head2", "bod6chunk05", "bod6chunk09" ),
		Head2_Bod6chunk05( "Head2", "bod6chunk05" ),
		Head2( "Head2" ),
		Body2_BRLeg01_BRLeg( "Body2", "BRLeg01", "BRLeg" ),
		Body2_BRLeg01( "Body2", "BRLeg01" ),
		Body2_BLLeg_BLLeg01( "Body2", "BLLeg", "BLLeg01" ),
		Body2_BLLeg( "Body2", "BLLeg" ),
		Body2_Bod1chunk14_Bod1chunk10( "Body2", "bod1chunk14", "bod1chunk10" ),
		Body2_Bod1chunk14_Bod1chunk11( "Body2", "bod1chunk14", "bod1chunk11" ),
		Body2_Bod1chunk14_Bod1chunk12( "Body2", "bod1chunk14", "bod1chunk12" ),
		Body2_Bod1chunk14_Bod1chunk13( "Body2", "bod1chunk14", "bod1chunk13" ),
		Body2_Bod1chunk14_Bod1chunk16( "Body2", "bod1chunk14", "bod1chunk16" ),
		Body2_Bod1chunk14_Bod1chunk15( "Body2", "bod1chunk14", "bod1chunk15" ),
		Body2_Bod1chunk14_Bod1chunk17( "Body2", "bod1chunk14", "bod1chunk17" ),
		Body2_Bod1chunk14_Bod1chunk18( "Body2", "bod1chunk14", "bod1chunk18" ),
		Body2_Bod1chunk14_Bod1chunk19( "Body2", "bod1chunk14", "bod1chunk19" ),
		Body2_Bod1chunk14_Bod1chunk20( "Body2", "bod1chunk14", "bod1chunk20" ),
		Body2_Bod1chunk14_Bod1chunk21( "Body2", "bod1chunk14", "bod1chunk21" ),
		Body2_Bod1chunk14_Bod1chunk09( "Body2", "bod1chunk14", "bod1chunk09" ),
		Body2_Bod1chunk14( "Body2", "bod1chunk14" ),
		Body2_Bod4chunk5_Bod4chunk4( "Body2", "bod4chunk5", "bod4chunk4" ),
		Body2_Bod4chunk5_Bod4chunk8( "Body2", "bod4chunk5", "bod4chunk8" ),
		Body2_Bod4chunk5_Bod4chunk9( "Body2", "bod4chunk5", "bod4chunk9" ),
		Body2_Bod4chunk5_Bod4chunk10( "Body2", "bod4chunk5", "bod4chunk10" ),
		Body2_Bod4chunk5_Bod4chunk11( "Body2", "bod4chunk5", "bod4chunk11" ),
		Body2_Bod4chunk5_Bod4chunk12( "Body2", "bod4chunk5", "bod4chunk12" ),
		Body2_Bod4chunk5_Bod4chunk7( "Body2", "bod4chunk5", "bod4chunk7" ),
		Body2_Bod4chunk5_Bod4chunk6( "Body2", "bod4chunk5", "bod4chunk6" ),
		Body2_Bod4chunk5_Bod4chunk3( "Body2", "bod4chunk5", "bod4chunk3" ),
		Body2_Bod4chunk5_Bod4chunk2( "Body2", "bod4chunk5", "bod4chunk2" ),
		Body2_Bod4chunk5_Bod4chunk1( "Body2", "bod4chunk5", "bod4chunk1" ),
		Body2_Bod4chunk5( "Body2", "bod4chunk5" ),
		Body2_Bod5chunk29_Bod5chunk28( "Body2", "bod5chunk29", "bod5chunk28" ),
		Body2_Bod5chunk29_Bod5chunk36( "Body2", "bod5chunk29", "bod5chunk36" ),
		Body2_Bod5chunk29_Bod5chunk27( "Body2", "bod5chunk29", "bod5chunk27" ),
		Body2_Bod5chunk29_Bod5chunk14( "Body2", "bod5chunk29", "bod5chunk14" ),
		Body2_Bod5chunk29_Bod5chunk16( "Body2", "bod5chunk29", "bod5chunk16" ),
		Body2_Bod5chunk29_Bod5chunk25( "Body2", "bod5chunk29", "bod5chunk25" ),
		Body2_Bod5chunk29_Bod5chunk12( "Body2", "bod5chunk29", "bod5chunk12" ),
		Body2_Bod5chunk29_Bod5chunk11( "Body2", "bod5chunk29", "bod5chunk11" ),
		Body2_Bod5chunk29_Bod5chunk6( "Body2", "bod5chunk29", "bod5chunk6" ),
		Body2_Bod5chunk29_Bod5chunk13( "Body2", "bod5chunk29", "bod5chunk13" ),
		Body2_Bod5chunk29_Bod5chunk17( "Body2", "bod5chunk29", "bod5chunk17" ),
		Body2_Bod5chunk29_Bod5chunk34( "Body2", "bod5chunk29", "bod5chunk34" ),
		Body2_Bod5chunk29_Bod5chunk8( "Body2", "bod5chunk29", "bod5chunk8" ),
		Body2_Bod5chunk29_Bod5chunk22( "Body2", "bod5chunk29", "bod5chunk22" ),
		Body2_Bod5chunk29_Bod5chunk24( "Body2", "bod5chunk29", "bod5chunk24" ),
		Body2_Bod5chunk29_Bod5chunk26( "Body2", "bod5chunk29", "bod5chunk26" ),
		Body2_Bod5chunk29_Bod5chunk23( "Body2", "bod5chunk29", "bod5chunk23" ),
		Body2_Bod5chunk29_Bod5chunk32( "Body2", "bod5chunk29", "bod5chunk32" ),
		Body2_Bod5chunk29_Bod5chunk35( "Body2", "bod5chunk29", "bod5chunk35" ),
		Body2_Bod5chunk29_Bod5chunk30( "Body2", "bod5chunk29", "bod5chunk30" ),
		Body2_Bod5chunk29_Bod5chunk15( "Body2", "bod5chunk29", "bod5chunk15" ),
		Body2_Bod5chunk29_Bod5chunk3( "Body2", "bod5chunk29", "bod5chunk3" ),
		Body2_Bod5chunk29_Bod5chunk18( "Body2", "bod5chunk29", "bod5chunk18" ),
		Body2_Bod5chunk29_Bod5chunk19( "Body2", "bod5chunk29", "bod5chunk19" ),
		Body2_Bod5chunk29_Bod5chunk20( "Body2", "bod5chunk29", "bod5chunk20" ),
		Body2_Bod5chunk29_Bod5chunk21( "Body2", "bod5chunk29", "bod5chunk21" ),
		Body2_Bod5chunk29_Bod5chunk7( "Body2", "bod5chunk29", "bod5chunk7" ),
		Body2_Bod5chunk29_Bod5chunk10( "Body2", "bod5chunk29", "bod5chunk10" ),
		Body2_Bod5chunk29_Bod5chunk33( "Body2", "bod5chunk29", "bod5chunk33" ),
		Body2_Bod5chunk29_Bod5chunk31( "Body2", "bod5chunk29", "bod5chunk31" ),
		Body2_Bod5chunk29_Bod5chunk2( "Body2", "bod5chunk29", "bod5chunk2" ),
		Body2_Bod5chunk29_Bod5chunk9( "Body2", "bod5chunk29", "bod5chunk9" ),
		Body2_Bod5chunk29_Bod5chunk1( "Body2", "bod5chunk29", "bod5chunk1" ),
		Body2_Bod5chunk29_Bod5chunk4( "Body2", "bod5chunk29", "bod5chunk4" ),
		Body2_Bod5chunk29_Bod5chunk5( "Body2", "bod5chunk29", "bod5chunk5" ),
		Body2_Bod5chunk29( "Body2", "bod5chunk29" ),
		Body2( "Body2" ),
		FLLeg01_Bottom( "FLLeg01", "Bottom" ),
		FLLeg01( "FLLeg01" ),
		FRLeg01_Bottom( "FRLeg01", "Bottom" ),
		FRLeg01( "FRLeg01" ),
		Bod2chunk5_Bod2chunk3( "bod2chunk5", "bod2chunk3" ),
		Bod2chunk5_Bod2chunk2( "bod2chunk5", "bod2chunk2" ),
		Bod2chunk5_Bod2chunk9( "bod2chunk5", "bod2chunk9" ),
		Bod2chunk5_Bod2chunk12( "bod2chunk5", "bod2chunk12" ),
		Bod2chunk5_Bod2chunk8( "bod2chunk5", "bod2chunk8" ),
		Bod2chunk5_Bod2chunk6( "bod2chunk5", "bod2chunk6" ),
		Bod2chunk5_Bod2chunk10( "bod2chunk5", "bod2chunk10" ),
		Bod2chunk5_Bod2chunk11( "bod2chunk5", "bod2chunk11" ),
		Bod2chunk5_Bod2chunk7( "bod2chunk5", "bod2chunk7" ),
		Bod2chunk5_Bod2chunk1( "bod2chunk5", "bod2chunk1" ),
		Bod2chunk5_Bod2chunk4( "bod2chunk5", "bod2chunk4" ),
		Bod2chunk5( "bod2chunk5" ),
		Bod3chunk5_Bod3chunk1( "bod3chunk5", "bod3chunk1" ),
		Bod3chunk5_Bod3chunk2( "bod3chunk5", "bod3chunk2" ),
		Bod3chunk5_Bod3chunk9( "bod3chunk5", "bod3chunk9" ),
		Bod3chunk5_Bod3chunk7( "bod3chunk5", "bod3chunk7" ),
		Bod3chunk5_Bod3chunk8( "bod3chunk5", "bod3chunk8" ),
		Bod3chunk5_Bod3chunk11( "bod3chunk5", "bod3chunk11" ),
		Bod3chunk5_Bod3chunk12( "bod3chunk5", "bod3chunk12" ),
		Bod3chunk5_Bod3chunk10( "bod3chunk5", "bod3chunk10" ),
		Bod3chunk5_Bod3chunk4( "bod3chunk5", "bod3chunk4" ),
		Bod3chunk5_Bod3chunk6( "bod3chunk5", "bod3chunk6" ),
		Bod3chunk5_Bod3chunk3( "bod3chunk5", "bod3chunk3" ),
		Bod3chunk5( "bod3chunk5" );
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
