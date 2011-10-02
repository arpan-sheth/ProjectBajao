package test.com.servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import com.database.DBConnectionManager;

public class TestServlets {
	private static DBConnectionManager connMgr;
	public static void main(String args[]){
        connMgr = DBConnectionManager.getInstance();
        Connection con = connMgr.getConnection("mysql");
        if (con == null) {
           System.out.println("Can't get connection");
            return;
        }
        ResultSet rs = null;
        ResultSetMetaData md = null;
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("select * from music.tags");
            md = rs.getMetaData();
            System.out.println("<H1>Employee data</H1>");
            while (rs.next()) {
            	System.out.println("<BR>");
                for (int i = 1; i < md.getColumnCount(); i++) {
                	System.out.print(rs.getString(i) + ", ");
                }
            }
            stmt.close();
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        connMgr.freeConnection("mysql", con);
	}
}
