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
import {CurrentStepDto} from "../../domain/CurrentStepDto";

interface Props {
    params: string;
}

interface State {
    processId: string
    processState: string,
    steps: StepPreviewDto[];
    // currentStep: StepPreviewDto | undefined;
    stepHistory: StepHistoryDto | undefined;
    student: StudentDto | undefined,
    remarks: RemarkDTO[],
    validations: StepValidationDto[],
    currentStep: CurrentStepDto | undefined,
    loadingInfo: boolean,
    loadingValidation: boolean,
    loadingRemarks: boolean
}

class MasterDetailsWrap extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
        console.log(props);
        this.state = {
            processId: this.props.params,
            steps: [],
            stepHistory: undefined,
            // currentStep: undefined,
            student: undefined,
            currentStep: undefined,
            remarks: [],
            validations: [],
            loadingInfo: true,
            loadingValidation: true,
            loadingRemarks: true,
            processState: ""
        }
    }


    componentDidMount() {
        console.log("did mount");
        this.getData();
    }

    getData() {
        masterService.getAllSteps(this.state.processId)
            .then(steps => {
                console.log(steps);
                if (steps.length == 0)
                    return;
                this.setState({
                    steps: steps.stepPreviewItems,
                    processState: steps.processState,
                    loadingInfo: false,
                    loadingValidation: false,
                    loadingRemarks: false
                })
            });
        masterService.getStudent(this.state.processId)
            .then(student => {
                console.log(student);
                this.setState({
                    student: student
                })
            });
        masterService.getCurrentStepInfo(this.state.processId)
            .then(currentStep => {
                this.setState({
                    currentStep: currentStep
                })
            });
    }

    render() {
        return (
            <div>
                {/*{this.stillLoading() && <LinearProgress sx={{height: '6px'}}/> }*/}
                <MasterDetails processId={this.state.processId} steps={this.state.steps} getHistoryStep={this.getHistoryStep}
                               historyStep={this.state.stepHistory} student={this.state.student} remarks={this.state.remarks}
                               validations={this.state.validations} processState={this.state.processState} newData={this.stillLoading()}
                               currentStep={this.state.currentStep}/>
            </div>
        )
    }

    stillLoading() {
        return this.state.loadingInfo || this.state.loadingValidation || this.state.loadingRemarks;
    }

    getHistoryStep = (stepId: string) => {
        this.setState({
            loadingInfo: true,
            loadingValidation: true,
            loadingRemarks: true
        });
        masterService.getHistoryStep(stepId)
            .then(stepHistory =>
                this.setState({
                    stepHistory: stepHistory,
                    loadingInfo: false
                })
            );
        RemarkService.getRemarksForStep(stepId)
            .then(remarks =>
                this.setState({
                    remarks: remarks,
                    loadingRemarks: false
                }));
        masterService.getStepValidations(stepId)
            .then(validations =>
                this.setState({
                    validations: validations,
                    loadingValidation: false
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