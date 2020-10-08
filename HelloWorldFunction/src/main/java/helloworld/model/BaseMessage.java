package helloworld.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class BaseMessage implements Serializable {

    private static final long serialVersionUID = 2887371402552197531L;

    @JsonProperty(value = "message")
    private String message;

    public BaseMessage() {
    }

    public BaseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
