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

package org.alice.ide.croquet.models.templates;

/**
 * @author Dennis Cosgrove
 */
public class BlockTemplateComposite extends TemplateComposite {
	private static class SingletonHolder {
		private static BlockTemplateComposite instance = new BlockTemplateComposite();
	}
	public static BlockTemplateComposite getInstance() {
		return SingletonHolder.instance;
	}
	private final java.util.Set< Class<?> > clses = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
	private BlockTemplateComposite() {
		super( java.util.UUID.fromString( "c61a35cf-5378-44d1-ae4d-8efd7ab40fd3" ) );
		clses.add( org.alice.ide.croquet.models.ast.cascade.statement.CountLoopInsertOperation.class );
		clses.add( org.alice.ide.croquet.models.ast.cascade.statement.WhileLoopInsertOperation.class );
		clses.add( org.alice.ide.croquet.models.ast.cascade.statement.DoInOrderInsertOperation.class );
		clses.add( org.alice.ide.croquet.models.ast.cascade.statement.DoTogetherInsertOperation.class );
		//todo
	}
	@Override
	public boolean contains( edu.cmu.cs.dennisc.croquet.Model model ) {
		if( clses.contains( model.getClass() ) ) {
			return true;
		}
		return false;
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent() {
		edu.cmu.cs.dennisc.croquet.JComponent< ? > rv = new org.alice.ide.ubiquitouspane.UbiquitousPane();
		rv.setBackgroundColor( new java.awt.Color( 250, 150, 105 ) );
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
		return rv;
	}
	@Override
	public void customizeTitleComponent( edu.cmu.cs.dennisc.croquet.BooleanState booleanState, edu.cmu.cs.dennisc.croquet.AbstractButton< ?, edu.cmu.cs.dennisc.croquet.BooleanState > button ) {
		super.customizeTitleComponent( booleanState, button );
		button.getAwtComponent().setIcon( new javax.swing.Icon() {
			public int getIconHeight() {
				return 24;
			}
			public int getIconWidth() {
				return 0;//32;
			}
			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
			}
		} );
	}
	@Override
	protected String getTextForTabTitle() {
		return "Action Ordering Boxes";
	}
}
