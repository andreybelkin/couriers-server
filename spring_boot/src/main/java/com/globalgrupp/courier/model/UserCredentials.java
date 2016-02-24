package com.globalgrupp.courier.model;

import org.hibernate.annotations.Cache;

import javax.persistence.*;

/**
 * Created by Lenovo on 24.02.2016.
 */

@Entity
@Table(name="user_credentials")
public class UserCredentials {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="login")
    private String login;

    @Column(name="password")
    private String password;

    public UserCredentials() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
