package br.com.biocomp.controller;

import br.com.biocomp.dto.DistanciaRequest;
import br.com.biocomp.dto.MatrizResponse;
import br.com.biocomp.dto.PassoAnimacaoResponse;
import br.com.biocomp.dto.ResultadoResponse;
import br.com.biocomp.model.Resultado;
import br.com.biocomp.service.DistanciaEdicaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/distancia-edicao")
@RequiredArgsConstructor
public class DistanciaEdicaoController {

    private final DistanciaEdicaoService service;

    private final long startTime = System.currentTimeMillis();

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

    @GetMapping("/status")
    public Map<String, Object> status() {

        long uptimeMillis = System.currentTimeMillis() - startTime;

        return Map.of(
                "status", "UP",
                "service", "distancia-edicao",
                "timestamp", LocalDateTime.now().toString(),
                "uptime", Duration.ofMillis(uptimeMillis).toString(),
                "javaVersion", System.getProperty("java.version"),
                "memoryUsedMB", (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024),
                "memoryMaxMB", Runtime.getRuntime().maxMemory() / (1024 * 1024)
        );
    }
}