/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Conexion {
   
   private Connection cn;
   
   public void conectar() throws SQLException {
       
       try{
           //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
           String url = "jdbc:mysql://localhost:3306/autorepuesto?autoReconnect=true&useSSL=false";
           cn = DriverManager.getConnection(url, "root", "leo12345");
           //cn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=Autorepuestos;user=sa;password=leo12345");
       }
       catch(Exception e){
           //System.out.println(e.getMessage());
           System.out.println("Chisjsksks");
       }
   }

    public Connection getConexion() {
        return cn;
    }
   
   public void desconectar(){
       if(cn!=null)
           try {
               cn.close();
       } catch (SQLException ex) {
           Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
   
}
