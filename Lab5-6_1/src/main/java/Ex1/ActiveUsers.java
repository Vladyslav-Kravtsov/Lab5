package main.java.Ex1;

import java.util.ArrayList;

public class ActiveUsers {
    private ArrayList<User> users ;

    public ActiveUsers() {
        this.users = new ArrayList<>();
    }

    public int size() {
        return users.size();
    }

    public boolean isEmpty() {
        return users.isEmpty();
    }

    public boolean contains(User o) {
        return users.contains(o);
    }

    public User get(int index) {
        return users.get(index);
    }

    public void add(User element) {
        users.add(element);
    }

    public void clear() {
        users.clear();
    }
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (User u : users)
            buf.append(u+"\n");
        return buf.toString();
    }
}
