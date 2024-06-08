package com.example.proj12_biblioteca.controller;

import android.content.Context;

import com.example.proj12_biblioteca.model.Livro;
import com.example.proj12_biblioteca.persistence.LivroDao;

import java.sql.SQLException;
import java.util.List;

public class LivroController {
    private LivroDao livroDao;

    public LivroController(Context context) {
        this.livroDao = new LivroDao(context);
    }

    public void insert(Livro livro) throws SQLException {
        livroDao.insert(livro);
    }

    public int update(Livro livro) throws SQLException {
        return livroDao.update(livro);
    }

    public void delete(Livro livro) throws SQLException {
        livroDao.delete(livro);
    }

    public Livro findOne(Livro livro) throws SQLException {
        return livroDao.findOne(livro);
    }

    public List<Livro> findAll() throws SQLException {
        return livroDao.findAll();
    }
}
