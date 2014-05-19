package arvoresb;
/**
 *
 * @author groda
 * 
 */
public class Nodo {

    private int m;
    private Registro[] filhos;

    public Nodo(int k, int M) {
        m = k;
        filhos = new Registro[M];
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public Registro[] getFilhos() {
        return filhos;
    }

    public void setFilhos(Registro[] filhos) {
        this.filhos = filhos;
    }
}
