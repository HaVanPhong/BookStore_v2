package com.example.bookstore.Database;

public class BookDAO {
    private  int id;
    private String img;
    private String tenSach;
    private int sao;
    private long gia;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public int getSao() {
        return sao;
    }

    public void setSao(int sao) {
        this.sao = sao;
    }

    public long getGia() {
        return gia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGia(long gia) {
        this.gia = gia;
    }

    public BookDAO() {
    }

    public BookDAO(int id, String img, String tenSach, int sao, long gia) {
        this.id= id;
        this.img = img;
        this.tenSach = tenSach;
        this.sao = sao;
        this.gia = gia;
    }
}
