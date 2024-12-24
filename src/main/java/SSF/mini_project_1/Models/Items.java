package SSF.mini_project_1.Models;

public class Items {
    private String title;
    private String categoryName;
    private String galleryURL;
    private String itemURL;
    private String price;
    
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public String getGalleryURL() {
        return galleryURL;
    }
    public void setGalleryURL(String galleryURL) {
        this.galleryURL = galleryURL;
    }
    public String getItemURL() {
        return itemURL;
    }
    public void setItemURL(String itemURL) {
        this.itemURL = itemURL;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Items [title=" + title + ", categoryName=" + categoryName + ", galleryURL=" + galleryURL + ", itemURL="
                + itemURL + ", price=" + price + "]";
    }
   
    
    
    
   
}
