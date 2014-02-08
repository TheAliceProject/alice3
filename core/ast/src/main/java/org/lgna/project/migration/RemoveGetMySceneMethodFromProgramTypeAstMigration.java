/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.project.migration;

/**
 * @author Dennis Cosgrove
 */
public class RemoveGetMySceneMethodFromProgramTypeAstMigration extends AstMigration {
	public RemoveGetMySceneMethodFromProgramTypeAstMigration( org.lgna.project.Version minimumVersion, org.lgna.project.Version resultVersion ) {
		super( minimumVersion, resultVersion );
	}

	@Override
	public void migrate( org.lgna.project.ast.Node node ) {
		if( node instanceof org.lgna.project.ast.NamedUserType ) {
			org.lgna.project.ast.NamedUserType type = (org.lgna.project.ast.NamedUserType)node;
			org.lgna.project.ast.UserMethod mainMethod = type.getDeclaredMethod( "main", String[].class );
			if( mainMethod != null ) {
				final org.lgna.project.ast.UserField mySceneField = type.getDeclaredField( "myScene" );
				final org.lgna.project.ast.UserMethod getMySceneMethod = type.getDeclaredMethod( "getMyScene" );
				if( ( mySceneField != null ) && ( getMySceneMethod != null ) ) {
					node.crawl( new edu.cmu.cs.dennisc.pattern.Crawler() {
						public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
							if( crawlable instanceof org.lgna.project.ast.MethodInvocation ) {
								org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)crawlable;
								if( methodInvocation.method.getValue() == getMySceneMethod ) {
									methodInvocation.method.setValue( mySceneField.getGetter() );
									edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "replacing", getMySceneMethod, "with", mySceneField.getGetter() );
								}
							}
						}
					}, org.lgna.project.ast.CrawlPolicy.COMPLETE, null );
					type.methods.remove( type.methods.indexOf( getMySceneMethod ) );
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "removing", getMySceneMethod );
				}
			}
		}
	}
}
