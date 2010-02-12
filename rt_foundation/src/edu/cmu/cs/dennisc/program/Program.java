/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.program;

/**
 * @author Dennis Cosgrove
 */
public abstract class Program extends javax.swing.JApplet {
//	static {
//		Thread.setDefaultUncaughtExceptionHandler( new UncaughtExceptionHandler() );
//	}
	
	//	private javax.swing.JLayeredPane this.topLevelLayeredPane = new javax.swing.JLayeredPane();
	private java.util.Map< String, String > argNameToValueMap = null;

	private boolean isClosed = false;
	protected boolean isClosed() {
		return this.isClosed;
	}
	public void setClosed( boolean isClosed ) {
		this.isClosed = isClosed;
	}
	//todo: override getParameterInfo()

	@Override
	public String getParameter( String name ) {
		if( this.argNameToValueMap != null ) {
			return this.argNameToValueMap.get( name );
		} else {
			return super.getParameter( name );
		}
	}
	public void setParameter( String name, String value ) {
		this.argNameToValueMap.put( name, value );
	}

	public void setArgs( String args[] ) {
		this.isClosed = false;
		this.argNameToValueMap = new java.util.HashMap< String, String >();
		for( String arg : args ) {
			int i = arg.indexOf( '=' );
			assert i != -1;
			String key = arg.substring( 0, i );
			String value = arg.substring( i + 1 );
			this.argNameToValueMap.put( key, value );
		}
	}

	//	public javax.swing.JLayeredPane getTopLevelLayeredPane() {
	//		return this.topLevelLayeredPane;
	//		//return getLayeredPane();
	//	}

	private javax.swing.JLayeredPane layeredPane;

	@Override
	public javax.swing.JLayeredPane getLayeredPane() {
		if( this.layeredPane != null ) {
			return this.layeredPane;
		} else {
			return super.getLayeredPane();
		}
	}

	private boolean isInitializationSuccessful = false;

	private java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore( 0 );

	@Override
	public final void init() {
		super.init();
		class InitializeThread extends edu.cmu.cs.dennisc.lang.ThreadWithRevealingToString {
			@Override
			public void run() {
				preInitialize();
				Program.this.isInitializationSuccessful = false;
				try {
					initialize();
					Program.this.isInitializationSuccessful = true;
				} finally {
					getContentPane().validate();
					postInitialize( Program.this.isInitializationSuccessful );
					Program.this.semaphore.release();
				}
			}
		}
		new InitializeThread().start();
	}

	
	@Override
	public final void start() {
		super.start();
		class RunThread extends edu.cmu.cs.dennisc.lang.ThreadWithRevealingToString {
			@Override
			public void run() {
				Program.this.semaphore.acquireUninterruptibly();
				if( Program.this.isInitializationSuccessful ) {
					Program.this.preRun();
					edu.cmu.cs.dennisc.alice.ProgramClosedException.invokeAndCatchProgramClosedException( new Runnable() {
						public void run() {
							Program.this.run();
						}
					} );
					Program.this.postRun();
				} else {
					System.err.println( "WARNING: initialization not successful; run() method will not be invoked." );
				}
			}
		};
		new RunThread().start();
	}

	protected abstract void preInitialize();
	protected abstract void initialize();
	protected abstract void postInitialize( boolean success );
	protected abstract void preRun();
	protected abstract void run();
	protected abstract void postRun();

//	protected abstract void handleShownForTheFirstTime();
	protected abstract boolean handleWindowClosing( java.awt.event.WindowEvent e );
	protected abstract void handleWindowClosed( java.awt.event.WindowEvent e );

	private static int toInt( String s, int valueIfNull ) {
		if( s != null ) {
			return Integer.parseInt( s );
		} else {
			return valueIfNull;
		}
	}

	//	private java.awt.Dimension this.preferredSize = new java.awt.Dimension( 640, 480 );
	//
	//	@Override
	//	public java.awt.Dimension getPreferredSize() {
	//		return this.preferredSize;
	//	}

	public class StartProgramComponentAdapter implements java.awt.event.ComponentListener {
		private boolean isAlreadyStarted = false;
		public void componentShown( java.awt.event.ComponentEvent e ) {
			if( this.isAlreadyStarted ) {
				//pass
			} else {
				this.isAlreadyStarted = true;
				Program.this.start();
			}
		}
		public void componentHidden( java.awt.event.ComponentEvent e ) {
		}
		public void componentMoved( java.awt.event.ComponentEvent e ) {
		}
		public void componentResized( java.awt.event.ComponentEvent e ) {
		}
	}

	protected void showInWindow( final java.awt.Window window, final boolean isExitDesiredOnClose ) {
		int xLocation = toInt( getParameter( "X_LOCATION" ), 0 );
		int yLocation = toInt( getParameter( "Y_LOCATION" ), 0 );
		window.setLocation( xLocation, yLocation );
		window.addWindowListener( new java.awt.event.WindowListener() {
			public void windowOpened( java.awt.event.WindowEvent e ) {
//				Program.this.handleShownForTheFirstTime();
			}
			public void windowActivated( java.awt.event.WindowEvent e ) {
			}
			public void windowDeactivated( java.awt.event.WindowEvent e ) {
			}
			public void windowDeiconified( java.awt.event.WindowEvent e ) {
			}
			public void windowIconified(java.awt.event.WindowEvent e) {
			}
			public void windowClosing( java.awt.event.WindowEvent e ) {
				if( Program.this.handleWindowClosing( e ) ) {
					window.dispose();
				}
			}
			public void windowClosed( java.awt.event.WindowEvent e ) {
				Program.this.handleWindowClosed( e );
				if( isExitDesiredOnClose ) {
					System.exit( 0 );
				}
				Program.this.setClosed( true );
			}
		} );
		window.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentHidden( java.awt.event.ComponentEvent e ) {
			}
			public void componentShown( java.awt.event.ComponentEvent e ) {
			}
			public void componentMoved( java.awt.event.ComponentEvent e ) {
				setParameter( "X_LOCATION", Integer.toString( e.getComponent().getX() ) );
				setParameter( "Y_LOCATION", Integer.toString( e.getComponent().getY() ) );
			}
			public void componentResized( java.awt.event.ComponentEvent e ) {
			}
		} );
		window.setVisible( true );
	}

	public void showInJFrame( String[] args, boolean isExitDesiredOnClose ) {
		setArgs( args );
		javax.swing.JFrame frame = new javax.swing.JFrame();
		frame.setDefaultCloseOperation( javax.swing.JFrame.DO_NOTHING_ON_CLOSE );
		int width = toInt( getParameter( "WIDTH" ), 640 );
		int height = toInt( getParameter( "HEIGHT" ), 480 );
		this.getContentPane().setPreferredSize( new java.awt.Dimension( width, height ) );
		String title = getParameter( "TITLE" );
		if( title != null ) {
			frame.setTitle( title );
		}

		//todo: obviously flicker on resize is not desired
		boolean isFlickerOnResizeDesired = true;
		if( isFlickerOnResizeDesired ) {
			frame.getContentPane().add( this );
		} else {
			frame.getContentPane().add( this.getContentPane() );
			this.layeredPane = frame.getLayeredPane();
		}

		init();
		frame.pack();
		frame.addComponentListener( new StartProgramComponentAdapter() );
		showInWindow( frame, isExitDesiredOnClose );

		frame.validate();
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				Program.this.getContentPane().repaint();
			}
		} );
	}

	public void showInJDialog( java.awt.Component owner, boolean isModal, String[] args ) {
		setArgs( args );

		String title = getParameter( "TITLE" );

		java.awt.Component root = javax.swing.SwingUtilities.getRoot( owner );
		javax.swing.JDialog dialog;
		if( root instanceof java.awt.Frame ) {
			dialog = new javax.swing.JDialog( (java.awt.Frame)root );
		} else if( root instanceof java.awt.Dialog ) {
			dialog = new javax.swing.JDialog( (java.awt.Frame)root );
		} else {
			dialog = new javax.swing.JDialog();
		}
		dialog.setTitle( title );
		dialog.setModal( isModal );

		dialog.getContentPane().setLayout( new java.awt.BorderLayout() );
		dialog.getContentPane().add( this, java.awt.BorderLayout.CENTER );

		int width = toInt( getParameter( "WIDTH" ), 640 );
		int height = toInt( getParameter( "HEIGHT" ), 480 );
		this.getContentPane().setPreferredSize( new java.awt.Dimension( width, height ) );

		init();
		dialog.pack();

		if( root != null ) {
			dialog.setLocationRelativeTo( root );
		}

		dialog.addComponentListener( new StartProgramComponentAdapter() );
		showInWindow( dialog, false );
	}
	public void showInJWindow( String[] args ) {
		setArgs( args );

		javax.swing.JWindow window = new javax.swing.JWindow();
		window.getContentPane().setLayout( new java.awt.BorderLayout() );
		window.getContentPane().add( this, java.awt.BorderLayout.CENTER );

		int width = toInt( getParameter( "WIDTH" ), 640 );
		int height = toInt( getParameter( "HEIGHT" ), 480 );
		this.getContentPane().setPreferredSize( new java.awt.Dimension( width, height ) );

		init();
		window.pack();

		window.addComponentListener( new StartProgramComponentAdapter() );
		showInWindow( window, false );
	}

	public void showInAWTContainer( java.awt.Container awtContainer, String[] args ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "showInAWTContainer", awtContainer, args );
		setArgs( args );
		init();
		awtContainer.setLayout( new java.awt.GridLayout( 1, 1 ) );
		//this.addComponentListener( new StartProgramComponentAdapter() );
		
//		class ComponentAdapter implements java.awt.event.ComponentListener {
//			private boolean isFirstTime = true;
//			public void componentShown( java.awt.event.ComponentEvent e ) {
//				if( this.isFirstTime ) {
//					Program.this.handleShownForTheFirstTime();
//					this.isFirstTime = false;
//				}
//			}
//			public void componentHidden( java.awt.event.ComponentEvent e ) {
//			}
//			public void componentResized( java.awt.event.ComponentEvent e ) {
//			}
//			public void componentMoved( java.awt.event.ComponentEvent e ) {
//			}
//		}
//		this.addComponentListener( new ComponentAdapter() );
		
		awtContainer.add( this );
		if( awtContainer instanceof javax.swing.JComponent ) {
			((javax.swing.JComponent)awtContainer).revalidate();
		}
		awtContainer.repaint();
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				java.awt.Container contentPane = Program.this.getContentPane();
				contentPane.repaint();
//				if( contentPane instanceof javax.swing.JComponent ) {
//					((javax.swing.JComponent)contentPane).revalidate();
//				}
				Program.this.start();
			}
		} );

		//todo: handle container "dispose"?
	}

	public static void main( final String[] args ) {
		String programClassname = System.getProperty( "java.main" );
		if( programClassname != null ) {
			programClassname.replace( '.', '/' );
			try {
				Class< ? extends Program > programClass = (Class< ? extends Program >)Class.forName( programClassname );
				//				Object o = programClass.newInstance();
				//				final Program program = (Program)o;
				final Program program = programClass.newInstance();
				javax.swing.SwingUtilities.invokeLater( new Runnable() {
					public void run() {
						program.showInJFrame( args, true );
					}
				} );
			} catch( IllegalAccessException iae ) {
				throw new RuntimeException( iae );
			} catch( InstantiationException ie ) {
				throw new RuntimeException( ie );
			} catch( ClassNotFoundException cnfe ) {
				throw new RuntimeException( cnfe );
			}
		} else {
			String javaMainText = "-Djava.main=package.Class";
			edu.cmu.cs.dennisc.clipboard.ClipboardUtilities.setClipboardContents( javaMainText );
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "UNABLE TO START PROGRAM" );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "------ -- ----- -------" );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "In order to run this program as an application, you need to set the system property java.main.  Please update your VM arguments." );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "The text below (NOTE: requires editing) has been copied to the clipboard for your convenience." );
			edu.cmu.cs.dennisc.print.PrintUtilities.println( javaMainText );
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
			edu.cmu.cs.dennisc.print.PrintUtilities.println();
		}
	}
}
