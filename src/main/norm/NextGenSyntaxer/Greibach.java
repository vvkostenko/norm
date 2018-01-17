package norm.NextGenSyntaxer;

import norm.lexer.Token;

import java.util.*;

public class Greibach {
    static HashMap<SyntaxMap.NonTerm, List<Rule>> convert(HashMap<SyntaxMap.NonTerm, List<Rule>> source) {
        for (SyntaxMap.NonTerm workingNonTerminal : source.keySet()) {
            List<Rule> currentRules = source.get(workingNonTerminal);
            boolean clear = false;
            while (!clear) {
                clear = true;
                for (int i = 0; i < currentRules.size(); i++) {
                    Rule rule = currentRules.get(i);
                    if (rule.get(0) instanceof SyntaxMap.NonTerm) {
                        clear = false;
                        Rule alpha = rule.mysubList(1, rule.size());
                        currentRules.remove(rule);
                        List<Rule> replacedNonTerminalRules = source.get(rule.get(0));
                        for (List<Object> replacedRule : replacedNonTerminalRules) {
                            Rule newRule = new Rule();
                            newRule.addAll(replacedRule);
                            newRule.addAll(alpha);
                            currentRules.add(newRule);
                        }
                    }
                }
            }
        }

        return source;
    }
}
