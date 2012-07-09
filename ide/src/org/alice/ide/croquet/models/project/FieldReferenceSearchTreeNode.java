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
package org.alice.ide.croquet.models.project;

import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;

import org.lgna.croquet.ItemCodec;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Collections;

/**
 * @author Matt May
 */
public class FieldReferenceSearchTreeNode {

	private FieldReferenceSearchTreeNode parent;
	private List<FieldReferenceSearchTreeNode> children = Collections.newLinkedList();
	private UserField field;
	private NodeType type;
	private final List<FieldAccess> references = Collections.newLinkedList();
	private static FieldReferenceSearchTreeNode root = new FieldReferenceSearchTreeNode( null );

	public enum NodeType {
		FIELD, HEADER_FIELD, ROOT;
	}

	public static FieldReferenceSearchTreeNode getRoot() {
		return root;
	}

	public static FieldReferenceSearchTreeNode createFieldNode( FieldReferenceSearchTreeNode parent, UserField field ) {
		FieldReferenceSearchTreeNode rv = new FieldReferenceSearchTreeNode( parent );
		rv.type = NodeType.FIELD;
		rv.field = field;
		parent.addChild( rv );
		return rv;
	}
	private static FieldReferenceSearchTreeNode createHeaderNodeNode( FieldReferenceSearchTreeNode parent, NodeType type ) {
		FieldReferenceSearchTreeNode rv = new FieldReferenceSearchTreeNode( parent );
		rv.type = type;
		parent.addChild( rv );
		return rv;
	}

	private FieldReferenceSearchTreeNode( FieldReferenceSearchTreeNode parent ) {
		if( parent != null ) {
			//			parent.addChild( this );
		} else {
			this.type = NodeType.ROOT;
		}
	}

	private void addChild( FieldReferenceSearchTreeNode child ) {
		if( !(type == NodeType.ROOT) ) {
			if( type == NodeType.FIELD ) {
				 if( child.type == NodeType.FIELD ) {
					getFields().addChild( child );
				} else {
					assert child.type == NodeType.HEADER_FIELD;// || child.type == NodeType.HEADER_REFERENCE;
					children.add( child );
				}
			} else if( type == NodeType.HEADER_FIELD ) {
				assert child.type == NodeType.FIELD;
				this.children.add( child );
			}
		} else {
			children.add( child );
		}
	}
	private FieldReferenceSearchTreeNode getFields() {
		if( this.type == NodeType.HEADER_FIELD ) {
			return this;
		} else if( this.type.equals( NodeType.FIELD ) ) {
			for( FieldReferenceSearchTreeNode child : children ) {
				if( child.type.equals( NodeType.HEADER_FIELD ) ) {
					return child;
				}
			}
			FieldReferenceSearchTreeNode newHeader = createHeaderNodeNode( this, NodeType.HEADER_FIELD );
			return newHeader;
		}
		return null;
	}

	public FieldReferenceSearchTreeNode getChild( int index ) {
		return children.get( index );
	}

	public int getChildIndex( FieldReferenceSearchTreeNode child ) {
		return children.indexOf( child );
	}

	public FieldReferenceSearchTreeNode getParent() {
		return this.parent;
	}

	public int getNumChildren() {
		return children.size();
	}

	public Icon getIcon() {
		return null;
	}
	public static ItemCodec<FieldReferenceSearchTreeNode> getNewItemCodec() {
		return new ItemCodec<FieldReferenceSearchTreeNode>() {

			public Class<FieldReferenceSearchTreeNode> getValueClass() {
				return null;
			}

			public FieldReferenceSearchTreeNode decodeValue( BinaryDecoder binaryDecoder ) {
				return null;
			}

			public void encodeValue( BinaryEncoder binaryEncoder, FieldReferenceSearchTreeNode value ) {
			}

			public StringBuilder appendRepresentation( StringBuilder rv, FieldReferenceSearchTreeNode value ) {
				return null;
			}

		};
	}

	public FieldReferenceSearchTreeNode find( UserField value ) {
		return findHelper( value, new LinkedList<FieldReferenceSearchTreeNode>() );
	}

	private FieldReferenceSearchTreeNode findHelper( UserField value, LinkedList<FieldReferenceSearchTreeNode> alreadyVisited ) {
		if( value.equals( this.field ) ) {
			return this;
		}
		alreadyVisited.add( this );
		for( FieldReferenceSearchTreeNode child : children ) {
			if( !find( alreadyVisited, child ) ) {
				FieldReferenceSearchTreeNode find = child.findHelper( value, alreadyVisited );
				if( find != null ) {
					return find;
				}
			}
		}
		return null;
	}

	private boolean find( LinkedList<FieldReferenceSearchTreeNode> alreadyVisited, FieldReferenceSearchTreeNode child ) {
		for( FieldReferenceSearchTreeNode node : alreadyVisited ) {
			if( node.field != null && child.field != null ) {
				if( node.field.getValueType() == child.field.getValueType() ) {
					if( node.field.getName() == child.field.getName() ) {
						if( node.field.getDeclaringType() == child.field.getDeclaringType() ) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public static void initFields( UserField sceneType ) {
		makeChildren( root, sceneType );
	}

	private static void makeChildren( FieldReferenceSearchTreeNode parentNode, UserField userField ) {
		FieldReferenceSearchTreeNode childNode = createFieldNode( parentNode, userField );
		for( UserField field : ((NamedUserType)userField.getValueType()).fields ) {
			if( field.getValueType() instanceof NamedUserType ) {
				if( root.find( field ) == null ) {
					makeChildren( childNode, field );
				} else {
					childNode.addChild( root.find( field ) );
				}
			} else
				createFieldNode( childNode, field );
		}
	}

	@Override
	public String toString() {
		switch( type ) {
		case ROOT:
			return "root";
		case HEADER_FIELD:
			return "Fields (" + getNumChildren() + "):";
		case FIELD:
			return field.getValueType().getName() + " " + field.getName() + " (" + references.size() + ")";
		default:
			return "UNHANDLED NODE TYPE";
		}
	}

	public void addReference( FieldAccess access ) {
		this.references.add( access );
	}
	
	public List<FieldAccess> getReferences(){
		return this.references;
	}
}
