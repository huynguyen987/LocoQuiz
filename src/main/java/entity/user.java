package entity;

import javax.management.relation.Role;
import java.sql.*;
import java.util.*;
public class user {
    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private String gender;
    private Role role;
    private String status;
    private Timestamp createAt;
    private Timestamp updateAt;
    private byte[] avatar;

    public user() {
    }

    public user(int id, String username, String email, String passwordHash, String fullName, String gender, Role role, String status, Timestamp createAt, Timestamp updateAt, byte[] avatar) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
    }
}
