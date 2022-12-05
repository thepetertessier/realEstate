package realEstate;
import realEstate.*;

// GUI
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.util.ArrayList;


public class Driver {
    final int WIDTH = 800;
    final int HEIGHT = 700;
    final int SIDE_BORDER = 50;
    final int TOP_BORDER = 10;

    final String[] SAMPLE_GEO_AREAS = {"North America", "Western Europe", "Eastern Europe", "South America", "Asia"};
    final String SAMPLE_GEO_AREA = SAMPLE_GEO_AREAS[0];
    final int BAD_INT_INPUT = -999; // What inputInt() will return if the user inputs a non-integer
    final Double BAD_DOUBLE_INPUT = -999.999; // What inputDouble() will return if the user inputs a non-Double

    // Initialize top-level GUI elements
    JFrame window = new JFrame("ISG Soft Real Estate Manager");
    JPanel propertyInfo = new JPanel();
    JLabel l_propertyInfo = new JLabel("Property Info:");

    // Initialize database
    Database database = new Database();

    // Helper functions
    private int inputInt(String message) {
        try {
            return Integer.parseInt(JOptionPane.showInputDialog(null, message));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Failed to create property: Please insert an integer", "Error", JOptionPane.ERROR_MESSAGE);
            return BAD_INT_INPUT;
        }
    }
    private Double inputDouble(String message) {
        try {
            return Double.parseDouble(JOptionPane.showInputDialog(null, message));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Failed to create property: Please insert a double", "Error", JOptionPane.ERROR_MESSAGE);
            return BAD_DOUBLE_INPUT;
        }
    }

    // Info JPanels for each property type
    final Rectangle infoJPanelRect = new Rectangle(20,50,250,330);
    final int ROW_PADDING = 2;
    final int LEFT_INFO_PADDING = 3;
    final int PADDING_BELOW_PROP_INFO = 65;
    SpringLayout infoLayout = new SpringLayout();
    Font infoFont = new Font("Serif", Font.PLAIN, 14);

    private JPanel propertyInfoPanel(Property prop) {
        JPanel panel = new JPanel();
        panel.setLayout(infoLayout);
        panel.setBackground(Color.lightGray);
        panel.setBounds(infoJPanelRect);

        JLabel l_name = new JLabel("Name: " + prop.name);
        JLabel l_location = new JLabel("Location: " + prop.location.formattedLocation());
        JLabel l_geoArea = new JLabel("Geographical Area: " + prop.location.geographicalArea);

        ArrayList<JLabel> labels = new ArrayList<JLabel>();
        labels.add(l_name);
        labels.add(l_location);
        labels.add(l_geoArea);

        if (prop instanceof Apartment) {
            labels.add(new JLabel("Area (m^2): " + ((Apartment) prop).area));
            labels.add(new JLabel("Bedrooms: " + ((Apartment) prop).bedrooms));
            labels.add(new JLabel("Bathrooms: " + ((Apartment) prop).bathrooms));
            labels.add(new JLabel("Floor number: " + ((Apartment) prop).floorNumber));
        } else if (prop instanceof VillageHouse) {
            labels.add(new JLabel("Number of floors: " + ((VillageHouse) prop).floors));
            labels.add(new JLabel("Number of rooms: " + ((VillageHouse) prop).rooms));
            labels.add(new JLabel("Patio material: " + ((VillageHouse) prop).patioMaterial));
            if (prop instanceof TownHouse) {
                labels.add(new JLabel("Community expenses: $" + ((TownHouse) prop).communityExpenses + "/m"));
            }
        } else if (prop instanceof Premise) {
            labels.add(new JLabel("Size: " + ((Premise) prop).size));
            if (prop instanceof CommercialPremise) {
                labels.add(new JLabel("Exterior doors: " + ((CommercialPremise) prop).exteriorDoors));
            } else if (prop instanceof IndustrialWarehouse) {
                labels.add(new JLabel("Doors: " + ((IndustrialWarehouse) prop).doors));
                labels.add(new JLabel("Rating: " + ((IndustrialWarehouse) prop).rating + "/5"));
            }
        } else {
            JOptionPane.showMessageDialog(null, "Whoops, we don't recognize the property for this info panel", "Property not recognized", JOptionPane.ERROR_MESSAGE);
        }

        infoLayout.putConstraint(SpringLayout.NORTH, l_name, ROW_PADDING, SpringLayout.NORTH, propertyInfo);

        for (JLabel l : labels) {
            l.setFont(infoFont);
            panel.add(l);
            infoLayout.putConstraint(SpringLayout.WEST, l, LEFT_INFO_PADDING, SpringLayout.WEST, propertyInfo);
        }
        for (int i = 1; i < labels.size(); i++) {
            infoLayout.putConstraint(SpringLayout.NORTH, labels.get(i), ROW_PADDING, SpringLayout.SOUTH, labels.get(i-1));
        }

        panel.validate();
        panel.repaint();

        return panel;
    }

    // Return Radio Button for a user-created property
    private JRadioButton propertyButton(String propertyType, String name, int buttonIndex) {
        Property property = database.properties.get(buttonIndex);
        JRadioButton button = new JRadioButton(property.getNameTag());
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                propertyInfo.removeAll();
                propertyInfo.add(l_propertyInfo);
                propertyInfo.add(propertyInfoPanel(property));
                propertyInfo.validate();
                propertyInfo.repaint();
            }
        });
        return button;
    }

    // Finally, the Driver
    public Driver() {
        // Populate Geographical areas set
        for (String geoArea : SAMPLE_GEO_AREAS) {
            database.addGeoArea(geoArea);
        }

        JLabel l1 = new JLabel("Welcome to the ISG Soft Real Estate Manager");
        final int l1_WIDTH = 500;
        l1.setBounds(SIDE_BORDER + WIDTH/2 - l1_WIDTH/2 - 20,10, l1_WIDTH,50);
        l1.setFont(new Font("Serif", Font.PLAIN, 24));
        
        final int b_geoArea_SPACING = SIDE_BORDER;
        final int b_geoArea_WIDTH = WIDTH/2 - b_geoArea_SPACING/2 - SIDE_BORDER;
        final int b_geoArea_HEIGHT = 20;

        JButton b_displayGeoAreas = new JButton("Display geographical areas (" + SAMPLE_GEO_AREAS.length + ")");
        b_displayGeoAreas.setBounds(SIDE_BORDER + b_geoArea_WIDTH + b_geoArea_SPACING,l1.getHeight() + TOP_BORDER,b_geoArea_WIDTH,b_geoArea_HEIGHT);
        b_displayGeoAreas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,database.getGeoAreas());
            }
        });

        JButton b_addGeoArea = new JButton("Add geographical area");
        b_addGeoArea.setBounds(SIDE_BORDER,l1.getHeight() + TOP_BORDER,b_geoArea_WIDTH,b_geoArea_HEIGHT);
        b_addGeoArea.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String geoArea = JOptionPane.showInputDialog("Geographical area:");
                if (geoArea == null) return;
                if (!database.addGeoArea(geoArea)) {
                    JOptionPane.showMessageDialog(null, "That geographical area already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
                b_displayGeoAreas.setText("Display geographical areas (" + database.geoAreas.size() +")");
            }
        });

        // Property Panel
        ButtonGroup propertyButtons = new ButtonGroup();

        JPanel propertyPanel = new JPanel();
        final int propertyPanel_WIDTH = WIDTH - SIDE_BORDER*2;
        final int propertyPanel_HEIGHT = HEIGHT - 200;
        propertyPanel.setBounds(SIDE_BORDER, l1.getHeight() + b_geoArea_HEIGHT + 30, propertyPanel_WIDTH, propertyPanel_HEIGHT);
        propertyPanel.setBackground(Color.lightGray);

        JLabel title = new JLabel("Choose the property you'd like to add:");
        final int title_WIDTH = 300;
        final int title_HEIGHT = 30;
        title.setBounds(SIDE_BORDER, TOP_BORDER, title_WIDTH, title_HEIGHT);
        title.setFont(new Font("Serif", Font.PLAIN, 18));
        propertyPanel.add(title);

        JComboBox<String> cb_propertyChoices = new JComboBox<String>();
        for (String property : Property.PURCHASEABLE_PROPERTIES) {
            cb_propertyChoices.addItem(property);
        }
        final int cb_pc_WIDTH = 150;
        final int IN_BORDER = 30;

        cb_propertyChoices.setBounds(SIDE_BORDER + title_WIDTH + 15, TOP_BORDER, cb_pc_WIDTH, title_HEIGHT);
        propertyPanel.add(cb_propertyChoices);

        // Property List subpanel
        JPanel propertyList = new JPanel();
        propertyList.setBounds(IN_BORDER, IN_BORDER + title_HEIGHT, propertyPanel_WIDTH/2 - IN_BORDER*2, propertyPanel_HEIGHT - IN_BORDER*2 - title_HEIGHT);
        propertyList.setBackground(Color.gray);
        propertyList.setLayout(null);
        propertyPanel.add(propertyList);

        JLabel l_properties = new JLabel("Properties:");
        l_properties.setFont(new Font("Arial Black", Font.PLAIN, 20));
        l_properties.setBounds(10,10,propertyList.getWidth()-20,30);
        l_properties.setHorizontalAlignment(JLabel.CENTER);
        propertyList.add(l_properties);

        JPanel propertyRadioPanel = new JPanel();
        propertyRadioPanel.setBounds(infoJPanelRect);
        propertyRadioPanel.setBackground(Color.lightGray);
        propertyRadioPanel.setLayout(null);
        propertyList.add(propertyRadioPanel);

        // Property Info subpanel
        propertyInfo.setBounds(IN_BORDER*3 + propertyList.getWidth(), IN_BORDER + title_HEIGHT, propertyPanel_WIDTH/2 - IN_BORDER*2, propertyPanel_HEIGHT - IN_BORDER*2 - title_HEIGHT);
        propertyInfo.setBackground(Color.gray);
        propertyInfo.setLayout(null);
        propertyPanel.add(propertyInfo);

        l_propertyInfo.setFont(new Font("Arial Black", Font.PLAIN, 20));
        l_propertyInfo.setBounds(10,10,propertyList.getWidth()-20,30);
        l_propertyInfo.setHorizontalAlignment(JLabel.CENTER);
        propertyInfo.add(l_propertyInfo);

        JButton b_addProperty = new JButton("Add property");
        b_addProperty.setBounds(SIDE_BORDER + title_WIDTH + cb_pc_WIDTH + 20, TOP_BORDER, 130, title_HEIGHT);
        propertyPanel.add(b_addProperty);
        b_addProperty.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String propertyType = (String) cb_propertyChoices.getSelectedItem();

                String name = JOptionPane.showInputDialog(null, "Name: ");
                if (name == null) return;
                String streetAddress = JOptionPane.showInputDialog("Street Address: ");
                if (streetAddress == null) return;
                String city = JOptionPane.showInputDialog(null, "City: ");
                if (city == null) return;
                String province = JOptionPane.showInputDialog(null, "Province/State: ");
                if (province == null) return;
                String geoArea = (String) JOptionPane.showInputDialog(null, "Geographical area: ", "Geo Area selection", JOptionPane.INFORMATION_MESSAGE, null, database.getGeoAreasArray(), SAMPLE_GEO_AREA);
                if (geoArea == null) return;

                Property property;
                if (propertyType.equals(Property.PURCHASEABLE_PROPERTIES[0])) { // Apartment
                    int area = inputInt("Area (m^2) (integer): ");
                    if (area == BAD_INT_INPUT) return;
                    int bedrooms = inputInt("Number of bedrooms: ");
                    if (bedrooms == BAD_INT_INPUT) return;
                    int bathrooms = inputInt("Number of bathrooms: ");
                    if (bathrooms == BAD_INT_INPUT) return;
                    int floorNumber = inputInt("Floor Number: ");  
                    if (floorNumber == BAD_INT_INPUT) return;

                    property = new Apartment(name, streetAddress, city, province, geoArea, area, bedrooms, bathrooms, floorNumber);
                } else if (propertyType.equals(Property.PURCHASEABLE_PROPERTIES[1]) || propertyType.equals(Property.PURCHASEABLE_PROPERTIES[4])) { // VillageHouse (1) or TownHouse (4)
                    int floors = inputInt("Number of floors: ");
                    if (floors == BAD_INT_INPUT) return;
                    int rooms = inputInt("Number of rooms: ");
                    if (rooms == BAD_INT_INPUT) return;
                    String patioMaterial = JOptionPane.showInputDialog("Patio material: ");
                    if (patioMaterial == null) return;
                    if (propertyType.equals(Property.PURCHASEABLE_PROPERTIES[4])) {
                        Double communityExpenses = inputDouble("Community expenses ($/m): ");
                        if (communityExpenses == BAD_DOUBLE_INPUT) return;
                        property = new TownHouse(name, streetAddress, city, province, geoArea, floors, rooms, patioMaterial, communityExpenses);
                    } else {
                        property = new VillageHouse(name, streetAddress, city, province, geoArea, floors, rooms, patioMaterial);
                    }
                } else if (propertyType.equals(Property.PURCHASEABLE_PROPERTIES[2]) || propertyType.equals(Property.PURCHASEABLE_PROPERTIES[3])) { // Commercial Premise (2) or Industrial Warehouse (3)
                    String[] accpetableSizes = {"unknown", "small", "medium", "large"}; // This is terrible coding but Premise.acceptableSizes didn't work
                    String size = (String) JOptionPane.showInputDialog(null, "Size: ", "Size selection", JOptionPane.INFORMATION_MESSAGE, null, accpetableSizes, "unknown");
                    if (size == null) return;
                    if (propertyType.equals(Property.PURCHASEABLE_PROPERTIES[2])) {
                        int exteriorDoors = inputInt("Exterior doors: ");
                        if (exteriorDoors == BAD_INT_INPUT) return;
                        property = new CommercialPremise(name, streetAddress, city, province, geoArea, size, exteriorDoors);
                    } else {
                        int doors = inputInt("Number of doors: ");
                        if (doors == BAD_INT_INPUT) return;
                        Double rating = inputDouble("Rating (out of 5): ");
                        if (rating == BAD_DOUBLE_INPUT) return;
                        property = new IndustrialWarehouse(name, streetAddress, city, province, geoArea, size, doors, rating);
                    }
                } else return;
                database.addProperty(property);

                int bCount = propertyButtons.getButtonCount();

                JRadioButton b_property = propertyButton(propertyType, name, bCount);
                propertyButtons.add(b_property);
                propertyRadioPanel.add(b_property);

                final int b_property_HEIGHT = 15;
                b_property.setBounds(0, bCount*b_property_HEIGHT, propertyRadioPanel.getWidth(),b_property_HEIGHT);
            }
        });

        final int b_purchase_WIDTH = 160;
        JButton b_purchase = new JButton("Purchase");
        b_purchase.setFont(new Font("Impact", Font.PLAIN, 18));
        b_purchase.setBounds(WIDTH/2 - b_purchase_WIDTH/2, propertyPanel_HEIGHT + 110, b_purchase_WIDTH, 40);
        b_purchase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Property> properties = database.properties;
                if (properties.size() == 0) {
                    JOptionPane.showMessageDialog(null, "No properties to purchase", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ArrayList<Object> boxArrayList = new ArrayList<Object>();

                JLabel l_purchase = new JLabel("Select the properties you'd like to purchase");
                l_purchase.setFont(new Font("Serif", Font.PLAIN, 18));
                boxArrayList.add(l_purchase);

                for (Property prop : properties) {
                    JCheckBox checkbox = new JCheckBox(prop.getNameTag());
                    boxArrayList.add(checkbox);
                }
                int result = JOptionPane.showConfirmDialog(null, boxArrayList.toArray(), "Purchase?", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.CANCEL_OPTION) {
                    return;
                }

                // Confirm the purchase with the confirmText in a message dialog
                String confirmText = "Congratulations on buying ";
                ArrayList<String> boughtProperties = new ArrayList<String>();
                for (int i = 1; i < boxArrayList.size(); i++) { // Skip first element, which is a label
                    JCheckBox box = (JCheckBox) boxArrayList.get(i);
                    if (box.isSelected()) {
                        boughtProperties.add(box.getText());
                    }
                }

                int size = boughtProperties.size();
                if (size == 0) {
                    return;
                } else {
                    confirmText += boughtProperties.get(0);
                }
                if (size >= 2) {
                    if (size > 2) {
                        confirmText += ", ";
                        for (int i = 1; i < size-1; i++) {
                            confirmText += boughtProperties.get(i) + ", ";
                        }
                    } else {
                        confirmText += " ";
                    }
                    confirmText += "and " + boughtProperties.get(size-1);
                }
                JOptionPane.showMessageDialog(null, confirmText + "!");
            }
        });

        // Initialize window
        propertyPanel.setLayout(null);

        window.add(l1);
        window.add(b_addGeoArea);
        window.add(b_displayGeoAreas);
        window.add(propertyPanel); // WIP: all components of property panel should be added but are not
        window.add(b_purchase);

        window.setSize(new Dimension(WIDTH,HEIGHT));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setVisible(true);
    }
    public static void main(String[] args) {
        new Driver();
    }
}

class Database {
    ArrayList<String> geoAreas; // all possible geographical areas
    // ArrayList<Visit> visits; // all visits
    // ArrayList<Purchase> purchases; // all purchases
    ArrayList<Property> properties; // all properties

    public Database() {
        geoAreas = new ArrayList<String>();
        // visits = new ArrayList<Visit>();
        // purchases = new ArrayList<Purchase>();
        properties = new ArrayList<Property>();
    }

    public boolean addGeoArea(String geoArea) {
        return geoAreas.add(geoArea);
    }
    // public boolean addVisit(Visit visit) {
    //     return visits.add(visit);
    // }
    // public boolean addPurchase(Purchase purchase) {
    //     return purchases.add(purchase);
    // }
    public boolean addProperty(Property property) {
        return properties.add(property);
    }

    public String getGeoAreas() {
        String result = "";
        for (String geoArea : geoAreas) {
            result += geoArea + "\n";
        }
        return result;
    }
    public String[] getGeoAreasArray() {
        String[] geoAreasArray = new String[geoAreas.size()];
        int i = 0;
        for (String geoArea : geoAreas) {
            geoAreasArray[i] = geoArea;
            i++;
        }
        return geoAreasArray;
    }
}

