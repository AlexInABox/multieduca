package sample;

//nur eine Test-datei
public class punkte {
    public static void main(String[] args) {
        Quiz q = new Quiz();
        String[] antworten = {"x","y","z","a"};
        int[] loesungen = {1,2};
        q.addFrage("null", antworten, loesungen, 20);

        int[] antworten2 = {3,2};
        System.out.println(Quiz.gen(q.getFrage(0), antworten2, 3));
    }
}
