package serverPackage1;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.*;
import java.io.*;


public class Serveur1 {
    public static void main(String[] args) {
        int port = 1234;
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Serveur en attente sur le port " + port + "...");
            while (true) {
                try (Socket s = ss.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                     PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {
                    System.out.println("Client connecté : " + s.getInetAddress());
                    String op1;
                    // Le client envoie 3 lignes : opérande 1, opérateur, opérande 2
                    while ((op1 = in.readLine()) != null) {
                        String operateur = in.readLine();
                        String op2 = in.readLine();
                        if (operateur == null || op2 == null) break;
                        System.out.println("Reçu : " + op1 + " " + operateur + " " + op2);
                        out.println(calculer(op1, operateur, op2));
                    }
                    System.out.println("Client déconnecté.");
                } catch (IOException e) {
                    System.err.println("Erreur client : " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String calculer(String a, String op, String b) {
        try {
            double x = Double.parseDouble(a), y = Double.parseDouble(b);
            double r;
            switch (op) {
                case "+": r = x + y; break;
                case "-": r = x - y; break;
                case "*": r = x * y; break;
                case "/":
                    if (y == 0) return "Erreur : division par zéro";
                    r = x / y; break;
                default: return "Erreur : opérateur inconnu";
            }
            return (r == Math.rint(r)) ? String.valueOf((long) r) : String.valueOf(r);
        } catch (NumberFormatException e) {
            return "Erreur : opérande invalide";
        }
    }
}