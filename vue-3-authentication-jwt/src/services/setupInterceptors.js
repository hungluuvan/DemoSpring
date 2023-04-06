import axiosInstance from "./api";
import TokenService from "./token.service";
const setup = (store) => {
  axiosInstance.interceptors.request.use(
    (config) => {
      const token = TokenService.getLocalAccessToken();
      if (token) {
        config.headers["Authorization"] = `Bearer ${token}`; // for Spring Boot back-end
        // config.headers["x-access-token"] = token; // for Node.js Express back-end
        // config.headers.common = { Authorization: `Bearer ${token}` };
      }
      return config;
    },
    (error) => {
      console.log("error", error);
      return Promise.reject(error);
    }
  );

  axiosInstance.interceptors.response.use(
    (res) => {
      return res;
    },
    async (err) => {
      const originalConfig = err.config;
      const status = err.status
      const errorData = err.data;
      if (originalConfig.url !== "/auth/login/" && err.response) {
        // Access Token was expired
        if (err.response.status === 401 && !originalConfig._retry) {
          originalConfig._retry = true;

          try {
            const rs = await axiosInstance.post("/auth/refreshtoken", {
              refresh: TokenService.getLocalRefreshToken(),
            });

            const { access } = rs.data;
            store.dispatch("auth/refreshToken", access);
            TokenService.updateLocalAccessToken(access);

            return axiosInstance(originalConfig);
          } catch (_error) {
            return Promise.reject(_error);
          }
        }
       
        return Promise.reject({...errorData, status });
      }
      // switch (status) {
      //   case errorContants.StatusCode.ValidationFailed:
      //   case errorContants.StatusCode.BadRequest: {
      //     return Promise.reject({ ...errorData, status });
      //   }
      //   case errorContants.StatusCode.Forbidden: {
      //     break;
      //   }
      //   case errorContants.StatusCode.InternalServerError: {
      //     break;
      //   }
      //   case errorContants.StatusCode.TooManyRequests: {
      //     break;
      //   }
      // }
      return Promise.reject({...err});
    }
  );
};

export default setup;
