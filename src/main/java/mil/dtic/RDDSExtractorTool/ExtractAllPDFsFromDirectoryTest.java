package mil.dtic.RDDSExtractorTool;

public class ExtractAllPDFsFromDirectoryTest {

	public static void main(String[] args) {
		
		String targetDir = "C:\\Dev\\RDDS\\RDDS_2019_CXE_MASTER_do_not_touch\\More,_in_addition_to_RDDS_JB_pdfs,_xmls,_zips\\RDTE_J_Books,_pdfs,_xmls,_zips\\Army_JB";
		ExtractAllPDFsFromDirectory.extractAttachments(targetDir);

	}

}
