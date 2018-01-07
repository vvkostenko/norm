package norm;

public class Hello {
    private String greeting = "Hello";

    public Hello(String greeting) {
        this.greeting = greeting;
    }

    public Hello() {
    }

    public static void main(String[] args) {
        Hello hello = new Hello();
        Hello aloha = new Hello("Aloha");
        System.out.println(hello.saySomething("World"));
        System.out.println(aloha.saySomething("el Presidente"));
    }

    public String saySomething(String name) {
        if (name == null)
            throw new IllegalArgumentException("Setup name");
        else
            return greeting + ", " + name;
    }
}
