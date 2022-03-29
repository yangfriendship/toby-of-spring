package springbook.factorybean;

import lombok.Getter;

@Getter
public class Message {

    private final String text;

    private Message(String text) {
        this.text = text;
    }

    public static Message newMessage(String text) {
        return new Message(text);
    }

}
