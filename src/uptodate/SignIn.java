package uptodate;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignIn extends JFrame implements ActionListener {
    
    JButton enter;
    JPanel panel;
    JLabel uName,pWord;
    final JTextField user,pass;
    static String DB_URL;
    public SignIn(String link)
    {
        DB_URL = link;
	uName = new JLabel();
	uName.setText("Username: ");
	user = new JTextField(40);

	pWord = new JLabel();
	pWord.setText("Password: ");
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
	setTitle("Sign In");
    }

    public void actionPerformed(ActionEvent ae)
    {
	String username = user.getText();
	String password = pass.getText();
	User.signIn(this,username,password,DB_URL);
    }
    
}
