package com.financas.api.controller;

import com.financas.api.entity.Expense;
import com.financas.api.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // POST /api/expenses - Criar despesa (com parcelas automáticas!)
    @PostMapping
    public ResponseEntity<List<Expense>> criar(@RequestBody Expense expense) {
        List<Expense> criadas = expenseService.criar(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(criadas);
    }

    // GET /api/expenses - Listar todas
    @GetMapping
    public ResponseEntity<List<Expense>> listarTodas() {
        List<Expense> expenses = expenseService.listarTodas();
        return ResponseEntity.ok(expenses);
    }

    // GET /api/expenses/periodo?inicio=2025-01-01&fim=2025-01-31
    @GetMapping("/periodo")
    public ResponseEntity<List<Expense>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        List<Expense> expenses = expenseService.listarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(expenses);
    }

    // GET /api/expenses/situacao/{situacao} - PENDENTE ou PAGO
    @GetMapping("/situacao/{situacao}")
    public ResponseEntity<List<Expense>> listarPorSituacao(@PathVariable String situacao) {
        List<Expense> expenses = expenseService.listarPorSituacao(situacao);
        return ResponseEntity.ok(expenses);
    }

    // GET /api/expenses/grupo/{grupoId} - Todas as parcelas de uma compra
    @GetMapping("/grupo/{grupoId}")
    public ResponseEntity<List<Expense>> listarParcelasDoGrupo(@PathVariable String grupoId) {
        List<Expense> parcelas = expenseService.listarParcelasDoGrupo(grupoId);
        return ResponseEntity.ok(parcelas);
    }

    // GET /api/expenses/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Expense> buscarPorId(@PathVariable Long id) {
        return expenseService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PATCH /api/expenses/{id}/pagar - Marcar como PAGO
    @PatchMapping("/{id}/pagar")
    public ResponseEntity<Expense> marcarComoPago(@PathVariable Long id) {
        try {
            Expense paga = expenseService.marcarComoPago(id);
            return ResponseEntity.ok(paga);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PATCH /api/expenses/{id}/pendente - Marcar como PENDENTE
    @PatchMapping("/{id}/pendente")
    public ResponseEntity<Expense> marcarComoPendente(@PathVariable Long id) {
        try {
            Expense pendente = expenseService.marcarComoPendente(id);
            return ResponseEntity.ok(pendente);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET /api/expenses/total/situacao/{situacao}?inicio=...&fim=...
    @GetMapping("/total/situacao/{situacao}")
    public ResponseEntity<BigDecimal> calcularTotalPorSituacao(
            @PathVariable String situacao,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        BigDecimal total = expenseService.calcularTotalPorSituacaoEPeriodo(situacao, inicio, fim);
        return ResponseEntity.ok(total);
    }

    // GET /api/expenses/total/tipo/{tipo}?inicio=...&fim=...
    @GetMapping("/total/tipo/{tipo}")
    public ResponseEntity<BigDecimal> calcularTotalPorTipo(
            @PathVariable String tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        BigDecimal total = expenseService.calcularTotalPorTipoEPeriodo(tipo, inicio, fim);
        return ResponseEntity.ok(total);
    }

    // PUT /api/expenses/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Expense> atualizar(@PathVariable Long id, @RequestBody Expense expense) {
        try {
            Expense atualizada = expenseService.atualizar(id, expense);
            return ResponseEntity.ok(atualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/expenses/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            expenseService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/expenses/grupo/{grupoId} - Deletar todas as parcelas
    @DeleteMapping("/grupo/{grupoId}")
    public ResponseEntity<Void> deletarGrupo(@PathVariable String grupoId) {
        expenseService.deletarGrupo(grupoId);
        return ResponseEntity.noContent().build();
    }
}