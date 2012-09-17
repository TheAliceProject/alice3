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

package org.alice.ide.croquet.models.declaration;

/**
 * @author Dennis Cosgrove
 */
public class DeclaringTypeState extends org.lgna.croquet.DefaultCustomItemState<org.lgna.project.ast.UserType> {
	public DeclaringTypeState( org.lgna.project.ast.UserType<?> initialValue ) {
		super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "20e50e4f-b627-4f5c-9851-5cbc18b5a5ee" ), org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.UserType.class ), initialValue );
	}

	@Override
	protected java.util.List<org.lgna.croquet.CascadeBlankChild> updateBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.UserType> blankNode ) {
		org.lgna.project.Project project = org.alice.ide.IDE.getActiveInstance().getProject();
		org.lgna.project.ast.NamedUserType programType = project.getProgramType();
		Iterable<org.lgna.project.ast.NamedUserType> types = project.getNamedUserTypes();
		for( org.lgna.project.ast.NamedUserType type : types ) {
			if( org.alice.ide.croquet.models.ui.preferences.IsIncludingProgramType.getInstance().getValue() || ( type != programType ) ) {
				rv.add( org.alice.ide.croquet.models.ast.declaration.TypeFillIn.getInstance( type ) );
			}
		}
		return rv;
	}

	public org.lgna.croquet.components.PopupButton createComponent() {
		final org.lgna.croquet.components.PopupButton rv = this.getCascadeRoot().getPopupPrepModel().createPopupButton();
		javax.swing.JButton awtButton = rv.getAwtComponent();
		final int PAD = 8;
		class TypeDropDownIcon extends edu.cmu.cs.dennisc.javax.swing.icons.DropDownArrowIcon {
			public TypeDropDownIcon() {
				super( 14 );
			}

			private org.alice.ide.common.TypeIcon getTypeIcon() {
				return org.alice.ide.common.TypeIcon.getInstance( DeclaringTypeState.this.getValue() );
			}

			@Override
			public int getIconWidth() {
				return super.getIconWidth() + PAD + this.getTypeIcon().getIconWidth();
			}

			@Override
			public int getIconHeight() {
				return Math.max( super.getIconHeight(), this.getTypeIcon().getIconHeight() );
			}

			@Override
			public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
				org.alice.ide.common.TypeIcon typeIcon = this.getTypeIcon();
				typeIcon.paintIcon( c, g, x, y );
				super.paintIcon( c, g, x + typeIcon.getIconWidth() + PAD, y );
			}
		}
		awtButton.setIcon( new TypeDropDownIcon() );
		this.addValueListener( new ValueListener<org.lgna.project.ast.UserType>() {
			public void changing( org.lgna.croquet.State<org.lgna.project.ast.UserType> state, org.lgna.project.ast.UserType prevValue, org.lgna.project.ast.UserType nextValue, boolean isAdjusting ) {
			}

			public void changed( org.lgna.croquet.State<org.lgna.project.ast.UserType> state, org.lgna.project.ast.UserType prevValue, org.lgna.project.ast.UserType nextValue, boolean isAdjusting ) {
				rv.revalidateAndRepaint();
			}
		} );
		return rv;
	}
}
