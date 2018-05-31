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
 * @author acer
 */
public class TEmpleado {
    private int empleadoID;
    private String nombre;
    private String apellido;
    private String cedula;
    private String sexo;
    
    private ResultSet rs;
    private Statement st;
    private Conexion con;
    private Connection miConex;
    public Integer registros;
    
    public TEmpleado() throws SQLException{
        con = new Conexion();
        con.conectar();
        miConex = con.getConexion();
        this.cargarDatos();
        this.actualizarEstado();
    }
    
    public DefaultTableModel mostar(String buscar){
        DefaultTableModel modelo;
        
        String [] titulos = {"ID","CEDULA","NOMBRES","APELLIDOS","SEXO"};
        String [] registro = new String [6];
        
        registros = 0;
        
        modelo = new DefaultTableModel(null, registro);
        
        PreparedStatement sp;
        
        
        try {
            sp = this.miConex.prepareStatement("select * from Empleado where empleado_ID like '%" + buscar + "%' order by nombres");
            
            while(rs.next()){
                registro [0]= rs.getString("ID");
                registro [1]= rs.getString("CEDULA");
                registro [2]= rs.getString("NOMBRES");
                registro [3]= rs.getString("APELLIDOS");
                registro [4]= rs.getString("SEXO");
                
                registros=registros+1;
                modelo.addRow(registro);
                
                
            } 
            
            this.actualizar();
            rs.last();
            this.actualizarEstado();
            return modelo;
        } catch (SQLException ex) {
            Logger.getLogger(TEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        
    }
    
    public void guardar(String ced, String nombre, String apellido, String Sexo){
        PreparedStatement sp;
        try {
            sp = this.miConex.prepareStatement("insert into Empleado(cedula,nombres,apellidos,sexo) values (?,?,?,?)");
           
            sp.setString(1,ced);
            sp.setString(2,nombre);
            sp.setString(3,apellido);
            sp.setString(4,Sexo);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Empleado registrado existosamente!",
                        "Nuevo Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            this.actualizar();
            rs.last();
            this.actualizarEstado();
        } catch (SQLException ex) {
            Logger.getLogger(TEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void modificar(String ced, String nombre, String apellido, String Sexo, int id){
        try {
            PreparedStatement sp;
            sp=this.miConex.prepareStatement("Update Empleado set cedula=?,nombres=?,apellidos=?,sexo=? Where empleado_ID=?");
            sp.setString(1, ced);
            sp.setString(2,nombre);
            sp.setString(3,apellido);
            sp.setString(4,Sexo);
            sp.setInt(5,id);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Empleado modificado existosamente!",
                        "Actualizar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
            Logger.getLogger(TEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminar(int empleado_ID){
        try {
            PreparedStatement sp;
            sp=this.miConex.prepareStatement("Delete from Empleado Where empleado_ID=?");
            sp.setInt(1,empleado_ID);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Empleado eliminado existosamente!",
                        "Eliminar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            this.actualizar();
            rs.last();
            this.actualizarEstado();
            } catch (SQLException ex) {
                Logger.getLogger(TEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizar(){
        try {
            rs.close();
            rs = st.executeQuery("Select empleado_ID,cedula,nombres,apellidos,sexo FROM Empleado");
        } catch (SQLException ex) {
            Logger.getLogger(TEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarDatos(){
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT empleado_ID,cedula,nombres,apellidos,sexo FROM Empleado");
            rs.next();
        } catch (SQLException ex) {
            //Logger.getLogger(TEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String obtenerNombreEmpleadoIniciado()
    {
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT concat(e.nombres,' ',' ',e.apellidos) as FullName FROM Empleado e" +
            " INNER JOIN Usuario u ON u.empleado_ID = e.empleado_ID WHERE u.estado = 'Iniciado'");
            rs.next();
            
            String name = rs.getString("FullName");
            return name;
        } catch (SQLException ex) {
            Logger.getLogger(TEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "Unknown";
    }
    
    public void cargarID()
    {
           
        try{
           st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
           rs = st.executeQuery("SELECT e.empleado_ID FROM Empleado e INNER JOIN Usuario u ON u.empleado_ID = e.empleado_ID "
           + "WHERE u.estado = 'Iniciado'");
           rs.next();
           
           empleadoID = rs.getInt("e.empleado_ID");
            
        } catch (SQLException ex) {
            Logger.getLogger(TEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizarEstado() throws SQLException{
        empleadoID = rs.getInt(1);
        cedula = rs.getString(2);
        nombre = rs.getString(3);
        apellido = rs.getString(4);
        sexo = rs.getString(5);
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
   
    public ArrayList<TEmpleado> cargarListaCargos(){
        ArrayList<TEmpleado> lista = new ArrayList<>();
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT empleado_ID,cedula,nombres,apellidos,sexo FROM Empleado");
            while(rs.next()){
                TEmpleado e = new TEmpleado();
                e.setEmpleadoID(rs.getInt(1));
                e.setCedula(rs.getString(2));
                e.setNombre(rs.getString(3));
                e.setApellido(rs.getString(4));
                e.setSexo(rs.getString(5));
                lista.add(e);
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(TEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    public int getEmpleadoID() {
        return empleadoID;
    }

    public void setEmpleadoID(int empleadoID) {
        this.empleadoID = empleadoID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

   
    public DefaultTableModel cargarTabla(){
        DefaultTableModel tabla = new DefaultTableModel();
        String encabezados[] = {"Código", "Nombre", "Apellido", "Cédula", "Sexo"};
        String filas[] = new String[5];
        tabla.setColumnIdentifiers(encabezados);
        try {
            //st = this.st.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            rs = st.executeQuery("SELECT empleado_ID, nombres, apellidos, cedula, sexo FROM Empleado");
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
    
    public DefaultTableModel cargarTabla(String q){
        DefaultTableModel tabla = new DefaultTableModel();
        String encabezados[] = {"Código", "Nombre", "Apellido", "Cédula", "Sexo"};
        String filas[] = new String[5];
        tabla.setColumnIdentifiers(encabezados);
        try {
            //st = this.st.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            
            rs = st.executeQuery(q);
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
  
    
    public boolean verificarExistenciaRegistros()
    {
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT empleado_ID,cedula,nombres,apellidos,sexo FROM Empleado");
            if(!rs.next())
            {
                this.rs.beforeFirst();
                return false;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(TEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
}
