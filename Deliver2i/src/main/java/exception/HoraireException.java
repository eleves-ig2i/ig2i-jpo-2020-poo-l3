package exception;

public class HoraireException  extends Exception{

    private String dateLue;

    public HoraireException(String dateLue) {
        this.dateLue = dateLue;
    }

    public String getDateLue() {
        return dateLue;
    }

    @Override
    public String toString() {
        return "HoraireException{" +
                "dateLue=" + dateLue +
                '}';
    }
}
