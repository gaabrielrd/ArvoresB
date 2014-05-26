package dessavezvai;

/**
 *
 * @author groda
 */
public class NodoB {

    public int mNumKeys = 0;
    public int[] mKeys = new int[2 * ArvoreB.T - 1];
    public Object[] mObjects = new Object[2 * ArvoreB.T - 1];
    public NodoB[] bNodosFilhos = new NodoB[2 * ArvoreB.T];
    public boolean isFolha;

    int binarySearch(int key) {
        int leftIndex = 0;
        int rightIndex = mNumKeys - 1;

        while (leftIndex <= rightIndex) {
            final int middleIndex = leftIndex + ((rightIndex - leftIndex) / 2);
            if (mKeys[middleIndex] < key) {
                leftIndex = middleIndex + 1;
            } else if (mKeys[middleIndex] > key) {
                rightIndex = middleIndex - 1;
            } else {
                return middleIndex;
            }
        }

        return -1;
    }

    boolean contains(int key) {
        return binarySearch(key) != -1;
    }

    // Remove an element from a node and also the left (0) or right (+1) child.
    void remove(int index, int leftOrRightChild) {
        if (index >= 0) {
            int i;
            for (i = index; i < mNumKeys - 1; i++) {
                mKeys[i] = mKeys[i + 1];
                mObjects[i] = mObjects[i + 1];
                if (!isFolha) {
                    if (i >= index + leftOrRightChild) {
                        bNodosFilhos[i] = bNodosFilhos[i + 1];
                    }
                }
            }
            mKeys[i] = 0;
            mObjects[i] = null;
            if (!isFolha) {
                if (i >= index + leftOrRightChild) {
                    bNodosFilhos[i] = bNodosFilhos[i + 1];
                }
                bNodosFilhos[i + 1] = null;
            }
            mNumKeys--;
        }
    }

    void shiftRightByOne() {
        if (!isFolha) {
            bNodosFilhos[mNumKeys + 1] = bNodosFilhos[mNumKeys];
        }
        for (int i = mNumKeys - 1; i >= 0; i--) {
            mKeys[i + 1] = mKeys[i];
            mObjects[i + 1] = mObjects[i];
            if (!isFolha) {
                bNodosFilhos[i + 1] = bNodosFilhos[i];
            }
        }
    }

    int subtreeRootNodeIndex(int key) {
        for (int i = 0; i < mNumKeys; i++) {
            if (key < mKeys[i]) {
                return i;
            }
        }
        return mNumKeys;
    }
}
