package com.java.test.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;

interface FilePrinter {
	void printFile() throws IOException;
	void closeFile();
}

abstract class AbstractFilePrinter implements FilePrinter {
	public void printFile() throws IOException {
		do {
			String line = readLine();
			if (line != null) {
				System.out.println(line);
			} else {
				return;
			}
		} while (true);
	}

	abstract String readLine() throws IOException;

	public AbstractFilePrinter(String filePath) {
		this.filePath = filePath;
	}

	public String getFilePath() {
		return filePath;
	}

	private String filePath;
}

class StreamFilePrinter extends AbstractFilePrinter {
	private InputStream in = null;

	public StreamFilePrinter(String filePath) throws FileNotFoundException {
		super(filePath);
		in = new FileInputStream(filePath);
	}

	@Override
	String readLine() throws IOException {
		StringBuilder builder = new StringBuilder();
		byte b;
		do {
			b = (byte) in.read();
			if (b == -1 || b == '\n') {
				break;
			}
			builder.append((char) b);
		} while (true);

		if (builder.length() > 0) {
			if (builder.charAt(builder.length() - 1) == '\r') {
				builder.deleteCharAt(builder.length() - 1);
			}
			return builder.toString();
		}
		return null;
	}

	public void closeFile() {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class BufferedFilePrinter extends AbstractFilePrinter {

	private BufferedReader in = null;

	public BufferedFilePrinter(String filePath) throws FileNotFoundException {
		super(filePath);
		in = new LineNumberReader(new FileReader(filePath));
	}

	@Override
	String readLine() throws IOException {
		return in.readLine();
	}

	public void closeFile() {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}

public class FileIO {
	public static final String filePath = "D:\\dns.txt";

	public static void main(String[] args) {
		FilePrinter filePrinter = null;
		try {
			// filePrinter = new BufferedFilePrinter(filePath);
			filePrinter = new StreamFilePrinter(filePath);
			filePrinter.printFile();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			filePrinter.closeFile();
		}
		
	}
}
