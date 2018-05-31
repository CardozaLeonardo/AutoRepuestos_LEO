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
public class TUsuario {
    private int idUsuario;
    private int idEmpleado;
    private String nombreUsuario;
    private String contraseña;
    private String estado;
    private TEmpleado nom;
    private ResultSet rs;
    private Statement st;
    private Conexion con;
    private Connection miConex;

    public TUsuario(int idUsuario, int idEmpleado, String nombre, String apellidos) {
        this.idUsuario = idUsuario;
        this.idEmpleado = idEmpleado;
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
    }
    
    public TUsuario() throws SQLException{
        con = new Conexion();
        con.conectar();
        miConex = con.getConexion();
        //this.cargarDatos();
        //this.actualizarEstado();
         
    }
    
    public void guardar(String usuario, String clave, int empleado_ID){
        PreparedStatement sp;
        try {
            sp = this.miConex.prepareStatement("Insert into Usuario(usuario, clave,estado, empleado_ID) values (?,?,?,?)");
            sp.setString(1,usuario);
            sp.setString(2,clave);
            sp.setString(3,"Noiniciado");
            sp.setInt(4, empleado_ID);
            
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Usuario guardado existosamente!",
                        "Nuevo Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            //this.actualizar();
            //rs.last();
            //this.actualizarEstado();
        } catch (SQLException ex) {
            Logger.getLogger(TCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardar(String usuario, String clave){
        PreparedStatement sp;
        String sq = "(SELECT max(empleado_ID) FROM Empleado)";
        try {
            sp = this.miConex.prepareStatement("Insert into Usuario(usuario, clave,estado, empleado_ID) values (?,?,?," + sq +")");
            sp.setString(1,usuario);
            sp.setString(2,clave);
            sp.setString(3,"Noiniciado");
            //sp.setInt(4, empleado_ID);
            
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Usuario guardado existosamente!",
                        "Nuevo Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            //this.actualizar();
            //rs.last();
            //this.actualizarEstado();
        } catch (SQLException ex) {
            Logger.getLogger(TCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean verificarUsuarioExistente(String usuario)
    {
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT * FROM Usuario WHERE usuario = '" + usuario + "'");
            if(!rs.next())
            {
               return true;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(TUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       JOptionPane.showMessageDialog(null,"Ya existe una cuenta con este nombre de usuario", "Usuario Existente", JOptionPane.INFORMATION_MESSAGE);
       return false;
    }
    
    public void eliminar(String username){
        try {
            PreparedStatement sp;
            sp=this.miConex.prepareStatement("Delete from Usuario Where usuario =?");
            sp.setString(1,username);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Cargo eliminado existosamente!",
                        "Eliminar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            //this.actualizar();
            //rs.last();
            //this.actualizarEstado();
            } catch (SQLException ex) {
                Logger.getLogger(TUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<String> obtenerListaCuentas(int empleado_ID)
    {
        ArrayList <String> cuentas = new ArrayList();
        
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT * FROM Usuario WHERE empleado_ID = " + empleado_ID);
            while(rs.next())
            {
                cuentas.add(rs.getString("usuario"));
            }
        } catch (SQLException ex) {
            //Logger.getLogger(TUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return cuentas;
    }
    
     public void modificar(int id, String nom, String ap, String ced){
        try {
            PreparedStatement sp;
            sp=this.miConex.prepareStatement("Update Usuario set usuario=?,usuario_ID=? where empleado_ID=? ");
            
            
            sp.setString(3,ced);
            sp.setString(2, nom);
            sp.setString(4, ap);
            sp.setInt(1, id);
            
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
            rs = st.executeQuery("Select * FROM Usuario");
        } catch (SQLException ex) {
            Logger.getLogger(TUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      public void cargarDatos(){
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT usuario FROM Usuario");
            rs.next();
        } catch (SQLException ex) {
            //Logger.getLogger(TUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
      public void actualizarEstado() throws SQLException{
        idUsuario = rs.getInt(1);
        nombreUsuario = rs.getString(2);
        contraseña = rs.getString(3);
        
    }
      
      public DefaultTableModel cargarTabla(){
        DefaultTableModel tabla = new DefaultTableModel();
        String encabezados[] = {"Usuario", "Empleado"};
        String filas[] = new String[5];
        tabla.setColumnIdentifiers(encabezados);
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            rs = st.executeQuery("SELECT e.usuario_ID, e.usuario, c.nombres FROM Usuario e INNER JOIN Empleado c ON e.empleado_ID = c.empleado_ID");
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
        String encabezados[] = {"Usuario", "Empleado"};
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
      
      //SETTER AND GETTER

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public TEmpleado getNom() {
        return nom;
    }

    public void setNom(TEmpleado nom) {
        this.nom = nom;
    }
    
    public boolean autenticar(String user, String pass)
    {
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT * FROM Usuario Where usuario = '" + user + "' and clave = '"+ pass +"'");
            if(!rs.next())
            {
                return false;
            }else{
               PreparedStatement sp;
               sp=this.miConex.prepareStatement("UPDATE Usuario set estado = 'Noiniciado'");
               sp.execute();
               sp=this.miConex.prepareStatement("UPDATE Usuario set estado = 'Iniciado' WHERE usuario =? and clave =?");
               sp.setString(1, user);
               sp.setString(2, pass);
               sp.execute();
               
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(TUsuario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
    }
    
    
    
}
