package org.example;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class UserManager {
    private static final String FILE_PATH = "user.yaml";
    private Yaml yaml;


    public UserManager() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true); // Αυτό προσθέτει καλύτερο indent
        options.setIndent(2); // ή 4 αν θέλεις πιο βαθύ
        this.yaml = new Yaml(options);
    }

    public List<User> loadUsers() {
        try (InputStream inputStream = new FileInputStream(FILE_PATH)) {
            Map<String, Object> data = yaml.load(inputStream);
            List<Map<String, Object>> userList = (List<Map<String, Object>>) data.get("users");
            List<User> users = new ArrayList<>();
            if (userList != null) {
                for (Map<String, Object> userData : userList) {
                    String username = (String) userData.get("username");
                    String password = (String) userData.get("password");

                    List<String> favorites = new ArrayList<>();
                    Object favObj = userData.get("favorites");
                    if (favObj instanceof List<?>) {
                        for (Object o : (List<?>) favObj) {
                            favorites.add(o.toString());
                        }
                    }

                    User user = new User(username, password);
                    user.setFavourites(favorites);
                    users.add(user);
                }
            }
            return users;

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public void saveUsers(List<User> users) {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> userList = new ArrayList<>();


        for (User user : users) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", user.getUsername());
            userData.put("password", user.getPassword());
            userData.put("favorites", user.getFavorites());


            userList.add(userData);
        }

        data.put("users", userList);

        try (OutputStream outputStream = new FileOutputStream(FILE_PATH)) {
            yaml.dump(data, new OutputStreamWriter(outputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void saveFavorites(List<String> favorites) {
        Map<String, Object> data = new HashMap<>();
        data.put("favorites", favorites);
        try (OutputStream outputStream = new FileOutputStream("favorites.yaml")) {
            yaml.dump(data, new OutputStreamWriter(outputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
