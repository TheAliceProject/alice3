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
package edu.cmu.cs.dennisc.cheshire;

import edu.cmu.cs.dennisc.tutorial.*;

/**
 * @author Dennis Cosgrove
 */
public abstract class GuidedInteraction {
	private static GuidedInteraction instance;
	public static GuidedInteraction getInstance() {
		return instance;
	}

	private edu.cmu.cs.dennisc.croquet.UserInformation userInformation;
	private AutomaticTutorialStencil stencil;
	private edu.cmu.cs.dennisc.croquet.RootContext sourceContext;

	/*package-private*/ class AutomaticTutorialStencil extends TutorialStencil {
		public AutomaticTutorialStencil( MenuPolicy menuPolicy, StepAccessPolicy stepAccessPolicy, ScrollingRequiredRenderer scrollingRequiredRenderer, boolean isOptimizedForBugRepro, edu.cmu.cs.dennisc.croquet.Group[] groups ) {
			super( menuPolicy, stepAccessPolicy, scrollingRequiredRenderer, isOptimizedForBugRepro, groups, edu.cmu.cs.dennisc.croquet.TransactionManager.getRootContext() );
		}
	}
	
	public GuidedInteraction( edu.cmu.cs.dennisc.croquet.UserInformation userInformation, MenuPolicy menuPolicy, StepAccessPolicy stepAccessPolicy, ScrollingRequiredRenderer scrollingRequiredRenderer, boolean isOptimizedForBugRepro, edu.cmu.cs.dennisc.croquet.Group[] groupsTrackedForRandomAccess ) {
		assert instance == null;
		instance = this;
		this.userInformation = userInformation;
		this.stencil = new AutomaticTutorialStencil( menuPolicy, stepAccessPolicy, scrollingRequiredRenderer, isOptimizedForBugRepro, groupsTrackedForRandomAccess );
	}
	
	public edu.cmu.cs.dennisc.croquet.UserInformation getUserInformation() {
		return this.userInformation;
	}
	
	private edu.cmu.cs.dennisc.croquet.Retargeter retargeter;
	public edu.cmu.cs.dennisc.croquet.Retargeter getRetargeter() {
		return this.retargeter;
	}
	public void setRetargeter( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		this.retargeter = retargeter;
	}
	
	public void retargetOriginalContext( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		this.sourceContext.retarget( retargeter );
	}
	
	public void setOriginalRoot( edu.cmu.cs.dennisc.croquet.RootContext sourceContext ) {
		this.sourceContext = sourceContext;
		final int N = sourceContext.getChildCount();
		for( int i=0; i<N; i++ ) {
			edu.cmu.cs.dennisc.croquet.HistoryNode node = sourceContext.getChildAt( i );
			if( node instanceof edu.cmu.cs.dennisc.croquet.ModelContext< ? > ) {
				edu.cmu.cs.dennisc.croquet.ModelContext< ? > context = (edu.cmu.cs.dennisc.croquet.ModelContext< ? >)node;
				if( context.isSuccessfullyCompleted() ) {
					this.stencil.addStep( new Page( context ) );
				}
			} else {
				if( node instanceof Message ) {
					Message message = (Message)node;
					this.stencil.addStep( new MessageStep( message.getTutorialStepTitle( null ), message.getText() ) );
				}
			}
		}
	}

	protected abstract java.util.List< RetargetableNote > addNotesToGetIntoTheRightStateWhenNoViewControllerCanBeFound( java.util.List< RetargetableNote > rv, ParentContextCriterion parentContextCriterion, edu.cmu.cs.dennisc.croquet.ModelContext< ? > modelContext );
	/*package-private*/ AutomaticTutorialStencil getStencil() {
		return this.stencil;
	}
	public void setSelectedIndex( int index ) {
		this.stencil.setSelectedIndex( index );
	}
	public void setVisible( boolean isVisible ) {
		if( isVisible ) {
			this.stencil.addToLayeredPane();
		} else {
			this.stencil.removeFromLayeredPane();
		}
	}
}
