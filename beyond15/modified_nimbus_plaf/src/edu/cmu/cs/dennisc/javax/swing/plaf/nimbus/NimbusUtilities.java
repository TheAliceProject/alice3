package edu.cmu.cs.dennisc.javax.swing.plaf.nimbus;

public class NimbusUtilities {
	private NimbusUtilities() {
		throw new AssertionError();
	}
	
	public static boolean installModifiedNimbus( javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo ) {
		try {
			//"com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
			final javax.swing.UIDefaults uiDefaults = javax.swing.UIManager.getLookAndFeelDefaults();
			final String DISABLED_PAINTER_KEY = "Button[Disabled].backgroundPainter";
			final com.sun.java.swing.Painter<javax.swing.JComponent> disabledPainter = (com.sun.java.swing.Painter<javax.swing.JComponent>) uiDefaults.get(DISABLED_PAINTER_KEY);
			class DisabledRespectingPainter implements com.sun.java.swing.Painter<javax.swing.JComponent> {
				private com.sun.java.swing.Painter<javax.swing.JComponent> defaultPainter;
				public DisabledRespectingPainter( String key ) {
					this.defaultPainter = (com.sun.java.swing.Painter<javax.swing.JComponent>) uiDefaults.get(key);
				}
				public void paint(java.awt.Graphics2D g, javax.swing.JComponent component, int width, int height) {
					if (component.isEnabled()) {
						this.defaultPainter.paint(g, component, width, height);
					} else {
						disabledPainter.paint(g, component, width, height);
					}
				}
			}
			String[] keys = {
					"Button[Default].backgroundPainter",
			        "Button[Default+Focused].backgroundPainter",
			        "Button[Default+MouseOver].backgroundPainter",
			        "Button[Default+Focused+MouseOver].backgroundPainter"
			};
			for( String key : keys ) {
				DisabledRespectingPainter disabledRespectingPainter = new DisabledRespectingPainter( key );
				uiDefaults.put( key, disabledRespectingPainter );
			}
			return true;
		} catch( Exception e ) {
			e.printStackTrace();
			return false;
		}
	}
}
