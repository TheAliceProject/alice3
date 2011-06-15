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

abstract class FromClipboardOperation extends org.alice.ide.croquet.models.ast.cascade.statement.StatementInsertOperation { 
	private final boolean isCopy;
	public FromClipboardOperation( java.util.UUID id, org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair, boolean isCopy ) {
		super( id, blockStatementIndexPair );
		this.isCopy = isCopy;
	}

	@Override
	protected final edu.cmu.cs.dennisc.alice.ast.Statement createStatement() {
		edu.cmu.cs.dennisc.alice.ast.AbstractNode node = Clipboard.getInstance().peek();
		//todo: recast if necessary
		if( node instanceof edu.cmu.cs.dennisc.alice.ast.Statement ) {
			edu.cmu.cs.dennisc.alice.ast.Statement statement = (edu.cmu.cs.dennisc.alice.ast.Statement)node;
			if( isCopy ) {
				return org.alice.ide.IDE.getSingleton().createCopy( statement );
			} else {
				Clipboard.getInstance().pop();
				return statement;
			}
		} else {
			return null;
		}
	}
}

class CopyFromClipboardOperation extends FromClipboardOperation {
	private static java.util.Map< org.alice.ide.codeeditor.BlockStatementIndexPair, CopyFromClipboardOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized CopyFromClipboardOperation getInstance( org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair ) {
		assert blockStatementIndexPair != null;
		CopyFromClipboardOperation rv = map.get( blockStatementIndexPair );
		if( rv != null ) {
			//pass
		} else {
			rv = new CopyFromClipboardOperation( blockStatementIndexPair );
			map.put( blockStatementIndexPair, rv );
		}
		return rv;
	}
	private CopyFromClipboardOperation( org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair ) {
		super( java.util.UUID.fromString( "fc162a45-2175-4ccf-a5f2-d3de969692c3" ), blockStatementIndexPair, true );
	}
}

class PasteFromClipboardOperation extends FromClipboardOperation {
	private static java.util.Map< org.alice.ide.codeeditor.BlockStatementIndexPair, PasteFromClipboardOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized PasteFromClipboardOperation getInstance( org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair ) {
		assert blockStatementIndexPair != null;
		PasteFromClipboardOperation rv = map.get( blockStatementIndexPair );
		if( rv != null ) {
			//pass
		} else {
			rv = new PasteFromClipboardOperation( blockStatementIndexPair );
			map.put( blockStatementIndexPair, rv );
		}
		return rv;
	}
	private PasteFromClipboardOperation( org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair ) {
		super( java.util.UUID.fromString( "4dea691b-af8f-4991-80e2-3db880f1883f" ), blockStatementIndexPair, false );
	}
}


class CopyToClipboardOperation extends org.alice.ide.operations.ActionOperation {
	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractNode, CopyToClipboardOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized CopyToClipboardOperation getInstance( edu.cmu.cs.dennisc.alice.ast.AbstractNode node ) {
		assert node != null;
		CopyToClipboardOperation rv = map.get( node );
		if( rv != null ) {
			//pass
		} else {
			rv = new CopyToClipboardOperation( node );
			map.put( node, rv );
		}
		return rv;
	}
	private final edu.cmu.cs.dennisc.alice.ast.AbstractNode node;
	private CopyToClipboardOperation( edu.cmu.cs.dennisc.alice.ast.AbstractNode node ) {
		super( org.lgna.croquet.Application.UI_STATE_GROUP, java.util.UUID.fromString( "9ae5c84b-60f4-486f-aaf1-bd7b5dc6ba86" ) );
		this.node = org.alice.ide.IDE.getSingleton().createCopy( node );
	}
	@Override
	protected void perform( org.lgna.croquet.history.ActionOperationStep step ) {
		Clipboard.getInstance().push( this.node );
		step.finish();
	}
}

class CutToClipboardEdit extends org.lgna.croquet.edits.Edit {
	private edu.cmu.cs.dennisc.alice.ast.Statement statement;
	private edu.cmu.cs.dennisc.alice.ast.BlockStatement originalBlockStatement;
	private int originalIndex;
	public CutToClipboardEdit( org.lgna.croquet.history.CompletionStep completionStep, edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		super( completionStep );
		this.statement = statement;
		this.originalBlockStatement = (edu.cmu.cs.dennisc.alice.ast.BlockStatement)this.statement.getParent();;
		assert this.originalBlockStatement != null;
		this.originalIndex = this.originalBlockStatement.statements.indexOf( this.statement );
		assert this.originalIndex != -1;
	}
	public CutToClipboardEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
		java.util.UUID statementId = binaryDecoder.decodeId();
		this.statement = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.lookupNode( project, statementId );
		java.util.UUID blockStatementId = binaryDecoder.decodeId();
		this.originalBlockStatement = edu.cmu.cs.dennisc.alice.project.ProjectUtilities.lookupNode( project, blockStatementId );
		this.originalIndex = binaryDecoder.decodeInt();
	}
	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.statement.getUUID() );
		binaryEncoder.encode( this.originalBlockStatement.getUUID() );
		binaryEncoder.encode( this.originalIndex );
	}
	@Override
	protected void doOrRedoInternal( boolean isDo ) {
		Clipboard.getInstance().push( this.statement );
		this.originalBlockStatement.statements.remove( this.originalIndex );
	}
	@Override
	protected void undoInternal() {
		Clipboard.getInstance().pop();
		this.originalBlockStatement.statements.add( this.originalIndex, this.statement );
	}
	@Override
	protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		rv.append( "cut to clipboard" );
		return rv;
	}
}

class CutToClipboardOperation extends org.alice.ide.operations.ActionOperation {
	private static java.util.Map< edu.cmu.cs.dennisc.alice.ast.Statement, CutToClipboardOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized CutToClipboardOperation getInstance( edu.cmu.cs.dennisc.alice.ast.Statement node ) {
		assert node != null;
		CutToClipboardOperation rv = map.get( node );
		if( rv != null ) {
			//pass
		} else {
			rv = new CutToClipboardOperation( node );
			map.put( node, rv );
		}
		return rv;
	}
	private final edu.cmu.cs.dennisc.alice.ast.Statement statement;
	private CutToClipboardOperation( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		super( org.lgna.croquet.Application.UI_STATE_GROUP, java.util.UUID.fromString( "9ae5c84b-60f4-486f-aaf1-bd7b5dc6ba86" ) );
		this.statement = statement;
	}
	@Override
	protected void perform( org.lgna.croquet.history.ActionOperationStep step ) {
		step.commitAndInvokeDo( new CutToClipboardEdit( step, statement ) );
	}
}


//todo
class ClipboardDropSite implements org.lgna.croquet.DropSite {
	public ClipboardDropSite() {
	}
	public ClipboardDropSite( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
	}
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
	}
}

/**
 * @author Dennis Cosgrove
 */
public class Clipboard extends org.lgna.croquet.components.DragComponent implements org.lgna.croquet.DropReceptor {
	class ClipboardDragModel extends org.alice.ide.croquet.models.CodeDragModel {
		public ClipboardDragModel() {
			super( java.util.UUID.fromString( "d6c25f14-7ed2-4cb8-90dd-f621af830060" ) );
		}
		@Override
		protected edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > getExpressionType() {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.VOID_TYPE;
		}
	}

	private final java.util.Stack< edu.cmu.cs.dennisc.alice.ast.AbstractNode > stack = edu.cmu.cs.dennisc.java.util.Collections.newStack();
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
//		stack.push( org.alice.ide.ast.NodeUtilities.createWhileLoop( new edu.cmu.cs.dennisc.alice.ast.BooleanLiteral( true ) ) );
//		stack.push( org.alice.ide.ast.NodeUtilities.createDoTogether() );
//		stack.push( org.alice.ide.ast.NodeUtilities.createDoInOrder() );		
		org.alice.ide.IDE.getSingleton().addToConcealedBin( this.subject );
		this.setDragModel( new ClipboardDragModel() );
		this.setMinimumPreferredWidth( 40 );
		this.refresh();
	}
	
	@Override
	protected javax.swing.JToolTip createToolTip( javax.swing.JToolTip jToolTip ) {
		return new edu.cmu.cs.dennisc.javax.swing.tooltips.JToolTip( this.subject.getAwtComponent() );
	}
	private void refresh() {
		this.subject.forgetAndRemoveAllComponents();
		if( this.stack.isEmpty() ) {
			this.setToolTipText( null );
		} else {
			this.setToolTipText( "" );
			edu.cmu.cs.dennisc.alice.ast.AbstractNode node = this.stack.peek();
			if( node instanceof edu.cmu.cs.dennisc.alice.ast.Statement ) {
				edu.cmu.cs.dennisc.alice.ast.Statement statement = (edu.cmu.cs.dennisc.alice.ast.Statement)node;
				subject.addComponent( org.alice.ide.IDE.getSingleton().getPreviewFactory().createStatementPane( statement ) );
			}
		}
		this.repaint();
	}
	public edu.cmu.cs.dennisc.alice.ast.AbstractNode peek() {
		return this.stack.peek();
	}
	public void push( edu.cmu.cs.dennisc.alice.ast.AbstractNode node ) {
		this.stack.push( node );
		this.refresh();
	}
	public edu.cmu.cs.dennisc.alice.ast.AbstractNode pop() {
		edu.cmu.cs.dennisc.alice.ast.AbstractNode rv = this.stack.pop();
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
	public boolean isPotentiallyAcceptingOf( org.lgna.croquet.components.DragComponent source ) {
		return source instanceof org.alice.ide.common.AbstractStatementPane;
	}
	public org.lgna.croquet.Model dragDropped( org.lgna.croquet.history.DragStep step ) {
		org.alice.ide.common.AbstractStatementPane pane = (org.alice.ide.common.AbstractStatementPane)step.getDragSource();
		edu.cmu.cs.dennisc.alice.ast.Statement statement = pane.getStatement();
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
	
	public org.lgna.croquet.Model getModel( org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair, boolean isCopy ) {
		if( isCopy ) {
			return CopyFromClipboardOperation.getInstance( blockStatementIndexPair );
		} else {
			return PasteFromClipboardOperation.getInstance( blockStatementIndexPair );
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
			
			java.awt.Shape paper = new java.awt.geom.Rectangle2D.Float( 0, 0, w, h );
			
			final boolean IS_SIMPLE = true;
			if( IS_SIMPLE || this.dragReceptorState != DragReceptorState.IDLE ) {
				g2.setPaint( new java.awt.GradientPaint( x,y, java.awt.Color.LIGHT_GRAY, x+w, y+h, java.awt.Color.WHITE ) );
				g2.fill( paper );
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
