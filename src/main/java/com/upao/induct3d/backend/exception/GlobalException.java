package com.upao.induct3d.backend.exception;

import com.upao.induct3d.backend.domain.MessageDTO;
import com.upao.induct3d.backend.domain.response.ApiErrorResponse;
import com.upao.induct3d.backend.utils.Operations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException e) {
        ApiErrorResponse error = new ApiErrorResponse("RESOURCE_NOT_FOUND", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AttributeException.class)
    public ResponseEntity<ApiErrorResponse> handleAttributeException(AttributeException e) {
        ApiErrorResponse error = new ApiErrorResponse("USER_ALREADY_EXISTS", e.getMessage(), null);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException e) {
        ApiErrorResponse error = new ApiErrorResponse("INVALID_CREDENTIALS", "Usuario o contraseña incorrectos", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFound(UserNotFoundException e) {
        ApiErrorResponse error = new ApiErrorResponse("USER_NOT_FOUND", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidOrExpiredCodeException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidOrExpiredCode(InvalidOrExpiredCodeException e) {
        ApiErrorResponse error = new ApiErrorResponse("INVALID_OR_EXPIRED_CODE",e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidOrExpiredTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidOrExpiredToken(InvalidOrExpiredTokenException e) {
        ApiErrorResponse error = new ApiErrorResponse("INVALID_OR_EXPIRED_TOKEN", "El token es inválido o ha expirado", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRefreshToken(InvalidRefreshTokenException e) {
        ApiErrorResponse error = new ApiErrorResponse("INVALID_REFRESH_TOKEN", "El token de renovación es inválido o ha expirado", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(TemplatesNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTemplatesNotFound(TemplatesNotFoundException e) {
        ApiErrorResponse error = new ApiErrorResponse("TEMPLATES_NOT_FOUND", "No existen templates disponibles", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TemplateNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTemplateNotFound(TemplateNotFoundException e) {
        ApiErrorResponse error = new ApiErrorResponse("TEMPLATE_NOT_FOUND", "El template solicitado no existe", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ToursNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleToursNotFound(ToursNotFoundException e) {
        ApiErrorResponse error = new ApiErrorResponse("TOURS_NOT_FOUND", "No hay recorridos por listar", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TourNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTourNotFound(TourNotFoundException e) {
        ApiErrorResponse error = new ApiErrorResponse("TOUR_NOT_FOUND", "El tour solicitado no existe", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDTO> generalException(Exception e) {
        return ResponseEntity.internalServerError()
                .body(new MessageDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    @ExceptionHandler(AuthUnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthUnauthorized(AuthUnauthorizedException e) {
        ApiErrorResponse error = new ApiErrorResponse("AUTH_UNAUTHORIZED", "No se ha enviado un token válido", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ForbiddenTourAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleForbiddenTourAccess(ForbiddenTourAccessException e) {
        ApiErrorResponse error = new ApiErrorResponse("FORBIDDEN_TOUR_ACCESS", "No tienes permisos para modificar este tour", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> validationException(MethodArgumentNotValidException e) {
        List<Map<String, String>> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> Map.<String, String>of("field", err.getField(), "message", err.getDefaultMessage()))
                .toList();
        ApiErrorResponse error = new ApiErrorResponse("VALIDATION_ERROR", "Los datos del recorrido no son válidos", details);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(FileRequiredException.class)
    public ResponseEntity<ApiErrorResponse> fileRequired(FileRequiredException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse("FILE_REQUIRED", "No se ha enviado ningún archivo", null)
        );
    }

    @ExceptionHandler(UploadException.class)
    public ResponseEntity<ApiErrorResponse> uploadFailed(UploadException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiErrorResponse("UPLOAD_FAILED", "No se pudo subir la imagen", null)
        );
    }

    @ExceptionHandler(AuthUnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> unauthorized(AuthUnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiErrorResponse("AUTH_UNAUTHORIZED", "No se ha enviado un token válido", null)
        );
    }

    @ExceptionHandler(UploadException.class)
    public ResponseEntity<MessageDTO> uploadException(UploadException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageDTO(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

}
