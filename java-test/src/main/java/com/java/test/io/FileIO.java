package com.java.test.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

interface FilePrinter {
	void printFile();
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

	public AbstractFilePrinter(String filePath, Charset charset) {
		this.filePath = filePath;
		this.charset = charset != null ? charset : Charset.defaultCharset();
	}

	public Charset getCharset() {
		return charset;
	}

	public String getFilePath() {
		return filePath;
	}

	private String filePath;
	private Charset charset;
}

class StreamFilePrinter extends AbstractFilePrinter {
	private InputStream in = null;

	public StreamFilePrinter(String filePath, Charset charset) {
		super(filePath, charset);
	}

	private byte[] toBytes(Vector<Byte> vec) {
		int i = 0;
		byte[] bytes = new byte[vec.size()];
		for (Byte b : vec) {
			bytes[i++] = b;
		}
		return bytes;
	}

	@Override
	String readLine() throws IOException {
		Vector<Byte> byteArray = new Vector<Byte>();
		do {
			int n = in.read();
			if (n == -1 || n == '\n') {
				break;
			}
			byteArray.add((byte) n);
		} while (true);

		if (byteArray.size() > 0) {
			if (getCharset().equals(StandardCharsets.UTF_16LE)
					|| (getCharset().equals(StandardCharsets.UTF_16) && ByteOrder
							.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN))) {
				if (byteArray.size() > 1
						&& byteArray.elementAt(byteArray.size() - 1) == '\0'
						&& byteArray.elementAt(byteArray.size() - 2) == '\r') {
					byteArray.setSize(byteArray.size() - 2);
				}
				return new String(toBytes(byteArray), getCharset());
			} else if (getCharset().equals(StandardCharsets.UTF_16BE)
					|| (getCharset().equals(StandardCharsets.UTF_16) && ByteOrder
							.nativeOrder().equals(ByteOrder.BIG_ENDIAN))) {
				if (byteArray.size() > 0
						&& byteArray.elementAt(byteArray.size() - 1) == '\0') {
					byteArray.setSize(byteArray.size() - 1);
				}
				if (byteArray.size() > 1
						&& byteArray.elementAt(byteArray.size() - 1) == '\r'
						&& byteArray.elementAt(byteArray.size() - 2) == '\0') {
					byteArray.setSize(byteArray.size() - 2);
				}
				return new String(toBytes(byteArray), getCharset());
			}
			if (byteArray.size() > 0
					&& byteArray.elementAt(byteArray.size() - 2) == '\r') {
				byteArray.setSize(byteArray.size() - 1);
			}

			return new String(toBytes(byteArray), getCharset());
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
		in = new BufferedInputStream(new FileInputStream(getFilePath()));
	}
}

class BufferedFilePrinter extends AbstractFilePrinter {

	private BufferedReader in = null;

	public BufferedFilePrinter(String filePath, Charset charset) {
		super(filePath, charset);
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
		in = new LineNumberReader(new InputStreamReader(new FileInputStream(
				getFilePath()), getCharset()));
	}
}

public class FileIO {
	public static final String filePath = System.getProperty(
			"resource.filepath", "D:\\unicode.txt");
	public static final String charset = System.getProperty("resource.charset",
			"UTF-16");

	public static void main(String[] args) {
		FilePrinter filePrinter = new StreamFilePrinter(filePath,
				Charset.forName(charset));
		filePrinter.printFile();
		FilePrinter filePrinterEx = new BufferedFilePrinter(filePath,
				Charset.forName(charset));
		filePrinterEx.printFile();
	}
}
