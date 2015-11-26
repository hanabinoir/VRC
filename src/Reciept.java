import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;



public class Reciept {
	
	private static final String Filename = "MotnhlyProfit.doc";

	Connection conn;
	
	Statement stat;
	
	ResultSet res,res2;
	
	
	public String CreateMonthlyProfit(){
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/vrcsoftware","root","");
			stat = (Statement) conn.createStatement();
			
		}catch(Exception e){
			System.out.println(e);
		}
		
		
		int i = 0;
		int y = 0;
		
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		month++;
		
		BufferedWriter writer = null;
		
		String findAgent = "select * from agent; ";
		
		
		try{
			 res = stat.executeQuery(findAgent);
			 
			 while(res.next() != false){					
				 
				 	i++;
				}
			 
			 
			 
			 File logFile = new File(Filename);
			 
			 writer = new BufferedWriter(new FileWriter(logFile));
			 
			 writer.write("Victor Reality Company Monthly Profit");
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 
			 writer.write("===================================================");
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 
			 
			 writer.write("No of agents: " + i);
			 writer.write(System.getProperty("line.separator"));
			 writer.write("Monthly Fees From Agents(No of Agents x $1000) : $" + i * 1000);
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 
			 String FindTrans = "select * from transaction; ";
			 	res = stat.executeQuery(FindTrans);
			 
			 		while(res.next() != false){					
			 		String transDate = res.getString("Trans_Date");
			 		
			 		String[] parts = transDate.split("-");
			 		
			 					 		
			 		
			 		if(Integer.parseInt(parts[1]) == month){
			 			y++;
			 			
			 		}
				 	
			 		}
			 		
			 writer.write("No of Transaction This Month: " + y);
			 writer.write(System.getProperty("line.separator"));
			 writer.write("income from Transaction Happen this month(No of Transaction x $250) :$" +  y*250 );
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 writer.write("===================================================");
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 writer.write("Total Profits: $" + (y*250 +  i * 1000) );
			 
			 
			 
			 
			 
			 
			 
			 
			 
			 writer.close();
			 
			 
		}
		catch (Exception e)
		{
			
			System.out.println( e);
			
		}
		
		
		
		
		
		
		
		
		return Filename;
		
		
		
		
		
		
		
		
		
		
		
	}

	
	public String CreateUnsoldProperties(){
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/vrcsoftware","root","");
			stat = (Statement) conn.createStatement();
			
		}catch(Exception e){
			System.out.println(e);
		}
		
		BufferedWriter writer = null;
		 
		String FileName = "UnsoldProperties.doc"; 
		
		 try{
			 
			 File logFile = new File(FileName);
			 
			 writer = new BufferedWriter(new FileWriter(logFile));
			 
			 writer.write("Victor Reality Company Unsold Properties");
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 
			 writer.write("===================================================");
			 String findPropty = "select * from property where Status = " + 1 + ";";
			 
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 
			 
			 try{
				 res = stat.executeQuery(findPropty);
				 
				 int i = 1;
				 
			
				 
				 writer.write(String.format("%5s %20s %25s %20s \r\n","","Property Type","Address","Price" ));
					
				 writer.write(System.getProperty("line.separator"));
				 writer.write(System.getProperty("line.separator"));
				 
				 while(res.next() != false){					
					 writer.write(System.getProperty("line.separator"));
					// writer.write(i + "\t" + res.getString("Property_Type") + "\t" + res.getString("Property_Address") + "\t" + " $" + res.getString("Listing_Price"));
					 writer.write(String.format("%5s %20s %25s %20s \r\n",i,res.getString("Property_Type"),res.getString("Property_Address"),"$" +res.getString("Listing_Price") ));
					 writer.write(System.getProperty("line.separator"));
					 	i++;
					}
				 
				 writer.close();
				 
			 }catch(Exception ec){
						System.out.println(ec.getStackTrace() + ec.getMessage());
					} 
			 
			 
			 
		 }catch(Exception e){
				System.out.println(e.getStackTrace() + e.getMessage());
			}
		 
		
		
		
		
		
		return FileName;
		
		
		
		
		
	}
	
	
	
	
	
	public String createTransactionReciept(int Agent_id,int Property_Id,int customer_ID ){
		
		
		
		
		
Reciept r = new Reciept();
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/vrcsoftware","root","");
			stat = (Statement) conn.createStatement();
			
		}catch(Exception e){
			System.out.println(e);
		}
		
		 BufferedWriter writer = null;
		 
		 String FileName = "Customer_" + String.valueOf(Agent_id) + "Property_" + String.valueOf(Property_Id) + "agent_" + String.valueOf(customer_ID);
		 
		 try{
			 
			 File logFile = new File(FileName + ".doc");
			 
			 writer = new BufferedWriter(new FileWriter(logFile));
			 
			// String formatStr = "%-20s %-15s %-15s %-15s %-15s%n";
			 
			 
			 
			 
			 writer.write("Victor Reality Company Transaction Slip");
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 writer.write("==============================================");
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 String findCust = "SELECT * FROM `customer` where Customer_ID = " + customer_ID + ";";
			 String findPropty = "select * from property where Property_ID = " + Property_Id + ";";
			 int price = 5;
			 String CustName = "";
			 
			 try{
				 res = stat.executeQuery(findCust);
				 
				 
				 while(res.next() != false){					
								 
					 CustName = res.getString("Customer_FirstName") + " " + res.getString("Customer_LastName");
						
					}		
				 
				 res2 = stat.executeQuery(findPropty);
				 
				 while(res2.next() != false){
					 
					 
					 price = res2.getInt("Listing_Price");
					 
					 
				 }
				 
				 
			 }
			 catch(Exception e){
				 
				 System.out.println(e.getMessage());
				 
			 }
			 
			 writer.write("Customer  Name: " + CustName + " || " + "Customer Id :" + customer_ID);
			 writer.write(System.getProperty("line.separator"));			 
			 writer.write(System.getProperty("line.separator"));
			 writer.write("Transaction Slip For Propertry no : " + Property_Id);
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 writer.write("Price for The Property : $" + price);
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 writer.write("Commision : $" + r.commision(price));
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 writer.write("GST : $" + 0.05 * r.commision(price));
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 writer.write("Total payable : $" + (r.commision(price) + 0.05 * r.commision(price))); 
			 
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 writer.write("Amount recivable(Property Price - Amount payable) : $" + (price - (r.commision(price) + 0.05 * r.commision(price)))); 
			 
			
			 
			
			 writer.close();


			 
			 
			 
		 }
		catch(Exception e){
			
			
		}
		
		
		
		
		
		
		
		return String.valueOf(FileName + ".doc");
	
		
		
		
		
	}
	
	
	public String CreateCommisionSlip(int Agent_id,int Property_Id,int customer_ID){
		
		Reciept r = new Reciept();
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/vrcsoftware","root","");
			stat = (Statement) conn.createStatement();
			
		}catch(Exception e){
			System.out.println(e);
		}
		
		 BufferedWriter writer = null;
		 
		 String FileName = "agent_" + String.valueOf(Agent_id) + "Property_" + String.valueOf(Property_Id) + "Customer_" + String.valueOf(customer_ID);
		 
		 try{
			 
			 File logFile = new File(FileName + ".doc");
			 
			 writer = new BufferedWriter(new FileWriter(logFile));
			 
			// String formatStr = "%-20s %-15s %-15s %-15s %-15s%n";
			 
			 
			 
			 
			 writer.write("Victor Reality Company Commision Slip");
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 writer.write("==============================================");
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 String findAgent = "select Agent_ID, Agent_LastName,Agent_FirstName from agent where Agent_ID = " + Agent_id + ";";
			 String findPropty = "select * from property where Property_ID = " + Property_Id + ";";
			 int price = 5;
			 String AgentName = "";
			 
			 try{
				 res = stat.executeQuery(findAgent);
				 
				 
				 while(res.next() != false){					
								 
					 AgentName = res.getString("Agent_FirstName") + " " + res.getString("Agent_LastName");
						
					}		
				 
				 res2 = stat.executeQuery(findPropty);
				 
				 while(res2.next() != false){
					 
					 
					 price = res2.getInt("Listing_Price");
					 
					 
				 }
				 
				 
			 }
			 catch(Exception e){
				 
				 System.out.println(e.getMessage());
				 
			 }
			 
			 writer.write("Agent Name: " + AgentName + " || " + "Agent Id :" + Agent_id);
			 writer.write(System.getProperty("line.separator"));			 
			 writer.write(System.getProperty("line.separator"));
			 writer.write("Commision Slip For Propertry no : " + Property_Id);
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 writer.write("Price for The Property : $" + price);
			 writer.write(System.getProperty("line.separator"));
			 writer.write(System.getProperty("line.separator"));
			 writer.write("Commision : $" + r.commision(price));
			 
			 
			 
			 
			 
			 
			 
			
			 
			
			 writer.close();


			 
			 
			 
		 }
		catch(Exception e){
			
			
		}
		 
		
		
		
		
		
		
		
		return String.valueOf(FileName + ".doc");
	}
	
	
	public double commision(int price){
		double res = 0;
		
		if(price <= 100000){
			
			res = 0.07 * price; 
			
		}
		else{
			
			res = (0.07*100000) + (0.03 * (price - 100000));
		}
		
		
		
		return res;
		
	}
	
	
	public static void main(String args[]){
		Reciept r = new Reciept();
		
		
		r.CreateMonthlyProfit();
		
		
		
	}
	
	
	
	

}
