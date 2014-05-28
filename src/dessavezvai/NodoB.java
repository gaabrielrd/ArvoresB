package dessavezvai;

/**
 *
 * @author groda
 */
public class NodoB {

    public int mNumKeys = 0;
    public int[] mKeys = new int[2 * ArvoreB.T - 1];
    // public Object[] mObjects = new Object[2 * ArvoreB.T - 1];
    public NodoB[] bNodosFilhos = new NodoB[2 * ArvoreB.T];
    public boolean isFolha;

    int buscaBinaria(int key) {
        int esquerdaI = 0;
        int direitaI = mNumKeys - 1;

        while (esquerdaI <= direitaI) {
            final int i = esquerdaI + ((direitaI - esquerdaI) / 2);
            if (mKeys[i] < key) {
                esquerdaI = i + 1;
            } else if (mKeys[i] > key) {
                direitaI = i - 1;
            } else {
                return i;
            }
        }
        //retorna -1 quando não achou
        return -1;
    }

    boolean contains(int key) {
        return buscaBinaria(key) != -1;
    }

    // Remove uma chave do nodo e também o filho direito (1) ou esquerdo (0) 
    void remove(int index, int direitaOuEsquerda) {
        if (index >= 0) {
            int i;
            for (i = index; i < mNumKeys - 1; i++) {
                mKeys[i] = mKeys[i + 1];
                if (!isFolha) {
                    if (i >= index + direitaOuEsquerda) {
                        bNodosFilhos[i] = bNodosFilhos[i + 1];
                    }
                }
            }
            mKeys[i] = 0;
            if (!isFolha) {
                if (i >= index + direitaOuEsquerda) {
                    bNodosFilhos[i] = bNodosFilhos[i + 1];
                }
                bNodosFilhos[i + 1] = null;
            }
            mNumKeys--;
        }
    }

    void empurraPraDireita() {
        if (!isFolha) {
            bNodosFilhos[mNumKeys + 1] = bNodosFilhos[mNumKeys];
        }
        for (int i = mNumKeys - 1; i >= 0; i--) {
            mKeys[i + 1] = mKeys[i];
            if (!isFolha) {
                bNodosFilhos[i + 1] = bNodosFilhos[i];
            }
        }
    }

    int indexDaSubArvore(int key) {
        for (int i = 0; i < mNumKeys; i++) {
            if (key < mKeys[i]) {
                return i;
            }
        }
        return mNumKeys;
    }
}
