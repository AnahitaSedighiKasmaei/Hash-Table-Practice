package csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Date;
import java.util.Arrays;

public class CsvCreator {
	private String[] titles;
	private String filePath;
	private String separator = ",";
	private final String newLine = "\r\n";
	FileWriter fileWriter;
	private boolean generateUniqueName = true;

	public CsvCreator(String path, String... titles) {
		this.titles = titles;
		if (path == null) {
			String unique = "";
			if (generateUniqueName) {
				unique = "-" + (new java.util.Date()).getTime();
			}
			path = (new File(".")).getAbsolutePath() + "\\output" + unique + ".csv";
		}
		this.filePath = path;

		File file = new File(filePath);
		try {
			fileWriter = new FileWriter(file, false);
			fileWriter.write(String.join(separator, titles));
			fileWriter.write(newLine);
			fileWriter.close();
			fileWriter = new FileWriter(file, true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public <T> void newRow(T... cells) {
		String[] arr = new String[cells.length];
		for (var i = 0; i < cells.length; i++)
			arr[i] = String.valueOf(cells[i]);
		try {
			fileWriter.write(String.join(separator, arr));
			fileWriter.write(newLine);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void close() {
		try {
			fileWriter.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public boolean isGenerateUniqueName() {
		return generateUniqueName;
	}

	public void setGenerateUniqueName(boolean generateUniqueName) {
		this.generateUniqueName = generateUniqueName;
	}

}
