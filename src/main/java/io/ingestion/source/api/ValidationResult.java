package io.ingestion.source.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jpetty on 7/6/15.
 */
public final class ValidationResult {

    private final List<String> errorMessages;

    public ValidationResult(){
        this.errorMessages = new ArrayList<String>();
    }

    public final ValidationResult addErrorMessage(String message){
        this.errorMessages.add(message);
        return this;
    }

    public final boolean hasErrors(){
        return !this.errorMessages.isEmpty();
    }

    public final boolean isValid(){
        return !this.hasErrors();
    }

    public final String[] getErrors(){
        return this.errorMessages.toArray(new String[this.errorMessages.size()]);
    }

    public final ValidationResult combineWith(final ValidationResult other){
        final ValidationResult output = new ValidationResult();
        output.errorMessages.addAll(this.errorMessages);
        if (other != null){
            output.errorMessages.addAll(other.errorMessages);
        }
        return output;
    }

    public final ValidationException toValidationException(){
        if (this.isValid()){
            return null;
        } else {
            return ValidationException.withMessages(this.errorMessages);
        }
    }

    public static final ValidationResult valid(){
        return new ValidationResult();
    }
}
