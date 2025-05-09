package com.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.demo.model.Products;

public interface IProductsService {

	 /**
	 * 分頁查詢
	 * @param page
	 * @param size
	 * @return
	 */
	//Page<Products> getPagedProducts(int page, int size);
    /**
     * 根據商品ID查詢商品
     * @param id 產品ID
     * @return 查詢到的商品實體
     */
    Products findProductsById(Integer id);

    /**
     * 查詢所有商品
     * @return 所有商品的實體列表
     */
    List<Products> findAllProducts();

    /**
     * 根據商品名稱查詢商品
     * @param product_name 商品名稱
     * @return 以商品名稱查詢到的商品實體列表
     */
    List<Products> findProductsByName(String name);

    /**
     * 查詢低庫存商品
     * @param threshold 庫存閾值
     * @return 庫存量低於指定閾值的商品實體列表
     */
    List<Products> findLowStockProducts(Integer threshold);

    /**
     * 根據需要的里程查詢商品（前台）
     * @param product_needmiles 需要的里程數
     * @return 返回里程數在某範圍的商品
     */
    

    /**
     * 新增商品
     * @param product_name 商品名稱
     * @param needmiles 所需里程
     * @param quantity 商品數量
     * @param product_image 商品圖片
     * @return 新增後的商品實體
     */
    Products save(Products product);

    /**
     * 根據商品ID刪除商品
     * @param id 商品ID
     */
    void deleteProductsById(Integer id);

    /**
     * 根據商品ID更新商品
     * @param id 商品ID
     * @param updateProducts 更新的商品實體
     * @return 更新後的商品實體
     */
    Products updateProductsById(Integer id, Products updateProduct);
	
    /**
     * @param min
     * @param max
     * @return
     */
    List<Products> findProductsByNeedmiles(Integer min, Integer max);
}
