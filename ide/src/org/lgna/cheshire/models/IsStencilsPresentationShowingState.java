package org.lgna.cheshire.models;

public class IsStencilsPresentationShowingState extends org.lgna.croquet.BooleanState {

	private final org.lgna.cheshire.stencil.StencilsPresentation stencilsPresentation;

	public IsStencilsPresentationShowingState( org.lgna.cheshire.stencil.StencilsPresentation stencilsPresentation, boolean isShowing) {
		super( org.lgna.cheshire.stencil.StencilsPresentation.PRESENTATION_GROUP, java.util.UUID.fromString( "1303fdcf-6ba4-4933-9754-5b7933f8c01f" ), isShowing);
		this.stencilsPresentation = stencilsPresentation;
		this.addAndInvokeValueListener( new org.lgna.croquet.State.ValueListener<Boolean>() {
			public void changing(org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting) {
			}
			public void changed(org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting) {
				IsStencilsPresentationShowingState.this.stencilsPresentation.setVisible( state.getValue() );
			}
		} );
	}

	public void showStencilsPresentation() {
		this.setValue(true);
	}

	public void hideStencilsPresentation() {
		this.setValue(false);
	}
}
