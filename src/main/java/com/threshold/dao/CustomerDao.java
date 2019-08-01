package com.threshold.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "dao")
public class CustomerDao {
	@Autowired
	JdbcTemplate template;

	// Customer table DB schemas
	public void save(Map<String, Object> reqData) {
		String sql = "INSERT INTO threshold.customer (name,email,phone,gender,dob,address,city,state,country,pincode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		template.update(sql,
				new Object[] { reqData.get("name"), reqData.get("email"), reqData.get("phone"), reqData.get("gender"),
						reqData.get("dob"), reqData.get("address"), reqData.get("city"), reqData.get("state"),
						reqData.get("country"), reqData.get("pincode") });

	}

	public List<Map<String, Object>> getUser(int id) {
		String sql = "SELECT customer.id,customer.name,customer.email,customer.phone,customer.gender,customer.dob,customer.address,customer.city,customer.state,customer.country,customer.pincode,customer.created_id,customer.created_time,customer.modified_id,customer.modified_time FROM threshold.customer WHERE customer.id="
				+ id;
		return (List<Map<String, Object>>) template.queryForList(sql);

	}

	public List<Map<String, Object>> userList() {
		String sql = "SELECT customer.id,customer.name,customer.email,customer.phone,customer.gender,customer.dob,customer.address,customer.city,customer.state,customer.country,customer.pincode,customer.created_id,customer.created_time,customer.modified_id,customer.modified_time FROM threshold.customer";
		return (List<Map<String, Object>>) template.queryForList(sql);

	}

	public int deleteUser(int id) {
		String sql = "DELETE FROM threshold.customer WHERE customer.id=" + id;
		return template.update(sql);

	}

	// Product table DB schemas

	public void productsave(Map<String, Object> reqData) {
		String sql = "INSERT INTO threshold.product (name,description,photo,price,tax) VALUES (?, ?, ?, ?, ?)";
		template.update(sql, new Object[] { reqData.get("name"), reqData.get("description"), reqData.get("photo"),
				reqData.get("price"), reqData.get("tax") });

	}

	public List<Map<String, Object>> getProduct(int id) {
		String sql = "SELECT product.id,product.name,product.description,product.photo,product.price,product.tax,product.created_id,product.created_time,product.modified_id,product.modified_time FROM threshold.product WHERE product.id ="
				+ id;
		return (List<Map<String, Object>>) template.queryForList(sql);

	}

	public List<Map<String, Object>> productList() {
		String sql = "SELECT product.id,product.name,product.description,product.photo,product.price,product.tax,product.created_id,product.created_time,product.modified_id,product.modified_time FROM threshold.product";
		return (List<Map<String, Object>>) template.queryForList(sql);

	}

	public int deleteProduct(int id) {
		String sql = "DELETE FROM threshold.product WHERE product.id=" + id;
		return template.update(sql);

	}

	// Order related Schema

	/*
	 * public void placeOrder(Map<String, Object> reqData) {
	 * 
	 * 
	 * KeyHolder keyHolder = new GeneratedKeyHolder();
	 * 
	 * double grand_total = 0; double grand_tax = 0; double order_total_amount = 0;
	 * 
	 * final String customer_id = reqData.get("customer_id").toString(); String
	 * address = reqData.get("order_address").toString();
	 * 
	 * DateFormat df = new SimpleDateFormat("dd/MM/yy"); Date dateobj = new Date();
	 * final String date = df.format(dateobj); System.out.println(date);
	 * 
	 * final String sql1 =
	 * "INSERT INTO threshold.order (customer_id,date,amount,tax_amount,total_amount,address) VALUES (?, ?, ?, ?, ?, ?)"
	 * ;
	 * 
	 * System.out.println("customer_id = "+customer_id);
	 * 
	 * 
	 * 
	 * template.update( new PreparedStatementCreator() { public PreparedStatement
	 * createPreparedStatement(Connection connection) throws SQLException {
	 * PreparedStatement ps = connection.prepareStatement(sql1, new String[]
	 * {"id"}); ps.setString(1, customer_id); ps.setString(2, date); ps.setString(3,
	 * 0+""); ps.setString(4, 0+""); ps.setString(5, 0+""); ps.setString(6, 0+"");
	 * return ps; } }, keyHolder);
	 * 
	 * BigInteger order_id = (BigInteger) keyHolder.getKey();
	 * System.out.println("order_id = "+order_id);
	 * 
	 * 
	 * 
	 * ArrayList<HashMap<String, Object>> products = (ArrayList<HashMap<String,
	 * Object>>) reqData.get("product_id"); int quantity; int id; for (HashMap
	 * eachProd : products) { id = (Integer) eachProd.get("id");
	 * System.out.println("id = " + id); quantity = (Integer)
	 * eachProd.get("quantity"); System.out.println("quantity = " + quantity);
	 * Map<String, Object> productresult = (Map<String, Object>)
	 * template.queryForMap(
	 * "SELECT product.name,product.tax,product.price FROM threshold.product WHERE product.id ="
	 * + id); String product_name = productresult.get("name").toString();
	 * System.out.println("product_name = " + product_name); double tax =
	 * Double.parseDouble(productresult.get("tax").toString()); int unit_price =
	 * Integer.parseInt(productresult.get("price").toString());
	 * System.out.println("unit_price = " + unit_price); double tx_amount =
	 * unit_price * (tax * 0.01); System.out.println("tx_amount = " + tx_amount);
	 * double tax_amount = quantity * tx_amount; System.out.println("tax_amount = "
	 * + tax_amount); double total_amount = tax_amount + (unit_price * quantity); //
	 * with out tax total amount System.out.println("order_id = " + order_id); int
	 * product_id = id; System.out.println("order_id = " + order_id +
	 * "product_id = " + product_id + "product_name = " + product_name +
	 * "quantity = " + quantity + "tax = " + tax + "unit_price = " + unit_price +
	 * "tax_amount = " + tax_amount + "total_amount = " + total_amount); String sql
	 * =
	 * "INSERT INTO threshold.order_product (order_id,product_id,product_name,quantity,tax,unit_price,tax_amount,total_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
	 * ;
	 * 
	 * template.update(sql, new Object[] { order_id, product_id, product_name,
	 * quantity, tax, unit_price, tax_amount, total_amount });
	 * 
	 * grand_total = total_amount + grand_total; grand_tax = tax_amount + grand_tax;
	 * 
	 * 
	 * }
	 * 
	 * order_total_amount = grand_total + grand_tax;
	 * 
	 * // Order
	 * 
	 * 
	 * 
	 * String updatedsql1 =
	 * "UPDATE threshold.order SET amount=?,tax_amount=?,total_amount=?,address=? WHERE order.id =?"
	 * ;
	 * 
	 * template.update(updatedsql1, new Object[] {grand_total, grand_tax,
	 * order_total_amount,address,order_id});
	 * 
	 * 
	 * 
	 * }
	 */

	public BigInteger saveOrder(final String customer_id, final String date, String amount, String tax_amount,
			String total_amount, String tax_amount1, String total_amount1, String address) {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		final String sql1 = "INSERT INTO threshold.order (customer_id,date,amount,tax_amount,total_amount,address) VALUES (?, ?, ?, ?, ?, ?)";

		System.out.println("customer_id = " + customer_id);

		template.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql1, new String[] { "id" });
				ps.setString(1, customer_id);
				ps.setString(2, date);
				ps.setString(3, 0 + "");
				ps.setString(4, 0 + "");
				ps.setString(5, 0 + "");
				ps.setString(6, 0 + "");
				return ps;
			}
		}, keyHolder);

		BigInteger order_id = (BigInteger) keyHolder.getKey();
		System.out.println("order_id = " + order_id);
		return order_id;

	}

	public void saveOrder_Product(BigInteger order_id, int product_id, String product_name, int quantity, double tax,
			double unit_price, double tax_amount1, double total_amount1) {

		String sql = "INSERT INTO threshold.order_product (order_id,product_id,product_name,quantity,tax,unit_price,tax_amount,total_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		template.update(sql, new Object[] { order_id, product_id, product_name, quantity, tax, unit_price, tax_amount1,
				total_amount1 });

	}

	public void finalSaveOrder(double grand_total, double grand_tax, double order_total_amount, String address,
			BigInteger order_id) {

		String updatedsql1 = "UPDATE threshold.order SET amount=?,tax_amount=?,total_amount=?,address=? WHERE order.id =?";

		template.update(updatedsql1, new Object[] { grand_total, grand_tax, order_total_amount, address, order_id });

	}

	/*
	 * public List<Map<String, Object>> orderList(String order_id) {
	 * 
	 * String sql =
	 * "SELECT product_name,quantity,tax,unit_price,tax_amount,total_amount FROM threshold.order_product WHERE order_id ="
	 * +order_id; return (List<Map<String, Object>>) template.queryForList(sql); }
	 */

	public List<Map<String, Object>> orderList() {

		String sql = "SELECT order.id,order.customer_id,order.date,order.amount,order.tax_amount,order.total_amount,order.address,order.created_id,order.created_time,order.modified_id,order.modified_time FROM threshold.order";
		return (List<Map<String, Object>>) template.queryForList(sql);
	}

	public List<Map<String, Object>> orderDisplay(int id) {
		String sql = "SELECT order.id,order.customer_id,order.date,order.amount,order.tax_amount,order.total_amount,order.address,order.created_id,order.created_time,order.modified_id,order.modified_time FROM threshold.order WHERE order.id ="
				+ id;
		return (List<Map<String, Object>>) template.queryForList(sql);
	}

	@Transactional(readOnly=true)
	public Map<String, Object> search(String search, String orderCond,Integer start, Integer rows) {
		HashMap<String,Object> _res=new HashMap<String, Object>();
		String query="SELECT SQL_CALC_FOUND_ROWS id,customer_id,date,amount,tax_amount,total_amount,address,created_id,created_time,modified_id,modified_time FROM threshold.order WHERE 1=1 $searchCondition$ ORDER BY $orderCondition$ LIMIT "+start+","+rows;
		query=query.replace("$searchCondition$", search).replace("$orderCondition$", orderCond);
		
		
		
		System.err.println(query+"**********************");
		List<Map<String, Object>> data= (List<Map<String, Object>>) template.queryForList(query);
		_res.put("records", template.queryForList("SELECT FOUND_ROWS()"));
		_res.put("data", data);
		return _res;
	}
	
	
}
