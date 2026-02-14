package com.financas.api.controller;

import com.financas.api.entity.Income;
import com.financas.api.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/incomes")
@CrossOrigin(origins = "*")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    // POST /api/incomes - Criar entrada
    @PostMapping
    public ResponseEntity<Income> criar(@RequestBody Income income) {
        Income criada = incomeService.criar(income);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    // GET /api/incomes - Listar todas
    @GetMapping
    public ResponseEntity<List<Income>> listarTodas() {
        List<Income> incomes = incomeService.listarTodas();
        return ResponseEntity.ok(incomes);
    }

    // GET /api/incomes/periodo?inicio=2025-01-01&fim=2025-01-31
    @GetMapping("/periodo")
    public ResponseEntity<List<Income>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<Income> incomes = incomeService.listarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(incomes);
    }

    // GET /api/incomes/tipo/{tipo}
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Income>> listarPorTipo(@PathVariable String tipo) {
        List<Income> incomes = incomeService.listarPorTipo(tipo);
        return ResponseEntity.ok(incomes);
    }

    // GET /api/incomes/total?inicio=2025-01-01&fim=2025-01-31
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> calcularTotal(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        BigDecimal total = incomeService.calcularTotalPorPeriodo(inicio, fim);
        return ResponseEntity.ok(total);
    }

    // GET /api/incomes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Income> buscarPorId(@PathVariable Long id) {
        return incomeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/incomes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Income> atualizar(@PathVariable Long id, @RequestBody Income income) {
        try {
            Income atualizada = incomeService.atualizar(id, income);
            return ResponseEntity.ok(atualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/incomes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            incomeService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}