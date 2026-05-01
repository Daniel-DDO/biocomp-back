package br.com.biocomp.controller;

import br.com.biocomp.dto.DistanciaRequest;
import br.com.biocomp.dto.MatrizResponse;
import br.com.biocomp.dto.PassoAnimacaoResponse;
import br.com.biocomp.dto.ResultadoResponse;
import br.com.biocomp.model.Resultado;
import br.com.biocomp.service.DistanciaEdicaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/distancia-edicao")
@RequiredArgsConstructor
public class DistanciaEdicaoController {

    private final DistanciaEdicaoService service;

    @PostMapping("/calcular")
    public ResultadoResponse calcular(
            @RequestBody DistanciaRequest request
    ) {

        Resultado resultado = service.calcular(
                request.getTexto1(),
                request.getTexto2()
        );

        return new ResultadoResponse(
                resultado.getDistancia(),
                resultado.getOperacoes()
        );
    }

    //animação visual
    @PostMapping("/animacao")
    public List<PassoAnimacaoResponse> animacao(
            @RequestBody DistanciaRequest request
    ) {

        return service.gerarPassosAnimacao(
                request.getTexto1(),
                request.getTexto2()
        );
    }

    @PostMapping("/matriz")
    public MatrizResponse matriz(
            @RequestBody DistanciaRequest request
    ) {

        return service.gerarMatriz(
                request.getTexto1(),
                request.getTexto2()
        );
    }
}