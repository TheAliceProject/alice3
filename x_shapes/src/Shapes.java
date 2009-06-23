abstract class Shape extends edu.cmu.cs.dennisc.pattern.DefaultInstancePropertyOwner {
	public edu.cmu.cs.dennisc.property.StringProperty name = new edu.cmu.cs.dennisc.property.StringProperty( this, "" );
	public edu.cmu.cs.dennisc.property.DoubleProperty x = new edu.cmu.cs.dennisc.property.DoubleProperty( this, 0.0 );
	public edu.cmu.cs.dennisc.property.DoubleProperty y = new edu.cmu.cs.dennisc.property.DoubleProperty( this, 0.0 );
	public edu.cmu.cs.dennisc.property.DoubleProperty width = new edu.cmu.cs.dennisc.property.DoubleProperty( this, 100.0 );
	public edu.cmu.cs.dennisc.property.DoubleProperty height = new edu.cmu.cs.dennisc.property.DoubleProperty( this, 100.0 );
	public edu.cmu.cs.dennisc.property.InstanceProperty< java.awt.Color > fillColor = new edu.cmu.cs.dennisc.property.InstanceProperty< java.awt.Color >( this, java.awt.Color.GRAY );
	public edu.cmu.cs.dennisc.property.InstanceProperty< java.awt.Color > drawColor = new edu.cmu.cs.dennisc.property.InstanceProperty< java.awt.Color >( this, java.awt.Color.BLACK );
	@Override
	public boolean isComposedOfGetterAndSetterProperties() {
		return true;
	}
	protected abstract java.awt.Shape createAWTShape();
	public void paint( java.awt.Graphics2D g2 ) {
		java.awt.Shape awtShape = this.createAWTShape();
		java.awt.Color fillColor = this.fillColor.getValue();
		if( fillColor != null ) {
			g2.setColor( fillColor );
			g2.fill( awtShape );
		}
		java.awt.Color drawColor = this.drawColor.getValue();
		if( drawColor != null ) {
			g2.setColor( drawColor );
			g2.draw( awtShape );
		}
	}
}

class Rectangle extends Shape {
	@Override
	public java.awt.Shape createAWTShape() {
		return new java.awt.geom.Rectangle2D.Double( this.x.getValue(), this.y.getValue(), this.width.getValue(), this.height.getValue() );
	}
}
class Ellipse extends Shape {
	@Override
	public java.awt.Shape createAWTShape() {
		return new java.awt.geom.Ellipse2D.Double( this.x.getValue(), this.y.getValue(), this.width.getValue(), this.height.getValue() );
	}
}

class CanvasPane extends swing.Pane implements zoot.DropReceptor {
	private java.util.List< Shape > shapes = new java.util.LinkedList< Shape >();

	public void dragStarted( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void dragEntered( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void dragUpdated( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void dragDropped( zoot.DragAndDropContext dragAndDropContext ) {
		zoot.ZDragComponent source = dragAndDropContext.getDragSource();
		Template template = (Template)source;
		Shape shape = template.createShape();
		shape.x.setValue( (double)dragAndDropContext.getLatestMouseEvent().getX() );
		shape.y.setValue( (double)dragAndDropContext.getLatestMouseEvent().getY() );
		this.shapes.add( shape );
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

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		for( Shape shape : this.shapes ) {
			shape.paint( g2 );
		}
	}
}

class DragAndDropTemplateOperation implements zoot.DragAndDropOperation {
	private java.util.List< zoot.DropReceptor > potentialDropReceptors = new java.util.LinkedList< zoot.DropReceptor >();
	public DragAndDropTemplateOperation( zoot.DropReceptor dropReceptor ) {
		this.potentialDropReceptors.add( dropReceptor );
	}
	public java.util.List< ? extends zoot.DropReceptor > createListOfPotentialDropReceptors( zoot.ZDragComponent dragSource ) {
		return this.potentialDropReceptors;
	}
	public void handleDragEnteredDropReceptor( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void handleDragExitedDropReceptor( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void handleDragStarted(zoot.DragAndDropContext dragAndDropContext) {
	}
	public void handleDragStopped( zoot.DragAndDropContext dragAndDropContext ) {
	}
}

abstract class Template extends zoot.ZDragComponent {
	private Shape shape = this.createShape();
	public Template() {
		this.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentShown( java.awt.event.ComponentEvent e ) {
			}
			public void componentHidden( java.awt.event.ComponentEvent e ) {
			}
			public void componentMoved( java.awt.event.ComponentEvent e ) {
			}
			public void componentResized( java.awt.event.ComponentEvent e ) {
				Template.this.shape.x.setValue( 4.0 );
				Template.this.shape.y.setValue( 4.0 );
				Template.this.shape.width.setValue( (double)e.getComponent().getWidth()-8.0 );
				Template.this.shape.height.setValue( (double)e.getComponent().getHeight()-8.0 );
			}
		} );
		
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return new java.awt.Dimension( 64, 32 );
	}
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
	protected abstract Shape createShape();

	@Override
	protected void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		this.shape.paint( g2 );
	}
	@Override
	protected void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		this.fillBounds( g2, x, y, width, height );
	}
	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
	}
}

class RectangleTemplate extends Template {
	@Override
	public Shape createShape() {
		return new Rectangle();
	}
}

class EllipseTemplate extends Template {
	@Override
	public Shape createShape() {
		return new Ellipse();
	}
}

class TemplatesPane extends swing.PageAxisPane {
	public TemplatesPane( DragAndDropTemplateOperation dragAndDropTemplateOperation ) {
		Template[] templates = new Template[]{ new RectangleTemplate(), new EllipseTemplate() };
		for( Template template : templates ) {
			template.setDragAndDropOperation( dragAndDropTemplateOperation );
			this.add( template );
		}
	}
}

public class Shapes extends zoot.ZFrame {
	public Shapes() {
		java.awt.Container contentPane = this.getContentPane();
		
		CanvasPane canvasPane = new CanvasPane();
		DragAndDropTemplateOperation dragAndDropTemplateOperation = new DragAndDropTemplateOperation( canvasPane );
		TemplatesPane templatesPane = new TemplatesPane( dragAndDropTemplateOperation );
		javax.swing.JSplitPane splitPane = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT, templatesPane, canvasPane );
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
		Shapes frame = new Shapes();
		frame.setSize( 640, 480 );
		frame.setVisible( true );
	}
}
