package org.alice.ide.declarationseditor.events.components;

import org.alice.ide.controlflow.ControlFlowComposite;
import org.alice.ide.declarationseditor.DeclarationComposite;
import org.alice.ide.declarationseditor.events.AddEventListenerCascade;
import org.lgna.croquet.components.LineAxisPanel;
import org.lgna.croquet.components.PopupButton;
import org.lgna.croquet.components.ScrollPane;
import org.lgna.project.ast.AbstractCode;

public class EventListenersView extends org.alice.ide.declarationseditor.code.components.CodeDeclarationView {
//	private final org.alice.ide.codedrop.CodeDropReceptor codeDropReceptor = new org.alice.ide.codedrop.CodeDropReceptor() {
//		@Override
//		public org.lgna.project.ast.AbstractCode getCode() {
//			return ((org.alice.ide.declarationseditor.CodeComposite)getComposite()).getDeclaration();
//		}
//	};
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

	@Override
	public org.alice.ide.codeeditor.CodeEditor getCodeDropReceptor() {
		return null;
	}
}
