package cc.asjks.bms.client.model;

public class Money {
    private String currency;
    private Double amount;

    public void setCurrency(String currency){
        this.currency=currency;
    }
    public String getCurrency(){
        return currency;
    }
    public void setAmount(Double amount){
        this.amount=amount;
    }
    public Double getAmount(){
        return amount;
    }
}
