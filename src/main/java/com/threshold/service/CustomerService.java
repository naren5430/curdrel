package com.threshold.service;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.threshold.dao.CustomerDao;

@Service(value="service")
public class CustomerService {
	
	@Autowired
	CustomerDao dao;
	
	@Autowired
	JdbcTemplate template;
//Customer Services
	@Transactional
	public void save(Map<String, Object> reqData) {
		dao.save(reqData);
	}
	public List<Map<String, Object>> getUser(int id) {
		return dao.getUser(id);
	}
	
	public List<Map<String, Object>> userList() {
		return dao.userList();
	}
	public int deleteUser(int id) {
		return dao.deleteUser(id);
	}
	
	//Product Services
	
	@Transactional
	public void productsave(Map<String, Object> reqData) {
		dao.productsave(reqData);
	}
	public List<Map<String, Object>> getProduct(int id) {
		return dao.getProduct(id);
	}
	
	public List<Map<String, Object>> productList() {
		return dao.productList();
	}
	
	public int deleteProduct(int id) {
		return dao.deleteProduct(id);
	}
	
	
	
	/*public void placeOrder(Map<String, Object> reqData) {
		 dao.placeOrder(reqData);
	}*/
	
	
public void placeOrder(Map<String, Object> reqData) {
		
		
	@SuppressWarnings("unused")
	KeyHolder keyHolder = new GeneratedKeyHolder();
		
		 double grand_total = 0;
		 double grand_tax = 0;
		 double order_total_amount = 0;
		
	     final String customer_id = reqData.get("customer_id").toString();
		 String address = reqData.get("order_address").toString();

		DateFormat df = new SimpleDateFormat("dd/MM/yy");
		Date dateobj = new Date();
		final String date = df.format(dateobj);
		System.out.println(date);
		
		String amount = "0",tax_amount = "0",total_amount = "0";
		BigInteger order_id = dao.saveOrder(customer_id,date,amount,tax_amount,total_amount,tax_amount,total_amount,address);
		
		
		 /*BigInteger order_id = (BigInteger) keyHolder.getKey();
		 System.out.println("order_id = "+order_id);
		*/
	
		System.out.println("order_id = "+order_id);
		
		@SuppressWarnings("unchecked")
		ArrayList<HashMap<String, Object>> products = (ArrayList<HashMap<String, Object>>) reqData.get("product_id");
		int quantity;
		int id;
		for (@SuppressWarnings("rawtypes") HashMap eachProd : products) {
			id = (Integer) eachProd.get("id");
			System.out.println("id = " + id);
			quantity = (Integer) eachProd.get("quantity");
			System.out.println("quantity = " + quantity);
			Map<String, Object> productresult = (Map<String, Object>) template.queryForMap(
					"SELECT product.name,product.tax,product.price FROM threshold.product WHERE product.id =" + id);
			String product_name = productresult.get("name").toString();
			System.out.println("product_name = " + product_name);
			double tax = Double.parseDouble(productresult.get("tax").toString());
			int unit_price = Integer.parseInt(productresult.get("price").toString());
			System.out.println("unit_price = " + unit_price);
			double tx_amount = unit_price * (tax * 0.01);
			System.out.println("tx_amount = " + tx_amount);
			double tax_amount1 = quantity * tx_amount;
			System.out.println("tax_amount = " + tax_amount1);
			double total_amount1 = tax_amount1 + (unit_price * quantity); // with out tax total amount
			System.out.println("order_id = " + order_id);
			int product_id = id;
			System.out.println("order_id = " + order_id + "product_id = " + product_id + "product_name = "
					+ product_name + "quantity = " + quantity + "tax = " + tax + "unit_price = " + unit_price
					+ "tax_amount = " + tax_amount1 + "total_amount = " + total_amount1);
			
			dao.saveOrder_Product(order_id,product_id,product_name,quantity,tax,unit_price,tax_amount1,total_amount1);
			
			
			

			grand_total = total_amount1 + grand_total;
			grand_tax = tax_amount1 + grand_tax;
			

		}
		
		order_total_amount = grand_total + grand_tax;

		// Order

		dao.finalSaveOrder(grand_total, grand_tax, order_total_amount,address,order_id);
		
		
		
		

	}

//OrderList 

public List<Map<String, Object>> orderList() {
	
	return dao.orderList();
	
}
public List<Map<String, Object>> orderDisplay(int id) {
	
	return dao.orderDisplay(id);
}

@Transactional(readOnly=true)
public Map<String, Object> search(String search, String sortCondition,Integer start, Integer rows) {
	
	return dao.search(search,sortCondition,start,rows);
}




}
