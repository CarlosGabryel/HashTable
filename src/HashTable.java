import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.nio.charset.StandardCharsets;

public class HashTable {
    private LinkedList<String>[] tabela; // Tabela hash com listas encadeadas
    private int tamanho; // Tamanho atual da tabela
    private int numElementos; // Número atual de elementos inseridos
    private int a; // Fator de carga desejado (média de elementos por posição)
    private int margem; // Margem para definir redimensionamento

    // Construtor da TabelaHash
    public HashTable(int n, int margem, int a) {
        this.a = a;
        this.margem = margem;
        this.tamanho = n / a; // Tamanho inicial da tabela
        this.tabela = new LinkedList[tamanho];
        this.numElementos = 0;

        // Inicializa cada posição da tabela com uma nova lista encadeada
        for (int i = 0; i < tamanho; i++) {
            tabela[i] = new LinkedList<String>();
        }
    }

    // Função hash utilizando SHA-512 para calcular o índice de uma chave
    private int funcaoHash(String chave) {
        try {
            // Cria uma instância de MessageDigest com o algoritmo SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            
            // Converte a string para bytes e calcula o hash SHA-512
            byte[] hashBytes = md.digest(chave.getBytes(StandardCharsets.UTF_8));
            
            // Converte os primeiros 8 bytes do hash em um long
            long hashLong = 0;
            for (int i = 0; i < 8; i++) {
                hashLong = (hashLong << 8) | (hashBytes[i] & 0xff);
            }
            
            // Calcula o índice da tabela aplicando o operador módulo
            return (int) (Math.abs(hashLong) % tamanho);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo SHA-512 não encontrado", e);
        }
    }

    // Método para inserir uma string na tabela
    public void insere(String str) {
        int indice = funcaoHash(str);
        tabela[indice].add(str);
        numElementos++;

        // Verificar se precisa redimensionar
        if (numElementos / (double) tamanho > (a * (1 + margem / 100.0))) {
            redimensionar();
        }
    }

    // Método para buscar uma string na tabela
    public boolean busca(String str) {
        int indice = funcaoHash(str);
        return tabela[indice].contains(str);
    }

    // Método para remover uma string da tabela
    public void remove(String str) {
        int indice = funcaoHash(str);
        tabela[indice].remove(str);
        numElementos--;
    }

    // Método para redimensionar a tabela quando necessário
    private void redimensionar() {
        int novoTamanho = tamanho * 2; // Exemplo de dobrar o tamanho
        LinkedList<String>[] novaTabela = new LinkedList[novoTamanho];

        for (int i = 0; i < novoTamanho; i++) {
            novaTabela[i] = new LinkedList<String>();
        }

        // Reinsere todos os elementos na nova tabela
        for (LinkedList<String> lista : tabela) {
            for (String str : lista) {
                int indice = funcaoHash(str); // Recalcula o índice usando a nova tabela
                novaTabela[indice].add(str);
            }
        }
        // Atualiza a tabela e o tamanho
        tabela = novaTabela;
        tamanho = novoTamanho;
    }
}
