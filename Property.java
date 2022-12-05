package realEstate;

import java.util.Arrays;
import java.util.HashSet;

public class Property {
    final static String[] PURCHASEABLE_PROPERTIES = {"Apartment", "VillageHouse", "Commercial Premise", "Industrial Warehouse", "Townhouse"};

    String name;
    Location location;

    public Property(String name, String streetAddress, String city, String province, String geographicalArea) {
        this.name = name;
        location = new Location(streetAddress, city, province, geographicalArea);
    }
    public String getNameTag() {
        return "(Property) " + name;
    }
}

class Location {
    String streetAddress;
    String city;
    String province;
    String geographicalArea;

    public Location(String streetAddress, String city, String province, String geographicalArea) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.province = province;
        this.geographicalArea = geographicalArea;
    }
    public String formattedLocation() {
        return streetAddress + " " + city + ", " + province;
    }
}

class Apartment extends Property {
    int area;
    int bedrooms;
    int bathrooms;
    int floorNumber;

    public Apartment(String name, String streetAddress, String city, String province, String geographicalArea, int area, int bedrooms, int bathrooms, int floorNumber) {
        super(name, streetAddress, city, province, geographicalArea);
        this.area = area;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.floorNumber = floorNumber;
    }
    public String getNameTag() {
        return "(Apartment) " + name;
    }
}

class Garage extends Property { // NOT purchaseable
    String surfaceMaterial; // e.g., brick ???
    boolean isUnderground; // false means surface level

    public Garage(String name, String streetAddress, String city, String province, String geographicalArea, String surfaceMaterial, boolean isUnderground) {
        super(name, streetAddress, city, province, geographicalArea);
        this.surfaceMaterial = surfaceMaterial;
        this.isUnderground = isUnderground;
    }
    public String getNameTag() {
        return "(Garage) " + name;
    }
}

class VillageHouse extends Property {
    int floors;
    int rooms;
    String patioMaterial;
    HashSet<HouseFloor> houseFloors;

    public VillageHouse(String name, String streetAddress, String city, String province, String geographicalArea, int floors, int rooms, String patioMaterial) {
        super(name, streetAddress, city, province, geographicalArea);
        this.floors = floors;
        this.rooms = rooms;
        this.patioMaterial = patioMaterial;
        houseFloors = new HashSet<HouseFloor>();
    }

    public boolean addFloor(HouseFloor floor) {
        return houseFloors.add(floor);
    }
    public boolean containsFloor(HouseFloor floor) {
        return houseFloors.contains(floor);
    }
    public String getNameTag() {
        return "(VillageHouse) " + name;
    }
}

abstract class Premise extends Property {
    String size; // ??? e.g.?

    final static String[] acceptableSizes = {"small", "medium", "large", "unknown"};

    void setSize(String size) {
        // If it's not an acceptable size, default to unknown
        this.size = Arrays.asList(acceptableSizes).contains(size) ? size : "unknown";
    }

    public Premise(String name, String streetAddress, String city, String province, String geographicalArea, String size) {
        super(name, streetAddress, city, province, geographicalArea);
        setSize(size);
    }

}

class CommercialPremise extends Premise {
    int exteriorDoors;

    public CommercialPremise(String name, String streetAddress, String city, String province, String geographicalArea, String size, int exteriorDoors) {
        super(name, streetAddress, city, province, geographicalArea, size);
        this.exteriorDoors = exteriorDoors;
    }
    public String getNameTag() {
        return "(Commercial Premise) " + name;
    }
}

class IndustrialWarehouse extends Premise {
    int doors;
    double rating; // out of 5

    public IndustrialWarehouse(String name, String streetAddress, String city, String province, String geographicalArea, String size, int doors, double rating) {
        super(name, streetAddress, city, province, geographicalArea, size);
        this.doors = doors;
        this.rating = rating;
    }
    public String getNameTag() {
        return "(Industrial Warehouse) " + name;
    }
}

class TownHouse extends VillageHouse {
    double communityExpenses; // annually ???

    public TownHouse(String name, String streetAddress, String city, String province, String geographicalArea, int floors, int rooms, String patioMaterial, double communityExpenses) {
        super(name, streetAddress, city, province, geographicalArea, floors, rooms, patioMaterial);
        this.communityExpenses = communityExpenses;
    }
    public String getNameTag() {
        return "(Townhouse) " + name;
    }
}

class HouseFloor {
    int floorNumber;
    int floorArea;

    public boolean equals(Object o) {
        return ((o instanceof HouseFloor) && (((HouseFloor) o).floorNumber == floorNumber));
    }
    public int hashCode() {
        return floorNumber;
    }
}
