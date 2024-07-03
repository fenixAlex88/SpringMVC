package by.alex.spring.controllers;

import by.alex.spring.dao.ReaderDAO;
import by.alex.spring.models.Reader;
import by.alex.spring.util.ReaderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/readers")
public class ReaderController {

    private final ReaderDAO readerDAO;
    private final ReaderValidator readerValidator;

    @Autowired
    public ReaderController(ReaderDAO readerDAO, ReaderValidator readerValidator) {
        this.readerDAO = readerDAO;
        this.readerValidator = readerValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("readers", readerDAO.getAllReaders());

        return "readers/index";
    }

    @GetMapping("/{id}")
    public String show (@PathVariable Integer id, Model model) {
        model.addAttribute("reader", readerDAO.getReaderById(id));
        model.addAttribute("books", readerDAO.getBookByReaderId(id));

        return "readers/show";
    }

    @GetMapping("/new")
    public String newReader (@ModelAttribute ("reader") Reader reader) {
        return "readers/new";
    }

    @PostMapping()
    public String createReader (@ModelAttribute ("reader") @Valid Reader reader, BindingResult bindingResult) {
        readerValidator.validate(reader, bindingResult);
        if (bindingResult.hasErrors()) {
            return "readers/new";
        }
        readerDAO.addReader(reader);
        return "redirect:/readers";
    }

    @GetMapping("/{id}/edit")
    public String editReader (@PathVariable("id") int id, Model model) {
        model.addAttribute("reader", readerDAO.getReaderById(id));
        return "readers/edit";
    }

    @PatchMapping("/{id}")
    public String updateReader (@PathVariable("id") int id, @ModelAttribute ("reader") @Valid Reader reader, BindingResult bindingResult) {
        readerValidator.validate(reader, bindingResult);
        if (bindingResult.hasErrors()) {
            return "readers/edit";
        }
        readerDAO.updateReader(reader);
        return "redirect:/readers";
    }

    @DeleteMapping("/id")
    public String deleteReader (@PathVariable("id") int id) {
        readerDAO.deleteReader(id);
        return "redirect:/readers";
    }

}
