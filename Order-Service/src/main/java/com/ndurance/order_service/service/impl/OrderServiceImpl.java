package com.ndurance.order_service.service.impl;

import com.ndurance.order_service.entity.AddressEntity;
import com.ndurance.order_service.entity.OrderEntity;
import com.ndurance.order_service.entity.ProductEntity;
import com.ndurance.order_service.feign_client.ProductClient;
import com.ndurance.order_service.feign_client.UserClient;
import com.ndurance.order_service.feign_client.model.OrderRequestModelC;
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
import java.util.concurrent.atomic.AtomicLong;

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
    public void saveOrder(OrderRequestModel orderRequestModel, boolean changeAddress ,boolean addressSame, String token) {
        OrderEntity order = new OrderEntity();

        // Set basic order details
        order.setOrderId(utils.generateAddressId(20));
        order.setOrderDate(new Date());
        order.setUser(orderRequestModel.getUser());

        // Prepare products and calculate total price
        Set<String> productIds = orderRequestModel.getProducts().keySet();
        List<ProductEntity> productEntities = new ArrayList<>();
        AtomicLong totalPrice = new AtomicLong(0);

        productIds.forEach(productId -> {
        	System.out.println("Product ID: " + productId);
        	
            ProductDTO productRest = productClient.getProductById(productId);
            System.out.println("Product ID: " + productRest.getProductId() + ", Price: " + productRest.getPrice() + ", Quantity: " + 1);
            // Retrieve the quantity from the request

            ProductEntity product = new ProductEntity();
            product.setProductId(productRest.getProductId());
            product.setName(productRest.getName());
            product.setDescription(productRest.getDescription());
            product.setImages(productRest.getImages());
            product.setPrice(productRest.getPrice());
            product.setType(productRest.getType());
            product.setQuantity(1);

            // Save product entity and add to the order's product list
            ProductEntity savedProduct = productRepository.save(product);
            productEntities.add(savedProduct);

            // Accumulate total price
            totalPrice.addAndGet(savedProduct.getPrice() * savedProduct.getQuantity());
        });

        order.setTotalPrice(totalPrice.get());
        System.out.print(" ============================== -  ****************** " + totalPrice.get());
        order.setProducts(productEntities);

        // Fetch user details and handle address
        UserRest userRest = userClient.getCustomerById(orderRequestModel.getUser(), token);

        if (changeAddress && addressSame) {
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
        } else if (changeAddress){
            AddressesModel billingAddressModel = userRest.getAddresses().get(0);
            AddressesModel shippingAddressModel = userRest.getAddresses().get(1);

            AddressEntity billingAddress = new AddressEntity();
            billingAddress.setAddressId(utils.generateAddressId(20));
            billingAddress.setCity(billingAddressModel.getCity());
            billingAddress.setCountry(billingAddressModel.getCountry());
            billingAddress.setStreetName(billingAddressModel.getStreetName());
            billingAddress.setPostalCode(billingAddressModel.getPostalCode());
            billingAddress.setOrder(order);

            AddressEntity shippingAddress = new AddressEntity();
            shippingAddress.setAddressId(utils.generateAddressId(20));
            shippingAddress.setCity(shippingAddressModel.getCity());
            shippingAddress.setCountry(shippingAddressModel.getCountry());
            shippingAddress.setStreetName(shippingAddressModel.getStreetName());
            shippingAddress.setPostalCode(shippingAddressModel.getPostalCode());
            shippingAddress.setOrder(order);

            AddressEntity biilingAddressEntity = addressRepository.save(billingAddress);
            AddressEntity shipingAddressEntity = addressRepository.save(shippingAddress);

            order.setShippingAddress(biilingAddressEntity);
            order.setBillingAddress(shipingAddressEntity);

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

	@Override
    public void saveOrder(OrderRequestModelC orderRequestModel, String token, String user) {
        OrderEntity order = new OrderEntity();
        order.setOrderId(utils.generateAddressId(20));
        order.setOrderDate(new Date());
        order.setUser(user);
        order.setRefund(false);
        order.setDelivered(false);

        AtomicLong totalPrice = new AtomicLong(0);
        List<ProductEntity> productEntities = new ArrayList<>();

        OrderEntity orderEntity = orderRepository.save(order);

        UserRest userRest = userClient.getCustomerById(user, token);
        AddressEntity address = null;

        if (userRest.getAddresses() != null && !userRest.getAddresses().isEmpty()) {
            AddressesModel model = userRest.getAddresses().get(0);

            address = new AddressEntity();
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

        // Process cart items
        orderRequestModel.getCart().forEach(cart -> {
            ProductDTO productRest = productClient.getProductById(cart.getProductId());

            ProductEntity product = new ProductEntity();
            product.setProductId(productRest.getProductId());
            product.setName(productRest.getName());
            product.setDescription(productRest.getDescription());
            product.setImages(productRest.getImages());
            product.setPrice(productRest.getPrice());
            product.setType(productRest.getType());
            product.setQuantity(cart.getQuantity());
            product.setOrder(orderEntity);

            ProductEntity savedProduct = productRepository.save(product);
            productEntities.add(savedProduct);

            totalPrice.addAndGet((long) savedProduct.getPrice() * savedProduct.getQuantity());
        });

        // Update the order with products and total price
        order.setProducts(productEntities);
        orderRepository.save(order);
    }
}

