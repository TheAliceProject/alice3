package gallery.croquet;

import org.lgna.croquet.Application;
import org.lgna.croquet.BooleanState;

public class IsVisualizationShowingState extends BooleanState {
	private static class SingletonHolder {
		private static IsVisualizationShowingState instance = new IsVisualizationShowingState();
	}
	public static IsVisualizationShowingState getInstance() {
		return SingletonHolder.instance;
	}
	private IsVisualizationShowingState() {
		super( Application.APPLICATION_UI_GROUP, java.util.UUID.fromString( "27fa1607-a722-4001-aa82-74cf938fd100" ), false );
	}
}
