
// Start the main function from the host.java file located in a subdirectory of
// the current directory called /net
// The main function is called "main" and it takes no arguments

public class test {
    public static void main(String[] args) {
        try {
            host.main(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}