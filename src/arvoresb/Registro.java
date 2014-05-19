package arvoresb;
/**
 *
 * @author groda
 * 
 */
public class Registro {
    // nodos internos: usam chave e proximo
    // nodos externos: usam chave e info

    private Comparable chave;
    private Object info;
    private Nodo proximo;

    public Registro(Comparable chave, Object info, Nodo proximo) {
        this.chave = chave;
        this.info = info;
        this.proximo = proximo;
    }

    public Comparable getChave() {
        return chave;
    }

    public void setChave(Comparable chave) {
        this.chave = chave;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    Nodo getProximo() {
        return proximo;
    }

    public void setProximo(Nodo proximo) {
        this.proximo = proximo;
    }
}
