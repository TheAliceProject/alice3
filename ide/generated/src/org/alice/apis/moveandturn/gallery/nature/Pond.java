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
	
public class Pond extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Pond() {
		super( "Nature/pond" );
	}
	public enum Part {
		Leaf02( "leaf02" ),
		Flower09( "flower09" ),
		Petal16( "petal16" ),
		Flower04( "flower04" ),
		Flower( "flower" ),
		Flower05( "flower05" ),
		Flower06( "flower06" ),
		Flower07( "flower07" ),
		Flower08( "flower08" ),
		Flower11( "flower11" ),
		Flower12( "flower12" ),
		Flower14( "flower14" ),
		Flower15( "flower15" ),
		Flower16( "flower16" ),
		Flower17( "flower17" ),
		Flower18( "flower18" ),
		Flower19( "flower19" ),
		Flower21( "flower21" ),
		Flower23( "flower23" ),
		Flower24( "flower24" ),
		Flower25( "flower25" ),
		Flower26( "flower26" ),
		Flower27( "flower27" ),
		Flower28( "flower28" ),
		Flower29( "flower29" ),
		Flower30( "flower30" ),
		Flower31( "flower31" ),
		Flower32( "flower32" ),
		Petal18( "petal18" ),
		Petal01( "petal01" ),
		Grasss01( "grasss01" ),
		Flowerz01( "flowerz01" ),
		Reed03( "reed03" ),
		Pod02( "pod02" ),
		Reed04( "reed04" ),
		Pod03( "pod03" ),
		Leaf03( "leaf03" ),
		Tlimb02( "tlimb02" ),
		Petal81( "petal81" ),
		Petal82( "petal82" ),
		Petal83( "petal83" ),
		Petal84( "petal84" ),
		Petal85( "petal85" ),
		Petal86( "petal86" ),
		Petal87( "petal87" ),
		Bush( "bush" ),
		Petal89( "petal89" ),
		Petal91( "petal91" ),
		Petal92( "petal92" ),
		Petal93( "petal93" ),
		Petal94( "petal94" ),
		Petal95( "petal95" ),
		Petal96( "petal96" ),
		Petal21( "petal21" ),
		Rock02( "rock02" ),
		Rock03( "rock03" ),
		Rock21( "rock21" ),
		Rock22( "rock22" ),
		Rock23( "rock23" ),
		Rock24( "rock24" ),
		Rock25( "rock25" ),
		Rock26( "rock26" ),
		Leaf20( "leaf20" ),
		Leaf101( "leaf101" ),
		Leaf97( "leaf97" ),
		Leaf98( "leaf98" ),
		Leaf99( "leaf99" ),
		Grasss02( "grasss02" ),
		Leaf04( "leaf04" ),
		Flowerz02( "flowerz02" ),
		Pool( "pool" );
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
