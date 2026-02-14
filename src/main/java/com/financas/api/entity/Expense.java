package com.financas.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(length = 50)
    private String tipo; // "Fixo" ou "Variável"

    @Column(length = 50)
    private String situacao; // "PENDENTE" ou "PAGO"

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "tipo_pagamento", length = 50)
    private String tipoPagamento; // "Débito", "Crédito", "PIX"

    @Column(length = 100)
    private String pessoa; // Para dívidas: "Guilherme", "Daniel"

    // SISTEMA DE PARCELAS
    @Column(name = "grupo_id")
    private String grupoId; // Agrupa todas as parcelas

    @Column(name = "parcela_atual")
    private Integer parcelaAtual; // 1, 2, 3...

    @Column(name = "parcela_total")
    private Integer parcelaTotal; // 10 (ou null para recorrentes)

    // DESPESAS RECORRENTES
    @Column(name = "recorrente")
    private Boolean recorrente = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (situacao == null) {
            situacao = "PENDENTE";
        }
        if (recorrente == null) {
            recorrente = false;
        }
    }
}