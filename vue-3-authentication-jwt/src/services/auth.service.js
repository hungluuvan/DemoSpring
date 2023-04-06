import api from "./api"
import TokenService from "./token.service";
class AuthService {
  login(user) {
    return api
      .post('/auth/signin', {
        username: user.username,
        password: user.password
      })
      .then(response => {
        console.log(response.data)
        if (response.data.accessToken) {
          TokenService.setUser(response.data);
        }

        return response.data;
      });
  }

  logout() {
    TokenService.removeUser();
  }

  register(user) {
    return api.post("/auth/signup", {
      username: user.username,
      email: user.email,
      password: user.password
    });
  }
}

export default new AuthService();
