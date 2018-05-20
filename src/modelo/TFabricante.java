

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

public class TFabricante 
{
    private int fabricanteID;
    private String nombre;
    private ResultSet rs;
    private Statement st;
    private Conexion con;
    private Connection miConex;
    
   /**
   @roseuid 5AF054D3026D
    */
   public TFabricante() throws SQLException 
   {
        con= new Conexion();
        con.conectar();
        miConex = con.getConexion();
        this.cargarDatos();
        this.actualizarEstado();
   }
   
   
   public void guardar(String nombre){
        PreparedStatement sp;
        try {
            sp = this.miConex.prepareStatement("Insert Into Fabricante(nombre) values (?)");
            sp.setString(1,nombre);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Fabricante registrado existosamente!",
                        "Nuevo Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            this.actualizar();
            rs.last();
            this.actualizarEstado();
        } catch (SQLException ex) {
            //Logger.getLogger(TCategoriaComponente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void modificar(int id, String nombre){
        try {
            PreparedStatement sp;
            sp=this.miConex.prepareStatement("Fabricante set nombre=? Where fabricante_ID=?");
            sp.setString(1,nombre);
            sp.setInt(2,id);
            sp.execute();
            rs.first();
            JOptionPane.showMessageDialog(null,
                        "¡Fabricante modificado existosamente!",
                        "Actualizar Registro",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
            //Logger.getLogger(TCargo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminar(int id){
        try {
            PreparedStatement sp;
            sp = this.miConex.prepareStatement("Delete from Fabricante Where fabricante_ID=?");
            sp.setInt(1,id);
            sp.execute();
            JOptionPane.showMessageDialog(null,
                        "¡Fabricante eliminado existosamente!",
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
            rs = st.executeQuery("Select fabricante_ID, nombre FROM Fabricante");
        } catch (SQLException ex) {
            //Logger.getLogger(TCargo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarDatos(){
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT fabricante_ID, nombre FROM Fabricante");
            rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(TCategoriaComponente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void actualizarEstado() throws SQLException{
        fabricanteID = rs.getInt(1);
        nombre = rs.getString(2);
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

    public int getFabricanteID() {
        return fabricanteID;
    }

    public void setFabricanteID(int fabricanteID) {
        this.fabricanteID = fabricanteID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
   
   
    public boolean buscarRegistro(int id) throws SQLException
    {
        boolean encontrado = false;
        int index = this.fabricanteID;
        rs.first();
        
        while(rs.next())
        {
            this.actualizarEstado();
            if(rs.getInt("fabricante_ID") == id)
            {
                encontrado = true;
                return encontrado;
            }
            
        }
        
        if(!encontrado)
        {
           rs.first();
           if(rs.getInt("fabricante_ID") == index)
           {
                return encontrado;
           }
           
           while(rs.next())
           {
                if(rs.getInt("fabricante_ID") == index)
                {
                    return encontrado;
                } 
           }
        }
        
        return encontrado;
    }
    
    public ArrayList<TFabricante> cargarListaFabricante(){
        ArrayList<TFabricante> lista = new ArrayList<>();
        try {
            st = this.miConex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = st.executeQuery("SELECT fabricante_ID, nombre FROM Fabricante");
            while(rs.next()){
                TFabricante cc = new TFabricante();
                cc.setFabricanteID(rs.getInt(1));
                cc.setNombre(rs.getString(2));
                lista.add(cc);
                
            }
        } catch (SQLException ex) {
            //Logger.getLogger(TCargo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
}


