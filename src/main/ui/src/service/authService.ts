import axios from '../axios/axios';

export const login = (username: string, password: string) => {
    axios.post("/login",{
        username: username,
        password: password
    }).then((data) => {
        return data
    });
}

export const register = (fullName: string,password: string,role: string,index: string) => {
    return axios.post("/")
}