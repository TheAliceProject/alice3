package org.alice.ide.croquet;

public class ImageView extends edu.cmu.cs.dennisc.croquet.Component<edu.cmu.cs.dennisc.javax.swing.components.JImageView> {
	private int desiredSize;
	public ImageView( int desiredSize ) {
		this.desiredSize = desiredSize;
	}
	@Override
	protected edu.cmu.cs.dennisc.javax.swing.components.JImageView createJComponent() {
		return new edu.cmu.cs.dennisc.javax.swing.components.JImageView(this.desiredSize);
	}
	public java.awt.image.BufferedImage getBufferedImage() {
		return this.getJComponent().getBufferedImage();
	}
	public void setBufferedImage(java.awt.image.BufferedImage bufferedImage) {
		this.getJComponent().setBufferedImage( bufferedImage );
	}
}
