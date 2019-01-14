package orar.experiment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilMethod {
	static Logger logger_ = LoggerFactory.getLogger(UtilMethod.class);

	public static void createABoxListFile(String aboxFullPath, String aboxListFullPath) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(aboxListFullPath))) {
			bw.write(aboxFullPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createLogFile(String logFilePath,String logMessage) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFilePath))) {
			bw.write(logMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readContent(String pathToTextFile) {
		File file = new File(pathToTextFile);
		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(fileReader);
			StringBuilder sBuilder = new StringBuilder();
			String line = null;
			while ((line = bReader.readLine()) != null) {
				sBuilder.append(line + "\n");

			}
			bReader.close();
			return sBuilder.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static void zipAFolder(String outputZipFileFullPath, String folderFullPath) {
		String zipCommand = "zip -j " + outputZipFileFullPath + " " + folderFullPath + "*";
		executeCommand(zipCommand);
	}

	public static void createZipFile(String outputZipFile, String localtion_of_input_file) {
		byte[] buffer = new byte[1024];

		try {

			FileOutputStream fos = new FileOutputStream(outputZipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			ZipEntry ze = new ZipEntry(localtion_of_input_file);
			zos.putNextEntry(ze);
			FileInputStream in = new FileInputStream(localtion_of_input_file);

			int len;
			while ((len = in.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}

			in.close();
			zos.closeEntry();

			// remember close it
			zos.close();

			System.out.println("Done");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void kill_sh_process_and_its_subprocesses() {
		ProcessBuilder pbuilder = new ProcessBuilder();
		String strCommand = "pkill -P $(pgrep -x -n sh)";
		System.out.println("Kill command:" + strCommand);
		pbuilder.command("bash", "-c", strCommand);
		Process process;
		try {
			process = pbuilder.start();
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void kill_all_previous_Konclude() {
		executeCommand("killall -9 Konclude");
		executeCommand("killall -9 KoncludeMac");
		executeCommand("killall -9 KoncludeLinux");
	}

	public static void kill_all_apps() {
		executeCommand("pkill -9 -f Orar21.jar");
		executeCommand("pkill -9 -f pagodaMac.jar");
		executeCommand("pkill -9 -f pagodaLinux.jar");
		executeCommand("pkill -9 -f turtle2OWLConverter.jar");
		executeCommand("killall -9 Konclude");
		executeCommand("killall -9 KoncludeMac");
		executeCommand("killall -9 KoncludeLinux");
	}

	public static void executeCommand(String command) {

		ProcessBuilder pbuilder = new ProcessBuilder();
		logger_.info("Run command :" + command);
		pbuilder.command("bash", "-c", command);
		/// Note: Very strange thing is that the following does not work equivalently:
		/// pbuilder.command("bash -c command");
		Process process;
		try {
			process = pbuilder.start();
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
