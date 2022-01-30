import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import java.awt.TextArea;
import java.awt.ScrollPane;
import javax.swing.JTable;

public class notepadgui {
	static int size;
	static Font asd;
	

	
	public void kay�tetmeislemi(String metin) {
		
		JFileChooser chooser = new JFileChooser();
		chooser.setName("Farkl� Kaydet");
		chooser.setDialogTitle("Farkl� Kaydet");
		chooser.setForeground(Color.white);
		chooser.setBackground(Color.WHITE);
		chooser.setToolTipText("");
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		           "Metin Belgeleri", "txt" );
		chooser.setFileFilter(filter);
		chooser.showSaveDialog(null);
		try {
			
			   
			FileWriter fileWriter= new FileWriter(chooser.getSelectedFile()+".txt");
			 fileWriter.write(metin);
			   fileWriter.close();
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}

	

	private JFrame frmNotepad;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					notepadgui window = new notepadgui();
					window.frmNotepad.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public notepadgui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmNotepad = new JFrame();
		frmNotepad.setTitle("Notepad v1.0");
		frmNotepad.setAutoRequestFocus(false);
		frmNotepad.setBounds(100, 100, 800, 600);
		frmNotepad.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTextArea textArea = new JTextArea(20,20);
		frmNotepad.getContentPane().add(textArea, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		frmNotepad.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		

		
		
		JMenuBar menuBar = new JMenuBar();
		frmNotepad.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mn�tmOpen = new JMenuItem("New");
		mn�tmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmNotepad.dispose();
				notepadgui frame1 = new  notepadgui();
				frame1.main(null);
			}
		});
		mnFile.add(mn�tmOpen);
		
		JMenuItem mn�tmExit = new JMenuItem("Exit");
		mn�tmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmNotepad.dispose();
			
			
			}
		});
		
		
		
		JMenuItem mn�tmOpen_1 = new JMenuItem("Open");
		mn�tmOpen_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser= new JFileChooser();
			     chooser.showOpenDialog(null);
			     String name = chooser.getSelectedFile().getPath();
			     File file = new File(name);
				    FileReader fr;
					try {
						fr = new FileReader(file);
						BufferedReader br = new BufferedReader(fr);
					    String line;
					    try {
							while((line = br.readLine()) != null){
							    
							    textArea.setText(line);
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				    
			     
			     
			}
		});
		mnFile.add(mn�tmOpen_1);
		
		JMenuItem mn�tmSave = new JMenuItem("Save");
		mn�tmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = textArea.getText();
				kay�tetmeislemi(text);
			}
		});
		mnFile.add(mn�tmSave);
		mnFile.add(mn�tmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JCheckBox chckbxTextWrap = new JCheckBox("Text Wrap");
		chckbxTextWrap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setLineWrap(true);
		        textArea.setWrapStyleWord(true);
		        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		        if(chckbxTextWrap.isSelected()) {
		        	textArea.setLineWrap(false);
			        textArea.setWrapStyleWord(false);
			        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		        }
			}
		});
		mnEdit.add(chckbxTextWrap);
		
		JMenu mnFormat = new JMenu("Format");
		menuBar.add(mnFormat);
		
		JMenuItem mn�tmFontOption = new JMenuItem("Font ");
		mn�tmFontOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFontChooser cho = new JFontChooser();
				  asd=cho.showDialog(null, "Font Option");
				 
				  textArea.setFont(asd);
			}
		});
		mnFormat.add(mn�tmFontOption);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JCheckBox chckbxNightMode = new JCheckBox("Night Mode");
		chckbxNightMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setBackground(Color.WHITE);
				textArea.setForeground(Color.BLACK);
				if(chckbxNightMode.isSelected()) {
					textArea.setBackground(Color.BLACK);
					textArea.setForeground(Color.WHITE);
				}
				
					
				
				}
		});
		mnView.add(chckbxNightMode);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mn�tmAboutNotepad = new JMenuItem("About Notepad");
		mn�tmAboutNotepad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null,null);
			}
		});
		mnHelp.add(mn�tmAboutNotepad);
		
		JMenuBar menuBar_1 = new JMenuBar();
		menuBar.add(menuBar_1);
		
		
		
		
	}

}
