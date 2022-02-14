import axios from '../axios/axios';
import {PersonDto} from '../domain/PersonDto'
import {StudentDto} from "../domain/StudentDto";

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
    },
    fetchStudents: (setData: (data:StudentDto[]) => void) => {
        axios.get("/person/students").then((data)=>{
            setData(data.data)
        })
    },

    getLoggedInUser: () => {
        let user = localStorage.getItem('user');
        console.log(user);
        return JSON.parse(user == null ? "" : user);
    }
}

export default personService;
