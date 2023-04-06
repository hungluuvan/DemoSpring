<template>
   
       <!-- <div class="container"> -->
        <div v-if="currentUser.roles.includes(role)"> <button @click="showModal" type="button" class="btn btn-danger" >
            Add New Order
        </button></div>
       
    <!-- </div> -->

    <div    class="modal fade" id="addForm" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header border-bottom-0">
                    <h5 class="modal-title" id="exampleModalLabel">Create Product</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form ref="addForm"  @submit.prevent="handleAddProduct">
                    <div class="modal-body">
                        <div class="form-group">
                            <label for="name">Name</label>
                            <input v-model="name" type="text" class="form-control" id="quantity" placeholder="Name">
                            </div>
                        <div class="form-group">
                            <label for="price">Price</label>
                            <input v-model="price" type="text" class="form-control" id="price" placeholder="Price">
                        </div>
                        <div class="form-group">
                            <label for="description">Description</label>
                            <input v-model="description" type="text" class="form-control" id="description" placeholder="Description">
                        </div>
                        <div class="form-group">
                            <label for="madeIn">Made In</label>
                            <input v-model="madeIn" type="text" class="form-control" id="madeIn" placeholder="Made In">
                        </div>
                        <div class="form-group">
                          <label for="Image">Image</label>
                            <input v-on:change="handleFileUpload()" ref="imageProduct" type="file" class="form-control" id="imageProduct" >
                        </div>
                    </div>
                    <div class="modal-footer border-top-0 d-flex justify-content-center">
                        <button type="submit" class="btn btn-success">Submit</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <table id="respTable">
      <thead>
        <tr>
          
          <th>ID</th>
          <th>Name</th>
          <th>Price</th>
          <th>Description</th>
          <th>Made In</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in products" :key="item.id">
        
          <td>
            <span>{{ index }}</span>
          </td>
          <td>
            <span><strong>{{ item.name }}</strong></span>
          </td>
          <td>
            <span>{{ item.price }}</span>
          </td>
          <td>
            <span>{{ item.description }}</span>
          </td>
          <td>
            <span>{{ item.madeIn }}</span>
          </td>
          
          <td>
            <router-link :to="{ name: 'product-detail', params: { productId: item.id } }"><button>Detail</button>
            </router-link>
          </td>
        </tr>
      </tbody>
    </table>
  </template>
  <script>
  import ProductService from "../services/product.service";
import $ from 'jquery'
//   import EventBus from "../common/EventBus";
  export default {
    name: "Product",
    data() {
      return {
        isShow: false,
        products: [],
        form: [],
        imageProduct:'',
        
        name:"",
        price:"",
        description:"",
        madeIn:"",
        
        modalShown : false,
        role : "ROLE_ADMIN"
      };
    },
    computed: {
      currentUser() {
        return this.$store.state.auth.user;
      },
    },
    methods: {
        handleAddProduct(){
          let formData = new FormData();
          formData.append('imageProduct', this.imageProduct);
          formData.append('name',this.name)
          formData.append('price',this.price)
          formData.append('description',this.description)
          formData.append('madeIn',this.madeIn)
          console.log(formData.values)
          ProductService.addProduct(formData).then((response) => {
          if (response.status == 201) {
            this.$refs.addForm.reset(); 
            this.$router.go(this.$router.currentRoute)
            $('#addForm').modal('hide')
            // this.$toast.success(`Success`,{
            //   position: 'top-right'
            // });
          }
          
        }
         
            ),
        (error) => {
        this.content =
          (error.response &&
            error.response.data &&
            error.response.data.message) ||
          error.message ||
          error.toString();
        // if (error.response && error.response.status === 403) {
        //   EventBus.dispatch("logout");
        // }
      }
    
        },
        showModal() {
            $('#addForm').modal('show')
   },
        handleFileUpload(){
            this.imageProduct = this.$refs.imageProduct.files[0];
          }
    },
    created() {
      ProductService.getProducts().then(
        (response) => {
          this.products = response.data.data;
        },
        (error) => {
          this.content =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();
          }
      );
    },
  };
  </script>
  <style scoped>
  @import url("https://fonts.googleapis.com/css?family=Lato:400,400i,700");
  
  .button-option {
    display: block;
    ;
  }
  
  .delete-button {
    margin-top: 20px;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  
  body {
    font-family: Lato, Arial, sans-serif;
  }
  
  table {
    margin-top: 10px;
    width: 100%;
    border-collapse: collapse;
  }
  
  th {
    background-color: #333;
    color: #fff;
  }
  
  th,
  td {
    text-align: left;
    padding: 12px;
    border-bottom: 1px solid #ddd;
  }
  
  td::before {
    display: none;
  }
  
  @media screen and (max-width: 767px) {
  
    table,
    tr,
    td {
      display: block;
    }
  
    thead {
      display: none;
    }
  
    tbody {
      display: block;
      width: 100%;
    }
  
    tr {
      border-top: 2px solid #ddd;
      border-bottom: 1px solid #ddd;
      display: grid;
      grid-template-columns: max-content auto;
      margin-bottom: 20px;
    }
  
    td {
      display: contents;
    }
  
    td::before {
      display: inline-block;
      font-weight: bold;
      padding: 8px;
      border-bottom: 1px solid #ddd;
    }
  
    td span {
      padding: 8px;
      border-bottom: 1px solid #ddd;
    }
  
    td:last-child {
      border-bottom: 0;
    }
  
    #add {
      background-color: aqua;
      margin-bottom: 20px;
    }
  }</style>
  