import {DocumentDto} from "./DocumentDto";

export interface ValidationDTO {
    stepName: string,
    studentName: string,
    downloadUrl: DocumentDto[]
}