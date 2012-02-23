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
package org.alice.ide.members.components;

/**
 * @author Dennis Cosgrove
 */
public abstract class MembersContentPanel extends org.lgna.croquet.components.PageAxisPanel {
	private final org.lgna.croquet.State.ValueListener<org.alice.ide.instancefactory.InstanceFactory> instanceFactoryListener = new org.lgna.croquet.State.ValueListener<org.alice.ide.instancefactory.InstanceFactory>() {
		public void changing( org.lgna.croquet.State< org.alice.ide.instancefactory.InstanceFactory > state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.instancefactory.InstanceFactory > state, org.alice.ide.instancefactory.InstanceFactory prevValue, org.alice.ide.instancefactory.InstanceFactory nextValue, boolean isAdjusting ) {
			MembersContentPanel.this.refreshLater();
		}
	};
	private final org.lgna.croquet.State.ValueListener< org.lgna.project.ast.NamedUserType > typeListener = new org.lgna.croquet.State.ValueListener< org.lgna.project.ast.NamedUserType >() {
		public void changing( org.lgna.croquet.State< org.lgna.project.ast.NamedUserType > state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.lgna.project.ast.NamedUserType > state, org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
			MembersContentPanel.this.refreshLater();
		}
	};
	public MembersContentPanel( org.lgna.croquet.TabComposite< ? > composite ) {
		super( composite );
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().addAndInvokeValueListener( this.instanceFactoryListener );
		org.alice.ide.declarationseditor.TypeState.getInstance().addValueListener( this.typeListener );
	}
	@Override
	protected void handleUndisplayable() {
		org.alice.ide.declarationseditor.TypeState.getInstance().removeValueListener( this.typeListener );
		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().removeValueListener( this.instanceFactoryListener );
		super.handleUndisplayable();
	}
	
	protected abstract void refresh( java.util.List< org.lgna.project.ast.AbstractType<?,?,?> > types );
	@Override
	protected void internalRefresh() {
		org.alice.ide.instancefactory.InstanceFactory instanceFactory = org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().getValue();
		java.util.List< org.lgna.project.ast.AbstractType<?,?,?> > types = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		if( instanceFactory != null ) {
			org.lgna.project.ast.AbstractType<?,?,?> type = instanceFactory.getValueType();
			while( type != null ) {
				types.add( type );
				if( type.isFollowToSuperClassDesired() ) {
					type = type.getSuperType();
				} else {
					break;
				}
			}
		}
		this.refresh( types );
	}
}

