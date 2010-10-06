import java.awt.event.MouseEvent;

enum Shape {
	RECTANGLE(java.awt.geom.Rectangle2D.Double.class), 
	ELLIPSE(java.awt.geom.Ellipse2D.Double.class);
	private Class< ? extends java.awt.Shape > cls;

	private Shape( Class< ? extends java.awt.Shape > cls ) {
		this.cls = cls;
	}
	public java.awt.Shape createAWTShape( double x, double y, double width, double height ) {
		java.lang.reflect.Constructor< ? extends java.awt.Shape > cnstrctr = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getConstructor( this.cls, Double.TYPE, Double.TYPE, Double.TYPE, Double.TYPE );
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( cnstrctr, x, y, width, height );
	}
};

abstract class DefaultDragComponent extends zoot.ZDragComponent {
	@Override
	protected int getInsetTop() {
		return 0;
	}
	@Override
	protected int getInsetLeft() {
		return 0;
	}
	@Override
	protected int getInsetBottom() {
		return 0;
	}
	@Override
	protected int getInsetRight() {
		return 0;
	}
}

abstract class ShapeDragComponent extends DefaultDragComponent {
	private Shape shape;

	public ShapeDragComponent( Shape shape ) {
		this.setShape( shape );
	}
	public Shape getShape() {
		return this.shape;
	}
	public void setShape( Shape shape ) {
		this.shape = shape;
	}
	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		g2.fill( this.shape.createAWTShape( x, y, width, height ) );
	}
	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		this.fillBounds( g2, x, y, width, height );
	}
}

class Template extends ShapeDragComponent {
	public Template( Shape shape ) {
		super( shape );
		this.setBackground( java.awt.Color.RED );
		this.setForeground( java.awt.Color.BLACK );
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return new java.awt.Dimension( 64, 32 );
	}
	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
	}
}

class SetBoundsOperation extends zoot.AbstractActionOperation {
	private javax.swing.JComponent component;
	private java.awt.Rectangle boundsPrev;
	private java.awt.Rectangle boundsNext;
	public SetBoundsOperation( javax.swing.JComponent component, int xNext, int yNext, int widthNext, int heightNext ) {
		this.component = component;
		this.boundsNext = new java.awt.Rectangle( xNext, yNext, widthNext, heightNext );
	}
	private void setBounds( java.awt.Rectangle bounds ) {
		component.setBounds( bounds );
		component.revalidate();
	}
	public void perform( zoot.ActionContext actionContext ) {
		this.boundsPrev = this.component.getBounds();
		actionContext.commit( new javax.swing.undo.AbstractUndoableEdit() {
			@Override
			public void undo() throws javax.swing.undo.CannotUndoException {
				setBounds( boundsPrev );
			}
			@Override
			public void redo() throws javax.swing.undo.CannotRedoException {
				setBounds( boundsNext );
			}
		} );
	}
	
}

class ResizeControlsPane extends edu.cmu.cs.dennisc.swing.SpringPane {
	class ResizeControl extends javax.swing.JComponent {
		class MouseAdapter implements java.awt.event.MouseListener, java.awt.event.MouseMotionListener {
			private int xPress;
			private int yPress;
			public void mouseEntered( MouseEvent e ) {
			}
			public void mousePressed( MouseEvent e ) {
				this.xPress = e.getX();
				this.yPress = e.getY();
			}
			public void mouseMoved( MouseEvent e ) {
			}
			public void mouseDragged( MouseEvent e ) {
			}
			public void mouseReleased( MouseEvent e ) {
				int xDelta = e.getX() - this.xPress;
				int yDelta = e.getY() - this.yPress;
				ResizeControl.this.handleResize( e, xDelta, yDelta );
			}
			public void mouseExited( MouseEvent e ) {
			}
			
			public void mouseClicked( MouseEvent e ) {
			}
		}

		private MouseAdapter mouseAdapter = new MouseAdapter();
		private Horizontal horizontal;
		private Vertical vertical;
		
		private void handleResize( MouseEvent e, int xDelta, int yDelta ) {
			javax.swing.JComponent component = (javax.swing.JComponent)this.getParent().getParent();
			java.awt.Point location = component.getLocation();
			java.awt.Dimension size = component.getPreferredSize();
			switch( horizontal ) {
			case EAST:
				size.width += xDelta; 
				break;
			case WEST:
				location.x += xDelta; 
				size.width -= xDelta; 
				break;
			}
			switch( vertical ) {
			case NORTH:
				location.y += yDelta; 
				size.height -= yDelta; 
				break;
			case SOUTH:
				size.height += yDelta; 
				break;
			}
			zoot.ZManager.performIfAppropriate( new SetBoundsOperation( component, location.x, location.y, size.width, size.height ), e, zoot.ZManager.CANCEL_IS_WORTHWHILE );
//			component.setLocation( location );
//			component.setPreferredSize( size );
//			component.revalidate();
		}
		
		public ResizeControl( Horizontal horizontal, Vertical vertical ) {
			this.horizontal = horizontal;
			this.vertical = vertical;
		}
		
		@Override
		public java.awt.Dimension getPreferredSize() {
			return new java.awt.Dimension( 8, 8 );
		}
		@Override
		public void addNotify() {
			super.addNotify();
			this.addMouseListener( this.mouseAdapter );
			this.addMouseMotionListener( this.mouseAdapter );
		}
		@Override
		public void removeNotify() {
			this.removeMouseMotionListener( this.mouseAdapter );
			this.removeMouseListener( this.mouseAdapter );
			super.removeNotify();
		}
		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			super.paintComponent( g );
			g.setColor( java.awt.Color.BLACK );
			g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
			g.setColor( java.awt.Color.WHITE );
			g.drawRect( 0, 0, this.getWidth()-1, this.getHeight()-1 );
		}
	}
	public ResizeControlsPane() {
		for( Horizontal horizontal : Horizontal.values() ) {
			for( Vertical vertical : Vertical.values() ) {
				if( horizontal == Horizontal.CENTER && vertical == Vertical.CENTER ) {
					//pass
				} else {
					ResizeControl resizeControl = new ResizeControl( horizontal, vertical );
					this.add( resizeControl, horizontal, 0, vertical, 0 );
				}
			}
		}
		this.setOpaque( false );
		//this.revalidate();
	}
}

class Instance extends ShapeDragComponent {
	private ResizeControlsPane resizeControlsPane = new ResizeControlsPane();
	private java.util.UUID uuid = java.util.UUID.randomUUID();
	public Instance( Shape shape ) {
		super( shape );
		this.setLeftButtonDoubleClickOperation( new zoot.AbstractActionOperation() {
			public void perform( zoot.ActionContext actionContext ) {
				Instance.this.setSelected( ! Instance.this.isSelected() );
			}
		} );
		this.setSelected( false );
		this.setLayout( new java.awt.BorderLayout() );
		this.add( this.resizeControlsPane, java.awt.BorderLayout.CENTER );
		this.revalidate();
	}
	@Override
	protected boolean isClickReservedForSelection() {
		return true;
	}
	public java.util.UUID getUUID() {
		return this.uuid;
	}
	public boolean isSelected() {
		return this.resizeControlsPane.isVisible();
	}
	public void setSelected( boolean isSelected ) {
		this.resizeControlsPane.setVisible( isSelected );
		this.revalidate();
		this.repaint();
	}
	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
	}
}

class LaissezFaireLayoutManager implements java.awt.LayoutManager {
	public void layoutContainer( java.awt.Container parent ) {
		for( java.awt.Component component : parent.getComponents() ) {
			component.setSize( component.getPreferredSize() );
		}
	}
	public java.awt.Dimension minimumLayoutSize( java.awt.Container parent ) {
		return new java.awt.Dimension( 0, 0 );
	}
	public java.awt.Dimension preferredLayoutSize( java.awt.Container parent ) {
		int xMax = -1;
		int yMax = -1;
		for( java.awt.Component component : parent.getComponents() ) {
			java.awt.Point location = component.getLocation();
			java.awt.Dimension size = component.getPreferredSize();
			xMax = Math.max( xMax, location.x + size.width );
			yMax = Math.max( yMax, location.y + size.height );
		}
		return new java.awt.Dimension( xMax+1, yMax+1 );
	}
	public void addLayoutComponent( String name, java.awt.Component comp ) {
	}
	public void removeLayoutComponent( java.awt.Component comp ) {
	}
}

class CanvasPane extends javax.swing.JPanel implements zoot.DropReceptor {
	public CanvasPane() {
		this.setLayout( new LaissezFaireLayoutManager() );
		this.setOpaque( false );
	}
	public void dragStarted( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void dragEntered( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void dragUpdated( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void dragDropped( zoot.DragAndDropContext dragAndDropContext ) {
		zoot.ZDragComponent source = dragAndDropContext.getDragSource();
		Instance instance;
		if( source instanceof Template ) {
			Template template = (Template)source;
			instance = new Instance( template.getShape() );
			java.awt.event.MouseEvent eSource = dragAndDropContext.getLatestMouseEvent();
			java.awt.event.MouseEvent e = edu.cmu.cs.dennisc.swing.SwingUtilities.convertMouseEvent( eSource.getComponent(), eSource, this );
			instance.setLocation( e.getPoint() );
			instance.setPreferredSize( template.getPreferredSize() );
			
			instance.revalidate();
			instance.setForeground( template.getForeground() );
			instance.setBackground( template.getBackground() );
			instance.setDragAndDropOperation( template.getDragAndDropOperation() );
			this.add( instance );
		} else {
			instance = (Instance)source;
			java.awt.event.MouseEvent eDrag = dragAndDropContext.getOriginalMouseEvent();
			java.awt.event.MouseEvent eDrop = dragAndDropContext.getLatestMouseEvent();
			int x = instance.getX() + eDrop.getX() - eDrag.getX();
			int y = instance.getY() + eDrop.getY() - eDrag.getY();
			instance.setLocation( x, y );
		}
		instance.setSelected( true );
		this.revalidate();
		this.repaint();
	}

	public void dragExited( zoot.DragAndDropContext dragAndDropContext, boolean isDropRecipient ) {
	}
	public void dragStopped( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public java.awt.Component getAWTComponent() {
		return this;
	}
	public boolean isPotentiallyAcceptingOf( zoot.ZDragComponent source ) {
		return true;
	}
}

class DragAndDropTemplateOperation implements zoot.DragAndDropOperation, javax.swing.undo.UndoableEdit {
	private java.util.List< zoot.DropReceptor > potentialDropReceptors = new java.util.LinkedList< zoot.DropReceptor >();

	public DragAndDropTemplateOperation( zoot.DropReceptor dropReceptor ) {
		this.potentialDropReceptors.add( dropReceptor );
	}
	public java.util.List< ? extends zoot.DropReceptor > createListOfPotentialDropReceptors( zoot.ZDragComponent dragSource ) {
		return this.potentialDropReceptors;
	}
	public void handleDragStarted( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void handleDragEnteredDropReceptor( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void handleDragExitedDropReceptor( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void handleDragStopped( zoot.DragAndDropContext dragAndDropContext ) {
	}

	public boolean isSignificant() {
		return true;
	}
	public boolean canRedo() {
		return true;
	}
	public boolean canUndo() {
		return true;
	}
	public void redo() throws javax.swing.undo.CannotRedoException {
	}
	public void undo() throws javax.swing.undo.CannotUndoException {
	}
	public void die() {
	}

	public String getPresentationName() {
		return null;
	}
	public String getRedoPresentationName() {
		return null;
	}
	public String getUndoPresentationName() {
		return null;
	}

	public boolean addEdit( javax.swing.undo.UndoableEdit anEdit ) {
		return false;
	}
	public boolean replaceEdit( javax.swing.undo.UndoableEdit anEdit ) {
		return false;
	}

}

class TemplatesPane extends swing.LineAxisPane {
	public TemplatesPane( DragAndDropTemplateOperation dragAndDropTemplateOperation ) {
		this.add( zoot.ZLabel.acquire( "templates:" ) );
		Template[] templates = new Template[] { new Template( Shape.RECTANGLE ), new Template( Shape.ELLIPSE ) };
		for( Template template : templates ) {
			template.setDragAndDropOperation( dragAndDropTemplateOperation );
			this.add( template );
		}
	}
}

abstract class HistoryFrame extends zoot.ZFrame {
	private javax.swing.undo.UndoManager undoManager = new javax.swing.undo.UndoManager();
	public HistoryFrame() {
		zoot.ZManager.addEditListener( new zoot.event.EditListener() {
			public void editCommitting( zoot.event.EditEvent e ) {
			}
			public void editCommitted( zoot.event.EditEvent e ) {
				HistoryFrame.this.handleEditCommitted( e );
			}
		} );
	}
	protected void handleEditCommitted( zoot.event.EditEvent e ) {
		javax.swing.undo.UndoableEdit edit = e.getEdit();
		edit.redo();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "editCommitted:", e.getEdit() );
	}
}

public class ShapesFrame extends HistoryFrame {
	public ShapesFrame() {
		java.awt.Container contentPane = this.getContentPane();

		CanvasPane canvasPane = new CanvasPane();
		DragAndDropTemplateOperation dragAndDropTemplateOperation = new DragAndDropTemplateOperation( canvasPane );
		TemplatesPane templatesPane = new TemplatesPane( dragAndDropTemplateOperation );
		javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane( javax.swing.JSplitPane.VERTICAL_SPLIT, templatesPane, canvasPane );
		contentPane.add( splitPane, java.awt.BorderLayout.CENTER );
	}
	@Override
	protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
	}
	@Override
	protected void handleQuit( java.util.EventObject e ) {
		System.exit( 0 );
	}
	public static void main( String[] args ) {
		ShapesFrame frame = new ShapesFrame();
		frame.setSize( 640, 480 );
		frame.setVisible( true );
	}
}
