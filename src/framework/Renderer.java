package framework;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import main.ClientMain;

public class Renderer {
	
	public JFrame frame;
	public JTextField textField = new JTextField(50);
	public JTextArea messageArea = new JTextArea(16, 50);
	
	public volatile String lastCommand = "";
	
	private ClientMain main;
	
	public Renderer(ClientMain main) {
		this.main = main;
	}
	
	public void createWindow() {
		frame = new JFrame("Poli-Verse");
		
		Font arialFont = new Font("Arial", Font.TRUETYPE_FONT, 20);
		
		textField.setFont(arialFont);
		textField.setEditable(true);
		
		messageArea.setFont(arialFont);
		messageArea.setEditable(false);
		
		DefaultCaret caret = (DefaultCaret) messageArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		Container contentPane = frame.getContentPane();
		contentPane.add(textField, BorderLayout.SOUTH);
		contentPane.add(new JScrollPane(messageArea), BorderLayout.CENTER);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		
		textField.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!textField.getText().isBlank()) {
						if (main.state == null) print(textField.getText());
						else print(main.clientData.USERNAME + ": " + textField.getText());
						lastCommand = textField.getText();
						textField.setText("");
					}
				}
			}
		);
	}
	
	public void print(String message) {
		messageArea.append(message + "\n");
	}
	
	public void clearScreen() {
		messageArea.setText("");
	}
	
	public void updateTitle(String title) {
		this.frame.setTitle(title);
	}
	
}
