package mobdev.agrikita.models.notifications;

import java.io.Serializable;

public class Notifications implements Serializable {

    private String title;
    private String content;
    private String date;
    private boolean read_status = false;

    public Notifications(String title, String content, String date, boolean read_status) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.read_status = read_status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRead_status() {
        return read_status;
    }

    public void setRead_status(boolean read_status) {
        this.read_status = read_status;
    }
}
