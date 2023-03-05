package com.damian.hibernate.service.impl;

import com.damian.hibernate.controller.PlaceOrderFormController;
import com.damian.hibernate.convertor.Convertor;
import com.damian.hibernate.dao.custom.OrderDetailDAO;
import com.damian.hibernate.dao.impl.OrderDAOimpl;
import com.damian.hibernate.dao.impl.OrderDetailDAOimpl;
import com.damian.hibernate.dao.util.DAOFactory;
import com.damian.hibernate.dao.util.DAOTypes;
import com.damian.hibernate.dto.Item_DTO;
import com.damian.hibernate.dto.OrderDetail_DTO;
import com.damian.hibernate.dto.Order_DTO;
import com.damian.hibernate.entity.OrderDetail;
import com.damian.hibernate.service.custom.OrderDetailServiceDAO;
import com.damian.hibernate.service.custom.OrderServiceDAO;
import com.damian.hibernate.service.util.ServiceFactory;
import com.damian.hibernate.service.util.ServiceTypes;
import com.damian.hibernate.service.util.SuperService;
import javafx.scene.control.Alert;

import java.util.List;

public class OrderServiceimpl implements OrderServiceDAO {
    private OrderDetail_DTO orderDetailDTO;


    @Override
    public boolean add(Order_DTO order) {
        OrderDAOimpl dao = (OrderDAOimpl) DAOFactory.getDAO(DAOTypes.ORDER);
        boolean b1 = dao.add(Convertor.convertOrderDTOToEntity(order));
        if(b1){
            OrderDetail_DTO orderDetails = PlaceOrderFormController.getOrderDetails();
            OrderDetailDAOimpl orderDetailDAOimpl = (OrderDetailDAOimpl) DAOFactory.getDAO(DAOTypes.ORDERDETAILS);
            boolean b2 = orderDetailDAOimpl.add(Convertor.convertOrderDetailDTOToEntity(orderDetails));
            if(b2){//Updating the Item.
                String itemCode = PlaceOrderFormController.getItemCode();
                ItemServiceimpl itemServiceimpl = (ItemServiceimpl) ServiceFactory.getService(ServiceTypes.ITEM);
                Item_DTO item = itemServiceimpl.search(itemCode);
                item.setQtyOnHand(item.getQtyOnHand() - orderDetails.getQty());
                boolean b3 = itemServiceimpl.update(item);
                if(b3){
                    return true;
                }else{
                    Alert a = new Alert(Alert.AlertType.ERROR, "Error while updating Items!");
                    a.show();
                }
            }else{
                Alert a = new Alert(Alert.AlertType.ERROR, "Error While Adding OrderDetails!");
                a.show();
            }

        }else{
            Alert a = new Alert(Alert.AlertType.ERROR, "Error While Adding Order!");
            a.show();
        }
        return false;
    }

    @Override
    public boolean update(Order_DTO orderDetail) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public Order_DTO search(String id) {
        return null;
    }

    @Override
    public List<Order_DTO> getAll() {
        return null;
    }

    public void setValues(OrderDetail_DTO orderDetailDTO){
        this.orderDetailDTO = orderDetailDTO;
    }
}
