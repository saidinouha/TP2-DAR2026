import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client1 {

    public static void main(String[] args) {
        try (Socket s = new Socket("localhost", 1234);
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Connecté au serveur. Saisissez une opération (ex : 55 * 25) ou 'exit'.");
            while (true) {
                System.out.print("> ");
                String saisie = sc.nextLine().trim();
                if (saisie.equalsIgnoreCase("exit")) break;

                // Recherche de l'opérateur (on commence à 1 pour autoriser un premier opérande négatif)
                int pos = -1;
                for (int i = 1; i < saisie.length(); i++) {
                    char c = saisie.charAt(i);
                    if (c == '+' || c == '-' || c == '*' || c == '/') { pos = i; break; }
                }
                if (pos == -1) {
                    System.out.println("Syntaxe invalide (ex : 55 * 25). Rien n'est envoyé.");
                    continue;
                }
                String op1 = saisie.substring(0, pos).trim();
                String operateur = String.valueOf(saisie.charAt(pos));
                String op2 = saisie.substring(pos + 1).trim();

                if (!estNombre(op1) || !estNombre(op2)) {
                    System.out.println("Syntaxe invalide (ex : 55 * 25). Rien n'est envoyé.");
                    continue;
                }

                out.println(op1);       // premier opérande
                out.println(operateur); // opérateur
                out.println(op2);       // deuxième opérande

                String resultat = in.readLine();
                if (resultat == null) { System.out.println("Serveur déconnecté."); break; }
                System.out.println("Résultat : " + resultat);
            }
        } catch (IOException e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }

    // Nombre valide : signe '-' optionnel, chiffres, au plus un point décimal
    private static boolean estNombre(String t) {
        if (t.isEmpty()) return false;
        int i = 0, chiffres = 0, points = 0;
        if (t.charAt(0) == '-') i = 1;
        for (; i < t.length(); i++) {
            char c = t.charAt(i);
            if (Character.isDigit(c)) chiffres++;
            else if (c == '.') points++;
            else return false;
        }
        return chiffres > 0 && points <= 1;
    }
}