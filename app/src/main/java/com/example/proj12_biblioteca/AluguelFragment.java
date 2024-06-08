package com.example.proj12_biblioteca;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proj12_biblioteca.controller.AluguelController;
import com.example.proj12_biblioteca.model.Aluguel;
import com.example.proj12_biblioteca.model.Aluno;
import com.example.proj12_biblioteca.model.Exemplar;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AluguelFragment extends Fragment {
    private View view;
    private EditText etRaAluno;
    private EditText etCodigoExemplar;
    private EditText etDataRetirada;
    private EditText etDataDevolucao;
    private Button btnInserirAluguel, btnBuscarAluguel, btnListarAluguel, btnEditarAluguel, btnExcluirAluguel;
    private TextView tvListaAluguel;
    private AluguelController aluguelController;

    public AluguelFragment() {
        super();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_aluguel, container, false);

        etRaAluno = view.findViewById(R.id.etRaAlunoAluguel);
        etCodigoExemplar = view.findViewById(R.id.etCodigoExemplar);
        etDataRetirada = view.findViewById(R.id.etDataRetirada);
        etDataDevolucao = view.findViewById(R.id.etDataDevolucao);
        btnInserirAluguel = view.findViewById(R.id.btnInserirAluguel);
        btnBuscarAluguel = view.findViewById(R.id.btnBuscarAluguel);
        btnListarAluguel = view.findViewById(R.id.btnListarAluguel);
        btnEditarAluguel = view.findViewById(R.id.btnEditarAluguel);
        btnExcluirAluguel = view.findViewById(R.id.btnExcluirAluguel);
        tvListaAluguel = view.findViewById(R.id.tvListaAluguel);
        tvListaAluguel.setMovementMethod(new ScrollingMovementMethod());

        aluguelController = new AluguelController(view.getContext());

        btnInserirAluguel.setOnClickListener(op -> inserir());
        btnEditarAluguel.setOnClickListener(op -> editar());
        btnExcluirAluguel.setOnClickListener(op -> excluir());
        btnBuscarAluguel.setOnClickListener(op -> buscar());
        btnListarAluguel.setOnClickListener(op -> listar());

        return view;
    }

    private void inserir() {
        Aluguel aluguel = aluguelDados();
        try {
            aluguelController.insert(aluguel);
            Toast.makeText(view.getContext(), "Aluguel inserido com sucesso!", Toast.LENGTH_LONG).show();
            limpaCampos();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void editar() {
        Aluguel aluguel = aluguelDados();
        try {
            aluguelController.update(aluguel);
            Toast.makeText(view.getContext(), "Aluguel atualizado com sucesso!", Toast.LENGTH_LONG).show();
            limpaCampos();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void excluir() {
        Aluguel aluguel = aluguelDados();
        try {
            aluguelController.delete(aluguel);
            Toast.makeText(view.getContext(), "Aluguel excluído com sucesso!", Toast.LENGTH_LONG).show();
            limpaCampos();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void buscar() {
        Aluguel aluguel = aluguelDados();
        try {
            aluguel = aluguelController.findOne(aluguel);
            if (aluguel != null) {
                preencherCampos(aluguel);
                Toast.makeText(view.getContext(), "Aluguel encontrado", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(view.getContext(), "Aluguel não encontrado", Toast.LENGTH_LONG).show();
                limpaCampos();
            }
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void listar() {
        try {
            List<Aluguel> alugueis = aluguelController.findAll();
            StringBuilder sb = new StringBuilder();
            for (Aluguel aluguel : alugueis) {
                sb.append(aluguel.toString()).append("\n");
            }
            tvListaAluguel.setText(sb.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Aluguel aluguelDados() {
        int ra = Integer.parseInt(etRaAluno.getText().toString());
        int codigoExemplar = Integer.parseInt(etCodigoExemplar.getText().toString());
        LocalDate dataRetirada = LocalDate.parse(etDataRetirada.getText().toString());
        LocalDate dataDevolucao = LocalDate.parse(etDataDevolucao.getText().toString());

        Aluno aluno = new Aluno(ra, null, null); // Você pode buscar o aluno completo se necessário
        Exemplar exemplar = new Exemplar(codigoExemplar, null, 0) {}; // Placeholder para Exemplar, ajuste conforme necessário

        return new Aluguel(aluno, exemplar, dataRetirada, dataDevolucao);
    }

    private void preencherCampos(Aluguel aluguel) {
        etRaAluno.setText(String.valueOf(aluguel.getAluno().getRA()));
        etCodigoExemplar.setText(String.valueOf(aluguel.getExemplar().getCodigo()));
        etDataRetirada.setText(aluguel.getDataRetirada().toString());
        etDataDevolucao.setText(aluguel.getDataDevolucao().toString());
    }

    private void limpaCampos() {
        etRaAluno.setText("");
        etCodigoExemplar.setText("");
        etDataRetirada.setText("");
        etDataDevolucao.setText("");
    }
}