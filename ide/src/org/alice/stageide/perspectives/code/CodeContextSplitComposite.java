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

package org.alice.stageide.perspectives.code;

/**
 * @author Dennis Cosgrove
 */
public class CodeContextSplitComposite extends org.lgna.croquet.ImmutableSplitComposite {
	private static class SingletonHolder {
		private static CodeContextSplitComposite instance = new CodeContextSplitComposite();
	}

	public static CodeContextSplitComposite getInstance() {
		return SingletonHolder.instance;
	}

	private final org.lgna.croquet.State.ValueListener<org.lgna.project.ast.NamedUserType> typeListener = new org.lgna.croquet.State.ValueListener<org.lgna.project.ast.NamedUserType>() {
		public void changing( org.lgna.croquet.State<org.lgna.project.ast.NamedUserType> state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<org.lgna.project.ast.NamedUserType> state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
			CodeContextSplitComposite.this.handleTypeStateChanged( nextValue );
		}
	};

	private final java.beans.PropertyChangeListener dividerLocationListener = new java.beans.PropertyChangeListener() {
		public void propertyChange( java.beans.PropertyChangeEvent e ) {
			if( ignoreDividerChangeCount > 0 ) {
				//pass
			} else {
				CodePerspectiveComposite otherComposite = CodePerspectiveComposite.getInstance();
				org.lgna.croquet.components.SplitPane otherSplitPane = otherComposite.getView();
				int prevValue = otherSplitPane.getDividerLocation();
				int nextValue = (int)( (Integer)e.getNewValue() * org.alice.stageide.croquet.models.run.RunOperation.WIDTH_TO_HEIGHT_RATIO );
				if( prevValue != nextValue ) {
					otherComposite.incrementIgnoreDividerLocationChangeCount();
					try {
						otherSplitPane.setDividerLocation( nextValue );
					} finally {
						otherComposite.decrementIgnoreDividerLocationChangeCount();
					}
				}
				//edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "inner:", e.getOldValue(), e.getNewValue() );
			}
		}
	};

	private void handleTypeStateChanged( org.lgna.project.ast.NamedUserType nextValue ) {
		org.lgna.croquet.Composite<?> composite;
		if( nextValue != null ) {
			if( nextValue.isAssignableTo( org.lgna.story.SScene.class ) ) {
				composite = org.alice.stageide.typecontext.SceneTypeComposite.getInstance();
			} else {
				composite = org.alice.stageide.typecontext.NonSceneTypeComposite.getInstance();
			}
			//			org.lgna.croquet.components.JComponent< ? > view = key.getView();
			//			if( view instanceof NonSceneTypeView ) {
			//				NonSceneTypeView nonSceneTypeView = (NonSceneTypeView)view;
			//				nonSceneTypeView.handlePreShow();
			//			}
		} else {
			composite = null;
		}
		this.sceneOrNonSceneComposite.showCard( composite );
	}

	private final org.lgna.croquet.CardOwnerComposite sceneOrNonSceneComposite;
	private int ignoreDividerChangeCount = 0;

	private CodeContextSplitComposite() {
		super( java.util.UUID.fromString( "c3336f34-9da4-4aaf-86ff-d742f4717d94" ) );
		this.sceneOrNonSceneComposite = this.createCardOwnerComposite( org.alice.stageide.typecontext.SceneTypeComposite.getInstance(), org.alice.stageide.typecontext.NonSceneTypeComposite.getInstance() );
	}

	@Override
	public org.lgna.croquet.Composite<?> getLeadingComposite() {
		return this.sceneOrNonSceneComposite;
	}

	@Override
	public org.lgna.croquet.Composite<?> getTrailingComposite() {
		return TypeOrCodeCardOwnerComposite.getInstance();
	}

	public void incrementIgnoreDividerLocationChangeCount() {
		this.ignoreDividerChangeCount++;
	}

	public void decrementIgnoreDividerLocationChangeCount() {
		this.ignoreDividerChangeCount--;
	}

	@Override
	protected org.lgna.croquet.components.SplitPane createView() {
		org.lgna.croquet.components.SplitPane rv = this.createVerticalSplitPane();
		rv.addDividerLocationChangeListener( this.dividerLocationListener );
		rv.setResizeWeight( 0.0 );
		return rv;
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		org.alice.ide.declarationseditor.TypeState.getInstance().addAndInvokeValueListener( this.typeListener );
	}

	@Override
	public void handlePostDeactivation() {
		org.alice.ide.declarationseditor.TypeState.getInstance().removeValueListener( this.typeListener );
		super.handlePostDeactivation();
	}
}
