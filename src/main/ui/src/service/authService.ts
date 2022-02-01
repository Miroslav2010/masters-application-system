import instance from "../axios/axios";

const authService = {
    login: (username: string, password: string, onSuccess: () => void) => {
        instance.post("/login", {
            username: username,
            password: password
        }).then((response) => {
            localStorage.setItem('user', response.data)
            onSuccess()
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