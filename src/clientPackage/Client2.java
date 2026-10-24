package clientPackage;

import serializablePackage.Operation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client2 {

    public static void main(String[] args) {
        try (Socket s = new Socket("localhost", 1234);
             Scanner sc = new Scanner(System.in)) {

            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());

            System.out.println("Connecté au serveur. Saisissez une opération (ex : 55 * 25) ou 'exit'.");
            while (true) {
                System.out.print("> ");
                String saisie = sc.nextLine().trim();
                if (saisie.equalsIgnoreCase("exit")) break;

                // Recherche de l'opérateur (départ à 1 pour autoriser un premier opérande négatif)
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
                char operateur = saisie.charAt(pos);
                String op2 = saisie.substring(pos + 1).trim();

                if (!estNombre(op1) || !estNombre(op2)) {
                    System.out.println("Syntaxe invalide (ex : 55 * 25). Rien n'est envoyé.");
                    continue;
                }

                // b) envoi de l'objet Operation
                out.writeObject(new Operation(Double.parseDouble(op1), operateur, Double.parseDouble(op2)));
                out.flush();

                // g) lecture et affichage du résultat
                String resultat = (String) in.readObject();
                System.out.println("Résultat : " + resultat);
            }
        } catch (IOException | ClassNotFoundException e) {
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