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
package edu.cmu.cs.dennisc.cascade;

/**
 * @author Dennis Cosgrove
 */
public abstract class Node implements edu.cmu.cs.dennisc.croquet.RetargetingData {
	private static java.util.Map< java.util.UUID, Node > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static <N extends Node> N lookup( java.util.UUID id ) {
		System.err.println( "lookup: " + id );
		return (N)map.get( id );
	}
	
	private java.util.UUID id;
	private Node parent = null;
	private Node nextSibling = null;
	protected java.util.List<Node> children = null;
	
	public Node() {
		this.setId( java.util.UUID.randomUUID() );
		if( this instanceof SeparatorFillIn ) {
			//pass
		} else {
			System.err.println( "Node: " + this.id + " " + this );
			//Thread.dumpStack();
		}
	}
	public Node( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.decode( binaryDecoder );
	}
	public java.util.UUID getId() {
		return this.id;
	}
	private void setId( java.util.UUID id ) {
		this.id = id;
		map.put( id, this );
	}
	
	protected abstract void decodeInternal( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder );
	protected abstract void encodeInternal( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder );
	public final void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		this.setId( binaryDecoder.decodeId() );
		System.err.println( "decode: " + this.id + " " + this );
		this.decodeInternal( binaryDecoder );
		final int N = binaryDecoder.decodeInt();
		this.children = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		for( int i=0; i<N; i++ ) {
			Node child = binaryDecoder.decodeBinaryEncodableAndDecodable();
			this.addChild( child );
		}
	}
	public final void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.id );
		this.encodeInternal( binaryEncoder );
		
		java.util.List< Node > children = this.getChildren();
		final int N = children.size();
		binaryEncoder.encode( N );
		for( int i=0; i<N; i++ ) {
			binaryEncoder.encode( children.get( i ) );
		}
	}
	private void internalAddKeyValuePairs( edu.cmu.cs.dennisc.croquet.Retargeter retargeter, Node replacement ) {
		retargeter.addKeyValuePair( this.getId(), replacement.getId() );
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "adding replacement:", this, replacement );
		System.err.println( "adding replacement: " +  this.getId() + " -> " + replacement.getId() );
		java.util.List<Node> children = this.getChildren();
		java.util.List<Node> replacementChildren = replacement.getChildren();
		final int N = children.size();
		assert N == replacementChildren.size();
		for( int i=0; i<N; i++ ) {
			Node child = children.get( i );
			Node replacementChild = replacementChildren.get( i );
			//children.set( i, replacementChild );
			child.internalAddKeyValuePairs( retargeter, replacementChild );
		}
	}	
	public void addKeyValuePairs( edu.cmu.cs.dennisc.croquet.Retargeter retargeter, edu.cmu.cs.dennisc.croquet.RetargetingData replacement ) {
		this.internalAddKeyValuePairs( retargeter, (Node)replacement );
	}
	
	protected void addChild( Node node ) {
		if( this.children.size() > 0 ) {
			Node prevLast = this.children.get( this.children.size()-1 );
			prevLast.nextSibling = node;
		}
		node.parent = this;
		node.nextSibling = null;
		this.children.add( node );
	}
	protected abstract void addPrefixChildren();
	protected abstract void cleanUp();
	protected abstract void addChildren();
	public java.util.List<Node> getChildren() {
		 if( this.children != null ) {
			 //pass
		 } else {
			 this.children = new java.util.LinkedList< Node >();
			 this.addPrefixChildren();
			 this.addChildren();
			 this.cleanUp();
		 }
		 return this.children;
	}
	
	protected Node getNextSibling() {
		return this.nextSibling;
	}
	protected Node getParent() {
		return this.parent;
	}
	protected void setParent( Node parent ) {
		this.parent = parent;
	}
	protected boolean isLast() {
		return false;
	}
	protected Blank getRootBlank() {
		if( this.parent != null ) {
			return this.parent.getRootBlank();
		} else {
			return (Blank)this;
		}
	}	
	protected Blank getNearestBlank() {
		return this.parent.getNearestBlank();
	}
	
	protected Blank getNextBlank() {
		Blank blank = this.getNearestBlank();
		if( blank.getNextSibling() != null ) {
			return (Blank)blank.getNextSibling();
		} else {
			if( this.parent != null ) {
				return this.parent.getNextBlank();
			} else {
				return null;
			}
		}
	}
	protected abstract Node getNextNode();
}
