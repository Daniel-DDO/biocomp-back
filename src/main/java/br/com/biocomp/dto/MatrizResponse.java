package br.com.biocomp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatrizResponse {

    private int distancia;
    private int[][] matriz;

}