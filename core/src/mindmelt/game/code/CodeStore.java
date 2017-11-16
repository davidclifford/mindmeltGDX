package mindmelt.game.code;

import java.util.ArrayList;
import java.util.List;

public class CodeStore {
    private List<Code> store = new ArrayList<>();

    public void add(Code code) {
        store.add(code);
    }
}
