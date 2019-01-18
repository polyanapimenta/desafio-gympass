package gympass.desafio;

import java.io.File;


/**
 * @author Polyana Pimenta
 * 
 * Esta classe serve para deletar o arquivo 'corrida.db' 
 * toda vez que o programa for rodado para que nao acumule dados desnecessarios nas tabelas
 * tendo a iniciacao, criacao de tabelas e inserts no banco de dados a partir do zero.
 *
 */
public class LimparDiretorio {
	void remover() {
		File f = new File("C:\\sqlite\\db\\corrida.db");
		f.delete();
	}
}
