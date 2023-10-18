package com.blumeglobal.springor.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(Long id){
        super("could not find with entity id: "+id);
    }
}
