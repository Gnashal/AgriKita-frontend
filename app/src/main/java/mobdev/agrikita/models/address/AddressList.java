package mobdev.agrikita.models.address;

import java.util.ArrayList;
import java.util.List;

public class AddressList {
    private List<Address> addresses;
    private static AddressList instance;

    public AddressList() {
        this.addresses = new ArrayList<>();
    }
    public static AddressList getInstance() {
        if (instance == null) {
            instance = new AddressList();
        }
        return instance;
    }

    public void addAddress(Address address) {
        addresses.add(address);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public Address getDefaultAddress() {
        for (Address address : addresses) {
            if (address.isDefault()) {
                return address;
            }
        }
        return null;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public boolean isEmpty() {
        return addresses.isEmpty();
    }

    public int size() {
        return addresses.size();
    }
}
