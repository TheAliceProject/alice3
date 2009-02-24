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
package edu.cmu.cs.dennisc.alice.ide;

import edu.cmu.cs.dennisc.alice.ide.issue.ExceptionHandler;

/**
 * @author Dennis Cosgrove
 */
public abstract class IDE extends edu.cmu.cs.dennisc.zoot.ZFrame {
	private static ExceptionHandler exceptionHandler;
	static {
		IDE.exceptionHandler = new ExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler( IDE.exceptionHandler );
	}

	public abstract java.io.File getApplicationRootDirectory();
	
	protected StringBuffer updateBugReportSubmissionTitle( StringBuffer rv ) {
		rv.append( "Please Submit Bug Report: " );
		this.updateTitlePrefix( rv );
		return rv;
	}
	private String getBugReportSubmissionTitle() {
		StringBuffer sb = new StringBuffer();
		updateBugReportSubmissionTitle( sb );
		return sb.toString();
	}
	protected String getApplicationName() {
		return "Alice";
	}
	protected StringBuffer updateTitlePrefix( StringBuffer rv ) {
		rv.append( this.getApplicationName() );
		rv.append( " " );
		rv.append( edu.cmu.cs.dennisc.alice.Version.getCurrentVersionText() );
		rv.append( " " );
		return rv;
	}
	protected StringBuffer updateTitle( StringBuffer rv ) {
		this.updateTitlePrefix( rv );
		if( this.file != null ) {
			rv.append( this.file.getAbsolutePath() );
			rv.append( " " );
		}
		if( this.isChanged() ) {
			rv.append( "*" );
		}
		return rv;
	}

	protected void updateTitle() {
		StringBuffer sb = new StringBuffer();
		this.updateTitle( sb );
		this.setTitle( sb.toString() );
	}

	protected abstract edu.cmu.cs.dennisc.alice.ide.editors.code.CodeEditor getCodeEditorInFocus();

	private ComponentStencil stencil;
	private java.util.List< ? extends java.awt.Component > holes = null;
	private edu.cmu.cs.dennisc.alice.ide.editors.common.PotentiallyDraggablePane potentialDragSource;
	private java.awt.Component currentDropReceptorComponent;

	protected boolean isFauxStencilDesired() {
		return this.isDragInProgress;
		//return true;
	}

	class ComponentStencil extends javax.swing.JPanel {
		public ComponentStencil() {
			setOpaque( false );
		}
		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			if( IDE.this.holes != null ) {
				//java.awt.geom.Area area = new java.awt.geom.Area( g2.getClipBounds() );
				java.awt.geom.Area area = new java.awt.geom.Area( new java.awt.Rectangle( 0, 0, getWidth(), getHeight() ) );
				synchronized( IDE.this.holes ) {
					if( IDE.this.currentDropReceptorComponent != null ) {
						this.setForeground( new java.awt.Color( 0, 0, 127, 95 ) );
					} else {
						this.setForeground( new java.awt.Color( 0, 0, 127, 127 ) );
					}

					java.awt.Rectangle potentialDragSourceBounds;
					if( IDE.this.potentialDragSource != null ) {
						potentialDragSourceBounds = IDE.this.potentialDragSource.getBounds();
						potentialDragSourceBounds = javax.swing.SwingUtilities.convertRectangle( IDE.this.potentialDragSource.getParent(), potentialDragSourceBounds, this );
					} else {
						potentialDragSourceBounds = null;
					}

					if( isFauxStencilDesired() ) {
						for( java.awt.Component component : IDE.this.holes ) {
							java.awt.Rectangle holeBounds = component.getBounds();
							holeBounds = javax.swing.SwingUtilities.convertRectangle( component.getParent(), holeBounds, this );
							area.subtract( new java.awt.geom.Area( holeBounds ) );
						}

						if( potentialDragSourceBounds != null ) {
							area.subtract( new java.awt.geom.Area( potentialDragSourceBounds ) );
						}
						g2.fill( area );
					}

					g2.setStroke( new java.awt.BasicStroke( 3.0f ) );
					final int BUFFER = 6;
					for( java.awt.Component component : IDE.this.holes ) {
						if( IDE.this.currentDropReceptorComponent == component ) {
							g2.setColor( java.awt.Color.GREEN );
						} else {
							g2.setColor( java.awt.Color.BLUE );
						}
						java.awt.Rectangle holeBounds = component.getBounds();
						holeBounds = javax.swing.SwingUtilities.convertRectangle( component, holeBounds, this );
						holeBounds.x -= BUFFER;
						holeBounds.y -= BUFFER;
						holeBounds.width += 2 * BUFFER;
						holeBounds.height += 2 * BUFFER;
						g2.draw( holeBounds );
					}
					if( potentialDragSourceBounds != null ) {
						g2.setColor( java.awt.Color.GREEN );
						g2.draw( potentialDragSourceBounds );
					}
				}
			}
		}
	}

	//public abstract void handleDelete( edu.cmu.cs.dennisc.alice.ast.Node node );

	public void showStencilOver( edu.cmu.cs.dennisc.alice.ide.editors.common.PotentiallyDraggablePane potentialDragSource, final edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		edu.cmu.cs.dennisc.alice.ide.editors.code.CodeEditor codeEditor = getCodeEditorInFocus();
		if( codeEditor != null ) {
			this.holes = codeEditor.findAllPotentialAcceptors( type );
			this.potentialDragSource = potentialDragSource;
			//java.awt.Rectangle bounds = codeEditor.getBounds();
			//bounds = javax.swing.SwingUtilities.convertRectangle( codeEditor, bounds, layeredPane );
			//this.stencil.setBounds( bounds );
			javax.swing.JLayeredPane layeredPane = this.getLayeredPane();
			if( this.stencil != null ) {
				//pass
			} else {
				this.stencil = new ComponentStencil();
			}
			this.stencil.setBounds( layeredPane.getBounds() );
			layeredPane.add( this.stencil, null );
			layeredPane.setLayer( this.stencil, javax.swing.JLayeredPane.POPUP_LAYER - 1 );
			this.stencil.repaint();
		}
	}
	public void hideStencil() {
		javax.swing.JLayeredPane layeredPane = this.getLayeredPane();
		if( this.stencil != null && this.stencil.getParent() == layeredPane ) {
			layeredPane.remove( this.stencil );
			layeredPane.repaint();
			this.holes = null;
			this.potentialDragSource = null;
		}
	}

	public void handleDragStarted( edu.cmu.cs.dennisc.alice.ide.editors.common.DropReceptor dropReceptor ) {
		this.potentialDragSource = null;
		if( this.stencil != null && this.holes != null ) {
			this.stencil.repaint();
		}
	}
	public void handleDragEntered( edu.cmu.cs.dennisc.alice.ide.editors.common.DropReceptor dropReceptor ) {
		this.currentDropReceptorComponent = dropReceptor.getAWTComponent();
		if( this.stencil != null && this.holes != null ) {
			this.stencil.repaint();
		}
	}
	public void handleDragExited( edu.cmu.cs.dennisc.alice.ide.editors.common.DropReceptor dropReceptor ) {
		this.currentDropReceptorComponent = null;
		if( this.stencil != null && this.holes != null ) {
			this.stencil.repaint();
		}
	}
	public void handleDragStopped( edu.cmu.cs.dennisc.alice.ide.editors.common.DropReceptor dropReceptor ) {
	}

	private static IDE s_singleton;

	public static IDE getSingleton() {
		return s_singleton;
	}

	private java.io.File file = null;
	private edu.cmu.cs.dennisc.alice.Project project = null;
	private edu.cmu.cs.dennisc.alice.ast.AbstractCode focusedCode = null;
	private edu.cmu.cs.dennisc.alice.ast.AbstractField fieldSelection = null;
	private edu.cmu.cs.dennisc.alice.ast.AbstractTransient transientSelection = null;

	private java.util.List< edu.cmu.cs.dennisc.alice.ide.event.IDEListener > ideListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.alice.ide.event.IDEListener >();
	//private java.util.List< edu.cmu.cs.dennisc.alice.ide.editors.common.DropReceptor > dropReceptors = new java.util.LinkedList< edu.cmu.cs.dennisc.alice.ide.editors.common.DropReceptor >();
	private edu.cmu.cs.dennisc.alice.ide.event.IDEListener[] ideListenerArray = null;

	public abstract edu.cmu.cs.dennisc.alice.ast.Node createCopy( edu.cmu.cs.dennisc.alice.ast.Node original );

	private boolean isDragInProgress = false;

	private edu.cmu.cs.dennisc.alice.ast.Comment commentThatWantsFocus = null;

	public edu.cmu.cs.dennisc.alice.ast.Comment getCommentThatWantsFocus() {
		return this.commentThatWantsFocus;
	}
	public void setCommentThatWantsFocus( edu.cmu.cs.dennisc.alice.ast.Comment commentThatWantsFocus ) {
		this.commentThatWantsFocus = commentThatWantsFocus;
	}

	private edu.cmu.cs.dennisc.alice.ide.event.IDEListener[] getIDEListeners() {
		if( this.ideListenerArray != null ) {
			//pass
		} else {
			synchronized( this.ideListeners ) {
				this.ideListenerArray = new edu.cmu.cs.dennisc.alice.ide.event.IDEListener[ this.ideListeners.size() ];
				this.ideListeners.toArray( this.ideListenerArray );
			}
		}
		return this.ideListenerArray;
	}
	protected void promptForLicenseAgreements() {
		final String IS_LICENSE_ACCEPTED_PREFERENCE_KEY = "isLicenseAccepted";
		try {
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.cmu.cs.dennisc.alice.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 1 of 3): Alice 3", edu.cmu.cs.dennisc.alice.License.TEXT, "Alice" );
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.wustl.cse.lookingglass.apis.walkandtouch.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 2 of 3): Looking Glass Walk & Touch API", edu.wustl.cse.lookingglass.apis.walkandtouch.License.TEXT, "the Looking Glass Walk & Touch API" );
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.cmu.cs.dennisc.nebulous.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 3 of 3): The Sims (TM) 2 Art Assets", edu.cmu.cs.dennisc.nebulous.License.TEXT, "The Sims (TM) 2 Art Assets" );
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			javax.swing.JOptionPane.showMessageDialog( this, "You must accept the license agreements in order to use Alice 3, the Looking Glass Walk & Touch API, and The Sims (TM) 2 Art Assets.  Exiting." );
			System.exit( -1 );
		}
	}
	public IDE() {
		IDE.exceptionHandler.setTitle( this.getBugReportSubmissionTitle() );
		IDE.exceptionHandler.setApplicationName( this.getApplicationName() );
		assert s_singleton == null;
		s_singleton = this;

		this.promptForLicenseAgreements();
		
		edu.cmu.cs.dennisc.alice.ide.editors.common.ExpressionLikeSubstance.setBorderFactory( this.createExpressionBorderFactory() );
		edu.cmu.cs.dennisc.alice.ide.editors.common.ExpressionLikeSubstance.setRenderer( this.createExpressionRenderer() );

		edu.cmu.cs.dennisc.alice.ide.editors.common.StatementLikeSubstance.setBorderFactory( this.createStatementBorderFactory() );
		edu.cmu.cs.dennisc.alice.ide.editors.common.StatementLikeSubstance.setRenderer( this.createStatementRenderer() );
		
		edu.cmu.cs.dennisc.alice.ide.editors.code.DropDownPane.setBorderFactory( this.createDropDownBorderFactory() );
		edu.cmu.cs.dennisc.alice.ide.editors.code.DropDownPane.setRenderer( this.createDropDownRenderer() );

		
		//edu.cmu.cs.dennisc.swing.InputPane.setDefaultOwnerFrame( this );
		this.vmForRuntimeProgram = createVirtualMachineForRuntimeProgram();
		this.vmForSceneEditor = createVirtualMachineForSceneEditor();

		this.runOperation = createRunOperation();
		this.exitOperation = createExitOperation();

		getContentPane().addMouseWheelListener( new edu.cmu.cs.dennisc.swing.plaf.metal.FontMouseWheelAdapter() );
		//todo
		setDefaultCloseOperation( javax.swing.JFrame.DO_NOTHING_ON_CLOSE );
		this.addWindowListener( new java.awt.event.WindowListener() {
			public void windowOpened( java.awt.event.WindowEvent e ) {
				IDE.this.handleWindowOpened( e );
			}
			public void windowClosed( java.awt.event.WindowEvent e ) {
			}
			public void windowClosing( java.awt.event.WindowEvent e ) {
				IDE.this.handleQuit( e );
			}
			public void windowActivated( java.awt.event.WindowEvent e ) {
			}
			public void windowDeactivated( java.awt.event.WindowEvent e ) {
			}
			public void windowIconified( java.awt.event.WindowEvent e ) {
			}
			public void windowDeiconified( java.awt.event.WindowEvent e ) {
			}
		} );

		//this.setLocale( new java.util.Locale( "en", "US", "java" ) );
		//javax.swing.JComponent.setDefaultLocale( new java.util.Locale( "en", "US", "java" ) );
	}
	
	public void handleQuit( java.util.EventObject e ) {
		IDE.this.performIfAppropriate( IDE.this.exitOperation, e );
	}

	protected abstract void handleWindowOpened( java.awt.event.WindowEvent e );
	protected edu.cmu.cs.dennisc.alice.ide.lookandfeel.StatementClassBorderFactory createStatementBorderFactory() {
		return new edu.cmu.cs.dennisc.alice.ide.lookandfeel.KnurlBorderFactory();
	}
	protected edu.cmu.cs.dennisc.alice.ide.lookandfeel.StatementClassRenderer createStatementRenderer() {
		return new edu.cmu.cs.dennisc.alice.ide.lookandfeel.RoundedRectangleStatementClassRenderer();
	}
	protected edu.cmu.cs.dennisc.alice.ide.lookandfeel.ExpressionTypeBorderFactory createExpressionBorderFactory() {
		return new edu.cmu.cs.dennisc.alice.ide.lookandfeel.BallAndSocketBorderFactory();
	}
	protected edu.cmu.cs.dennisc.alice.ide.lookandfeel.ExpressionTypeRenderer createExpressionRenderer() {
		return new edu.cmu.cs.dennisc.alice.ide.lookandfeel.BallAndSocketExpressionTypeRenderer();
	}
	protected edu.cmu.cs.dennisc.alice.ide.lookandfeel.DropDownBorderFactory createDropDownBorderFactory() {
		return new edu.cmu.cs.dennisc.alice.ide.lookandfeel.ArrowDropDownBorderFactory();
	}
	protected edu.cmu.cs.dennisc.alice.ide.lookandfeel.DropDownRenderer createDropDownRenderer() {
		return new edu.cmu.cs.dennisc.alice.ide.lookandfeel.ArrowDropDownRenderer();
	}

	protected abstract void handleWindowClosing();
	//	public void addDropReceptorIfNecessary( edu.cmu.cs.dennisc.alice.ide.editors.common.DropReceptor dropReceptor ) {
	//		synchronized( this.dropReceptors ) {
	//			if( this.dropReceptors.contains( dropReceptor ) ) {
	//				//pass
	//			} else {
	//				this.dropReceptors.add( dropReceptor );
	//			}
	//		}
	//	}
	//	public void removeDropReceptorIfNecessary( edu.cmu.cs.dennisc.alice.ide.editors.common.DropReceptor dropReceptor ) {
	//		synchronized( this.dropReceptors ) {
	//			if( this.dropReceptors.contains( dropReceptor ) ) {
	//				//pass
	//			} else {
	//				this.dropReceptors.remove( dropReceptor );
	//			}
	//		}
	//	}
	public java.util.List< ? extends edu.cmu.cs.dennisc.alice.ide.editors.common.DropReceptor > getPotentialDropReceptors( edu.cmu.cs.dennisc.alice.ide.editors.common.PotentiallyDraggablePane source ) {
		if( source instanceof edu.cmu.cs.dennisc.alice.ide.editors.common.ExpressionLikeSubstance ) {
			edu.cmu.cs.dennisc.alice.ide.editors.common.ExpressionLikeSubstance expressionLikeSubstance = (edu.cmu.cs.dennisc.alice.ide.editors.common.ExpressionLikeSubstance)source;
			return getCodeEditorInFocus().findAllPotentialAcceptors( expressionLikeSubstance.getExpressionType() );
		} else {
			java.util.List< edu.cmu.cs.dennisc.alice.ide.editors.common.DropReceptor > rv = new java.util.LinkedList< edu.cmu.cs.dennisc.alice.ide.editors.common.DropReceptor >();
			edu.cmu.cs.dennisc.alice.ide.editors.code.CodeEditor codeEditor = this.getCodeEditorInFocus();
			if( codeEditor != null ) {
				rv.add( codeEditor );
			}
			//			for( edu.cmu.cs.dennisc.alice.ide.editors.common.DropReceptor dropReceptor : this.dropReceptors ) {
			//				if( dropReceptor.isPotentiallyAcceptingOf( source ) ) {
			//					rv.add( dropReceptor );
			//				}
			//			}
			return rv;
		}
	}

	private edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vmForRuntimeProgram;
	private edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vmForSceneEditor;

	public abstract void createProjectFromBootstrap();
	public abstract void promptUserForStatement( java.awt.event.MouseEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Statement > taskObserver );
	public abstract void promptUserForExpression( edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression, java.awt.event.MouseEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver );
	public abstract void promptUserForMore( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter, java.awt.event.MouseEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver );
	public abstract void unsetPreviousExpression();

	public edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine createVirtualMachineForRuntimeProgram() {
		return new edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine();
	}
	public edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine createVirtualMachineForSceneEditor() {
		return new edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine();
	}
	//todo: remove?
	public final edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine getVirtualMachineForRuntimeProgram() {
		return this.vmForRuntimeProgram;
	}
	//todo: remove?
	public final edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine getVirtualMachineForSceneEditor() {
		return this.vmForSceneEditor;
	}

	private Operation runOperation;
	private Operation exitOperation;

	protected abstract Operation createRunOperation();
	protected abstract Operation createExitOperation();
	public final Operation getRunOperation() {
		return this.runOperation;
	}
	public final Operation getExitOperation() {
		return this.exitOperation;
	}

	public void addIDEListener( edu.cmu.cs.dennisc.alice.ide.event.IDEListener l ) {
		synchronized( this.ideListeners ) {
			this.ideListeners.add( l );
			this.ideListenerArray = null;
		}
	}

	public void removeIDEListener( edu.cmu.cs.dennisc.alice.ide.event.IDEListener l ) {
		synchronized( this.ideListeners ) {
			this.ideListeners.remove( l );
			this.ideListenerArray = null;
		}
	}

	protected void fireLocaleChanging( edu.cmu.cs.dennisc.alice.ide.event.LocaleEvent e ) {
		synchronized( this.ideListeners ) {
			for( edu.cmu.cs.dennisc.alice.ide.event.IDEListener l : this.ideListeners ) {
				l.localeChanging( e );
			}
		}
	}
	protected void fireLocaleChanged( edu.cmu.cs.dennisc.alice.ide.event.LocaleEvent e ) {
		synchronized( this.ideListeners ) {
			for( edu.cmu.cs.dennisc.alice.ide.event.IDEListener l : this.ideListeners ) {
				l.localeChanged( e );
			}
		}
	}

	@Override
	public void setLocale( java.util.Locale locale ) {
		if( this.ideListeners != null ) {
			java.util.Locale prevLocale = this.getLocale();
			edu.cmu.cs.dennisc.alice.ide.event.LocaleEvent e = new edu.cmu.cs.dennisc.alice.ide.event.LocaleEvent( this, prevLocale, locale );
			fireLocaleChanging( e );
			super.setLocale( locale );

			//todo: remove
			javax.swing.JComponent.setDefaultLocale( locale );
			//

			fireLocaleChanged( e );
		} else {
			super.setLocale( locale );
		}
	}

	protected void fireProjectOpening( edu.cmu.cs.dennisc.alice.ide.event.ProjectOpenEvent e ) {
		synchronized( this.ideListeners ) {
			for( edu.cmu.cs.dennisc.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.projectOpening( e );
			}
		}
	}

	protected void fireProjectOpened( edu.cmu.cs.dennisc.alice.ide.event.ProjectOpenEvent e ) {
		synchronized( this.ideListeners ) {
			for( edu.cmu.cs.dennisc.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.projectOpened( e );
			}
		}
	}

	public edu.cmu.cs.dennisc.alice.Project getProject() {
		return this.project;
	}

	public void setProject( edu.cmu.cs.dennisc.alice.Project project ) {
		edu.cmu.cs.dennisc.alice.ide.event.ProjectOpenEvent e = new edu.cmu.cs.dennisc.alice.ide.event.ProjectOpenEvent( this, this.project, project );
		fireProjectOpening( e );
		this.project = project;
		fireProjectOpened( e );
	}

	public java.io.File getFile() {
		return this.file;
	}
	public void setFile( java.io.File file ) {
		if( file.exists() ) {
			this.file = file;
			this.setProject( edu.cmu.cs.dennisc.alice.io.FileUtilities.readProject( this.file ) );
			this.updateTitle();
		} else {
			StringBuffer sb = new StringBuffer();
			sb.append( "Cannot read project from file:\n\t" );
			sb.append( file.getAbsolutePath() );
			sb.append( "\nIt does not exist." );
			javax.swing.JOptionPane.showMessageDialog( this, sb.toString(), "Cannot read file", javax.swing.JOptionPane.ERROR_MESSAGE );
		}
	}

	protected void fireMethodFocusChanging( edu.cmu.cs.dennisc.alice.ide.event.FocusedCodeChangeEvent e ) {
		synchronized( this.ideListeners ) {
			for( edu.cmu.cs.dennisc.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.focusedCodeChanging( e );
			}
		}
	}

	protected void fireMethodFocusChanged( edu.cmu.cs.dennisc.alice.ide.event.FocusedCodeChangeEvent e ) {
		synchronized( this.ideListeners ) {
			for( edu.cmu.cs.dennisc.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.focusedCodeChanged( e );
			}
		}
	}

	public String getInstanceTextForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field, boolean isOutOfScopeTagDesired ) {
		String text;
		if( field != null ) {
			text = field.getName();
			edu.cmu.cs.dennisc.alice.ast.AbstractCode focusedCode = getFocusedCode();
			if( focusedCode != null ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType scopeType = focusedCode.getDeclaringType();
				if( field.getValueType() == scopeType ) {
					text = "this (a.k.a. " + text + ")";
				} else if( field.getDeclaringType() == scopeType ) {
					text = "this." + text;
				} else if( isOutOfScopeTagDesired ) {
					text = "out of scope: " + text;
				}
			}
		} else {
			text = null;
		}
		return text;
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractCode getFocusedCode() {
		return this.focusedCode;
	}

	public void setFocusedCode( edu.cmu.cs.dennisc.alice.ast.AbstractCode nextFocusedCode ) {
		if( nextFocusedCode == this.focusedCode ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.alice.ide.event.FocusedCodeChangeEvent e = new edu.cmu.cs.dennisc.alice.ide.event.FocusedCodeChangeEvent( this, this.focusedCode, nextFocusedCode );
			fireMethodFocusChanging( e );
			this.focusedCode = nextFocusedCode;
			fireMethodFocusChanged( e );
		}
	}

	protected void fireFieldSelectionChanging( edu.cmu.cs.dennisc.alice.ide.event.FieldSelectionEvent e ) {
		synchronized( this.ideListeners ) {
			for( edu.cmu.cs.dennisc.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.fieldSelectionChanging( e );
			}
		}
	}

	protected void fireFieldSelectionChanged( edu.cmu.cs.dennisc.alice.ide.event.FieldSelectionEvent e ) {
		synchronized( this.ideListeners ) {
			for( edu.cmu.cs.dennisc.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.fieldSelectionChanged( e );
			}
		}
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractField getFieldSelection() {
		return this.fieldSelection;
	}

	public void setFieldSelection( edu.cmu.cs.dennisc.alice.ast.AbstractField fieldSelection ) {
		edu.cmu.cs.dennisc.alice.ide.event.FieldSelectionEvent e = new edu.cmu.cs.dennisc.alice.ide.event.FieldSelectionEvent( this, this.fieldSelection, fieldSelection );
		fireFieldSelectionChanging( e );
		this.fieldSelection = fieldSelection;
		fireFieldSelectionChanged( e );
	}

	protected void fireTransientSelectionChanging( edu.cmu.cs.dennisc.alice.ide.event.TransientSelectionEvent e ) {
		synchronized( this.ideListeners ) {
			for( edu.cmu.cs.dennisc.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.transientSelectionChanging( e );
			}
		}
	}

	protected void fireTransientSelectionChanged( edu.cmu.cs.dennisc.alice.ide.event.TransientSelectionEvent e ) {
		synchronized( this.ideListeners ) {
			for( edu.cmu.cs.dennisc.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.transientSelectionChanged( e );
			}
		}
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractTransient getTransientSelection() {
		return this.transientSelection;
	}

	public void setTransientSelection( edu.cmu.cs.dennisc.alice.ast.AbstractTransient transientSelection ) {
		edu.cmu.cs.dennisc.alice.ide.event.TransientSelectionEvent e = new edu.cmu.cs.dennisc.alice.ide.event.TransientSelectionEvent( this, this.transientSelection, transientSelection );
		fireTransientSelectionChanging( e );
		this.transientSelection = transientSelection;
		fireTransientSelectionChanged( e );
	}

	private java.util.Stack< Operation > history = new java.util.Stack< Operation >();
	private int historyLengthAtLastFileOperation = 0;
	private boolean isMarkedChanged = false;

	private boolean isChanged() {
		return this.isMarkedChanged || this.history.size() > this.historyLengthAtLastFileOperation;
	}
	public boolean isProjectUpToDateWithFile() {
		if( this.file != null ) {
			return isChanged() == false;
		} else {
			return true;
		}
	}
	@Deprecated
	public void markChanged( String description ) {
		this.isMarkedChanged = true;
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: convert change to operation ( " + description + " )" );
		this.updateTitle();
	}

	protected void addToHistory( Operation operation ) {
		this.history.push( operation );
		updateTitle();
	}
	protected void handlePreparedOperation( Operation operation, java.util.EventObject e, java.util.List< java.util.EventObject > preparationUpdates, Operation.PreparationResult preparationResult ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "handlePreparedOperation", operation, preparationResult );
		if( preparationResult != null ) {
			if( preparationResult == Operation.PreparationResult.CANCEL ) {
				//pass
			} else {
				operation.perform();
				if( preparationResult == Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY ) {
					addToHistory( operation );
				}
			}
		}
	}
	public void performIfAppropriate( Operation operation, java.util.EventObject e ) {
		final java.util.List< java.util.EventObject > preparationUpdates = new java.util.LinkedList< java.util.EventObject >();
		Operation.PreparationResult preparationResult = operation.prepare( e, new Operation.PreparationObserver() {
			public void update( java.util.EventObject e ) {
				preparationUpdates.add( e );
			}
		} );
		handlePreparedOperation( operation, e, preparationUpdates, preparationResult );
	}

	private void updateHistoryLengthAtLastFileOperation() {
		//this.history.clear();
		this.historyLengthAtLastFileOperation = this.history.size();
		this.isMarkedChanged = false;
		this.updateTitle();
	}
	public void loadProjectFrom( java.io.File file ) {
		this.mapUUIDToNode.clear();
		this.updateHistoryLengthAtLastFileOperation();
		setFile( file );
		//todo: find a better solution to concurrent modification exception
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				//edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = getProject().getProgramType().getDeclaredFields().get( 0 );
				edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = getSceneField();
				if( sceneField != null ) {
					edu.cmu.cs.dennisc.alice.ast.AbstractMethod runMethod = sceneField.getValueType().getDeclaredMethod( "run" );
					IDE.this.setFocusedCode( runMethod );
					java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractField > fields = sceneField.getValueType().getDeclaredFields();
					final int N = fields.size();
					int i = N-1;
					while( i>=0 ) {
						edu.cmu.cs.dennisc.alice.ast.AbstractField field = fields.get( i );
						if( field.getValueType().isArray() ) {
							//pass
						} else {
							IDE.this.setFieldSelection( field );
							break;
						}
						i --;
					}
				}
			}
		} );
	}
	public void loadProjectFrom( String path ) {
		loadProjectFrom( new java.io.File( path ) );
	}
	protected abstract void generateCodeForSceneSetUp();
	protected abstract void preserveProjectProperties();
	public void saveProjectTo( java.io.File file ) {
		edu.cmu.cs.dennisc.alice.Project project = getProject();
		this.generateCodeForSceneSetUp();
		this.preserveProjectProperties();
		edu.cmu.cs.dennisc.alice.io.FileUtilities.writeProject( project, file );
		this.file = file;
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "project saved to: ", file.getAbsolutePath() );
		this.updateHistoryLengthAtLastFileOperation();
	}
	public void saveProjectTo( String path ) {
		saveProjectTo( new java.io.File( path ) );
	}

	//	private static java.util.ResourceBundle getResourceBundleForASTColors( java.util.Locale locale ) {
	//		String baseName = "edu.cmu.cs.dennisc.alice.ast.Colors";
	//		return java.util.ResourceBundle.getBundle( baseName, locale );
	//	}

	public static java.awt.Color getColorForASTClass( Class< ? > cls ) {
		//		java.util.ResourceBundle resourceBundle = getResourceBundleForASTColors( javax.swing.JComponent.getDefaultLocale() );
		//		String key;
		//
		//		Class< ? > originalCls = cls;
		//		do {
		//			if( cls != null ) {
		//				key = cls.getSimpleName();
		//				cls = cls.getSuperclass();
		//			} else {
		//				throw new RuntimeException( "cannot find resource for " + originalCls );
		//			}
		//			try {
		//				String s = resourceBundle.getString( key );
		//				break;
		//			} catch( RuntimeException re ) {
		//				//pass;
		//			}
		//		} while( true );
		//		
		java.awt.Color rv;
		String s = edu.cmu.cs.dennisc.util.ResourceBundleUtilities.getStringFromSimpleNames( cls, "edu.cmu.cs.dennisc.alice.ast.Colors", javax.swing.JComponent.getDefaultLocale() );
		//String s = resourceBundle.getString( key );
		if( s.startsWith( "0x" ) ) {
			int i = Integer.parseInt( s.substring( 2 ), 16 );
			rv = new java.awt.Color( i );
		} else {
			String[] rgbStrings = s.split( " " );
			int r = Integer.parseInt( rgbStrings[ 0 ] );
			int g = Integer.parseInt( rgbStrings[ 1 ] );
			int b = Integer.parseInt( rgbStrings[ 2 ] );
			rv = new java.awt.Color( r, g, b );
		}
		return rv;
	}
	public static java.awt.Color getColorForASTInstance( edu.cmu.cs.dennisc.alice.ast.Node node ) {
		if( node != null ) {
			return getColorForASTClass( node.getClass() );
		} else {
			return java.awt.Color.RED;
		}
	}

	public java.awt.Color getProcedureColor() {
		return new java.awt.Color( 0xedc484 );
	}
	public java.awt.Color getFunctionColor() {
		return new java.awt.Color( 0xb4ccaf );
	}
	public java.awt.Color getConstructorColor() {
		return getFunctionColor();
	}
	public java.awt.Color getCodeDeclaredInAliceColor( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice codeDeclaredInAlice ) {
		if( codeDeclaredInAlice instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)codeDeclaredInAlice;
			if( methodDeclaredInAlice.isProcedure() ) {
				return getProcedureColor();
			} else {
				return getFunctionColor();
			}
		} else {
			return getConstructorColor();
		}
	}
	public java.awt.Color getFieldColor() {
		return new java.awt.Color( 0x85abc9 );
	}

	@Deprecated
	protected edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getProgramType() {
		return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)getProject().getProgramType();
	}
	@Deprecated
	protected edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getSceneField() {
		return getProgramType().fields.get( 0 );

	}
	@Deprecated
	protected edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getSceneType() {
		return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)getSceneField().getDeclaringType();
	}
	public boolean isInScope() {
		//todo?
		return createInstanceExpression() != null;
	}
	public edu.cmu.cs.dennisc.alice.ast.Expression createInstanceExpression( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) /*throws OutOfScopeException*/{
		edu.cmu.cs.dennisc.alice.ast.AbstractCode focusedCode = getFocusedCode();
		if( focusedCode != null && field != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType focusedCodeDeclaringType = focusedCode.getDeclaringType();
			if( focusedCodeDeclaringType != null ) {
				edu.cmu.cs.dennisc.alice.ast.ThisExpression thisExpression = new edu.cmu.cs.dennisc.alice.ast.ThisExpression();
				if( focusedCodeDeclaringType.equals( field.getValueType() ) ) {
					return thisExpression;
				} else if( focusedCodeDeclaringType.equals( field.getDeclaringType() ) ) {
					return new edu.cmu.cs.dennisc.alice.ast.FieldAccess( thisExpression, field );
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	public edu.cmu.cs.dennisc.alice.ast.Expression createInstanceExpression() /*throws OutOfScopeException*/{
		return createInstanceExpression( getFieldSelection() );
	}

	public boolean isSelectedFieldInScope() {
		return createInstanceExpression() != null;
	}
	public boolean isFieldInScope( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		return createInstanceExpression( field ) != null;
	}
//	public abstract edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice promptUserForParameterDeclaredInAlice( edu.cmu.cs.dennisc.alice.ast.NodeListProperty< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice > parametersProperty );
	//	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractField, edu.cmu.cs.dennisc.alice.ide.operations.FieldSelectionOperation > mapFieldToSelectionOperation = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractField, edu.cmu.cs.dennisc.alice.ide.operations.FieldSelectionOperation >();
	//	public edu.cmu.cs.dennisc.alice.ide.operations.FieldSelectionOperation getFieldSelectionOperationForField( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
	//		edu.cmu.cs.dennisc.alice.ide.operations.FieldSelectionOperation rv = mapFieldToSelectionOperation.get( field );
	//		if( rv != null ) {
	//			//pass
	//		} else {
	//			rv = new edu.cmu.cs.dennisc.alice.ide.operations.FieldSelectionOperation( field );
	//			mapFieldToSelectionOperation.put( field, rv );
	//		}
	//		return rv;
	//	}
	public boolean isDragInProgress() {
		return this.isDragInProgress;
	}
	public void setDragInProgress( boolean isDragInProgress ) {
		this.isDragInProgress = isDragInProgress;
		this.currentDropReceptorComponent = null;
	}
	
	private java.util.Map< java.util.UUID, edu.cmu.cs.dennisc.alice.ast.Node > mapUUIDToNode = new java.util.HashMap< java.util.UUID, edu.cmu.cs.dennisc.alice.ast.Node >();


	private static <E extends edu.cmu.cs.dennisc.alice.ast.Node> E getAncestor( edu.cmu.cs.dennisc.alice.ast.Node node, Class<E> cls ) {
		edu.cmu.cs.dennisc.alice.ast.Node ancestor = node.getParent();
		while( ancestor != null ) {
			if( cls.isAssignableFrom( ancestor.getClass() ) ) {
				break;
			} else {
				ancestor = ancestor.getParent();
			}
		}
		return (E)ancestor;
	}
	
	protected void ensureNodeVisible( edu.cmu.cs.dennisc.alice.ast.Node node ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractCode nextFocusedCode = getAncestor( node, edu.cmu.cs.dennisc.alice.ast.AbstractCode.class );
		if( nextFocusedCode != null ) {
			this.setFocusedCode( nextFocusedCode );
		}
	}
	private edu.cmu.cs.dennisc.alice.ast.Node getNodeForUUID( java.util.UUID uuid ) {
		edu.cmu.cs.dennisc.alice.ast.Node rv = mapUUIDToNode.get( uuid );
		if( rv != null ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = this.getProgramType();
			type.crawl( new edu.cmu.cs.dennisc.pattern.Crawler() {
				public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
					if( crawlable instanceof edu.cmu.cs.dennisc.alice.ast.Node ) {
						edu.cmu.cs.dennisc.alice.ast.Node node = (edu.cmu.cs.dennisc.alice.ast.Node)crawlable;
						mapUUIDToNode.put( node.getUUID(), node );
					}
				}
			}, true );
			rv = mapUUIDToNode.get( uuid );
		}
		return rv;
	}
	public java.awt.Component getComponentForNode( edu.cmu.cs.dennisc.alice.ast.Node node ) {
		if( node instanceof edu.cmu.cs.dennisc.alice.ast.Statement ) {
			edu.cmu.cs.dennisc.alice.ast.Statement statement = (edu.cmu.cs.dennisc.alice.ast.Statement)node;
			ensureNodeVisible( node );
			return edu.cmu.cs.dennisc.alice.ide.editors.code.AbstractStatementPane.lookup( statement );
		} else {
			return null;
		}
	}
	public java.awt.Component getComponentForNode( java.util.UUID uuid ) {
		return getComponentForNode( getNodeForUUID( uuid ) );
	}
	
}
