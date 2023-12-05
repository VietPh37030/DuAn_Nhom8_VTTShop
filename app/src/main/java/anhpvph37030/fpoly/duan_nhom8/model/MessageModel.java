package anhpvph37030.fpoly.duan_nhom8.model;

public class MessageModel {


    public static final String SENT_BY_ME ="Me";
    public static final String SENT_BY_GPT ="GPT";

    String message , sentBy;

    public MessageModel(String message, String sentBy) {
        this.message = message;
        this.sentBy = sentBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }
}
