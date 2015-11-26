
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login {

	private JFrame frmLogin;
	private JTextField txtName;
	private String name;
	private String pwd;
	private Connection conn;
	private java.sql.Statement stat;
	private ResultSet res;
	private JPasswordField txtPwd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			 conn = DriverManager.getConnection("jdbc:mysql://localhost/vrcsoftware","root","");
			 stat = conn.createStatement();
		}catch(Exception e){
			System.out.println("Error: " + e);
		}
		
		frmLogin = new JFrame();
		frmLogin.setBounds(100, 100, 200, 135);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(null);
		
		JLabel lblUsername = new JLabel("Username: ");
		lblUsername.setBounds(10, 11, 88, 14);
		frmLogin.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password: ");
		lblPassword.setBounds(10, 36, 88, 14);
		frmLogin.getContentPane().add(lblPassword);
		
		txtName = new JTextField();
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER)
					txtPwd.requestFocus();
			}
		});
		txtName.setBounds(90, 8, 86, 20);
		frmLogin.getContentPane().add(txtName);
		txtName.setColumns(10);

		txtPwd = new JPasswordField();
		txtPwd.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					login();
			}
		});
		txtPwd.setBounds(90, 36, 84, 20);
		txtPwd.setEchoChar('*');
		frmLogin.getContentPane().add(txtPwd);
		
		JButton btnLogin = new JButton("Log in");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
		});
		btnLogin.setBounds(90, 62, 70, 23);
		frmLogin.getContentPane().add(btnLogin);
	}
	
	public void openFrame(){
		frmLogin.setVisible(true);
	}
	
	private void login(){
		name = txtName.getText();
		pwd = new String(txtPwd.getPassword());
		try {
			String query = "SELECT * FROM `clerk` WHERE `Clerk_Name` ='" + name + "';";
			res = stat.executeQuery(query);
			res.next();
			if(pwd.equals(res.getString("Clerk_Pass"))){
				VRCsoftware vrc = new VRCsoftware();
				frmLogin.setVisible(false);
				vrc.openFrame();
			} else{
				JOptionPane.showMessageDialog(null, "Invalid password");
			}
			
		} catch (SQLException se) {
			JOptionPane.showMessageDialog(null, "Invalid username");
		}
	}
}
