package com.ndurance.order_service.service.impl;

import com.ndurance.order_service.entity.AddressEntity;
import com.ndurance.order_service.entity.OrderEntity;
import com.ndurance.order_service.entity.ProductEntity;
import com.ndurance.order_service.feign_client.ProductClient;
import com.ndurance.order_service.feign_client.UserClient;
import com.ndurance.order_service.feign_client.model.ProductRest;
import com.ndurance.order_service.feign_client.model.UserRest;
import com.ndurance.order_service.repository.AddressRepository;
import com.ndurance.order_service.repository.OrderRepository;
import com.ndurance.order_service.repository.ProductRepository;
import com.ndurance.order_service.service.OrderService;
import com.ndurance.order_service.shared.AddressesModel;
import com.ndurance.order_service.shared.Utils;
import com.ndurance.order_service.shared.dto.ProductDTO;
import com.ndurance.order_service.shared.model.request.OrderRequestModel;
import com.ndurance.order_service.shared.model.response.OrderRest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private Utils utils;
    @Autowired
    private UserClient userClient;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public void saveOrder(OrderRequestModel orderRequestModel, boolean addressSame, String token) {
        OrderEntity order = new OrderEntity();

        order.setOrderId(utils.generateAddressId(20));
        order.setOrderDate(new Date());
        order.setUser(orderRequestModel.getUser());

        Set<String> products = orderRequestModel.getProducts().keySet();
        List<ProductEntity> productEntities = new ArrayList<>();


        AtomicReference<Long> totalPrice = new AtomicReference<>(0L);

        products.forEach(p->{
            ProductDTO productRest = productClient.getProductById(p, token);
            ProductEntity product = new ProductEntity();
            product.setProductId(productRest.getProductId());
            product.setName(productRest.getName());
            product.setName(productRest.getDescription());
            product.setImages(productRest.getImages());
            product.setPrice(productRest.getPrice());
            product.setType(productRest.getType());
            product.setQuantity(1);

            ProductEntity save = productRepository.save(product);
            productEntities.add(save);
            totalPrice.set(  ((long) ((long) save.getPrice() * save.getQuantity())) + totalPrice.get());

        });

        order.setTotalPrice(totalPrice.get());
        order.setProducts(productEntities);

        UserRest userRest = userClient.getCustomerById(orderRequestModel.getUser(), token);

        if (addressSame) {
            if (userRest.getAddresses() != null && !userRest.getAddresses().isEmpty()) {
                AddressesModel model = userRest.getAddresses().get(0);

                AddressEntity address = new AddressEntity();
                address.setAddressId(utils.generateAddressId(20));
                address.setCity(model.getCity());
                address.setCountry(model.getCountry());
                address.setStreetName(model.getStreetName());
                address.setPostalCode(model.getPostalCode());
                address.setOrder(order);

                addressRepository.save(address);
                order.setShippingAddress(address);
                order.setBillingAddress(address);
            } else {
                throw new RuntimeException("No address found for the user.");
            }
        } else {

            AddressesModel billing_addresses_req = orderRequestModel.getBillingAddresses();
            AddressEntity billing_address = new AddressEntity();

            billing_address.setAddressId(utils.generateUserId(10));
            billing_address.setCity(billing_addresses_req.getCity());
            billing_address.setCountry(billing_addresses_req.getCountry());
            billing_address.setStreetName(billing_address.getStreetName());
            billing_address.setPostalCode(billing_address.getPostalCode());
            billing_address.setOrder(order);

            addressRepository.save(billing_address);
            order.setBillingAddress(billing_address);

            AddressEntity shipping_address = new AddressEntity();
            AddressesModel shipping_address_req = orderRequestModel.getShippingAddress();

            shipping_address.setAddressId(utils.generateUserId(10));
            shipping_address.setCity(shipping_address_req.getCity());
            shipping_address.setCountry(shipping_address_req.getCountry());
            shipping_address.setStreetName(shipping_address_req.getStreetName());
            shipping_address.setPostalCode(shipping_address_req.getPostalCode());
            billing_address.setOrder(order);

            addressRepository.save(shipping_address);

            order.setShippingAddress(shipping_address);
        }


        order.setRefund(false);
        order.setDelivered(false);

        orderRepository.save(order);
    }

    @Override
    public OrderRest getOrder(String orderId) {
        return modelMapper.map(orderRepository.findByOrderId(orderId), OrderRest.class);
    }

    @Override
    public List<OrderRest> getOrders(String user) {
        List<OrderRest> orderRests = new ArrayList<>();
        orderRepository.findByUser(user).forEach(order->{
            orderRests.add(modelMapper.map(order, OrderRest.class));
        });
        return orderRests;
    }
}

