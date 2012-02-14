package gallery.croquet;

public class NextOperation extends org.lgna.croquet.ActionOperation {

	public NextOperation() {
		super(org.lgna.croquet.Application.UI_STATE_GROUP, java.util.UUID.fromString( "476ad1aa-df7e-4cc1-8d7c-751fe1d557db" ));
	}

	@Override
	protected void perform(org.lgna.croquet.history.CompletionStep<?> step) {
		//GalleryResourceTreeSelectionState.getInstance().getSelectedNode();
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo("NEXT");
		step.finish();
	}

}
