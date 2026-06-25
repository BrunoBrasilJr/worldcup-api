package com.portfolio.worldcup.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;   // quando o erro ocorreu
    private int status;                // codigo HTTP (ex: 404)
    private String error;              // nome do erro (ex: "Not Found")
    private String message;            // mensagem amigavel
    private String path;               // qual URL gerou o erro
}