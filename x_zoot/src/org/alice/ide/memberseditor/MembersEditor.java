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
package org.alice.ide.memberseditor;

abstract class MembersTab extends swing.PageAxisPane {
	protected static final int INDENT = 16;
	private static final int TYPE_PAD = 8;

	public MembersTab() {
		this.setBackground( org.alice.ide.IDE.getColorFor( this.getKey() ) );
		this.setOpaque( true );
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	protected abstract String getKey();
	public final String getTitle() {
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( "org.alice.ide.memberseditor.TabTitles", javax.swing.JComponent.getDefaultLocale() );
		return resourceBundle.getString( this.getKey() );
	}

	private java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > types = new java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.AbstractType >();

	private Iterable< edu.cmu.cs.dennisc.alice.ast.AbstractType > getTypes() {
		return this.types;
	}

	public void handleFieldSelection( org.alice.ide.event.FieldSelectionEvent e ) {
		this.types.clear();
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = e.getNextValue();
		if( field != null ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractType type = field.getValueType();
			while( type != null ) {
				this.types.add( type );
				if( type.isFollowToSuperClassDesired() ) {
					type = type.getSuperType();
				} else {
					break;
				}
			}
		}
		this.refresh();
	}

	private static boolean isInclusionDesired( edu.cmu.cs.dennisc.alice.ast.AbstractMember member ) {
		if( member instanceof edu.cmu.cs.dennisc.alice.ast.AbstractMethod ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)member;
			if( method.isStatic() ) {
				return false;
			}
		} else if( member instanceof edu.cmu.cs.dennisc.alice.ast.AbstractField ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractField field = (edu.cmu.cs.dennisc.alice.ast.AbstractField)member;
			if( field.isStatic() ) {
				return false;
			}
		}
		if( member.isPublicAccess() || member.isDeclaredInAlice() ) {
			edu.cmu.cs.dennisc.alice.annotations.Visibility visibility = member.getVisibility();
			return visibility == null || visibility.equals( edu.cmu.cs.dennisc.alice.annotations.Visibility.PRIME_TIME );
		} else {
			return false;
		}
	}
	protected abstract java.awt.Component[] createTemplates( edu.cmu.cs.dennisc.alice.ast.AbstractMember member );
	
	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractType, zoot.ZButton > mapTypeToCreateButton = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractType, zoot.ZButton >();
	protected abstract zoot.ZButton createCreateMemberButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type );
	private final zoot.ZButton getCreateMemberButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		zoot.ZButton rv = this.mapTypeToCreateButton.get( type );
		if( rv != null ) {
			//pass
		} else {
			rv = this.createCreateMemberButton( type );
			this.mapTypeToCreateButton.put( type, rv );
		}
		return rv;
	}

	protected void refresh() {
		this.removeAll();
		//swing.PageAxisPane typePane = new swing.PageAxisPane();
		for( edu.cmu.cs.dennisc.alice.ast.AbstractType type : this.getTypes() ) {
			javax.swing.JComponent component = getIDE().getFactory().create( this, type );
			this.add( component );
			for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : type.getDeclaredFields() ) {
				if( isInclusionDesired( field ) ) {
					java.awt.Component[] templates = this.createTemplates( field );
					if( templates != null ) {
						for( java.awt.Component template : templates ) {
							this.add( javax.swing.Box.createVerticalStrut( 2 ) );
							this.add( template );
						}
					}
				}
			}
			for( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method : type.getDeclaredMethods() ) {
				if( isInclusionDesired( method ) ) {
					method = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)method.getShortestInChain();
					java.awt.Component[] templates = this.createTemplates( method );
					if( templates != null ) {
						for( java.awt.Component template : templates ) {
							this.add( javax.swing.Box.createVerticalStrut( 2 ) );
							this.add( template );
						}
					}
				}
			}
			if( type.isDeclaredInAlice() ) {
				this.add( new swing.LineAxisPane(
					javax.swing.Box.createHorizontalStrut( INDENT ),
					this.getCreateMemberButton( (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)type )
				) );
			}
			this.add( javax.swing.Box.createVerticalStrut( TYPE_PAD ) );
		}
		this.revalidate();
		this.repaint();
	}
}

abstract class DragSourcePane extends javax.swing.JPanel {
	
}

abstract class MethodInvocationTemplate<E> extends DragSourcePane {
	private edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation;
	public MethodInvocationTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		this.methodInvocation = org.alice.ide.ast.NodeUtilities.createIncompleteMethodInvocation( null, method );
		this.setOpaque( false );
		//this.setLayout( new edu.cmu.cs.dennisc.awt.ExpandAllToBoundsLayoutManager() );
		this.setLayout( new java.awt.GridLayout( 1, 1 ) );
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	public void setExpression( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
//		if( expression != null ) {
//			if( expression.equals( this.methodInvocation.expression.getValue() ) ) {
//				//pass
//			} else {
				this.removeAll();
				this.methodInvocation.expression.setValue( expression );
				this.add( org.alice.ide.IDE.getSingleton().getTemplatesFactory().createComponent( this.methodInvocation ) );
//			}
//		} else {
//			this.removeAll();
//		}
	}
}

class ProcedureBorder implements javax.swing.border.Border {
	private static final int KNURL_WIDTH = 8;
	private static final int INSET = 4;
	private static final int TOP = INSET;
	private static final int LEFT = INSET + KNURL_WIDTH + 2;
	private static final int BOTTOM = INSET;
	private static final int RIGHT = INSET;
	private java.awt.Insets insets = new java.awt.Insets( TOP, LEFT, BOTTOM, RIGHT );
	public java.awt.Insets getBorderInsets( java.awt.Component c ) {
		return this.insets;
	}
	public boolean isBorderOpaque() {
		return false;
	}
	public void paintBorder( java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g.setColor( c.getBackground() );
		g.fillRoundRect( x, y, width-1, height-1, 16, 16 );
		g.setColor( java.awt.Color.BLACK );
		edu.cmu.cs.dennisc.awt.KnurlUtilities.paintKnurl5( g2, x+2, y+2, KNURL_WIDTH, height-2 );
		g.setColor( c.getForeground() );
		g.drawRoundRect( x, y, width-1, height-1, 16, 16 );
	}
}

class ProcedureInvocationTemplate extends MethodInvocationTemplate< edu.cmu.cs.dennisc.alice.ast.ExpressionStatement > {
	private static ProcedureBorder singletonBorder = new ProcedureBorder(); 
	public ProcedureInvocationTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		super( method );
		setBackground( getIDE().getColorForASTClass( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement.class ) );
		setForeground( java.awt.Color.GRAY );
		setBorder( ProcedureInvocationTemplate.singletonBorder );
	}
}

class FunctionBorder implements javax.swing.border.Border {
	private static final int KNURL_WIDTH = 8;
	private static final int INSET = 4;
	private static final int TOP = INSET;
	private static final int LEFT = INSET + KNURL_WIDTH + 2;
	private static final int BOTTOM = INSET;
	private static final int RIGHT = INSET;
	private java.awt.Insets insets = new java.awt.Insets( TOP, LEFT, BOTTOM, RIGHT );
	public java.awt.Insets getBorderInsets( java.awt.Component c ) {
		return this.insets;
	}
	public boolean isBorderOpaque() {
		return false;
	}
	public void paintBorder( java.awt.Component c, java.awt.Graphics g, int x, int y, int width, int height ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g.setColor( c.getBackground() );
		g.fillRoundRect( x, y, width-1, height-1, 16, 16 );
		g.setColor( java.awt.Color.BLACK );
		edu.cmu.cs.dennisc.awt.KnurlUtilities.paintKnurl5( g2, x+2, y+2, KNURL_WIDTH, height-2 );
		g.setColor( c.getForeground() );
		g.drawRoundRect( x, y, width-1, height-1, 16, 16 );
	}
}

class FunctionInvocationTemplate extends MethodInvocationTemplate< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > {
	private static FunctionBorder singletonBorder = new FunctionBorder(); 
	public FunctionInvocationTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		super( method );
		setBackground( getIDE().getColorForASTClass( edu.cmu.cs.dennisc.alice.ast.MethodInvocation.class ) );
		setForeground( java.awt.Color.GRAY );
		setBorder( FunctionInvocationTemplate.singletonBorder );
	}
}

abstract class MethodsTab extends MembersTab {
	protected abstract javax.swing.JComponent createProcedureTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method );
	protected abstract javax.swing.JComponent createFunctionTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method );

	@Override
	protected java.awt.Component[] createTemplates( edu.cmu.cs.dennisc.alice.ast.AbstractMember member ) {
		javax.swing.JComponent component;
		if( member instanceof edu.cmu.cs.dennisc.alice.ast.AbstractMethod ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)member;
			if( method.getNextShorterInChain() != null ) {
				component = null;
			} else {
				if( method.isProcedure() ) {
					component = createProcedureTemplate( method );
				} else if( method.isFunction() ) {
					component = createFunctionTemplate( method );
				} else {
					component = null;
				}
			}
		} else {
			component = null;
		}
		java.awt.Component[] rv;
		if( component != null ) {
			swing.LineAxisPane line = new swing.LineAxisPane();
			line.add( javax.swing.Box.createHorizontalStrut( INDENT ) );
			//if( member.isDeclaredInAlice() ) {
			if( member instanceof edu.cmu.cs.dennisc.alice.ast.AbstractCode ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractCode code = (edu.cmu.cs.dennisc.alice.ast.AbstractCode)member;
				if( code.isDeclaredInAlice() ) {
					zoot.ZButton editButton = new zoot.ZButton( new org.alice.ide.operations.ast.FocusCodeOperation( code ) );
					line.add( editButton );
				}
			}
			line.add( component );
			rv = new java.awt.Component[] { line };
		} else {
			rv = null;
		}
		return rv;
	}
}

class ProceduresTab extends MethodsTab {
	@Override
	protected String getKey() {
		return "procedure";
	}
	@Override
	protected zoot.ZButton createCreateMemberButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		return new zoot.ZButton( new org.alice.ide.operations.ast.CreateProcedureOperation( type ) );
	}
	@Override
	protected javax.swing.JComponent createFunctionTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		return null;
	}
	@Override
	protected javax.swing.JComponent createProcedureTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		MethodInvocationTemplate rv = new ProcedureInvocationTemplate( method );
		rv.setExpression( getIDE().createInstanceExpression() );
		return rv;
	}
}

class FunctionsTab extends MethodsTab {
	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractType, zoot.ZButton > mapTypeToEditConstructorButton = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractType, zoot.ZButton >();
	private zoot.ZButton createEditConstructorButton( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		throw new RuntimeException();
	}
	private final zoot.ZButton getEditConstructorButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		zoot.ZButton rv = this.mapTypeToEditConstructorButton.get( type );
		if( rv != null ) {
			//pass
		} else {
			rv = this.createEditConstructorButton( type );
			this.mapTypeToEditConstructorButton.put( type, rv );
		}
		return rv;
	}

	@Override
	protected zoot.ZButton createCreateMemberButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		return new zoot.ZButton( new org.alice.ide.operations.ast.CreateFunctionOperation( type ) );
	}

	@Override
	protected String getKey() {
		return "function";
	}
	@Override
	protected javax.swing.JComponent createFunctionTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		MethodInvocationTemplate rv = new FunctionInvocationTemplate( method );
		rv.setExpression( getIDE().createInstanceExpression() );
		return rv;
//		return new zoot.ZLabel( method.toString() );
	}
	@Override
	protected javax.swing.JComponent createProcedureTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		return null;
	}
}

class FieldsTab extends MembersTab {
	@Override
	protected String getKey() {
		return "field";
	}
	@Override
	protected zoot.ZButton createCreateMemberButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		return new zoot.ZButton( new org.alice.ide.operations.ast.CreateFieldOperation( type ) );
	}
	@Override
	protected java.awt.Component[] createTemplates( edu.cmu.cs.dennisc.alice.ast.AbstractMember member ) {
		return null;
	}
}

class TabbedPane extends zoot.ZTabbedPane {
	private MembersTab[] membersTabs = { new ProceduresTab(), new FunctionsTab(), new FieldsTab() };
	public TabbedPane() {
		super( null );
		for( MembersTab membersTab : membersTabs ) {
			this.addMembersTab( membersTab );
		}
	}
	private void addMembersTab( MembersTab membersTab ) {
		zoot.ZScrollPane scrollPane = new zoot.ZScrollPane( membersTab );
		scrollPane.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		scrollPane.setBackground( membersTab.getBackground() );
		scrollPane.getVerticalScrollBar().setUnitIncrement( 12 );
		this.addTab( membersTab.getTitle(), scrollPane );
	}
	public void handleFieldSelection( org.alice.ide.event.FieldSelectionEvent e ) {
		for( MembersTab membersTab : membersTabs ) {
			membersTab.handleFieldSelection( e );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class MembersEditor extends org.alice.ide.Editor< edu.cmu.cs.dennisc.alice.ast.AbstractType > implements org.alice.ide.event.IDEListener {
	private TabbedPane tabbedPane = new TabbedPane();

	public MembersEditor() {
		this.setLayout( new edu.cmu.cs.dennisc.awt.ExpandAllToBoundsLayoutManager() );
		this.add( this.tabbedPane );
	}
//	@Override
//	public java.awt.Dimension getPreferredSize() {
//		return new java.awt.Dimension( 100, 100 );
//	}

	public void fieldSelectionChanging( org.alice.ide.event.FieldSelectionEvent e ) {
	}
	public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
		this.tabbedPane.handleFieldSelection( e );
	}

	public void localeChanging( org.alice.ide.event.LocaleEvent e ) {
	}
	public void localeChanged( org.alice.ide.event.LocaleEvent e ) {
	}


	public void focusedCodeChanging( org.alice.ide.event.FocusedCodeChangeEvent e ) {
	}
	public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
	}


	public void projectOpening( org.alice.ide.event.ProjectOpenEvent e ) {
	}
	public void projectOpened( org.alice.ide.event.ProjectOpenEvent e ) {
	}


	public void transientSelectionChanging( org.alice.ide.event.TransientSelectionEvent e ) {
	}
	public void transientSelectionChanged( org.alice.ide.event.TransientSelectionEvent e ) {
	}
}
