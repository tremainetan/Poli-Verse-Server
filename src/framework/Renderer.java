package framework;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
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
		
		//Splash Screen
		JLabel imageIcon = new JLabel(new ImageIcon(ImageLoader.LOADING_PAGE), SwingConstants.CENTER);
		JWindow window = new JWindow();
		window.getContentPane().setBackground(new Color(88, 88, 88));
		window.getContentPane().add(imageIcon);
		Toolkit toolkit = Toolkit.getDefaultToolkit();  
		int windowWidth = ((int) toolkit.getScreenSize().getWidth());  
		int windowHeight = ((int) toolkit.getScreenSize().getHeight());  
		window.setSize(windowWidth, windowHeight);
		window.requestFocus();
		window.setVisible(true);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		window.setVisible(false);
		window.dispose();
		
		//Chat Service
		textField.setFont(arialFont);
		textField.setEditable(true);
		
		messageArea.setFont(arialFont);
		messageArea.setEditable(false);
		
		DefaultCaret caret = (DefaultCaret) messageArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		Container contentPane = frame.getContentPane();
		contentPane.add(textField, BorderLayout.SOUTH);
		contentPane.add(new JScrollPane(messageArea), BorderLayout.CENTER);
		
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
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
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
