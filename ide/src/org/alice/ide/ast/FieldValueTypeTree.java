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
					map.put( type,  typeNode );
				}
				return typeNode;
			} else {
				return null;
			}
		}
		public void insertField( org.lgna.project.ast.UserField field ) {
			FieldNode fieldNode = new FieldNode( getTypeNode( field.getValueType() ), field );
		}
		public void output() {
			StringBuilder sb = new StringBuilder();
			this.root.append( sb, 0 );
			System.out.println( sb.toString() );
		}
	}
	private static class Node<N extends org.lgna.project.ast.AbstractDeclaration> {
		private final TypeNode parent;
		private final N declaration;
		private final int depth;
		public Node( TypeNode parent, N declaration ) {
			this.parent = parent;
			if( this.parent != null ) {
				this.depth = this.parent.getDepth() + 1;
			} else {
				this.depth = 0;
			}
			this.declaration = declaration;
		}
		public int getDepth() {
			return this.depth;
		}
		protected void append( StringBuilder sb, int level ) {
			for( int i=0; i<level; i++ ) {
				sb.append( "\t" );
			}
			sb.append( this.declaration );
			sb.append( "\n" );
		}
	}
	private static class TypeNode extends Node<org.lgna.project.ast.AbstractType<?,?,?>> {
		private final java.util.List<TypeNode> typeNodes = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		private final java.util.List<FieldNode> fieldNodes = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		public TypeNode( TypeNode parent, org.lgna.project.ast.AbstractType<?,?,?> type ) {
			super( parent, type );
			if( parent != null ) {
				parent.typeNodes.add( this );
			}
		}
		
		@Override
		protected void append( StringBuilder sb, int level ) {
			super.append( sb, level );
			for( TypeNode typeNode : this.typeNodes ) {
				typeNode.append( sb, level+1 );
			}
			for( FieldNode fieldNode : this.fieldNodes ) {
				fieldNode.append( sb, level+1 );
			}
		}
	}
	private static class FieldNode extends Node<org.lgna.project.ast.UserField> {
		public FieldNode( TypeNode parent, org.lgna.project.ast.UserField field ) {
			super( parent, field );
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
		data.output();
	}
	public static void main( String[] args ) throws Exception {
		org.lgna.project.Project project = org.lgna.project.io.IoUtilities.readProject( args[ 0 ] );
		org.lgna.project.ast.NamedUserType sceneType = (org.lgna.project.ast.NamedUserType)project.getProgramType().fields.get( 0 ).getValueType();
		FieldValueTypeTree tree = new FieldValueTypeTree( sceneType, org.lgna.story.Biped.class, org.lgna.story.Quadruped.class, org.lgna.story.Swimmer.class, org.lgna.story.Flyer.class, org.lgna.story.Prop.class, org.lgna.story.Entity.class );
		
	}
}
