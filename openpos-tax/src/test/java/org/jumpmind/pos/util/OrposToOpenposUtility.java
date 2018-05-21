package org.jumpmind.pos.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.cj.jdbc.Driver;

public class OrposToOpenposUtility {

    public static void main(String[] args) throws Exception {
        DriverManager.registerDriver(new Driver());
        Connection c = null;
        try {
            c = DriverManager.getConnection("jdbc:mysql://localhost/registerdb?serverTimezone=UTC",
                    "root", "!B@ng!");

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from  pa_athy_tx");
            while (rs.next()) {
                int id = rs.getInt("ID_ATHY_TX");
                String name = rs.getString("NM_ATHY_TX");
                System.out.println("id=" + id + ", name=" + name);
            }
                        
            rs.close();
            stmt.close();
        } finally {
            if (c != null) {
                c.close();
            }
        }

    }

}
