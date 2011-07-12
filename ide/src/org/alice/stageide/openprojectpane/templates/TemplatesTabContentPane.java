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
package org.alice.stageide.openprojectpane.templates;

public class TemplatesTabContentPane extends org.alice.ide.openprojectpane.ListContentPanel {
	private static java.net.URI[] uris;
	static {
		java.util.List< java.net.URI > list = new java.util.LinkedList< java.net.URI >();
		String[] resourceNames = { "DirtProject.a3p", "GrassyProject.a3p", "MoonProject.a3p", "SandyProject.a3p", "SeaProject.a3p", "SnowyProject.a3p" };
		for( String resourceName : resourceNames ) {
			java.net.URI uri = getURI( resourceName );
			if( uri != null ) {
				list.add( uri );
			}
		}
		TemplatesTabContentPane.uris = new java.net.URI[ list.size() ];
		list.toArray( TemplatesTabContentPane.uris );
	}

	private static java.net.URI getURI( String resourceName ) {
		java.io.File applicationRootDirectory = org.alice.ide.IDE.getActiveInstance().getApplicationRootDirectory();
		//java.io.File applicationRootDirectory = new java.io.File( "c:/Program Files/Alice3Beta" );
		java.io.File file = new java.io.File( applicationRootDirectory, "projects/templates/" + resourceName );
		if( file != null ) {
			if( file.exists() ) {
				return file.toURI();
			} else {
				return null;
			}
		} else {
			return null;
		}
//		java.net.URL url = TemplatesTabContentPane.class.getResource( resourceName );
//		if( url != null ) {
//			try {
//				return url.toURI();
//			} catch( java.net.URISyntaxException urise ) {
//				return null;
//			}
//		} else {
//			return null;
//		}
	}
	@Override
	protected String getTextForZeroProjects() {
		return "there are no template projects.";
	}
	@Override
	protected java.net.URI[] getURIs() {
		return TemplatesTabContentPane.uris;
	}
}
