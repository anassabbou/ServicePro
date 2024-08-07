package com.example.springbootprojet.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {
    private final CustomerRowMapper customerRowMapper;
    private final JdbcTemplate jdbcTemplate;

    public CustomerJDBCDataAccessService(
            CustomerRowMapper customerrowMapper,
            JdbcTemplate jdbcTemplate) {
        this.customerRowMapper = customerrowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql= """ 
                SELECT id, name, email, age, gender
                FROM customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        var sql= """
                SELECT id, name, email, age, gender
                FROM customer WHERE id = (?)
                """;
        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql= """
                INSERT INTO customer(name, email, age, gender) VALUES (?, ?, ?, ?);
                """;
        var result= jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getGender().name()
                );
        System.out.println("jdbcTemplate.update = " + result);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        var sql= """
                 SELECT count(id)
                 FROM customer
                 WHERE email = ?
                """;
        Integer count= jdbcTemplate.queryForObject(sql,Integer.class,email);
        return count != null && count > 0;
    }

    @Override
    public boolean existsPersonWithId(Integer customerId) {
        var sql= """
                 SELECT count(id)
                 FROM customer
                 WHERE id = ?
                """;
        Integer count= jdbcTemplate.queryForObject(sql,Integer.class,customerId);
        return count != null && count>0;

    }

    @Override
    public void deleteCustomerById(Integer id) {
        if(existsPersonWithId(id)){
        var sql= """
                DELETE FROM customer WHERE id = ?
                """;
         int result=jdbcTemplate.update(sql, id);
            System.out.println("deleteCustomerById = "+ result);
        }
    }

    @Override
    public void updateCustomer(Customer update) {
        if(update.getName() != null){
            var sql= """
                    UPDATE customer SET name= ? WHERE id= ?
                    """;
            int res= jdbcTemplate.update(sql, update.getName(), update.getId());
            System.out.println("updateCustomer -name =" + res);
        }
        if(update.getAge() != null){
            var sql= """
                    UPDATE customer SET age= ? WHERE id= ?
                    """;
            int res= jdbcTemplate.update(sql, update.getAge(), update.getId());
            System.out.println("updateCustomer -age =" + res);
        }
        if(update.getEmail() != null){
            var sql= """
                    UPDATE customer SET email= ? WHERE id= ?
                    """;
            int res= jdbcTemplate.update(sql, update.getEmail(), update.getId());
            System.out.println("updateCustomer -email =" + res);
        }

    }
}
