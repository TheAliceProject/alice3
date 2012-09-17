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
package edu.cmu.cs.dennisc.java.awt.geom;

/**
 * @author Dennis Cosgrove
 */
public class TransformContext {
	private java.util.Stack<java.awt.geom.AffineTransform> m_affineTransformStack = new java.util.Stack<java.awt.geom.AffineTransform>();
	private java.awt.geom.AffineTransform m_affineTransform = new java.awt.geom.AffineTransform();

	public void initialize() {
		assert m_affineTransformStack.isEmpty();
		m_affineTransform.setToIdentity();
	}

	protected void update( java.awt.geom.AffineTransform affineTransform ) {
	}

	public java.awt.geom.AffineTransform accessAffineTransform() {
		return m_affineTransform;
	}

	public java.awt.geom.AffineTransform getAffineTransform() {
		return new java.awt.geom.AffineTransform( m_affineTransform );
	}

	public void pushAffineTransform() {
		m_affineTransformStack.push( new java.awt.geom.AffineTransform( m_affineTransform ) );
	}

	public void popAffineTransform() {
		m_affineTransform.setTransform( m_affineTransformStack.pop() );
		update( m_affineTransform );
	}

	public void setAffineTransform( java.awt.geom.AffineTransform affineTransform ) {
		m_affineTransform.setTransform( affineTransform );
		update( m_affineTransform );
	}

	public void multiplyAffineTransform( java.awt.geom.AffineTransform affineTransform ) {
		m_affineTransform.concatenate( affineTransform );
		update( m_affineTransform );
	}
}
