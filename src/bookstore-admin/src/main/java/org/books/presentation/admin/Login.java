package org.books.presentation.admin;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "LOGIN")
@NamedQuery(name = Login.findByUserNameQuery, query = "SELECT x FROM Login x WHERE LOWER(x.userName) = LOWER(:userName)")
public class Login implements Serializable {

    public static final String findByUserNameQuery = "Login.findByUserNameQuery";

    @TableGenerator(
            name = "general_sequence",
            table = "GENERAL_SEQUENCE",
            allocationSize = 10
    )

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "general_sequence")
    @Column(name = "ID")
    protected Long id;
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String userName;
    @Column(name = "PASSWORD")
    private String password;

    @Column
    private String groupname;

    public Login() {
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public Login(String userName, String password, String groupname) {
        this.userName = userName;
        this.password = password;
        this.groupname = groupname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
