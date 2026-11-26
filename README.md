# Sudoku

Projeto de estudo e aprendizado em Java. Ele implementa um tabuleiro de Sudoku 9×9 e oferece duas formas de interação: uma interface gráfica com Swing e um menu de terminal. O projeto é simples e não usa banco de dados nem serviços externos.

## Tecnologias e requisitos

- JDK 21 ou superior, com o Java 21 selecionado para compilar o projeto.
- Apache Maven instalado e disponível no terminal como `mvn`.
- IntelliJ IDEA (opcional, recomendado para abrir a configuração gráfica já incluída).

O `pom.xml` não declara bibliotecas externas. Swing e AWT fazem parte do JDK.

## Baixar o projeto

Clone a branch `ui`, que contém a interface gráfica e esta documentação:

```bash
git clone --branch ui https://github.com/TheGabrielVieira/Sudoku.git
cd Sudoku
```

Confira as instalações:

```bash
java -version
mvn -version
```

## Compilar

Na pasta do projeto, execute:

```bash
mvn clean package
```

As classes compiladas ficam em `target/classes`.

## Executar pela IntelliJ IDEA

1. Abra a pasta clonada como projeto Maven (ou abra o `pom.xml`) e aguarde a importação.
2. Selecione a configuração **Sudoku UI** na lista de configurações de execução.
3. Execute-a. A configuração compartilhada em `.run/Sudoku UI.run.xml` seleciona `br.com.sudokuproject.UIMain` e já fornece os argumentos para um tabuleiro válido.

Para executar a versão de terminal, crie uma configuração do tipo **Application** com a classe principal `br.com.sudokuproject.Main`. Use os mesmos argumentos descritos abaixo e execute. O menu permite iniciar o jogo, inserir e remover números, visualizar o tabuleiro, conferir o estado, limpar e finalizar.

## Executar pelo terminal

Depois de compilar, a interface gráfica pode ser iniciada com:

```text
java -cp target/classes br.com.sudokuproject.UIMain <argumentos do tabuleiro>
```

Para usar o modo de terminal, substitua a classe principal por `br.com.sudokuproject.Main`:

```text
java -cp target/classes br.com.sudokuproject.Main <argumentos do tabuleiro>
```

Passe os argumentos do tabuleiro abaixo no lugar de `<argumentos do tabuleiro>`. Cada entrada deve ser um argumento separado. Como a configuração contém `;`, coloque cada argumento entre aspas ao executar diretamente por um shell.

## Formato da configuração do tabuleiro

O programa exige uma entrada para cada uma das 81 casas, inclusive as que podem ser editadas. O formato de cada argumento é:

```text
coluna,linha;valorEsperado,fixed
```

- `coluna` e `linha`: coordenadas entre `0` e `8`.
- `valorEsperado`: número entre `1` e `9` usado para inicializar a casa. Para uma casa fixa, esse valor é exibido e não pode ser editado.
- `fixed`: `true` para uma pista fixa ou `false` para uma casa editável.

Este exemplo é uma configuração completa e válida. Os argumentos estão agrupados por linha do tabuleiro; espaços ou quebras de linha separam argumentos:

```text
0,0;5,true 1,0;3,true 2,0;4,false 3,0;6,false 4,0;7,true 5,0;8,false 6,0;9,false 7,0;1,false 8,0;2,false
0,1;6,true 1,1;7,false 2,1;2,false 3,1;1,true 4,1;9,true 5,1;5,true 6,1;3,false 7,1;4,false 8,1;8,false
0,2;1,false 1,2;9,true 2,2;8,true 3,2;3,false 4,2;4,false 5,2;2,false 6,2;5,false 7,2;6,true 8,2;7,false
0,3;8,true 1,3;5,false 2,3;9,false 3,3;7,false 4,3;6,true 5,3;1,false 6,3;4,false 7,3;2,false 8,3;3,true
0,4;4,true 1,4;2,false 2,4;6,false 3,4;8,true 4,4;5,false 5,4;3,true 6,4;7,false 7,4;9,false 8,4;1,true
0,5;7,true 1,5;1,false 2,5;3,false 3,5;9,false 4,5;2,true 5,5;4,false 6,5;8,false 7,5;5,false 8,5;6,true
0,6;9,false 1,6;6,true 2,6;1,false 3,6;5,false 4,6;3,false 5,6;7,false 6,6;2,true 7,6;8,true 8,6;4,false
0,7;2,false 1,7;8,false 2,7;7,false 3,7;4,true 4,7;1,true 5,7;9,true 6,7;6,false 7,7;3,false 8,7;5,true
0,8;3,false 1,8;4,false 2,8;5,false 3,8;2,false 4,8;8,true 5,8;6,false 6,8;1,false 7,8;7,true 8,8;9,true
```

O parser rejeita entradas malformadas, coordenadas fora do intervalo, posições repetidas ou uma configuração incompleta. Ao usar um shell, não digite literalmente `<argumentos do tabuleiro>`: substitua esse texto pelos 81 argumentos e coloque cada argumento entre aspas, por exemplo `"0,0;5,true"`.

## Estrutura do código

```text
src/main/java/br/com/sudokuproject/
├── Main.java                  # Entrada do modo de terminal
├── UIMain.java                # Entrada da interface gráfica
├── model/                     # Tabuleiro, casas e estado da partida
├── service/                   # Serviço do tabuleiro e notificações da UI
├── ui/custom/                 # Componentes e montagem da interface Swing
└── util/                      # Parser da configuração e modelo textual do tabuleiro
```

- `model/Board` guarda as casas, calcula o estado da partida e verifica repetições em linhas, colunas e setores 3×3.
- `model/Space` representa uma casa, incluindo valor atual, valor de configuração e se ela é fixa.
- `service/BoardService` adapta as operações do modelo para a interface gráfica. `NotifierService` notifica os campos quando o tabuleiro é reiniciado.
- `ui/custom/` contém a janela, os painéis, os campos numéricos, os botões e a tela principal.
- `util/GameConfigParser` valida os argumentos antes de criar o jogo. `BoardTemplate` formata a visualização em texto usada pelo terminal.

## Como o jogo funciona

Na inicialização, os argumentos são validados e convertidos em uma configuração 9×9. Casas marcadas com `true` começam preenchidas e bloqueadas; casas com `false` começam vazias e editáveis. A partida pode ser consultada e reiniciada. Uma partida é considerada completa quando todas as casas têm valor e não há números repetidos em nenhuma linha, coluna ou setor 3×3.

Este projeto foi desenvolvido para praticar Java, orientação a objetos, Swing, organização em camadas e comunicação por eventos. É um projeto de aprendizado, não um produto finalizado.
