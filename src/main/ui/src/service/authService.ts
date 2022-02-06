import instance from "../axios/axios";

const authService = {
    login: (username: string, password: string, onSuccess: () => void) => {
        instance.post("/person/login",{fullName:username,password:password},{withCredentials:true}).then((response)=>{
            console.log(response)
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
    }
}

export default authService;