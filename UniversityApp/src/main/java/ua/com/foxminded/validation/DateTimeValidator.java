package ua.com.foxminded.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateTimeValidator implements ConstraintValidator<ValidDateTime, String> {

	private Pattern pattern;
	private Matcher matcher;
	private static final String DATE_TIME_PATTERN = "^\\d{4}[-]\\d{2}[-]\\d{2}[T]\\d{2}[:]\\d{2}[:]\\d{2}$";

	@Override
	public boolean isValid(final String dateTime, final ConstraintValidatorContext context) {
		pattern = Pattern.compile(DATE_TIME_PATTERN);
		if (dateTime == null) {
			return false;
		}
		matcher = pattern.matcher(dateTime);
		return matcher.matches();
	}

}