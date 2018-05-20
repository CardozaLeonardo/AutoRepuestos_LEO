/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;



/**
 *
 * @author nelson
 */
public class TCliente {
    
    private int idCliente;
    private String nombres;
    private String cedula;
    private String apellidos;
    private String sexo;
    private ResultSet rs;
    private Statement st;
    private Conexion con;
    private Connection miConex;
    
    public TCliente(int idCliente, String nombres, String apellidos, String cedula, String sexo) {
        this.idCliente = idCliente;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.sexo = sexo;
    }
    
    

     public TCliente() throws SQLException{
        con = new Conexion();
        con.conectar();
        miConex = con.getConexion();
        this.cargarDatos();
        this.actualizarEstado();
         
    }
     
      public void guardar(String nombre, String apellido, String cedula, String sexo){
        PreparedStatement sp;
        try {
            sp = this.miConex.prepareStatement("insert into Cliente(nombres,apellidos, cedula, sexo) values (?,?,?,?)");
            sp.setString(1,nombre);
            sp.setString(2,apellido);
            sp.setString(3, cedula);
            sp.setString(4, sexo);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Cliente guardado existosamente!",
                        "Nuevo Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            this.actualizar();
            rs.last();
            this.actualizarEstado();
        } catch (SQLException ex) {
            Logger.getLogger(TCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
      public void eliminar(int id){
        try {
            PreparedStatement sp;
            sp=this.miConex.prepareStatement("Delete from Cliente Where cliente_ID=?");
            sp.setInt(1,id);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Cliente eliminado existosamente!",
                        "Eliminar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            this.actualizar();
            rs.last();
            this.actualizarEstado();
            } catch (SQLException ex) {
                Logger.getLogger(TCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
      public void modificar(int id, String nom, String ap, String ced, String sex){
       try {
            PreparedStatement sp;
            sp=this.miConex.prepareStatement("Update Cliente set nombres=?,apellidos=?, cedula=?, sexo=? where cliente_ID=? ");
            
            sp.setString(1, nom);
            sp.setString(2,ap);
            sp.setString(3, ced);
            sp.setString(4, sex);
            sp.setInt(5, id);
            
            sp.execute();
            
            JOptionPane.showMessageDialog(null,
                        "¡Modificación exitosa!",
                        "Actualizar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
            Logger.getLogger(TCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
      
      
      public void actualizar(){
        try {
            rs.close();
            rs = st.executeQuery("Select * FROM Cliente");
        } catch (SQLException ex) {
            Logger.getLogger(TCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
      public void cargarDatos(){
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT cliente_ID, nombres, apellidos, cedula FROM Cliente");
            rs.next();
        } catch (SQLException ex) {
            //Logger.getLogger(TCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
      public void actualizarEstado() throws SQLException{
        idCliente = rs.getInt(1);
        nombres = rs.getString(2);
        apellidos = rs.getString(3);
        cedula = rs.getString(4);
    }
    
         public void siguiente() throws SQLException {
        if (!rs.isLast()) {
            rs.next();
            this.actualizarEstado();
        } else {
            JOptionPane.showMessageDialog(null, "No hay más registros");
        }
    }

    public void anterior() throws SQLException {
        if (!rs.isFirst()) {
            rs.previous();
            this.actualizarEstado();
        } else {
            JOptionPane.showMessageDialog(null, "No hay más registros");
        }
    }

    public void ultimo() throws SQLException {
        rs.last();
        this.actualizarEstado();
    }

    public void primero() throws SQLException {
        rs.first();
        this.actualizarEstado();
    }
      
      
      public DefaultTableModel cargarTabla(){
        DefaultTableModel tabla = new DefaultTableModel();
        String encabezados[] = {"Código", "Nombre", "Apellido", "Cédula", "Sexo"};
        String filas[] = new String[5];
        tabla.setColumnIdentifiers(encabezados);
        try {
            //st = this.st.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            rs = st.executeQuery("SELECT cliente_ID, nombres, apellidos, cedula, sexo FROM Cliente");
            while(rs.next()){
                filas[0] = String.valueOf(rs.getInt(1));
                filas[1] = rs.getString(2);
                filas[2] = rs.getString(3);
                filas[3] = rs.getString(4);
                filas[4] = rs.getString(5);
                tabla.addRow(filas);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tabla;
    }
      
      public DefaultTableModel cargarTabla(String tb){
        DefaultTableModel tabla = new DefaultTableModel();
        String encabezados[] = {"Código", "Nombre", "Apellido", "Cédula", "Sexo"};
        String filas[] = new String[5];
        tabla.setColumnIdentifiers(encabezados);
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            rs = st.executeQuery(tb);
            while(rs.next()){
                filas[0] = String.valueOf(rs.getInt(1));
                filas[1] = rs.getString(2);
                filas[2] = rs.getString(3);
                filas[3] = rs.getString(4);
                filas[4] = rs.getString(5);
                tabla.addRow(filas);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tabla;
    }
      
      
      /////////////////////////////////////////////////////////7

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Connection getMiConex() {
        return miConex;
    }

    public void setMiConex(Connection miConex) {
        this.miConex = miConex;
    }

    

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
    
   
   public boolean verificarExistenciaRegistros()
   {
       
       try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT cliente_ID, nombres, apellidos, cedula FROM Cliente");
            if(!rs.next())
            {
                return false;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(TCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       return true;
   }
      
      
}
