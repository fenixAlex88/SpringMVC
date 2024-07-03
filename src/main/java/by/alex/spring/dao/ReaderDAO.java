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
public class ReaderDAO {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public ReaderDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reader> getAllReaders() {
        return jdbcTemplate.query("select * from reader", new BeanPropertyRowMapper<>(Reader.class));
    }

    public Reader getReaderById(int id) {
        return jdbcTemplate.query("SELECT * FROM Reader WHERE reader_id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Reader.class))
                .stream().findFirst().orElse(null);
    }

    public void addReader(Reader reader) {
        jdbcTemplate.update("INSERT INTO Reader (name, year) VALUES(?,?)", reader.getName(), reader.getYear());
    }

    public void updateReader(Reader reader) {
        jdbcTemplate.update("UPDATE Reader SET name=?, year=? WHERE reader_id=?", reader.getName(), reader.getYear(), reader.getId());
    }

    public void deleteReader(int id) {
        jdbcTemplate.update("DELETE FROM Reader WHERE reader_id=?", id);
    }

    public Optional<Reader> getReaderByName(String name) {
        return jdbcTemplate.query("SELECT * FROM Reader WHERE name=?", new Object[]{name}, new BeanPropertyRowMapper<>(Reader.class))
                .stream().findFirst();
    }

    public List<Book> getBookByReaderId(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE reader_id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
    }

}
