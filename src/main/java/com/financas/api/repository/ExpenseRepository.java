package com.financas.api.repository;

import com.financas.api.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Buscar despesas por período
    List<Expense> findByDataVencimentoBetween(LocalDate startDate, LocalDate endDate);

    // Buscar por situação (PENDENTE ou PAGO)
    List<Expense> findBySituacao(String situacao);

    // Buscar por tipo (Fixo ou Variável)
    List<Expense> findByTipo(String tipo);

    // Buscar por categoria
    List<Expense> findByCategoryId(Long categoryId);

    // Buscar dívidas de uma pessoa específica
    List<Expense> findByPessoa(String pessoa);

    // IMPORTANTE: Buscar todas as parcelas de um mesmo grupo
    List<Expense> findByGrupoId(String grupoId);

    // Buscar todas as despesas recorrentes
    List<Expense> findByRecorrenteTrue();

    // Somar total PENDENTE ou PAGO em um período
    @Query("SELECT SUM(e.valor) FROM Expense e WHERE e.situacao = :situacao AND e.dataVencimento BETWEEN :startDate AND :endDate")
    BigDecimal sumValorBySituacaoAndPeriodo(String situacao, LocalDate startDate, LocalDate endDate);

    // Somar total de gastos Fixos ou Variáveis
    @Query("SELECT SUM(e.valor) FROM Expense e WHERE e.tipo = :tipo AND e.dataVencimento BETWEEN :startDate AND :endDate")
    BigDecimal sumValorByTipoAndPeriodo(String tipo, LocalDate startDate, LocalDate endDate);
}