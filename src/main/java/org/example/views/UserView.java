package org.example.views;

import org.example.dao.DataDAO;
import org.example.model.Data;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserView {
    private static final Logger LOGGER = Logger.getLogger(UserView.class.getName());
    private final String email;

    public UserView(String email) {
        this.email = email;
    }

    public void home() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome " + email);
            System.out.println("Press 1 to show hidden files");
            System.out.println("Press 2 to hide a new file");
            System.out.println("Press 3 to unhide a file");
            System.out.println("Press 0 to exit");

            int ch;
            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException ex) {
                System.out.println("Invalid input");
                continue;
            }

            switch (ch) {
                case 1 -> showHiddenFiles();
                case 2 -> hideNewFile(sc);
                case 3 -> unhideFile(sc);
                case 0 -> {
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void showHiddenFiles() {
        List<Data> files = DataDAO.getAllFiles(email);
        System.out.println("ID - File Name");
        for (Data file : files) {
            System.out.println(file.getId() + " - " + file.getFileName());
        }
    }

    private void hideNewFile(Scanner sc) {
        System.out.println("Enter the file path:");
        String path = sc.nextLine();
        File f = new File(path);
        Data file = new Data(0, f.getName(), path, email);
        int result = DataDAO.hideFile(file);
        if (result > 0) {
            System.out.println("File hidden successfully.");
        } else {
            System.out.println("Failed to hide file.");
        }
    }

    private void unhideFile(Scanner sc) {
        List<Data> files = DataDAO.getAllFiles(email);
        System.out.println("ID - File Name");
        for (Data file : files) {
            System.out.println(file.getId() + " - " + file.getFileName());
        }
        System.out.println("Enter the ID of the file to unhide:");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid ID");
            return;
        }
        DataDAO.unhide(id);
    }
}
