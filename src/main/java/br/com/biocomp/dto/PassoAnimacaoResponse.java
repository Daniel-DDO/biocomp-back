package br.com.biocomp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassoAnimacaoResponse {

    private String antes;
    private String operacao;
    private String depois;

}