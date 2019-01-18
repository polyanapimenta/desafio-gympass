package gympass.desafio.bancodados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Polyana Pimenta
 * 
 * Esta classe serve como implementacao de modelo de banco de dados relacional
 * usando SQLite JDBC para gravar os registros da corrida de forma estruturada,
 * conforme anexo do diagrama relacional no README.md
 * 
 * Unidades de medidas consideradas
 *  - tabela Classificacao:
 *  	* velocidade_media lê em KM/h
 *  	* tempo_prova lê em minuto
 *  
 *  - tabela Registro:
 *  	* velocidade_media_volta lê em m/s
 *  	*  tempo_volta lê minutos  
 *  
 *  OBS: Linhas de código comentadas podem serem descomentadas para vizualização na saída.
 */

public class SQLiteJDBC {
	private Queue<String> codPiloto = new LinkedList<>();

	public SQLiteJDBC() {
		
	}
	
	SQLiteJDBC(boolean criarTabelas) {
		System.out.println("Conexao com SQLite estabelecida!");
		String tabelaCorrida = "CREATE TABLE IF NOT EXISTS CORRIDA (COD_PILOTO text, ID_REGISTRO integer, PRIMARY KEY (COD_PILOTO, ID_REGISTRO), FOREIGN KEY (COD_PILOTO) REFERENCES PILOTO (CODIGO), FOREIGN KEY (ID_REGISTRO) REFERENCES REGISTRO (ID));";
		String tabelaPiloto = "CREATE TABLE IF NOT EXISTS PILOTO (codigo text PRIMARY KEY, nome text NOT NULL, id_classificacao, FOREIGN KEY (id_classificacao) REFERENCES CLASSIFICACAO (id));";
		String tabelaRegistro = "CREATE TABLE IF NOT EXISTS REGISTRO (id integer PRIMARY KEY AUTOINCREMENT, hora text NOT NULL, volta int NOT NULL, tempo_volta real NOT NULL, velocidade_media_volta real NOT NULL);";
		String tabelaClassificacao = "CREATE TABLE IF NOT EXISTS CLASSIFICACAO (id integer PRIMARY KEY AUTOINCREMENT, hora_chegada text NOT NULL, ultima_volta int NOT NULL, tempo_prova real NOT NULL, velocidade_media real NOT NULL);";
		
		System.out.println("\nCriando tabelas no banco de dados..");
		criarTabela(tabelaCorrida);
		criarTabela(tabelaPiloto);
		criarTabela(tabelaRegistro);
		criarTabela(tabelaClassificacao);
		System.out.println("\nInserindo registros.. Aguarde..");
	}

	private Connection conectar() {
		Connection c = null;
		try {
			String url = "jdbc:sqlite:C:/sqlite/db/corrida.db";
			c = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println("Erro ao criar conexao: " + e.getMessage());
		}
		return c;
	}
	
	private void criarTabela(String sql) {
		//System.out.println(sql);
		try (Connection c = this.conectar(); Statement stmt = c.createStatement()) {
			stmt.execute(sql);

		} catch (SQLException e2) {
			System.out.println("Erro ao criar tabela: " + e2.getMessage());
		}
	}
	
	private int selectCodigoPiloto(String codPiloto) {
		int count = 0;
		String sql = "SELECT COUNT(*) FROM PILOTO WHERE CODIGO = " + "'" + codPiloto + "'" + ";";
		try (Connection c = this.conectar(); Statement stmt = c.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				count = rs.getInt("count(*)");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao selecionar dados: " + e.getMessage());
		}
		return count;
	}
	
	void popularRelacaoMM(String[] registro) {
		String sqlPiloto = "INSERT INTO PILOTO (CODIGO, NOME) VALUES (?,?);\n";
		String sqlRegistro = "INSERT INTO REGISTRO (HORA, VOLTA, TEMPO_VOLTA, VELOCIDADE_MEDIA_VOLTA) VALUES (?,?,?,?)\n";
		String sqlCorrida = "INSERT INTO CORRIDA (COD_PILOTO, ID_REGISTRO) VALUES (?,?);\n";
		
		ResultSet rs = null;
		PreparedStatement pstmt = null, pstmt2 = null, pstmt3 = null;
		Connection c = null;
		
		try {
			c = this.conectar();
			if(c == null)
				return;
			
			c.setAutoCommit(false);
			// populando piloto
			int dadosRepetidos = selectCodigoPiloto(registro[1]);
			if (dadosRepetidos < 1) {
				pstmt = c.prepareStatement(sqlPiloto);
				pstmt.setString(1, registro[1]);
				pstmt.setString(2, registro[2]);
				pstmt.executeUpdate();
				this.codPiloto.offer(registro[1]);
			}
			    
			// populando registro
			pstmt2 = c.prepareStatement(sqlRegistro, Statement.RETURN_GENERATED_KEYS);
			pstmt2.setString(1, registro[0]);
			pstmt2.setInt(2, Integer.parseInt(registro[3]));
			pstmt2.setDouble(3, Double.parseDouble(registro[4]));
			pstmt2.setDouble(4, Double.parseDouble(registro[5]));
			pstmt2.executeUpdate();
			
			// pegando o id gerado de registro
            rs = pstmt2.getGeneratedKeys();
            int idRegistro = 0;
            if (rs.next()) {
            	idRegistro = rs.getInt(1);
            }

            // populando Corrida
            pstmt3 = c.prepareStatement(sqlCorrida);
            pstmt3.setString(1, registro[1]);
            pstmt3.setInt(2, idRegistro);
            pstmt3.executeUpdate();
            
			c.commit();

		} catch (SQLException e3) {
			System.out.println("Erro ao inserir dadosy: " + e3.getMessage());
		} finally {
			try {
				if (rs != null)
                    rs.close();
				if (pstmt != null)
					pstmt.close();
				if (pstmt2 != null)	
					pstmt2.close();
				if (c != null)
					c.close();
			} catch (SQLException e3) {
				System.out.println(e3.getMessage());
			}
		}
	}

	void popularClassificacao() {
		String horaChegada = null;;
		int ultimaVolta = 0;
		double tempoProva = 0;
		double velocidadeMedia = 0;
		
		String sqlHoraChegada = null;
		String sqlUltimaVolta = null;
		String sqlTempoProva = null;
		String sqlVelocidadeMedia = null;
		
		int count = selectCount("PILOTO");
		while (count != 0) {
			if (this.codPiloto.size() > 0) {
				sqlUltimaVolta = "SELECT MAX(VOLTA) AS 'VOLTA' FROM REGISTRO WHERE ID IN (SELECT ID_REGISTRO FROM CORRIDA WHERE COD_PILOTO = '"+ this.codPiloto.peek() +"')";
				sqlHoraChegada = "SELECT HORA FROM REGISTRO WHERE VOLTA = (" + sqlUltimaVolta + ") AND ID IN (SELECT ID_REGISTRO FROM CORRIDA WHERE COD_PILOTO = '"+ this.codPiloto.peek() +"');";
				sqlTempoProva = "SELECT SUM(TEMPO_VOLTA) AS \"TEMPO_PROVA\" FROM REGISTRO WHERE ID IN (SELECT ID_REGISTRO FROM CORRIDA WHERE COD_PILOTO = '"+ this.codPiloto.peek() +"');";
				sqlVelocidadeMedia = "SELECT SUM(VELOCIDADE_MEDIA_VOLTA) AS 'VELOCIDADE_VOLTA' FROM REGISTRO WHERE ID IN (SELECT ID_REGISTRO FROM CORRIDA WHERE COD_PILOTO = '"+ this.codPiloto.peek() +"');";
	
				Connection c = null;
				Statement stmt = null;
				PreparedStatement pstmt = null, pstmt2 = null;
				ResultSet rs = null;
				
				try {
					c = this.conectar();
					if(c == null)
						return;
					// capturando dados a serem inseridos em classificacao
					stmt = c.createStatement(); 
					rs = stmt.executeQuery(sqlHoraChegada);			
					while (rs.next()) {
						horaChegada = rs.getString("HORA");
					}
					rs.close();
					rs = stmt.executeQuery(sqlUltimaVolta);			
					while (rs.next()) {
						ultimaVolta = rs.getInt("VOLTA");
					}
					rs.close();
					rs = stmt.executeQuery(sqlTempoProva);
					while (rs.next()) {
						tempoProva = rs.getDouble("TEMPO_PROVA");
					}
					rs.close();
					rs = stmt.executeQuery(sqlVelocidadeMedia);			
					while (rs.next()) {
						velocidadeMedia = (rs.getDouble("VELOCIDADE_VOLTA") / ultimaVolta) * 3.622;
					}	
					// inserindo dados na tabela classificacao
					String sql = "INSERT INTO CLASSIFICACAO (hora_chegada, ultima_volta, tempo_prova, velocidade_media) VALUES (?,?,?,?)\n";	
					
					c.setAutoCommit(false);
					pstmt = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					pstmt.setString(1, horaChegada);
					pstmt.setInt(2, ultimaVolta);
					pstmt.setDouble(3, tempoProva);
					pstmt.setDouble(4, velocidadeMedia);
					pstmt.executeUpdate();
					
					// pegando o id gerado em Classificacao
		            rs = pstmt.getGeneratedKeys();
		            int idClassificacao = 0;
		            if (rs.next()) {
		            	idClassificacao = rs.getInt(1);
		            }
		            // atualizando id_classificacao na tabela piloto
		            String sqlUpdatePiloto = "UPDATE PILOTO SET id_classificacao = ? WHERE codigo = ?";
		
		            pstmt2 = c.prepareStatement(sqlUpdatePiloto);
		            pstmt2.setInt(1, idClassificacao);
		            pstmt2.setString(2, this.codPiloto.poll());
		            pstmt2.executeUpdate();
		            
					c.commit();
		
				} catch (SQLException e8) {
					System.out.println("Erro: " + e8.getMessage());
				} finally {
					try {
						if (pstmt != null)
							pstmt.close();
						if (pstmt2 != null)
							pstmt2.close();
						if (rs != null)
		                    rs.close();
						if (stmt != null)
							stmt.close();
						if (c != null)
							c.close();
					} catch (SQLException e8) {
						System.out.println(e8.getMessage());
					}
				}
			} else {
				return;
			}
			count--;
		}
	}
		
	public void selectPiloto() {
		String sql = "SELECT * FROM PILOTO;";
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			c = this.conectar();
			if(c == null)
				return;
			stmt = c.createStatement(); 
			rs = stmt.executeQuery(sql);
			
			System.out.println("\nPILOTO:\ncodigo\tnome\t\tid_classificacao");
			
			while (rs.next()) {
				System.out.print(rs.getString("codigo"));
				System.out.print("\t" + rs.getString("nome")); 
				System.out.println("\t\t" + rs.getInt("id_classificacao"));
			}
			
		} catch (SQLException e4) {
			System.out.println("Erro ao selecionar dados: " + e4.getMessage());
		} finally {
			try {
				if (rs != null)
                    rs.close();
				if (stmt != null)
					stmt.close();
				if (c != null)
					c.close();
			} catch (SQLException e4) {
				System.out.println(e4.getMessage());
			}
		}
	}
	
	
	public void selectCorrida() {
		String sql = "SELECT * FROM CORRIDA;";
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			c = this.conectar();
			if(c == null)
				return;
			stmt = c.createStatement(); 
			rs = stmt.executeQuery(sql);
			
			System.out.println("\nCORRIDA:\ncod_piloto\tid_registro");
			
			while (rs.next()) {
				System.out.println(rs.getString("cod_piloto") + "\t\t" + rs.getInt("id_registro"));
			}
			
		} catch (SQLException e5) {
			System.out.println("Erro ao selecionar dados: " + e5.getMessage());
		} finally {
			try {
				if (rs != null)
                    rs.close();
				if (stmt != null)
					stmt.close();
				if (c != null)
					c.close();
			} catch (SQLException e5) {
				System.out.println(e5.getMessage());
			}
		}
	}
	
	
	public void selectRegistro() {
		String sql = "SELECT * FROM REGISTRO;";
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			c = this.conectar();
			if(c == null)
				return;
			stmt = c.createStatement(); 
			rs = stmt.executeQuery(sql);
			
			System.out.println("\nREGISTRO:\nid\thora\t\tvolta\ttempo_volta\tvelocidade_media_volta");
			
			while (rs.next()) {
				System.out.print(rs.getInt("id"));
				System.out.print("\t" + rs.getString("hora"));
				System.out.print("\t" + rs.getInt("volta"));
				System.out.printf("\t %.5f", rs.getDouble("tempo_volta"));	
				System.out.printf("\t\t %.2f", rs.getDouble("velocidade_media_volta"));
				System.out.print("\n");
			}
			
		} catch (SQLException e6) {
			System.out.println("Erro ao selecionar dados: " + e6.getMessage());
		} finally {
			try {
				if (rs != null)
                    rs.close();
				if (stmt != null)
					stmt.close();
				if (c != null)
					c.close();
			} catch (SQLException e6) {
				System.out.println(e6.getMessage());
			}
		}
	}
	
	
	public void selectClassificacao() {
		String sql = "SELECT * FROM CLASSIFICACAO;";
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			c = this.conectar();
			if(c == null)
				return;
			stmt = c.createStatement(); 
			rs = stmt.executeQuery(sql);
			
			System.out.println("\nCLASSIFICACAO:\nid\thora_chegada\tultima_volta\ttempo_prova\tvelocidade_media");
			
			while (rs.next()) {
				System.out.print(rs.getInt("id"));
				System.out.print("\t" + rs.getString("hora_chegada"));
				System.out.print("\t" + rs.getInt("ultima_volta"));
				System.out.printf("\t\t %.5f", rs.getDouble("tempo_prova"));
				System.out.printf("\t\t %.2f", rs.getDouble("velocidade_media"));	 
				System.out.print("\n");
			}
			
		} catch (SQLException e7) {
			System.out.println("Erro ao selecionar dados: " + e7.getMessage());
		} finally {
			try {
				if (rs != null)
                    rs.close();
				if (stmt != null)
					stmt.close();
				if (c != null)
					c.close();
			} catch (SQLException e7) {
				System.out.println(e7.getMessage());
			}
		}
	}	

	public int selectCount(String tabela) {
		String sql = "SELECT COUNT(*) FROM " + tabela + ";";
		int count = 0;
		try (Connection c = this.conectar(); Statement stmt = c.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				count = rs.getInt("count(*)");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao selecionar dados: " + e.getMessage());
		}
		return count;
	}
	
	public void selectPosicaoChegada() {
		StringBuilder sb = new StringBuilder();
		String sql = "SELECT P.CODIGO, P.NOME, C.ULTIMA_VOLTA, C.TEMPO_PROVA FROM CLASSIFICACAO C JOIN PILOTO P ON C.ID = P.ID_CLASSIFICACAO ORDER BY C.TEMPO_PROVA ASC;";
		int count = 1;
		
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
	
		try {
			c = this.conectar();
			if(c == null)
				return;
			
			stmt = c.createStatement(); 
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				sb.append("\n Posicao de chegada: " + count + " Lugar");
				sb.append("\n Código Piloto: " +  rs.getString("CODIGO"));
				sb.append("\n Nome Piloto: " + rs.getString("NOME"));
				sb.append("\n Qtd. de Voltas Concluidas: " + rs.getInt("ULTIMA_VOLTA"));
				sb.append("\n Tempo Total de Prova: " + String.format("%.3f", rs.getDouble("TEMPO_PROVA")) + " Minutos");
				sb.append("\n\n---------------------------------------\n");
				count++;
			}
			System.out.println(sb.toString());
		} catch (SQLException e6) {
			System.out.println("Erro ao selecionar dados: " + e6.getMessage());
		} finally {
			try {
				if (rs != null)
                    rs.close();
				if (stmt != null)
					stmt.close();
				if (c != null)
					c.close();
			} catch (SQLException e6) {
				System.out.println(e6.getMessage());
			}
		}
	}

	public void selectMelhorVolta() {
		StringBuilder sb = new StringBuilder();
		String sql = "SELECT P.CODIGO, P.NOME, R.VOLTA, R.TEMPO_VOLTA, R.VELOCIDADE_MEDIA_VOLTA"
				+ " FROM REGISTRO R JOIN CORRIDA C "
				+ " ON R.ID = C.ID_REGISTRO "
				+ " JOIN PILOTO P"
				+ " ON C.COD_PILOTO = P.CODIGO"
				+ " ORDER BY R.TEMPO_VOLTA ASC;";
		double tmp = 0;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
	
		try {
			c = this.conectar();
			if(c == null)
				return;
			
			stmt = c.createStatement(); 
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				sb.append("\n Código Piloto: " +  rs.getString("CODIGO"));
				sb.append("\n Nome Piloto: " + rs.getString("NOME"));
				sb.append("\n Volta: " + rs.getInt("VOLTA"));
				sb.append("\n Tempo da Volta: " + String.format("%.3f", rs.getDouble("TEMPO_VOLTA")) + " Minutos");
				tmp = rs.getDouble("VELOCIDADE_MEDIA_VOLTA") * 3.622;
				sb.append("\n Velocidade Media na Volta: " + String.format("%.2f", tmp) + " Km/h");
				sb.append("\n\n---------------------------------------\n");
			}
			System.out.println(sb.toString());
		} catch (SQLException e6) {
			System.out.println("Erro ao selecionar dados: " + e6.getMessage());
		} finally {
			try {
				if (rs != null)
                    rs.close();
				if (stmt != null)
					stmt.close();
				if (c != null)
					c.close();
			} catch (SQLException e6) {
				System.out.println(e6.getMessage());
			}
		}
	}
	
	public void selectVelMediaCorrida() {
		StringBuilder sb = new StringBuilder();
		String sql = "SELECT P.CODIGO, P.NOME, C.VELOCIDADE_MEDIA"
				+ " FROM CLASSIFICACAO C JOIN PILOTO P "
				+ " ON C.ID = P.ID_CLASSIFICACAO "
				+ " ORDER BY C.VELOCIDADE_MEDIA DESC;";

		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
	
		try {
			c = this.conectar();
			if(c == null)
				return;
			
			stmt = c.createStatement(); 
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				sb.append("\n Código Piloto: " +  rs.getString("CODIGO"));
				sb.append("\n Nome Piloto: " + rs.getString("NOME"));
				sb.append("\n Velocidade Media na Corrida: " + String.format("%.2f", rs.getDouble("VELOCIDADE_MEDIA")) + " Km/h");
				sb.append("\n\n-----------------------------------------\n");
			}
			System.out.println(sb.toString());
		} catch (SQLException e6) {
			System.out.println("Erro ao selecionar dados: " + e6.getMessage());
		} finally {
			try {
				if (rs != null)
                    rs.close();
				if (stmt != null)
					stmt.close();
				if (c != null)
					c.close();
			} catch (SQLException e6) {
				System.out.println(e6.getMessage());
			}
		}
	}

	public void selectVoltaCorrida() {
		StringBuilder sb = new StringBuilder();
		String sql = "SELECT P.CODIGO, P.NOME, R.VOLTA, R.TEMPO_VOLTA, R.VELOCIDADE_MEDIA_VOLTA"
				+ " FROM REGISTRO R JOIN CORRIDA C "
				+ " ON R.ID = C.ID_REGISTRO "
				+ " JOIN PILOTO P"
				+ " ON C.COD_PILOTO = P.CODIGO"
				+ " ORDER BY R.TEMPO_VOLTA ASC"
				+ " LIMIT 1";
		double tmp = 0;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
	
		try {
			c = this.conectar();
			if(c == null)
				return;
			
			stmt = c.createStatement(); 
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				sb.append(" Código Piloto: " +  rs.getString("CODIGO"));
				sb.append("\n Nome Piloto: " + rs.getString("NOME"));
				sb.append("\n Volta: " + rs.getInt("VOLTA"));
				sb.append("\n Tempo da Volta: " + String.format("%.3f", rs.getDouble("TEMPO_VOLTA")) + " Minutos");
				tmp = rs.getDouble("VELOCIDADE_MEDIA_VOLTA") * 3.622;
				sb.append("\n Velocidade Media na Volta: " + String.format("%.2f", tmp) + " Km/h");
				sb.append("\n\n---------------------------------------\n");
			}
			System.out.println(sb.toString());
		} catch (SQLException e6) {
			System.out.println("Erro ao selecionar dados: " + e6.getMessage());
		} finally {
			try {
				if (rs != null)
                    rs.close();
				if (stmt != null)
					stmt.close();
				if (c != null)
					c.close();
			} catch (SQLException e6) {
				System.out.println(e6.getMessage());
			}
		}
	}

	public void selectTempoChegada() {
		StringBuilder sb = new StringBuilder();
		String sql = "SELECT hora_chegada FROM CLASSIFICACAO ORDER BY tempo_prova ASC LIMIT 1";
		String vencedor;
		Connection c = null;
		Statement stmt = null;
		ResultSet rs = null;
	
		try {
			c = this.conectar();
			if(c == null)
				return;
			
			stmt = c.createStatement(); 
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				vencedor = rs.getString("hora_chegada");
				System.out.println(vencedor);
				sb.append("\n\n-----------------------------------------\n");
			}
			System.out.println(sb.toString());
		} catch (SQLException e6) {
			System.out.println("Erro ao selecionar dados: " + e6.getMessage());
		} finally {
			try {
				if (rs != null)
                    rs.close();
				if (stmt != null)
					stmt.close();
				if (c != null)
					c.close();
			} catch (SQLException e6) {
				System.out.println(e6.getMessage());
			}
		}
	}
}
