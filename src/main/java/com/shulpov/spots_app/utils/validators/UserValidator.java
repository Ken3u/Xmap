package com.shulpov.spots_app.utils.validators;

import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;

/**
 * Класс, проверяющий экземпляры класса пользователя на валидность. Класс-@Component.
 * @author Victor Shulpov "vshulpov@gmail.com"
 * @version 1.0
 * @since 1.0
 */
@Component
public class UserValidator implements Validator {
    /** Сервис пользователей */
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserValidator.class);

    /**
     * Конструктор компонента
     * @param userService сервис пользователей
     */
    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод, проверяющий на совпадение класса валидируемый объект
     * @param aClass валидируемый класс
     * @return true, если валидируемый класс соотвествует валидатору, иначе - false
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    /**
     *
     * @param target валидируемый класс
     * @param errors список ошибок, относящийся к валидируему классу
     */
    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        logger.atInfo().log("validate user.name={} errors={}", user.getName(), errors);
        boolean isExistingName = userService.findByName(user.getName()).isPresent();
        if(isExistingName) {
            logger.atInfo().log("validate user.name={} name already exists", user.getName());
            // поле, код ошибки, сообщение ошибки
            errors.rejectValue("name", "", "Пользователь с таким именем уже существует");
        }

        boolean isExistingEmail = userService.findByEmail(user.getEmail()).isPresent();
        if(isExistingEmail) {
            logger.atInfo().log("validate user.email={} name already exists", user.getEmail());
            // поле, код ошибки, сообщение ошибки
            errors.rejectValue("email", "", "Пользователь с таким почтовым ящиком уже существует");
        }

        boolean isExistingPhone = userService.findByPhoneNumber(user.getPhoneNumber()).isPresent();
        if(isExistingPhone) {
            logger.atInfo().log("validate user.phoneNumber={} name already exists", user.getPhoneNumber());
            // поле, код ошибки, сообщение ошибки
            errors.rejectValue("phoneNumber", "", "Пользователь с таким телефоном уже существует");
        }

        boolean birthdayLessToday = user.getBirthday().before(new Date(System.currentTimeMillis()));
        if(!birthdayLessToday) {
            logger.atInfo().log("validate birthday is in future birthday={}", user.getBirthday());
            // поле, код ошибки, сообщение ошибки
            errors.rejectValue("birthday", "", "День рождения ещё не наступил");
        }
    }
}
