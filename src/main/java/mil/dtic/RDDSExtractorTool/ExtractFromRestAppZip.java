package mil.dtic.RDDSExtractorTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;

public class ExtractFromRestAppZip {
	public static void extractPDFs(final String zipFile, final String destDirectory) throws IOException, ParserConfigurationException, SAXException {
		String tempDirectoryPath = zipFile.substring(0,zipFile.lastIndexOf(File.separator)) + File.separator + "ExtractionTemp";
		File tempDirectory = new File(tempDirectoryPath);
		if(!tempDirectory.exists()) {
			tempDirectory.mkdir();
		}
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			if(zipEntry.getName().contains(".pdf")) {
				File newFile = newFile(tempDirectory, zipEntry);
	            FileOutputStream fos = new FileOutputStream(newFile);
	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	                fos.write(buffer, 0, len);
	            }
	            fos.close();
			}
			System.out.println("Extracted pdf file: " + zipEntry.getName() + " to the directory: " + tempDirectoryPath);
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        ExtractAllPDFsFromDirectory.extractAttachments(tempDirectoryPath);
        moveFilesToDestDirectory(tempDirectoryPath, destDirectory);
	}
	
	public static void moveFilesToDestDirectory(String fileDirectory, String destDirectory) throws IOException {
		File tempFileDirectory = new File(fileDirectory);
		String[] allFiles = tempFileDirectory.list();
		for(String file : allFiles) {
			File transferFile = new File(fileDirectory + File.separator + file);
			File finalSave = new File(destDirectory + File.separator + file);
			FileUtils.copyFile(transferFile, finalSave);
		}
		FileUtils.deleteDirectory(tempFileDirectory);
	}
	
	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
         
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
         
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
         
        return destFile;
    }
}
