package org.alice.netbeans.aliceprojectwizard.alicecomponentpalette.items.resources;

import org.openide.util.NbBundle;

/**
 * @author Dennis Cosgrovs
 */
public class I18nUtilities {

	private I18nUtilities() {
		throw new AssertionError();
	}
	private static final String PREFIX = "<html><body><code><pre>";
	private static final String POSTFIX = "</pre></code></body></html>";

	public static String getCode(Class<?> cls) {
		StringBuilder keyBuilder = new StringBuilder();
		keyBuilder.append("HINT_html-");
		keyBuilder.append(cls.getSimpleName().toUpperCase());



		String rv = NbBundle.getMessage(I18nUtilities.class, keyBuilder.toString());
		rv = rv.replaceAll(PREFIX, "");
		rv = rv.replaceAll(POSTFIX, "");
		rv = rv.replaceAll("&lt;", "<");
		return rv;
	}
}
