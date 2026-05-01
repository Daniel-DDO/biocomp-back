package br.com.biocomp.service;

import br.com.biocomp.dto.MatrizResponse;
import br.com.biocomp.dto.PassoAnimacaoResponse;
import br.com.biocomp.model.Operacao;
import br.com.biocomp.model.Resultado;
import br.com.biocomp.model.TipoOperacao;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DistanciaEdicaoService {

    public Resultado calcular(String texto1, String texto2) {
        //monta a matriz de programação dinâmica
        int[][] matriz = montarMatriz(texto1, texto2);

        //backtracking
        List<Operacao> operacoes = reconstruirOperacoes(texto1, texto2, matriz);

        //a distância fica na última célula da matriz
        int distancia = matriz[texto1.length()][texto2.length()];

        return new Resultado(distancia, operacoes);
    }

    private int[][] montarMatriz(String texto1, String texto2) {

        int n = texto1.length();
        int m = texto2.length();

        int[][] matriz = new int[n + 1][m + 1];

        //inicialização

        for (int i = 0; i <= n; i++) {
            matriz[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            matriz[0][j] = j;
        }

        //preenche a matriz
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {

                char c1 = texto1.charAt(i - 1);
                char c2 = texto2.charAt(j - 1);

                if (c1 == c2) {
                    matriz[i][j] = matriz[i - 1][j - 1]; //match
                } else {
                    int replace = matriz[i - 1][j - 1]; //vem da diagonal esquerda superior

                    int insert = matriz[i][j - 1]; //vem da esquerda

                    int delete = matriz[i - 1][j]; //vem de cima

                    //escolhe o menor custo
                    matriz[i][j] = 1 + Math.min(
                            replace,
                            Math.min(insert, delete)
                    );
                }
            }
        }

        return matriz;
    }

    //backtracking
    //prioridade:
    //1º replace ou match
    //2º insert, 3º delete
    private List<Operacao> reconstruirOperacoes(
            String texto1,
            String texto2,
            int[][] matriz
    ) {

        List<Operacao> operacoes = new ArrayList<>();

        int i = texto1.length();
        int j = texto2.length();

        //começa no final da matriz
        while (i > 0 || j > 0) {

            //match ou replace
            if (i > 0 && j > 0) {

                char c1 = texto1.charAt(i - 1);
                char c2 = texto2.charAt(j - 1);

                //match
                if (c1 == c2 &&
                        matriz[i][j] == matriz[i - 1][j - 1]) {

                    i--;
                    j--;
                    continue;
                }

                //replace
                if (matriz[i][j] == matriz[i - 1][j - 1] + 1) {

                    operacoes.add(new Operacao(TipoOperacao.REPLACE, i, c2));

                    i--;
                    j--;
                    continue;
                }
            }

            //insert
            if (j > 0 &&
                    matriz[i][j] == matriz[i][j - 1] + 1) {

                operacoes.add(new Operacao(TipoOperacao.INSERT, i + 1, texto2.charAt(j - 1)));

                j--;
                continue;
            }

            //delete
            if (i > 0 &&
                    matriz[i][j] == matriz[i - 1][j] + 1) {

                operacoes.add(new Operacao(TipoOperacao.DELETE, i, null));

                i--;
            }
        }

        Collections.reverse(operacoes);
        ajustarPosicoes(operacoes);

        return operacoes;
    }

    //ajusta as posições dps de insert e delete porque a string muda durante a execução
    private void ajustarPosicoes(List<Operacao> operacoes) {

        int deslocamento = 0;

        for (Operacao op : operacoes) {

            op.setPosicao(op.getPosicao() + deslocamento);

            if (op.getTipo() == TipoOperacao.INSERT) {
                deslocamento++;
            }

            if (op.getTipo() == TipoOperacao.DELETE) {
                deslocamento--;
            }
        }
    }

    //animações para o frontend
    public List<PassoAnimacaoResponse> gerarPassosAnimacao(
            String texto1,
            String texto2
    ) {

        Resultado resultado = calcular(texto1, texto2);
        List<Operacao> operacoes = resultado.getOperacoes();

        List<PassoAnimacaoResponse> passos = new ArrayList<>();

        String atual = texto1;

        for (Operacao op : operacoes) {

            String antes = atual;
            String depois = aplicarOperacao(atual, op);

            passos.add(
                    new PassoAnimacaoResponse(
                            antes,
                            op.getTipo().name(),
                            depois
                    )
            );

            atual = depois;
        }

        return passos;
    }

    private String aplicarOperacao(
            String texto,
            Operacao operacao
    ) {

        StringBuilder sb = new StringBuilder(texto);

        int pos = operacao.getPosicao() - 1;

        if (operacao.getTipo() == TipoOperacao.INSERT) {
            sb.insert(pos, operacao.getValor());
        }

        if (operacao.getTipo() == TipoOperacao.DELETE) {
            sb.deleteCharAt(pos);
        }

        if (operacao.getTipo() == TipoOperacao.REPLACE) {
            sb.setCharAt(pos, operacao.getValor());
        }

        return sb.toString();
    }

    //visualização da matriz
    public MatrizResponse gerarMatriz(
            String texto1,
            String texto2
    ) {

        int[][] matriz = montarMatriz(texto1, texto2);

        int distancia = matriz[texto1.length()][texto2.length()];

        return new MatrizResponse(
                distancia,
                matriz
        );
    }

}