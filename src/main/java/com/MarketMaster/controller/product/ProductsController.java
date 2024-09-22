package com.MarketMaster.controller.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.MarketMaster.bean.product.ProductBean;
import com.MarketMaster.dao.product.ProductDao;

@Controller
public class ProductsController {

	@Autowired
	private ProductDao productDao;

	@GetMapping("/productHomepage")
	public String productHomePage() {
		return "product/productHomepage";
	}

	@GetMapping("/getOneProduct")
	public String GetOneProduct(@RequestParam String productId, Model m) {
		ProductBean product = productDao.getOne(productId);

		m.addAttribute("product", product);
		return "product/GetOneProduct";
	}

	@GetMapping("/getPagesProduct")
	public String GetPagesProduct(Model m) {
		List<ProductBean> products = productDao.getAll();
		m.addAttribute("products", products);
		return "product/GetPagesProducts";
	}

	@PostMapping("/insertProduct")
	public String InsertProduct(@ModelAttribute("product") ProductBean product, Model m) {

		ProductBean existproduct = productDao.getOne(product.getProductId());
		if (existproduct == null) {
			productDao.insertProduct(product);
			m.addAttribute("product", product);
			return "product/ShowInsertProduct";
		} else {
			m.addAttribute("errorMessage", "商品編號已存在，請檢查後重新輸入。");
			return "product/InsertProduct";
		}
	}

	@GetMapping("/insertProduct")
	public String showInsertProductForm(Model model) {
		return "product/InsertProduct";
	}

	@PostMapping("/getShelveProduct")
	public String getShelveProduct(@RequestParam String productId, Model m) {
		ProductBean product = productDao.getOne(productId);
		m.addAttribute("product", product);
		return "/product/GetShelveProduct";
	}

	@PostMapping("/shelveProduct")
	public String shelveProduct(@RequestParam String productId, @RequestParam Integer numberOfShelve, Model m) {
		productDao.shelveProduct(productId, numberOfShelve);

		List<ProductBean> products = productDao.getAll();
		m.addAttribute("products", products);
		return "product/GetPagesProducts";
	}
	
	@PostMapping("/getUpdateProduct")
	public String getUpdateProduct(@RequestParam String productId, Model m) {
		ProductBean product = productDao.getOne(productId);
		m.addAttribute("product", product);
		return "/product/GetUpdateProduct";
	}
	
	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute ProductBean product , Model m) {
		productDao.updateProduct(product);
		
		List<ProductBean> products = productDao.getAll();
		m.addAttribute("products", products);
		return "product/GetPagesProducts";
	}
	
	@PostMapping("/removeProduct")
	public String removeProduct(@RequestParam String productId , Model m) {
		productDao.removeProduct(productId);
		
		List<ProductBean> products = productDao.getAll();
		m.addAttribute("products", products);
		return "product/GetPagesProducts";
	}
	
}
