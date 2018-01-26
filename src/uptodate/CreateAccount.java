/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uptodate;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author marktan
 */
public class CreateAccount extends JFrame implements ActionListener {
    JButton enter;
    JPanel panel;
    JLabel uName,pWord;
    final JTextField user,pass;
    static String DB_URL;
    public CreateAccount(String link)
    {
        DB_URL = link;
	uName = new JLabel();
	uName.setText("New Username: ");
	user = new JTextField(40);

	pWord = new JLabel();
	pWord.setText("New Password: ");
	pass = new JPasswordField(40);
        
	enter = new JButton("Enter");

	panel = new JPanel(new GridLayout(3,1));
	panel.add(uName);
	panel.add(user);
	panel.add(pWord);
	panel.add(pass);
        panel.add(enter);
	this.add(panel, BorderLayout.CENTER);
	enter.addActionListener(this);
	setTitle("Create Account");
    }

    public void actionPerformed(ActionEvent ae)
    {
	String username = user.getText();
	String password = pass.getText();
	User.createAccount(username,password,DB_URL);
    }
}
