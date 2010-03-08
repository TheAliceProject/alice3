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
package org.alice.apis.moveandturn.gallery.hawaii.people;
	
public class ChiefessHeaddress extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public ChiefessHeaddress() {
		super( "Hawaii/People/ChiefessHeaddress" );
	}
	public enum Part {
		Circle01( "Circle01" ),
		Circle04( "Circle04" ),
		Circle05( "Circle05" ),
		Circle08( "Circle08" ),
		Circle11( "Circle11" ),
		Circle12( "Circle12" ),
		Circle13( "Circle13" ),
		Circle15( "Circle15" ),
		Circle16( "Circle16" ),
		Circle20( "Circle20" ),
		Circle21( "Circle21" ),
		Circle24( "Circle24" ),
		Circle25( "Circle25" ),
		Circle27( "Circle27" ),
		Circle28( "Circle28" ),
		Circle32( "Circle32" ),
		Circle35( "Circle35" ),
		Circle36( "Circle36" ),
		Circle37( "Circle37" ),
		Circle40( "Circle40" ),
		Circle41( "Circle41" ),
		Circle45( "Circle45" ),
		Circle46( "Circle46" ),
		Circle50( "Circle50" ),
		Circle53( "Circle53" ),
		Circle54( "Circle54" ),
		Circle55( "Circle55" ),
		Circle57( "Circle57" ),
		Circle58( "Circle58" ),
		Circle61( "Circle61" ),
		Circle65( "Circle65" ),
		Circle66( "Circle66" ),
		Circle67( "Circle67" );
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
