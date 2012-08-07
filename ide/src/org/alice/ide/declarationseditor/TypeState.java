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
public class TypeState extends org.lgna.croquet.DefaultCustomItemState< org.lgna.project.ast.NamedUserType > {
	private static class SingletonHolder {
		private static TypeState instance = new TypeState();
	}
	public static TypeState getInstance() {
		return SingletonHolder.instance;
	}

	
	private final edu.cmu.cs.dennisc.property.event.ListPropertyListener< org.lgna.project.ast.UserField > fieldsAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< org.lgna.project.ast.UserField >() {
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< org.lgna.project.ast.UserField > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< org.lgna.project.ast.UserField > e ) {
			TypeState.this.handleFieldsChanged();
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< org.lgna.project.ast.UserField > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< org.lgna.project.ast.UserField > e ) {
			TypeState.this.handleFieldsChanged();
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< org.lgna.project.ast.UserField > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< org.lgna.project.ast.UserField > e ) {
			TypeState.this.handleFieldsChanged();
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< org.lgna.project.ast.UserField > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< org.lgna.project.ast.UserField > e ) {
			TypeState.this.handleFieldsChanged();
		}
	};

	private TypeState() {
		super( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "99019283-9a9e-4500-95a4-c4748d762137" ), org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.NamedUserType.class ), null );
	}

	private void handleFieldsChanged() {
		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().handleAstChangeThatCouldBeOfInterest();
	}
	
	@Override
	protected void fireChanged( org.lgna.project.ast.NamedUserType prevValue, org.lgna.project.ast.NamedUserType nextValue, boolean isAdjusting ) {
		super.fireChanged( prevValue, nextValue, isAdjusting );
		if( prevValue != nextValue ) {
			if( prevValue != null ) {
				prevValue.fields.removeListPropertyListener( this.fieldsAdapter );
			}
			if( nextValue != null ) {
				nextValue.fields.addListPropertyListener( this.fieldsAdapter );
			}
		}
	}
	private java.util.List< org.lgna.croquet.CascadeBlankChild > addTypeFillIns( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > node ) {
		rv.add( org.alice.ide.croquet.models.ast.declaration.TypeFillIn.getInstance( node.getValue() ) );
		for( edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > child : node.getChildren() ) {
			addTypeFillIns( rv, child );
		}
		return rv;
	}
	
	@Override
	protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateBlankChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< org.lgna.project.ast.NamedUserType > blankNode ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.alice.ide.ApiConfigurationManager apiConfigurationManager = ide.getApiConfigurationManager();
		edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > root = apiConfigurationManager.getNamedUserTypesAsTreeFilteredForSelection();
		if( root != null ) {
			for( edu.cmu.cs.dennisc.tree.Node< org.lgna.project.ast.NamedUserType > node : root.getChildren() ) {
				addTypeFillIns( rv, node );
			}
		}
		return rv;
	}
}
