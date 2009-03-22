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
package org.alice.ide;

import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;

import org.alice.ide.operations.AbstractItemSelectionOperation;

import edu.cmu.cs.dennisc.alice.ast.AbstractParameter;
import edu.cmu.cs.dennisc.alice.ast.AbstractType;
import edu.cmu.cs.dennisc.alice.ast.Expression;
import edu.cmu.cs.dennisc.alice.ast.Node;
import edu.cmu.cs.dennisc.alice.ast.Statement;
import edu.cmu.cs.dennisc.task.TaskObserver;

/**
 * @author Dennis Cosgrove
 */

public abstract class IDE extends zoot.ZFrame implements zoot.DragAndDropOperation { //todo
	private static org.alice.ide.issue.ExceptionHandler exceptionHandler;
	private static IDE singleton;

	static {
		IDE.exceptionHandler = new org.alice.ide.issue.ExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler( IDE.exceptionHandler );
	}

	public static IDE getSingleton() {
		return IDE.singleton;
	}

	public zoot.DragAndDropOperation getDragAndDropOperation() {
		//todo
		return this;
	}

	private org.alice.ide.sceneeditor.SceneEditor sceneEditor = this.createSceneEditor();
	private org.alice.ide.memberseditor.MembersEditor membersEditor = this.createClassMembersEditor();
	private org.alice.ide.listenerseditor.ListenersEditor listenersEditor = this.createListenersEditor();
	private org.alice.ide.editorstabbedpane.EditorsTabbedPane editorsTabbedPane = this.createEditorsTabbedPane();
	private zoot.ZLabel feedback = new zoot.ZLabel();

	protected org.alice.ide.sceneeditor.SceneEditor createSceneEditor() {
		return new org.alice.ide.sceneeditor.SceneEditor();
	}
	protected org.alice.ide.listenerseditor.ListenersEditor createListenersEditor() {
		return new org.alice.ide.listenerseditor.ListenersEditor();
	}
	protected org.alice.ide.memberseditor.MembersEditor createClassMembersEditor() {
		return new org.alice.ide.memberseditor.MembersEditor();
	}
	protected org.alice.ide.editorstabbedpane.EditorsTabbedPane createEditorsTabbedPane() {
		return new org.alice.ide.editorstabbedpane.EditorsTabbedPane();
	}

	private java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > expressionFillerInners;

	public IDE() {
		IDE.exceptionHandler.setTitle( this.getBugReportSubmissionTitle() );
		IDE.exceptionHandler.setApplicationName( this.getApplicationName() );
		assert IDE.singleton == null;
		IDE.singleton = this;

		this.promptForLicenseAgreements();

		this.addIDEListener( this.sceneEditor );
		this.addIDEListener( this.membersEditor );
		this.addIDEListener( this.listenersEditor );
		this.addIDEListener( this.editorsTabbedPane );

		Perspective perspective = new Perspective();
		perspective.activate( this.sceneEditor, this.membersEditor, this.listenersEditor, this.editorsTabbedPane );
		this.getContentPane().setLayout( new java.awt.BorderLayout() );
		this.getContentPane().add( perspective, java.awt.BorderLayout.CENTER );
		this.getContentPane().add( this.feedback, java.awt.BorderLayout.SOUTH );

		//edu.cmu.cs.dennisc.swing.InputPane.setDefaultOwnerFrame( this );
		this.vmForRuntimeProgram = createVirtualMachineForRuntimeProgram();
		this.vmForSceneEditor = createVirtualMachineForSceneEditor();

		this.runOperation = this.createRunOperation();
		this.exitOperation = this.createExitOperation();

		getContentPane().addMouseWheelListener( new edu.cmu.cs.dennisc.swing.plaf.metal.FontMouseWheelAdapter() );

		//this.setLocale( new java.util.Locale( "en", "US", "java" ) );
		//javax.swing.JComponent.setDefaultLocale( new java.util.Locale( "en", "US", "java" ) );

		this.expressionFillerInners = this.addExpressionFillerInners( new java.util.LinkedList< org.alice.ide.cascade.fillerinners.ExpressionFillerInner >() );
		javax.swing.JMenuBar menuBar = this.createMenuBar();
		this.setJMenuBar( menuBar );
	}
	protected java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > addExpressionFillerInners( java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > rv ) {
		rv.add( new org.alice.ide.cascade.fillerinners.NumberFillerInner() );
		rv.add( new org.alice.ide.cascade.fillerinners.IntegerFillerInner() );
		rv.add( new org.alice.ide.cascade.fillerinners.BooleanFillerInner() );
		rv.add( new org.alice.ide.cascade.fillerinners.StringFillerInner() );
		return rv;
	}

	protected javax.swing.JMenuBar createMenuBar() {
		javax.swing.JMenuBar rv = new javax.swing.JMenuBar();

		javax.swing.JMenu fileMenu = zoot.ZManager.createMenu( "File", java.awt.event.KeyEvent.VK_F, 
				this.exitOperation 
		);
		javax.swing.JMenu editMenu = zoot.ZManager.createMenu( "Edit", java.awt.event.KeyEvent.VK_E, 
				new org.alice.ide.operations.edit.UndoOperation(), 
				new org.alice.ide.operations.edit.RedoOperation(),
				zoot.ZManager.MENU_SEPARATOR,
				new org.alice.ide.operations.edit.CutOperation(),
				new org.alice.ide.operations.edit.CopyOperation(),
				new org.alice.ide.operations.edit.PasteOperation() 
		);
		javax.swing.JMenu runMenu = zoot.ZManager.createMenu( "Run", java.awt.event.KeyEvent.VK_R,
				this.runOperation
		);
		
		class LocaleItemSelectionOperation extends AbstractItemSelectionOperation< java.util.Locale > {
			private javax.swing.ListModel listModel = new javax.swing.AbstractListModel() {
				private java.util.Locale[] candidates = {
						new java.util.Locale( "en", "US" ),
						new java.util.Locale( "en", "US", "complex" ),
						new java.util.Locale( "en", "US", "java" )
				};
				public Object getElementAt( int index ) {
					return this.candidates[ index ];
				}
				public int getSize() {
					return this.candidates.length;
				}
			};
			public LocaleItemSelectionOperation() {
				//this.getSingleSelectionModelForConfiguringSwingComponents().setSelectedIndex( 0 );
			}
			public javax.swing.ListModel getListModel() {
				return this.listModel;
			}
//			public String getText( java.util.Locale locale ) {
//				return locale.getDisplayName();
//			}
//			public javax.swing.Icon getIcon( java.util.Locale locale ) {
//				return null;
//			}
			
			public void performSelectionChange( zoot.ItemSelectionContext< java.util.Locale > context ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "performSelectionChange:", context );
				context.cancel();
			}
		}
		
		javax.swing.JMenu setLocaleMenu = zoot.ZManager.createMenu( "Set Locale", java.awt.event.KeyEvent.VK_L,
				new LocaleItemSelectionOperation()
		);
		
		javax.swing.JMenu windowMenu = zoot.ZManager.createMenu( "Window", java.awt.event.KeyEvent.VK_W,
				new org.alice.ide.operations.window.ToggleExpandContractSceneEditorOperation()
		);
		windowMenu.add( setLocaleMenu );
		javax.swing.JMenu helpMenu = zoot.ZManager.createMenu( "Help", java.awt.event.KeyEvent.VK_H, 
				new org.alice.ide.operations.help.HelpOperation(), 
				new org.alice.ide.operations.help.AboutOperation() 
		);
		rv.add( fileMenu );
		rv.add( editMenu );
		rv.add( runMenu );
		rv.add( windowMenu );
		rv.add( helpMenu );
		return rv;
	}

	public boolean isJava() {
		return getLocale().getVariant().equals( "java" );
	}

	private Factory factory = this.createFactory();

	public Factory getFactory() {
		return this.factory;
	}
	protected Factory createFactory() {
		return new Factory();
	}

	private org.alice.ide.memberseditor.Factory templatesFactory = this.createTemplatesFactory();

	public org.alice.ide.memberseditor.Factory getTemplatesFactory() {
		return this.templatesFactory;
	}
	protected org.alice.ide.memberseditor.Factory createTemplatesFactory() {
		return new org.alice.ide.memberseditor.Factory();
	}

	private org.alice.ide.codeeditor.Factory codeFactory = this.createCodeFactory();

	public org.alice.ide.codeeditor.Factory getCodeFactory() {
		return this.codeFactory;
	}
	protected org.alice.ide.codeeditor.Factory createCodeFactory() {
		return new org.alice.ide.codeeditor.Factory();
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

	private java.util.List< zoot.DropReceptor > dropReceptors = new java.util.LinkedList< zoot.DropReceptor >();

	protected abstract org.alice.ide.codeeditor.CodeEditor getCodeEditorInFocus();

	private ComponentStencil stencil;
	private java.util.List< ? extends java.awt.Component > holes = null;
	private zoot.ZDragComponent potentialDragSource;
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

	public void showStencilOver( zoot.ZDragComponent potentialDragSource, final edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		org.alice.ide.codeeditor.CodeEditor codeEditor = getCodeEditorInFocus();
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

	public void handleDragStarted( zoot.DropReceptor dropReceptor ) {
		this.potentialDragSource = null;
		if( this.stencil != null && this.holes != null ) {
			this.stencil.repaint();
		}
	}
	public void handleDragEntered( zoot.DropReceptor dropReceptor ) {
		this.currentDropReceptorComponent = dropReceptor.getAWTComponent();
		if( this.stencil != null && this.holes != null ) {
			this.stencil.repaint();
		}
	}
	public void handleDragExited( zoot.DropReceptor dropReceptor ) {
		this.currentDropReceptorComponent = null;
		if( this.stencil != null && this.holes != null ) {
			this.stencil.repaint();
		}
	}
	public void handleDragStopped( zoot.DropReceptor dropReceptor ) {
	}

	private edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vmForRuntimeProgram;
	private edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vmForSceneEditor;

	private java.io.File file = null;
	private edu.cmu.cs.dennisc.alice.Project project = null;

	private edu.cmu.cs.dennisc.alice.ast.AbstractCode focusedCode = null;
	private edu.cmu.cs.dennisc.alice.ast.AbstractField fieldSelection = null;
	private edu.cmu.cs.dennisc.alice.ast.AbstractTransient transientSelection = null;

	private java.util.List< org.alice.ide.event.IDEListener > ideListeners = new java.util.LinkedList< org.alice.ide.event.IDEListener >();
	private org.alice.ide.event.IDEListener[] ideListenerArray = null;

	public abstract edu.cmu.cs.dennisc.alice.ast.Node createCopy( edu.cmu.cs.dennisc.alice.ast.Node original );

	private zoot.ActionOperation runOperation = this.createRunOperation();
	private zoot.ActionOperation exitOperation = this.createExitOperation();

	protected zoot.ActionOperation createRunOperation() {
		return new org.alice.ide.operations.run.RunOperation();
	}
	protected zoot.ActionOperation createExitOperation() {
		return new org.alice.ide.operations.file.ExitOperation();
	}
	public final zoot.ActionOperation getRunOperation() {
		return this.runOperation;
	}
	public final zoot.ActionOperation getExitOperation() {
		return this.exitOperation;
	}

	private boolean isDragInProgress = false;

	private edu.cmu.cs.dennisc.alice.ast.Comment commentThatWantsFocus = null;

	public edu.cmu.cs.dennisc.alice.ast.Comment getCommentThatWantsFocus() {
		return this.commentThatWantsFocus;
	}
	public void setCommentThatWantsFocus( edu.cmu.cs.dennisc.alice.ast.Comment commentThatWantsFocus ) {
		this.commentThatWantsFocus = commentThatWantsFocus;
	}

	private org.alice.ide.event.IDEListener[] getIDEListeners() {
		if( this.ideListenerArray != null ) {
			//pass
		} else {
			synchronized( this.ideListeners ) {
				this.ideListenerArray = new org.alice.ide.event.IDEListener[ this.ideListeners.size() ];
				this.ideListeners.toArray( this.ideListenerArray );
			}
		}
		return this.ideListenerArray;
	}
	protected void promptForLicenseAgreements() {
		final String IS_LICENSE_ACCEPTED_PREFERENCE_KEY = "isLicenseAccepted";
		try {
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.cmu.cs.dennisc.alice.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 1 of 3): Alice 3", edu.cmu.cs.dennisc.alice.License.TEXT, "Alice" );
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.wustl.cse.lookingglass.apis.walkandtouch.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 2 of 3): Looking Glass Walk & Touch API",
					edu.wustl.cse.lookingglass.apis.walkandtouch.License.TEXT_FOR_USE_IN_ALICE, "the Looking Glass Walk & Touch API" );
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.cmu.cs.dennisc.nebulous.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 3 of 3): The Sims (TM) 2 Art Assets",
					edu.cmu.cs.dennisc.nebulous.License.TEXT, "The Sims (TM) 2 Art Assets" );
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			javax.swing.JOptionPane.showMessageDialog( this, "You must accept the license agreements in order to use Alice 3, the Looking Glass Walk & Touch API, and The Sims (TM) 2 Art Assets.  Exiting." );
			System.exit( -1 );
		}
	}

	public void performIfAppropriate( zoot.ActionOperation actionOperation, java.util.EventObject e, boolean isCancelWorthwhile ) {
		zoot.ZManager.performIfAppropriate( actionOperation, e, isCancelWorthwhile );
	}

	@Override
	protected void handleQuit( java.util.EventObject e ) {
		this.performIfAppropriate( this.exitOperation, e, true );
	}
	//	protected abstract void handleWindowOpened( java.awt.event.WindowEvent e );
	//	protected abstract void handleWindowClosing();

	public java.util.List< ? extends zoot.DropReceptor > getPotentialDropReceptors( zoot.ZDragComponent source ) {
		if( source.getSubject() instanceof org.alice.ide.ast.ExpressionLikeSubstance ) {
			org.alice.ide.ast.ExpressionLikeSubstance expressionLikeSubstance = (org.alice.ide.ast.ExpressionLikeSubstance)source.getSubject();
			return getCodeEditorInFocus().findAllPotentialAcceptors( expressionLikeSubstance.getExpressionType() );
		} else {
			java.util.List< zoot.DropReceptor > rv = new java.util.LinkedList< zoot.DropReceptor >();
			org.alice.ide.codeeditor.CodeEditor codeEditor = this.getCodeEditorInFocus();
			if( codeEditor != null ) {
				rv.add( codeEditor );
			}
			//			for( alice.ide.ast.DropReceptor dropReceptor : this.dropReceptors ) {
			//				if( dropReceptor.isPotentiallyAcceptingOf( source ) ) {
			//					rv.add( dropReceptor );
			//				}
			//			}
			return rv;
		}
	}

	//	def addSeparatorIfNecessary( self, blank, text, isNecessary ):
	//		if isNecessary:
	//			blank.addSeparator( text )
	//		return False
	//
	//	def addFillInAndPossiblyPartFills( self, blank, expression, type, type2 ):
	//		blank.addChild( SimpleExpressionFillIn( expression ) )
	//		if type.isAssignableTo( org.alice.apis.moveandturn.PolygonalModel ):
	//			if type2.isAssignableFrom( org.alice.apis.moveandturn.Model ):
	//				typeInJava = type.getFirstTypeEncounteredDeclaredInJava()
	//				clsInJava = typeInJava.getCls()
	//				try:
	//					paramType = clsInJava.Part
	//				except:
	//					paramType = None
	//			else:
	//				paramType = None
	//			if paramType:
	//				getPartMethod = typeInJava.getDeclaredMethod( "getPart", [ paramType ] )
	//				
	//				#todo
	//				
	//				blank.addChild( MethodInvocationFillIn( getPartMethod, expression ) )
	//
	//	def _addExpressionBonusFillInsForType( self, blank, type ):
	//		codeInFocus = self.getFocusedCode()
	//		if codeInFocus:
	//			isNecessary = True
	//			selectedType = codeInFocus.getDeclaringType()
	//			if type.isAssignableFrom( selectedType ):
	//				isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
	//				#blank.addChild( SimpleExpressionFillIn( alice.ast.ThisExpression() ) )
	//				self.addFillInAndPossiblyPartFills( blank, alice.ast.ThisExpression(), selectedType, type )
	//			for field in selectedType.fields.iterator():
	//				fieldType = field.valueType.getValue()
	//				if type.isAssignableFrom( fieldType ):
	//					isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
	//					expression = alice.ast.FieldAccess( alice.ast.ThisExpression(), field )
	//					self.addFillInAndPossiblyPartFills( blank, expression, fieldType, type )
	//				if fieldType.isArray():
	//					fieldComponentType = fieldType.getComponentType()
	//					if type.isAssignableFrom( fieldComponentType ):
	//						isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
	//						expression = alice.ast.FieldAccess( alice.ast.ThisExpression(), field )
	//						blank.addChild( ecc.dennisc.alice.ide.cascade.ArrayAccessFillIn( fieldType, expression ) )
	//					if type.isAssignableFrom( alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ):
	//						isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
	//						fieldAccess = alice.ast.FieldAccess( alice.ast.ThisExpression(), field )
	//						arrayLength = alice.ast.ArrayLength( fieldAccess )
	//						blank.addChild( ecc.dennisc.alice.ide.cascade.SimpleExpressionFillIn( arrayLength ) )
	//					
	//			#acceptableParameters = []
	//			for parameter in codeInFocus.getParameters():
	//				if type.isAssignableFrom( parameter.getValueType() ):
	//					isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
	//					self.addFillInAndPossiblyPartFills( blank, alice.ast.ParameterAccess( parameter ), parameter.getValueType(), type )
	//			for variable in ecc.dennisc.alice.ast.getVariables( codeInFocus ):
	//				if type.isAssignableFrom( variable.valueType.getValue() ):
	//					isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
	//					self.addFillInAndPossiblyPartFills( blank, alice.ast.VariableAccess( variable ), variable.valueType.getValue(), type )
	//			for constant in ecc.dennisc.alice.ast.getConstants( codeInFocus ):
	//				if type.isAssignableFrom( constant.valueType.getValue() ):
	//					isNecessary = self.addSeparatorIfNecessary( blank, "in scope", isNecessary )
	//					self.addFillInAndPossiblyPartFills( blank, alice.ast.ConstantAccess( constant ), constant.valueType.getValue(), type )
	//			if isNecessary:
	//				pass
	//			else:
	//				blank.addSeparator()
	//
	//	def addSampleValueFillIns( self, blank, fillerInner ):
	//		fillerInner.addFillIns( blank )
	//		
	//	def addFillIns( self, blank, type ):
	//		if type is alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.AngleInRevolutions ):
	//			type = alice.ast.TypeDeclaredInJava.get( java.lang.Number )
	//			fillInType = alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.AngleInRevolutions )
	//		elif type is alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Portion ):
	//			type = alice.ast.TypeDeclaredInJava.get( java.lang.Number )
	//			fillInType = alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Portion )
	//		else:
	//			if type is alice.ast.TypeDeclaredInJava.get( java.lang.Object ):
	//				fillInType = alice.ast.TypeDeclaredInJava.get( java.lang.String )
	//			elif type is alice.ast.TypeDeclaredInJava.get( java.lang.Double ):
	//				type = alice.ast.TypeDeclaredInJava.get( java.lang.Number )
	//				fillInType = alice.ast.TypeDeclaredInJava.get( java.lang.Number )
	//			else:
	//				fillInType = type
	//		if self.__dict__.has_key( "_prevExpression" ):
	//			if self._prevExpression.getType().isAssignableTo( type ):
	//				blank.addChild( ecc.dennisc.alice.ide.cascade.PrevExpressionFillIn( self._prevExpression ) )
	//				blank.addSeparator()
	//				#self.addFillInAndPossiblyPartFills( blank, self._prevExpression, self._prevExpression.getType() )
	//
	//		if type.isAssignableTo( java.lang.Enum ):
	//			self.addSampleValueFillIns( blank, ecc.dennisc.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner( type ) )
	//		else:
	//			for fillerInner in self._fillerInners:
	//				if fillerInner.isAssignableTo( fillInType ):
	//					self.addSampleValueFillIns( blank, fillerInner )
	//					break
	//#					if type is alice.ast.TypeDeclaredInJava.get( java.lang.Object ):
	//#						class MyMenuFillIn( edu.cmu.cs.dennisc.cascade.MenuFillIn ):
	//#							def __init__(self, fillerInner ):
	//#								edu.cmu.cs.dennisc.cascade.MenuFillIn.__init__( self, fillerInner._type.getName() )
	//#								self._fillerInner = fillerInner
	//#							def addChildrenToBlank(self, blank):
	//#								self._fillerInner.addFillIns( blank )
	//#						blank.addChild( MyMenuFillIn( fillerInner ) )
	//#					else:
	//#						self.addSampleValueFillIns( blank, fillerInner )
	//					
	//
	//		self._addExpressionBonusFillInsForType( blank, type )
	//
	//		if type.isArray():
	//			prevArray = None
	//			#todo
	//#			if self.__dict__.has_key( "_prevExpression" ):
	//#				if True: #todo? self._prevExpression.getType().isAssignableTo( type ):
	//#					prevArray = self._prevExpression
	//			blank.addChild( ecc.dennisc.alice.ide.cascade.CustomArrayFillIn( prevArray ) )
	//
	//		if blank.getChildren().size():
	//			pass
	//		else:
	//			message = "sorry.  no fillins found for " + type.getName() + ". canceling."
	//			blank.addChild( edu.cmu.cs.dennisc.cascade.CancelFillIn( message ) )
	public void addFillIns( edu.cmu.cs.dennisc.cascade.Blank blank, AbstractType type ) {
		for( org.alice.ide.cascade.fillerinners.ExpressionFillerInner expressionFillerInner : this.expressionFillerInners ) {
			if( expressionFillerInner.isAssignableTo( type ) ) {
				expressionFillerInner.addFillIns( blank );
			}
		}
	}
	public abstract void createProjectFromBootstrap();
	public abstract void promptUserForStatement( java.awt.event.MouseEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Statement > taskObserver );

	private edu.cmu.cs.dennisc.alice.ast.Expression prevExpression = null;

	private edu.cmu.cs.dennisc.cascade.Blank createExpressionBlank( edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression ) {
		this.prevExpression = prevExpression;
		return new org.alice.ide.cascade.ExpressionBlank( type );

	}
	public void promptUserForExpression( edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression, java.awt.event.MouseEvent e,
			edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
		edu.cmu.cs.dennisc.cascade.Blank blank = createExpressionBlank( type, prevExpression );
		blank.showPopupMenu( e.getComponent(), e.getX(), e.getY(), taskObserver );
	}
	public void promptUserForMore( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter, java.awt.event.MouseEvent e, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
		edu.cmu.cs.dennisc.cascade.Blank blank = createExpressionBlank( parameter.getValueType(), null );
		blank.showPopupMenu( e.getComponent(), e.getX(), e.getY(), taskObserver );
	}
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

	public void addIDEListener( org.alice.ide.event.IDEListener l ) {
		synchronized( this.ideListeners ) {
			this.ideListeners.add( l );
			this.ideListenerArray = null;
		}
	}

	public void removeIDEListener( org.alice.ide.event.IDEListener l ) {
		synchronized( this.ideListeners ) {
			this.ideListeners.remove( l );
			this.ideListenerArray = null;
		}
	}

	protected void fireLocaleChanging( org.alice.ide.event.LocaleEvent e ) {
		synchronized( this.ideListeners ) {
			for( org.alice.ide.event.IDEListener l : this.ideListeners ) {
				l.localeChanging( e );
			}
		}
	}
	protected void fireLocaleChanged( org.alice.ide.event.LocaleEvent e ) {
		synchronized( this.ideListeners ) {
			for( org.alice.ide.event.IDEListener l : this.ideListeners ) {
				l.localeChanged( e );
			}
		}
	}

	@Override
	public void setLocale( java.util.Locale locale ) {
		if( this.ideListeners != null ) {
			java.util.Locale prevLocale = this.getLocale();
			org.alice.ide.event.LocaleEvent e = new org.alice.ide.event.LocaleEvent( this, prevLocale, locale );
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

	protected void fireProjectOpening( org.alice.ide.event.ProjectOpenEvent e ) {
		synchronized( this.ideListeners ) {
			for( org.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.projectOpening( e );
			}
		}
	}

	protected void fireProjectOpened( org.alice.ide.event.ProjectOpenEvent e ) {
		synchronized( this.ideListeners ) {
			for( org.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.projectOpened( e );
			}
		}
	}

	public edu.cmu.cs.dennisc.alice.Project getProject() {
		return this.project;
	}

	public void setProject( edu.cmu.cs.dennisc.alice.Project project ) {
		org.alice.ide.event.ProjectOpenEvent e = new org.alice.ide.event.ProjectOpenEvent( this, this.project, project );
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

	protected void fireMethodFocusChanging( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		synchronized( this.ideListeners ) {
			for( org.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.focusedCodeChanging( e );
			}
		}
	}

	protected void fireMethodFocusChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		synchronized( this.ideListeners ) {
			for( org.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.focusedCodeChanged( e );
			}
		}
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractCode getFocusedCode() {
		return this.focusedCode;
	}

	public void setFocusedCode( edu.cmu.cs.dennisc.alice.ast.AbstractCode nextFocusedCode ) {
		if( nextFocusedCode == this.focusedCode ) {
			//pass
		} else {
			org.alice.ide.event.FocusedCodeChangeEvent e = new org.alice.ide.event.FocusedCodeChangeEvent( this, this.focusedCode, nextFocusedCode );
			fireMethodFocusChanging( e );
			this.focusedCode = nextFocusedCode;
			fireMethodFocusChanged( e );
		}
	}

	protected void fireFieldSelectionChanging( org.alice.ide.event.FieldSelectionEvent e ) {
		synchronized( this.ideListeners ) {
			for( org.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.fieldSelectionChanging( e );
			}
		}
	}

	protected void fireFieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
		synchronized( this.ideListeners ) {
			for( org.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.fieldSelectionChanged( e );
			}
		}
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractField getFieldSelection() {
		return this.fieldSelection;
	}

	public void setFieldSelection( edu.cmu.cs.dennisc.alice.ast.AbstractField fieldSelection ) {
		org.alice.ide.event.FieldSelectionEvent e = new org.alice.ide.event.FieldSelectionEvent( this, this.fieldSelection, fieldSelection );
		fireFieldSelectionChanging( e );
		this.fieldSelection = fieldSelection;
		fireFieldSelectionChanged( e );
	}

	protected void fireTransientSelectionChanging( org.alice.ide.event.TransientSelectionEvent e ) {
		synchronized( this.ideListeners ) {
			for( org.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.transientSelectionChanging( e );
			}
		}
	}

	protected void fireTransientSelectionChanged( org.alice.ide.event.TransientSelectionEvent e ) {
		synchronized( this.ideListeners ) {
			for( org.alice.ide.event.IDEListener l : getIDEListeners() ) {
				l.transientSelectionChanged( e );
			}
		}
	}

	public edu.cmu.cs.dennisc.alice.ast.AbstractTransient getTransientSelection() {
		return this.transientSelection;
	}

	public void setTransientSelection( edu.cmu.cs.dennisc.alice.ast.AbstractTransient transientSelection ) {
		org.alice.ide.event.TransientSelectionEvent e = new org.alice.ide.event.TransientSelectionEvent( this, this.transientSelection, transientSelection );
		fireTransientSelectionChanging( e );
		this.transientSelection = transientSelection;
		fireTransientSelectionChanged( e );
	}

	private java.util.Stack< zoot.Operation > history = new java.util.Stack< zoot.Operation >();
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

	//	protected void addToHistory( Operation operation ) {
	//		this.history.push( operation );
	//		updateTitle();
	//	}
	//	protected void handlePreparedOperation( Operation operation, java.util.EventObject e, java.util.List< java.util.EventObject > preparationUpdates, Operation.PreparationResult preparationResult ) {
	//		edu.cmu.cs.dennisc.print.PrintUtilities.println( "handlePreparedOperation", operation, preparationResult );
	//		if( preparationResult != null ) {
	//			if( preparationResult == Operation.PreparationResult.CANCEL ) {
	//				//pass
	//			} else {
	//				operation.perform();
	//				if( preparationResult == Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY ) {
	//					addToHistory( operation );
	//				}
	//			}
	//		}
	//	}
	//	public void performIfAppropriate( Operation operation, java.util.EventObject e ) {
	//		final java.util.List< java.util.EventObject > preparationUpdates = new java.util.LinkedList< java.util.EventObject >();
	//		Operation.PreparationResult preparationResult = operation.prepare( e, new Operation.PreparationObserver() {
	//			public void update( java.util.EventObject e ) {
	//				preparationUpdates.add( e );
	//			}
	//		} );
	//		handlePreparedOperation( operation, e, preparationUpdates, preparationResult );
	//	}

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
					int i = N - 1;
					while( i >= 0 ) {
						edu.cmu.cs.dennisc.alice.ast.AbstractField field = fields.get( i );
						if( field.getValueType().isArray() ) {
							//pass
						} else {
							IDE.this.setFieldSelection( field );
							break;
						}
						i--;
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

	private static java.awt.Color toColor( String s ) {
		java.awt.Color rv;
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
		return toColor( edu.cmu.cs.dennisc.util.ResourceBundleUtilities.getStringFromSimpleNames( cls, "edu.cmu.cs.dennisc.alice.ast.Colors", javax.swing.JComponent.getDefaultLocale() ) );
	}
	public static java.awt.Color getColorForASTInstance( edu.cmu.cs.dennisc.alice.ast.Node node ) {
		if( node != null ) {
			return getColorForASTClass( node.getClass() );
		} else {
			return java.awt.Color.RED;
		}
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
	public boolean isDragInProgress() {
		return this.isDragInProgress;
	}
	public void setDragInProgress( boolean isDragInProgress ) {
		this.isDragInProgress = isDragInProgress;
		this.currentDropReceptorComponent = null;
	}

	public void toggleExpandContactOfSceneEditor() {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: toggleExpandContactOfSceneEditor" );
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

	private java.util.Map< java.util.UUID, edu.cmu.cs.dennisc.alice.ast.Node > mapUUIDToNode = new java.util.HashMap< java.util.UUID, edu.cmu.cs.dennisc.alice.ast.Node >();

	//	private static <E extends edu.cmu.cs.dennisc.alice.ast.Node> E getAncestor( edu.cmu.cs.dennisc.alice.ast.Node node, Class<E> cls ) {
	//		edu.cmu.cs.dennisc.alice.ast.Node ancestor = node.getParent();
	//		while( ancestor != null ) {
	//			if( cls.isAssignableFrom( ancestor.getClass() ) ) {
	//				break;
	//			} else {
	//				ancestor = ancestor.getParent();
	//			}
	//		}
	//		return (E)ancestor;
	//	}
	//	
	//	protected void ensureNodeVisible( edu.cmu.cs.dennisc.alice.ast.Node node ) {
	//		edu.cmu.cs.dennisc.alice.ast.AbstractCode nextFocusedCode = getAncestor( node, edu.cmu.cs.dennisc.alice.ast.AbstractCode.class );
	//		if( nextFocusedCode != null ) {
	//			this.setFocusedCode( nextFocusedCode );
	//		}
	//	}
	//	private edu.cmu.cs.dennisc.alice.ast.Node getNodeForUUID( java.util.UUID uuid ) {
	//		edu.cmu.cs.dennisc.alice.ast.Node rv = mapUUIDToNode.get( uuid );
	//		if( rv != null ) {
	//			//pass
	//		} else {
	//			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = this.getProgramType();
	//			type.crawl( new edu.cmu.cs.dennisc.pattern.Crawler() {
	//				public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
	//					if( crawlable instanceof edu.cmu.cs.dennisc.alice.ast.Node ) {
	//						edu.cmu.cs.dennisc.alice.ast.Node node = (edu.cmu.cs.dennisc.alice.ast.Node)crawlable;
	//						mapUUIDToNode.put( node.getUUID(), node );
	//					}
	//				}
	//			}, true );
	//			rv = mapUUIDToNode.get( uuid );
	//		}
	//		return rv;
	//	}
	//	public java.awt.Component getComponentForNode( edu.cmu.cs.dennisc.alice.ast.Node node ) {
	//		if( node instanceof edu.cmu.cs.dennisc.alice.ast.Statement ) {
	//			final edu.cmu.cs.dennisc.alice.ast.Statement statement = (edu.cmu.cs.dennisc.alice.ast.Statement)node;
	//			ensureNodeVisible( node );
	//			alice.ide.codeeditor.AbstractStatementPane rv = alice.ide.codeeditor.AbstractStatementPane.lookup( statement );
	//			javax.swing.SwingUtilities.invokeLater( new Runnable() {
	//				public void run() {
	//					alice.ide.codeeditor.AbstractStatementPane pane = alice.ide.codeeditor.AbstractStatementPane.lookup( statement );
	//					if( pane != null ) {
	//						pane.scrollRectToVisible( javax.swing.SwingUtilities.getLocalBounds( pane ) );
	//					}
	//				}
	//			} );
	//			return rv;
	//		} else {
	//			return null;
	//		}
	//	}
	//	public java.awt.Component getComponentForNode( java.util.UUID uuid ) {
	//		return getComponentForNode( getNodeForUUID( uuid ) );
	//	}

	public static java.awt.Color getColorFor( String key ) {
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( "org.alice.ide.Colors", javax.swing.JComponent.getDefaultLocale() );
		String s = resourceBundle.getString( key );
		return toColor( s );
	}
	public static java.awt.Color getProcedureColor() {
		return new java.awt.Color( 0xedc484 );
	}
	public static java.awt.Color getFunctionColor() {
		return new java.awt.Color( 0xb4ccaf );
	}
	public static java.awt.Color getConstructorColor() {
		return getFunctionColor();
	}
	public static java.awt.Color getCodeDeclaredInAliceColor( edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice codeDeclaredInAlice ) {
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
	public static java.awt.Color getFieldColor() {
		return new java.awt.Color( 0x85abc9 );
	}

	public static void main( String[] args ) {
		edu.cmu.cs.dennisc.alice.reflect.ClassInfoManager.setDirectory( new java.io.File( "/program files/alice/3.beta.0027/application/classinfos" ) );
		IDE ide = new IDE() {

			@Override
			protected org.alice.ide.codeeditor.CodeEditor getCodeEditorInFocus() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Node createCopy( Node original ) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void createProjectFromBootstrap() {
				// TODO Auto-generated method stub

			}

			@Override
			protected void generateCodeForSceneSetUp() {
				// TODO Auto-generated method stub

			}

			@Override
			public File getApplicationRootDirectory() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void handleWindowOpened( WindowEvent e ) {
				// TODO Auto-generated method stub

			}

			@Override
			protected void preserveProjectProperties() {
				// TODO Auto-generated method stub

			}

			@Override
			public void promptUserForStatement( MouseEvent e, TaskObserver< Statement > taskObserver ) {
				// TODO Auto-generated method stub

			}

			@Override
			public void unsetPreviousExpression() {
				// TODO Auto-generated method stub

			}

		};
		ide.loadProjectFrom( new java.io.File( edu.cmu.cs.dennisc.alice.io.FileUtilities.getMyProjectsDirectory(), "a.a3p" ) );
		ide.setSize( 1000, 1000 );
		ide.setVisible( true );
	}
}
