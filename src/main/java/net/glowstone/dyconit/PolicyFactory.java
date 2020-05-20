package net.glowstone.dyconit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

final class PolicyFactory {
    private PolicyFactory() {}

    static IPolicy loadPolicy() {
        String policy = readFile();

        switch(policy) {
            case "Donnybrook":
                System.out.println("Donnybrook policy loaded.");
                return PolicyDonnybrook.setPolicy();
            case "Epicenter":
                System.out.println("Epicenter policy loaded.");
                return PolicyEpicenter.setPolicy();
            default:
                System.out.println("Epicenter policy loaded.");
                return PolicyEpicenter.setPolicy();
        }
    }

    static private String readFile() {
        try {
            File file = new File("./config/policyconfig.txt");

            Scanner sc = new Scanner(file);
            sc.useDelimiter("policy=");

            return sc.next();

        } catch (FileNotFoundException e) {
            return "";
        }
    }
}
