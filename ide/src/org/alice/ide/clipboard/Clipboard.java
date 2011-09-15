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

/**
 * @author Dennis Cosgrove
 */
public class Clipboard extends org.lgna.croquet.components.DragComponent< javax.swing.AbstractButton, org.alice.ide.clipboard.ClipboardDragModel > implements org.lgna.croquet.DropReceptor {

	private final java.util.Stack< org.lgna.project.ast.AbstractNode > stack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
	private static class SingletonHolder {
		private static Clipboard instance = new Clipboard();
	}
	public static Clipboard getInstance() {
		return SingletonHolder.instance;
	}
	private ClipboardDropSite dropSite = new ClipboardDropSite();
	private org.lgna.croquet.components.FlowPanel subject = new org.lgna.croquet.components.FlowPanel();
	
	private static enum DragReceptorState {
		IDLE( java.awt.Color.ORANGE.darker() ),
		STARTED( java.awt.Color.YELLOW ),
		ENTERED( java.awt.Color.GREEN );
		private final java.awt.Paint paint;
		private DragReceptorState( java.awt.Paint paint ) {
			this.paint = paint;
		}
		public java.awt.Paint getPaint() {
			return this.paint;
		}
		
	};
	private DragReceptorState dragReceptorState = DragReceptorState.IDLE;
	private Clipboard() {
		super( new ClipboardDragModel() );
		this.setMinimumPreferredWidth( 40 );
		this.refresh();
	}
	@Override
	public org.alice.ide.clipboard.ClipboardDragModel getModel() {
		if( this.stack != null && this.stack.size() > 0 ) {
			return super.getModel();
		} else {
			return null;
		}
	}
	
	private void refresh() {
		this.subject.forgetAndRemoveAllComponents();
		if( this.stack.isEmpty() ) {
			this.setToolTipText( null );
		} else {
			this.setToolTipText( "" );
			org.lgna.project.ast.AbstractNode node = this.stack.peek();
			if( node instanceof org.lgna.project.ast.Statement ) {
				org.lgna.project.ast.Statement statement = (org.lgna.project.ast.Statement)node;
				subject.addComponent( org.alice.ide.x.PreviewAstI18nFactory.getInstance().createStatementPane( statement ) );
				subject.revalidateAndRepaint();
			}
		}
		this.repaint();
	}
	public org.lgna.project.ast.AbstractNode peek() {
		if( this.stack.size() > 0  ) {
			return this.stack.peek();
		} else {
			return null;
		}
	}
	public void push( org.lgna.project.ast.AbstractNode node ) {
		this.stack.push( node );
		this.refresh();
	}
	public org.lgna.project.ast.AbstractNode pop() {
		org.lgna.project.ast.AbstractNode rv = this.stack.pop();
		this.refresh();
		return rv;
	}
	
	private void setDragReceptorState( DragReceptorState dragReceptorState ) {
		this.dragReceptorState = dragReceptorState;
		this.repaint();
	}
	public void dragStarted( org.lgna.croquet.history.DragStep step ) {
		this.setDragReceptorState( DragReceptorState.STARTED );
	}
	public void dragEntered( org.lgna.croquet.history.DragStep step ) {
		this.setDragReceptorState( DragReceptorState.ENTERED );
//		step.getDragSource().hideDragProxy();
	}
	public org.lgna.croquet.DropSite dragUpdated( org.lgna.croquet.history.DragStep step ) {
		return this.dropSite;
	}
	public boolean isPotentiallyAcceptingOf( org.lgna.croquet.DragModel dragModel ) {
		return dragModel instanceof org.alice.ide.ast.draganddrop.statement.AbstractStatementDragModel;
	}
	public org.lgna.croquet.Model dragDropped( org.lgna.croquet.history.DragStep step ) {
		org.alice.ide.common.AbstractStatementPane pane = (org.alice.ide.common.AbstractStatementPane)step.getDragSource();
		org.lgna.project.ast.Statement statement = pane.getStatement();
		boolean isCopy = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.isQuoteControlUnquoteDown( step.getLatestMouseEvent() );
		if( isCopy ) {
			return CopyToClipboardOperation.getInstance( statement );
		} else {
			return CutToClipboardOperation.getInstance( statement );
		}
	}
	public void dragExited( org.lgna.croquet.history.DragStep step, boolean isDropRecipient ) {
//		step.getDragSource().showDragProxy();
		this.setDragReceptorState( DragReceptorState.STARTED );
	}
	public void dragStopped( org.lgna.croquet.history.DragStep step ) {
		this.setDragReceptorState( DragReceptorState.IDLE );
	}
	
	public <R extends org.lgna.croquet.DropReceptor> org.lgna.croquet.resolvers.CodableResolver< org.lgna.croquet.DropReceptor > getCodableResolver() {
		return new org.lgna.croquet.resolvers.SingletonResolver( this );
	}
	public org.lgna.croquet.components.TrackableShape getTrackableShape( org.lgna.croquet.DropSite potentialDropSite ) {
		return this;
	}
	public String getTutorialNoteText( org.lgna.croquet.Model model, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		return "clipboard";
	}
	public org.lgna.croquet.components.JComponent< ? > getViewController() {
		return this;
	}
	
	
	@Override
	public org.lgna.croquet.components.Component< ? > getSubject() {
		return this.subject;
	}

	@Override
	protected javax.swing.AbstractButton createAwtComponent() {
		class JClipboard extends javax.swing.AbstractButton {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumWidth( super.getPreferredSize(), 40 );
			}
			@Override
			public javax.swing.JToolTip createToolTip() {
				return new edu.cmu.cs.dennisc.javax.swing.tooltips.JToolTip( Clipboard.this.subject.getAwtComponent() );
			}
		}
		JClipboard rv = new JClipboard();
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2,2,2,2 ) );
		return rv;
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
	
		if( holeRadius > 2 ) {
			float holeDiameter = holeRadius*2;
			java.awt.geom.Area area = new java.awt.geom.Area( path );
			area.subtract( new java.awt.geom.Area( new java.awt.geom.Ellipse2D.Float( xC-holeRadius, yB-holeDiameter, holeDiameter, holeDiameter ) ) );
			return area;
		} else {
			return path;
		}
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
		
		g2.setPaint( this.dragReceptorState.getPaint() );
		g2.fill( board );
		g2.setPaint( java.awt.Color.BLACK );
		g2.draw( board );

		if( this.stack.isEmpty() ) {
			//pass
		} else {

			float x = width*0.1f;
			float y = height*0.2f;
			float w = width*0.8f;
			float h = height*0.725f;
			
			g2.translate(x, y);
			
			java.awt.geom.Rectangle2D.Float paper = new java.awt.geom.Rectangle2D.Float( 0, 0, w, h );
			
			final boolean IS_SIMPLE = true;
			if( IS_SIMPLE || this.dragReceptorState != DragReceptorState.IDLE ) {
				g2.setPaint( new java.awt.GradientPaint( x,y, java.awt.Color.LIGHT_GRAY, x+w, y+h, java.awt.Color.WHITE ) );
				g2.fill( paper );
				
				g2.setPaint( java.awt.Color.DARK_GRAY );

				if( this.stack.size() > 1 ) {
					edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g2, Integer.toString( this.stack.size() ), paper );
				}
				
				final int SHADOW_SIZE = this.getHeight()/50; 
				if( SHADOW_SIZE > 2 ) {
					g2.setPaint( new java.awt.Color( 31, 31, 31, 127 ) );
					java.awt.geom.GeneralPath pathShadow = new java.awt.geom.GeneralPath();
					pathShadow.moveTo( w, 0 );
					pathShadow.lineTo( w+SHADOW_SIZE, h+SHADOW_SIZE );
					pathShadow.lineTo( 0, h );
					pathShadow.lineTo( w, h );
					pathShadow.closePath();
					g2.fill( pathShadow );
				}
			} else {
				java.awt.Shape prevClip = g2.getClip();
				g2.setClip( paper );
				final float SCALE = 0.4f;
				java.awt.geom.AffineTransform prevTransform = g2.getTransform();
				g2.scale( SCALE, SCALE );
				this.subject.getAwtComponent().print( g2 );
				g2.setTransform( prevTransform );
				g2.setClip( prevClip );
			}
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
