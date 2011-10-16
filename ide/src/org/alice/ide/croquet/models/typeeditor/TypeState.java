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

package org.alice.ide.croquet.models.typeeditor;

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
	private TypeState() {
		super( org.lgna.croquet.Application.UI_STATE_GROUP, java.util.UUID.fromString( "99019283-9a9e-4500-95a4-c4748d762137" ), org.alice.ide.croquet.codecs.NodeCodec.getInstance( org.lgna.project.ast.NamedUserType.class ), null );
	}
	
	
	private static class Node {
		public final org.lgna.project.ast.NamedUserType value;
		public final java.util.List< Node > children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		public Node( org.lgna.project.ast.NamedUserType value ) {
			this.value = value;
		}
	}
	
	private Node getNode( org.lgna.project.ast.NamedUserType type, java.util.List< Node > list, java.util.Map< org.lgna.project.ast.NamedUserType, Node > map ) {
		Node rv = map.get( type );
		if( rv != null ) {
			//pass
		} else {
			rv = new Node( type );
			map.put( type, rv );
			org.lgna.project.ast.AbstractType< ?,?,? > superType = type.getSuperType();
			if( superType instanceof org.lgna.project.ast.NamedUserType ) {
				Node superNode = getNode( (org.lgna.project.ast.NamedUserType)superType, list, map );
				superNode.children.add( rv );
			} else {
				list.add( rv );
			}
		}
		return rv;
	}
	
	private java.util.List< Node > createTree( Iterable< org.lgna.project.ast.NamedUserType > types ) {
		java.util.List< Node > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		java.util.Map< org.lgna.project.ast.NamedUserType, Node > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		for( org.lgna.project.ast.NamedUserType type : types ) {
			getNode( type, list, map );
		}
		
		return list;
	}
	
	private java.util.List< org.lgna.croquet.CascadeBlankChild > addTypeFillIns( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, Node node ) {
		rv.add( TypeFillIn.getInstance( node.value ) );
		for( Node child : node.children ) {
			addTypeFillIns( rv, child );
		}
		return rv;
	}
	
	@Override
	protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateBlankChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< org.lgna.project.ast.NamedUserType > blankNode ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.Project project = ide.getProject();
		Iterable< org.lgna.project.ast.NamedUserType > types = project.getNamedUserTypes();

		java.util.List< Node > list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		java.util.Map< org.lgna.project.ast.NamedUserType, Node > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		for( org.lgna.project.ast.NamedUserType type : types ) {
			getNode( type, list, map );
		}
		for( Node node : list ) {
			addTypeFillIns( rv, node );
		}
//		for( org.lgna.project.ast.NamedUserType type : types ) {
//			rv.add( TypeFillIn.getInstance( type ) );
//		}
		return rv;
	}
}
