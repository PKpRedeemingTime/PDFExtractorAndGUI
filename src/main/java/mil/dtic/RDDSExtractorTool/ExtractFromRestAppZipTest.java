package mil.dtic.RDDSExtractorTool;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ExtractFromRestAppZipTest {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		
		String zipFile = "C:\\Dev\\RDDS\\RDDS_2019_CXE_MASTER_do_not_touch\\More,_in_addition_to_RDDS_JB_pdfs,_xmls,_zips\\RDTE_J_Books,_pdfs,_xmls,_zips\\Army_JB\\R2_Selections_Individual_repackaged.zip";
		String destDirectory = "C:\\Dev\\RDDS\\RDDS_2019_CXE_MASTER_do_not_touch\\More,_in_addition_to_RDDS_JB_pdfs,_xmls,_zips\\RDTE_J_Books,_pdfs,_xmls,_zips\\Army_JB\\FinalDestination";
		ExtractFromRestAppZip.extractPDFs(zipFile, destDirectory);
		
	}

}
