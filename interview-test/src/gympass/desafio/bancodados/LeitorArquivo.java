package gympass.desafio.bancodados;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Polyana Pimenta
 * 
 *         Esta classe serve para extrair os registros de uma corrida de kart
 *         contidos no arquivo 'log.txt' na raiz da pasta do projeto.
 */

public class LeitorArquivo {
	private String nomeArquivo = null;

	public LeitorArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public void extrairDados() {
		SQLiteJDBC bancoDados = new SQLiteJDBC(true);
		BufferedReader bufferConteudo = null;
		String[] registro = null;

		try {
			FileInputStream dadosExternos = new FileInputStream(nomeArquivo);
			InputStreamReader arquivo = new InputStreamReader(dadosExternos, StandardCharsets.UTF_8);
			bufferConteudo = new BufferedReader(arquivo);

			String linha;

			do {
				linha = bufferConteudo.readLine();

				if (linha != null) {
					linha = linha.replace(" – ", ";").replace(",", ".");

					registro = linha.split(";");
					registro[4] = registro[4].replace(".", "").replace(":", ".");
					
					bancoDados.popularRelacaoMM(registro);
				}
			} while (linha != null);

			bancoDados.popularClassificacao();
			
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo nao encontrado: " + e.getMessage());

		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Nao existe dado no indice: " + e.getMessage());

		} catch (IOException e) {
			System.out.println("IO Erro: " + e.getMessage());

		} finally {

			if (bufferConteudo != null) {
				try {
					bufferConteudo.close();
				} catch (IOException e) {
					System.out.println("IO Erro ao fechar o arquivo: " + e.getMessage());
				}
			}
		}
	}
}
