package autotutorial.ecard;

class ECardPanel extends javax.swing.JPanel {

	public enum CardState {
		EMPTY, PHOTO, BUBBLE_PHOTO
	}

	private java.awt.Image image;
	protected CardState state;

	public ECardPanel(CardState state) {
		setImage(state);
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

		java.awt.Dimension size = new java.awt.Dimension(image.getWidth(null), image.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
		repaint();
	}

	@Override
	public void paintComponent(java.awt.Graphics g) {
		g.drawImage(image, 0, 0, null);
	}
}
