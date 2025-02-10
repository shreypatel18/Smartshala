package SmartShala.SmartShala.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Notice {


    String showTo;
    String Description;

    public String getShowTo() {
        return showTo;
    }

    public void setShowTo(String showTo) {
        this.showTo = showTo;
    }

    public String getDescription() {
        return Description;
    }
    public void setDescription(String description) {
        Description = description;
    }

}
