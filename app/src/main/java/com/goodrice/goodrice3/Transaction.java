package com.goodrice.goodrice3;

public class Transaction {
    String phase;
    String barcode;
    String product;
    String company;
    String result;

    public Transaction() {
    }

    public Transaction(String phase, String barcode, String product, String company, String result) {
        this.phase = phase;
        this.barcode = barcode;
        this.product = product;
        this.company = company;
        this.result = result;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
