/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dessavezvai;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author groda
 */
public class Main {

    public static void main(String[] args) {
        ArvoreB arvore = new ArvoreB();
        arvore.add(10, null);
        arvore.add(10, null);
        arvore.add(2, null);
        arvore.add(44, null);
        arvore.add(26, null);
        arvore.add(53, null);
        arvore.add(1, null);
        arvore.add(77, null);
        arvore.add(64, null);

        System.out.println(arvore.toString());

        arvore.delete(26);
        
        System.out.println(arvore.toString());
    }
}
