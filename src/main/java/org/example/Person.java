package org.example;

public class Person {
    private int idPerson;
    private String nom;

    public Person(int idPerson, String nom) {
        this.idPerson = idPerson;
        this.nom = nom;
    }

    public int getIdPerson() {
        return idPerson;
    }

    public String getNom() {
        return nom;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
