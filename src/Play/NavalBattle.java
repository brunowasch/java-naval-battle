package Play;

import java.util.Random;
import java.util.Scanner;

public class NavalBattle {

    // Define modo de posicionamento de barcos
    public static void posicionarNavios(int[][] tabuleiro, String jogador) {
        Scanner ler = new Scanner(System.in);
        System.out.println("Você deseja posicionar os navios manualmente ou automaticamente?");
        System.out.println("1 - Manual");
        System.out.println("2 - Automático");
        int escolha = ler.nextInt();
        if (escolha == 1) {
            posicionarNaviosManualmente(tabuleiro, jogador);
        } else {
            posicionarNaviosAutomaticamente(tabuleiro);
        }
    }

    // Jogo contra a máquina
    public static void singlePlayer() {
        Scanner ler = new Scanner(System.in);
        System.out.println("\nPressione ENTER e passe para começar o jogo...");
        new Scanner(System.in).nextLine();

        System.out.println("Vamos começar a brincadeira! 😉");

        // Inicializa tabuleiros
        int[][] tabuleiroJogador = new int[10][10];
        int[][] tabuleiroMaquina = new int[10][10];
        boolean[][] jaAtacou = new boolean[10][10];
        int[] ultimoAcerto = {-1, -1};

        posicionarNavios(tabuleiroJogador, "Jogador");
        posicionarNaviosAutomaticamente(tabuleiroMaquina);

        int acertosJogador = 0;
        int acertosMaquina = 0;
        int totalNavios = 20;

        while (acertosJogador < totalNavios && acertosMaquina < totalNavios) {
            System.out.println("\n🔫 Sua vez de atacar!");
            mostrarTabuleiro(tabuleiroMaquina, false);
            jogarRodada(tabuleiroMaquina);

            acertosJogador = contarAcertos(tabuleiroMaquina);
            if (acertosJogador == totalNavios) break;

            System.out.println("\n🤖 Vez da máquina:");
            mostrarTabuleiro(tabuleiroJogador, false);
            jogadaMaquinaSimples(tabuleiroJogador, jaAtacou, ultimoAcerto);

            acertosMaquina = contarAcertos(tabuleiroJogador);
        }

        // Resultado final
        System.out.println("\n🏁 Fim de jogo!");
        if (acertosJogador == totalNavios) {
            System.out.println("🎉 Você venceu! Todos os navios da máquina foram afundados!");
        } else {
            System.out.println("🤖 A máquina venceu! Todos os seus navios foram afundados!");
        }

        System.out.println("\n🧭 Seu tabuleiro:");
        mostrarTabuleiro(tabuleiroJogador, true);

        System.out.println("\n🧭 Tabuleiro da máquina:");
        mostrarTabuleiro(tabuleiroMaquina, true);
    }
    // Jogo multiplayer
    public static void multiPlayer() {
        Scanner ler = new Scanner(System.in);
        System.out.print("Jogador 1, digite seu username: ");
        String player1 = ler.nextLine();

        System.out.print("Jogador 2, digite seu username: ");
        String player2 = ler.nextLine();

        int[][] tabuleiro1 = new int[10][10];
        int[][] tabuleiro2 = new int[10][10];
        ler = new Scanner(System.in);
        System.out.println(" " + player1 + ",");
        posicionarNavios(tabuleiro1, player1);
        System.out.println("\nPressione ENTER e passe para o próximo jogador...");
        new Scanner(System.in).nextLine();
        System.out.println(" " + player2 + ",");
        posicionarNavios(tabuleiro2, player2);


        int acertos1 = 0;
        int acertos2 = 0;
        int totalNavios = 20;
        boolean vezDoJogador1 = true;

        while (acertos1 < totalNavios && acertos2 < totalNavios) {
            String jogadorAtual = vezDoJogador1 ? player1 : player2;
            int[][] tabuleiroInimigo = vezDoJogador1 ? tabuleiro2 : tabuleiro1;

            System.out.println("\n=======================");
            System.out.println("Vez de " + jogadorAtual);
            mostrarTabuleiro(tabuleiroInimigo, false);
            jogarRodada(tabuleiroInimigo);

            int acertosAtual = 0;
            for (int[] linha : tabuleiroInimigo) {
                for (int celula : linha) {
                    if (celula == 2) acertosAtual++;
                }
            }

            if (vezDoJogador1) {
                acertos1 = acertosAtual;
            } else {
                acertos2 = acertosAtual;
            }

            if (acertos1 == totalNavios || acertos2 == totalNavios) {
                break;
            }

            System.out.println("Pressione ENTER para passar o turno...");
            ler.nextLine();
            vezDoJogador1 = !vezDoJogador1;
        }

        // Exibe o vencedor
        System.out.println("\n=======================");
        if (acertos1 == totalNavios) {
            System.out.println("🎉 " + player1 + " venceu! Todos os navios do " + player2 + " foram afundados!");
            System.out.println("Tabuleiro do " + player2 + ":");
            mostrarTabuleiro(tabuleiro2, true);
        } else {
            System.out.println("🎉 " + player2 + " venceu! Todos os navios do " + player1 + " foram afundados!");
            System.out.println("Tabuleiro do " + player1 + ":");
            mostrarTabuleiro(tabuleiro1, true);
        }
    }

    // Função principal
    public static void main(String[] args) {
        Scanner ler = new Scanner(System.in);
        String user;
        System.out.println("== Batalha Naval == ");
        System.out.print("Dê um nome para seu usuário: ");
        user = ler.nextLine();
        ler = new Scanner(System.in);

        // Escolha do modo de jogo
        System.out.println("Olá, " + user + "! Você deseja jogar:\n1-Singleplayer (contra a máquina)\n2-Multiplayer");
        int tipoJogo = ler.nextInt();
        if (tipoJogo == 1) {
            singlePlayer();
        } else if (tipoJogo == 2) {
            multiPlayer();
        }
    }

    // Representação: 0 = água, 1 = navio, 2 = acerto, 3 = erro (Posiciona navios manualmente no tabuleiro)
    public static void posicionarNaviosManualmente(int[][] tabuleiro, String jogador) {
        Scanner ler = new Scanner(System.in);
        int[] tamanhos = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
        System.out.println("\n" + jogador + ", posicione seus navios no tabuleiro!");

        for (int i = 0; i < tamanhos.length; i++) {
            int tamanho = tamanhos[i];
            boolean posicionado = false;

            while (!posicionado) {
                System.out.println("\nNavio de " + tamanho + " espaço(s).");
                mostrarTabuleiro(tabuleiro, true);
                System.out.print("Informe a linha inicial (0 a 9): ");
                int linha = ler.nextInt();

                System.out.print("Informe a coluna inicial (A a J): ");
                char colunaChar = ler.next().toUpperCase().charAt(0);
                int coluna = colunaChar - 'A';

                System.out.print("Direção (H para horizontal, V para vertical): ");
                char direcao = ler.next().toUpperCase().charAt(0);

                if (verificarPosicaoValida(tabuleiro, linha, coluna, tamanho, direcao)) {
                    for (int j = 0; j < tamanho; j++) {
                        if (direcao == 'H') {
                            tabuleiro[linha][coluna + j] = 1;
                        } else {
                            tabuleiro[linha + j][coluna] = 1;
                        }
                    }
                    posicionado = true;
                } else {
                    System.out.println("❌ Posição inválida ou sobreposição detectada. Tente novamente.");
                }
            }
        }
    }
    // Verifica se é possível posicionar o navio sem sair do tabuleiro ou sobrepor
    public static boolean verificarPosicaoValida(int[][] tabuleiro, int linha, int coluna, int tamanho, char direcao) {
        if (direcao == 'H') {
            if (coluna + tamanho > 10) return false;
            for (int i = 0; i < tamanho; i++) {
                if (tabuleiro[linha][coluna + i] != 0) return false;
            }
        } else if (direcao == 'V') {
            if (linha + tamanho > 10) return false;
            for (int i = 0; i < tamanho; i++) {
                if (tabuleiro[linha + i][coluna] != 0) return false;
            }
        } else {
            return false;
        }
        return true;
    }

    // Tabuleiro
    public static void mostrarTabuleiro(int[][] tabuleiro, boolean mostrarNavios) {
        System.out.print("   ");
        for (char coluna = 'A'; coluna <= 'J'; coluna++) {
            System.out.printf("%-3s", coluna);
        }
        System.out.println();

        for (int l = 0; l < 10; l++) {
            System.out.printf("%-3d", l);
            for (int c = 0; c < 10; c++) {
                if (tabuleiro[l][c] == 2) {
                    System.out.print("💥 ");
                } else if (tabuleiro[l][c] == 3) {
                    System.out.print("🌊 ");
                } else if (mostrarNavios && tabuleiro[l][c] == 1) {
                    System.out.print("🚢 ");
                } else {
                    System.out.print("*  ");
                }
            }
            System.out.println();
        }
    }

    // Jogada humana
    public static void jogarRodada(int[][] tabuleiro) {
        Scanner ler = new Scanner(System.in);
        System.out.print("Informe a linha (0 a 9): ");
        int linha = ler.nextInt();

        System.out.print("Informe a coluna (A a J): ");
        char colChar = ler.next().toUpperCase().charAt(0);
        int coluna = colChar - 'A';

        if (linha < 0 || linha >= 10 || coluna < 0 || coluna >= 10) {
            System.out.println("Posição inválida! Tente novamente.");
            return;
        }

        if (tabuleiro[linha][coluna] == 1) {
            tabuleiro[linha][coluna] = 2;
            System.out.println("💥 Você acertou um navio!");
        } else if (tabuleiro[linha][coluna] == 0) {
            tabuleiro[linha][coluna] = 3;
            System.out.println("💦 Tiro na água!");
        } else {
            System.out.println("Você já atirou aqui. Tente outra posição.");
        }
    }

    // Marca a jogada da máquina
    public static void marcarJogada(int[][] tabuleiro, boolean[][] jaAtacou, int linha, int coluna, int[] ultimoAcerto) {
        jaAtacou[linha][coluna] = true;
        if (tabuleiro[linha][coluna] == 1) {
            tabuleiro[linha][coluna] = 2;
            ultimoAcerto[0] = linha;
            ultimoAcerto[1] = coluna;
            System.out.println("🤖 A máquina acertou um navio! 💥");
        } else {
            tabuleiro[linha][coluna] = 3;
            ultimoAcerto[0] = -1;
            ultimoAcerto[1] = -1;
            System.out.println("🤖 A máquina deu um tiro na água! 💦");
        }
    }

    // Jogada automática
    public static void jogadaMaquinaSimples(int[][] tabuleiro, boolean[][] jaAtacou, int[] ultimoAcerto) {
        Random aleatorio = new Random();
        int linha, coluna;

        if (ultimoAcerto[0] != -1) {
            int[][] direcoes = {{-1,0},{1,0},{0,-1},{0,1}};
            for (int[] d : direcoes) {
                int novaLinha = ultimoAcerto[0] + d[0];
                int novaColuna = ultimoAcerto[1] + d[1];
                if (novaLinha >= 0 && novaLinha < 10 && novaColuna >= 0 && novaColuna < 10 && !jaAtacou[novaLinha][novaColuna]) {
                    linha = novaLinha;
                    coluna = novaColuna;
                    marcarJogada(tabuleiro, jaAtacou, linha, coluna, ultimoAcerto);
                    return;
                }
            }
        }

        do {
            linha = aleatorio.nextInt(10);
            coluna = aleatorio.nextInt(10);
        } while (jaAtacou[linha][coluna]);

        marcarJogada(tabuleiro, jaAtacou, linha, coluna, ultimoAcerto);
    }

    // Posiciona os navios aleatoriamente
    public static void posicionarNaviosAutomaticamente(int[][] tabuleiro) {
        Random aleatorio = new Random();
        int[] tamanhos = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

        for (int tamanho : tamanhos) {
            boolean posicionado = false;
            while (!posicionado) {
                int linha = aleatorio.nextInt(10);
                int coluna = aleatorio.nextInt(10);
                char direcao = aleatorio.nextBoolean() ? 'H' : 'V';

                if (verificarPosicaoValida(tabuleiro, linha, coluna, tamanho, direcao)) {
                    for (int j = 0; j < tamanho; j++) {
                        if (direcao == 'H') {
                            tabuleiro[linha][coluna + j] = 1;
                        } else {
                            tabuleiro[linha + j][coluna] = 1;
                        }
                    }
                    posicionado = true;
                }
            }
        }
    }

    // Conta acertos.
    public static int contarAcertos(int[][] tabuleiro) {
        int acertos = 0;
        for (int[] linha : tabuleiro) {
            for (int celula : linha) {
                if (celula == 2) acertos++;
            }
        }
        return acertos;
    }

}