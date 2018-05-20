//Source file: F:\\modelo\\CategoriaComponente.java

package modelo;

//import java.beans.Statement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class TCategoriaComponente 
{
   private int categoriaComponenteID;
   private String nombre;
   private String descripcion;
   private ResultSet rs;
   private Statement st;
   private Conexion con;
   private Connection miConex;
   /**
   @roseuid 5AF054D303B0
    */
   
   
   public TCategoriaComponente() throws SQLException 
   {
        con= new Conexion();
        con.conectar();
        miConex = con.getConexion();
        this.cargarDatos();
        this.actualizarEstado();
   }
   
   public void guardar(String nombre, String desc){
        PreparedStatement sp;
        try {
            sp = this.miConex.prepareStatement("Insert Into CategoriaComponente(nombre,descripcion) values (?,?)");
            sp.setString(1,nombre);
            sp.setString(2,desc);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Categoría registrada existosamente!",
                        "Nuevo Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            this.actualizar();
            rs.last();
            this.actualizarEstado();
        } catch (SQLException ex) {
            //Logger.getLogger(TCategoriaComponente.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,
                        "¡Ingrese adecuadamente los datos!","Error al guardar",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void modificar(int id, String nombre, String descripcion){
        try {
            PreparedStatement sp;
            sp=this.miConex.prepareStatement("Update CategoriaComponente set nombre=?, descripcion=? Where categoriaComponente_ID=?");
            sp.setString(1,nombre);
            sp.setString(2,descripcion);
            sp.setInt(3,id);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Categoría modificada existosamente!",
                        "Actualizar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            this.actualizar();
            rs.first();
            this.actualizarEstado();
            } catch (SQLException ex) {
            //Logger.getLogger(TCargo.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,
                        "¡Ingrese adecuadamente los datos!","Error al guardar",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void eliminar(int id){
        try {
            PreparedStatement sp;
            sp = this.miConex.prepareStatement("Delete from CategoriaComponente Where categoriaComponente_ID = ?");
            sp.setInt(1,id);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Categoría eliminada existosamente!",
                        "Eliminar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            this.actualizar();
            rs.last();
            this.actualizarEstado();
            } catch (SQLException ex) {
                //Logger.getLogger(TTipoComponente.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null,
                        "No se puede eliminar este registro. Motivo: relacion con otros regisros","Error al guardar",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void actualizar(){
        try {
            rs.close();
            rs = st.executeQuery("Select categoriaComponente_ID, nombre, descripcion FROM categoriaComponente");
        } catch (SQLException ex) {
            //Logger.getLogger(TCargo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarDatos(){
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT categoriaComponente_ID, nombre, descripcion FROM CategoriaComponente");
            rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(TCategoriaComponente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizarEstado() throws SQLException{
        categoriaComponenteID = rs.getInt(1);
        nombre = rs.getString(2);
        descripcion = rs.getString(3);
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

    public int getCategoriaComponenteID() {
        return categoriaComponenteID;
    }

    public void setCategoriaComponenteID(int categoriaComponenteID) {
        this.categoriaComponenteID = categoriaComponenteID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
   
    public boolean buscarRegistro(int id) throws SQLException
    {
        boolean encontrado = false;
        int index = this.categoriaComponenteID;
        rs.first();
        
        while(rs.next())
        {
            this.actualizarEstado();
            if(rs.getInt("categoriaComponente_ID") == id)
            {
                encontrado = true;
                return encontrado;
            }
            
        }
        
        if(!encontrado)
        {
           rs.first();
           if(rs.getInt("categoriaComponente_ID") == index)
           {
                return encontrado;
           }
           
           while(rs.next())
           {
                if(rs.getInt("categoriaComponente_ID") == index)
                {
                    return encontrado;
                } 
           }
        }
        
        return encontrado;
    }
    
    public ArrayList<TCategoriaComponente> cargarListaCategoria(){
        ArrayList<TCategoriaComponente> lista = new ArrayList<>();
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT categoriaComponente_ID, nombre, descripcion FROM CategoriaComponente");
            while(rs.next()){
                TCategoriaComponente cc = new TCategoriaComponente();
                cc.setCategoriaComponenteID(rs.getInt(1));
                cc.setNombre(rs.getString(2));
                cc.setDescripcion(rs.getString(3));
                lista.add(cc);
                
            }
        } catch (SQLException ex) {
            //Logger.getLogger(TCargo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
   
}
