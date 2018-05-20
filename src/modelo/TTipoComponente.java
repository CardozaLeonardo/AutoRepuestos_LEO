//Source file: F:\\modelo\\TipoComponente.java

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

public class TTipoComponente 
{
   private int tipoComponenteID;
   private String nombre;
   private String descripcion;
   private ResultSet rs;
   private Statement st;
   private Conexion con;
   private Connection miConex;
   
   /**
   @roseuid 5AF054D400A1
    */
   public TTipoComponente() throws SQLException 
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
            sp = this.miConex.prepareStatement("Insert Into TipoComponente(nombre,descripcion) values (?,?)");
            sp.setString(1,nombre);
            sp.setString(2,desc);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Tipo Componente existosamente!",
                        "Nuevo Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            this.actualizar();
            rs.last();
            this.actualizarEstado();
        } catch (SQLException ex) {
            Logger.getLogger(TCategoriaComponente.class.getName()).log(Level.SEVERE, null, ex);
            /*JOptionPane.showMessageDialog(null,
                        "¡Ingrese adecuadamente los datos!","Error al guardar",JOptionPane.ERROR_MESSAGE);*/
        }
    }
    
    public void modificar(int id, String nombre, String descripcion){
        try {
            PreparedStatement sp;
            sp=this.miConex.prepareStatement("Update TipoComponente set nombre=?, descripcion=? Where tipoComponente_ID=?");
            sp.setString(1,nombre);
            sp.setString(2,descripcion);
            sp.setInt(3,id);
            sp.execute();
            rs.first();
            JOptionPane.showMessageDialog(null,
                        "¡Tipo componente modificado existosamente!",
                        "Actualizar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
            //Logger.getLogger(TCargo.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null,
                        "¡Ingrese adecuadamente los datos!","Error al guardar",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void eliminar(int id){
        try {
            PreparedStatement sp;
            sp = this.miConex.prepareStatement("Delete from TipoComponente Where tipoComponente_ID=?");
            sp.setInt(1,id);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Tipo componente eliminado existosamente!",
                        "Eliminar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            this.actualizar();
            rs.last();
            this.actualizarEstado();
            } catch (SQLException ex) {
                //Logger.getLogger(TCargo.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null,
                        "No se puede eliminar este registro. Motivo: relacion con otros regisros","Error al guardar",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void actualizar(){
        try {
            rs.close();
            rs = st.executeQuery("Select tipoComponente_ID, nombre, descripcion FROM tipoComponente");
        } catch (SQLException ex) {
            //Logger.getLogger(TCargo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarDatos(){
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT tipoComponente_ID, nombre, descripcion FROM TipoComponente");
            rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(TCategoriaComponente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizarEstado() throws SQLException{
        tipoComponenteID = rs.getInt(1);
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

    public int getTipoComponenteID() {
        return tipoComponenteID;
    }

    public void setTipoComponenteID(int tipoComponenteID) {
        this.tipoComponenteID = tipoComponenteID;
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
        int index = this.tipoComponenteID;
        rs.first();
        
        while(rs.next())
        {
            this.actualizarEstado();
            if(rs.getInt("tipoComponente_ID") == id)
            {
                encontrado = true;
                return encontrado;
            }
            
        }
        
        if(!encontrado)
        {
           rs.first();
           if(rs.getInt("tipoComponente_ID") == index)
           {
                return encontrado;
           }
           
           while(rs.next())
           {
                if(rs.getInt("tipoComponente_ID") == index)
                {
                    return encontrado;
                } 
           }
        }
        
        return encontrado;
    }
    
    public ArrayList<TTipoComponente> cargarListaTipo(){
        ArrayList<TTipoComponente> lista = new ArrayList<>();
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT tipoComponente_ID, nombre, descripcion FROM TipoComponente");
            while(rs.next()){
                TTipoComponente cc = new TTipoComponente();
                cc.setTipoComponenteID(rs.getInt(1));
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
