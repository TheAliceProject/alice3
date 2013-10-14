/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.java.awt;

/**
 * @author Dennis Cosgrove
 */
public final class GraphicsContext {
	private static final GraphicsContext edtInstance = new GraphicsContext();
	private static final edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<Thread, GraphicsContext> map = edu.cmu.cs.dennisc.java.util.Collections.newInitializingIfAbsentHashMap();

	public static GraphicsContext getInstanceAndPushGraphics( java.awt.Graphics g ) {
		GraphicsContext rv;
		if( javax.swing.SwingUtilities.isEventDispatchThread() ) {
			rv = edtInstance;
		} else {
			rv = map.getInitializingIfAbsent( Thread.currentThread(), new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<Thread, GraphicsContext>() {
				public edu.cmu.cs.dennisc.java.awt.GraphicsContext initialize( java.lang.Thread key ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "note: creating graphics context on thread", key );
					return new GraphicsContext();
				}
			} );
		}
		rv.pushGraphics( (java.awt.Graphics2D)g );
		return rv;
	}

	private final java.util.Stack<java.awt.Graphics2D> g2Stack = edu.cmu.cs.dennisc.java.util.Collections.newStack();

	private final java.util.Stack<java.awt.Paint> paintStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
	private final java.util.Stack<java.awt.Stroke> strokeStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
	private final java.util.Stack<java.awt.Font> fontStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
	private final java.util.Stack<java.awt.Shape> clipStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
	private final java.util.Stack<java.awt.geom.AffineTransform> transformStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();

	private final java.util.Stack<Object> antialiasingStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
	private final java.util.Stack<Object> textAntialiasingStack = edu.cmu.cs.dennisc.java.util.Collections.newStack();

	public GraphicsContext() {
	}

	public void pushGraphics( java.awt.Graphics2D g ) {
		this.g2Stack.push( g );
	}

	public void popGraphics() {
		this.g2Stack.pop();
	}

	public void pushFont() {
		this.fontStack.push( this.g2Stack.peek().getFont() );
	}

	public void popFont() {
		this.g2Stack.peek().setFont( this.fontStack.pop() );
	}

	public void pushClip() {
		this.clipStack.push( this.g2Stack.peek().getClip() );
	}

	public void popClip() {
		this.g2Stack.peek().setClip( this.clipStack.pop() );
	}

	public void pushTransform() {
		this.transformStack.push( this.g2Stack.peek().getTransform() );
	}

	public void popTransform() {
		this.g2Stack.peek().setTransform( this.transformStack.pop() );
	}

	public void pushPaint() {
		this.paintStack.push( this.g2Stack.peek().getPaint() );
	}

	public void popPaint() {
		this.g2Stack.peek().setPaint( this.paintStack.pop() );
	}

	public void pushStroke() {
		this.strokeStack.push( this.g2Stack.peek().getStroke() );
	}

	public void popStroke() {
		this.g2Stack.peek().setStroke( this.strokeStack.pop() );
	}

	private static void pushAndSetRenderingHint( java.awt.Graphics2D g2, java.util.Stack<Object> stack, java.awt.RenderingHints.Key key, Object value ) {
		stack.push( g2.getRenderingHint( key ) );
		g2.setRenderingHint( key, value );
	}

	private static void popRenderingHint( java.awt.Graphics2D g2, java.util.Stack<Object> stack, java.awt.RenderingHints.Key key ) {
		g2.setRenderingHint( key, stack.pop() );
	}

	public void pushAndSetAntialiasing( boolean b ) {
		pushAndSetRenderingHint( this.g2Stack.peek(), this.antialiasingStack, java.awt.RenderingHints.KEY_ANTIALIASING, b ? java.awt.RenderingHints.VALUE_ANTIALIAS_ON : java.awt.RenderingHints.VALUE_ANTIALIAS_OFF );
	}

	public void popAntialiasing() {
		popRenderingHint( this.g2Stack.peek(), this.antialiasingStack, java.awt.RenderingHints.KEY_ANTIALIASING );
	}

	public void pushAndSetTextAntialiasing( boolean b ) {
		pushAndSetRenderingHint( this.g2Stack.peek(), this.textAntialiasingStack, java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, b ? java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON : java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_OFF );
	}

	public void popTextAntialiasing() {
		popRenderingHint( this.g2Stack.peek(), this.textAntialiasingStack, java.awt.RenderingHints.KEY_TEXT_ANTIALIASING );
	}
}
