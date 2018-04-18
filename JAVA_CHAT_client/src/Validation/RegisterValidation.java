package validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidation {

    public boolean validateEmail(String Email) {
        String regx = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        Pattern email = Pattern.compile(regx);
        Matcher match = email.matcher(Email);
        if (match.find()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validateText(String name) {
        String regx = "^[A-Za-z][A-Za-z0-9_]*";
        Pattern userName = Pattern.compile(regx);
        Matcher match = userName.matcher(name);
        if (match.find()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validatePassword(String pass) {
        String regx = "(?=.*[0-9])(?=.*[a-z]).{8,}";
        Pattern password = Pattern.compile(regx);
        Matcher match = password.matcher(pass);
        if (match.find()) {
            return true;
        } else {
            return false;
        }
    }
}
