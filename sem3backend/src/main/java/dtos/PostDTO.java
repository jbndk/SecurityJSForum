package dtos;

public class PostDTO {

    private String category;
    private String imageurl;
    private String content;
    private String author;

    public PostDTO() {
    }

    public PostDTO(String category, String imageurl, String content, String author) {
        this.category = category;
        this.imageurl = imageurl;
        this.content = content;
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}