package com.kuit.chatdiary.domain;

public enum Sender {

    USER(0),
    DADA(1),
    CHICHI(2),
    LULU(3);

    private final int index;
    Sender(int index){
        this.index = index;
    }
    public int getIndex(){
        return index;
    }

    public static Sender getByIndex(int index) {
        for (Sender sender : Sender.values()) {
            if (sender.index == index) {
                return sender;
            }
        }
        throw new IllegalArgumentException("Invalid index for Sender: " + index);
    }

}
