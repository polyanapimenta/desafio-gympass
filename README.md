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

Modelo Entidade Relacionamento:

> ![MER](/mer.png "Modelo Entidade Relacionamento")

Diagrama Entidade Relacionamento:

> ![DER](/der.png "Diagrama Entidade Relacionamento")


### Unidades de medidas consideradas no progrma: 
> #### Tabela Classificacao:
> * velocidade_media lê em Km/h
> * tempo_prova lê em minuto
> #### Tabela Registro:
> * velocidade_media_volta lê em m/s
> * tempo_volta lê em Minutos  

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
038	F.MASSA	  	        1
033	R.BARRICHELLO		2
002	K.RAIKKONEN		3
023	M.WEBBER		4
015	F.ALONSO		5
011	S.VETTEL		6
```

```
CORRIDA:
cod_piloto	id_registro
038		1
033		2
002		3
023		4
015		5
038		6
033		7
002		8
023		9
015		10
038		11
033		12
002		13
023		14
015		15
011		16
038		17
033		18
002		19
023		20
015		21
011		22
011		23
```

```
REGISTRO:
id	hora		volta	tempo_volta	velocidade_media_volta
1	23:49:08.277	1	 1,02852		 44,28
2	23:49:10.858	1	 1,04352		 43,24
3	23:49:11.075	1	 1,04108		 43,41
4	23:49:12.667	1	 1,04414		 43,20
5	23:49:30.976	1	 1,18456		 35,47
6	23:50:11.447	2	 1,03170		 44,05
7	23:50:14.860	2	 1,04002		 43,48
8	23:50:15.057	2	 1,03982		 43,49
9	23:50:17.472	2	 1,04805		 42,94
10	23:50:37.987	2	 1,07011		 41,53
11	23:51:14.216	3	 1,02769		 44,33
12	23:51:18.576	3	 1,03716		 43,68
13	23:51:19.044	3	 1,03987		 43,49
14	23:51:21.759	3	 1,04287		 43,29
15	23:51:46.691	3	 1,08704		 40,50
16	23:52:01.796	1	 3,31315		 13,17
17	23:52:17.003	4	 1,02787		 44,32
18	23:52:22.586	4	 1,04010		 43,47
19	23:52:22.120	4	 1,03076		 44,12
20	23:52:25.975	4	 1,04216		 43,34
21	23:53:06.741	4	 1,20050		 34,76
22	23:53:39.660	2	 1,37864		 28,44
23	23:54:57.757	3	 1,18097		 35,63
```

```
CLASSIFICACAO:
id	hora_chegada	ultima_volta	tempo_prova	velocidade_media
1	23:52:17.003	4		 4,11578		 160,26
2	23:52:22.586	4		 4,16080		 157,44
3	23:52:22.120	4		 4,15153		 158,02
4	23:52:25.975	4		 4,17722		 156,44
5	23:53:06.741	4		 4,54221		 137,88
6	23:54:57.757	3		 5,87276		 93,25

```

Saída no método resultadoCorrida()
----------------------------------
#### resultadoCorrida()

```
---------------------------------------
	RESULTADO DA CORRIDA
---------------------------------------


 Posicao de chegada: 1 Lugar
 Código Piloto: 038
 Nome Piloto: F.MASSA
 Qtd. de Voltas Concluidas: 4
 Tempo Total de Prova: 4,116 Minutos

---------------------------------------

 Posicao de chegada: 2 Lugar
 Código Piloto: 002
 Nome Piloto: K.RAIKKONEN
 Qtd. de Voltas Concluidas: 4
 Tempo Total de Prova: 4,152 Minutos

---------------------------------------

 Posicao de chegada: 3 Lugar
 Código Piloto: 033
 Nome Piloto: R.BARRICHELLO
 Qtd. de Voltas Concluidas: 4
 Tempo Total de Prova: 4,161 Minutos

---------------------------------------

 Posicao de chegada: 4 Lugar
 Código Piloto: 023
 Nome Piloto: M.WEBBER
 Qtd. de Voltas Concluidas: 4
 Tempo Total de Prova: 4,177 Minutos

---------------------------------------

 Posicao de chegada: 5 Lugar
 Código Piloto: 015
 Nome Piloto: F.ALONSO
 Qtd. de Voltas Concluidas: 4
 Tempo Total de Prova: 4,542 Minutos

---------------------------------------

 Posicao de chegada: 6 Lugar
 Código Piloto: 011
 Nome Piloto: S.VETTEL
 Qtd. de Voltas Concluidas: 3
 Tempo Total de Prova: 5,873 Minutos

---------------------------------------
```

Saída nos demais métodos bônus:
-------------------------------

#### melhorVoltaCadaPiloto()

#### melhorVoltaNaCorrida()

```
-----------------------------------------
	MELHOR VOLTA NA CORRIDA
-----------------------------------------

 Código Piloto: 038
 Nome Piloto: F.MASSA
 Volta: 3
 Tempo da Volta: 1,028 Minutos
 Velocidade Media na Volta: 160,58 Km/h

---------------------------------------
```

#### velocidadeMediaCadaPiloto()

```
-----------------------------------------
	VELOCIDADE MEDIA NA CORRIDA
-----------------------------------------


 Código Piloto: 038
 Nome Piloto: F.MASSA
 Velocidade Media na Corrida: 160,26 Km/h

-----------------------------------------

 Código Piloto: 002
 Nome Piloto: K.RAIKKONEN
 Velocidade Media na Corrida: 158,02 Km/h

-----------------------------------------

 Código Piloto: 033
 Nome Piloto: R.BARRICHELLO
 Velocidade Media na Corrida: 157,44 Km/h

-----------------------------------------

 Código Piloto: 023
 Nome Piloto: M.WEBBER
 Velocidade Media na Corrida: 156,44 Km/h

-----------------------------------------

 Código Piloto: 015
 Nome Piloto: F.ALONSO
 Velocidade Media na Corrida: 137,88 Km/h

-----------------------------------------

 Código Piloto: 011
 Nome Piloto: S.VETTEL
 Velocidade Media na Corrida: 93,25 Km/h

-----------------------------------------
```
#### tempoChegadaAposVencedor()

Instruções para rodar o projeto:
--------------------------------

* Usar máquina Windows (O projeto não foi testado em Linux ou Mac, podendo haver variações de diretorio)
* IDE sugerida: Eclipse
* Copiar toda a pasta "\sqlite" para a raiz de C:\
* **Feito isso, verificar se o caminho segue igual a: C:\sqlite\db**
* **Obs: Dentro da IDE Eclipse verificar o jar que se encontra no diretorio "~\desafio-gympass\interview-test\lib\sqlite-jdbc-3.23.1.jar" está dentro do [Java Build Path (em Referenced Libraries)](https://pt.wikihow.com/Adicionar-JARs-nos-Caminhos-de-Acesso-de-Projeto-em-Eclipse-(Java))**
* Apenas isso para rodar o projeto na IDE..
