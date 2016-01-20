package c.sqlite;

/**
 * Created by Łukasz on 2016-01-20.
 */
public class Text {
    private String text;
    private long id;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;

    }

    public Text(){};

    public Text(String text) {
        this.text = text;
    }

    public Text(String text, long id) {
        this.text = text;
        this.id = id;
    }

    @Override
    public String toString() {
        return id + ": " + text;
    }
}
