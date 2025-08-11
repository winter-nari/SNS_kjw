package com.winternari.sns_project.global.common.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ClientRequest<T> {
    private boolean success;
    private String message;
    private T data;
}
