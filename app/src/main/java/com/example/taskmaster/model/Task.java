package com.example.taskmaster.model;

//The state should be one of "new", "assigned", "in progress", or "complete".
public class Task {
    String title;
    String body;
    State state;

    public Task(String title, String body) {
        this.title = title;
        this.body = body;
        this.state = State.NEW;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
