package com.threshold.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.threshold.dao.CustomerDao;
import com.threshold.service.CustomerService;

@RestController
public class Controller {

	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	CustomerDao dao;
	@Autowired
	CustomerService service;

	// Customer REST Services
	// User will be created register here
	@RequestMapping(value = "/userregistration", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, Object> reqData) {
		Map<String, Object> _res = new HashMap<String, Object>();
		service.save(reqData);
		_res.put("success", true);

		return new ResponseEntity<Map<String, Object>>(_res, HttpStatus.OK);
	}

	// User will able to view user data with there id
	@RequestMapping(value = "/userGet/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getUser(@PathVariable int id) {
		Map<String, Object> _res = new HashMap<String, Object>();

		List<Map<String, Object>> userData = service.getUser(id);
		_res.put("data", userData);
		_res.put("success", true);

		return new ResponseEntity<Map<String, Object>>(_res, HttpStatus.OK);
	}

	// Here you are able to view the user list
	@RequestMapping(value = "/userList", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> userList() {
		Map<String, Object> _res = new HashMap<String, Object>();

		List<Map<String, Object>> userData = service.userList();
		_res.put("data", userData);
		_res.put("success", true);

		return new ResponseEntity<Map<String, Object>>(_res, HttpStatus.OK);
	}

	// Here you can delete the user by using its id
	@RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable int id) {
		Map<String, Object> _res = new HashMap<String, Object>();

		int userData = service.deleteUser(id);
		_res.put("success", true);

		return new ResponseEntity<Map<String, Object>>(_res, HttpStatus.OK);
	}

	// Product REST Services
	@RequestMapping(value = "/productCreation", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> productCreation(@RequestBody Map<String, Object> reqData) {
		Map<String, Object> _res = new HashMap<String, Object>();
		service.productsave(reqData);
		_res.put("success", true);

		return new ResponseEntity<Map<String, Object>>(_res, HttpStatus.OK);
	}

	@RequestMapping(value = "/getProduct/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getProduct(@PathVariable int id) {
		Map<String, Object> _res = new HashMap<String, Object>();

		List<Map<String, Object>> productData = service.getProduct(id);
		_res.put("data", productData);
		_res.put("success", true);

		return new ResponseEntity<Map<String, Object>>(_res, HttpStatus.OK);
	}

	@RequestMapping(value = "/productList", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> productList() {
		Map<String, Object> _res = new HashMap<String, Object>();

		List<Map<String, Object>> productData = service.productList();
		_res.put("data", productData);
		_res.put("success", true);

		return new ResponseEntity<Map<String, Object>>(_res, HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteProduct/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable int id) {
		Map<String, Object> _res = new HashMap<String, Object>();

		int ProductData = service.deleteProduct(id);
		_res.put("success", true);

		return new ResponseEntity<Map<String, Object>>(_res, HttpStatus.OK);
	}

	// Order related API's

	/*
	 * { "customer_id":"1", "order_address":"Ameerpet", "product_id": [ { "id": 1,
	 * "quantity": 2 }, { "id": 2, "quantity": 3 }, { "id": 4, "quantity": 5 } ] }
	 */

	@RequestMapping(value = "/placeOrder", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody Map<String, Object> reqData) {
		Map<String, Object> _res = new HashMap<String, Object>();
		service.placeOrder(reqData);
		_res.put("success", true);

		return new ResponseEntity<Map<String, Object>>(_res, HttpStatus.OK);
	}

	// Will Display order List
	@RequestMapping(value = "/OrderList", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> orderList() {
		Map<String, Object> _res = new HashMap<String, Object>();
		List<Map<String, Object>> orderList = service.orderList();
		_res.put("OrderList", orderList);
		_res.put("success", true);

		return new ResponseEntity<Map<String, Object>>(_res, HttpStatus.OK);
	}

	// Will Display Order of particular order id given
	@RequestMapping(value = "/orderDisplay/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> orderDisplay(@PathVariable int id) {
		Map<String, Object> _res = new HashMap<String, Object>();
		List<Map<String, Object>> orderDisplay = service.orderDisplay(id);
		_res.put("OrderList", orderDisplay);
		_res.put("success", true);

		return new ResponseEntity<Map<String, Object>>(_res, HttpStatus.OK);
	}

	// Searching
	// Sorting Functionality headerName= id,name,designation,... type = ASC or DESC
	// in Rest API
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> search(@RequestBody Map<String, Object> reqData) {
		Map<String, Object> res = new HashMap<String, Object>();

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if (reqData.get("filters") != null)
			list = (List<Map<String, Object>>) reqData.get("filters");

		String search = "";
		int i = 0;
		for (Map<String, Object> entry : list) {
			// for field handling
			/*
			 * if((String) entry.get("value") == null) {
			 * 
			 * System.out.println(entry.get("field").toString()+
			 * "value should not be null or empty");
			 * 
			 * }
			 */
			String percentail = (String) entry.get("value");
			if (percentail.contains("%")) {
				percentail = percentail.replace("%", "\\%");

				if (i == list.size() - 1) {
					search += " AND " + entry.get("field").toString() + " LIKE " + "'%" + percentail + "%'";

				} else {
					search += " AND " + entry.get("field").toString() + " LIKE " + "'%" + percentail + "%'";
					i = i + 1;
				}

			} else {

				if (i == list.size() - 1) {
					search += " AND " + entry.get("field").toString() + " LIKE " + "'%" + entry.get("value").toString()
							+ "%'";

				} else {
					search += " AND " + entry.get("field").toString() + " LIKE " + "'%" + entry.get("value").toString()
							+ "%'";
					i = i + 1;
				}

			}

		}

		List<Map<String, Object>> sortList = new ArrayList<Map<String, Object>>();
		if (reqData.get("sorting") != null)
			sortList = (List<Map<String, Object>>) reqData.get("sorting");
		String sortCondition = "";
		for (Map<String, Object> entry : sortList) {
			sortCondition += entry.get("sidx").toString() + " " + entry.get("sord").toString() + " ,";
		}
		if (!(sortCondition.length() == 0 || sortCondition.equals("")))
			sortCondition = sortCondition.substring(0, sortCondition.length() - 1);
		Integer start = Integer.parseInt(reqData.get("start") + "");
		Integer page = Integer.parseInt(reqData.get("page") + "");
		Integer rows = Integer.parseInt(reqData.get("rows") + "");
		if (start == 0) {
			start = 1;
		}
		start = (page - 1) * rows;

		Map<String, Object> _res = new HashMap<String, Object>();
		Map<String, Object> searchDisplay = service.search(search, sortCondition, start, rows);
		_res.put("SearchList", searchDisplay);
		_res.put("success", true);

		return new ResponseEntity<Map<String, Object>>(_res, HttpStatus.OK);
	}

	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody String uploadFileHandler(@RequestParam("file") MultipartFile file) {
		String name = file.getOriginalFilename();
		if (!file.isEmpty()) {
			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file
				String rootPath = System.getProperty("catalina.home");
				File dir = new File(rootPath + File.separator + "tmpFiles");
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				
				File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				logger.info("Server File Location=" + serverFile.getAbsolutePath());

				return "You successfully uploaded file=" + name;
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + name + " because the file was empty.";
		}
	}
}
