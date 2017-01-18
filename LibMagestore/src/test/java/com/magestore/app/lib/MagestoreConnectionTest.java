package com.magestore.app.lib;

import com.google.gson.stream.JsonReader;
import com.magestore.app.lib.connection.Connection;
import com.magestore.app.lib.connection.ResultReading;
import com.magestore.app.lib.connection.Statement;
import com.magestore.app.lib.connection.http.MagestoreConnection;

import org.json.JSONObject;
import org.junit.Test;

/**
 * Created by Mike on 12/12/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class MagestoreConnectionTest {
    @Test
    public void connection_isCorrect() throws Exception {
        /*
        import java.sql.*;
        class MysqlCon{
        public static void main(String args[]){
        try{
        Class.forName("com.mysql.jdbc.Driver");
        Connection con=DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/sonoo","root","root");
        //here sonoo is database name, root is username and password
        Statement stmt=con.createStatement();
        ResultReading rs=stmt.executeQuery("select * from emp");
        while(rs.next())
        System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
        con.close();
        }catch(Exception e){ System.out.println(e);}
        }
        }
         */

        String strQuery = "/products?searchCriteria[pageSize]=300&searchCriteria[currentPage]=${currentPage}&session=${sessionID}";
        Connection connection = MagestoreConnection.getConnection("http://demo-magento2.magestore.com/webpos/rest/default/V1/webpos", "", "");
        Statement stmt = connection.createStatement();
        stmt.prepareQuery(strQuery);
        stmt.setParam("currentPage", 1);
        stmt.setParam("sessionID", "ab8vimirgfi6qt7t6pvj827a46");
        ResultReading rs = stmt.execute();
        String str = rs.readResult2String();
        rs.close();
        stmt.close();
        connection.close();

        JSONObject obj = new JSONObject("");
        JsonReader reader;
//        reader.
//        com.google.gson.stream.JsonReader reader = new JsonReader();
//        stmt.prepareQuery("/contacts/name/${name}/age/${age}");
//        stmt.setParams("name", "mike");
//        stmt.setParams("age", "30");
//        stmt.execute();
    }

    public void getProduct_isCorrect() throws Exception {

    }
}
