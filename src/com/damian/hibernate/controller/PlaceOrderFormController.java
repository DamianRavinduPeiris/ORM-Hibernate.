package com.damian.hibernate.controller;

import animatefx.animation.Bounce;
import animatefx.animation.LightSpeedIn;
import animatefx.animation.Shake;
import com.damian.hibernate.convertor.Convertor;
import com.damian.hibernate.dto.Customer_DTO;
import com.damian.hibernate.dto.Item_DTO;
import com.damian.hibernate.dto.OrderDetail_DTO;
import com.damian.hibernate.dto.Order_DTO;
import com.damian.hibernate.entity.Item;
import com.damian.hibernate.entity.Orders;
import com.damian.hibernate.model.TableModel;
import com.damian.hibernate.service.impl.CustomerServiceimpl;
import com.damian.hibernate.service.impl.ItemServiceimpl;
import com.damian.hibernate.service.impl.OrderServiceimpl;
import com.damian.hibernate.service.util.ServiceFactory;
import com.damian.hibernate.service.util.ServiceTypes;
import com.damian.hibernate.util.NavigationTypes;
import com.damian.hibernate.util.Navigator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class PlaceOrderFormController implements Initializable {
    private static final ObservableList<TableModel> tableModels = FXCollections.observableArrayList();
    public Label itemPriceLabel;
    public JFXButton atcButton;
    public JFXComboBox<String> cidCmb;
    public JFXComboBox<String> itemCodeCmb;
    public TableView<TableModel> tableView;
    public TableColumn c1;
    public TableColumn c2;
    public TableColumn c3;
    public TableColumn c4;
    public TableColumn c5;
    public Label oidLabel;
    public JFXButton placeOrderButton;
    public Label netTotalLabel;
    public Label ItemNameLabel;
    public Label qtyOnHandLabel;
    public TextField qtyField;
    public Label nameLabel;
    public Label priceLabel;
    public JFXButton clear;
    public Label mainLabel;
    private Item_DTO itemDto;

    public static OrderDetail_DTO getOrderDetails() {
        for (int i = 0; i < tableModels.size(); i++) {
            return new OrderDetail_DTO(tableModels.get(i).getOID(), tableModels.get(i).getItemCode(), tableModels.get(i).getQty(), tableModels.get(i).getUnitPrice());
        }
        return null;

    }

    public static String getItemCode() {
        for (int i = 0; i < tableModels.size(); i++) {
            return tableModels.get(i).getItemCode();
        }
        return null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        c1.setCellValueFactory(new PropertyValueFactory<>("oID"));
        c2.setCellValueFactory(new PropertyValueFactory<>("description"));
        c3.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        c4.setCellValueFactory(new PropertyValueFactory<>("qty"));
        c5.setCellValueFactory(new PropertyValueFactory<>("itemCode"));

        CustomerServiceimpl cs = (CustomerServiceimpl) ServiceFactory.getService(ServiceTypes.CUSTOMER);
        List<Customer_DTO> customerDtoList = cs.getAllCustomers();

        ObservableList<String> customerIds = FXCollections.observableArrayList();
        for (Customer_DTO c : customerDtoList) {
            customerIds.add(c.getId());
        }
        cidCmb.setItems(customerIds);

        ItemServiceimpl service = (ItemServiceimpl) ServiceFactory.getService(ServiceTypes.ITEM);
        List<Item_DTO> itemDtoList = service.getAll();
        ObservableList<String> itemCodes = FXCollections.observableArrayList();

        for (Item_DTO id : itemDtoList) {
            System.out.println(id.getItemCode());
            itemCodes.add(id.getItemCode());
        }
        itemCodeCmb.setItems(itemCodes);


        Random random = new Random();
        int i = random.nextInt(1000);
        oidLabel.setText("OD" + i);

        new LightSpeedIn(mainLabel).play();
        new Shake(tableView).play();
        new Bounce(atcButton).play();
        new Bounce(placeOrderButton).play();
    }

    public void atcButtonOnAction(ActionEvent actionEvent) throws InterruptedException {
        String qty = qtyField.getText();
        TableModel tm = new TableModel(oidLabel.getText(), ItemNameLabel.getText(), Double.valueOf(priceLabel.getText()), Integer.valueOf(qtyField.getText()), itemCodeCmb.getValue());
        if (qtyField.getText().isEmpty() || qtyField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Fields cannot be empty!");
            alert.show();
        }
        if (qty.matches(".*[a-zA-Z]+.*")) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("Quantity cannot be a string!");
            alert1.show();

        } else {
            tableModels.add(tm);
            tableView.setItems(tableModels);
            new Shake(tableView).play();

        }
        double total = 0;

        for (int i = 0; i < tableModels.size(); i++) {
            total += tableModels.get(i).getUnitPrice() * tableModels.get(i).getQty();
        }
        netTotalLabel.setText(String.valueOf(total));
        new Shake(netTotalLabel).play();
    }

    public void cidCmbOnAction(ActionEvent actionEvent) {
        CustomerServiceimpl service = (CustomerServiceimpl) ServiceFactory.getService(ServiceTypes.CUSTOMER);
        Customer_DTO customer = service.getCustomer(cidCmb.getValue());
        nameLabel.setText(customer.getName());

    }

    public void itemCodeCmbOnAction(ActionEvent actionEvent) {
        ItemServiceimpl service = (ItemServiceimpl) ServiceFactory.getService(ServiceTypes.ITEM);
        Item_DTO item = service.search(itemCodeCmb.getValue());
        ItemNameLabel.setText(item.getDescription());
        priceLabel.setText(String.valueOf(item.getUnitPrice()));
        qtyOnHandLabel.setText(String.valueOf(item.getQtyOnHand()));
    }

    public void tableViewOnAction(MouseEvent mouseEvent) {
    }

    public void placeOrderButtonOnAction(ActionEvent actionEvent) {
        OrderServiceimpl os = (OrderServiceimpl) ServiceFactory.getService(ServiceTypes.ORDER);
        CustomerServiceimpl cs = (CustomerServiceimpl) ServiceFactory.getService(ServiceTypes.CUSTOMER);

        Customer_DTO customer = cs.getCustomer(cidCmb.getValue());
        ArrayList<Item> itemsList = new ArrayList<>();
        ArrayList<Orders> ordersList = new ArrayList<>();

        for (int i = 0; i < tableModels.size(); i++) {
            String itemCode = tableModels.get(i).getItemCode();
            ItemServiceimpl itemService = (ItemServiceimpl) ServiceFactory.getService(ServiceTypes.ITEM);
            itemDto = itemService.search(itemCode);
            itemsList.add(Convertor.convertItemDTOToEntity(itemDto));
        }
        Order_DTO order = new Order_DTO(oidLabel.getText(), Date.valueOf(LocalDate.now()), Convertor.convertCustomerDTOToEntity(customer), itemsList);
        ordersList.add(Convertor.convertOrderDTOToEntity(order));
        itemDto.setOrdersList(ordersList);

        boolean b = os.add(order);

        if (b) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Order Placed Successfully!");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Order placing Failed!");
            alert.show();
        }


    }

    public void backButtonOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) itemCodeCmb.getScene().getWindow();
        Navigator.navigate(NavigationTypes.HOME, stage);
    }

    public void clearButtonOnAction(ActionEvent actionEvent) throws IOException {
        Navigator.navigate(NavigationTypes.ORDERS, (Stage) clear.getScene().getWindow());
    }
}
