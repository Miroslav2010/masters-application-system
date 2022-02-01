import axios from '../axios/axios';
import {PersonDto} from '../domain/PersonDto'

const personService = {
    fetchRoles: (setData: (data: string[]) => void) => {
        axios.get("/person/roles").then((data) => {
            setData(data.data)
        });
    },
    fetchProfessors: (setData: (data:PersonDto[]) => void) => {
        axios.get("/person/professors").then((data)=>{
            setData(data.data)
        })
    }
}

export default personService;
