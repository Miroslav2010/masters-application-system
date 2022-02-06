import React from "react";
import masterService from "../../service/masterService";
import {useParams} from "react-router-dom";
import {StepPreviewDto} from "../../domain/stepPreviewDto";
import MasterDetails from "./MasterDetails";
import {StepHistoryDto} from "../../domain/StepHistoryDto";
import {StudentDto} from "../../domain/StudentDto";
import RemarkService from "../../service/remarkService";
import {RemarkDTO} from "../../domain/remarkResponse";
import {StepValidationDto} from "../../domain/StepValidationDto";

interface Props {
    params: string;
}

interface State {
    processId: string
    steps: StepPreviewDto[];
    currentStep: StepPreviewDto | undefined;
    stepHistory: StepHistoryDto | undefined;
    student: StudentDto | undefined,
    remarks: RemarkDTO[],
    validations: StepValidationDto[]
}

class MasterDetailsWrap extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
        console.log(props);
        this.state = {
            processId: this.props.params,
            steps: [],
            stepHistory: undefined,
            currentStep: undefined,
            student: undefined,
            remarks: [],
            validations: []
        }
    }


    componentDidMount() {
        console.log("did mount");
        masterService.getAllSteps(this.state.processId)
            .then(steps => {
                console.log(steps);
                if (steps.length == 0)
                    return;
                this.setState({
                    steps: steps.slice(0, -1),
                    currentStep: steps[steps.length - 1]
                })
            });
        masterService.getStudent(this.state.processId)
            .then(student => {
                console.log(student);
                this.setState({
                    student: student
                })
            });
    }

    render() {
        return (
            <MasterDetails processId={this.state.processId} steps={this.state.steps} getHistoryStep={this.getHistoryStep}
                           historyStep={this.state.stepHistory} student={this.state.student} remarks={this.state.remarks}
                           validations={this.state.validations} currentStep={this.state.currentStep}/>
        )
    }

    getHistoryStep = (stepId: string) => {
        masterService.getHistoryStep(stepId)
            .then(stepHistory =>
                this.setState({
                    stepHistory: stepHistory
                })
            );
        RemarkService.getRemarksForStep(stepId)
            .then(remarks =>
                this.setState({
                    remarks: remarks
                }));
        masterService.getStepValidations(stepId)
            .then(validations =>
                this.setState({
                    validations: validations
                }));
    }

}

// export default MasterDetailsWrap;
export default (props: any) => (
    <MasterDetailsWrap
        {...props}
        params={useParams()["id"]}
    />
);