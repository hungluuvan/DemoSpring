<template>
    <div class="col-2 back-button">
      <button @click="handleBack">Back</button>
    </div>
    <form>
      <div class="form-group row">
        <label for="name" class="col-sm-2 col-form-label">Name</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="falavour" v-model="product.name" placeholder="Name" />
        </div>
      </div>
      <div class="form-group row">
        <label for="price" class="col-sm-2 col-form-label">Price</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" v-model="product.price" id="price" placeholder="Price" />
        </div>
      </div>
      <div class="form-group row">
        <label for="description" class="col-sm-2 col-form-label">Description</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" v-model="product.description" id="description" placeholder="Description" />
        </div>
      </div>
      <div class="form-group row">
        <label for="madeIn" class="col-sm-2 col-form-label">Made In</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" v-model="product.madeIn" id="madeIn" placeholder="Made In" />
        </div>
      </div>
      <div class="form-group row">
        <label for="Img" class="col-sm-2 col-form-label">Image</label>
        <input v-on:change="handleFileUpload()" ref="imageProduct" type="file" class="form-control" id="imageProduct" >
        <div class="col-sm-10">
          <img :src="`data:image/png;base64,${product.image}`" />
        </div>
      </div>
      
      <div v-if="errorMessage">{{ errorMessage }}</div>
    </form>
    <div v-if="currentUser.roles.includes(role)" class="form-group row">
      <div class="col-6">
        <button @click.prevent="handleUpdate" type="submit" class="btn btn-primary">Update</button>
      </div>
      <div class="col-4">
        <button @click="handleDelete" type="button" class="btn btn-primary">Delete</button>
      </div>
    </div>
  </template>
  <script>
  import ProductService from "../services/product.service"
  export default {
    name: "DetailProduct",
    components: {
      // Form,
      // Field,
      // ErrorMessage,
    },
    data() {
      return {
        imageProduct :"",
        product: "",
        errorMessage: "",
        role : "ROLE_ADMIN",
        isAdmin : false,
      }
    },
    computed: {
      currentUser() {
        return this.$store.state.auth.user;
      },
  
    },
    created() {
      ProductService.getDetailProduct(this.$route.params.productId).then(
        (response) => {
          this.product = response.data.data;
        },
        (error) => {
          this.content =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();
          if (error.response && error.response.status === 403) {
            this.errorMessage="Something went wrong"
            // EventBus.dispatch("logout");
          }
        }
      );
    },
    methods: {
      handleFileUpload(){
            this.imageProduct = this.$refs.imageProduct.files[0];
          },
      handleUpdate() {
        let formData = new FormData()
        formData.append("imageProduct",this.imageProduct)
        formData.append("name",this.product.name)
        formData.append("price",this.product.price)
        formData.append("description",this.product.description)
        formData.append("madeIn",this.product.madeIn)
        ProductService.updateProduct(this.$route.params.productId, formData)
          .then((response) => {
            this.$router.go(this.$router.currentRoute)
           return response.data;
          }),
          (error) => {
            this.content =
              (error.response &&
                error.response.data &&
                error.response.data.message) ||
              error.message ||
              error.toString();
            if (error.response && error.response.status === 403) {
            //   EventBus.dispatch("logout");
            this.errorMessage="Something went wrong"
        }
          }
  
  
      },
      handleDelete() {
        ProductService.deleteProduct(this.$route.params.productId)
          .then((response) => {
            if (response.status == 204) {
                console.log("pass")
            //   this.$toast.success(`Deleted`, {
            //     position: 'top-right'
            //   });
              this.$router.push({ name: "products" })
  
            }
  
  
            return response.data;
          }),
          (error) => {
            this.content =
              (error.response &&
                error.response.data &&
                error.response.data.message) ||
              error.message ||
              error.toString();
            if (error.response && error.response.status === 403) {
                this.errorMessage="Something went wrong"
            //   EventBus.dispatch("logout");
            }
          }
  
  
      },
      handleBack() {
        this.$router.push({ name: "products" })
      }
    }
  }
  </script>
  <style>
  .back-button {
    margin: 0px 20px 20px 20px;
  }
  </style>