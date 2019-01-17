Desafio Gympass
===============

A partir de um input de um arquivo de log, montar o resultado da corrida com as seguintes informações: 
Posição Chegada, Código Piloto, Nome Piloto, Qtde Voltas Completadas e Tempo Total de Prova.

Obs:
* A corrida termina quando o primeiro colocado completa 4 voltas
  (Ganha quem completa o numero de voltas em um tempo mais curto)
  
 Bônus:
 ------
 * Descobrir a melhor volta de cada piloto
 * Descobrir a melhor volta da corrida
 * Calcular a velocidade média de cada piloto durante toda corrida
 * Descobrir quanto tempo cada piloto chegou após o vencedor

Solução:
--------

A solução foi desenvolvida em Java, utilizando a estratégia de criar uma modelagem de banco de dados relacional com [SQLite](http://www.sqlitetutorial.net/sqlite-java/) para armazenar os dados de log da corrida de kart de forma organizada de acordo com o DER abaixo.

Diagrama de entidade e relacionamento:

> Inserir DER <

Instruções para rodar o projeto:
--------------------------------

* Usar máquina Windows (O projeto não foi testado em Linux ou Mac, podendo haver variações de diretorio)
* IDE sugerida: Eclipse
* Extrair SQLite.zip em C:\
* **Após ter feito a extração, verificar se o caminho segue igual a: C:\sqlite\db**
* **Obs: Dentro da IDE Eclipse verificar o jar que se encontra no diretorio "~\interview-test\lib\sqlite-jdbc-3.23.1.jar" está dentro do [Java Build Path (em Referenced Libraries)](https://pt.wikihow.com/Adicionar-JARs-nos-Caminhos-de-Acesso-de-Projeto-em-Eclipse-(Java))**
* Apenas isso para rodar o projeto na IDE..
