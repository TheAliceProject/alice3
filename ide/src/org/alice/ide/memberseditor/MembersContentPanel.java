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
package org.alice.ide.memberseditor;

/**
 * @author Dennis Cosgrove
 */
public abstract class MembersContentPanel extends edu.cmu.cs.dennisc.croquet.PageAxisPanel {
	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.alice.ast.Accessible> fieldSelectionObserver = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.alice.ast.Accessible>() {
		public void changed(edu.cmu.cs.dennisc.alice.ast.Accessible nextValue) {
			MembersContentPanel.this.handleAccessibleSelection( nextValue );
		}
	};
	private edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField> partSelectionObserver = new edu.cmu.cs.dennisc.croquet.ListSelectionState.ValueObserver<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField>() {
		public void changed(edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField nextValue) {
			MembersContentPanel.this.handlePartSelection( nextValue );
		}
	};
	@Override
	protected void handleAddedTo( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
		super.handleAddedTo( parent );
		org.alice.ide.croquet.models.members.PartSelectionState.getInstance().addValueObserver( this.partSelectionObserver );
		org.alice.ide.IDE.getSingleton().getAccessibleListState().addAndInvokeValueObserver( this.fieldSelectionObserver );
	}
	@Override
	protected void handleRemovedFrom( edu.cmu.cs.dennisc.croquet.Component< ? > parent ) {
		org.alice.ide.croquet.models.members.PartSelectionState.getInstance().removeValueObserver( this.partSelectionObserver );
		org.alice.ide.IDE.getSingleton().getAccessibleListState().removeValueObserver( this.fieldSelectionObserver );
		super.handleRemovedFrom( parent );
	}
	
	protected abstract void refresh( java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > types );
	
	private void refresh() {
		edu.cmu.cs.dennisc.alice.ast.Accessible accessible = org.alice.ide.IDE.getSingleton().getAccessibleListState().getSelectedItem();
		java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > types = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		if( accessible != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type = accessible.getValueType();
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField field = org.alice.ide.croquet.models.members.PartSelectionState.getInstance().getSelectedItem();
			if( field != null ) {
				edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava fieldType = field.getValueType();
				Class<?> cls = fieldType.getClassReflectionProxy().getReification();
				Class<?> enclosingCls = cls.getEnclosingClass();
				if( enclosingCls != null ) {
					try {
						java.lang.reflect.Method mthd = enclosingCls.getMethod( "getPart", cls );
						type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( mthd.getReturnType() );
					} catch( NoSuchMethodException nsme ) {
						//pass
					}
				}
			}
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
	private void handleAccessibleSelection( edu.cmu.cs.dennisc.alice.ast.Accessible accessible ) {
		this.refresh();
	}
	private void handlePartSelection( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField field ) {
		this.refresh();
	}
}

