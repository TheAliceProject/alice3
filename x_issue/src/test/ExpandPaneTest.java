package test;

public class ExpandPaneTest extends edu.cmu.cs.dennisc.swing.ExpandPane {
	@Override
	protected javax.swing.JLabel createLabel() {
		edu.cmu.cs.dennisc.moot.ZLabel rv = new edu.cmu.cs.dennisc.moot.ZLabel();
		rv.setFontToScaledFont( 2.0f );
		return rv;
	}
	@Override
	protected java.lang.String getCollapsedLabelText() {
		return "would you like to provide insight into this problem?";
	}
	@Override
	protected java.lang.String getExpandedLabelText() {
		return "provide insight:";
	}
	@Override
	protected javax.swing.JComponent createCenterPane() {
		javax.swing.JPanel rv = new javax.swing.JPanel();
		rv.setBackground( java.awt.Color.RED );
		rv.setPreferredSize( new java.awt.Dimension( 640, 480 ) );
		return rv;
	}
	public static void main( String[] args ) {
		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.getContentPane().add( new ExpandPaneTest() );
		frame.setDefaultCloseOperation( javax.swing.JFrame.EXIT_ON_CLOSE );
		//frame.setSize( 640, 480 );
		frame.pack();
		frame.setVisible( true );
	}
}
