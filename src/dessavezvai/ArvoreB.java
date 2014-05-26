package dessavezvai;

import java.util.ArrayList;

public class ArvoreB {

    public static final int T = 3;
    private NodoB bRaiz;
    private static final int LEFT_CHILD_NODE = 0;
    private static final int RIGHT_CHILD_NODE = 1;

    

    public ArvoreB() {
        bRaiz = new NodoB();
        bRaiz.isFolha = true;
    }

    public void add(int chave, Object object) {
        NodoB nodoRaiz = bRaiz;
        if (!update(bRaiz, chave, object)) {
            if (nodoRaiz.mNumKeys == (2 * T - 1)) {
                NodoB novoNodo = new NodoB();
                bRaiz = novoNodo;
                novoNodo.isFolha = false;
                bRaiz.bNodosFilhos[0] = nodoRaiz;
                divideNodoFilho(novoNodo, 0, nodoRaiz); // Split nodoRaiz and move its median (middle) chave up into novoNodo.
                insereNaoCheio(novoNodo, chave, object); // Insert the chave into the B-Tree with root novoNodo.
            } else {
                insereNaoCheio(nodoRaiz, chave, object); // Insert the chave into the B-Tree with root nodoRaiz.
            }
        }
    }

        // Split the nodo, nodo, of a B-Tree into two nodos that both contain T-1 elements and move nodo's median chave up to the nodoPai.
    // This method will only be called if nodo is full; nodo is the i-th child of nodoPai.
    void divideNodoFilho(NodoB nodoPai, int i, NodoB nodo) {
        NodoB bNovoNodo = new NodoB();
        bNovoNodo.isFolha = nodo.isFolha;
        bNovoNodo.mNumKeys = T - 1;
        for (int j = 0; j < T - 1; j++) { // Copy the last T-1 elements of nodo into bNovoNodo.
            bNovoNodo.mKeys[j] = nodo.mKeys[j + T];
            bNovoNodo.mObjects[j] = nodo.mObjects[j + T];
        }
        if (!bNovoNodo.isFolha) {
            for (int j = 0; j < T; j++) { // Copy the last T pointers of nodo into bNovoNodo.
                bNovoNodo.bNodosFilhos[j] = nodo.bNodosFilhos[j + T];
            }
            for (int j = T; j <= nodo.mNumKeys; j++) {
                nodo.bNodosFilhos[j] = null;
            }
        }
        for (int j = T; j < nodo.mNumKeys; j++) {
            nodo.mKeys[j] = 0;
            nodo.mObjects[j] = null;
        }
        nodo.mNumKeys = T - 1;

        // Insert a (child) pointer to nodo bNovoNodo into the nodoPai, moving other chaves and pointers as necessary.
        for (int j = nodoPai.mNumKeys; j >= i + 1; j--) {
            nodoPai.bNodosFilhos[j + 1] = nodoPai.bNodosFilhos[j];
        }
        nodoPai.bNodosFilhos[i + 1] = bNovoNodo;
        for (int j = nodoPai.mNumKeys - 1; j >= i; j--) {
            nodoPai.mKeys[j + 1] = nodoPai.mKeys[j];
            nodoPai.mObjects[j + 1] = nodoPai.mObjects[j];
        }
        nodoPai.mKeys[i] = nodo.mKeys[T - 1];
        nodoPai.mObjects[i] = nodo.mObjects[T - 1];
        nodo.mKeys[T - 1] = 0;
        nodo.mObjects[T - 1] = null;
        nodoPai.mNumKeys++;
    }

    // Insert an element into a B-Tree. (The element will ultimately be inserted into a leaf nodo). 
    void insereNaoCheio(NodoB nodo, int chave, Object object) {
        int i = nodo.mNumKeys - 1;
        if (nodo.isFolha) {
            // Since nodo is not a full nodo insert the new element into its proper place within nodo.
            while (i >= 0 && chave < nodo.mKeys[i]) {
                nodo.mKeys[i + 1] = nodo.mKeys[i];
                nodo.mObjects[i + 1] = nodo.mObjects[i];
                i--;
            }
            i++;
            nodo.mKeys[i] = chave;
            nodo.mObjects[i] = object;
            nodo.mNumKeys++;
        } else {
                        // Move back from the last chave of nodo until we find the child pointer to the nodo
            // that is the root nodo of the subtree where the new element should be placed.
            while (i >= 0 && chave < nodo.mKeys[i]) {
                i--;
            }
            i++;
            if (nodo.bNodosFilhos[i].mNumKeys == (2 * T - 1)) {
                divideNodoFilho(nodo, i, nodo.bNodosFilhos[i]);
                if (chave > nodo.mKeys[i]) {
                    i++;
                }
            }
            insereNaoCheio(nodo.bNodosFilhos[i], chave, object);
        }
    }

    public void delete(int chave) {
        delete(bRaiz, chave);
    }

    public void delete(NodoB nodo, int chave) {
        if (nodo.isFolha) { // 1. If the chave is in nodo and nodo is a leaf nodo, then delete the chave from nodo.
            int i;
            if ((i = nodo.binarySearch(chave)) != -1) { // chave is i-th chave of nodo if nodo contains chave.
                nodo.remove(i, LEFT_CHILD_NODE);
            }
        } else {
            int i;
            if ((i = nodo.binarySearch(chave)) != -1) { // 2. If nodo is an internal nodo and it contains the chave... (chave is i-th chave of nodo if nodo contains chave)                   
                NodoB filhoEsquerdo = nodo.bNodosFilhos[i];
                NodoB filhoDireito = nodo.bNodosFilhos[i + 1];
                if (filhoEsquerdo.mNumKeys >= T) { // 2a. If the predecessor child nodo has at least T chaves...
                    NodoB predecessorNode = filhoEsquerdo;
                    NodoB erasureNode = predecessorNode; // Make sure not to delete a chave from a nodo with only T - 1 elements.
                    while (!predecessorNode.isFolha) { // Therefore only descend to the previous nodo (erasureNode) of the predecessor nodo and delete the chave using 3.
                        erasureNode = predecessorNode;
                        predecessorNode = predecessorNode.bNodosFilhos[nodo.mNumKeys - 1];
                    }
                    nodo.mKeys[i] = predecessorNode.mKeys[predecessorNode.mNumKeys - 1];
                    nodo.mObjects[i] = predecessorNode.mObjects[predecessorNode.mNumKeys - 1];
                    delete(erasureNode, nodo.mKeys[i]);
                } else if (filhoDireito.mNumKeys >= T) { // 2b. If the successor child nodo has at least T chaves...
                    NodoB successorNode = filhoDireito;
                    NodoB erasureNode = successorNode; // Make sure not to delete a chave from a nodo with only T - 1 elements.
                    while (!successorNode.isFolha) { // Therefore only descend to the previous nodo (erasureNode) of the predecessor nodo and delete the chave using 3.
                        erasureNode = successorNode;
                        successorNode = successorNode.bNodosFilhos[0];
                    }
                    nodo.mKeys[i] = successorNode.mKeys[0];
                    nodo.mObjects[i] = successorNode.mObjects[0];
                    delete(erasureNode, nodo.mKeys[i]);
                } else { // 2c. If both the predecessor and the successor child nodo have only T - 1 chaves...
                    // If both of the two child nodos to the left and right of the deleted element have the minimum number of elements,
                    // namely T - 1, they can then be joined into a single nodo with 2 * T - 2 elements.
                    int indexMediano = mergeNodes(filhoEsquerdo, filhoDireito);
                    moveKey(nodo, i, RIGHT_CHILD_NODE, filhoEsquerdo, indexMediano); // Delete i's right child pointer from nodo.
                    delete(filhoEsquerdo, chave);
                }
            } else { // 3. If the chave is not resent in nodo, descent to the root of the appropriate subtree that must contain chave...
                // The method is structured to guarantee that whenever delete is called recursively on nodo "nodo", the number of chaves
                // in nodo is at least the minimum degree T. Note that this condition requires one more chave than the minimum required
                // by usual B-tree conditions. This strengthened condition allows us to delete a chave from the tree in one downward pass
                // without having to "back up".
                i = nodo.subtreeRootNodeIndex(chave);
                NodoB childNode = nodo.bNodosFilhos[i]; // childNode is i-th child of nodo.                               
                if (childNode.mNumKeys == T - 1) {
                    NodoB leftChildSibling = (i - 1 >= 0) ? nodo.bNodosFilhos[i - 1] : null;
                    NodoB rightChildSibling = (i + 1 <= nodo.mNumKeys) ? nodo.bNodosFilhos[i + 1] : null;
                    if (leftChildSibling != null && leftChildSibling.mNumKeys >= T) { // 3a. The left sibling has >= T chaves...                                              
                        // Move a chave from the subtree's root nodo down into childNode along with the appropriate child pointer.
                        // Therefore, first shift all elements and children of childNode right by 1.
                        childNode.shiftRightByOne();
                        childNode.mKeys[0] = nodo.mKeys[i - 1]; // i - 1 is the chave index in nodo that is smaller than childNode's smallest chave.
                        childNode.mObjects[0] = nodo.mObjects[i - 1];
                        if (!childNode.isFolha) {
                            childNode.bNodosFilhos[0] = leftChildSibling.bNodosFilhos[leftChildSibling.mNumKeys];
                        }
                        childNode.mNumKeys++;

                        // Move a chave from the left sibling into the subtree's root nodo. 
                        nodo.mKeys[i - 1] = leftChildSibling.mKeys[leftChildSibling.mNumKeys - 1];
                        nodo.mObjects[i - 1] = leftChildSibling.mObjects[leftChildSibling.mNumKeys - 1];

                        // Remove the chave from the left sibling along with its right child nodo.
                        leftChildSibling.remove(leftChildSibling.mNumKeys - 1, RIGHT_CHILD_NODE);
                    } else if (rightChildSibling != null && rightChildSibling.mNumKeys >= T) { // 3a. The right sibling has >= T chaves...                                    
                        // Move a chave from the subtree's root nodo down into childNode along with the appropriate child pointer.
                        childNode.mKeys[childNode.mNumKeys] = nodo.mKeys[i]; // i is the chave index in nodo that is bigger than childNode's biggest chave.
                        childNode.mObjects[childNode.mNumKeys] = nodo.mObjects[i];
                        if (!childNode.isFolha) {
                            childNode.bNodosFilhos[childNode.mNumKeys + 1] = rightChildSibling.bNodosFilhos[0];
                        }
                        childNode.mNumKeys++;

                        // Move a chave from the right sibling into the subtree's root nodo. 
                        nodo.mKeys[i] = rightChildSibling.mKeys[0];
                        nodo.mObjects[i] = rightChildSibling.mObjects[0];

                        // Remove the chave from the right sibling along with its left child nodo.                                                
                        rightChildSibling.remove(0, LEFT_CHILD_NODE);
                    } else { // 3b. Both of childNode's siblings have only T - 1 chaves each...
                        if (leftChildSibling != null) {
                            int indexMediano = mergeNodes(childNode, leftChildSibling);
                            moveKey(nodo, i - 1, LEFT_CHILD_NODE, childNode, indexMediano); // i - 1 is the median chave index in nodo when merging with the left sibling.                          
                        } else if (rightChildSibling != null) {
                            int indexMediano = mergeNodes(childNode, rightChildSibling);
                            moveKey(nodo, i, RIGHT_CHILD_NODE, childNode, indexMediano); // i is the median chave index in nodo when merging with the right sibling.
                        }
                    }
                }
                delete(childNode, chave);
            }
        }
    }

    // Merge two nodos and keep the median chave (element) empty.
    int mergeNodes(NodoB dstNode, NodoB srcNode) {
        int indexMediano;
        if (srcNode.mKeys[0] < dstNode.mKeys[dstNode.mNumKeys - 1]) {
            int i;
            // Shift all elements of dstNode right by srcNode.mNumKeys + 1 to make place for the srcNode and the median chave.
            if (!dstNode.isFolha) {
                dstNode.bNodosFilhos[srcNode.mNumKeys + dstNode.mNumKeys + 1] = dstNode.bNodosFilhos[dstNode.mNumKeys];
            }
            for (i = dstNode.mNumKeys; i > 0; i--) {
                dstNode.mKeys[srcNode.mNumKeys + i] = dstNode.mKeys[i - 1];
                dstNode.mObjects[srcNode.mNumKeys + i] = dstNode.mObjects[i - 1];
                if (!dstNode.isFolha) {
                    dstNode.bNodosFilhos[srcNode.mNumKeys + i] = dstNode.bNodosFilhos[i - 1];
                }
            }

            // Clear the median chave (element).
            indexMediano = srcNode.mNumKeys;
            dstNode.mKeys[indexMediano] = 0;
            dstNode.mObjects[indexMediano] = null;

            // Copy the srcNode's elements into dstNode.
            for (i = 0; i < srcNode.mNumKeys; i++) {
                dstNode.mKeys[i] = srcNode.mKeys[i];
                dstNode.mObjects[i] = srcNode.mObjects[i];
                if (!srcNode.isFolha) {
                    dstNode.bNodosFilhos[i] = srcNode.bNodosFilhos[i];
                }
            }
            if (!srcNode.isFolha) {
                dstNode.bNodosFilhos[i] = srcNode.bNodosFilhos[i];
            }
        } else {
            // Clear the median chave (element).
            indexMediano = dstNode.mNumKeys;
            dstNode.mKeys[indexMediano] = 0;
            dstNode.mObjects[indexMediano] = null;

            // Copy the srcNode's elements into dstNode.
            int offset = indexMediano + 1;
            int i;
            for (i = 0; i < srcNode.mNumKeys; i++) {
                dstNode.mKeys[offset + i] = srcNode.mKeys[i];
                dstNode.mObjects[offset + i] = srcNode.mObjects[i];
                if (!srcNode.isFolha) {
                    dstNode.bNodosFilhos[offset + i] = srcNode.bNodosFilhos[i];
                }
            }
            if (!srcNode.isFolha) {
                dstNode.bNodosFilhos[offset + i] = srcNode.bNodosFilhos[i];
            }
        }
        dstNode.mNumKeys += srcNode.mNumKeys;
        return indexMediano;
    }

    // Move the chave from srcNode at index into dstNode at indexMediano. Note that the element at index is already empty.
    void moveKey(NodoB srcNode, int srcKeyIndex, int childIndex, NodoB dstNode, int indexMediano) {
        dstNode.mKeys[indexMediano] = srcNode.mKeys[srcKeyIndex];
        dstNode.mObjects[indexMediano] = srcNode.mObjects[srcKeyIndex];
        dstNode.mNumKeys++;

        srcNode.remove(srcKeyIndex, childIndex);

        if (srcNode == bRaiz && srcNode.mNumKeys == 0) {
            bRaiz = dstNode;
        }
    }

    public Object search(int chave) {
        return search(bRaiz, chave);
    }

    // Recursive search method.
    public Object search(NodoB nodo, int chave) {
        int i = 0;
        while (i < nodo.mNumKeys && chave > nodo.mKeys[i]) {
            i++;
        }
        if (i < nodo.mNumKeys && chave == nodo.mKeys[i]) {
            return nodo.mObjects[i];
        }
        if (nodo.isFolha) {
            return null;
        } else {
            return search(nodo.bNodosFilhos[i], chave);
        }
    }

    public Object search2(int chave) {
        return search2(bRaiz, chave);
    }

    // Iterative search method.
    public Object search2(NodoB nodo, int chave) {
        while (nodo != null) {
            int i = 0;
            while (i < nodo.mNumKeys && chave > nodo.mKeys[i]) {
                i++;
            }
            if (i < nodo.mNumKeys && chave == nodo.mKeys[i]) {
                return nodo.mObjects[i];
            }
            if (nodo.isFolha) {
                return null;
            } else {
                nodo = nodo.bNodosFilhos[i];
            }
        }
        return null;
    }

    private boolean update(NodoB nodo, int chave, Object object) {
        while (nodo != null) {
            int i = 0;
            while (i < nodo.mNumKeys && chave > nodo.mKeys[i]) {
                i++;
            }
            if (i < nodo.mNumKeys && chave == nodo.mKeys[i]) {
                nodo.mObjects[i] = object;
                return true;
            }
            if (nodo.isFolha) {
                return false;
            } else {
                nodo = nodo.bNodosFilhos[i];
            }
        }
        return false;
    }

    // Inorder walk over the tree.
    String printBTree(NodoB nodo) {
        String string = "";
        if (nodo != null) {
            if (nodo.isFolha) {
                for (int i = 0; i < nodo.mNumKeys; i++) {
                    string += nodo.mKeys[i] + ", ";
                }
            } else {
                int i;
                for (i = 0; i < nodo.mNumKeys; i++) {
                    string += printBTree(nodo.bNodosFilhos[i]);
                    string += nodo.mKeys[i] + ", ";
                }
                string += printBTree(nodo.bNodosFilhos[i]);
            }
        }
        return string;
    }

    public String toString() {
        return printBTree(bRaiz);
    }

    void validate() throws Exception {
        ArrayList<Integer> array = getKeys(bRaiz);
        for (int i = 0; i < array.size() - 1; i++) {
            if (array.get(i) >= array.get(i + 1)) {
                throw new Exception("B-Tree invalid: " + array.get(i) + " greater than " + array.get(i + 1));
            }
        }
    }

    // Inorder walk over the tree.
    public ArrayList<Integer> getKeys(NodoB nodo) {
        ArrayList<Integer> array = new ArrayList<Integer>();
        if (nodo != null) {
            if (nodo.isFolha) {
                for (int i = 0; i < nodo.mNumKeys; i++) {
                    array.add(nodo.mKeys[i]);
                }
            } else {
                int i;
                for (i = 0; i < nodo.mNumKeys; i++) {
                    array.addAll(getKeys(nodo.bNodosFilhos[i]));
                    array.add(nodo.mKeys[i]);
                }
                array.addAll(getKeys(nodo.bNodosFilhos[i]));
            }
        }
        return array;
    }
}