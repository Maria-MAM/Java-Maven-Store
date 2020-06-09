package model;

public class Comanda {
    private int id;
    private String produseComandate;
    private double valoare;
    private int userId;

    public Comanda() {
    }

    public Comanda(String produseComandate, double valoare, int userId) {
        this.produseComandate = produseComandate;
        this.valoare = valoare;
        this.userId = userId;
    }

    public Comanda(String produseComandate, double valoare) {
        this.produseComandate = produseComandate;
        this.valoare = valoare;
    }

    public Comanda(int id, String produseComandate, double valoare, int userId) {
        this.id = id;
        this.produseComandate = produseComandate;
        this.valoare = valoare;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduseComandate() {
        return produseComandate;
    }

    public void setProduseComandate(String produseComandate) {
        this.produseComandate = produseComandate;
    }

    public double getValoare() {
        return valoare;
    }

    public void setValoare(double valoare) {
        this.valoare = valoare;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Comanda{" +
                "id=" + id +
                ", produseComandate='" + produseComandate + '\'' +
                ", valoare=" + valoare +
                ", userId=" + userId +
                '}';
    }
}
