package mobdev.agrikita.models.address;

public class Address {
    private long DbIndex;
    private String name;
    private String phone;
    private boolean isDefault;
    private String region;
    private String province;
    private String city;
    private String barangay;
    private String streetName;
    private String zipCode;
    private String label;


    public Address(String phone,String name, boolean isDefault, String region, String province, String city, String barangay, String streetName, String zipCode, String label) {
        this.isDefault = isDefault;
        this.phone = phone;
        this.name = name;
        this.region = region;
        this.province = province;
        this.city = city;
        this.barangay = barangay;
        this.label = label;
        this.zipCode = zipCode;
        this.streetName = streetName;
    }

    public void setDbIndex(long dbIndex) {
        this.DbIndex = dbIndex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public long getDbIndex() {
        return DbIndex;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public String getName() {
        return name;
    }

    public String getBarangay() {
        return barangay;
    }

    public String getCity() {
        return city;
    }

    public String getLabel() {
        return label;
    }

    public String getProvince() {
        return province;
    }

    public String getRegion() {
        return region;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getZipCode() {
        return zipCode;
    }
}
