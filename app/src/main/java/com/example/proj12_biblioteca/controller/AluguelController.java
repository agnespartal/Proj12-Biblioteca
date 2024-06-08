package com.example.proj12_biblioteca.controller;

import android.content.Context;

import com.example.proj12_biblioteca.model.Aluguel;
import com.example.proj12_biblioteca.persistence.AluguelDao;

import java.sql.SQLException;
import java.util.List;

public class AluguelController {
    private AluguelDao aluguelDao;

    public AluguelController(Context context) {
        this.aluguelDao = new AluguelDao(context);
    }

    public void insert(Aluguel aluguel) throws SQLException {
        aluguelDao.insert(aluguel);
    }

    public int update(Aluguel aluguel) throws SQLException {
        return aluguelDao.update(aluguel);
    }

    public void delete(Aluguel aluguel) throws SQLException {
        aluguelDao.delete(aluguel);
    }

    public Aluguel findOne(Aluguel aluguel) throws SQLException {
        return aluguelDao.findOne(aluguel);
    }

    public List<Aluguel> findAll() throws SQLException {
        return aluguelDao.findAll();
    }
}
