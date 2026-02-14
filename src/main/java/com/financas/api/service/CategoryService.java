package com.financas.api.service;

import com.financas.api.entity.Category;
import com.financas.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Criar nova categoria
    public Category criar(Category category) {
        // Verifica se já existe categoria com esse nome
        if (categoryRepository.existsByNome(category.getNome())) {
            throw new RuntimeException("Já existe uma categoria com esse nome!");
        }
        return categoryRepository.save(category);
    }

    // Listar todas as categorias
    public List<Category> listarTodas() {
        return categoryRepository.findAll();
    }

    // Listar por tipo (entrada ou saida)
    public List<Category> listarPorTipo(String tipo) {
        return categoryRepository.findByTipo(tipo);
    }

    // Buscar por ID
    public Optional<Category> buscarPorId(Long id) {
        return categoryRepository.findById(id);
    }

    // Atualizar categoria
    public Category atualizar(Long id, Category categoryAtualizada) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada!"));

        category.setNome(categoryAtualizada.getNome());
        category.setTipo(categoryAtualizada.getTipo());
        category.setCor(categoryAtualizada.getCor());

        return categoryRepository.save(category);
    }

    // Deletar categoria
    public void deletar(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Categoria não encontrada!");
        }
        categoryRepository.deleteById(id);
    }
}