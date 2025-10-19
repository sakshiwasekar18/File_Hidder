package org.example.dao;

import org.example.db.MyConnection;
import org.example.model.Data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataDAO {
    private static final Logger LOGGER = Logger.getLogger(DataDAO.class.getName());

    // Get all hidden files for a user
    public static List<Data> getAllFiles(String email) {
        List<Data> files = new ArrayList<>();
        try {
            Connection connection = MyConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM data WHERE email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String path = rs.getString("path");
                files.add(new Data(id, name, path, email));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error fetching files for: " + email, ex);
        }
        return files;
    }

    // Hide a file (delete original after saving)
    public static int hideFile(Data file) {
        int result = -1;
        try {
            Connection connection = MyConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO data(name, path, email, bin_data) VALUES (?, ?, ?, ?)"
            );
            ps.setString(1, file.getFileName());
            ps.setString(2, file.getPath());
            ps.setString(3, file.getEmail());

            File f = new File(file.getPath());
            FileInputStream fis = new FileInputStream(f);
            ps.setBinaryStream(4, fis, (int) f.length());

            result = ps.executeUpdate();
            fis.close();

            if (result > 0) {
                if (f.delete()) {
                    LOGGER.info("Original file deleted after hiding: " + f.getAbsolutePath());
                } else {
                    LOGGER.warning("Failed to delete original file: " + f.getAbsolutePath());
                }
            }
        } catch (SQLException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error hiding file: " + file.getFileName(), ex);
        }
        return result;
    }

    // Unhide a file (restore it to disk)
    public static void unhide(int id) {
        try {
            Connection connection = MyConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT path, bin_data FROM data WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String path = rs.getString("path");
                Blob blob = rs.getBlob("bin_data");

                try (InputStream is = blob.getBinaryStream();
                     FileOutputStream fos = new FileOutputStream(path)) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                }

                // Delete the record from DB after unhiding
                ps = connection.prepareStatement("DELETE FROM data WHERE id = ?");
                ps.setInt(1, id);
                ps.executeUpdate();

                LOGGER.info("Successfully unhidden file: " + path);
            }
        } catch (SQLException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error unhiding file with ID: " + id, ex);
        }
    }
}
