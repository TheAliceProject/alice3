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
package org.alice.ide.croquet.models.project;

import java.util.LinkedList;
import java.util.Map;

import org.alice.ide.ProjectApplication;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.Operation;
import org.lgna.croquet.TabComposite;
import org.lgna.croquet.TabSelectionState;
import org.lgna.croquet.components.Container;
import org.lgna.croquet.components.Dialog;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.UserMethod;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Dennis Cosgrove
 */
public class SearchOperation extends org.lgna.croquet.InformationDialogOperation {

	private static class SingletonHolder {
		private static SearchOperation instance = new SearchOperation();
	}

	public static Operation getInstance() {
		return SingletonHolder.instance;
	}

	private SearchOperation() {
		super( java.util.UUID.fromString( "b34e805e-e6ef-4f08-af53-df98e1653732" ) );
	}
	@Override
	protected Container<?> createContentPane( CompletionStep<?> step, Dialog dialog ) {
		final Map<UserMethod,LinkedList<MethodInvocation>> methodParentMap = Collections.newHashMap();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.ast.NamedUserType programType = ide.getStrippedProgramType();
		if( programType != null ) {
			class StatementCountCrawler implements edu.cmu.cs.dennisc.pattern.Crawler {
				private java.util.Map<Class<? extends org.lgna.project.ast.Statement>,Integer> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

				public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
					if( crawlable instanceof MethodInvocation ) {
						MethodInvocation methodInvocation = (MethodInvocation)crawlable;
						UserMethod method = methodInvocation.getFirstAncestorAssignableTo( UserMethod.class );
						if( methodParentMap.get( method ) == null ) {
							methodParentMap.put( method, new LinkedList<MethodInvocation>() );
						}
						methodParentMap.get( method ).add( methodInvocation );
					}
				}
			}
			StatementCountCrawler crawler = new StatementCountCrawler();
			programType.crawl( crawler, true );
			SearchComposite searchDialog = new SearchComposite( methodParentMap );
			ReferencesComposite refDialog = new ReferencesComposite( methodParentMap );
			searchDialog.addSelectedListener( refDialog );
			TabSelectionState<TabComposite<?>> state = new TabSelectionState<TabComposite<?>>( ProjectApplication.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "6f6d1d21-dcd3-4c79-a2f8-7b9b7677f64d" ), new ItemCodec<TabComposite<?>>() {

				public Class<TabComposite<?>> getValueClass() {
					return null;
				}

				public TabComposite<?> decodeValue( BinaryDecoder binaryDecoder ) {
					return null;
				}

				public void encodeValue( BinaryEncoder binaryEncoder, TabComposite<?> value ) {
				}

				public StringBuilder appendRepresentation( StringBuilder rv, TabComposite<?> value ) {
					return null;
				}
			} );
			state.addItem( searchDialog );
			state.addItem( refDialog );
			state.setSelectedIndex( 0 );
			return state.createFolderTabbedPane();
		} else {
			return new org.lgna.croquet.components.Label( "open a project first" );
		}
	}
	@Override
	protected void releaseContentPane( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.components.Dialog dialog, org.lgna.croquet.components.Container<?> contentPane ) {
	}
}
