// test.textboxes.Subscription.java
package test.textboxes;

public class Subscription {
    public Long id;
    public String username;
    public String productName;
    public String deliveryType;
    public boolean active;
    public String address;
    public String contactInfo;

    public Subscription() {
    }

    public Subscription(String productName, String deliveryType, boolean active, String address, String contactInfo) {
        this.productName = productName;
        this.deliveryType = deliveryType;
        this.active = active;
        this.address = address;
        this.contactInfo = contactInfo;
    }
}
