package com.sean.soap.client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class CapturedText extends OutputStream {

	private String prefix;
	private Obtainer obtainer;
	private PrintStream printStream;
	private StringBuilder stringBuilder;
	
	public CapturedText(String prefix, Obtainer obtainer, PrintStream printStream) {
	
		this.prefix = prefix;
		this.printStream = printStream;
		this.obtainer = obtainer;
	
		stringBuilder = new StringBuilder(120);
		stringBuilder.append(prefix);
	}

	// Input text is appended to the PrintStream displayed in the Java Swing application.
	@Override
	public void write(int input) throws IOException {

		char character = (char) input;
		String value = Character.toString(character);
		stringBuilder.append(value);
		
		if (value.equals("\n")) {
		
			obtainer.appendText(stringBuilder.toString());
			stringBuilder.delete(0, stringBuilder.length());
			stringBuilder.append(prefix);
		}
		
		printStream.print(character);
	}        
}