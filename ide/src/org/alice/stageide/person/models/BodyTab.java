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

package org.alice.stageide.person.models;

/**
 * @author Dennis Cosgrove
 */
public class BodyTab extends ContentTab<org.lgna.croquet.components.BorderPanel> {
	private static class SingletonHolder {
		private static BodyTab instance = new BodyTab();
	}

	public static BodyTab getInstance() {
		return SingletonHolder.instance;
	}

	private BodyTab() {
		super( java.util.UUID.fromString( "10c0d057-a5d7-4a36-8cd7-c30f46f5aac2" ) );
	}

	@Override
	protected org.lgna.croquet.components.BorderPanel createView() {
		java.awt.Color backgroundColor = org.alice.stageide.person.components.MainPanel.BACKGROUND_COLOR;
		org.lgna.croquet.components.List<?> list = new org.alice.stageide.person.components.FullBodyOutfitList();
		org.lgna.croquet.components.ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( list );
		scrollPane.setBothScrollBarIncrements( 66, 66 );
		scrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );

		org.lgna.croquet.components.Slider slider = ObesityLevelState.getInstance().createSlider();
		slider.setBackgroundColor( backgroundColor );

		org.lgna.croquet.components.BorderPanel obesityLevelPane = new org.lgna.croquet.components.BorderPanel.Builder()
				.lineStart( SetObesityToInShapeOperation.getInstance().createButton() )
				.center( slider )
				.lineEnd( SetObesityToOutOfShapeOperation.getInstance().createButton() )
				.build();

		org.lgna.croquet.components.BorderPanel rv = new org.lgna.croquet.components.BorderPanel.Builder()
				.hgap( 8 )
				.vgap( 8 )
				.center( scrollPane )
				.pageEnd( obesityLevelPane )
				.build();
		rv.setBackgroundColor( backgroundColor );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
		return rv;
	}
}
