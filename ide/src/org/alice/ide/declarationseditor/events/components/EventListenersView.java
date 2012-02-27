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
			EventListenersView.this.handleStatementsChanged( true );
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< Statement > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< Statement > e ) {
			EventListenersView.this.handleStatementsChanged( false );
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< Statement > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< Statement > e ) {
			EventListenersView.this.handleStatementsChanged( false );
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< Statement > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< Statement > e ) {
			EventListenersView.this.handleStatementsChanged( false );
		}
		
	};

	public EventListenersView( org.alice.ide.declarationseditor.CodeComposite composite ) {
		super( composite );
		this.eventsPanel = new EventsContentPanel(composite.getDeclaration());
//		BorderPanel panel = new BorderPanel();
		PopupButton button = AddEventListenerCascade.getInstance().getRoot().getPopupPrepModel().createPopupButton();

		scroll = new org.lgna.croquet.components.ScrollPane( eventsPanel );
		scroll.setBorder( null );
		scroll.setBothScrollBarIncrements( 12, 24 );
		LineAxisPanel bottom = new LineAxisPanel( button );
		final StickyBottomPanel panel = new StickyBottomPanel( scroll, bottom );
		this.addComponent( panel, Constraint.CENTER );
		scroll.getAwtComponent().getViewport().addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				Object src = e.getSource();
				if( src instanceof java.awt.Component ) {
					java.awt.Component awtComponent = (java.awt.Component)src;
					if( awtComponent.isValid() ) {
						//pass
					} else {
						panel.revalidateAndRepaint();
					}
				}
			}
		} );
		
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 8, 4, 8 ) );
		java.awt.Color color = this.eventsPanel.getBackgroundColor();
		this.setBackgroundColor( color );
		scroll.setBackgroundColor( color );
		this.addComponent( ControlFlowComposite.getInstance( composite.getDeclaration() ).getView(), Constraint.PAGE_END );
	}
	
	private void handleStatementsChanged( boolean isScrollDesired ) {
		this.revalidateAndRepaint();
		if( isScrollDesired ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				public void run() {
					javax.swing.JScrollBar verticalScrollBar = EventListenersView.this.scroll.getAwtComponent().getVerticalScrollBar();
					verticalScrollBar.setValue( verticalScrollBar.getMaximum() );
				}
			} );
		}
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
