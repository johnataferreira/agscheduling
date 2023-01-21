package br.ufu.scheduling.test;

import java.util.Scanner;

public class Teste {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String value = "";
        
        while (true) {
            value = sc.nextLine();
            System.out.println(value.replace("=(", "=STDEV(").replace(" + ", "; ").replace("/10", ""));
        }
    }
}
