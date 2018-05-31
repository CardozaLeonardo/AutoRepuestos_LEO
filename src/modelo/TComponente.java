
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
import vistas.TipoCompForm;

public class TComponente 
{
   private int componenteID;
   private String descripcion;
   private float precioUnitario;
   private int stock;
   public TFabricante fabricante;
   public TCategoriaComponente categoria;
   private TTipoComponente tipo;
   private ResultSet rs;
   private Statement st;
   private Conexion con;
   private Connection miConex;
   
   /**
   @roseuid 5AF054D5023A
    */
   public TComponente() throws SQLException 
   {
        con = new Conexion();
        con.conectar();
        miConex = con.getConexion();
        //this.cargarDatos();
        //this.actualizarEstado(); 
        fabricante = new TFabricante();
        categoria = new TCategoriaComponente();
        tipo = new TTipoComponente();
   }
   
   public void guardar(String descripcion, float precio, int stock, int tipoID, int categoriaID, int marcaID){
        PreparedStatement sp;
        try {
            sp = this.miConex.prepareStatement("Insert Into Componente(descripcion,precioUnitario, stock, tipoComponente_ID,categoriaComponente_ID, fabricante_ID) values (?,?,?,?,?,?)");
            sp.setString(1,descripcion);
            sp.setFloat(2,precio);
            sp.setInt(3,stock);
            sp.setInt(4,tipoID);
            sp.setInt( 5,categoriaID);
            sp.setInt( 6,marcaID);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Componente registrado existosamente!",
                        "Nuevo Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            //this.actualizar();
            //rs.last();
            //this.actualizarEstado();
        } catch (SQLException ex) {
            Logger.getLogger(TCategoriaComponente.class.getName()).log(Level.SEVERE, null, ex);
            /*JOptionPane.showMessageDialog(null,
                        "¡Ingrese adecuadamente los datos!","Error al guardar",JOptionPane.ERROR_MESSAGE);*/
        }
    }
   
   public void modificar(int id, String descripcion, float precio, int stock, int tipoID, int categoriaID, int marcaID){
        try {
            PreparedStatement sp;
            sp=this.miConex.prepareStatement("Update Componente set descripcion=?, precioUnitario=?, stock =?, tipoComponente_ID= ?, categoriaComponente_ID=?, fabricante_ID =? Where Componente_ID=?");
            sp.setString(1,descripcion);
            sp.setFloat(2,precio);
            sp.setInt(3,stock);
            sp.setInt(4,tipoID);
            sp.setInt(5,categoriaID);
            sp.setInt(6,marcaID);
            sp.setInt(7,id);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Componente modificado existosamente!",
                        "Actualizar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            //this.actualizar();
            //rs.first();
            //this.actualizarEstado();
            } catch (SQLException ex) {
            //Logger.getLogger(TCargo.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,
                        "¡Ingrese adecuadamente los datos!","Error al guardar",JOptionPane.ERROR_MESSAGE);
        }
    }
   
   
   public void actualizar(){
        try {
            rs.close();
            rs = st.executeQuery("Select * FROM Componente");
        } catch (SQLException ex) {
            //Logger.getLogger(TCargo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
   public void eliminar(int id){
        try {
            PreparedStatement sp;
            sp = this.miConex.prepareStatement("Delete from Componente Where Componente_ID = ?");
            sp.setInt(1,id);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Componente eliminado existosamente!",
                        "Eliminar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            //this.actualizar();
            //rs.last();
            //this.actualizarEstado();
            } catch (SQLException ex) {
                //Logger.getLogger(TTipoComponente.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null,
                        "No se puede eliminar este registro. Motivo: relacion con otros regisros","Error al guardar",JOptionPane.ERROR_MESSAGE);
        }
    }
   
   public boolean verificarStock(int cant, int id)
   {
       try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT stock FROM componente WHERE componente_ID = " + id);
            if(!rs.next())
            {
                return false;
            }else{
                int stock = rs.getInt("stock");
                
                if(stock < cant)
                {
                    JOptionPane.showMessageDialog(null, "No se cuenta con suficente Stock", "Stock insuficiente", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            }
            
        } catch (SQLException ex) {
            //Logger.getLogger(TUsuario.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        return true;
   }
   
   
   
   public ArrayList<TFabricante> cargarComboBFab(){
        ArrayList<TFabricante> lista = new ArrayList<>();
        lista = fabricante.cargarListaFabricante();
        return lista;
    }
   
   public ArrayList<TTipoComponente> cargarComboBTipo(){
        ArrayList<TTipoComponente> lista = new ArrayList<>();
        lista = tipo.cargarListaTipo();
        return lista;
    }
   
   public ArrayList<TCategoriaComponente> cargarComboBCategoria(){
        ArrayList<TCategoriaComponente> lista = new ArrayList<>();
        lista = categoria.cargarListaCategoria();
        return lista;
    }
   
   public DefaultTableModel cargarTabla(){
        DefaultTableModel tabla = new DefaultTableModel();
        String encabezados[] = {"ComponenteID", "Descripcion", "Precio", "Stock", "Tipo", "Marca", "Categoria"};
        String filas[] = new String[7];
        tabla.setColumnIdentifiers(encabezados);
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT c.componente_ID, c.descripcion, c.precioUnitario, c.stock, t.nombre, m.nombre, s.nombre "
              +"FROM Componente c INNER JOIN TipoComponente t ON c.tipoComponente_ID = t.tipoComponente_ID INNER JOIN "
              +"Fabricante m ON c.fabricante_ID = m.fabricante_ID INNER JOIN CategoriaComponente s ON c.categoriaComponente_ID = "
              +"s.categoriaComponente_ID");
            while(rs.next()){
                filas[0] = String.valueOf(rs.getInt(1));
                filas[1] = rs.getString(2);
                filas[2] = rs.getString(3);
                filas[3] = rs.getString(4);
                filas[4] = rs.getString(5);
                filas[5] = rs.getString(6);
                filas[6] = rs.getString(7);
                tabla.addRow(filas);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TComponente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tabla;
    }
   
   public DefaultTableModel cargarTabla(String query){
        DefaultTableModel tabla = new DefaultTableModel();
        String encabezados[] = {"ComponenteID", "Descripcion", "Precio", "Stock", "Tipo", "Marca", "Categoria"};
        String filas[] = new String[7];
        tabla.setColumnIdentifiers(encabezados);
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery(query);
            while(rs.next()){
                filas[0] = String.valueOf(rs.getInt(1));
                filas[1] = rs.getString(2);
                filas[2] = rs.getString(3);
                filas[3] = rs.getString(4);
                filas[4] = rs.getString(5);
                filas[5] = rs.getString(6);
                filas[6] = rs.getString(7);
                tabla.addRow(filas);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(TCargo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tabla;
    }

    public int getComponenteID() {
        return componenteID;
    }

    public void setComponenteID(int componenteID) {
        this.componenteID = componenteID;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
   
  public boolean actualizarStock(int cant, int id)
  {
      
    try {

        PreparedStatement sp;
        sp = this.miConex.prepareStatement("Update Componente set stock = stock -? where componente_ID =?");
        sp.setInt(1,cant);
        sp.setInt(2,id);
        sp.execute();
     } catch (SQLException ex) {
          return false;
     } 
      
     
      return true;
  }
   
}
