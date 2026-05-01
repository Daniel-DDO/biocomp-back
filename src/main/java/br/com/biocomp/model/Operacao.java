package br.com.biocomp.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operacao {
    private TipoOperacao tipo;
    private int posicao;
    private Character valor;
}
