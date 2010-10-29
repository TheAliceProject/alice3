public class Main {
	public static void main( String[] args ) {
		for( int size : new int[] { 16, 32 } ) {
			java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( size, size, java.awt.image.BufferedImage.TYPE_3BYTE_BGR );
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)image.getGraphics();
			g2.setColor( java.awt.Color.WHITE );
			g2.fillRect( 0, 0, size, size );
			g2.setColor( java.awt.Color.BLACK );
			if( size == 16 ) {
				edu.cmu.cs.dennisc.awt.KnurlUtilities.paintKnurl3( g2, 1, 0, 5, size  );
			} else {
				edu.cmu.cs.dennisc.awt.KnurlUtilities.paintKnurl5( g2, 1, 0, 7, size  );
			}
			g2.dispose();
			edu.cmu.cs.dennisc.image.ImageUtilities.write( "/icon" + size + ".png", image );
		}
	}
}
