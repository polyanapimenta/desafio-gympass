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

Exemplo de saída do programa:
-----------------------------

Conexao com SQLite estabelecida!

Criando tabelas no banco de dados..
```
CREATE TABLE IF NOT EXISTS CORRIDA (COD_PILOTO text, ID_REGISTRO integer, PRIMARY KEY (COD_PILOTO, ID_REGISTRO), FOREIGN KEY (COD_PILOTO) REFERENCES PILOTO (CODIGO), FOREIGN KEY (ID_REGISTRO) REFERENCES REGISTRO (ID));
CREATE TABLE IF NOT EXISTS PILOTO (codigo text PRIMARY KEY, nome text NOT NULL, id_classificacao, FOREIGN KEY (id_classificacao) REFERENCES CLASSIFICACAO (id));
CREATE TABLE IF NOT EXISTS REGISTRO (id integer PRIMARY KEY AUTOINCREMENT, hora text NOT NULL, volta int NOT NULL, tempo_volta real NOT NULL, velocidade_media_volta real NOT NULL);
CREATE TABLE IF NOT EXISTS CLASSIFICACAO (id integer PRIMARY KEY AUTOINCREMENT, hora_chegada text NOT NULL, ultima_volta int NOT NULL, tempo_prova real NOT NULL, velocidade_media real NOT NULL);
```

Inserindo registros.. Aguarde..

```
PILOTO:
codigo	nome		id_classificacao
038	F.MASSA		1
033	R.BARRICHELLO		2
002	K.RAIKKONEN		3
023	M.WEBBER		4
015	F.ALONSO		5
011	S.VETTEL		6
```


Instruções para rodar o projeto:
--------------------------------

* Usar máquina Windows (O projeto não foi testado em Linux ou Mac, podendo haver variações de diretorio)
* IDE sugerida: Eclipse
* Extrair SQLite.zip em C:\
* **Após ter feito a extração, verificar se o caminho segue igual a: C:\sqlite\db**
* **Obs: Dentro da IDE Eclipse verificar o jar que se encontra no diretorio "~\interview-test\lib\sqlite-jdbc-3.23.1.jar" está dentro do [Java Build Path (em Referenced Libraries)](https://pt.wikihow.com/Adicionar-JARs-nos-Caminhos-de-Acesso-de-Projeto-em-Eclipse-(Java))**
* Apenas isso para rodar o projeto na IDE..
