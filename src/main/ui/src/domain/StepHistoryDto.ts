import {DocumentDto} from "./DocumentDto";

export interface StepHistoryDto{
    id: string,
    name: string,
    closed: Date | null,
    created: Date,
    orderNumber: number,
    description?: string | undefined,
    topic?: string | undefined,
    application: DocumentDto | undefined,
    mentorApproval: DocumentDto | undefined,
    biography: DocumentDto | undefined,
    supplement: DocumentDto | undefined,
    document: DocumentDto | undefined
}