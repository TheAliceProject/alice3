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
package org.alice.ide.member;

/**
 * @author Dennis Cosgrove
 */
public final class FunctionTabComposite extends MemberTabComposite {
	private static class SingletonHolder {
		private static FunctionTabComposite instance = new FunctionTabComposite();
	}
	public static FunctionTabComposite getInstance() {
		return SingletonHolder.instance;
	}
	private org.lgna.croquet.State.ValueListener<org.alice.ide.instancefactory.InstanceFactory> instanceFactorySelectionObserver = new org.lgna.croquet.State.ValueListener<org.alice.ide.instancefactory.InstanceFactory>() {
		public void changing( org.lgna.croquet.State< org.alice.ide.instancefactory.InstanceFactory > state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.instancefactory.InstanceFactory > state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
			if( isAdjusting ) {
				//pass
			} else {
				FunctionTabComposite.this.handleInstanceFactoryChanged( prevValue, nextValue );
			}
		}
	};
	
	private FunctionTabComposite() {
		super( java.util.UUID.fromString( "a2a01f20-37ba-468f-b35b-2b6a2ed94ac7" ), new org.alice.ide.members.filters.FunctionFilter() );
		edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "todo: handlePreActivation tab composites" );
		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().addAndInvokeValueListener( this.instanceFactorySelectionObserver );
	}
	
	private void handleInstanceFactoryChanged( org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue ) {
		for( ReturnTypeFilteredComposite returnTypeFilteredComposite : this.getReturnTypeFilteredComposites() ) {
			returnTypeFilteredComposite.getView().refreshLater();
		}
		this.getView().refreshLater();
	}
	
	public java.util.List<ReturnTypeFilteredComposite> getReturnTypeFilteredComposites() {
		return edu.cmu.cs.dennisc.java.util.Collections.newArrayList( 
				ReturnTypeFilteredComposite.getInstance( org.lgna.project.ast.JavaType.BOOLEAN_OBJECT_TYPE ), 
				ReturnTypeFilteredComposite.getInstance( org.lgna.project.ast.JavaType.DOUBLE_OBJECT_TYPE ),
				ReturnTypeFilteredComposite.getInstance( org.lgna.project.ast.JavaType.INTEGER_OBJECT_TYPE ), 
				ReturnTypeFilteredComposite.getInstance( org.lgna.project.ast.JavaType.getInstance( String.class ) ), 
				ReturnTypeFilteredComposite.getInstance( org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SJoint.class ) ) 
		);
	}
	@Override
	protected org.alice.ide.member.views.MemberTabView createView() {
		return new org.alice.ide.member.views.FunctionTabView( this );
	}
//todo
//	@Override
//	public void handlePreActivation() {
//		super.handlePreActivation();
//		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().addAndInvokeValueListener( this.instanceFactorySelectionObserver );
//	}
//	@Override
//	public void handlePostDeactivation() {
//		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().removeValueListener( this.instanceFactorySelectionObserver );
//		super.handlePostDeactivation();
//	}
}
