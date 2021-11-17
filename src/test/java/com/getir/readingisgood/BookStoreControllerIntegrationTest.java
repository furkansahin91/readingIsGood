package com.getir.readingisgood;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.controller.BookStoreController;
import com.getir.entity.Book;
import com.getir.entity.BookOrder;
import com.getir.entity.Customer;
import com.getir.entity.CustomerOrder;
import com.getir.model.LoginResponse;
import com.getir.model.PostCustomerRequest;
import com.getir.model.PostOrderRequest;
import com.getir.repository.BookRepository;
import com.getir.repository.CustomerRepository;
import com.getir.repository.OrderRepository;
import com.getir.service.BookService;
import com.getir.service.CustomerService;
import com.getir.service.OrderService;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.ws.rs.core.Application;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
@Import(SecurityTestConfig.class)
public class BookStoreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper objectMapper;
    private static String token;

    private BookStoreController bookStoreController;
    private CustomerService customerService;
    private OrderService orderService;
    private BookService bookService;

    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private BookRepository bookRepository;

    @Before
    public void init() {
        objectMapper = new ObjectMapper();
        customerService = new CustomerService(customerRepository);
        bookService = new BookService(bookRepository);
        orderService = new OrderService(customerService, bookService, orderRepository);
        bookStoreController = new BookStoreController(customerService, orderService);
        final LoginResponse loginResponse = (LoginResponse) bookStoreController.login("admin").getBody();
        token = loginResponse.getToken();
        mockMvc = MockMvcBuilders.standaloneSetup(new BookStoreController(customerService, orderService)).build();
    }

    @Test
    public void createCustomer_ShouldThrowBadRequest_WhenMissingParamSentInTheRequest() throws Exception {
        PostCustomerRequest request = buildPostCustomerRequest();
        request.setFirstName("");
        mockMvc.perform(post("/books/customers").header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)).characterEncoding("utf-8")).andExpect(status().isBadRequest());

    }

    @Test
    public void createCustomer_Should_Be_Successful_WhenTokenIsSent() throws Exception {
        PostCustomerRequest request = buildPostCustomerRequest();
        Customer customer = buildCustomerEntity(request);
        when(customerRepository.save(any())).thenReturn(customer);

        mockMvc.perform(post("/books/customers").header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)).characterEncoding("utf-8"))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"));
    }

    @Test
    public void createOrder_ShouldThrowBadRequest_WhenMissingParamSent() throws Exception {
        PostOrderRequest postOrderRequest = new PostOrderRequest();
        postOrderRequest.setBookIsbns(Arrays.asList("1111111111111"));
        mockMvc.perform(post("/books/orders").header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(postOrderRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createOrder_Should_Be_Successful_WhenTokenIsSent() throws Exception {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setEmail("furkansahin20@gmail.com");
        when(customerService.findCustomerByEmail(any())).thenReturn(customer);
        when(customerRepository.findCustomerByEmail(any())).thenReturn(customer);

        Book book = buildBookEntity();
        when(bookService.findBookByIsbn("1111111111111")).thenReturn(book);
        when(orderRepository.save(any())).thenReturn(buildCustomerOrder(customer));

        PostOrderRequest postOrderRequest = new PostOrderRequest();
        postOrderRequest.setCustomerEmail(customer.getEmail());
        postOrderRequest.setBookIsbns(Arrays.asList("1111111111111"));

        mockMvc.perform(post("/books/orders").header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON).content(asJsonString(postOrderRequest)))
                .andExpect(status().isCreated()).andExpect(header().exists("Location"));
    }

    private CustomerOrder buildCustomerOrder(Customer customer) {
        CustomerOrder customerOrder = new CustomerOrder();
        BookOrder bookOrder = new BookOrder();
        Book book = null;
        customerOrder.setCustomer(customer);
        customerOrder.setTotalAmount(5d);
        customerOrder.setId(1);
        customerOrder.setChangedBy("admin");
        book = buildBookEntity();
        bookOrder.setBook(book);
        bookOrder.setCustomerOrder(customerOrder);
        bookOrder.setId(1);
        customerOrder.setBookOrders(Arrays.asList(bookOrder));
        return customerOrder;
    }

    @NotNull
    private Book buildBookEntity() {
        Book book = new Book();
        book.setInStock(2);
        book.setId(1);
        book.setIsbn("1111111111111");
        book.setName("book_1");
        book.setPrice(4d);
        return book;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Customer buildCustomerEntity(PostCustomerRequest postCustomerRequest) {
        Customer customer = new Customer();
        customer.setEmail(postCustomerRequest.getEmail());
        customer.setUsername(postCustomerRequest.getUsername());
        customer.setAddress(postCustomerRequest.getAddress());
        customer.setPhoneNumber(postCustomerRequest.getPhoneNumber());
        customer.setLastName(postCustomerRequest.getLastName());
        customer.setFirstName(postCustomerRequest.getFirstName());
        customer.setId(1);
        return customer;
    }

    private PostCustomerRequest buildPostCustomerRequest() {
        PostCustomerRequest postCustomerRequest = new PostCustomerRequest();
        postCustomerRequest.setAddress("ist");
        postCustomerRequest.setEmail("furkansahin20@gmail.com");
        postCustomerRequest.setFirstName("furkan");
        postCustomerRequest.setUsername("furkan");
        postCustomerRequest.setLastName("sahin");
        postCustomerRequest.setPhoneNumber(5350290174l);
        return postCustomerRequest;
    }

    @Test
    public void getOrderDetails_Should_Be_Successful_WhenTokenIsSent() throws Exception {
        Optional<CustomerOrder> customerOrderOptional = Optional.of(buildCustomerOrder(null));
        when(orderRepository.findById(any())).thenReturn(customerOrderOptional);
        mockMvc.perform(get("/books/orders?id=1").header("Authorization", token))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getCustomerOrders_Should_Be_Successful_WhenTokenIsSent() throws Exception {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setEmail("furkansahin20@gmail.com");
        when(customerService.findCustomerByEmail("furkansahin20@gmail.com")).thenReturn(customer);
        when(customerRepository.findCustomerByEmail("furkansahin20@gmail.com")).thenReturn(customer);
        List<CustomerOrder> customerOrders = Arrays.asList(buildCustomerOrder(null));
        when(orderRepository.findOrdersByCustomer_Id(1)).thenReturn(customerOrders);

        mockMvc.perform(get("/books/orders/customer?customer_email=furkansahin20@gmail.com").header("Authorization", token))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
