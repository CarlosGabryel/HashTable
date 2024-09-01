public class TabelaHash_java {

    // Método principal para testes
    public static void main(String[] args) {
        HashTable tabela = new HashTable(10, 50, 2);

        tabela.insere("teste1");
        tabela.insere("teste2");
        tabela.insere("teste3");

        System.out.println("Busca teste1: " + tabela.busca("teste1")); // true
        System.out.println("Busca teste4: " + tabela.busca("teste4")); // false

        tabela.remove("teste1");
        System.out.println("Busca teste1 após remoção: " + tabela.busca("teste1")); // false
    }

}
