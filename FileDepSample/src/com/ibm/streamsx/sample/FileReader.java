package com.ibm.streamsx.sample;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import com.ibm.streamsx.topology.function.Supplier;

public class FileReader implements Supplier<Iterable<String>>, Iterable<String>, Iterator<String> {
	private static final long serialVersionUID = 1L;

	private String filename;
	private transient BufferedReader reader;
	private transient String nextLine;
	
	public FileReader(String filename) {
		this.filename = filename;
	}
	
	private Object readResolve() throws Exception {
		// read the file contents
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream(this.filename);
		if(is == null)
			throw new FileNotFoundException("Unable to find '" + filename + "' in any loaded libraries.");

		reader = new BufferedReader(new InputStreamReader(is));
		return this;
		
	}
	
	@Override
	public boolean hasNext() {
		try {
			return ((nextLine = reader.readLine()) != null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String next() {
		return nextLine;
	}

	@Override
	public Iterator<String> iterator() {
		return this;
	}

	@Override
	public Iterable<String> get() {
		return this;
	}

}
