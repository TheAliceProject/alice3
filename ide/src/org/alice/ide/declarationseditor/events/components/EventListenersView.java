package org.alice.ide.declarationseditor.events.components;

import org.alice.ide.controlflow.ControlFlowComposite;
import org.alice.ide.declarationseditor.events.AddEventListenerCascade;
import org.lgna.croquet.components.LineAxisPanel;
import org.lgna.croquet.components.PopupButton;
import org.lgna.croquet.components.ScrollPane;

public class EventListenersView extends org.alice.ide.declarationseditor.code.components.AbstractCodeDeclarationView {
	private final EventsContentPanel eventsPanel;
	public EventListenersView( org.alice.ide.declarationseditor.CodeComposite composite ) {
		super( composite );
		this.eventsPanel = new EventsContentPanel(composite.getDeclaration());
		
		ScrollPane scrollPane = new ScrollPane( eventsPanel );
		this.addComponent( scrollPane, Constraint.CENTER );
		PopupButton button = AddEventListenerCascade.getInstance().getRoot().getPopupPrepModel().createPopupButton();
		this.addComponent( new LineAxisPanel( button ), Constraint.PAGE_START );
		
		this.addComponent( ControlFlowComposite.getInstance( composite.getDeclaration() ).getView(), Constraint.PAGE_END );
	}
	@Override
	public org.alice.ide.codedrop.CodeDropReceptor getCodeDropReceptor() {
		return this.eventsPanel;
	}
}
