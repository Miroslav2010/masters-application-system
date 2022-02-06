import axios from "../axios/axios";
import {blob} from "stream/consumers";

const DocumentService = {

    downloadFile: async(downloadUrl: string) => {
        return axios.post(`/api/document?fileLocation=${downloadUrl}`, {
            responseType: 'blob',
        }).then(response => blob(response.data));
    }
}

export default DocumentService;
