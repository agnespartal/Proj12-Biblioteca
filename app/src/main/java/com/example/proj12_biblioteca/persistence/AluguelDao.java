package com.example.proj12_biblioteca.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.proj12_biblioteca.model.Aluguel;
import com.example.proj12_biblioteca.model.Aluno;
import com.example.proj12_biblioteca.model.Exemplar;
import com.example.proj12_biblioteca.model.Livro;
import com.example.proj12_biblioteca.model.Revista;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AluguelDao extends SQLiteOpenHelper implements ICRUDDao<Aluguel> {
    private static final String DATABASE_NAME = "biblioteca.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Aluguel";
    private static final String COLUMN_EXEMPLAR_CODIGO = "ExemplarCodigo";
    private static final String COLUMN_ALUNO_RA = "AlunoRA";
    private static final String COLUMN_DATA_RETIRADA = "data_retirada";
    private static final String COLUMN_DATA_DEVOLUCAO = "data_devolucao";

    private Context context;

    public AluguelDao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_EXEMPLAR_CODIGO + " INTEGER, " +
                COLUMN_ALUNO_RA + " INTEGER, " +
                COLUMN_DATA_RETIRADA + " VARCHAR(10), " +
                COLUMN_DATA_DEVOLUCAO + " VARCHAR(10), " +
                "PRIMARY KEY (" + COLUMN_EXEMPLAR_CODIGO + ", " + COLUMN_ALUNO_RA + "))";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void insert(Aluguel aluguel) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXEMPLAR_CODIGO, aluguel.getExemplar().getCodigo());
        values.put(COLUMN_ALUNO_RA, aluguel.getAluno().getRA());
        values.put(COLUMN_DATA_RETIRADA, aluguel.getDataRetirada().toString());
        values.put(COLUMN_DATA_DEVOLUCAO, aluguel.getDataDevolucao().toString());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public int update(Aluguel aluguel) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATA_RETIRADA, aluguel.getDataRetirada().toString());
        values.put(COLUMN_DATA_DEVOLUCAO, aluguel.getDataDevolucao().toString());
        int rowsAffected = db.update(TABLE_NAME, values, COLUMN_EXEMPLAR_CODIGO + " = ? AND " + COLUMN_ALUNO_RA + " = ?",
                new String[]{String.valueOf(aluguel.getExemplar().getCodigo()), String.valueOf(aluguel.getAluno().getRA())});
        db.close();
        return rowsAffected;
    }

    @Override
    public void delete(Aluguel aluguel) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_EXEMPLAR_CODIGO + " = ? AND " + COLUMN_ALUNO_RA + " = ?",
                new String[]{String.valueOf(aluguel.getExemplar().getCodigo()), String.valueOf(aluguel.getAluno().getRA())});
        db.close();
    }

    @Override
    public Aluguel findOne(Aluguel aluguel) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_EXEMPLAR_CODIGO, COLUMN_ALUNO_RA, COLUMN_DATA_RETIRADA, COLUMN_DATA_DEVOLUCAO},
                COLUMN_EXEMPLAR_CODIGO + " = ? AND " + COLUMN_ALUNO_RA + " = ?", new String[]{String.valueOf(aluguel.getExemplar().getCodigo()), String.valueOf(aluguel.getAluno().getRA())},
                null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Exemplar exemplar = getExemplarByCodigo(cursor.getInt(0));
            Aluno aluno = new Aluno(cursor.getInt(1), null, null); // Placeholder, obtenha o aluno completo se necessário
            Aluguel foundAluguel = new Aluguel(aluno, exemplar, LocalDate.parse(cursor.getString(2)), LocalDate.parse(cursor.getString(3)));
            cursor.close();
            db.close();
            return foundAluguel;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    @Override
    public List<Aluguel> findAll() throws SQLException {
        List<Aluguel> alugueis = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Exemplar exemplar = getExemplarByCodigo(cursor.getInt(0));
                Aluno aluno = new Aluno(cursor.getInt(1), null, null); // Placeholder, obtenha o aluno completo se necessário
                Aluguel aluguel = new Aluguel(aluno, exemplar, LocalDate.parse(cursor.getString(2)), LocalDate.parse(cursor.getString(3)));
                alugueis.add(aluguel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return alugueis;
    }

    private Exemplar getExemplarByCodigo(int codigo) {
        Exemplar exemplar = null;
        LivroDao livroDao = new LivroDao(context);
        RevistaDao revistaDao = new RevistaDao(context);

        Livro livro = livroDao.findOne(new Livro(codigo, null, 0, null, 0));
        if (livro != null) {
            exemplar = livro;
        } else {
            Revista revista = revistaDao.findOne(new Revista(codigo, null, 0, null));
            if (revista != null) {
                exemplar = revista;
            }
        }

        return exemplar;
    }
}