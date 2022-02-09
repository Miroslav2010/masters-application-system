import instance from "../axios/axios";
import {PersonDto} from "../domain/PersonDto";
import axios from "../axios/axios";
import {MajorDto} from "../domain/MajorDto";
import {CreateMasterDto} from "../domain/CreateMasterDto";
import {StepHistoryDto} from "../domain/StepHistoryDto";

const masterService = {
    getAllMajors: (setData: (data:MajorDto[]) => void) => {
        instance.get("/api/master/major").then((data)=>{
            setData(data.data)
        })
    },
    createMaster: (createMasterDto:CreateMasterDto) => {
        instance.post("/api/master",createMasterDto)
    },

    createMasterTopic: (processId: string, topic: string, description: string, application: File | null, mentorApproval: File | null,
                        biography: File | null, supplement: File | null) => {
        let formData = new FormData();
        formData.append("topic", topic);
        formData.append("description", description);
        formData.append("biography", biography!);
        formData.append("mentorApproval", mentorApproval!);
        formData.append("application", application!);
        formData.append("supplement", supplement!);
        return axios.post(`api/master/master-topic/${processId}`, formData, {
            headers: {'enctype': 'multipart/form-data' }
        })
            .then(result => result.data);
    },
    uploadDraft: (processId: string, application: File | null)=>{
        return instance.post("/api/master/draft",{
            processId: processId,
            draft: application
        },{withCredentials:true})
    }
    ,

    getAllMasters: () => {
        return axios.get(`/api/master/all`)
            .then(result => result.data);
    },

    getAllSteps: (processId: string) => {
        return axios.get(`/api/master/${processId}/all-steps`)
            .then(result => result.data);
    },

    getStudent: (processId: string) => {
        return axios.get(`/api/master/${processId}/student`)
            .then(result => result.data);
    },

    getHistoryStep: (stepId: string): Promise<StepHistoryDto> => {
        return axios.get(`/api/history/${stepId}`)
            .then(result => result.data);
    },

    getStepValidations: (stepId: string) => {
        return axios.get(`/api/history/validations/${stepId}`)
            .then(result => result.data);
    }
}

export default masterService;