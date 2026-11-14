package br.com.bookstore;

import br.com.bookstore.dao.LivroDAO;
import br.com.bookstore.dao.LivroDAOSqlite;
import br.com.bookstore.model.Livro;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        LivroDAO dao = new LivroDAOSqlite();

        Livro l1 = new Livro("Clean Code", "Robert C. Martin", 89.90);
        Livro l2 = new Livro("Design Patterns", "Erich Gamma", 129.50);
        Livro l3 = new Livro("Refactoring", "Martin Fowler", 99.00);

        dao.salvar(l1);
        dao.salvar(l2);
        dao.salvar(l3);

        System.out.println("IDs gerados: " + l1.getId() + ", " + l2.getId() + ", " + l3.getId());

        dao.buscarPorId(2L).ifPresentOrElse(
                livro -> System.out.println("Encontrado: " + livro),
                () -> System.out.println("Livro com ID 2 não encontrado."));

        dao.buscarPorId(999L).ifPresentOrElse(
                livro -> System.out.println("Encontrado: " + livro),
                () -> System.out.println("Livro com ID 999 não encontrado."));

        List<Livro> livros = dao.listarTodos();
        System.out.println();
        System.out.printf("%-5s | %-20s | %-20s | %10s%n", "ID", "TÍTULO", "AUTOR", "PREÇO");
        System.out.println("-".repeat(65));
        for (Livro l : livros) {
            System.out.printf("%-5d | %-20s | %-20s | R$ %7.2f%n", l.getId(), l.getTitulo(), l.getAutor(), l.getPreco());
        }
    }
}
