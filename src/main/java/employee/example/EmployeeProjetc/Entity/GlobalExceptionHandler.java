package employee.example.EmployeeProjetc.Entity;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class GlobalExceptionHandler {
    public org.springframework.http.ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String errorMessage) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.value());
        errorResponse.put("error_message", errorMessage);
        return org.springframework.http.ResponseEntity.status(status).body(errorResponse);
    }

    public org.springframework.http.ResponseEntity<Map<String, Object>> buildSuccessResponse(String message) {
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("status", HttpStatus.OK.value());
        successResponse.put("message", message);
        return org.springframework.http.ResponseEntity.ok().body(successResponse);
    }
}
