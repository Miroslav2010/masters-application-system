import axios from "../axios/axios";

const RemarkService = {
    getRemarks: (processId: string | undefined) => {
        return axios.get(`/api/remark/${processId}`)
            .then(result => result.data);
    },

    addRemark: (processId: string | undefined, remark: string, remarkId: string | undefined) => {
        console.log(remarkId);
        if (remarkId != undefined)
            return axios.post(`/api/remark/${processId}/edit`, {
                remarkId: remarkId,
                remark: remark
            });
        else
            return axios.post(`/api/remark/${processId}`, {
            remark: remark
            });
    },

    deleteRemark: (remarkId: string) => {
        return axios.delete(`/api/remark/${remarkId}`);
    },

    getRemarksForStep: (stepId: string | undefined) => {
        return axios.get(`/api/remark/step/${stepId}`)
            .then(result => result.data);
    },
}

export default RemarkService;