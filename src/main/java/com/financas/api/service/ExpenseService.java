package com.financas.api.service;

import com.financas.api.entity.Expense;
import com.financas.api.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    // ========== CRIAR DESPESA (COM PARCELAS AUTOMÁTICAS) ==========
    public List<Expense> criar(Expense expense) {
        List<Expense> despesasCriadas = new ArrayList<>();

        // Gera um ID único para agrupar as parcelas
        String grupoId = UUID.randomUUID().toString();

        // CASO 1: Despesa PARCELADA (ex: Cama 10x)
        if (expense.getParcelaTotal() != null && expense.getParcelaTotal() > 1) {

            for (int i = 1; i <= expense.getParcelaTotal(); i++) {
                Expense parcela = new Expense();
                parcela.setDescricao(expense.getDescricao());
                parcela.setCategory(expense.getCategory());
                parcela.setValor(expense.getValor()); // Valor de cada parcela
                parcela.setTipo(expense.getTipo());
                parcela.setSituacao("PENDENTE");
                parcela.setTipoPagamento(expense.getTipoPagamento());
                parcela.setPessoa(expense.getPessoa());
                parcela.setGrupoId(grupoId);
                parcela.setParcelaAtual(i);
                parcela.setParcelaTotal(expense.getParcelaTotal());
                parcela.setRecorrente(false);

                // Data de vencimento: soma i-1 meses à data inicial
                LocalDate dataVencimento = expense.getDataVencimento().plusMonths(i - 1);
                parcela.setDataVencimento(dataVencimento);

                Expense salva = expenseRepository.save(parcela);
                despesasCriadas.add(salva);
            }

            return despesasCriadas;
        }

        // CASO 2: Despesa RECORRENTE (ex: Internet todo mês)
        if (expense.getRecorrente() != null && expense.getRecorrente()) {

            // Vamos criar para os próximos 12 meses
            for (int i = 0; i < 12; i++) {
                Expense recorrente = new Expense();
                recorrente.setDescricao(expense.getDescricao());
                recorrente.setCategory(expense.getCategory());
                recorrente.setValor(expense.getValor());
                recorrente.setTipo("Fixo"); // Recorrente sempre é Fixo
                recorrente.setSituacao("PENDENTE");
                recorrente.setTipoPagamento(expense.getTipoPagamento());
                recorrente.setGrupoId(grupoId);
                recorrente.setRecorrente(true);
                recorrente.setParcelaAtual(null);
                recorrente.setParcelaTotal(null); // Infinito

                LocalDate dataVencimento = expense.getDataVencimento().plusMonths(i);
                recorrente.setDataVencimento(dataVencimento);

                Expense salva = expenseRepository.save(recorrente);
                despesasCriadas.add(salva);
            }

            return despesasCriadas;
        }

        // CASO 3: Despesa ÚNICA/SIMPLES
        expense.setGrupoId(grupoId);
        expense.setSituacao("PENDENTE");
        Expense salva = expenseRepository.save(expense);
        despesasCriadas.add(salva);

        return despesasCriadas;
    }

    // ========== LISTAR TODAS ==========
    public List<Expense> listarTodas() {
        return expenseRepository.findAll();
    }

    // ========== LISTAR POR PERÍODO ==========
    public List<Expense> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return expenseRepository.findByDataVencimentoBetween(dataInicio, dataFim);
    }

    // ========== LISTAR POR SITUAÇÃO ==========
    public List<Expense> listarPorSituacao(String situacao) {
        return expenseRepository.findBySituacao(situacao);
    }

    // ========== LISTAR PARCELAS DE UM GRUPO ==========
    public List<Expense> listarParcelasDoGrupo(String grupoId) {
        return expenseRepository.findByGrupoId(grupoId);
    }

    // ========== BUSCAR POR ID ==========
    public Optional<Expense> buscarPorId(Long id) {
        return expenseRepository.findById(id);
    }

    // ========== MARCAR COMO PAGO ==========
    public Expense marcarComoPago(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada!"));

        expense.setSituacao("PAGO");
        expense.setDataPagamento(LocalDate.now());

        return expenseRepository.save(expense);
    }

    // ========== MARCAR COMO PENDENTE ==========
    public Expense marcarComoPendente(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada!"));

        expense.setSituacao("PENDENTE");
        expense.setDataPagamento(null);

        return expenseRepository.save(expense);
    }

    // ========== CALCULAR TOTAIS ==========
    public BigDecimal calcularTotalPorSituacaoEPeriodo(String situacao, LocalDate dataInicio, LocalDate dataFim) {
        BigDecimal total = expenseRepository.sumValorBySituacaoAndPeriodo(situacao, dataInicio, dataFim);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal calcularTotalPorTipoEPeriodo(String tipo, LocalDate dataInicio, LocalDate dataFim) {
        BigDecimal total = expenseRepository.sumValorByTipoAndPeriodo(tipo, dataInicio, dataFim);
        return total != null ? total : BigDecimal.ZERO;
    }

    // ========== ATUALIZAR ==========
    public Expense atualizar(Long id, Expense expenseAtualizada) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada!"));

        expense.setDescricao(expenseAtualizada.getDescricao());
        expense.setCategory(expenseAtualizada.getCategory());
        expense.setValor(expenseAtualizada.getValor());
        expense.setTipo(expenseAtualizada.getTipo());
        expense.setDataVencimento(expenseAtualizada.getDataVencimento());
        expense.setTipoPagamento(expenseAtualizada.getTipoPagamento());
        expense.setPessoa(expenseAtualizada.getPessoa());

        return expenseRepository.save(expense);
    }

    // ========== DELETAR ==========
    public void deletar(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new RuntimeException("Despesa não encontrada!");
        }
        expenseRepository.deleteById(id);
    }

    // ========== DELETAR TODAS AS PARCELAS DE UM GRUPO ==========
    public void deletarGrupo(String grupoId) {
        List<Expense> parcelas = expenseRepository.findByGrupoId(grupoId);
        expenseRepository.deleteAll(parcelas);
    }
}