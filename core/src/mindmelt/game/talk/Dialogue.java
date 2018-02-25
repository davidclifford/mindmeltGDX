package mindmelt.game.talk;

public class Dialogue {
    private String keyword;
    private String reply;
    private int replyCode;

    public Dialogue(String keyword, String reply, int replyCode) {
        this.keyword = keyword;
        this.reply = reply;
        this.replyCode = replyCode;
    }

    public boolean isKeyword(String word) {
        String [] words = keyword.split("\\|");
        for(String w:words) {
            if(w.startsWith(word)) {
                return true;
            }
        }
        return false;
    }

    public String getReply() {
        return reply;
    }

    public int getReplyCode() {
        return replyCode;
    }

    public boolean hasCode() {
        return replyCode > 0;
    }

    public boolean isBye() {
        return keyword.equals("bye");
    }
}
