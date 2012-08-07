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

package org.alice.ide.croquet.components.declaration;

/**
 * @author Dennis Cosgrove
 */
public class GalleryFieldDeclarationPanel extends FieldDeclarationPanel< org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation > {
	private final org.lgna.croquet.State.ValueListener< org.lgna.project.ast.Expression > initializerListener = new org.lgna.croquet.State.ValueListener< org.lgna.project.ast.Expression >() {
		public void changing( org.lgna.croquet.State< org.lgna.project.ast.Expression > state, org.lgna.project.ast.Expression prevValue, org.lgna.project.ast.Expression nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.project.ast.Expression > state, org.lgna.project.ast.Expression prevValue, org.lgna.project.ast.Expression nextValue, boolean isAdjusting ) {
			GalleryFieldDeclarationPanel.this.update();
		}
	};
	private final org.lgna.croquet.components.Label iconLabel = new org.lgna.croquet.components.Label();

	public GalleryFieldDeclarationPanel( org.alice.ide.croquet.models.declaration.ManagedFieldDeclarationOperation model ) {
		super( model );
		this.iconLabel.setVerticalAlignment( org.lgna.croquet.components.VerticalAlignment.TOP );
		this.addLineEndComponent( this.iconLabel );
	}
	private void update() {
		this.updateNameTextField();
		javax.swing.Icon prevIcon = this.iconLabel.getIcon();
		javax.swing.Icon nextIcon;
		
		org.lgna.project.ast.InstanceCreation instanceCreation = this.getInstanceCreationFromInitializer();
		java.lang.reflect.Field fld = this.getFldFromInstanceCreationInitializer( instanceCreation );
		if( fld != null ) {
			java.awt.Image thumbnail = org.lgna.story.implementation.alice.AliceResourceUtilties.getThumbnail( fld.getDeclaringClass(), fld.getName() );
			if( thumbnail != null ) {
				nextIcon = new javax.swing.ImageIcon( thumbnail );
			} else {
				nextIcon = null;
			}
		} else {
			nextIcon = null;
		}
		this.iconLabel.setIcon( nextIcon );
		if( edu.cmu.cs.dennisc.javax.swing.IconUtilities.areSizesEqual( prevIcon, nextIcon ) ) {
			//pass
		} else {
			this.revalidateAndRepaint();
		}
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.getModel().getInitializerState().addValueListener( this.initializerListener );
		this.update();
	}
	@Override
	protected void handleUndisplayable() {
		this.getModel().getInitializerState().removeValueListener( this.initializerListener );
		super.handleUndisplayable();
	}
}
