package com.kuit.chatdiary.domain;

public enum Sender {
    USER(0),
    DADA(1),
    CHICHI(2),
    LULU(3);

    private final int sender;
    Sender(int sender){
        this.sender = sender;
    }

    public Integer sender(){
        return sender;
    }
}
