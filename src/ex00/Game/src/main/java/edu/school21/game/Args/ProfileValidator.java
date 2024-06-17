package edu.school21.game.Args;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class ProfileValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        String[] allowedValues = {"production", "develop", "p", "dev"};
        for (String allowedValue : allowedValues) {
            if (allowedValue.equalsIgnoreCase(value)) {
                return;
            }
        }
        throw new ParameterException("Parameter " + name + " should be one of " + String.join(", ", allowedValues));
    }
}
