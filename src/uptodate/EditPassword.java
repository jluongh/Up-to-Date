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
public class EditPassword extends JFrame implements ActionListener {
    
    JButton enter;
    JPanel panel;
    JLabel uName,oldPword,newPword;
    final JTextField user,oldPass,newPass;
    static String DB_URL;
    public EditPassword(String link)
    {
        DB_URL = link;
	uName = new JLabel();
	uName.setText("Username: ");
	user = new JTextField(40);

	oldPword = new JLabel();
	oldPword.setText("Old Password: ");
	oldPass = new JPasswordField(40);

        newPword = new JLabel();
	newPword.setText("Password: ");
	newPass = new JPasswordField(40);
	enter = new JButton("Enter");
        
	panel = new JPanel(new GridLayout(4,1));
	panel.add(uName);
	panel.add(user);
	panel.add(oldPword);
	panel.add(oldPass);
        panel.add(newPword);
	panel.add(newPass);
	panel.add(enter);
	this.add(panel, BorderLayout.CENTER);
	enter.addActionListener(this);
	setTitle("Edit Password");
    }
    
       public void actionPerformed(ActionEvent ae)
    {
	String username = user.getText();
	String oldpassword = oldPass.getText();
        String newpassword = newPass.getText();
	User.editPassword(this,username,oldpassword,newpassword,DB_URL);
    }
}
