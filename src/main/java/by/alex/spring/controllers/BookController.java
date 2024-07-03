package by.alex.spring.controllers;

import by.alex.spring.dao.BookDAO;
import by.alex.spring.dao.ReaderDAO;
import by.alex.spring.models.Book;
import by.alex.spring.models.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookDAO bookDAO;
    private final ReaderDAO readerDAO;

    @Autowired
    public BookController(BookDAO bookDAO, ReaderDAO readerDAO) {
        this.bookDAO = bookDAO;
        this.readerDAO = readerDAO;
    }

    @GetMapping
    public String getBooks(Model model) {
        model.addAttribute("books", bookDAO.getAllBooks());
        return "books/index";
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable("id") int id, Model model, @ModelAttribute("reader") Reader reader) {
        model.addAttribute("book", bookDAO.getBookById(id));
        Optional<Reader> bookOwner = bookDAO.getReaderById(id);
        if (bookOwner.isPresent()) {
            model.addAttribute("owner", bookOwner.get());
        } else {
            model.addAttribute("readers", readerDAO.getAllReaders());
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String saveBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        bookDAO.addBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.getBookById(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        bookDAO.updateBook(book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookDAO.deleteBook(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        bookDAO.releaseBook(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int id, @ModelAttribute("reader") Reader reader) {
        bookDAO.assignBook(id, reader);
        return "redirect:/books/" + id;
    }
}
