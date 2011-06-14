package org.lgna.croquet.components;

public abstract class PopupContainer extends AbstractButton< javax.swing.AbstractButton, org.lgna.croquet.PopupPrepModel > {
	public PopupContainer( org.lgna.croquet.PopupPrepModel model ) {
		super( model );
	}
	@Override
	protected javax.swing.AbstractButton createAwtComponent() {
		class JPopupPanel extends javax.swing.JButton {
			private boolean isBorderInitialized = false;
			public JPopupPanel() {
				this.setUI( javax.swing.plaf.basic.BasicButtonUI.createUI( this ) );
			}
			private void updateBorderIfNecessary() {
				if( this.isBorderInitialized ) {
					//pass
				} else {
					this.setBorder( javax.swing.BorderFactory.createEmptyBorder( PopupContainer.this.getInsetTop(), PopupContainer.this.getInsetLeft(), PopupContainer.this.getInsetBottom(), PopupContainer.this.getInsetRight() ) );
				}
			}
			@Override
			public void addNotify() {
				super.addNotify();
				this.updateBorderIfNecessary();
			}
			@Override
			public void removeNotify() {
				super.removeNotify();
			}
			@Override
			public void invalidate() {
				this.isBorderInitialized = false;
				this.updateBorderIfNecessary();
				super.invalidate();
			}
			@Override
			public void doLayout() {
				this.isBorderInitialized = false;
				this.updateBorderIfNecessary();
				super.doLayout();
			}
			@Override
			protected void paintBorder( java.awt.Graphics g ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				int x = 0;
				int y = 0;
				int width = this.getWidth();
				int height = this.getHeight();
				java.awt.Paint prevPaint = g2.getPaint();
				try {
					g2.setPaint( PopupContainer.this.getBackgroundPaint( x, y, width, height ) );
					PopupContainer.this.paintPrologue( g2, x, y, width, height );
				} finally {
					g2.setPaint( prevPaint );
				}
			}
			@Override
			public void paint(java.awt.Graphics g) {
				super.paint(g);
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				int x = 0;
				int y = 0;
				int width = this.getWidth();
				int height = this.getHeight();
				java.awt.Paint prevPaint = g2.getPaint();
				g2.setPaint( PopupContainer.this.getForegroundPaint( x, y, width, height ) );
				try {
					PopupContainer.this.paintEpilogue( g2, x, y, width, height );
				} finally {
					g2.setPaint( prevPaint );
				}
			}
		}
		JPopupPanel rv = new JPopupPanel();
		rv.setLayout( new javax.swing.BoxLayout( rv, javax.swing.BoxLayout.LINE_AXIS ) );
		rv.setAction( this.getModel().getAction() );
		return rv;
	}

	protected java.awt.Paint getForegroundPaint( int x, int y, int width, int height ) {
		return this.getForegroundColor();
	}
	protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int height ) {
		return this.getBackgroundColor();
	}
	
	protected abstract int getInsetTop();
	protected abstract int getInsetLeft();
	protected abstract int getInsetBottom();
	protected abstract int getInsetRight();

	protected abstract void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height );
	protected abstract void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height );
	protected abstract void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height );
	
	@Deprecated
	protected boolean isActive() {
		javax.swing.ButtonModel buttonModel = this.getAwtComponent().getModel();
		return buttonModel.isArmed() || buttonModel.isPressed() || buttonModel.isRollover();
	}
	
	public void addComponent( Component<?> component ) {
		this.internalAddComponent( component );
	}
	public void removeAllComponents() {
		this.internalRemoveAllComponents();
	}
	public void forgetAndRemoveAllComponents() {
		this.internalForgetAndRemoveAllComponents();
	}
}
