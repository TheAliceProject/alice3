public class TestRadioButtons {
	public static void main( String[] args ) {
		class LocaleComboBoxModel extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel {
			private java.util.Locale[] candidates = { 
					new java.util.Locale( "en", "US" ), 
					new java.util.Locale( "en", "US", "complex" ), 
					new java.util.Locale( "en", "US", "java" ) 
			};
			private int selectedIndex;

			public LocaleComboBoxModel() {
				this.selectedIndex = 0;
			}
			public Object getElementAt( int index ) {
				return this.candidates[ index ];
			}
			public int getSize() {
				return this.candidates.length;
			}
			public Object getSelectedItem() {
				if( 0 <= this.selectedIndex && this.selectedIndex < this.candidates.length ) {
					return this.candidates[ this.selectedIndex ];
				} else {
					return null;
				}
			}
			public void setSelectedItem( Object selectedItem ) {
				int index = -1;
				if( selectedItem != null ) {
					int i = 0;
					for( java.util.Locale locale : this.candidates ) {
						if( selectedItem.equals( locale ) ) {
							index = i;
							break;
						}
						i++;
					}
				}
				this.selectedIndex = index;
			}
		}

		class LocaleItemSelectionOperation extends edu.cmu.cs.dennisc.zoot.AbstractItemSelectionOperation< java.util.Locale > {
			public LocaleItemSelectionOperation() {
				super( new LocaleComboBoxModel() );
			}
			@Override
			protected String getNameFor( int index, java.util.Locale locale ) {
				if( locale != null ) {
					return locale.getDisplayName();
				} else {
					return "null";
				}
			}
			
			@Override
			protected void handleSelectionChange( java.util.Locale value ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleSelectionChange:", value );
			}
			@Override
			public boolean isSignificant() {
				return false;
			}
		}

		final LocaleItemSelectionOperation localeItemSelectionOperation = new LocaleItemSelectionOperation();
		javax.swing.JPanel radioButtons = edu.cmu.cs.dennisc.zoot.ZManager.createRadioButtons( localeItemSelectionOperation );
		edu.cmu.cs.dennisc.zoot.ZFrame frame = new edu.cmu.cs.dennisc.zoot.ZFrame() {
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}
			@Override
			protected void handleQuit( java.util.EventObject e ) {
				System.exit( 0 );
			}
		};
		frame.getContentPane().add( radioButtons );
		frame.pack();
		frame.setVisible( true );
		
		new Thread() {
			@Override
			public void run() {
				edu.cmu.cs.dennisc.lang.ThreadUtilities.sleep( 2000 );
				int index = 1;
				localeItemSelectionOperation.getButtonModelForConfiguringSwing( index ).setSelected( true );
			}
		}.start();
	}
}
