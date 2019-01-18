package gympass.desafio;

/**
 * @author Polyana Pimenta
 * 
 *         Desafio:
 *         
 *         A partir de um input de um arquivo de log, montar o resultado da
 *         corrida com as seguintes informa��es: Posi��o Chegada, C�digo Piloto,
 *         Nome Piloto, Qtde Voltas Completadas e Tempo Total de Prova.
 * 
 *         Obs:
 *
 *         - A corrida termina quando o primeiro colocado completa 4 voltas
 *         (Ganha quem completa o numero de voltas em um tempo mais curto)
 *         
 *         B�nus:
 *         
 *         Descobrir a melhor volta de cada piloto
 *         Descobrir a melhor volta da corrida
 *         Calcular a velocidade m�dia de cada piloto durante toda corrida
 *         Descobrir quanto tempo cada piloto chegou ap�s o vencedor
 *         
 *         OBS: Linhas de c�digo comentadas podem serem descomentadas para vizualiza��o na sa�da.
 */

public class CorridaTeste {
	
	public static void main(String[] args) {
		LimparDiretorio ld = new LimparDiretorio();
		ld.remover();
		
		Corrida corrida = new Corrida();
		
//		corrida.seleionarTodasTabelas();
		
		corrida.resultadoCorrida();
		
//		corrida.melhorVoltaCadaPiloto();
		
//		corrida.melhorVoltaNaCorrida();
		
//		corrida.velocidadeMediaCadaPiloto();
	
//		corrida.tempoChegadaAposVencedor();	
	}
}
