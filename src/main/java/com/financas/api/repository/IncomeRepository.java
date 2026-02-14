package com.financas.api.repository;

import com.financas.api.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    // Buscar entradas por período
    List<Income> findByDataBetween(LocalDate startDate, LocalDate endDate);

    // Buscar por tipo (Salário, HE, 13º)
    List<Income> findByTipo(String tipo);

    // Somar total de entradas em um período
    @Query("SELECT SUM(i.valor) FROM Income i WHERE i.data BETWEEN :startDate AND :endDate")
    BigDecimal sumValorByPeriodo(LocalDate startDate, LocalDate endDate);
}