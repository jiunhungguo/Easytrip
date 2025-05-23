package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.demo.model.Products;
import com.demo.service.IProductsService;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 */
@RestController
@RequestMapping("/api/products")
public class ProductsController {

	@Autowired
	private IProductsService iProductsService;

	/**
	 * 根據商品ID查詢商品。
	 * 
	 * @param id 商品的ID
	 * @return 產品的詳細資料
	 */
	@GetMapping("/{id}")
	public Products findProductById(@PathVariable("id") Integer id) {

		Products product = iProductsService.findProductsById(id);
		if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "商品不存在");
        }
		return product;
	}

	/**
	 * 查詢所有商品。
	 * 
	 * @return 所有商品的列表
	 */

	@GetMapping("/all")
	public List<Products> findAllProducts() {
		List<Products> products = iProductsService.findAllProducts();
		 if (products.isEmpty()) {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "沒有商品資料");
	        }
		return products;
	}

	// 根據商品名稱查詢商品

	@GetMapping("/name")
	public List<Products> findProductsByName(@RequestParam("name") String name) {
		List<Products> products = iProductsService.findProductsByName(name);
		 if (products.isEmpty()) {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "沒有符合名稱的商品");
	        }
		return products;
	}

	// 查詢低庫存商品
	@GetMapping("/lowstock")
	public List<Products> searchlowstock(@RequestParam("threshold") Integer threshold) {
		List<Products> products = iProductsService.findLowStockProducts(threshold);
		 if (products.isEmpty()) {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "庫存不足的商品未找到");
	        }
		return products;
	}

	// 根據里程範圍查詢商品

	@GetMapping("/needmiles")
	public List<Products> searchByNeedmiles(@RequestParam("min") Integer min,@RequestParam("max") Integer max) {
		List<Products> products = iProductsService.findProductsByNeedmiles(min, max);
		;
		 if (products.isEmpty()) {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "沒有符合的商品");
	        }
		return products;
	}

	/**
	 * 新增一個商品。
	 * @param products 新商品的詳細資料
	 * @return product 新增的商品資料
	 */
	@PostMapping
	public Products create(@RequestBody Products product) {

		return iProductsService.save(product);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Integer id) {
		iProductsService.deleteProductsById(id);
	}

	/**
	 * 更新指定ID商品的詳細資料。
	 * 
	 * @param productId      商品ID
	 * @param product 更新後的商品資料
	 * @return 更新後的商品資料
	 */
	@PutMapping("/{id}")
	public Products update(@PathVariable Integer id, @RequestBody Products product) {
		  Products existingProduct = iProductsService.findProductsById(id);
	        if (existingProduct == null) {
	            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "商品不存在");
	        }
		return iProductsService.updateProductsById(id, product);
	}
}
