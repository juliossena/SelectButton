package dados.bdMySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controle.IFiltroBusca;
import dados.IRepoAlunos;
import modelo.Data;
import modelo.alunos.Aluno;
import modelo.alunos.Curso;
import modelo.alunos.Turma;

public class RepoAlunos implements IRepoAlunos {
	private ComunicacaoBD comunicacao;
	
	public RepoAlunos() {
		comunicacao = new ComunicacaoBD();
	}
	
	@Override
	public void cadastraAluno(List<Aluno> alunos) throws ClassNotFoundException, SQLException {
		String comando = "INSERT INTO Aluno (NmAluno, MatriculaAluno, CodTurma, DtTurma, CodCurso) VALUES ";
		
		for (Aluno aluno : alunos) {
			String turma = "NULL";
			String dataTurma = "NULL";
			
			if (aluno.getTurma() != null && aluno.getTurma().getCodigo() > 0) {
				turma = "'" + aluno.getTurma().getCodigo() + "'";
				dataTurma = "CURDATE()";
			}
			if (aluno.getDataTurma() != null) {
				dataTurma = aluno.getDataTurma().getStringBD();
			}
			comando += "('" + aluno.getNome() + "', '" + aluno.getMatricula() + "', " + turma + ", " +dataTurma+ ", '" + aluno.getCurso().getCodigo() + "'),";
		}
		
		comando = comando.substring(0, comando.length() - 1);
		
		comunicacao.escreverBD(comando);
	}

	@Override
	public void editaAluno(Aluno aluno) throws ClassNotFoundException, SQLException {
		String turma = "";
		String dataTurma = "";
		
		if (aluno.getTurma() != null) {
			turma = aluno.getTurma().getCodigo() + "";
		}
		if (aluno.getDataTurma() != null) {
			dataTurma = " DtTurma=CUR_DATE(), ";
			 
		}
		
		String comando = "UPDATE Aluno SET NmAluno='" + aluno.getNome() + "', MatriculaAluno='" + aluno.getMatricula() + "', CodTurma='" + turma + "'," + dataTurma + " CodCurso='" + aluno.getCurso().getCodigo() + "' WHERE CodAluno='" + aluno.getCodigo() + "'";
	
		comunicacao.escreverBD(comando);
	}

	@Override
	public void deletaAluno(List<Aluno> alunos) throws ClassNotFoundException, SQLException {
		String comando = "DELETE FROM Aluno WHERE CodAluno IN (";
		
		for (Aluno aluno : alunos) {
			comando += "'" + aluno.getCodigo() + "',";
		}
		comando = comando.substring(0, comando.length() - 1);
		comando += ")";
		
		comunicacao.escreverBD(comando);
	}

	@Override
	public List<Aluno> getAlunos(IFiltroBusca filtro) throws ClassNotFoundException, SQLException {
		String comando = "SELECT * FROM Aluno AS A ";
		
		if (filtro != null) {
			comando += filtro.getWhere();
		} else {
			comando += "INNER JOIN Curso AS C ON A.CodCurso=C.CodCurso LEFT JOIN Turma AS T ON A.CodTurma=T.CodTurma ORDER BY A.NmAluno";
		}
		ResultSet rs = comunicacao.buscarBD(comando);
		
		List<Aluno> alunos = new ArrayList<Aluno>();
		while (rs.next()) {
			Aluno aluno = new Aluno();
			aluno.setCodigo(rs.getInt("A.CodAluno"));
			aluno.setNome(rs.getString("A.NmAluno"));
			aluno.setMatricula(rs.getString("A.MatriculaAluno"));
			
			Curso curso = new Curso();
			curso.setCodigo(rs.getInt("C.CodCurso"));
			curso.setAno(rs.getInt("C.Ano"));
			curso.setCodigo(rs.getInt("C.CodCurso"));
			curso.setNome(rs.getString("C.NmCurso"));
			
			Turma turma = null;
			
			try {
				aluno.setDataTurma(new Data(rs.getDate("A.DtTurma").getTime()));
				turma = new Turma();
				turma.setCodigo(rs.getInt("T.CodTurma"));
				turma.setNome(rs.getString("T.NmTurma"));
				turma.setCurso(curso);
			} catch (NullPointerException ex) {
				turma = null;
			}
			
			aluno.setCurso(curso);
			aluno.setTurma(turma);
			alunos.add(aluno);
		}
		
		return alunos;
	}

}
