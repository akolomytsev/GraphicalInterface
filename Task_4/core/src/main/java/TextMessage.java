import java.time.LocalDateTime;

public class TextMessage extends AbstractMessage {
    private String from;
    private String to;
    private String message;
    private LocalDateTime sendAt;

    private TextMessage(){}

    public static TextMessage of(String from, String message){
        TextMessage m = new TextMessage();
        m.from = from;
        m.message = message;
        m.sendAt = LocalDateTime.now();
        return m;
    }
    public static TextMessage of(String from,String to, String message) {
        TextMessage m = new TextMessage();
        m.from = from;
        m.message = message;
        m.to = to;
        m.sendAt = LocalDateTime.now();
        return m;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getSendAt() {
        return sendAt;
    }
}
