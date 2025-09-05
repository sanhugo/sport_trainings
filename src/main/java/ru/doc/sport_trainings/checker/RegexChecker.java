package ru.doc.sport_trainings.checker;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegexChecker {
    public boolean checkforNumber(String num)
    {
        String num_correct = num.trim();
        if (num_correct.isBlank())
        {
            return false;
        } else {
            String regex = "^(0|[1-9][0-9]*)([.,][0-9]+)?$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(num_correct);
            return matcher.matches();
        }
    }
}
