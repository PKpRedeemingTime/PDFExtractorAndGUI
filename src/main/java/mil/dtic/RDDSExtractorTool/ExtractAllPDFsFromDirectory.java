package mil.dtic.RDDSExtractorTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentNameDictionary;
import org.apache.pdfbox.pdmodel.PDEmbeddedFilesNameTreeNode;
import org.apache.pdfbox.pdmodel.common.filespecification.PDComplexFileSpecification;
import org.apache.pdfbox.pdmodel.common.filespecification.PDEmbeddedFile;

public class ExtractAllPDFsFromDirectory {
	public static void extractAttachments(final String targetDir) {
		findAllPDFs(targetDir);
	}
	
	public static void findAllPDFs(final String targetDir) {
		File file = new File(targetDir);
		String[] allFiles = file.list();
		List<String> allPDFs = new ArrayList<String>();
		for(String testFile : allFiles) {
			if(testFile.contains(".pdf")) {
				allPDFs.add(testFile);
			}
		}
		if (allPDFs.size() < 1) {
			throw new NullPointerException("There are no PDFs to parse in this folder");
		} else {
			extractFromPDFs(allPDFs, targetDir);
		}
	}
	
	public static void extractFromPDFs(final List<String> allPDFs, final String targetDir) {
		for(String pdf : allPDFs) {
			String fileName = targetDir + File.separator + pdf;
			try (final PDDocument doc = PDDocument.load(new File(fileName))) {
				final PDDocumentNameDictionary namesDictionary = new PDDocumentNameDictionary(doc.getDocumentCatalog());
				final PDEmbeddedFilesNameTreeNode efTree = namesDictionary.getEmbeddedFiles();
				if(Objects.isNull(efTree)) {
					System.out.println("No attachments found in file: " + pdf);
					continue;
				}
				Map<String, PDComplexFileSpecification> names = efTree.getNames();
				extractFiles(names, targetDir);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			
		}
	}
	
	private static void extractFiles(Map<String, PDComplexFileSpecification> names, String targetDir) throws IOException {
		int count = 1;
        for (Map.Entry<String, PDComplexFileSpecification> entry : names.entrySet()) {
        	if(entry.getKey().contains(".zip")) {
        		PDComplexFileSpecification fileSpec = entry.getValue();
                String fileName = fileSpec.getFilename();
                PDEmbeddedFile embeddedFile = getEmbeddedFile(fileSpec);
                copyFileToDirectory(fileName, embeddedFile, targetDir, count);
                count++;
        	}
        }
    }
	
	private static PDEmbeddedFile getEmbeddedFile(PDComplexFileSpecification fileSpec) {
        PDEmbeddedFile embeddedFile = null;
        if (fileSpec != null) {
            embeddedFile = fileSpec.getEmbeddedFileUnicode();
            if (embeddedFile == null) {
                embeddedFile = fileSpec.getEmbeddedFileDos();
            }
            if (embeddedFile == null) {
                embeddedFile = fileSpec.getEmbeddedFileMac();
            }
            if (embeddedFile == null) {
                embeddedFile = fileSpec.getEmbeddedFileUnix();
            }
            if (embeddedFile == null) {
                embeddedFile = fileSpec.getEmbeddedFile();
            }
        }
        return embeddedFile;
    }
	
	private static void copyFileToDirectory(final String srcFile, final PDEmbeddedFile embeddedFile, String destDirectory, int count) throws IOException {
		String temp = srcFile.substring(0, srcFile.lastIndexOf('.')) + "_" + count + ".zip";
		String embeddedFilename = destDirectory + "/" + temp;
		File file = new File(embeddedFilename);
		System.out.println("Copied File " + temp + " to the directory: " + destDirectory);
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(embeddedFile.toByteArray());
		} catch (IOException e) {
			System.out.println("File copying failed. src: " + srcFile + "; Destination directory: " + embeddedFile);
			System.out.println(e.getMessage());
		}
		extractFromZip(embeddedFilename, destDirectory);
	}
	
	public static void extractFromZip(String zipFile, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			if(zipEntry.getName().contains(".xml")) {
				File newFile = newFile(destDir, zipEntry);
	            FileOutputStream fos = new FileOutputStream(newFile);
	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	                fos.write(buffer, 0, len);
	            }
	            fos.close();
	            System.out.println("Extracted xml file: " + zipEntry.getName() + " to the directory: " + destDirectory);
			}
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
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
