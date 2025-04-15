package application;

public class Iscrizione {

    private String codiceUnivoco;


    /**
     * @ invariant numeroDiIscritti > 0;
     */
    private int numeroDiIscritti;

    /**
     * @ requires codiceUnivoco != null && !codiceUnivoco.isEmpty();
     * @ requires numeroDiIscritti > 0
     */
    public Iscrizione(String codiceUnivoco, int numeroDiIscritti) {
        this.codiceUnivoco = codiceUnivoco;
        this.numeroDiIscritti = numeroDiIscritti;
    }

    public String getCodiceUnivoco() {
        return codiceUnivoco;
    }

    /**
     * @ requires codiceUnivoco != null && !codiceUnivoco.isEmpty();
     */
    public void setCodiceUnivoco(String codiceUnivoco) {
        this.codiceUnivoco = codiceUnivoco;
    }

    public int getNumeroDiIscritti() {
        return numeroDiIscritti;
    }

    /**
     * @ requires numeroDiIscritti > 0
     */
    public void setNumeroDiIscritti(int numeroDiIscritti) {
        this.numeroDiIscritti = numeroDiIscritti;
    }
}
