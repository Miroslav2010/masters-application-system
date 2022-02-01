import axios from '../axios/axios';
import {useParams} from "react-router-dom";
import {ValidationDTO} from "../domain/validationResponse";

const ValidationService = {
    getDownloadUrl: (processId: string | undefined) => {
        return axios.get(`/api/master/download-url/${processId}`);
    },

    getValidationDetails: (processId: string | undefined): Promise<ValidationDTO> => {
        return axios.get(`/api/master/${processId}/validation-details`)
            .then(result => result.data);
    },

    validate: (processId: string | undefined, validationStatus: string) => {
        return axios.post(`/api/master/validation-step`,
            {
                processId: processId,
                validationStatus: validationStatus
            });
    },

    login: () => {
        return axios.post(`/login`,
            {
                "username": "Student",
                "password": "123"
            }).then(res => console.log(res));
    }
}

export default ValidationService;
