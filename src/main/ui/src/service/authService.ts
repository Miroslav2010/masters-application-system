import instance from "../axios/axios";
import {User} from "../domain/User";

const authService = {
    login: (username: string, password: string, onSuccess: () => void) => {
        instance.post("/person/login",{fullName:username,password:password},{withCredentials:true}).then((response)=>{
            let user:User = response.data
            localStorage.setItem("user",JSON.stringify(user))
        })
    },
    register: (fullName: string, password: string, roles: string[], index: string, onSuccess: () => void) => {
        instance.post("/person", {
            fullName: fullName,
            password: password,
            roles: roles,
            index: index
        }).then
        (() => {
            onSuccess()
        })
    },
    logout:()=>{
        localStorage.clear()
        instance.post("/person/logout",{},{withCredentials:true}).then(()=>{})
    }
}

export default authService;