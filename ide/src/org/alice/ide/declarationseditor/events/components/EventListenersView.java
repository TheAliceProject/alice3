package org.alice.ide.declarationseditor.events.components;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;

import org.alice.ide.codeeditor.ParametersPane;
import org.alice.ide.controlflow.ControlFlowComposite;
import org.alice.ide.declarationseditor.DeclarationComposite;
import org.alice.ide.declarationseditor.components.DeclarationView;
import org.alice.ide.declarationseditor.events.AddEventListenerCascade;
import org.lgna.croquet.components.LineAxisPanel;
import org.lgna.croquet.components.PopupButton;
import org.lgna.croquet.components.ScrollPane;
import org.lgna.project.ast.AbstractCode;

public class EventListenersView extends DeclarationView {

	private final EventsContentPanel eventsPanel;
//	private final PageAxisPanel contentPanel = new PageAxisPanel();
	//	private final CodeEditor codeEditor;
	public EventListenersView( DeclarationComposite composite ) {
		super( composite );
		PopupButton button = AddEventListenerCascade.getInstance().getRoot().getPopupPrepModel().createPopupButton();
		eventsPanel = new EventsContentPanel(composite);
		
//		BorderPanel borderPanel = new BorderPanel();
//		eventsPanel.addComponent( this.contentPanel, Constraint.PAGE_START );
		ScrollPane scrollPane = new ScrollPane( eventsPanel );
		this.addComponent( scrollPane, Constraint.CENTER );
		this.addComponent( new LineAxisPanel( button ), Constraint.PAGE_START );
		
		this.addComponent( ControlFlowComposite.getInstance( (AbstractCode)composite.getDeclaration() ).getView(), Constraint.PAGE_END );
	}

//	@Override
//	protected void internalRefresh() {
//		super.internalRefresh();
//		eventsPanel.refresh();
////		this.eventsPanel.forgetAndRemoveAllComponents();
//////		this.contentPanel.forgetAndRemoveAllComponents();
//////		contentPanel.setBorder( BorderFactory.createBevelBorder( javax.swing.border.BevelBorder.LOWERED ) );
////		for( Statement statement : getStatements() ) {
////			if( statement instanceof ExpressionStatement ) {
////				ExpressionStatement expressionStatement = (ExpressionStatement)statement;
////				Expression expression = expressionStatement.expression.getValue();
////				if( expression instanceof MethodInvocation ) {
////					MethodInvocation methodInvocation = (MethodInvocation)expression;
//////					this.contentPanel.addComponent( new EventListenerComponent( methodInvocation ) );
////					this.eventsPanel.addContent( new EventListenerComponent( methodInvocation ) );
////				}
////			}
////		}
//////		this.contentPanel.addComponent( BoxUtilities.createVerticalGlue() );
////		this.eventsPanel.addContent( BoxUtilities.createVerticalGlue() );
//	}

	public int print( Graphics graphics, PageFormat pageFormat, int pageIndex ) throws PrinterException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public org.alice.ide.codeeditor.CodeEditor getCodeDropReceptor() {
		return null;
	}

	@Override
	public boolean isPrintSupported() {
		// TODO Auto-generated method stub
		return false;
	}

}
