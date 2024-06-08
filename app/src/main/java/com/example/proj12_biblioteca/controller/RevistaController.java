package com.example.proj12_biblioteca.controller;

import android.content.Context;

import com.example.proj12_biblioteca.model.Revista;
import com.example.proj12_biblioteca.persistence.RevistaDao;

import java.sql.SQLException;
import java.util.List;

public class RevistaController {
    private RevistaDao revistaDao;

    public RevistaController(Context context) {
        this.revistaDao = new RevistaDao(context);
    }

    public void insert(Revista revista) throws SQLException {
        revistaDao.insert(revista);
    }

    public int update(Revista revista) throws SQLException {
        return revistaDao.update(revista);
    }

    public void delete(Revista revista) throws SQLException {
        revistaDao.delete(revista);
    }

    public Revista findOne(Revista revista) throws SQLException {
        return revistaDao.findOne(revista);
    }

    public List<Revista> findAll() throws SQLException {
        return revistaDao.findAll();
    }
}
