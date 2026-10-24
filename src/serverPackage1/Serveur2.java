package serverPackage1;


import serializablePackage.Operation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


import java.io.*;
import java.net.*;

public class Serveur2 {
    public static void main(String[] args) {
        int port = 1234;
        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Serveur en attente sur le port " + port + "...");
            while (true) {
                try (Socket s = ss.accept()) {
                    System.out.println("Client connecté : " + s.getInetAddress());
                    // Le flux de sortie est créé (et vidé) avant le flux d'entrée pour éviter un blocage
                    ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                    out.flush();
                    ObjectInputStream in = new ObjectInputStream(s.getInputStream());

                    while (true) {
                        Operation op;
                        try {
                            op = (Operation) in.readObject();          // c) réception de l'objet
                        } catch (EOFException e) {
                            break;                                      // le client a fermé la connexion
                        }
                        System.out.println("Reçu : " + op);
                        double a = op.getOperande1();                   // d) récupération des données
                        double b = op.getOperande2();
                        out.writeObject(calculer(a, op.getOperateur(), b)); // e) + f)
                        out.flush();
                    }
                    System.out.println("Client déconnecté.");
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Erreur client : " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String calculer(double x, char op, double y) {
        double r;
        switch (op) {
            case '+': r = x + y; break;
            case '-': r = x - y; break;
            case '*': r = x * y; break;
            case '/':
                if (y == 0) return "Erreur : division par zéro";
                r = x / y; break;
            default: return "Erreur : opérateur inconnu";
        }
        return (r == Math.rint(r)) ? String.valueOf((long) r) : String.valueOf(r);
    }
}