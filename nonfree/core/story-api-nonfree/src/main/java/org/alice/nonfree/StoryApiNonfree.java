package org.alice.nonfree;

import org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resourceutilities.NebulousStorytellingResources;

public class StoryApiNonfree extends NebulousStoryApi {

	@Override
	public boolean isNonFreeEnabled() {
		return true;
	}

	@Override
	public String getNebulousResourceInstallPath() {
		return NebulousStorytellingResources.NEBULOUS_RESOURCE_INSTALL_PATH;
	}

	@Override
	public JointImplementationAndVisualDataFactory getFactory( JointedModelResource resource ) {
		return org.lgna.story.implementation.sims2.JointImplementationAndVisualDataFactory.getInstance( resource );
	}

	@Override
	public void setMipMappingDesiredOnNebulousTexture( edu.cmu.cs.dennisc.texture.Texture texture ) {
		if( texture instanceof edu.cmu.cs.dennisc.nebulous.NebulousTexture ) {
			edu.cmu.cs.dennisc.nebulous.NebulousTexture nebulousTexture = (edu.cmu.cs.dennisc.nebulous.NebulousTexture)texture;
			nebulousTexture.setMipMappingDesired( true );
		}
	}
}
