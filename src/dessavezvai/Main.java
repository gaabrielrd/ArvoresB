package dessavezvai;

import java.util.Random;

/**
 *
 * @author GabrielRoda, MattheusSgrott
 */
public class Main {

    public static void main(String[] args) {
        ArvoreB arvore = new ArvoreB();
        Random geranumeros = new Random();
        for(int i = 0; i < 50000;i++){
            arvore.add(geranumeros.nextInt(100000));
        }

        System.out.println(arvore.toString());

        arvore.delete(26);
        
        System.out.println(arvore.toString());
        
        if(arvore.search(10)){
            System.out.println("Achou!");
        }
       
    }
}
