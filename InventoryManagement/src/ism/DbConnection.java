package ism;
import java.sql.Connection;
import java.sql.DriverManager;
public class DbConnection {
	private static Connection con;
	
	public static Connection connect() {
		try {
			if(con==null || con.isClosed()) {
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/tbl_product","root","@Shre3029");
			
			}
			return con;
			
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		

	}
	public static void dissconnect() {
		try {
			if(con!=null && !con.isClosed()) {
				con.close();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
