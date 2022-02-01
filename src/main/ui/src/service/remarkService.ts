import axios from "../axios/axios";

const RemarkService = {
    getRemarks: (processId: string | undefined) => {
        return axios.get(`/api/remark/${processId}`)
            .then(result => result.data);
    },

    addRemark: (processId: string | undefined, remark: string) => {
        console.log(remark);
        return axios.post(`/api/remark/${processId}`, {
            remark: remark
        });
    },

    deleteRemark: (remarkId: string) => {
        return axios.delete(`/api/remark/${remarkId}`);
    }
}

export default RemarkService;