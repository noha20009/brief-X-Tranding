package org.example;

public class Person {
    private int id;
    private String nom;

    public Person(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setIdPerson(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    @Override
    public String toString(){
        return "id : " +id+ ", nom : " +nom;
    }
}
