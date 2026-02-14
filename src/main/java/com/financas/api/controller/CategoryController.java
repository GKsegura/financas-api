package com.financas.api.controller;

import com.financas.api.entity.Category;
import com.financas.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*") // Permite acesso do frontend
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // POST /api/categories - Criar categoria
    @PostMapping
    public ResponseEntity<Category> criar(@RequestBody Category category) {
        try {
            Category criada = categoryService.criar(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(criada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /api/categories - Listar todas
    @GetMapping
    public ResponseEntity<List<Category>> listarTodas() {
        List<Category> categories = categoryService.listarTodas();
        return ResponseEntity.ok(categories);
    }

    // GET /api/categories/tipo/{tipo} - Listar por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Category>> listarPorTipo(@PathVariable String tipo) {
        List<Category> categories = categoryService.listarPorTipo(tipo);
        return ResponseEntity.ok(categories);
    }

    // GET /api/categories/{id} - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> buscarPorId(@PathVariable Long id) {
        return categoryService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/categories/{id} - Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<Category> atualizar(@PathVariable Long id, @RequestBody Category category) {
        try {
            Category atualizada = categoryService.atualizar(id, category);
            return ResponseEntity.ok(atualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/categories/{id} - Deletar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            categoryService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}