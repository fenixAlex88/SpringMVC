package by.alex.spring.util;

import by.alex.spring.dao.ReaderDAO;
import by.alex.spring.models.Reader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ReaderValidator implements Validator {
    private final ReaderDAO readerDAO;
    @Autowired
    public ReaderValidator(ReaderDAO readerDAO) {
        this.readerDAO = readerDAO;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return Reader.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Reader reader = (Reader) target;
        if(readerDAO.getReaderByName(reader.getName()).isPresent())
            errors.rejectValue("name", null, "Человек с таким именем уже существует");
    }
}
