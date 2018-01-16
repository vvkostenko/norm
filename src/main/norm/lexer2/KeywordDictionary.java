package norm.lexer2;

import norm.lexer.TokenType;

import java.util.*;

/**
 * Created by akim on 16.01.18.
 */
public class KeywordDictionary {
    private Map<Character, Set<String>> map = new HashMap<>();
    private Map<String, TokenType> typeMap = new HashMap<>();

    public Set<Character> getFirstChars() {
        return map.keySet();
    }

    public KeywordDictionary add(String keyword, TokenType resultType) {
        typeMap.put(keyword, resultType);
        if (map.containsKey(keyword.charAt(0))) {
            map.get(keyword.charAt(0)).add(keyword);
        } else {
            Set<String> kset = new HashSet<>();
            kset.add(keyword);
            map.put(keyword.charAt(0), kset);
        }

        return this;
    }

    public Set<String> getFor(char ch) {
        return !map.containsKey(ch) ? Collections.emptySet() : map.get(ch);
    }

    public TokenType getType(String type) {
        return typeMap.get(type);
    }
}
