
import java.awt.Desktop;
import java.awt.EventQueue;
import java.sql.*;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JComboBox;
import javax.swing.JLabel;

public class VRCsoftware {

	private JFrame frmVRC;
	private JTable tblProperty;
	private JTextField txtSrchPrpty;
	private JTextField txtSrchTrans;
	private JComboBox<String> cboPropty;
	private JComboBox<String> cboTrans;
	private JTable tblTrans;
	private JTextField txtSrchAgt;
	private JTable tblAgents;
	private Connection conn;
	private Statement stat;
	private ResultSet res;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/*VRCsoftware window = new VRCsoftware();
					window.frmVRC.setVisible(true);*/
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
	public VRCsoftware() {
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
		
		frmVRC = new JFrame();
		frmVRC.setTitle("Victor Reality Company");
		frmVRC.setBounds(100, 100, 450, 325);
		frmVRC.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVRC.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 38, 414, 237);
		frmVRC.getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Properties", null, panel, null);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 42, 365, 156);
		panel.add(scrollPane);
		
		tblProperty = new JTable();
		tblProperty.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Property No.", "Property Address", "Sold (Y/N)"
			}
		){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			
			public boolean isCellEditable(int row, int column){
				return false;
			}
		});
		final DefaultTableModel modPropty = (DefaultTableModel) tblProperty.getModel();
		String findPropty = "select Property_ID, Property_Address, Status from property " + 
				"order by Property_ID;";
		try{
			res = stat.executeQuery(findPropty);
			
			while(res.next() != false){
				modPropty.addRow(new Object[] {
						res.getInt("Property_ID"), 
						res.getString("Property_Address"), 
						res.getBoolean("Status")
				});
			}
		} catch(SQLException se){
			JOptionPane.showMessageDialog(null, se);
		}
		
		tblProperty.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() == 2){
					int id = (int)tblProperty.getModel().getValueAt(tblProperty.getSelectedRow(), 0);
					Temp temp = new Temp();
					temp.openFrm();
					temp.showSellBtn();
					temp.proptyDetails(id);
				}
			}
		});
		
		scrollPane.setViewportView(tblProperty);
		
		txtSrchPrpty = new JTextField();
		txtSrchPrpty.setColumns(10);
		txtSrchPrpty.setBounds(10, 11, 237, 20);
		panel.add(txtSrchPrpty);
		
		JButton btnAddPropty = new JButton("");
		btnAddPropty.setIcon(new ImageIcon(VRCsoftware.class.getResource("/iconimg/add.png")));
		btnAddPropty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Temp temp = new Temp();
				temp.openFrm();
				temp.addPropty();
			}
		});
		btnAddPropty.setBounds(385, 42, 16, 16);
		panel.add(btnAddPropty);
		
		JButton btnDelPropty = new JButton("");
		btnDelPropty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int proptyID = (int) tblProperty.getModel().getValueAt(tblProperty.getSelectedRow(), 0);
				String delete = "delete from property where Property_ID = " + proptyID + ";";
				
				try{
					int row = tblProperty.getSelectedRow();
					stat.execute(delete);
					modPropty.removeRow(row);
					frmVRC.repaint();
					JOptionPane.showMessageDialog(null, "Record Deleted");
				} catch(SQLException se){
					JOptionPane.showMessageDialog(null, se);
				}
			}
		});
		btnDelPropty.setIcon(new ImageIcon(VRCsoftware.class.getResource("/iconimg/delete.png")));
		btnDelPropty.setBounds(385, 69, 16, 16);
		panel.add(btnDelPropty);
		
		JButton btnEditPropty = new JButton("");
		btnEditPropty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int proptyID = (int) tblProperty.getModel().getValueAt(tblProperty.getSelectedRow(), 0);
				Temp temp = new Temp();
				temp.openFrm();
				temp.showSellBtn();
				temp.proptyDetails(proptyID);
			}
		});
		btnEditPropty.setIcon(new ImageIcon(VRCsoftware.class.getResource("/iconimg/edit.png")));
		btnEditPropty.setBounds(385, 182, 16, 16);
		panel.add(btnEditPropty);
		
		cboPropty = new JComboBox<String>();
		cboPropty.setModel(new DefaultComboBoxModel<String>(
				new String[] {
						"Property ID", 
						"By Type", 
						"Minimum Price", 
						"Minimum Rooms", 
						"Minnimum Lot size", 
						"Minimum Finished area"
						}));
		cboPropty.setBounds(292, 11, 109, 20);
		panel.add(cboPropty);
		
		txtSrchPrpty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (modPropty.getRowCount() > 0) {
				    for (int i = modPropty.getRowCount() - 1; i > -1; i--) {
				    	modPropty.removeRow(i);
				    }
				}
				
				String SrchType = cboPropty.getSelectedItem().toString();
				String Srch = txtSrchPrpty.getText().toString().trim();
				String Sql = "select Property_ID, Property_Address, Status from property " + 
						"order by Property_ID;";
				
				if(!Srch.equals("")){
					switch(SrchType){
					case "By Type":
						Sql = "select Property_ID, Property_Address, Status from property " + 
					"where Property_Type like '%" + Srch + "%' " + 
						"order by Property_ID;";
						break;
					case "Minimum Price":
						Sql = "select Property_ID, Property_Address, Status from property " + 
					"where Listing_Price >= '" + Integer.parseInt(Srch) + "' " + 
						"order by Property_ID;";
						break;
					case "Minimum Rooms":
						Sql = "select Property_ID, Property_Address, Status from property " + 
					"where Number_Of_Bedrooms >= '" + Integer.parseInt(Srch) + "' " + 
						"order by Property_ID;";
						break;
					case "Minnimum Lot size":
						Sql = "select Property_ID, Property_Address, Status from property " + 
					"where Lot_Size >= '" + Integer.parseInt(Srch) + "' " + 
						"order by Property_ID;";
						break;
					case "Minimum Finished area":
						Sql = "select Property_ID, Property_Address, Status from property " + 
					"where Finished_Floor_Size >= '" + Integer.parseInt(Srch) + "' " + 
						"order by Property_ID;";
						break;
					case "Property ID":
						Sql = "select Property_ID, Property_Address, Status from property " + 
					"where Property_ID = '" + Integer.parseInt(Srch) + "' " + 
						"order by Property_ID;";
						break;
					}
				}
				
				try{
					res = stat.executeQuery(Sql);
					
					while(res.next() != false){
						modPropty.addRow(new Object[] {
								res.getInt("Property_ID"), 
								res.getString("Property_Address"), 
								res.getBoolean("Status")
						});
					}
				} catch(SQLException se){
					JOptionPane.showMessageDialog(null, se);
				}
				
				
			}
		});

		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Agents", null, panel_1, null);
		panel_1.setLayout(null);
		
		txtSrchAgt = new JTextField();
		txtSrchAgt.setColumns(10);
		txtSrchAgt.setBounds(152, 12, 247, 20);
		panel_1.add(txtSrchAgt);
		
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 43, 363, 155);
		panel_1.add(scrollPane_2);
		
		tblAgents = new JTable();
		tblAgents.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() == 2){
					int id = (int) tblAgents.getModel().getValueAt(tblAgents.getSelectedRow(), 0);
					Temp temp = new Temp();
					temp.openFrm();
					temp.agentDetails(id);
				}
			}
		});
		tblAgents.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Agent ID", "Agent Name"
			}
		){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			
			public boolean isCellEditable(int row, int column){
				return false;
			}
		});
		final DefaultTableModel modAgent = (DefaultTableModel) tblAgents.getModel();
		String findAgent = "select Agent_ID, Agent_LastName from agent;";
		try{
			res = stat.executeQuery(findAgent);
			
			while(res.next() != false){
				modAgent.addRow(new Object[] {
						res.getInt("Agent_ID"), 
						res.getString("Agent_LastName")
				});
			}
		} catch(SQLException se){
			JOptionPane.showMessageDialog(null, se);
		}
		scrollPane_2.setViewportView(tblAgents);
		txtSrchAgt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {		
				
				if (modAgent.getRowCount() > 0) {
				    for (int i = modAgent.getRowCount() - 1; i > -1; i--) {
				    	modAgent.removeRow(i);
				    }
				}
				String Srch = txtSrchAgt.getText().toString();
				
				
				
                 String Sql = "select Agent_ID, Agent_LastName from agent;";
				
				if(!Srch.equals("")){						
					
					Sql = "select Agent_ID, Agent_LastName from agent where Agent_ID = '" +  Srch +"';";		
				
			}
				try{
					res = stat.executeQuery(Sql);
					
					while(res.next() != false){
						modAgent.addRow(new Object[] {
								res.getInt("Agent_ID"), 
								res.getString("Agent_LastName")
						});
					}
				} catch(SQLException se){
					JOptionPane.showMessageDialog(null, se);
				}	
			}
		});

		
		JButton btnAddAgent = new JButton("");
		btnAddAgent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Temp temp = new Temp();
				temp.openFrm();
				temp.addAgent();
				frmVRC.repaint();
			}
		});
		btnAddAgent.setIcon(new ImageIcon(VRCsoftware.class.getResource("/iconimg/add.png")));
		btnAddAgent.setBounds(383, 42, 16, 16);
		panel_1.add(btnAddAgent);
		
		JButton btnDelAgent = new JButton("");
		btnDelAgent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int agentID = (int) tblAgents.getModel().getValueAt(tblAgents.getSelectedRow(), 0);
				String selPropty = "select * from `property` where Agent_ID = " + agentID + ";";
				String delete = "delete from agent where Agent_ID = " + agentID + ";";
				
				try{
					res = stat.executeQuery(selPropty);
					if(res.next())
						JOptionPane.showMessageDialog(null, "There are properties under agent " + agentID);
					else{
						int row = tblAgents.getSelectedRow();
						stat.execute(delete);
						modAgent.removeRow(row);
						frmVRC.repaint();
						JOptionPane.showMessageDialog(null, "Record Deleted");
					}
					/*
					 * int row = tblAgents.getSelectedRow();
					stat.execute(delete);
					modAgent.removeRow(row);
					frmVRC.repaint();
					JOptionPane.showMessageDialog(null, "Record Deleted");
					 */
				} catch(SQLException se){
					JOptionPane.showMessageDialog(null, se);
				}
			}
		});
		btnDelAgent.setIcon(new ImageIcon(VRCsoftware.class.getResource("/iconimg/delete.png")));
		btnDelAgent.setBounds(383, 69, 16, 16);
		panel_1.add(btnDelAgent);
		
		JButton btnEditAgent = new JButton("");
		btnEditAgent.setIcon(new ImageIcon(VRCsoftware.class.getResource("/iconimg/edit.png")));
		btnEditAgent.setBounds(383, 182, 16, 16);
		panel_1.add(btnEditAgent);
		
		JLabel lblSearchById = new JLabel("Search by id: ");
		lblSearchById.setBounds(10, 15, 99, 14);
		panel_1.add(lblSearchById);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Transactions", null, panel_2, null);
		panel_2.setLayout(null);
		
		txtSrchTrans = new JTextField();
		txtSrchTrans.setColumns(10);
		txtSrchTrans.setBounds(10, 12, 270, 20);
		panel_2.add(txtSrchTrans);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 43, 389, 155);
		panel_2.add(scrollPane_1);
		
		tblTrans = new JTable();
		tblTrans.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					int id = (int) tblTrans.getModel().getValueAt(tblTrans.getSelectedRow(), 0);
					Temp temp = new Temp();
					temp.openFrm();
					temp.transDetails(id);
					temp.hideBtn();
				}
			}
		});
		tblTrans.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Transaction No.", "Price", "Date"
			}
		){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			
			public boolean isCellEditable(int row, int column){
				return false;
			}
		});
		final DefaultTableModel modTrans = (DefaultTableModel) tblTrans.getModel();
		String findTrans = "select Trans_ID, Price, Trans_Date from transaction;";
		try{
			res = stat.executeQuery(findTrans);
			
			while(res.next() != false){
				modTrans.addRow(new Object[] {
						res.getInt("Trans_ID"), 
						res.getString("Price"),
						res.getDate("Trans_Date")
				});
			}
		} catch(SQLException se){
			JOptionPane.showMessageDialog(null, se);
		}
		scrollPane_1.setViewportView(tblTrans);
		
		cboTrans = new JComboBox<String>();
		cboTrans.setModel(new DefaultComboBoxModel<String>(
				new String[] {
						"Trans ID", 
						"Date", 
						"Mininum Price", 
						"Property ID", 
						"Agent ID", 
						"Cust ID"}));
		cboTrans.setBounds(290, 12, 109, 20);
		panel_2.add(cboTrans);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Reports", null, panel_3, null);
		panel_3.setLayout(null);
		
		JButton btnProperiesUnsold = new JButton("Unsold Properties");
		btnProperiesUnsold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
			}
		});
		panel_3.add(btnProperiesUnsold);
		
		JButton btnUnsoldProperties = new JButton("Unsold Properties");
		btnUnsoldProperties.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Reciept r = new Reciept();
				
				
				String UNsold =	r.CreateUnsoldProperties().toString();
				
				
					try{
						if (Desktop.isDesktopSupported()){
						Desktop.getDesktop().open(new File(UNsold));
						}
					}catch(Exception ed){
						System.out.println(ed.getMessage());
						
					}
				
				
			}
		});
		btnUnsoldProperties.setBounds(20, 71, 166, 23);
		panel_3.add(btnUnsoldProperties);
		
		JButton btnNewButton = new JButton("Monthly Profit");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					Reciept r = new Reciept();
				
				
				String Mreport =	r.CreateMonthlyProfit().toString();
				
				
					try{
						if (Desktop.isDesktopSupported()){
						Desktop.getDesktop().open(new File(Mreport));
						}
					}catch(Exception ed){
						System.out.println(ed.getMessage());
						
					}
				
				
			}
		});
		btnNewButton.setBounds(231, 71, 144, 23);
		panel_3.add(btnNewButton);

		
	
		
		
		txtSrchTrans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (modTrans.getRowCount() > 0) {
				    for (int i = modTrans.getRowCount() - 1; i > -1; i--) {
				    	modTrans.removeRow(i);
				    }
				}
				
				String SrchType = cboTrans.getSelectedItem().toString();
				String Srch = txtSrchTrans.getText().toString().trim();
				String Sql = "select Trans_ID, Price, Trans_Date from transaction;";
				
				if(!Srch.equals("")){
					switch(SrchType){
					case "Trans ID":
						Sql = "select Trans_ID, Price, Trans_Date from transaction " + 
					"where Trans_ID = " + Integer.parseInt(Srch);
						break;
					case "Date":
						Sql = "select Trans_ID, Price, Trans_Date from transaction " + 
					"where Trans_Date = '" + Srch + "'";
						break;
					case "Minimum Price":
						Sql = "select Trans_ID, Price, Trans_Date from transaction " + 
					"where Price >= " + Integer.parseInt(Srch);
						break;
					case "Property ID":
						Sql = "select Trans_ID, Price, Trans_Date from transaction " + 
					"where Property_ID = " + Integer.parseInt(Srch);
						break;
					case "Agent ID":
						Sql = "select Trans_ID, Price, Trans_Date from transaction " + 
					"where Agent_ID = " + Integer.parseInt(Srch);
					case "Cust ID":
						Sql = "select Trans_ID, Price, Trans_Date from transaction " + 
					"where Cust_ID = " + Integer.parseInt(Srch);
						break;
					}
				}
				
				try{
					res = stat.executeQuery(Sql);
					
					while(res.next() != false){
						modTrans.addRow(new Object[] {
								res.getInt("Trans_ID"), 
								res.getString("Price"),
								res.getDate("Trans_Date")
						});
					}
				} catch(SQLException se){
					JOptionPane.showMessageDialog(null, se);
				}
			}
		});
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 60, 21);
		frmVRC.getContentPane().add(menuBar);
		
		JMenu mnSystem = new JMenu("System");
		menuBar.add(mnSystem);
		mnSystem.setMnemonic('Q');
		
		JMenuItem mntmSwitchAccount = new JMenuItem("Switch account");
		mntmSwitchAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmVRC.dispose();
				Login login = new Login();
				login.openFrame();
			}
		});
		mnSystem.add(mntmSwitchAccount);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		JMenuItem mntmNewCustomer = new JMenuItem("New Customer");
		mntmNewCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Cust cust = new Cust();
				cust.openFrame();
			}
		});
		mnSystem.add(mntmNewCustomer);
		mnSystem.add(mntmExit);
	}
	
	public void openFrame(){
		frmVRC.setVisible(true);
	}
	
	public void clrFrame(){
		frmVRC.dispose();
	}
}
