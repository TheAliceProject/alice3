package org.alice.ide.declarationseditor.events.components;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;

import org.alice.ide.codeeditor.CodeEditor;
import org.alice.ide.controlflow.ControlFlowComposite;
import org.alice.ide.controlflow.components.ControlFlowPanel;
import org.alice.ide.declarationseditor.DeclarationComposite;
import org.alice.ide.declarationseditor.components.DeclarationView;
import org.alice.ide.declarationseditor.events.AddEventListenerCascade;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.LineAxisPanel;
import org.lgna.croquet.components.PageAxisPanel;
import org.lgna.croquet.components.PopupButton;
import org.lgna.project.ast.AbstractCode;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NodeProperty;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.StatementListProperty;
import org.lgna.project.ast.UserCode;

import edu.cmu.cs.dennisc.property.event.ListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ListPropertyListener;
import edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter;

public class EventListenersView extends DeclarationView {

	private final PageAxisPanel thisShouldHaveBeenItsOwnClassAndMattWillDoThatShortly = new PageAxisPanel();
	private final ListPropertyListener< Statement > statementsListener = new SimplifiedListPropertyAdapter< Statement >() {
		@Override
		protected void changing( ListPropertyEvent< Statement > e ) {
		}
		@Override
		protected void changed( ListPropertyEvent< Statement > e ) {
			EventListenersView.this.refreshLater();
		}
	};
	//	private final CodeEditor codeEditor;
	public EventListenersView( DeclarationComposite composite ) {
		super( composite );
		PopupButton button = AddEventListenerCascade.getInstance().getRoot().getPopupPrepModel().createPopupButton();

		this.addComponent( this.thisShouldHaveBeenItsOwnClassAndMattWillDoThatShortly, Constraint.CENTER );
		this.addComponent( new LineAxisPanel( button ), Constraint.PAGE_START );
		
		this.addComponent( ControlFlowComposite.getInstance( (AbstractCode)composite.getDeclaration() ).getView(), Constraint.PAGE_END );
	}

	private NodeProperty< ? extends BlockStatement > getBodyProperty() {
		UserCode code = (UserCode)((DeclarationComposite)this.getComposite()).getDeclaration();
		return code.getBodyProperty();
	}

	@Override
	protected void internalRefresh() {
		super.internalRefresh();
		this.thisShouldHaveBeenItsOwnClassAndMattWillDoThatShortly.forgetAndRemoveAllComponents();
		for( Statement statement : getStatements() ) {
			if( statement instanceof ExpressionStatement ) {
				ExpressionStatement expressionStatement = (ExpressionStatement)statement;
				Expression expression = expressionStatement.expression.getValue();
				if( expression instanceof MethodInvocation ) {
					MethodInvocation methodInvocation = (MethodInvocation)expression;
					this.thisShouldHaveBeenItsOwnClassAndMattWillDoThatShortly.addComponent( new Label( methodInvocation.method.getValue().getName() ) );
				}
			}
		}
	}

	private StatementListProperty getStatements() {
		return this.getBodyProperty().getValue().statements;
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.getStatements().addListPropertyListener( this.statementsListener );
		this.refreshLater();
	}
	@Override
	protected void handleUndisplayable() {
		this.getStatements().removeListPropertyListener( this.statementsListener );
		super.handleUndisplayable();
	}
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
