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
package org.alice.apis.moveandturn.gallery.nature;
	
public class Flower extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Flower() {
		super( "Nature/flower" );
	}
	public enum Part {
		Blade4( "blade4" ),
		Blade3( "blade3" ),
		Blade5( "blade5" ),
		Blade1( "blade1" ),
		Blade2( "blade2" ),
		Blade6( "blade6" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal16( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal16" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal15( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal15" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal14( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal14" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal13( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal13" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal05( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal05" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal12( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal12" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal07( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal07" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal04( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal04" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal10( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal10" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal06( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal06" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal03( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal03" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal11( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal11" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal02( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal02" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal01( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal01" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal09( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal09" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal08( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal08" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal17( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal17" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal18( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal18" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal19( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal19" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead_Petal20( "mainstem3", "MainStem2", "MainStem1", "FlowerHead", "petal20" ),
		Mainstem3_MainStem2_MainStem1_FlowerHead( "mainstem3", "MainStem2", "MainStem1", "FlowerHead" ),
		Mainstem3_MainStem2_MainStem1( "mainstem3", "MainStem2", "MainStem1" ),
		Mainstem3_MainStem2( "mainstem3", "MainStem2" ),
		Mainstem3( "mainstem3" );
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
