package br.com.bookstore.dao;

import br.com.bookstore.model.Livro;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LivroDAOSqlite implements LivroDAO {
    private static final String URL = "jdbc:sqlite:bookstore.db";

    public LivroDAOSqlite() {
        String sql = "CREATE TABLE IF NOT EXISTS livros ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "titulo TEXT NOT NULL, "
                + "autor TEXT NOT NULL, "
                + "preco REAL NOT NULL);";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void salvar(Livro livro) {
        String sql = "INSERT INTO livros (titulo, autor, preco) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setDouble(3, livro.getPreco());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    livro.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        String sql = "SELECT id, titulo, autor, preco FROM livros WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearLinha(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Livro> listarTodos() {
        String sql = "SELECT id, titulo, autor, preco FROM livros ORDER BY id";
        List<Livro> livros = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                livros.add(mapearLinha(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return livros;
    }

    private Livro mapearLinha(ResultSet rs) throws SQLException {
        return new Livro(
                rs.getLong("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getDouble("preco"));
    }
}
