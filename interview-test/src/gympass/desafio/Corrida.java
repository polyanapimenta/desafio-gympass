package gympass.desafio;

import gympass.desafio.bancodados.LeitorArquivo;
import gympass.desafio.bancodados.SQLiteJDBC;


/**
 * @author Polyana Pimenta
 * 
 * Esta classe serve para implementar os comportamentos/problemas composto no desafio
 * 
 * 		// Outros métodos que podem ser visualizados na saida
 *		rs.selectPiloto();
 *		rs.selectCorrida();
 *		rs.selectRegistro();
 *		rs.selectClassificacao();
 *
 */
public class Corrida {
	private SQLiteJDBC rs = new SQLiteJDBC();
	Corrida() {
		LeitorArquivo input = new LeitorArquivo("log.txt");
		
		input.extrairDados();
	}
	
	void resultadoCorrida() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n---------------------------------------\n");
		sb.append("\tRESULTADO DA CORRIDA");
		sb.append("\n---------------------------------------\n");
		System.out.println(sb.toString());
		rs.selectPosicaoChegada();
	}
	
	void melhorVoltaCadaPiloto() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n---------------------------------------\n");
		sb.append("\tMELHOR VOLTA POR PILOTO");
		sb.append("\n---------------------------------------\n");
		System.out.println(sb.toString());
		rs.selectMelhorVolta();
	}
	
	void melhorVoltaNaCorrida() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n-----------------------------------------\n");
		sb.append("\tMELHOR VOLTA NA CORRIDA");
		sb.append("\n-----------------------------------------\n");
		System.out.println(sb.toString());
		rs.selectVoltaCorrida();
	}
	
	void velocidadeMediaCadaPiloto() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n-----------------------------------------\n");
		sb.append("\tVELOCIDADE MEDIA NA CORRIDA");
		sb.append("\n-----------------------------------------\n");
		System.out.println(sb.toString());
		rs.selectVelMediaCorrida();
	}
	
	void tempoChegadaAposVencedor() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n-----------------------------------------\n");
		sb.append("\tTEMPO CHEGADA APÓS VENCEDOR");
		sb.append("\n-----------------------------------------\n");
		System.out.println(sb.toString());
		rs.selectTempoChegada();
	}
	
	void seleionarTodasTabelas() {
		rs.selectPiloto();
		rs.selectCorrida();
		rs.selectRegistro();
		rs.selectClassificacao();
	}
}
