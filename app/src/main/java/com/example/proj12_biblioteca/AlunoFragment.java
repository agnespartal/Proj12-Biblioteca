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

import com.example.proj12_biblioteca.controller.AlunoController;
import com.example.proj12_biblioteca.model.Aluno;

import java.sql.SQLException;
import java.util.List;

public class AlunoFragment extends Fragment {
    private View view;
    private EditText etRaAluno;
    private EditText etNomeAluno;
    private EditText etEmailAluno;
    private Button btnInserirAluno, btnBuscarAluno, btnListarAluno, btnEditarAluno, btnExcluirAluno;
    private TextView tvListaAluno;
    private AlunoController alunoController;

    public AlunoFragment() {
        super();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_aluno, container, false);

        etRaAluno = view.findViewById(R.id.etRaAluno);
        etNomeAluno = view.findViewById(R.id.etNomeAluno);
        etEmailAluno = view.findViewById(R.id.etEmailAluno);
        btnInserirAluno = view.findViewById(R.id.btnInserirAluno);
        btnBuscarAluno = view.findViewById(R.id.btnBuscarAluno);
        btnListarAluno = view.findViewById(R.id.btnListarAluno);
        btnEditarAluno = view.findViewById(R.id.btnEditarAluno);
        btnExcluirAluno = view.findViewById(R.id.btnExcluirAluno);
        tvListaAluno = view.findViewById(R.id.tvListaAluno);
        tvListaAluno.setMovementMethod(new ScrollingMovementMethod());

        alunoController = new AlunoController(view.getContext());

        btnInserirAluno.setOnClickListener(op -> inserir());
        btnEditarAluno.setOnClickListener(op -> editar());
        btnExcluirAluno.setOnClickListener(op -> excluir());
        btnBuscarAluno.setOnClickListener(op -> buscar());
        btnListarAluno.setOnClickListener(op -> listar());

        return view;
    }

    private void inserir() {
        Aluno aluno = alunoDados();
        try {
            alunoController.insert(aluno);
            Toast.makeText(view.getContext(), "Aluno inserido com sucesso!", Toast.LENGTH_LONG).show();
            limpaCampos();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void editar() {
        Aluno aluno = alunoDados();
        try {
            alunoController.update(aluno);
            Toast.makeText(view.getContext(), "Aluno atualizado com sucesso!", Toast.LENGTH_LONG).show();
            limpaCampos();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void excluir() {
        Aluno aluno = alunoDados();
        try {
            alunoController.delete(aluno);
            Toast.makeText(view.getContext(), "Aluno excluído com sucesso!", Toast.LENGTH_LONG).show();
            limpaCampos();
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void buscar() {
        Aluno aluno = alunoDados();
        try {
            aluno = alunoController.findOne(aluno);
            if (aluno != null) {
                preencherCampos(aluno);
                Toast.makeText(view.getContext(), "Aluno encontrado", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(view.getContext(), "Aluno não encontrado", Toast.LENGTH_LONG).show();
                limpaCampos();
            }
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void listar() {
        try {
            List<Aluno> alunos = alunoController.findAll();
            StringBuilder sb = new StringBuilder();
            for (Aluno aluno : alunos) {
                sb.append(aluno.toString()).append("\n");
            }
            tvListaAluno.setText(sb.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Aluno alunoDados() {
        int ra = Integer.parseInt(etRaAluno.getText().toString());
        String nome = etNomeAluno.getText().toString();
        String email = etEmailAluno.getText().toString();
        return new Aluno(ra, nome, email);
    }

    private void preencherCampos(Aluno aluno) {
        etRaAluno.setText(String.valueOf(aluno.getRA()));
        etNomeAluno.setText(aluno.getNome());
        etEmailAluno.setText(aluno.getEmail());
    }

    private void limpaCampos() {
        etRaAluno.setText("");
        etNomeAluno.setText("");
        etEmailAluno.setText("");
    }
}