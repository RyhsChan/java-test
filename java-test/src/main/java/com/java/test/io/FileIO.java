package com.java.test.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;

interface FilePrinter {
	void printFile();

	void closeFile() throws IOException;

	void openFile() throws IOException;
}

abstract class AbstractFilePrinter implements FilePrinter {
	// template method
	public void printFile() {
		try {
			openFile();
			do {
				String line = readLine();
				if (line != null) {
					System.out.println(line);
				} else {
					break;
				}
			} while (true);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				closeFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	abstract String readLine() throws IOException;

	abstract public void openFile() throws IOException;

	abstract public void closeFile() throws IOException;

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

	public StreamFilePrinter(String filePath) {
		super(filePath);
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

	@Override
	public void closeFile() throws IOException {
		if (in != null) {
			in.close();
		}
	}

	@Override
	public void openFile() throws IOException {
		closeFile();
		in = new FileInputStream(getFilePath());
	}
}

class BufferedFilePrinter extends AbstractFilePrinter {

	private BufferedReader in = null;

	public BufferedFilePrinter(String filePath) {
		super(filePath);
	}

	@Override
	String readLine() throws IOException {
		return in.readLine();
	}

	@Override
	public void closeFile() throws IOException {
		if (in != null) {
			in.close();
		}
	}

	public void openFile() throws IOException {
		closeFile();
		in = new LineNumberReader(new FileReader(getFilePath()));
	}
}

public class FileIO {
	public static final String filePath = System.getProperty(
			"resource.filepath", "D:\\dns.txt");

	public static void main(String[] args) {
		FilePrinter filePrinter = new StreamFilePrinter(filePath);
		FilePrinter filePrinterEx = new BufferedFilePrinter(filePath);
		filePrinter.printFile();
		filePrinterEx.printFile();
	}
}
