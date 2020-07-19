package cc.asjks.bms.client.model;

import java.util.List;

public class Ad {
    private String result;
    private List<DataBean> data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    public void setData(List<DataBean> data){
        this.data=data;
    }
    public List<DataBean> getData(){
        return data;
    }

    public static class DataBean {
        private int id;
        private String contact;
        private String content;
        private String img;

        public void setId(int id){
            this.id=id;
        }
        public int getId(){
            return id;
        }
        public void setContact(String contact){
            this.contact=contact;
        }
        public String getContact(){
            return contact;
        }
        public void setContent(String content){
            this.content=content;
        }
        public String getContent(){
            return content;
        }
        public void setImg(String img){
            this.img=img;
        }
        public String getImg(){
            return img;
        }
    }
}
