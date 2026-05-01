package br.com.biocomp.dto;

import br.com.biocomp.model.Operacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultadoResponse {

    private int distancia;
    private List<Operacao> operacoes;

}