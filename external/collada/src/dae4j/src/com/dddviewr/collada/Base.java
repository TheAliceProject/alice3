package com.dddviewr.collada;

import java.io.PrintStream;

public abstract class Base {
	public String createIndent(int count) {
		String spaces = "                ";
		String indent = "";
		while (indent.length() < (count & 0xF0))
			indent = indent + spaces;
		indent = indent + spaces.substring(0, count & 0xF);
		return indent;
	}

	public abstract void dump(PrintStream paramPrintStream, int paramInt);
}