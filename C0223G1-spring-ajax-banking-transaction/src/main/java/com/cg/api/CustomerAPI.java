package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.dto.CustomerResDTO;
import com.cg.service.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping("/api/customers")
public class CustomerAPI {

    @Autowired
    private ICustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getAllCustomers() {

        List<Customer> customers = customerService.findAll();

        List<CustomerResDTO> customerResDTOS = new ArrayList<>();

        for (Customer item : customers) {
            CustomerResDTO customerResDTO = new CustomerResDTO();
            customerResDTO.setId(item.getId());
            customerResDTO.setFullName(item.getFullName());
            customerResDTO.setEmail(item.getEmail());
            customerResDTO.setPhone(item.getPhone());
            customerResDTO.setBalance(item.getBalance());
            customerResDTO.setAddress(item.getAddress());

            customerResDTOS.add(customerResDTO);
        }

        return new ResponseEntity<>(customerResDTOS, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getById(@PathVariable Long customerId) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            Map<String, String> data = new HashMap<>();
            data.put("message", "Khách hàng không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(customerOptional.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Customer customer) {

        customer.setId(null);
        customer.setBalance(BigDecimal.ZERO);
        Customer newCustomer = customerService.save(customer);

        return new ResponseEntity<>(newCustomer, HttpStatus.OK);
    }

//    @PatchMapping("/edit/{customerId}")
//    public ResponseEntity<?> update(@PathVariable String customerId, @RequestBody CustomerReqDTO customerReqDTO, BindingResult bindingResult) {
//        LocationRegionReqDTO locationRegionReqDTO = customerReqDTO.getLocationRegionReqDTO();
//
//        if (!validateUtils.isNumberValid(customerId)) {
//            throw new DataInputException("Mã khách hàng không hợp lệ");
//        }
//
//        new CustomerReqDTO().validate(customerReqDTO, bindingResult);
//
//        if (bindingResult.hasFieldErrors()) {
//            return appUtils.mapErrorToResponse(bindingResult);
//        }
//
//        Long id = Long.parseLong(customerId);
//
//        Optional<CustomerDTO> customerOptional = customerService.findCustomerDTOById(id);
//
//        if (!customerOptional.isPresent()) {
//            throw new DataInputException("Mã khách hàng không tồn tại");
//        }
//
//        Boolean existEmail = customerService.existsByEmailAndIdIsNotAndDeletedIsFalse(customerReqDTO.getEmail(), id);
//
//        if (existEmail) {
//            throw new EmailExistsException("Email đã tồn tại");
//        }
//
//        Boolean existsPhone = customerService.existsByPhoneAndIdIsNotAndDeletedIsFalse(customerReqDTO.getPhone(), id);
//
//        if (existsPhone) {
//            throw new EmailExistsException("Phone đã tồn tại");
//        }
//
//        CustomerDTO customerDTO = customerOptional.get();
//
//        CustomerResDTO customerResDTO = customerService.saveUpdatedCustomerFromDTO(customerReqDTO, customerDTO);
//
//        return new ResponseEntity<>(customerResDTO, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/delete/{customerId}")
//    public ResponseEntity<?> deleteCustomer(@PathVariable String customerId) {
//        if (!validateUtils.isNumberValid(customerId)) {
//            throw new DataInputException("Mã khách hàng không hợp lệ");
//        }
//        customerService.suspendCustomer(Long.parseLong(customerId));
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
