//Source file: F:\\modelo\\Venta.java
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

public class TVenta 
{
    private int venta_ID;
    private int cantidad;
    private TComponente componente;
    private ResultSet rs;
    private Statement st;
    private Conexion con;
    private Connection miConex;
   
   /**
   @roseuid 5AF054D50101
    */
   public TVenta() throws SQLException 
   {
        con= new Conexion();
        con.conectar();
        miConex = con.getConexion();
        componente = new TComponente();
        /*this.cargarDatos();
        this.actualizarEstado();*/
   }
   
   
   public void guardar(int facturaID, int componenteID, int cantidad){
        PreparedStatement sp;
        try {
            sp = this.miConex.prepareStatement("Insert Into Venta(factura_ID,componente_ID,cantidad) values (?,?,?)");
            sp.setInt(1,facturaID);
            sp.setInt(2,componenteID);
            sp.setInt(3, cantidad);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Categoría registrada existosamente!",
                        "Nuevo Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            /*this.actualizar();
            rs.last();
            this.actualizarEstado();*/
        } catch (SQLException ex) {
            //Logger.getLogger(TCategoriaComponente.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,
                        "¡Ingrese adecuadamente los datos!","Error al guardar",JOptionPane.ERROR_MESSAGE);
        }
    }
   
    public void eliminar(int id){
        try {
            PreparedStatement sp;
            sp = this.miConex.prepareStatement("Delete from Venta Where venta_ID = ?");
            sp.setInt(1,id);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Eliminado!",
                        "Eliminar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            /*this.actualizar();
            rs.last();
            this.actualizarEstado();*/
            } catch (SQLException ex) {
                //Logger.getLogger(TTipoComponente.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null,
                        "No se puede eliminar este registro. Motivo: relacion con otros regisros","Error al guardar",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void eliminarTV(int idFactura){
        try {
            PreparedStatement sp;
            sp = this.miConex.prepareStatement("Delete from Venta Where factura_ID = ?");
            sp.setInt(1,idFactura);
            sp.execute();
            /*JOptionPane.showMessageDialog(null,
                        "¡Eliminado!",
                        "Eliminar Registro",
                        JOptionPane.INFORMATION_MESSAGE);*/
            /*this.actualizar();
            rs.last();
            this.actualizarEstado();*/
            } catch (SQLException ex) {
                Logger.getLogger(TTipoComponente.class.getName()).log(Level.SEVERE, null, ex);
                /*JOptionPane.showMessageDialog(null,
                        "No se puede eliminar este registro. Motivo: relacion con otros regisros","Error al guardar",JOptionPane.ERROR_MESSAGE);*/
        }
    }
    
  public DefaultTableModel cargarTabla(int ID){
        DefaultTableModel tabla = new DefaultTableModel();
        String encabezados[] = {"Codigo","CodigoCom","Descripcion", "Precio", "Cantidad", "Subtotal"};
        String filas[] = new String[6];
        tabla.setColumnIdentifiers(encabezados);
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT v.venta_ID,c.componente_ID,  c.descripcion, c.precioUnitario, v.cantidad, (c.precioUnitario * v.cantidad) FROM"
             + " Venta v INNER JOIN Componente c ON v.componente_ID = c.componente_ID WHERE v.factura_ID = " + ID);
            while(rs.next()){
                filas[0] = String.valueOf(rs.getInt(1));
                filas[1] = String.valueOf(rs.getInt(2));
                filas[2] = rs.getString(3);
                filas[3] = String.valueOf(rs.getFloat(4));
                filas[4] = String.valueOf(rs.getInt(5));
                filas[5] = String.valueOf(rs.getFloat(6));
                tabla.addRow(filas);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tabla;
    }
 
    public DefaultTableModel cargarTablaCom()
    {
        return componente.cargarTabla();
    }
    
    public DefaultTableModel cargarTablaCom(String q)
    {
        return componente.cargarTabla(q);
    }
    
    public ArrayList<TTipoComponente> cargarCBTipo()
    {
        return componente.cargarComboBTipo();
    }
    
    public ArrayList<TFabricante> cargarCBFac()
    {
        return componente.cargarComboBFab();
    }
    
    public ArrayList<TCategoriaComponente> cargarCBCat()
    {
        return componente.cargarComboBCategoria();
    }
   
    public boolean actualizarStock(int cant, int id)
    {
        return componente.actualizarStock(cant, id);
    }
    
    
}
