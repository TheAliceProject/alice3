package edu.cmu.cs.dennisc.java.util;

public class Collections {
	private Collections() {
		throw new AssertionError();
	}
	public static <E> java.util.HashSet< E > newHashSet() {
		return new java.util.HashSet< E >();
	}
}
