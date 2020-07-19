package cc.asjks.bms.client.model;

public class MenuBean {
    private int img;
    private String txt;

    public MenuBean() {
    }

    public MenuBean(int img, String txt) {
        this.img = img;
        this.txt = txt;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
