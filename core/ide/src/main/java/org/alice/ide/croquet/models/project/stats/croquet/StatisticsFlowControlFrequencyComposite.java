/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.croquet.models.project.stats.croquet;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.alice.ide.croquet.models.project.stats.croquet.views.StatisticsFlowControlFrequencyView;
import org.lgna.croquet.MutableDataSingleSelectListState;
import org.lgna.croquet.SimpleTabComposite;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserMethod;

import edu.cmu.cs.dennisc.java.util.Maps;

public class StatisticsFlowControlFrequencyComposite extends SimpleTabComposite<StatisticsFlowControlFrequencyView> {
	private final Map<UserMethod, List<Statement>> methodToConstructMap = Maps.newHashMap();
	private final MutableDataSingleSelectListState<UserMethod> userMethodList = createMutableListState( "userMethodList", UserMethod.class, org.alice.ide.croquet.codecs.NodeCodec.getInstance( UserMethod.class ), -1 );
	public final static UserMethod root = new UserMethod();

	public StatisticsFlowControlFrequencyComposite() {
		super( java.util.UUID.fromString( "b12770d1-e65e-430f-92a1-dc3159a85a7b" ), IsCloseable.FALSE );
		root.setName( "Project" );
		refresh();
	}

	@Override
	protected StatisticsFlowControlFrequencyView createView() {
		return new StatisticsFlowControlFrequencyView( this );
	}

	public Map<UserMethod, List<Statement>> getMethodToConstructMap() {
		return this.methodToConstructMap;
	}

	public MutableDataSingleSelectListState<UserMethod> getUserMethodList() {
		return this.userMethodList;
	}

	private class StatementCountCrawler implements edu.cmu.cs.dennisc.pattern.Crawler {
		private java.util.Map<Class<? extends org.lgna.project.ast.Statement>, Integer> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

		@Override
		public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
			if( crawlable instanceof org.lgna.project.ast.Statement ) {
				org.lgna.project.ast.Statement statement = (org.lgna.project.ast.Statement)crawlable;
				UserMethod method = statement.getFirstAncestorAssignableTo( UserMethod.class );
				if( ( method != null ) && !method.getManagementLevel().isGenerated()
						// This condition prevents counting methods of the Program class (e.g., main method) which a user cannot edit
						&& !( method.getDeclaringType().isAssignableTo( org.lgna.story.SProgram.class ) ) && statement.isEnabled.getValue() ) {
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
		if( !method.equals( StatisticsFlowControlFrequencyComposite.root ) ) {
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

	public int getMaximum( Class[] clsArr ) {
		int maxCount = 0;
		for( Class cls : clsArr ) {
			int count = getCount( StatisticsFlowControlFrequencyComposite.root, cls );
			if( count > maxCount ) {
				maxCount = count;
			}
		}
		return maxCount;
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		refresh();
	}

	private void refresh() {
		userMethodList.clear();
		methodToConstructMap.clear();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		StatementCountCrawler crawler = new StatementCountCrawler();
		ide.crawlFilteredProgramType( crawler );
		List<UserMethod> methodContainingConstructList = new LinkedList<UserMethod>();
		for( UserMethod method : methodToConstructMap.keySet() ) {
			methodContainingConstructList.add( method );
		}
		java.util.Collections.sort( methodContainingConstructList, new Comparator<UserMethod>() {
			@Override
			public int compare( UserMethod o1, UserMethod o2 ) {
				return o1.getName().compareTo( o2.getName() );
			}
		} );
		getUserMethodList().addItem( root );
		for( UserMethod method : methodContainingConstructList ) {
			getUserMethodList().addItem( method );
		}
		userMethodList.setSelectedIndex( 0 );
	}
}
