package net.etfbl.sanja.ids;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class LogManager {
	private static final String LOG_FILE = "ids.log";
	private static final String LOG_FOLDER = "C://sigurnost";
	
	public static synchronized void multipleLog(List<LogMessage> messages) {
		for(LogMessage logMessage : messages) {
			log(logMessage);
		}
	}
	
	public static synchronized void log(LogMessage message) {
		try {
			createFileIfNotExists();
			PrintWriter out = getLogFileWriter();
			out.println(message.toString());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void createFileIfNotExists() throws IOException {
		Path logFolderPath = Paths.get(LOG_FOLDER);
		try {
			Files.createDirectory(logFolderPath);
		} catch (FileAlreadyExistsException e) {
			System.out.println("Folder " + logFolderPath.getFileName() + " already exists");
		}
		
		Path logFilePath = Paths.get(LOG_FOLDER + File.separator + LOG_FILE);
		try {
			Files.createFile(logFilePath);
		} catch (FileAlreadyExistsException e) {
			System.out.println("File " + logFilePath.getFileName() + " already exists");
		}
	}
	
	private static PrintWriter getLogFileWriter() throws IOException {
		return new PrintWriter(new BufferedWriter(new FileWriter(new File(LOG_FOLDER + File.separator + LOG_FILE), true)), true);
	}
}
