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

package org.alice.ide.clipboard;

abstract class ClipboardOperation extends org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertOperation { 
	public ClipboardOperation( java.util.UUID id, org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair ) {
		super( id, blockStatementIndexPair );
	}
	@Override
	protected final edu.cmu.cs.dennisc.alice.ast.Statement createStatement() {
		return org.alice.ide.ast.NodeUtilities.createDoInOrder();
	}
}

class CopyClipboardOperation extends ClipboardOperation {
	private static java.util.Map< org.alice.ide.codeeditor.BlockStatementIndexPair, CopyClipboardOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized CopyClipboardOperation getInstance( org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair ) {
		assert blockStatementIndexPair != null;
		CopyClipboardOperation rv = map.get( blockStatementIndexPair );
		if( rv != null ) {
			//pass
		} else {
			rv = new CopyClipboardOperation( blockStatementIndexPair );
			map.put( blockStatementIndexPair, rv );
		}
		return rv;
	}
	private CopyClipboardOperation( org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair ) {
		super( java.util.UUID.fromString( "fc162a45-2175-4ccf-a5f2-d3de969692c3" ), blockStatementIndexPair );
	}
}

class PasteClipboardOperation extends ClipboardOperation {
	private static java.util.Map< org.alice.ide.codeeditor.BlockStatementIndexPair, PasteClipboardOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized PasteClipboardOperation getInstance( org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair ) {
		assert blockStatementIndexPair != null;
		PasteClipboardOperation rv = map.get( blockStatementIndexPair );
		if( rv != null ) {
			//pass
		} else {
			rv = new PasteClipboardOperation( blockStatementIndexPair );
			map.put( blockStatementIndexPair, rv );
		}
		return rv;
	}
	private PasteClipboardOperation( org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair ) {
		super( java.util.UUID.fromString( "4dea691b-af8f-4991-80e2-3db880f1883f" ), blockStatementIndexPair );
	}
}


/**
 * @author Dennis Cosgrove
 */
public class Clipboard extends org.lgna.croquet.components.DragComponent {
	class ClipboardDragModel extends org.alice.ide.croquet.models.CodeDragModel {
		public ClipboardDragModel() {
			super( java.util.UUID.fromString( "d6c25f14-7ed2-4cb8-90dd-f621af830060" ) );
		}
		@Override
		protected edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > getExpressionType() {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.VOID_TYPE;
		}
//		@Override
//		public java.util.List< ? extends org.lgna.croquet.DropReceptor > createListOfPotentialDropReceptors( org.lgna.croquet.components.DragComponent dragSource ) {
//			if( Clipboard.this.stack.isEmpty() ) {
//				return java.util.Collections.emptyList();
//			} else {
//				org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
//				if( ide != null ) {
//					org.alice.ide.codeeditor.CodeEditor codeEditor = ide.getCodeEditorInFocus();
//					if( codeEditor != null ) {
//						java.util.List< org.lgna.croquet.DropReceptor > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
//						rv.add( codeEditor );
//						return rv;
//					} else {
//						//todo: investigate
//						return java.util.Collections.emptyList();
//					}
//				} else {
//					return java.util.Collections.emptyList();
//				}
//			}
//		}
	}

	private final java.util.Stack< edu.cmu.cs.dennisc.alice.ast.Node > stack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
	private static class SingletonHolder {
		private static Clipboard instance = new Clipboard();
	}
	public static Clipboard getInstance() {
		return SingletonHolder.instance;
	}
	//private org.lgna.croquet.components.Component< ? > subject = new org.lgna.croquet.components.SwingAdapter( new javax.swing.JButton( "todo" ) );
	private org.lgna.croquet.components.FlowPanel subject = new org.lgna.croquet.components.FlowPanel();
	private Clipboard() {
		subject.addComponent( org.alice.ide.IDE.getSingleton().getPreviewFactory().createStatementPane( org.alice.ide.ast.NodeUtilities.createDoInOrder() ) );
		org.alice.ide.IDE.getSingleton().addToConcealedBin( this.subject );
		this.setDragModel( new ClipboardDragModel() );
		this.setMinimumPreferredWidth( 40 );
	}
	
	@Override
	public org.lgna.croquet.components.Component< ? > getSubject() {
		return this.subject;
	}
	
	public org.lgna.croquet.Model getModel( org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair, boolean isCopy ) {
		if( isCopy ) {
			return CopyClipboardOperation.getInstance( blockStatementIndexPair );
		} else {
			return PasteClipboardOperation.getInstance( blockStatementIndexPair );
		}
	}
	
	@Override
	protected java.awt.Dimension getPreferredSize( java.awt.Dimension size ) {
		return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumWidth( size, 40 );
	}
	
	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new java.awt.GridLayout();
	}
	@Override
	protected int getInsetBottom() {
		return 2;
	}
	@Override
	protected int getInsetLeft() {
		return 2;
	}
	@Override
	protected int getInsetRight() {
		return 2;
	}
	@Override
	protected int getInsetTop() {
		return 2;
	}

	
	private static java.awt.Shape createClip( float x, float y, float width, float height, float holeRadius ) {
		float xADelta = width*0.2f;
		float xBDelta = width*0.425f;
		float x0 = x;
		float xC = x+width*0.5f;
		float x1 = x+width;
		float y0 = y;
		float yA = y+height*0.6f;
		float yB = y+height*0.25f;
		float y1 = y+height;
		
		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( x0, y1 );
		path.quadTo( x0, yA, x0+xADelta, yA );
		path.quadTo( x0+xADelta, yB, x0+xBDelta, yB );
		path.quadTo( x0+xBDelta, y0, xC, y0 );
		path.quadTo( x1-xBDelta, y0, x1-xBDelta, yB );
		path.quadTo( x1-xADelta, yB, x1-xADelta, yA );
		path.quadTo( x1, yA, x1, y1 );
		path.closePath();
	
		float holeDiameter = holeRadius*2;
		java.awt.geom.Area area = new java.awt.geom.Area( path );
		area.subtract( new java.awt.geom.Area( new java.awt.geom.Ellipse2D.Float( xC-holeRadius, yB-holeDiameter, holeDiameter, holeDiameter ) ) );
		
		return area;
	}
	private void paintClipboard( java.awt.Graphics2D g2 ) {
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		
		java.awt.Insets insets = this.getInsets();
		float width = this.getWidth() - insets.left - insets.right;
		float height = this.getHeight() - insets.top - insets.bottom;
		
		g2.translate( insets.left, insets.top );
		float round = this.getWidth()*0.1f;
		java.awt.geom.RoundRectangle2D board = new java.awt.geom.RoundRectangle2D.Float( 0.025f*width, 0.1f*height, 0.95f*width, 0.875f*height, round, round );
		java.awt.Shape clip = createClip( 0.2f*width, 0.01f*height, 0.6f*width, 0.2f*height, 0.02f*height );
		
		g2.setPaint( java.awt.Color.ORANGE.darker() );
		g2.fill( board );
		g2.setPaint( java.awt.Color.BLACK );
		g2.draw( board );

		float x = width*0.1f;
		float y = height*0.15f;
		float w = width*0.8f;
		float h = height*0.775f;
		
		
		g2.setPaint( new java.awt.GradientPaint( x,y, java.awt.Color.LIGHT_GRAY, x+w, y+h, java.awt.Color.WHITE ) );
		g2.fill( new java.awt.geom.Rectangle2D.Float( x, y, w, h ) );
		
		final int SHADOW_SIZE = this.getHeight()/50; 
		if( SHADOW_SIZE > 2 ) {
			g2.translate(x, y);
			g2.setPaint( new java.awt.Color( 31, 31, 31, 127 ) );
			java.awt.geom.GeneralPath pathShadow = new java.awt.geom.GeneralPath();
			pathShadow.moveTo( w, 0 );
			pathShadow.lineTo( w+SHADOW_SIZE, h+SHADOW_SIZE );
			pathShadow.lineTo( 0, h );
			pathShadow.lineTo( w, h );
			pathShadow.closePath();
			g2.fill( pathShadow );
			g2.translate(-x, -y);
		}

		
		g2.setPaint( new java.awt.GradientPaint( 0, height*0.1f, java.awt.Color.LIGHT_GRAY, 0, height*0.2f, java.awt.Color.DARK_GRAY ) );
		g2.fill( clip );
		g2.setPaint( java.awt.Color.BLACK );
		g2.draw( clip );
		g2.translate( -insets.left, -insets.top );
	}

	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.fillRect( x, y, width, height );
	}
	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
	}
	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		this.paintClipboard( g2 );
	}
//	@Override
//	protected javax.swing.JComponent createAwtComponent() {
//		return new javax.swing.JComponent() {
//			@Override
//			protected void paintComponent( java.awt.Graphics g ) {
//				super.paintComponent( g );
//				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//				Clipboard.this.paintClipboard( g2 );
//			}
//			@Override
//			public java.awt.Dimension getPreferredSize() {
//				return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumWidth( super.getPreferredSize(), 40 );
//			}
//		};
//	}
//	public static void main( String[] args ) {
//		org.lgna.croquet.Application application = new org.lgna.croquet.Application() {
//			@Override
//			protected org.lgna.croquet.components.Component< ? > createContentPane() {
//				return new Clipboard();
//			}
//			@Override
//			public org.lgna.croquet.DropReceptor getDropReceptor( org.lgna.croquet.DropSite dropSite ) {
//				return null;
//			}
//			@Override
//			protected void handleAbout( org.lgna.croquet.triggers.Trigger trigger ) {
//			}
//			@Override
//			protected void handleOpenFile( org.lgna.croquet.triggers.Trigger trigger ) {
//			}
//			@Override
//			protected void handlePreferences( org.lgna.croquet.triggers.Trigger trigger ) {
//			}
//			@Override
//			protected void handleQuit( org.lgna.croquet.triggers.Trigger trigger ) {
//				System.exit( 0 );
//			}
//			@Override
//			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
//			}
//		};
//		application.initialize( args );
//		application.getFrame().pack();
//		application.getFrame().setVisible( true );
//	}
}
