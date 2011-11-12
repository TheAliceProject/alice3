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

package org.alice.stageide.typecontext;

/**
 * @author Dennis Cosgrove
 */
public class SceneOrNonSceneCardComposite extends org.lgna.croquet.CardComposite {
	private static class SingletonHolder {
		private static SceneOrNonSceneCardComposite instance = new SceneOrNonSceneCardComposite();
	}
	public static SceneOrNonSceneCardComposite getInstance() {
		return SingletonHolder.instance;
	}
	
	private final org.lgna.croquet.State.ValueObserver< org.lgna.project.ast.NamedUserType > typeListener = new org.lgna.croquet.State.ValueObserver< org.lgna.project.ast.NamedUserType >() {
		public void changing( org.lgna.croquet.State< org.lgna.project.ast.NamedUserType > state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.project.ast.NamedUserType > state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
			SceneOrNonSceneCardComposite.this.handleTypeStateChanged( nextValue );
		}
	};

	private SceneOrNonSceneCardComposite() {
		super( java.util.UUID.fromString( "9d3525cd-c560-4a00-9de4-7c2b5a926ae9" ),
				SceneTypeComposite.getInstance(), 
				NonSceneTypeComposite.getInstance() 
		);
	}
	@Override
	public org.lgna.croquet.components.CardPanel createView() {
		org.lgna.croquet.components.CardPanel rv = super.createView();
		rv.showComposite( SceneTypeComposite.getInstance() );
		return rv;
	}

	private void handleTypeStateChanged( org.lgna.project.ast.NamedUserType nextValue ) {
		org.lgna.croquet.Composite< ? > composite;
		if( nextValue != null ) {
			if( nextValue.isAssignableTo( org.lgna.story.Scene.class ) ) {
				composite = SceneTypeComposite.getInstance();
			} else {
				composite = NonSceneTypeComposite.getInstance();
			}
//			org.lgna.croquet.components.JComponent< ? > view = key.getView();
//			if( view instanceof NonSceneTypeView ) {
//				NonSceneTypeView nonSceneTypeView = (NonSceneTypeView)view;
//				nonSceneTypeView.handlePreShow();
//			}
		} else {
			composite = null;
		}
		this.getView().showComposite( composite );
	}

	public void handleActivation() {
		this.getView().addComposite( SceneTypeComposite.getInstance() );
	}
}
