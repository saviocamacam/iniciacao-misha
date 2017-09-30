package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Conexao {
    
    private Connection con;
    private String url = "jdbc:postgresql://localhost:5432/facebook";
    private String usuario = "postgres";
    private String senha = "root";
    
    
    public Connection getConnection (){
        try {
            con = DriverManager.getConnection(url, usuario, senha);
            
        } catch (SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    return con;
    }
    
    public Conexao(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro no construtor da conexão");
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}