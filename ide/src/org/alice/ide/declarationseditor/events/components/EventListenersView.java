package org.alice.ide.declarationseditor.events.components;

import org.alice.ide.controlflow.ControlFlowComposite;
import org.alice.ide.declarationseditor.events.AddEventListenerCascade;
import org.lgna.croquet.components.Component;
import org.lgna.croquet.components.LineAxisPanel;
import org.lgna.croquet.components.PopupButton;
import org.lgna.croquet.components.ScrollPane;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserCode;

public class EventListenersView extends org.alice.ide.declarationseditor.code.components.AbstractCodeDeclarationView {
	private final EventsContentPanel eventsPanel;
	private ScrollPane scroll; 
	private final edu.cmu.cs.dennisc.property.event.ListPropertyListener< Statement > statementsListener = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< Statement >() {
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< Statement > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< Statement > e ) {
			EventListenersView.this.revalidateAndRepaint();
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< Statement > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< Statement > e ) {
			EventListenersView.this.revalidateAndRepaint();
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< Statement > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< Statement > e ) {
			EventListenersView.this.revalidateAndRepaint();
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< Statement > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< Statement > e ) {
			EventListenersView.this.revalidateAndRepaint();
		}
		
	};

	public EventListenersView( org.alice.ide.declarationseditor.CodeComposite composite ) {
		super( composite );
		this.eventsPanel = new EventsContentPanel(composite.getDeclaration());
//		BorderPanel panel = new BorderPanel();
		PopupButton button = AddEventListenerCascade.getInstance().getRoot().getPopupPrepModel().createPopupButton();

		StickyBottomScrollPanel panel = new StickyBottomScrollPanel();
//		panel.addComponent( new Label("Initialize Events: "), Constraint.PAGE_START );
		scroll = new ScrollPane( eventsPanel );
		panel.addTop( scroll );
		panel.addBottom( new LineAxisPanel( button ));
		this.addComponent( panel, Constraint.CENTER );
		this.setBackgroundColor( this.eventsPanel.getBackgroundColor() );
//		this.addComponent( this.eventsPanel, Constraint.CENTER );
//		this.addComponent( new LineAxisPanel( button ), Constraint.PAGE_END );
		this.addComponent( ControlFlowComposite.getInstance( composite.getDeclaration() ).getView(), Constraint.PAGE_END );
	}
	@Override
	public org.alice.ide.codedrop.CodeDropReceptor getCodeDropReceptor() {
		return this.eventsPanel;
	}
	@Override
	protected void handleAddedTo(Component<?> parent) {
		super.handleAddedTo(parent);
		org.alice.ide.declarationseditor.CodeComposite codeComposite = (org.alice.ide.declarationseditor.CodeComposite)this.getComposite();
		UserCode userCode = (UserCode)codeComposite.getDeclaration();
		userCode.getBodyProperty().getValue().statements.addListPropertyListener( this.statementsListener );
	}
	@Override
	protected void handleRemovedFrom(Component<?> parent) {
		org.alice.ide.declarationseditor.CodeComposite codeComposite = (org.alice.ide.declarationseditor.CodeComposite)this.getComposite();
		UserCode userCode = (UserCode)codeComposite.getDeclaration();
		userCode.getBodyProperty().getValue().statements.removeListPropertyListener( this.statementsListener );
		super.handleRemovedFrom(parent);
	}
}
