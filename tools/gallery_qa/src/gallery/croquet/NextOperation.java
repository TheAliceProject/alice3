package gallery.croquet;

import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.history.ActionOperationStep;

import edu.cmu.cs.dennisc.java.util.logging.Logger;

public class NextOperation extends ActionOperation {

	public NextOperation() {
		super(Application.UI_STATE_GROUP, java.util.UUID.fromString( "476ad1aa-df7e-4cc1-8d7c-751fe1d557db" ));
	}

	@Override
	protected void perform(ActionOperationStep step) {
		//GalleryResourceTreeSelectionState.getInstance().getSelectedNode();
		Logger.todo("NEXT");
		step.finish();
	}

}
