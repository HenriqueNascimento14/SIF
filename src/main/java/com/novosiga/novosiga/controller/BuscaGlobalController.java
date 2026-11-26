package com.novosiga.novosiga.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.novosiga.novosiga.dto.SugestaoBusca;
import com.novosiga.novosiga.service.BuscaGlobalService;

@RestController
@RequestMapping("/api/busca")
public class BuscaGlobalController {

    @Autowired
    private BuscaGlobalService buscaGlobalService;

    @GetMapping
    public List<SugestaoBusca> buscar(@RequestParam(required = false) String q) {
        return buscaGlobalService.buscar(q);
    }
}
