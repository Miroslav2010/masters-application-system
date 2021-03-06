import instance from "../axios/axios";
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
        return instance.post("/api/master",createMasterDto)
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
        return instance.post(`api/master/master-topic/${processId}`, formData, {
            headers: {'enctype': 'multipart/form-data' }
        })
            .then(result => result.data);
    },

    uploadDraft: (processId: string, draft: File | null) => {
        let formData = new FormData();
        formData.append("draft", draft!);
        return instance.post(`api/master/${processId}/draft`, formData, {
            headers: {'enctype': 'multipart/form-data' }
        }).then(result => result.data);
    },

    getAllMasters: (page: number, size: number, filter: string, orderBy: string, order: string) => {
        return instance.get(`/api/master/all?page=${page}&size=${size}&sort=${orderBy},${order}&filter=${filter}`)
            .then(result => result.data);
    },

    getAllSteps: (processId: string) => {
        return instance.get(`/api/master/${processId}/all-steps`)
            .then(result => result.data);
    },

    getMasterBasicInfo: (processId: string) => {
        return instance.get(`/api/master/${processId}/masterInfo`)
            .then(result => result.data);
    },

    getHistoryStep: (stepId: string): Promise<StepHistoryDto> => {
        return instance.get(`/api/history/${stepId}`)
            .then(result => result.data);
    },

    getStepValidations: (stepId: string) => {
        return instance.get(`/api/history/validations/${stepId}`)
            .then(result => result.data);
    },

    getCurrentStepInfo: (processId: string) => {
        return instance.get(`/api/master/${processId}/current-step`)
            .then(result => result.data);
    },

    cancelChangeLoop: (processId: string) => {
        return instance.post(`/api/master/${processId}/cancel-revision`)
            .then(result => result.data);
    },

    setArchiveNumber: (processId: string, archiveNumber: string) => {
        return instance.post(`/api/master/archive-number`, {
            processId: processId,
            archiveNumber: archiveNumber
        }).then(result => result.data);
    },

    translate: (name: string) => {
        let map = new Map<string, string> ([
            ["APPLICATION", "??????????????"],
            ["DOCUMENT_APPLICATION", "?????????????????????? ???? ??????????????????????????"],
            ["INITIAL_MENTOR_REVIEW", "???????????????? ???? ????????????"],
            ["STUDENT_SERVICE_REVIEW", "???????????????? ???? ???????????????????? ????????????"],
            ["INITIAL_NNK_REVIEW", "???????????????? ???? ??????"],
            ["INITIAL_SECRETARY_REVIEW", "???????????????? ????????????????"],
            ["STUDENT_DRAFT", "?????????????? ?????????????????? ??????????"],
            ["DRAFT_MENTOR_REVIEW", "???????????? ????????????????"],
            ["DRAFT_SECRETARY_REVIEW", "???????????????? ????????????????"],
            ["DRAFT_NNK_REVIEW", "?????? ????????????????"],
            ["SECOND_DRAFT_SECRETARY_REVIEW", "???????????????? ????????????????"],
            ["DRAFT_COMMITTEE_REVIEW", "???????????????? ???? ??????????"],
            ["STUDENT_CHANGES_DRAFT", "?????????????? ???????????? ?????????? ??????????????"],
            ["MENTOR_REPORT", "???????????? ?????????????? ???????????? ???? ???????????????? ?? ?????????????????? ????????????????"],
            ["REPORT_REVIEW", "???????????????????? ???? ????????????????"],
            ["REPORT_SECRETARY_REVIEW", "???????????????? ???????????????? ????????????????"],
            ["REPORT_STUDENT_SERVICE", "???????????????????? ???????????? ???????????????? ????????????????"],
            ["APPLICATION_FINISHED", "???????????????? ???? ???????????????????? ?? ??????????????. ???? ???????? ?????????????? ???? ??????????????????????????"],
            ["FINISHED", "?????????????????? ??????????????????????"]
        ]);
        return map.get(name);
    }

}

export default masterService;