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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import org.alice.ide.croquet.models.project.views.FlowControlFrequencyView;
import org.lgna.croquet.DefaultListSelectionState;
import org.lgna.croquet.ItemCodec;
import org.lgna.croquet.ListSelectionState;
import org.lgna.croquet.State;
import org.lgna.croquet.State.ValueListener;
import org.lgna.croquet.TabComposite;
import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.HorizontalAlignment;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.ScrollPane;
import org.lgna.croquet.components.View;
import org.lgna.project.ast.AbstractEachInTogether;
import org.lgna.project.ast.AbstractForEachLoop;
import org.lgna.project.ast.ConditionalStatement;
import org.lgna.project.ast.CountLoop;
import org.lgna.project.ast.DoInOrder;
import org.lgna.project.ast.DoTogether;
import org.lgna.project.ast.LocalDeclarationStatement;
import org.lgna.project.ast.ReturnStatement;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.WhileLoop;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;
import edu.cmu.cs.dennisc.java.util.Collections;

public class FlowControlFrequencyComposite extends TabComposite<View<?,?>> {
	private Map<UserMethod,List<Statement>> methodToConstructMap;
	private final ListSelectionState<UserMethod> userMethodList = createListSelectionState( createKey( "userMethodList" ), UserMethod.class, org.alice.ide.croquet.codecs.NodeCodec.getInstance( UserMethod.class ), -1 );
	public static UserMethod dummy = new UserMethod();

	public FlowControlFrequencyComposite() {
		super( java.util.UUID.fromString( "b12770d1-e65e-430f-92a1-dc3159a85a7b" ) );
		methodToConstructMap = Collections.newHashMap();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.ast.NamedUserType programType = ide.getStrippedProgramType();

		StatementCountCrawler crawler = new StatementCountCrawler();
		List<UserMethod> a = new LinkedList<UserMethod>();
		programType.crawl( crawler, true );
		for( UserMethod method : methodToConstructMap.keySet() ) {
			a.add( method );
		}
		//		final GridPanel rv = GridPanel.createGridPane( 2, 1 );

		//		userMethodList = new DefaultListSelectionState<UserMethod>( ProjectApplication.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "06b77424-763b-4fdc-a1cb-1404eaefa1d2" ), codec );
		java.util.Collections.sort( a, new Comparator<UserMethod>() {
			public int compare( UserMethod o1, UserMethod o2 ) {
				return o1.getName().compareTo( o2.getName() );
			}
		} );
		getUserMethodList().addItem( dummy );
		for( UserMethod method : a ) {
			getUserMethodList().addItem( method );
		}
		dummy.setName( "Project" );
	}
	@Override
	public boolean isCloseable() {
		return false;
	}

	@Override
	protected View<?,?> createView() {
		return new FlowControlFrequencyView( this );
	}

	public Map<UserMethod,List<Statement>> getMethodToConstructMap() {
		return this.methodToConstructMap;
	}

	public ListSelectionState<UserMethod> getUserMethodList() {
		return this.userMethodList;
	}

	private class StatementCountCrawler implements edu.cmu.cs.dennisc.pattern.Crawler {
		private java.util.Map<Class<? extends org.lgna.project.ast.Statement>,Integer> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

		public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
			if( crawlable instanceof org.lgna.project.ast.Statement ) {
				org.lgna.project.ast.Statement statement = (org.lgna.project.ast.Statement)crawlable;
				UserMethod method = statement.getFirstAncestorAssignableTo( UserMethod.class );
				if( method != null && !method.getManagementLevel().isGenerated()
				// This condition prevents counting methods of the Program class (e.g., main method) which a user cannot edit
						&& !(method.getDeclaringType().isAssignableTo( org.lgna.story.Program.class )) && statement.isEnabled.getValue() ) {
					if( !methodToConstructMap.keySet().contains( method ) ) {
						methodToConstructMap.put( method, new LinkedList<Statement>() );
					}
					methodToConstructMap.get( method ).add( statement );
				}
			}
		}
	}

	public int getCount( UserMethod method, Class<? extends Statement> cls ) {
		int count = 0;
		if( !method.equals( FlowControlFrequencyComposite.dummy ) ) {
			for( Statement statement : methodToConstructMap.get( method ) ) {
				if( statement.getClass().isAssignableFrom( cls ) ) {
					++count;
				}
			}
		} else {
			for( UserMethod userMethod : methodToConstructMap.keySet() ) {
				for( Statement statement : methodToConstructMap.get( userMethod ) ) {
					if( statement.getClass().isAssignableFrom( cls ) ) {
						++count;
					}
				}
			}
		}
		return count;
	}

	public int getMaximum(Class[] clsArr) {
		int maxCount = 0;
		for( Class cls : clsArr ) {
			int count = getCount( FlowControlFrequencyComposite.dummy, cls );
			if( count > maxCount ) {
				maxCount = count;
			}
		}
		return maxCount;
	}

}