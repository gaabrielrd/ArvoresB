package arvoresb;

/**
 *
 * @author groda, mattheus
 *
 *
 * M precisa ser par maior ou igual a 4
 * @param <Chave> é o tipo de chave a ser usada
 * @param <Valor> é o tipo de dado a ser armazenado
 *
 */
public class ArvoreB<Chave extends Comparable<Chave>, Valor> {

    private static final int M = 4;    // max filhos per B-tree node = M-1

    private Nodo raiz;             // raiz of the B-tree
    private int HT;                // altura of the B-tree
    private int N;                 // number of chave-info pairs in the B-tree

    // constructor
    public ArvoreB() {
        raiz = new Nodo(0, M);
    }

    // return number of chave-info pairs in the B-tree
    public int tamanho() {
        return N;
    }

    // return altura of B-tree
    public int altura() {
        return HT;
    }

    // procura for given chave, return associated info; return null if no such chave
    public Valor get(Chave chave) {
        return procura(raiz, chave, HT);
    }

    private Valor procura(Nodo x, Chave chave, int ht) {
        Registro[] filhos = x.getFilhos();

        // external node
        if (ht == 0) {
            for (int j = 0; j < x.getM(); j++) {
                if (igual(chave, filhos[j].getChave())) {
                    return (Valor) filhos[j].getInfo();
                }
            }
        } // internal node
        else {
            for (int j = 0; j < x.getM(); j++) {
                if (j + 1 == x.getM() || menor(chave, filhos[j + 1].getChave())) {
                    return procura(filhos[j].getProximo(), chave, ht - 1);
                }
            }
        }
        return null;
    }

    public void insere(Chave chave, Valor info) {
        Nodo u = insert(raiz, chave, info, HT);
        N++;
        if (u == null) {
            return;
        }

        // precisa divir
        Nodo t = new Nodo(2, M);
        t.getFilhos()[0] = new Registro(raiz.getFilhos()[0].getChave(), null, raiz);
        t.getFilhos()[1] = new Registro(u.getFilhos()[0].getChave(), null, u);
        raiz = t;
        HT++;
    }

    private Nodo insert(Nodo h, Chave chave, Valor info, int ht) {
        int j;
        Registro t = new Registro(chave, info, null);

        // external node
        if (ht == 0) {
            for (j = 0; j < h.getM(); j++) {
                if (menor(chave, h.getFilhos()[j].getChave())) {
                    break;
                }
            }
        } // internal node
        else {
            for (j = 0; j < h.getM(); j++) {
                if ((j + 1 == h.getM()) || menor(chave, h.getFilhos()[j + 1].getChave())) {
                    Nodo u = insert(h.getFilhos()[j++].getProximo(), chave, info, ht - 1);
                    if (u == null) {
                        return null;
                    }
                    t.setChave(u.getFilhos()[0].getChave());
                    t.setProximo(u);
                    break;
                }
            }
        }

        for (int i = h.getM(); i > j; i--) {
            h.getFilhos()[i] = h.getFilhos()[i - 1];
        }
        h.getFilhos()[j] = t;
        h.setM(h.getM() + 1);
        if (h.getM() < M) {
            return null;
        } else {
            return divide(h);
        }
    }

    // divide node in half
    private Nodo divide(Nodo h) {
        Nodo t = new Nodo(M / 2, M);
        h.setM(M / 2);
        for (int j = 0; j < M / 2; j++) {
            t.getFilhos()[j] = h.getFilhos()[M / 2 + j];
        }
        return t;
    }

    // for debugging
    public String toString() {
        return toString(raiz, HT, "") + "\n";
    }

    private String toString(Nodo h, int ht, String indent) {
        String s = "";
        Registro[] filhos = h.getFilhos();

        if (ht == 0) {
            for (int j = 0; j < h.getM(); j++) {
                s += indent + filhos[j].getChave() + " " + filhos[j].getInfo() + "\n";
            }
        } else {
            for (int j = 0; j < h.getM(); j++) {
                if (j > 0) {
                    s += indent + "(" + filhos[j].getChave() + ")\n";
                }
                s += toString(filhos[j].getProximo(), ht - 1, indent + "     ");
            }
        }
        return s;
    }

    // comparison functions - make Comparable instead of Key to avoid casts
    private boolean menor(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) < 0;
    }

    private boolean igual(Comparable k1, Comparable k2) {
        return k1.compareTo(k2) == 0;
    }

    public static void main(String[] args) {
        ArvoreB<Integer, String> st = new ArvoreB<Integer, String>();

        st.insere(7, "128.112.136.11");
        st.insere(4, "128.112.128.15");
        st.insere(66, "130.132.143.21");
        st.insere(73, "209.052.165.60");
        st.insere(12, "17.112.152.32");
        st.insere(1, "207.171.182.16");
        st.insere(43, "66.135.192.87");
        st.insere(34, "64.236.16.20");
        st.insere(99, "216.239.41.99");
        st.insere(58, "199.239.136.200");
        st.insere(88, "207.126.99.140");
        st.insere(38, "143.166.224.230");
        st.insere(22, "66.35.250.151");
        st.insere(23, "199.181.135.201");
        st.insere(77, "63.111.66.11");
        st.insere(55, "216.109.118.65");

        System.out.println("7:  " + st.get(7));
        System.out.println();

        System.out.println("tamanho:    " + st.tamanho());
        System.out.println("altura:  " + st.altura());
        System.out.println(st);
        System.out.println();

    }

}
