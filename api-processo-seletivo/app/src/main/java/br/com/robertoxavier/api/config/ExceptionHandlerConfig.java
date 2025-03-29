package br.com.robertoxavier.api.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerConfig extends ResponseEntityExceptionHandler {


    @ExceptionHandler({ Exception.class })
    protected ResponseEntity<Object> handleExceptionDomain(RuntimeException ex, WebRequest request) {
        String mensagemUsuario = ex.getMessage();

        String mensagemDesenvolvedor = tratarMsgDesenvolvedor(ex);

        List<DetalhesErro> erros = Arrays.asList(DetalhesErro.builder().addMsgUsuario(mensagemUsuario)
                .addMsgDesenvolvedor(mensagemDesenvolvedor).addStatus(HttpStatus.BAD_REQUEST)
                .addHttpMethod(getHttpMethod(request)).addPath(getPath(request)).build());

        return super.handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ NotFoundException.class })
    protected ResponseEntity<Object> handleExceptionNotFound(RuntimeException ex, WebRequest request) {

        String mensagemUsuario = ex.getMessage();

        String mensagemDesenvolvedor = tratarMsgDesenvolvedor(ex);

        List<DetalhesErro> erros = Arrays.asList(DetalhesErro.builder().addMsgUsuario(mensagemUsuario)
                .addMsgDesenvolvedor(mensagemDesenvolvedor).addStatus(HttpStatus.BAD_REQUEST)
                .addHttpMethod(getHttpMethod(request)).addPath(getPath(request)).build());

        return super.handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private String getPath(WebRequest request) {

        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }

    private String getHttpMethod(WebRequest request) {

        return ((ServletWebRequest) request).getRequest().getMethod();
    }

    private String tratarMsgDesenvolvedor(RuntimeException ex) {
        String msg = "Causa: " + (ex.getCause() != null ? ex.getCause().toString() : ex.toString());
        if (ex.getStackTrace() != null && ex.getStackTrace().length > 0) {
            for (int i = 0; i < ex.getStackTrace().length; i++) {
                msg += "\n" + ex.getStackTrace()[i].toString();
            }
        }
        return msg;
    }


}
