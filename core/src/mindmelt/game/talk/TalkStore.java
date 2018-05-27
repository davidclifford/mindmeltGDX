package mindmelt.game.talk;

import com.badlogic.gdx.math.MathUtils;
import mindmelt.game.engine.Engine;

import java.util.ArrayList;
import java.util.List;

public class TalkStore {
    private List<Talk> talkList;

    public TalkStore() {
        this.talkList = new ArrayList<>();
    }

    public void addTalk(Talk talk) {
        talkList.add(talk);
    }

    public boolean replyFirst(int id) {
        for(Talk t: talkList) {
            if (t.getId() == id) {
                if(t.isTalkfirst())
                    return true;
            }
        }
        return false;
    }

    public String getReply(int id, String keyword, Engine engine) {
        for(Talk t: talkList) {
            if (t.getId() == id) {
                Dialogue reply = t.getReply(keyword);
                if (reply != null) {
                    if (reply.hasCode()) {
                        engine.addTrigger(String.format("Reply %d,%d", id,reply.getReplyCode()));
                    }
                    return reply.getReply();
                }
                return randomDunno();
            }
        }
        return "Doesn't talk";
    }

    private String randomDunno() {
        switch (MathUtils.random(3)) {
            case 0:
                return "Ask something more specific";
            case 1:
                return "You'll have to ask someone else";
            case 2:
                return "I don't know about that";
        }
        return "Dunno, what you are saying";
    }
}