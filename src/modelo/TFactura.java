/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.Date;
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
 * @author del
 */
public class TFactura {
    
    private int factura_ID;
    private Date fecha;
    private float total;
    private TEmpleado empleado;
    private TCliente cliente;
    private TVenta ventas;
    private ResultSet rs;
    private Statement st;
    private Conexion con;
    private Connection miConex;

    public TFactura() throws SQLException
    {
        con= new Conexion();
        con.conectar();
        miConex = con.getConexion();
        ventas = new TVenta();
        cliente = new TCliente();
        empleado = new TEmpleado();
        //this.cargarDatos();
        //this.actualizarEstado();
    }
    
   public void guardar(String fecha, int empleadoID, int clienteID, float total){
        PreparedStatement sp;
        try {
            sp = this.miConex.prepareStatement("Insert Into Factura(fecha, empleado_ID,cliente_ID, total) values (?,?,?,?)");
            sp.setString(1,fecha);
            sp.setInt(2,empleadoID);
            sp.setInt(3,clienteID);
            sp.setFloat(4,total);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Factura registrada existosamente!",
                        "Nuevo Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            /*this.actualizar();
            rs.last();
            this.actualizarEstado();*/
        } catch (SQLException ex) {
            Logger.getLogger(TFactura.class.getName()).log(Level.SEVERE, null, ex);
            /*JOptionPane.showMessageDialog(null,
                        "¡Ingrese adecuadamente los datos!","Error al guardar",JOptionPane.ERROR_MESSAGE);*/
        }
    }
   
   public void eliminar(int id){
        try {
            PreparedStatement sp;
            sp = this.miConex.prepareStatement("Delete from Factura Where factura_ID = ?");
            sp.setInt(1,id);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Factura eliminada existosamente!",
                        "Eliminar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            /*this.actualizar();
            rs.last();
            this.actualizarEstado();*/
            } catch (SQLException ex) {
                Logger.getLogger(TTipoComponente.class.getName()).log(Level.SEVERE, null, ex);
                /*JOptionPane.showMessageDialog(null,
                        "No se puede eliminar este registro. Motivo: relacion con otros regisros","Error al guardar",JOptionPane.ERROR_MESSAGE);*/
        }
    }
   
   public DefaultTableModel cargarTablaVentas()
   {
       return ventas.cargarTabla(this.factura_ID);
   }
   
   
   public DefaultTableModel cargarTablaClientes()
   {
       return cliente.cargarTabla();
   }
   
   public DefaultTableModel cargarTablaEmple()
   {
       return empleado.cargarTabla();
   }
   
   public DefaultTableModel cargarTablaEmple(String q)
   {
       return empleado.cargarTabla(q);
   }
   
   public DefaultTableModel cargarTablaClientes(String q)
   {
       return cliente.cargarTabla(q);
   }
   
   public DefaultTableModel cargarTablaCompo()
    {
        return ventas.cargarTablaCom();
    }
    
    public DefaultTableModel cargarTablaCompo(String q)
    {
        return ventas.cargarTablaCom(q);
    }
    
    public ArrayList<TTipoComponente> cargarCBTipo()
    {
        return ventas.cargarCBTipo();
    }
    
    public ArrayList<TFabricante> cargarCBFac()
    {
        return ventas.cargarCBFac();
    }
    
    public ArrayList<TCategoriaComponente> cargarCBCat()
    {
        return ventas.cargarCBCat();
    }

    public int getFactura_ID() {
        return factura_ID;
    }

    public void setFactura_ID(int factura_ID) {
        this.factura_ID = factura_ID;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
    
    public void cargarID(){
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT max(factura_ID) + 1 as id from Factura");
            rs.next();
            
            int id = rs.getInt("id");
            factura_ID = id;
            
            if(factura_ID == 0){
                factura_ID = 1;
            }            
            
        } catch (SQLException ex) {
            Logger.getLogger(TCategoriaComponente.class.getName()).log(Level.SEVERE, null, ex);
            factura_ID = 1;
        }
    }
    
   public void guardarVenta(int facturaID, int componenteID, int cantidad)
   {
       ventas.guardar(facturaID, componenteID, cantidad);
   }
   
   public boolean actualizarStock(int ns, int id)
   {
       return ventas.actualizarStock(ns, id);
   }
   
   public void eliminarTV(int idFactura)
   {
       ventas.eliminarTV(idFactura);
   }
   
   public void eliminarV(int id)
   {
       ventas.eliminar(id);
   }
   
   public void guardarTotal()
  {
       try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("Update factura set total = " + total + " where factura_ID = " + factura_ID);
            rs.next();
        } catch (SQLException ex) {
            
        } 
     
  }
}
