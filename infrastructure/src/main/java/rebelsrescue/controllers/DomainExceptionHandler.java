package rebelsrescue.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import rebelsrescue.fleet.FleetDomainException;
import rebelsrescue.fleet.FleetNotFoundException;
import rebelsrescue.fleet.InsufficientStarShipsException;

/**
 * Global exception handler for domain exceptions.
 * Converts domain exceptions to proper HTTP responses.
 */
@RestControllerAdvice
public class DomainExceptionHandler {

    @ExceptionHandler(FleetNotFoundException.class)
    public ProblemDetail handleFleetNotFound(FleetNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            ex.getMessage()
        );
        problemDetail.setTitle("Fleet Not Found");
        return problemDetail;
    }

    @ExceptionHandler(InsufficientStarShipsException.class)
    public ProblemDetail handleInsufficientStarShips(InsufficientStarShipsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNPROCESSABLE_ENTITY,
            ex.getMessage()
        );
        problemDetail.setTitle("Insufficient StarShips");
        return problemDetail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage()
        );
        problemDetail.setTitle("Invalid Request");
        return problemDetail;
    }

    @ExceptionHandler(FleetDomainException.class)
    public ProblemDetail handleDomainException(FleetDomainException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getMessage()
        );
        problemDetail.setTitle("Domain Error");
        return problemDetail;
    }
}

