package org.lgna.croquet;

public final class CascadePopupCompletionModel<B> extends CompletionModel {
	private final CascadePopupPrepModel<B> popupPrepModel;
	public CascadePopupCompletionModel( Group group, CascadePopupPrepModel<B> popupPrepModel ) {
		super( group, java.util.UUID.fromString( "56116a5f-a081-4ce8-9626-9c515c6c5887" ) );
		this.popupPrepModel = popupPrepModel;
	}
	public CascadePopupPrepModel<B> getPopupPrepModel() {
		return this.popupPrepModel;
	}
	@Override
	protected void localize() {
	}
	@Override
	public org.lgna.croquet.steps.Step<?> fire(org.lgna.croquet.Trigger trigger) {
		throw new RuntimeException();
	}
	@Override
	public boolean isAlreadyInState( org.lgna.croquet.Edit< ? > edit ) {
		return false;
	}
}