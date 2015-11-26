import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

public class Temp {

	private JFrame tempFrm;
	private JTable tblDetails;
	private JButton btnSell;
	private JButton btnSave;
	private JButton btnCommitSlip;
	private JButton btnTransSlip;
	private Connection conn;
	private Statement stat;
	private ResultSet res;
	private String query = "";
	private String[] propty = {"Property ID", "Property Type", "Property Address", "Agent ID", "Listing Price ($)", 
			"Sold", "Number Of Bedrooms", "Number Of Bathrooms", "Lot Size", "Finished Floor Size"};
	private String[] agents = {"Agent_ID", "Agent_FirstName", "Agent_LastName", "Agent_Company", "Agent_Address", 
			"Agent_PhoneNumber", "Agent_Email", "Password"};
	private String[] trans = {"No.", "Date", "Price", "Property ID", "Agent ID", "Customer ID"};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/*Temp window = new Temp();
					window.tempFrm.setVisible(true);*/
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
	public Temp() {
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
		
		tempFrm = new JFrame();
		tempFrm.setBounds(100, 100, 240, 350);
		tempFrm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tempFrm.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 200, 239);
		tempFrm.getContentPane().add(scrollPane);
		
		tblDetails = new JTable();
		tblDetails.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
			}
		));
		scrollPane.setViewportView(tblDetails);
		
		btnSave = new JButton("");
		btnSave.setIcon(new ImageIcon(Temp.class.getResource("/iconimg/save.png")));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch(query){
				case "updatePropty":
					updatePropty();
					break;
				case "addPropty":
					insertPropty();
					break;
				case "updateAgent":
					updateAgent();
					break;
				case "addAgent":
					insertAgent();
					break;
				}
			}
		});
		btnSave.setBounds(168, 261, 16, 16);
		tempFrm.getContentPane().add(btnSave);
		
		JButton btnDone = new JButton("");
		btnDone.setIcon(new ImageIcon(Temp.class.getResource("/iconimg/check.png")));
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tempFrm.setVisible(false);
				VRCsoftware vrc = new VRCsoftware();
				vrc.clrFrame();
				vrc.openFrame();
			}
		});
		btnDone.setBounds(194, 261, 16, 16);
		tempFrm.getContentPane().add(btnDone);
		
		btnSell = new JButton("Sell");
		btnSell.setVisible(false);
		btnSell.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Trans trans = new Trans();
				trans.openFrame();
				trans.showCust();;
				trans.set_propID(tblDetails.getModel().getValueAt(0, 1));
				trans.set_agentID(tblDetails.getModel().getValueAt(3, 1));
				trans.set_price(tblDetails.getModel().getValueAt(4, 1));
			}
		});
		btnSell.setBounds(10, 261, 63, 23);
		tempFrm.getContentPane().add(btnSell);
		
		btnCommitSlip = new JButton("Commition Slip");
		
		
		btnCommitSlip.setBounds(10, 261, 81, 40);
		tempFrm.getContentPane().add(btnCommitSlip);
		btnCommitSlip.setVisible(false);
		
		btnTransSlip = new JButton("Transaction Slip");
		btnTransSlip.setBounds(101, 261, 81, 40);
		tempFrm.getContentPane().add(btnTransSlip);
		btnTransSlip.setVisible(false);
	}
	
	public void openFrm(){
		tempFrm.setVisible(true);
	}
	
	public void hideBtn(){
		btnSave.setVisible(false);
	}
	
	private void tblEditable1(){
		tblDetails.setModel(new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			
			public boolean isCellEditable(int row, int column){
				if(row == 0 || column == 0)
					return false;
				return true;
			}});
	}
	
	private void tblEditable2(){
		tblDetails.setModel(new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			
			public boolean isCellEditable(int row, int column){
				if(column == 0)
					return false;
				return true;
			}});
	}
	
	private void tblEditable3(){
		tblDetails.setModel(new DefaultTableModel(){
			private static final long serialVersionUID = 1L;

			
			public boolean isCellEditable(int row, int column){
				if(column == 1 && row == 4)
					return false;
				return true;
			}});
	}
	
	public void showSellBtn(){
		btnSell.setVisible(true);
	}
	
	public void proptyDetails(int id){
		tblEditable1();
		query = "updatePropty";
		DefaultTableModel mod = (DefaultTableModel) tblDetails.getModel();
		mod.addColumn("Property", propty);
		String find = "select * from property where Property_ID = " + id + ";";
		Object[] details = new Object[propty.length];
		try{
			res = stat.executeQuery(find);
			while(res.next())
				for(int i = 0; i < propty.length; i++)
					details[i] = res.getObject(i+1);
		} catch(SQLException se){
			JOptionPane.showMessageDialog(null, se);
		}
		if(details[5].toString() == "true")
			btnSell.setVisible(false);
		mod.addColumn("value", details);
	}
	
	public void agentDetails(int id){
		tblEditable1();
		query = "updateAgent";
		DefaultTableModel mod = (DefaultTableModel) tblDetails.getModel();
		mod.addColumn("Property", agents);
		String find = "select * from agent where `Agent_ID` = " + id + ";";
		Object[] details = new Object[propty.length];
		try{
			res = stat.executeQuery(find);
			while(res.next()){
				for(int i = 0; i < agents.length; i++)
					details[i] = res.getObject(i+1);
			}
		} catch(SQLException se){
			JOptionPane.showMessageDialog(null, se);
		}
		mod.addColumn("value", details);
	}
	
	public void transDetails(final int id){
		DefaultTableModel mod = (DefaultTableModel) tblDetails.getModel();
		mod.addColumn("Property", trans);
		final String find = "select * from transaction where Trans_ID = " + id + ";";
		Object[] details = new Object[trans.length];
		 
		
		try{
			res = stat.executeQuery(find);
			while(res.next()){
				
				for(int i = 0; i < trans.length; i++)
					details[i] = res.getObject(i+1);
			}} catch(SQLException se){
			JOptionPane.showMessageDialog(null, se);
		}
		mod.addColumn("value", details);
		btnCommitSlip.setVisible(true);
		btnTransSlip.setVisible(true);
		
		btnTransSlip.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				
				String TransactionSlip = "";
				
				try {					
					res = stat.executeQuery(find);
					while(res.next()){
						TransactionSlip = res.getString("Commition_Slip");						
							
					}
					
				     if (Desktop.isDesktopSupported()) {
				       Desktop.getDesktop().open(new File(TransactionSlip));
				     }
				   } catch (IOException ioe) {
				     ioe.printStackTrace();
				  }catch(SQLException se){
						JOptionPane.showMessageDialog(null, se);
					}
				
				
				
			}
		
		});
		
		btnCommitSlip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String find = "select * from transaction where Trans_ID = " + id + ";";
				String CommisionSlip = "";
				
				try {					
					res = stat.executeQuery(find);
					while(res.next()){
						CommisionSlip = res.getString("Commition_Slip");						
							
					}
					
				     if (Desktop.isDesktopSupported()) {
				       Desktop.getDesktop().open(new File(CommisionSlip));
				     }
				   } catch (IOException ioe) {
				     ioe.printStackTrace();
				  }catch(SQLException se){
						JOptionPane.showMessageDialog(null, se);
					}
				
				
				
				
			}
		});
		
		
	}
	
	public void addPropty(){
		tblEditable2();
		tblEditable3();
		query ="addPropty";
		DefaultTableModel mod = (DefaultTableModel) tblDetails.getModel();
		mod.addColumn("Property");
		for(int i = 1; i < propty.length; i ++){
			mod.addRow(new String[]{
					propty[i]
			});
		}
		mod.addColumn("value");
		mod.setValueAt(false, 4, 1);
	}
	
	public void addAgent(){
		tblEditable2();
		query ="addAgent";
		DefaultTableModel mod = (DefaultTableModel) tblDetails.getModel();
		mod.addColumn("Property");
		for(int i = 1; i < agents.length; i ++){
			mod.addRow(new String[]{
					agents[i]
			});
		}
		mod.addColumn("value");
	}
	
	private void insertPropty(){
		Object[] details = new Object[propty.length - 1];
		for(int i = 0; i < details.length; i ++){
			details[i] = tblDetails.getModel().getValueAt(i, 1);
		}
		String insert = "insert into property " + 
		"(Property_Type, Property_Address, Agent_ID, Listing_Price, Status, " + 
		"Number_Of_Bedrooms, Number_Of_Bathrooms, Lot_Size, Finished_Floor_Size, Date) values " + 
		"('" + details[0] + "', '" + details[1] + "', " + details[2] + ", " + 
		details[3] + ", " + details[4] + ", " + details[5] + ", " + 
		details[6] +  ", " + details[7] + ", " + details[8] + ", '" + 
		LocalDate.now().toString() + "');";
		
		try{
			stat.executeUpdate(insert);
			JOptionPane.showMessageDialog(null, "Record Added");
		} catch(SQLException se){
			JOptionPane.showMessageDialog(null, se);
		} catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(null, "Please input a valid value");
		}
	}
	
	private void insertAgent(){
		Object[] details = new Object[agents.length - 1];
		for(int i = 0; i < details.length; i ++){
			details[i] = tblDetails.getModel().getValueAt(i, 1);
		}
		String insert = "INSERT INTO `vrcsoftware`.`agent` (" + 
		"`Agent_ID`, `Agent_FirstName`, `Agent_LastName`, `Agent_Company`, " + 
		"`Agent_Address`, `Agent_PhoneNumber`, `Agent_Email`, `Password`) " + 
		"VALUES (NULL, ";
		for(int i = 0; i < details.length - 1; i++)
			insert += "'" + details[i] + "', ";
		insert += "'" + details[details.length - 1] + "');";
		
		try{
			stat.executeUpdate(insert);
			JOptionPane.showMessageDialog(null, "Record Added");
		} catch(SQLException se){
			JOptionPane.showMessageDialog(null, se);
		}
	}
	
	private void updatePropty(){
		Object[] details = new Object[propty.length];
		for(int i = 0; i < propty.length; i ++){
			details[i] = tblDetails.getModel().getValueAt(i, 1);
		}
		String updateQuery = "update property set " + 
		"Property_Type = '" + details[1] + "', " + 
		"Property_Address = '" + details[2] + "', " + 
		"Agent_ID = " + details[3] + ", " + 
		"Listing_Price = " + details[4] + ", " + 
		"Status = " + details[5] + ", " + 
		"Number_Of_Bedrooms = " + details[6] + ", " + 
		"Number_Of_Bathrooms = " + details[7] + ", " + 
		"Lot_Size = " + details[8] +  ", " + 
		"Finished_Floor_Size = " + details[9] + 
		" where Property_ID = " + details[0] + ";";
		
		try{
			stat.executeUpdate(updateQuery);
			JOptionPane.showMessageDialog(null, "Details Updated");
		} catch(SQLException se){
			JOptionPane.showMessageDialog(null, se);
		} catch(NumberFormatException nfe){
			JOptionPane.showMessageDialog(null, nfe.getCause());
		}
	}
	
	private void updateAgent(){
		Object[] details = new Object[agents.length];
		for(int i = 0; i < agents.length; i ++){
			details[i] = tblDetails.getModel().getValueAt(i, 1);
		}
		String updateQuery = "UPDATE `vrcsoftware`.`agent` " + 
		"SET `Agent_FirstName` = '" + details[1] + "', " + 
		"`Agent_LastName` = '" + details[2] + "', " + 
		"`Agent_Company` = '" + details[3] + "', " + 
		"`Agent_Address` = '" + details[4] + "', " + 
		"`Agent_PhoneNumber` = '" + details[5] + "', " + 
		"`Agent_Email` = '" + details[6] + "', " + 
		"`Password` = '" + details[7] + "' " + 
		"WHERE `agent`.`Agent_ID` = " + details[0] + ";";
		
		try{
			stat.executeUpdate(updateQuery);
			JOptionPane.showMessageDialog(null, "Details Updated");
		} catch(SQLException se){
			JOptionPane.showMessageDialog(null, se);
		}
	}
}
