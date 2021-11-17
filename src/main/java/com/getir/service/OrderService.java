package com.getir.service;

import com.getir.entity.Book;
import com.getir.entity.BookOrder;
import com.getir.entity.Customer;
import com.getir.entity.CustomerOrder;
import com.getir.exception.IllegalArgumentApplicationException;
import com.getir.exception.NotFoundException;
import com.getir.model.*;
import com.getir.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    final
    CustomerService customerService;
    final
    BookService bookService;
    final
    OrderRepository orderRepository;

    public OrderService(CustomerService customerService, BookService bookService, OrderRepository orderRepository) {
        this.customerService = customerService;
        this.bookService = bookService;
        this.orderRepository = orderRepository;
    }

    public long createNewOrder(PostOrderRequest request) {
        final String customerEmail = request.getCustomerEmail();
        Customer customer = findCustomerByEmail(customerEmail);

        List<Book> foundBooks = getFoundBooks(request);

        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCustomer(customer);
        customerOrder.setChangeTimestamp(LocalDateTime.now());
        double amount = 0;

        BookOrder bookOrder = null;
        List<BookOrder> bookOrders = new ArrayList<>();
        for (Book book : foundBooks) {
            bookOrder = new BookOrder();
            bookOrder.setCustomerOrder(customerOrder);
            bookOrder.setBook(book);
            bookOrders.add(bookOrder);

            book.setInStock(book.getInStock() - 1);
            bookService.bookRepository.save(book);

            amount += book.getPrice();
        }
        customerOrder.setBookOrders(bookOrders);
        customerOrder.setTotalAmount(amount);
        CustomerOrder savedCustomerOrder = orderRepository.save(customerOrder);
        return savedCustomerOrder.getId();
    }

    private List<Book> getFoundBooks(PostOrderRequest request) {
        List<Book> foundBooks = new ArrayList<>();
        for (String bookIsbn : request.getBookIsbns()) {
            Book book = bookService.findBookByIsbn(bookIsbn);
            if (book != null && book.getInStock() > 0) {
                foundBooks.add(book);
            } else {
                throw new IllegalArgumentApplicationException("the book with ISBN number " + bookIsbn + " was not found or is not in stock, please try again later!", bookIsbn, "book_isbn");
            }
        }
        return foundBooks;
    }

    public GetCustomerOrdersResponse getCustomerOrders(String customerEmail) {
        Customer customer = findCustomerByEmail(customerEmail);

        List<CustomerOrder> ordersByCustomerId = orderRepository.findOrdersByCustomer_Id(customer.getId());

        GetCustomerOrdersResponse response = new GetCustomerOrdersResponse();
        CustomerOrderResponse customerOrderResponse = null;
        List<CustomerOrderResponse> customerOrderResponses = new ArrayList<>();
        BookModel bookModel = null;
        List<BookModel> bookModels = null;
        for (CustomerOrder customerOrder : ordersByCustomerId) {
            bookModels = new ArrayList<>();
            customerOrderResponse = new CustomerOrderResponse();
            customerOrderResponse.setId(customerOrder.getId());
            customerOrderResponse.setTotalAmount(customerOrder.getTotalAmount());
            for (BookOrder bookOrder : customerOrder.getBookOrders()) {
                bookModel = new BookModel();
                bookModel.setIsbn(bookOrder.getBook().getIsbn());
                bookModel.setName(bookOrder.getBook().getName());
                bookModel.setPrice(bookOrder.getBook().getPrice());
                bookModels.add(bookModel);
            }
            customerOrderResponse.setBooks(bookModels);
            customerOrderResponses.add(customerOrderResponse);

        }
        response.setCustomerOrders(customerOrderResponses);
        return response;
    }

    private Customer findCustomerByEmail(String customerEmail) {
        Customer customer = customerService.findCustomerByEmail(customerEmail);
        if (customer == null) {
            throw new NotFoundException("customer not found with the given email address, please first register as a customer!", customerEmail, "customer_email");
        }
        return customer;
    }

    public GetOrderDetailsResponse getOrderDetails(Long id) {
        GetOrderDetailsResponse response = new GetOrderDetailsResponse();
        BookModel bookModel = null;
        List<BookModel> bookModels = new ArrayList<>();

        Optional<CustomerOrder> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            final CustomerOrder customerOrder = orderOptional.get();
            response.setId(customerOrder.getId());
            response.setTotalAmount(customerOrder.getTotalAmount());
            for (BookOrder bookOrder : customerOrder.getBookOrders()) {
                bookModel = new BookModel();
                bookModel.setIsbn(bookOrder.getBook().getIsbn());
                bookModel.setName(bookOrder.getBook().getName());
                bookModel.setPrice(bookOrder.getBook().getPrice());
                bookModels.add(bookModel);
            }
            response.setBooks(bookModels);

        } else {
            throw new NotFoundException("Order not found with the given id", String.valueOf(id), "order_id");
        }
        return response;
    }
}
