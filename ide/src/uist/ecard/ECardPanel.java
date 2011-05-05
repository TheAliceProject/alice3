package uist.ecard;

class ECardPanel extends javax.swing.JComponent {

	public enum CardState {
		EMPTY, PHOTO, BUBBLE_PHOTO
	}

	private java.awt.Image image;
	protected CardState state;

	public ECardPanel(CardState state) {
		setImage(state);
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		if( this.image != null ) {
			return new java.awt.Dimension( this.image.getWidth( this ), this.image.getWidth( this ) );
		} else {
			return new java.awt.Dimension( 400, 300 );
		}
	}
	protected void setImage(CardState state) {
		this.state = state;
		if (state == CardState.EMPTY) {
			this.image = new javax.swing.ImageIcon(ECardPanel.class.getResource("resources/e-card.png")).getImage();
		} else if (state == CardState.PHOTO) {
			this.image = new javax.swing.ImageIcon(ECardPanel.class.getResource("resources/e-card-photo-" + ECardApplication.getSingleton().getYear() + ".png")).getImage();
		} else if (state == CardState.BUBBLE_PHOTO) {
			this.image = new javax.swing.ImageIcon(ECardPanel.class.getResource("resources/e-card-photo-bubble-" + ECardApplication.getSingleton().getYear() + ".png")).getImage();
		}
		this.revalidate();
		this.repaint();
	}

	@Override
	public void paintComponent(java.awt.Graphics g) {
		super.paintComponent( g );
		edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredImage( g, image, this );
	}
}
