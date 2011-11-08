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

package org.alice.stageide.typecontext.components;

/**
 * @author Dennis Cosgrove
 */
public class SceneOrNonSceneCardPanel extends org.lgna.croquet.components.CardPanel {
	private final Key sceneKey;
	private final Key nonSceneKey;
	private final org.lgna.croquet.State.ValueObserver< org.lgna.project.ast.NamedUserType > declarationListener = new org.lgna.croquet.State.ValueObserver< org.lgna.project.ast.NamedUserType >() {
		public void changing( org.lgna.croquet.State< org.lgna.project.ast.NamedUserType > state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.project.ast.NamedUserType > state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
			SceneOrNonSceneCardPanel.this.handleDeclarationStateChanged( nextValue );
		}
	};
	public SceneOrNonSceneCardPanel( org.alice.ide.croquet.SingletonViewComposite sceneComposite, org.alice.ide.croquet.SingletonViewComposite nonSceneComposite ) {
		this.sceneKey = this.createKey( sceneComposite.getView() );
		this.nonSceneKey = this.createKey( nonSceneComposite.getView() );
		this.addComponent( this.sceneKey );
		this.addComponent( this.nonSceneKey );
	}
	
	private void handleDeclarationStateChanged( org.lgna.project.ast.NamedUserType nextValue ) {
		Key key;
		if( nextValue != null ) {
			if( nextValue.isAssignableTo( org.lgna.story.Scene.class ) ) {
				key = this.sceneKey;
			} else {
				key = this.nonSceneKey;
			}
		} else {
			key = null;
		}
		this.show( key );
	}
	@Override
	protected void handleAddedTo( org.lgna.croquet.components.Component< ? > parent ) {
		super.handleAddedTo( parent );
		//org.alice.ide.instancefactory.InstanceFactoryState.getInstance().addValueObserver( this.instanceFactorySelectionObserver );
		org.alice.ide.croquet.models.typeeditor.TypeState.getInstance().addAndInvokeValueObserver( this.declarationListener );
	}
	@Override
	protected void handleRemovedFrom( org.lgna.croquet.components.Component< ? > parent ) {
		//org.alice.ide.instancefactory.InstanceFactoryState.getInstance().removeValueObserver( this.instanceFactorySelectionObserver );
		org.alice.ide.croquet.models.typeeditor.TypeState.getInstance().removeValueObserver( this.declarationListener );
		super.handleRemovedFrom( parent );
	}
}
