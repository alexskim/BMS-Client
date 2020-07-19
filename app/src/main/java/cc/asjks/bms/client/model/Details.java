package cc.asjks.bms.client.model;

import java.util.List;

public class Details {
    private String result;
    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }
    public static class DataBean {
        private int id;
        private int uid;
        private String date;
        private int toUid;
        private Double amount;
        private String currency;
        private String type;

        public int getId(){
            return id;
        }
        public void setId(int id){
            this.id=id;
        }
        public int getUid(){
            return uid;
        }
        public void setUid(int uid){
            this.uid=uid;
        }
        public String getDate(){
            return date;
        }
        public void setDate(String date){
            this.date=date;
        }
        public int getToUid(){
            return toUid;
        }
        public void setToUid(int toUid){
            this.toUid=toUid;
        }
        public Double getAmount(){
            return amount;
        }
        public void setAmount(Double amount){
            this.amount=amount;
        }
        public String getCurrency(){
            return currency;
        }
        public void setCurrency(String currency){
            this.currency=currency;
        }
        public String getType(){
            return type;
        }
        public void setType(String type){
            this.type=type;
        }
    }
}
