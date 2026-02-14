package com.financas.api.service;

import com.financas.api.entity.Income;
import com.financas.api.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    // Criar nova entrada
    public Income criar(Income income) {
        return incomeRepository.save(income);
    }

    // Listar todas as entradas
    public List<Income> listarTodas() {
        return incomeRepository.findAll();
    }

    // Listar por período
    public List<Income> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return incomeRepository.findByDataBetween(dataInicio, dataFim);
    }

    // Listar por tipo
    public List<Income> listarPorTipo(String tipo) {
        return incomeRepository.findByTipo(tipo);
    }

    // Buscar por ID
    public Optional<Income> buscarPorId(Long id) {
        return incomeRepository.findById(id);
    }

    // Calcular total de entradas em um período
    public BigDecimal calcularTotalPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        BigDecimal total = incomeRepository.sumValorByPeriodo(dataInicio, dataFim);
        return total != null ? total : BigDecimal.ZERO;
    }

    // Atualizar entrada
    public Income atualizar(Long id, Income incomeAtualizada) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada não encontrada!"));

        income.setValor(incomeAtualizada.getValor());
        income.setDescricao(incomeAtualizada.getDescricao());
        income.setTipo(incomeAtualizada.getTipo());
        income.setData(incomeAtualizada.getData());

        return incomeRepository.save(income);
    }

    // Deletar entrada
    public void deletar(Long id) {
        if (!incomeRepository.existsById(id)) {
            throw new RuntimeException("Entrada não encontrada!");
        }
        incomeRepository.deleteById(id);
    }
}