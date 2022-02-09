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

    getAllMasters: () => {
        return instance.get(`/api/master/all`)
            .then(result => result.data);
    },

    getAllSteps: (processId: string) => {
        return instance.get(`/api/master/${processId}/all-steps`)
            .then(result => result.data);
    },

    getStudent: (processId: string) => {
        return instance.get(`/api/master/${processId}/student`)
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

    translate: (name: string) => {
        let map = new Map<string, string> ([
            ["APPLICATION", "Пријава"],
            ["DOCUMENT_APPLICATION", "Прикачување на документација"],
            ["INITIAL_MENTOR_REVIEW", "Проверка од ментор"],
            ["STUDENT_SERVICE_REVIEW", "Проверка од студентска служба"],
            ["INITIAL_NNK_REVIEW", "Проверка од ННК"],
            ["INITIAL_SECRETARY_REVIEW", "Секретар валидира"],
            ["STUDENT_DRAFT", "Студент прикачува драфт"],
            ["DRAFT_MENTOR_REVIEW", "Ментор валидира"],
            ["DRAFT_SECRETARY_REVIEW", "Секретар валидира"],
            ["DRAFT_NNK_REVIEW", "ННК валидира"],
            ["SECOND_DRAFT_SECRETARY_REVIEW", "Секретар валидира"],
            ["DRAFT_COMMITTEE_REVIEW", "Проверка на драфт"],
            ["STUDENT_CHANGES_DRAFT", "Студент менува драфт верзија"],
            ["MENTOR_REPORT", "Ментор затвора циклус на поправки и прикачува извештај"],
            ["REPORT_REVIEW", "Валидирање на извештај"],
            ["REPORT_SECRETARY_REVIEW", "Секретар валидира извештај"],
            ["REPORT_STUDENT_SERVICE", "Студентска служба валидира извештај"],
            ["APPLICATION_FINISHED", "Процесот на апликација е завршен. Се чека одбрана на магистерската"],
            ["FINISHED", "Одбранета магистерска"]
        ]);
        return map.get(name);
    }

}

export default masterService;