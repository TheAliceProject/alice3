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
package org.alice.stageide.type.croquet.data;

/**
 * @author Dennis Cosgrove
 */
public class MemberListData extends org.lgna.croquet.data.RefreshableListData<org.lgna.project.ast.Member> {
	private final org.lgna.croquet.StringState filterState;

	private final org.lgna.croquet.event.ValueListener<String> filterListener = new org.lgna.croquet.event.ValueListener<String>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<String> e ) {
			refresh();
		}
	};
	private java.util.List<org.lgna.project.ast.Member> allMembers;

	public MemberListData( org.lgna.croquet.StringState filterState ) {
		super( org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.Member.class ) );
		this.filterState = filterState;
	}

	private static void build( java.util.List<org.lgna.project.ast.Member> list, org.alice.stageide.type.croquet.TypeNode typeNode ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> type = typeNode.getType();
		list.addAll( type.getDeclaredMethods() );
		final int N = typeNode.getChildCount();
		for( int i = 0; i < N; i++ ) {
			build( list, (org.alice.stageide.type.croquet.TypeNode)typeNode.getChildAt( i ) );
		}
	}

	public void connect( org.alice.stageide.type.croquet.TypeNode root ) {
		java.util.List<org.lgna.project.ast.Member> list = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		build( list, root );
		java.util.Collections.sort( list, new java.util.Comparator<org.lgna.project.ast.Member>() {
			@Override
			public int compare( org.lgna.project.ast.Member o1, org.lgna.project.ast.Member o2 ) {
				return o1.getName().compareTo( o2.getName() );
			}
		} );
		this.allMembers = list;
		this.filterState.addAndInvokeNewSchoolValueListener( this.filterListener );
	}

	public void disconnect() {
		this.filterState.removeNewSchoolValueListener( this.filterListener );
	}

	@Override
	protected java.util.List<org.lgna.project.ast.Member> createValues() {
		if( ( this.allMembers != null ) && ( this.allMembers.size() > 0 ) ) {
			String filter = this.filterState.getValue();
			if( ( filter != null ) && ( filter.length() > 0 ) ) {
				java.util.List<org.lgna.project.ast.Member> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				for( org.lgna.project.ast.Member member : this.allMembers ) {
					if( member.getName().contains( filter ) ) {
						rv.add( member );
					}
				}
				return rv;
			} else {
				return this.allMembers;
			}
		} else {
			return java.util.Collections.emptyList();
		}
	}
}
