import application.Session;

public class TestMain {
    public static void main(String[] args) {
        Session session = new Session();
        Configuratore configuratore = new Configuratore("C_Dilbert", "admin");
        session.salva();
    }
}
