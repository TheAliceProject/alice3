package stencil;

public class HoleGroup {
//	private Proxy proxy = new Proxy();
	private Stencil stencil;
	private Hole[] holes;
	private java.awt.Component northDecorator;
	private java.awt.Component eastDecorator;
	private java.awt.Component southDecorator;
	private java.awt.Component westDecorator;
	
	private int northDecoratorOffset = 0;

	public HoleGroup( Hole... holes ) {
		initialize( holes );
	}
	
	public HoleGroup( java.util.List< Hole > holes ) {
		Hole[] array = new Hole[ holes.size() ];
		holes.toArray( array );
		initialize( array );
	}
	public HoleGroup( java.util.List< Hole > holes, int northDecoratorOffset) {
		this( holes );
		this.northDecoratorOffset = northDecoratorOffset;
	}
	private void initialize( Hole[] holes ) {
		this.holes = holes;
//		for( Hole hole : this.holes ) {
//			Proxy proxy = hole.getProxy();
//			// todo
//		}
	}

	public Hole[] getHoles() {
		return this.holes;
	}
	
	public int getNorthDecoratorOffset() {
		return this.northDecoratorOffset;
	}

	private void addDecoratorIfNecessary( java.awt.Component decorator ) {
		if( decorator != null ) {
			if( this.stencil != null ) {
				// todo
			}
		}
	}

	private void removeDecoratorIfNecessary( java.awt.Component decorator ) {
		if( decorator != null ) {
			if( this.stencil != null ) {
				// todo
			}
		}
	}

//	public Proxy getProxy() {
//		return proxy;
//	}

	public Stencil getStencil() {
		return this.stencil;
	}

	public void setStencil( Stencil stencil ) {
		this.stencil = stencil;
	}

	public int getPad() {
		return 16;
	}

	public java.awt.Component getNorthDecorator() {
		return this.northDecorator;
	}

	public void setNorthDecorator( java.awt.Component northDecorator ) {
		removeDecoratorIfNecessary( this.northDecorator );
		this.northDecorator = northDecorator;
		addDecoratorIfNecessary( this.northDecorator );
	}

	public java.awt.Component getEastDecorator() {
		return this.eastDecorator;
	}

	public void setEastDecorator( java.awt.Component eastDecorator ) {
		removeDecoratorIfNecessary( this.eastDecorator );
		this.eastDecorator = eastDecorator;
		addDecoratorIfNecessary( this.eastDecorator );
	}

	public java.awt.Component getSouthDecorator() {
		return this.southDecorator;
	}

	public void setSouthDecorator( java.awt.Component southDecorator ) {
		removeDecoratorIfNecessary( this.southDecorator );
		this.southDecorator = southDecorator;
		addDecoratorIfNecessary( this.southDecorator );
	}

	public java.awt.Component getWestDecorator() {
		return this.westDecorator;
	}

	public void setWestDecorator( java.awt.Component westDecorator ) {
		removeDecoratorIfNecessary( this.westDecorator );
		this.westDecorator = westDecorator;
		addDecoratorIfNecessary( this.westDecorator );
	}
}
