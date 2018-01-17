package norm.NextGenSyntaxer;

import norm.lexer.Token;

import java.util.*;

public class Greibach {
    static HashMap<SyntaxMap.Element, List<Rule>> convert(HashMap<SyntaxMap.Element, List<Rule>> source) {
        for (SyntaxMap.Element workingNonTerminal : source.keySet()) {
            List<Rule> currentRules = source.get(workingNonTerminal);
            boolean clear = false;
            while (!clear) {
                clear = true;
                for (int i = 0; i < currentRules.size(); i++) {
                    Rule checkingRule = currentRules.get(i);
                    if (checkingRule.get(0) instanceof SyntaxMap.NonTerm) {
                        clear = false;
                        List<Rule> replacedNonTerminalRules = source.get(checkingRule.get(0));
                        List<SyntaxMap.Element> alpha = checkingRule.subList(1, checkingRule.size());
                        currentRules.remove(checkingRule);
                        for (Rule otherRule : replacedNonTerminalRules) {
                            Rule newRule = new Rule();
                            newRule.addAll(otherRule.getElements());
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
