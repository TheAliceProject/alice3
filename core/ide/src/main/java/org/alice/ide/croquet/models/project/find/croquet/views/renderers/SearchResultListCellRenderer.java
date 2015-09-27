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
package org.alice.ide.croquet.models.project.find.croquet.views.renderers;

import java.util.ArrayList;
import java.util.Stack;

import org.alice.ide.croquet.models.project.find.croquet.AbstractFindComposite;

/**
 * @author Dennis Cosgrove
 */
public class SearchResultListCellRenderer extends edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer<org.alice.ide.croquet.models.project.find.core.SearchResult> {

	private final AbstractFindComposite composite;

	public SearchResultListCellRenderer( AbstractFindComposite composite ) {
		this.composite = composite;
	}

	@Override
	protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, org.alice.ide.croquet.models.project.find.core.SearchResult value, int index, boolean isSelected, boolean cellHasFocus ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "<HTML>" );
		String finalValue = getNameString( value.getName(), composite.getSearchState().getValue() );
		sb.append( finalValue );
		sb.append( " (" );
		sb.append( value.getReferences().size() );
		sb.append( ")" );
		sb.append( "</HTML>" );
		rv.setText( sb.toString() );
		rv.setIcon( value.getIcon() );
		return rv;
	}

	private String getNameString( String name, String value ) {
		String rv = name;
		String unparsed = value.toLowerCase();
		ArrayList<String> list = edu.cmu.cs.dennisc.java.util.Lists.newArrayList();
		while( true ) {
			if( unparsed.length() == 0 ) {
				break;
			} else if( unparsed.contains( "*" ) ) {
				int starIndex = unparsed.indexOf( "*" );
				if( starIndex != unparsed.length() ) {
					list.add( unparsed.substring( 0, starIndex ) );
					unparsed = unparsed.substring( starIndex + 1 );
				}
			} else {
				list.add( unparsed );
				break;
			}
		}
		String temp = name.toLowerCase();
		Stack<Integer> stack = new Stack<Integer>();
		int itr = 0;
		for( String str : list ) {
			int start = temp.indexOf( str );
			stack.push( itr + start );
			stack.push( itr + start + str.length() );
			itr = itr + start + str.length();
			temp = temp.substring( itr );
		}
		while( !stack.isEmpty() ) {
			Integer index = stack.pop();
			rv = rv.substring( 0, index ) + "</strong>" + rv.substring( index );
			index = stack.pop();
			rv = rv.substring( 0, index ) + "<strong>" + rv.substring( index );
		}
		return rv;
	}
}
