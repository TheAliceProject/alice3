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

package org.alice.ide.member.views;

/**
 * @author Dennis Cosgrove
 */
public class MethodsSubView extends org.lgna.croquet.components.PageAxisPanel {
	public MethodsSubView( org.alice.ide.member.MethodsSubComposite composite ) {
		super( composite );
		this.setMaximumSizeClampedToPreferredSize( true );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 0, 8, 12, 0 ) );
	}
	@Override
	protected void internalRefresh() {
		super.internalRefresh();
		org.alice.ide.member.MethodsSubComposite composite = (org.alice.ide.member.MethodsSubComposite)this.getComposite();
		this.removeAllComponents();
		for( org.lgna.project.ast.AbstractMethod method : composite.getMethods() ) {
			org.lgna.croquet.components.DragComponent<?,?> dragComponent = org.alice.ide.members.components.templates.TemplateFactory.getFunctionInvocationTemplate( method );
			org.lgna.croquet.components.JComponent<?> component;
			if( method.isUserAuthored() ) {
				org.alice.ide.declarationseditor.CodeComposite codeComposite = org.alice.ide.declarationseditor.CodeComposite.getInstance( method );
				org.lgna.croquet.BooleanState isSelectedState = org.alice.ide.declarationseditor.DeclarationTabState.getInstance().getItemSelectedState( codeComposite );
				org.lgna.croquet.components.ToggleButton button = isSelectedState.createToggleButton();
				button.getAwtComponent().setText( "edit" );
				component = new org.lgna.croquet.components.LineAxisPanel( button, dragComponent );
			} else {
				component = dragComponent;
			}
			this.addComponent( component );
		}
	}
}
