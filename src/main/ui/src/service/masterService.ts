import instance from "../axios/axios";
import {PersonDto} from "../domain/PersonDto";
import axios from "../axios/axios";
import {MajorDto} from "../domain/MajorDto";
import {CreateMasterDto} from "../domain/CreateMasterDto";

const masterService = {
    getAllMajors: (setData: (data:MajorDto[]) => void) => {
        instance.get("/api/master/major").then((data)=>{
            setData(data.data)
        })
    },
    createMaster: (createMasterDto:CreateMasterDto) => {
        instance.post("/api/master",createMasterDto)
    }
}

export default masterService;