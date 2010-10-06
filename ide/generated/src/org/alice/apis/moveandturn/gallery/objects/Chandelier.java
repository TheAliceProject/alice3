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
	
public class Chandelier extends org.alice.apis.moveandturn.gallery.GalleryModel {
	public Chandelier() {
		super( "Objects/chandelier" );
	}
	public enum Part {
		Strut5( "Strut5" ),
		Strut6( "Strut6" ),
		Strut7( "Strut7" ),
		Strut8( "Strut8" ),
		Strut1( "Strut1" ),
		Strut2( "Strut2" ),
		Strut3( "Strut3" ),
		Strut4( "Strut4" ),
		Lamp5( "Lamp5" ),
		Lamp6( "Lamp6" ),
		Lamp7( "Lamp7" ),
		Lamp8( "Lamp8" ),
		Lamp1( "Lamp1" ),
		Lamp2( "Lamp2" ),
		Lamp3( "Lamp3" ),
		Lamp4( "Lamp4" ),
		CChain_WCSmall_LittleS8( "CChain", "WCSmall", "LittleS8" ),
		CChain_WCSmall_LittleS1( "CChain", "WCSmall", "LittleS1" ),
		CChain_WCSmall_LittleS2( "CChain", "WCSmall", "LittleS2" ),
		CChain_WCSmall_LittleS3( "CChain", "WCSmall", "LittleS3" ),
		CChain_WCSmall_LittleS4( "CChain", "WCSmall", "LittleS4" ),
		CChain_WCSmall_LittleS5( "CChain", "WCSmall", "LittleS5" ),
		CChain_WCSmall_LittleS6( "CChain", "WCSmall", "LittleS6" ),
		CChain_WCSmall_LittleS7( "CChain", "WCSmall", "LittleS7" ),
		CChain_WCSmall( "CChain", "WCSmall" ),
		CChain( "CChain" ),
		RChain( "RChain" ),
		LChain( "LChain" );
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
