package SublindWay_server.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> nullPointerException(NullPointerException e){
        return new ResponseEntity<>("Null값 검출", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler
    public ResponseEntity<String> argsException(IllegalAccessException e){
        return new ResponseEntity<>("잘못된 요청", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> globalException(Exception e){
        return new ResponseEntity<>("서버 에러", HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
