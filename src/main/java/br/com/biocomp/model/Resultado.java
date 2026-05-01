package br.com.biocomp.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resultado {

    private int distancia;
    private List<Operacao> operacoes;

}