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
	
public class Lake extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Lake() {
		super( "Environments/lake" );
	}
	public enum Part {
		Treehigh( "treehigh" ),
		Ice( "ice" ),
		Sky( "sky" ),
		Cabin( "cabin" ),
		Groundsnow( "groundsnow" ),
		Tree( "tree" ),
		Tree01( "tree01" ),
		Tree02( "tree02" ),
		Tree03( "tree03" ),
		Tree04( "tree04" ),
		Tree05( "tree05" ),
		Tree06( "tree06" ),
		Tree07( "tree07" ),
		Tree08( "tree08" ),
		Tree09( "tree09" ),
		Tree10( "tree10" ),
		Tree11( "tree11" ),
		Tree12( "tree12" ),
		Tree13( "tree13" ),
		Tree14( "tree14" ),
		Tree15( "tree15" ),
		Tree16( "tree16" ),
		Tree17( "tree17" ),
		Tree18( "tree18" ),
		Tree19( "tree19" ),
		Tree20( "tree20" ),
		Tree21( "tree21" ),
		Tree22( "tree22" ),
		Tree23( "tree23" ),
		Tree24( "tree24" ),
		Tree25( "tree25" ),
		Tree26( "tree26" ),
		Tree27( "tree27" ),
		Tree28( "tree28" ),
		Tree29( "tree29" ),
		Tree30( "tree30" ),
		Tree31( "tree31" ),
		Tree32( "tree32" ),
		Tree33( "tree33" ),
		Tree34( "tree34" ),
		Tree35( "tree35" ),
		Tree36( "tree36" ),
		Tree37( "tree37" ),
		Tree38( "tree38" ),
		Tree39( "tree39" ),
		Tree40( "tree40" ),
		Tree41( "tree41" ),
		Tree42( "tree42" ),
		Tree43( "tree43" ),
		Tree44( "tree44" ),
		Tree45( "tree45" ),
		Tree46( "tree46" ),
		Tree47( "tree47" ),
		Tree48( "tree48" ),
		Tree49( "tree49" ),
		Tree50( "tree50" ),
		Tree51( "tree51" ),
		Tree52( "tree52" ),
		Tree53( "tree53" ),
		Tree54( "tree54" ),
		Tree55( "tree55" ),
		Tree56( "tree56" ),
		Tree57( "tree57" ),
		Tree58( "tree58" ),
		Tree77( "tree77" ),
		Tree78( "tree78" ),
		Tree79( "tree79" ),
		Tree82( "tree82" ),
		Tree83( "tree83" ),
		Tree85( "tree85" ),
		Tree86( "tree86" ),
		Tree87( "tree87" ),
		Tree88( "tree88" ),
		Tree89( "tree89" ),
		Tree90( "tree90" ),
		Tree91( "tree91" ),
		Tree92( "tree92" ),
		Tree93( "tree93" ),
		Tree94( "tree94" ),
		Treehigh01( "treehigh01" ),
		Treehigh02( "treehigh02" ),
		Treehigh03( "treehigh03" ),
		Treehigh04( "treehigh04" ),
		Treehigh05( "treehigh05" ),
		Treehigh06( "treehigh06" ),
		Treehigh07( "treehigh07" ),
		Treehigh08( "treehigh08" ),
		Treehigh09( "treehigh09" ),
		Treehigh10( "treehigh10" ),
		Treehigh11( "treehigh11" ),
		Treehigh12( "treehigh12" ),
		Treehigh13( "treehigh13" ),
		Treehigh14( "treehigh14" ),
		Treehigh15( "treehigh15" ),
		Treehigh16( "treehigh16" ),
		Treehigh17( "treehigh17" ),
		Treehigh18( "treehigh18" ),
		Treehigh19( "treehigh19" ),
		Tree95( "tree95" ),
		Tree96( "tree96" ),
		Tree97( "tree97" ),
		Tree98( "tree98" ),
		Tree99( "tree99" ),
		Tree100( "tree100" ),
		Tree101( "tree101" ),
		Tree102( "tree102" ),
		Tree103( "tree103" ),
		Tree104( "tree104" ),
		Tree105( "tree105" ),
		Tree106( "tree106" ),
		Tree107( "tree107" ),
		Tree108( "tree108" ),
		Tree109( "tree109" ),
		Tree110( "tree110" ),
		Tree111( "tree111" ),
		Treehigh20( "treehigh20" ),
		Tree112( "tree112" ),
		Tree113( "tree113" ),
		Tree114( "tree114" ),
		Treehigh21( "treehigh21" ),
		Tree115( "tree115" ),
		Tree116( "tree116" ),
		Tree117( "tree117" ),
		Tree118( "tree118" ),
		Tree119( "tree119" ),
		Tree120( "tree120" ),
		Tree121( "tree121" );
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
