package com.kuit.chatdiary.request;

public class ChatRequest {
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSelectedModel() {
        return selectedModel;
    }

    public void setSelectedModel(Integer selectedModel) {
        this.selectedModel = selectedModel;
    }

    private Long userId;
    private String content;
    private Integer selectedModel;
}
