package com.example.proj12_biblioteca.controller;

import android.content.Context;

import com.example.proj12_biblioteca.model.Aluno;
import com.example.proj12_biblioteca.persistence.AlunoDao;

import java.sql.SQLException;
import java.util.List;

public class AlunoController {
    private AlunoDao alunoDao;

    public AlunoController(Context context) {
        this.alunoDao = new AlunoDao(context);
    }

    public void insert(Aluno aluno) throws SQLException {
        alunoDao.insert(aluno);
    }

    public int update(Aluno aluno) throws SQLException {
        return alunoDao.update(aluno);
    }

    public void delete(Aluno aluno) throws SQLException {
        alunoDao.delete(aluno);
    }

    public Aluno findOne(Aluno aluno) throws SQLException {
        return alunoDao.findOne(aluno);
    }

    public List<Aluno> findAll() throws SQLException {
        return alunoDao.findAll();
    }
}