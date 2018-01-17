package norm.NextGenSyntaxer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class Rule {

    private List<SyntaxMap.Element> elements = new ArrayList<>();

    public Rule add(SyntaxMap.Element element) {
        elements.add(element);
        return this;
    }

    public Rule addAll(Collection<? extends SyntaxMap.Element> elements) {
        this.elements.addAll(elements);
        return this;
    }

    public List<SyntaxMap.Element> getElements() {
        return elements;
    }

    public void setElements(List<SyntaxMap.Element> elements) {
        this.elements = elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rule)) return false;
        Rule rule = (Rule) o;
        return Objects.equals(getElements(), rule.getElements());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getElements());
    }

    public SyntaxMap.Element get(int index) {
        return elements.get(index);
    }

    public int size() {
        return elements.size();
    }

    public List<SyntaxMap.Element> subList(int begin, int end) {
        return elements.subList(begin,end);
    }
}
