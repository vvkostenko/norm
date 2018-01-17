package norm.NextGenSyntaxer;

import java.util.ArrayList;

public class Rule extends ArrayList<Object> {
    public Rule mysubList(int begin, int end) {
        Rule r = new Rule();
        for(int i = begin; i < end; i++) {
            r.add(this.get(i));
        }
        return r;
    }


}
