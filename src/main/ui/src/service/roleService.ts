import axios from '../axios/axios';

const roleService = {
    fetchRoles: ()=>{
        return axios.get("/person/roles");
    }
}

export default roleService;
