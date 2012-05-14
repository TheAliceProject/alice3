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
package org.alice.ide.ast;

/**
 * @author Dennis Cosgrove
 */
public class FieldValueTypeTree {
	private static class Data {
		private final java.util.Map< org.lgna.project.ast.AbstractType,TypeNode > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		private final RootNode root = new RootNode();
		public Data( Class<?>[] topLevelClses ) {
			//map.put( null, this.root );
			for( Class<?> cls : topLevelClses ) {
				org.lgna.project.ast.JavaType type = org.lgna.project.ast.JavaType.getInstance( cls );
				TypeNode typeNode = new TypeNode( root, type );
				map.put( type,  typeNode );
			}
		}
		private TypeNode getTypeNode( org.lgna.project.ast.AbstractType<?,?,?> type ) {
			if( type != null ) {
				TypeNode typeNode = map.get( type );
				if( typeNode != null ) {
					//pass
				} else {
					typeNode = new TypeNode( getTypeNode( type.getSuperType() ), type );
				}
				return typeNode;
			} else {
				return null;
			}
		}
		public void insertField( org.lgna.project.ast.UserField field ) {
			FieldNode fieldNode = new FieldNode( getTypeNode( field.getValueType() ), field );
		}
	}
	private static class Node {
		private final TypeNode parent;
		public Node( TypeNode parent ) {
			this.parent = parent;
		}
	}
	private static class TypeNode extends Node {
		private final org.lgna.project.ast.AbstractType<?,?,?> type;
		private final java.util.List<TypeNode> typeNodes = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		private final java.util.List<FieldNode> fieldNodes = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		public TypeNode( TypeNode parent, org.lgna.project.ast.AbstractType<?,?,?> type ) {
			super( parent );
			this.type = type;
			parent.typeNodes.add( this );
		}
	}
	private static class FieldNode extends Node {
		private final org.lgna.project.ast.UserField field;
		public FieldNode( TypeNode parent, org.lgna.project.ast.UserField field ) {
			super( parent );
			this.field = field;
			parent.fieldNodes.add( this );
		}
	}
	private static class RootNode extends TypeNode {
		public RootNode() {
			super( null, null );
		}
	}
	public FieldValueTypeTree( org.lgna.project.ast.UserType<?> declaringType, Class<?>... topLevelClses ) {
		Data data = new Data( topLevelClses );
		for( org.lgna.project.ast.UserField field : declaringType.fields ) {
			data.insertField( field );
		}
	}
}
