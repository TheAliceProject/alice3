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
package org.alice.ide.declarationseditor;

/**
 * @author Dennis Cosgrove
 */
public class DeclarationCompositeHistory {
	private static class SingletonHolder {
		private static DeclarationCompositeHistory instance = new DeclarationCompositeHistory();
	}
	public static DeclarationCompositeHistory getInstance() {
		return SingletonHolder.instance;
	}
	private java.util.List< DeclarationComposite > history = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private int index = -1;
	private final org.lgna.croquet.State.ValueObserver< DeclarationComposite > declarationListener = new org.lgna.croquet.State.ValueObserver< DeclarationComposite >() {
		public void changing( org.lgna.croquet.State< org.alice.ide.declarationseditor.DeclarationComposite > state, org.alice.ide.declarationseditor.DeclarationComposite prevValue, org.alice.ide.declarationseditor.DeclarationComposite nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.declarationseditor.DeclarationComposite > state, org.alice.ide.declarationseditor.DeclarationComposite prevValue, org.alice.ide.declarationseditor.DeclarationComposite nextValue, boolean isAdjusting ) {
			DeclarationCompositeHistory.this.appendIfAppropriate( nextValue );
		}
	};
	private final org.alice.ide.ProjectApplication.ProjectObserver projectListener = new org.alice.ide.ProjectApplication.ProjectObserver() {
		public void projectOpening( org.lgna.project.Project previousProject, org.lgna.project.Project nextProject ) {
		}
		public void projectOpened( org.lgna.project.Project previousProject, org.lgna.project.Project nextProject ) {
			DeclarationCompositeHistory.this.resetStack();
		}
	};

	private int ignoreCount = 0;
	private DeclarationCompositeHistory() {
		org.alice.ide.IDE.getActiveInstance().addProjectObserver( this.projectListener );
		DeclarationTabState.getInstance().addValueObserver( this.declarationListener );
		this.resetStack();
	}
	
	private void pushIgnore() {
		this.ignoreCount ++;
	}
	private void popIgnore() {
		this.ignoreCount --;
	}
	
	private void appendIfAppropriate( org.alice.ide.declarationseditor.DeclarationComposite declarationComposite ) {
		if( this.ignoreCount == 0 ) {
			if( declarationComposite != null ) {
				if( this.index > 0 ) {
					this.history = this.history.subList( this.index, this.history.size() );
				}
				java.util.ListIterator< DeclarationComposite > listIterator = this.history.listIterator();
				while( listIterator.hasNext() ) {
					DeclarationComposite current = listIterator.next();
					if( current == declarationComposite ) {
						listIterator.remove();
					}
				}
				this.index = 0;
				this.history.add( 0, declarationComposite );
				this.updateBackEnabled();
				this.updateFrontEnabled();
			}
		}
	}
	private void updateBackEnabled() {
		boolean isEnabled = (this.index+1) < this.history.size();
		BackwardOperation.getInstance().setEnabled( isEnabled );
		BackwardCascade.getInstance().getRoot().getPopupPrepModel().setEnabled( isEnabled );
	}
	private void updateFrontEnabled() {
		boolean isEnabled = 0 < this.index;
		ForwardOperation.getInstance().setEnabled( isEnabled );
		ForwardCascade.getInstance().getRoot().getPopupPrepModel().setEnabled( isEnabled );
	}
	private void resetStack() {
		this.history.clear();
		this.index = -1;
		this.appendIfAppropriate( DeclarationTabState.getInstance().getValue() );
		this.updateBackEnabled();
		this.updateFrontEnabled();
	}
	
	private void setIndex( int index ) {
		this.pushIgnore();
		try {
			this.index = index;
			this.updateBackEnabled();
			this.updateFrontEnabled();
			DeclarationTabState.getInstance().setValue( this.history.get( this.index ) );
		} finally {
			this.popIgnore();
		}
	}
	
	public void goBackward() {
		this.setIndex( index++ );
	}
	public void goForward() {
		this.setIndex( index-- );
	}
	public void setDeclarationComposite( DeclarationComposite declarationComposite ) {
		this.setIndex( this.history.indexOf( declarationComposite ) );
	}
	
	public java.util.List< DeclarationComposite > getBackwardList() {
		int minInclusive = this.index+1;
		int maxExclusive = this.history.size();
		if( minInclusive < maxExclusive ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.testing( this.index, minInclusive, maxExclusive );
			edu.cmu.cs.dennisc.java.util.logging.Logger.testing( this.history );
			edu.cmu.cs.dennisc.java.util.logging.Logger.testing( this.history.subList( minInclusive, maxExclusive ) );
			return this.history.subList( minInclusive, maxExclusive );
		} else {
			return java.util.Collections.emptyList();
		}
	}
	public java.util.List< DeclarationComposite > getForwardList() {
		int minInclusive = 0;
		int maxExclusive = this.index;
		if( minInclusive < maxExclusive ) {
			java.util.List< DeclarationComposite > rv = this.history.subList( minInclusive, maxExclusive );
			java.util.Collections.reverse( rv );
			return rv;
		} else {
			return java.util.Collections.emptyList();
		}
	}
}
