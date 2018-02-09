package dados.bdMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL {
	private Connection c;
	
	private String nomeServer;
	private String nomeBd;
	private String nomeUsuario;
	private String senha;
	private String nomeDriver;
	
	public ConexaoMySQL(String nomeServer, String nomeBd, String nomeUsuario, String senha) {
		this.nomeServer = nomeServer;
		this.nomeBd = nomeBd;
		this.nomeUsuario = nomeUsuario;
		this.senha = senha;
		this.nomeDriver ="com.mysql.jdbc.Driver";
	}
	
	public java.sql.Connection getConexao() throws SQLException, ClassNotFoundException {
		if (c == null || c.isClosed()) {
			Class.forName(nomeDriver);
			String url = "jdbc:mysql://" + 	nomeServer + "/" + nomeBd;
//			String url = "jdbc:mysql://" + "192.168.43.238:3306" + "/" + nomeBd;
			
			System.out.println(url);
			c = DriverManager.getConnection(url, nomeUsuario, senha);
		}
		
		return c;
	}
	
	public void fechaConexao() throws SQLException{
		if (c != null) {
			c.close();
		}
	}
}
