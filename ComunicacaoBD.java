package dados.bdMySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ComunicacaoBD {
	private static ConexaoMySQL conexao;
	
	public ComunicacaoBD() {
		if (conexao == null) {
//			conexao = new ConexaoMySQL("localhost", "ReaderCode", "root", "");
			conexao = new ConexaoMySQL("192.168.0.101:3306", "ReaderCode", "root", "");
		}
	}
	
	public void escreverBD(String comando) throws ClassNotFoundException, SQLException {
		PreparedStatement stmt = conexao.getConexao().prepareStatement(comando);
		stmt.execute();
		stmt.close();
	}

	public ResultSet buscarBD(String comando) throws ClassNotFoundException, SQLException {
		Statement stmt = conexao.getConexao().createStatement();
		ResultSet rs = stmt.executeQuery(comando);
		return rs;
	}
}
