package com.example.mobilebanking;

import com.example.mobilebanking.account.Account;
import com.example.mobilebanking.bank.Bank;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JSONPlaceholder {
    @GET("customers")
    Call<List<Customer>> getCustomers();
    @GET("customers/{id}")
    Call<List<Customer>> getCustomer(Long id);
    @POST("customers")
    Call<Customer> createCustomer(@Body Customer customer);
    @PUT("customers/{id}")
    Call<Customer> putCustomer(@Path("id") Long id, @Body Customer customer);
    @DELETE("customers/{id}")
    Call<Void> deleteCustomer(@Path("id") Long id);

    // bank accounts
    @GET("bankAccounts")
    Call<List<Bank>> getBanks(Long id);
    @GET("/customer/{id}/bankAccounts")
    Call<List<Bank>> getCustomerBanks(Long id);
    @POST("bankAccounts")
    Call<Bank> createBank(@Body Bank bank);
    @PUT("bankAccounts/{id}")
    Call<Bank> putBank(@Path("id") Long id, @Body Bank bank);
    @DELETE("bankAccounts/{id}")
    Call<Void> deleteBank(@Path("id") Long id);

    // account operations
    @GET("accountOperations/{id}")
    Call<List<Account>> getAccountOperations(Long id);
    @GET("/bankAccount/accountOperations/{id}")
    Call<List<Account>> getBankAccountOperations(Long id);
    @POST("accountOperations")
    Call<Account> createAccountOperation(@Body Account account);
    @PUT("accountOperations/{id}")
    Call<Account> putAccountOperation(@Path("id") Long id, @Body Account account);
    @DELETE("accountOperations/{id}")
    Call<Void> deleteAccountOperation(@Path("id") Long id);

}