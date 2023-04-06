import api from "./api";

class ProductService {
  getProducts() {
    return api.get("/products");
  }
  getDetailProduct(productId) {
    return api.get("/products/" + productId );
  }
  addProduct(formData){
    return api.post("/products",formData,
    {
      headers: {
        "Content-Type": "multipart/form-data"
      }})
  }
  updateProduct(productId,formData) {
    return api.put("/products/" + productId ,formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }})
  }
  deleteProduct(productId) {
    return api.delete("/products/" + productId )
  }
  
}

export default new ProductService();
