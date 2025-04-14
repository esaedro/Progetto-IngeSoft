package application;

public class Iscrizione {

    private String codiceUnivoco;
    private int numeroDiIscritti;

    public Iscrizione(String codiceUnivoco, int numeroDiIscritti) {
        this.codiceUnivoco = codiceUnivoco;
        this.numeroDiIscritti = numeroDiIscritti;
    }

    public String getCodiceUnivoco() {
        return codiceUnivoco;
    }

    public void setCodiceUnivoco(String codiceUnivoco) {
        this.codiceUnivoco = codiceUnivoco;
    }

    public int getNumeroDiIscritti() {
        return numeroDiIscritti;
    }

    public void setNumeroDiIscritti(int numeroDiIscritti) {
        this.numeroDiIscritti = numeroDiIscritti;
    }
}
