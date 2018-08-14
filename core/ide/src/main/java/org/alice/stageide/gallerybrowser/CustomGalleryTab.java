package org.alice.stageide.gallerybrowser;

import org.alice.stageide.modelresource.ResourceNodeTreeState;
import org.alice.stageide.modelresource.UserDefinedResourceNodeTreeState;

import java.util.UUID;

public class CustomGalleryTab extends TreeOwningGalleryTab {
	public CustomGalleryTab() {
		super( UUID.fromString( "e6e90493-25fb-4e82-ac5a-b3673f2c7548" ) );
	}

	@Override
	public ResourceNodeTreeState getResourceNodeTreeSelectionState() {
		return UserDefinedResourceNodeTreeState.getInstance();
	}
}
