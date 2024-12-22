package SSF.mini_project_1.Models;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Member {
    
    @NotNull
    private String name;
    private String wishlist;
    private String secretsanta;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getWishlist() {
        return wishlist;
    }
    public void setWishlist(String wishlist) {
        this.wishlist = wishlist;
    }
    public String getSecretsanta() {
        return secretsanta;
    }
    public void setSecretsanta(String secretsanta) {
        this.secretsanta = secretsanta;
    }
    @Override
    public String toString() {
        return "Member [name=" + name + ", wishlist=" + wishlist + ", secretsanta=" + secretsanta + "]";
    }

    
   
}
