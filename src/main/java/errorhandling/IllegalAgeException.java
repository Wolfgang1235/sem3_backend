package errorhandling;

import facades.UserFacade;

public class IllegalAgeException extends Exception {

    public IllegalAgeException(int age) {
        super("Age should be between " + UserFacade.MINIMUM_AGE + " and " + UserFacade.MAXIMUM_AGE + " but found: " + age);
    }
}
