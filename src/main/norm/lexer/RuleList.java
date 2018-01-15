package norm.lexer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by akim on 12.01.18.
 */
public class RuleList {
    Map<Character, Rule> map = new HashMap<Character, Rule>();

    public RuleList add(char c, Rule rule) {
        map.put(c, rule);
        return this;
    }

    public Rule getRule(char c) {
        return map.get(c);
    }

    public boolean contains(char c) {
        return map.containsKey(c);
    }
}
