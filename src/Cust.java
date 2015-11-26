import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Cust {

	private JFrame frmCust;
	private JTextField txtFN;
	private JTextField txtLN;
	private JTextField txtAddress;
	private JLabel lblAddress;
	private JTextField txtPhone;
	private JLabel lblContact;
	private JLabel lblEmail;
	private JTextField txtEmail;
	private Connection conn;
	private Statement stat;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/*Cust window = new Cust();
					window.frmCust.setVisible(true);*/
					/*VRCsoftware vrc = new VRCsoftware();
					vrc.openFrame();*/
					Login login = new Login();
					login.openFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public Cust() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/vrcsoftware","root","");
			stat = (Statement) conn.createStatement();
			
		} catch(Exception e){
			System.out.println(e);
		}
		
		frmCust = new JFrame();
		frmCust.setTitle("New Customer");
		frmCust.setBounds(100, 100, 250, 240);
		frmCust.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmCust.getContentPane().setLayout(null);
		
		JLabel lblFirstName = new JLabel("First Name: ");
		lblFirstName.setBounds(10, 11, 79, 14);
		frmCust.getContentPane().add(lblFirstName);
		
		txtFN = new JTextField();
		txtFN.setBounds(99, 8, 86, 20);
		frmCust.getContentPane().add(txtFN);
		txtFN.setColumns(10);
		
		JLabel lblLastName = new JLabel("Last Name: ");
		lblLastName.setBounds(10, 39, 79, 14);
		frmCust.getContentPane().add(lblLastName);
		
		txtLN = new JTextField();
		txtLN.setColumns(10);
		txtLN.setBounds(99, 36, 86, 20);
		frmCust.getContentPane().add(txtLN);
		
		JButton btnSaveCust = new JButton("");
		btnSaveCust.setIcon(new ImageIcon(Cust.class.getResource("/iconimg/save.png")));
		btnSaveCust.setBounds(169, 157, 16, 16);
		frmCust.getContentPane().add(btnSaveCust);
		
		txtAddress = new JTextField();
		txtAddress.setColumns(10);
		txtAddress.setBounds(99, 64, 86, 20);
		frmCust.getContentPane().add(txtAddress);
		
		lblAddress = new JLabel("Address: ");
		lblAddress.setBounds(10, 67, 79, 14);
		frmCust.getContentPane().add(lblAddress);
		
		txtPhone = new JTextField();
		txtPhone.setColumns(10);
		txtPhone.setBounds(99, 95, 86, 20);
		frmCust.getContentPane().add(txtPhone);
		
		lblContact = new JLabel("Phone No.");
		lblContact.setBounds(10, 98, 79, 14);
		frmCust.getContentPane().add(lblContact);
		
		lblEmail = new JLabel("Email: ");
		lblEmail.setBounds(10, 129, 79, 14);
		frmCust.getContentPane().add(lblEmail);
		
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(99, 126, 86, 20);
		frmCust.getContentPane().add(txtEmail);
		
		btnSaveCust.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String insert = "INSERT INTO `vrcsoftware`.`customer` " + 
						"(`Customer_ID`, `Customer_FirstName`, `Customer_LastName`, " + 
						"`Customer_Address`, `Customer_PhoneNumber`, `Customer_Email`) " + 
						"VALUES (NULL, '" + txtFN.getText().toString() + "', '" + 
						txtLN.getText().toString() + "', '" + 
						txtAddress.getText().toString() + "', '" + 
						txtPhone.getText().toString() + "', '" + 
						txtEmail.getText().toString() + "');";
				
				try{
					stat.executeUpdate(insert);
					JOptionPane.showMessageDialog(null, "New Customer Added");
					frmCust.setVisible(false);
				} catch(SQLException se){
					JOptionPane.showMessageDialog(null, se);
				}
			}
		});
	}
	
	public void openFrame(){
		frmCust.setVisible(true);
	}
}
