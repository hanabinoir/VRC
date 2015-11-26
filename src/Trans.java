import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

public class Trans {

	private JFrame frmTrans;
	private JTable tblCust;
	private JButton btnCreate;
	private JButton btnNewCust;
	private JLabel lblPropertyId;
	private JLabel lblAgentId;
	private JLabel lblPrice;
	private Connection conn;
	private Statement stat;
	private ResultSet res;
	private int propID, agentID, price;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/*Trans window = new Trans();
					window.frmTrans.setVisible(true);*/
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
	public Trans() {
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
			
		}catch(Exception e){
			System.out.println(e);
		}
		
		frmTrans = new JFrame();
		frmTrans.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmTrans.setTitle("Create Transaction");
		frmTrans.setBounds(100, 100, 285, 335);
		frmTrans.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(7, 98, 252, 153);
		frmTrans.getContentPane().add(scrollPane);
		
		tblCust = new JTable();
		tblCust.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"No.", "Last Name", "First Name"
			}
		));
		scrollPane.setViewportView(tblCust);
		
		btnCreate = new JButton("Submit");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int custID = (int) tblCust.getModel().getValueAt(tblCust.getSelectedRow(), 0);
				
				Reciept r = new Reciept();
				String tans = r.createTransactionReciept(agentID, propID, custID);
				
				String CommisionSlip = r.CreateCommisionSlip(agentID, propID, custID); 
				
				
				
				
			String insert = "INSERT INTO `vrcsoftware`.`transaction` (`Trans_ID`, `Trans_Date`, `Price`, `Proper_ID`, `Agent_ID`, `Cust_ID`, `Commition_Slip`, `Transaction_Slip`) VALUES (NULL, '" + LocalDate.now().toString() + "', '" + price  + "', '" + propID + "', '" + agentID  + "', '" + custID + "','" + CommisionSlip + "', '" + tans + "');";
				try{
					stat.executeUpdate(insert);
					JOptionPane.showMessageDialog(null, "Record Added");
					frmTrans.dispose();
					VRCsoftware vrc = new VRCsoftware();
					vrc.openFrame();
				} catch(SQLException se){
					System.out.println(se);
				}
				try{
					if (Desktop.isDesktopSupported()) {
					       Desktop.getDesktop().open(new File(CommisionSlip));
					       Desktop.getDesktop().open(new File(tans));
					     }
					   } catch (IOException ioe) {
					     ioe.printStackTrace();
					  
					   }
			}
		});
		btnCreate.setBounds(10, 262, 89, 23);
		frmTrans.getContentPane().add(btnCreate);
		
		btnNewCust = new JButton("");
		btnNewCust.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Cust cust = new Cust();
				cust.openFrame();
			}
		});
		btnNewCust.setIcon(new ImageIcon(Trans.class.getResource("/iconimg/add.png")));
		btnNewCust.setBounds(243, 262, 16, 16);
		frmTrans.getContentPane().add(btnNewCust);
		
		lblPropertyId = new JLabel("Property ID: ");
		lblPropertyId.setBounds(7, 11, 92, 14);
		frmTrans.getContentPane().add(lblPropertyId);
		
		lblAgentId = new JLabel("Agent ID: ");
		lblAgentId.setBounds(7, 36, 92, 14);
		frmTrans.getContentPane().add(lblAgentId);
		
		lblPrice = new JLabel("Price: ");
		lblPrice.setBounds(7, 61, 92, 14);
		frmTrans.getContentPane().add(lblPrice);
	}
	
	public void openFrame(){
		frmTrans.setVisible(true);
	}
	
	public void showCust(){
		String findCust = "SELECT `Customer_ID`, `Customer_LastName`, `Customer_FirstName` FROM `customer`;";
		DefaultTableModel mod = (DefaultTableModel) tblCust.getModel();
		
		try{
			res = stat.executeQuery(findCust);
			while(res.next()){
				mod.addRow(new Object[]{
						res.getInt(1), 
						res.getString(2),
						res.getString(3)
				});
			}
		} catch(SQLException se){
			JOptionPane.showMessageDialog(null, se);
		}
	}
	
	public void set_propID(Object i){
		propID = (int) i;
		lblPropertyId.setText(lblPropertyId.getText() + propID);
	}
	
	public void set_agentID(Object i){
		agentID = (int) i;
		lblAgentId.setText(lblAgentId.getText() + agentID);
	}
	
	public void set_price(Object i){
		price = (int) i;
		lblPrice.setText(lblPrice.getText() + "$" + NumberFormat.getInstance().format(price));
	}
}
