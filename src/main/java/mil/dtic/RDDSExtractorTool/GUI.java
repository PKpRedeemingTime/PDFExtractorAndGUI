package mil.dtic.RDDSExtractorTool;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class GUI {
	private JFrame mainFrame;
	private JPanel mainBodyPanel;
	private JPanel pdfPanel;
	private JPanel zipPanel;
	private JLabel statusLabel;
	private JLabel pdfPanelLabel;
	private JFileChooser pdfDirectoryJFC;
	private JLabel zipPanelLabel;
	private JPanel zipFileChooser;
	private JLabel zipFileChooserLabel;
	private JFileChooser zipFileJFC;
	private JPanel destDirectoryChooser;
	private JLabel destDirectoryChooserLabel;
	private JFileChooser destDirectoryJFC;
	private GridBagConstraints mbpGBC;
	private GridBagConstraints pdfGBC;
	private GridBagConstraints zipGBC;
	private GridBagConstraints zfcGBC;
	private GridBagConstraints ddcGBC;
	
	private void show() {
		mainFrame = new JFrame("Document Extractor");
		JFrame.setDefaultLookAndFeelDecorated(true);
		mainFrame.setSize(1300,900);
		mainFrame.setLayout(new FlowLayout());
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addComponentsToMainFrame();
		
		mainFrame.pack();
		mainFrame.setVisible(true);
		
	}
	
	public void addComponentsToMainFrame() {
		mainBodyPanel = new JPanel();
		mainBodyPanel.setLayout(new GridBagLayout());
		mbpGBC = new GridBagConstraints();
		mbpGBC.fill = GridBagConstraints.HORIZONTAL;
		addComponentsToMainBodyPanel();
		mainFrame.add(mainBodyPanel);
	}
	
	public void addComponentsToMainBodyPanel() {
		pdfPanel = new JPanel();
		pdfPanel.setLayout(new GridBagLayout());
		pdfGBC = new GridBagConstraints();
		pdfGBC.fill = GridBagConstraints.HORIZONTAL;
		mbpGBC.gridx = 0;
		mbpGBC.gridy = 0;
		addComponentsToPDFPanel();
		mainBodyPanel.add(pdfPanel, mbpGBC);
		
		zipPanel = new JPanel();
		zipPanel.setLayout(new GridBagLayout());
		zipGBC = new GridBagConstraints();
		zipGBC.fill = GridBagConstraints.HORIZONTAL;
		mbpGBC.gridx = 0;
		mbpGBC.gridy = 1;
		addComponentsToZipPanel();
		mainBodyPanel.add(zipPanel, mbpGBC);
		
		statusLabel = new JLabel();
		statusLabel.setText("<html><span style='font-size:15px; color:red'><BR>No selections made...</span></html>");
		mbpGBC.gridx = 0;
		mbpGBC.gridy = 2;
		mainBodyPanel.add(statusLabel, mbpGBC);
	}
	
	public void addComponentsToPDFPanel() {
		pdfPanelLabel = new JLabel();
		pdfPanelLabel.setText("<html><span style='font-size:15px; color:red'>If you are extracting from JBook(s) from the Comptroller, please select the directory that contains the PDF(s).<BR></span></html>");
		pdfGBC.gridx = 0;
		pdfGBC.gridy = 0;
		pdfPanel.add(pdfPanelLabel, pdfGBC);
		
		pdfDirectoryJFC = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		pdfDirectoryJFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		pdfGBC.gridx = 0;
		pdfGBC.gridy = 1;
		pdfPanel.add(pdfDirectoryJFC, pdfGBC);
		
		JButton extractPDF = new JButton("<html><span style='font-size:15px'>Extract From JBook</span></html>");
		extractPDF.setActionCommand("ExtractPDF");
		extractPDF.addActionListener(new ButtonClickListener());
		pdfGBC.gridx = 0;
		pdfGBC.gridy = 2;
		pdfPanel.add(extractPDF, pdfGBC);
		
	}
	
	public void addComponentsToZipPanel() {
		zipPanelLabel = new JLabel();
		zipPanelLabel.setText("<html><span style='font-size:15px; color:red'><BR>If you are extracting from a zip from the RestApp, please select the zip file and choose a directory to save the extracted files.<BR></span></html>");
		zipGBC.gridx = 0;
		zipGBC.gridy = 0;
		zipGBC.gridwidth = 2;
		zipPanel.add(zipPanelLabel, zipGBC);
		
		zipFileChooser = new JPanel();
		zipFileChooser.setLayout(new GridBagLayout());
		zfcGBC = new GridBagConstraints();
		zfcGBC.fill = GridBagConstraints.HORIZONTAL;
		zipGBC.gridx = 0;
		zipGBC.gridy = 1;
		zipGBC.gridwidth = 1;
		addComponentsToZipFileChooser();
		zipPanel.add(zipFileChooser, zipGBC);
		
		destDirectoryChooser = new JPanel();
		destDirectoryChooser.setLayout(new GridBagLayout());
		ddcGBC = new GridBagConstraints();
		ddcGBC.fill = GridBagConstraints.HORIZONTAL;
		zipGBC.gridx = 1;
		zipGBC.gridy = 1;
		addComponentsToDestDirectoryChooser();
		zipPanel.add(destDirectoryChooser, zipGBC);
		
		JButton extractZip = new JButton("<html><span style='font-size:15px'>Extract From Zip</span></html>");
		extractZip.setActionCommand("ExtractZip");
		extractZip.addActionListener(new ButtonClickListener());
		zipGBC.gridx = 0;
		zipGBC.gridy = 2;
		zipGBC.gridwidth = 2;
		zipPanel.add(extractZip, zipGBC);
	}
	
	public void addComponentsToZipFileChooser() {
		zipFileChooserLabel = new JLabel();
		zipFileChooserLabel.setText("<html><span style='font-size:15px; color:red'><BR>Please select a zip to extract.</span></html>");
		zfcGBC.gridx = 0;
		zfcGBC.gridy = 0;
		zipFileChooser.add(zipFileChooserLabel, zfcGBC);
		
		zipFileJFC = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		zipFileJFC.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("zip files", "zip");
		zipFileJFC.addChoosableFileFilter(filter);
		zfcGBC.gridx = 0;
		zfcGBC.gridy = 1;
		zipFileChooser.add(zipFileJFC, zfcGBC);
	}
	
	public void addComponentsToDestDirectoryChooser() {
		destDirectoryChooserLabel = new JLabel();
		destDirectoryChooserLabel.setText("<html><span style='font-size:15px; color:red'><BR>Please select a destination directory.</span></html>");
		ddcGBC.gridx = 0;
		ddcGBC.gridy = 0;
		destDirectoryChooser.add(destDirectoryChooserLabel, ddcGBC);
		
		destDirectoryJFC = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		destDirectoryJFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		ddcGBC.gridx = 0;
		ddcGBC.gridy = 1;
		destDirectoryChooser.add(destDirectoryJFC, ddcGBC);
	}
	
	private class ButtonClickListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			String targetDir = "";
	        String zipFile = "";
	        String destDirectory = "";
	        if(command.equals( "ExtractPDF")) {
	        	File selectedFile = pdfDirectoryJFC.getCurrentDirectory();
	        	targetDir = selectedFile.getPath();
	        	if(targetDir.contentEquals("Z:\\Desktop") || targetDir.contentEquals("")) {
	        		statusLabel.setText("<html><span style='font-size:15px; color:red'><BR>Please choose a directory.</span></html>");
	        		return;
	        	}
	        	ExtractAllPDFsFromDirectory.extractAttachments(targetDir);
	        	statusLabel.setText("<html><span style='font-size:15px; color:red'>Your PDF files have been extracted. Please verify in the target folder.</span></html>");
	        } else if(command.equals( "ExtractZip"))  {
	        	try {
	        		File selectedZip = zipFileJFC.getSelectedFile();
	        		zipFile = selectedZip.getPath();
	        	} catch(NullPointerException npe) {
	        		statusLabel.setText("<html><span style='font-size:15px; color:red'><BR>Please choose a zip file to extract.</span></html>");
	        		return;
	        	}
	        	File selectedDestination = destDirectoryJFC.getCurrentDirectory();
				destDirectory = selectedDestination.getPath();
				statusLabel.setText("<html><span style='font-size:15px; color:red'>Your files have been extracted. Please verify in the target folder.</span></html>");
				if(destDirectory.contentEquals("Z:\\Desktop") || destDirectory.contentEquals("")) {
					statusLabel.setText("<html><span style='font-size:15px; color:red'><BR>Please choose a destination directory for your files.</span></html>");
					return;
				} else if(zipFile.contentEquals("")) {
					statusLabel.setText("<html><span style='font-size:15px; color:red'>Please choose a zip file to extract.</span></html>");
				} else {
					try {
						ExtractFromRestAppZip.extractPDFs(zipFile, destDirectory);
					} catch (IOException | ParserConfigurationException | SAXException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					statusLabel.setText("<html><span style='font-size:15px; color:red'><BR>Your files have been extracted. Please verify in the destination directory.</span></html>");
				}
			}
		}
	}
	
	public static void main(String[] args) {		
		GUI GUI = new GUI();
		GUI.show();
	}

}
