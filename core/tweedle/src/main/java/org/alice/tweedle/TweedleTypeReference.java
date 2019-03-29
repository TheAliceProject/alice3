package org.alice.tweedle;

import org.alice.tweedle.run.Frame;
import org.alice.tweedle.run.TweedleObject;

public class TweedleTypeReference extends TweedleType implements InvocableMethodHolder {
	public TweedleTypeReference( String typeName ) {
		super( typeName );
	}

	@Override public boolean willAcceptValueOfType( TweedleType type) {
		if (this.equals( type ) ) {
			return true;
		} else{
			throw new TweedleLinkException( "Attempt to use an unlinked type " + getName() );
		}
	}

	@Override public void invoke( Frame frame, TweedleObject target, TweedleMethod method, TweedleValue[] arguments ) {
		throw new TweedleLinkException("Attempt to invoke the method " + method.getName() + " on an unlinked type " + getName());
	}

	@Override public boolean equals( Object obj ) {
		return obj instanceof TweedleTypeReference && this.getName().equals( ((TweedleTypeReference) obj).getName() );
	}

	@Override public int hashCode() {
		return getName().hashCode();
	}
}
