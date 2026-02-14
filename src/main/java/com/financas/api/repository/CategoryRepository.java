package com.financas.api.repository;

import com.financas.api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Buscar categorias por tipo (entrada ou saida)
    List<Category> findByTipo(String tipo);

    // Verificar se já existe uma categoria com esse nome
    boolean existsByNome(String nome);
}