package by.alex.spring.dao;

import by.alex.spring.models.Book;
import by.alex.spring.models.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getAllBooks() {
        return jdbcTemplate.query("select * from book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book getBookById(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE book_id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findFirst().orElse(null);
    }

    public void addBook(Book book) {
        jdbcTemplate.update("INSERT INTO Book (title, author, year) VALUES(?,?,?)", book.getTitle(), book.getAuthor(), book.getYear());
    }

    public void updateBook(Book book) {
        jdbcTemplate.update("UPDATE Book SET title=?, author=? ,year=? WHERE reader_id=?", book.getTitle(), book.getAuthor(), book.getYear(), book.getId());
    }

    public void deleteBook(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE book_id=?", id);
    }

    public Optional<Reader> getReaderById(int id) {
        return jdbcTemplate.query("SELECT Reader.* FROM Book JOIN Reader ON Book.reader_id = Reader.reader_id " +
                "WHERE Book.book_id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Reader.class))
                .stream().findFirst();
    }

    public void releaseBook(int id) {
        jdbcTemplate.update("UPDATE Book SET reader_id=NULL WHERE book_id=?", id);
    }

    public void assignBook(int id, Reader reader) {
        jdbcTemplate.update("UPDATE Book SET reader_id=? WHERE book_id=?", reader.getId(), id);
    }
}
