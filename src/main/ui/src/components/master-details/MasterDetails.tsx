import * as React from 'react';
import {useState} from 'react';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import CssBaseline from '@mui/material/CssBaseline';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import {StepPreviewDto} from "../../domain/stepPreviewDto";
import {StepHistoryDto} from "../../domain/StepHistoryDto";
import HistoryStep from "../history-step/HistoryStep";
import {StudentDto} from "../../domain/StudentDto";
import {RemarkDTO} from "../../domain/remarkResponse";
import {StepValidationDto} from "../../domain/StepValidationDto";
import ValidationPage from "../ValidationPage";
import MasterTopic from "../master/MasterTopic";
import DocumentUpload from "../document-upload/DocumentUploadPage";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {Button} from "@mui/material";
import {useNavigate} from "react-router-dom";
import CircularProgress from '@mui/material/CircularProgress';
import masterService from "../../service/masterService";
import AttachmentPage from "../attachment/AttachmentPage";
import {CurrentStepDto} from "../../domain/CurrentStepDto";
import personService from "../../service/personService";
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import RuleIcon from '@mui/icons-material/Rule';
import UploadFileIcon from '@mui/icons-material/UploadFile';
import HowToRegOutlinedIcon from '@mui/icons-material/HowToRegOutlined';
import {PersonDto} from "../../domain/PersonDto";
import NoteAddIcon from '@mui/icons-material/NoteAdd';
import ArchiveNumberDialog from "../../dialog/ArchiveNumberDialog";

const drawerWidth = 400;

interface Props {
    processId: string,
    steps: StepPreviewDto[],
    currentStep: CurrentStepDto | undefined,
    processState: string,
    getHistoryStep: Function,
    historyStep: StepHistoryDto | undefined,
    student: StudentDto | undefined,
    mentor: PersonDto | undefined,
    archiveNumber: string | null,
    remarks: RemarkDTO[],
    validations: StepValidationDto[],
    newData: boolean
}

export default function MasterDetails(props: Props) {
    const [activeStep, setActiveStep] = useState(0);
    // console.log(props);
    const [currentStepActive, setCurrentStepActive] = useState(true);
    const [loading, setLoading] = useState(false);
    const [openDialog, setOpenDialog] = useState(false);
    // console.log(loading);
    const navigate = useNavigate();
    const goBack = () => {
        navigate("/masters");
    }

    const translate = (name: string) => {
        return masterService.translate(name);
    }

    const isAssignedOnThisStep = () => {
        console.log(props.currentStep?.assignedPersons);
        console.log(props.currentStep?.assignedRole);
        let loggedInPerson = personService.getLoggedInUser();
        if (loggedInPerson == "")
            return false;

        if (props.currentStep?.assignedPersons.length == 0 && loggedInPerson["roles"][0] == props.currentStep?.assignedRole)
            return true;
        return props.currentStep?.assignedPersons.includes(loggedInPerson["fullName"]);
    }

    const showCancelLoopButton = () => {
        let loggedInUser = personService.getLoggedInUser();
        if (loggedInUser == "")
            return false;
        console.log(props.currentStep?.assignedRole);
        console.log(loggedInUser);
        return loggedInUser["roles"][0] == "PROFESSOR" &&
            loggedInUser["fullName"] == props.mentor?.fullName &&
            (props.currentStep?.processState == "STUDENT_CHANGES_DRAFT" || props.currentStep?.processState == "DRAFT_COMMITTEE_REVIEW");
    }

    const cancelChangeLoop = () => {
        masterService.cancelChangeLoop(props.processId)
            .then(_ => window.location.reload());
    }

    const showSetArchiveNumber = () => {
        if (props.student == undefined)
            return false;
        console.log(props.archiveNumber);
        let loggedInUser = personService.getLoggedInUser();
        if (loggedInUser == "")
            return false;
        console.log(props.currentStep?.assignedRole);
        console.log(loggedInUser);
        return loggedInUser["roles"][0] == "SECRETARY" && props.archiveNumber == null &&
            (props.currentStep?.processState != "DOCUMENT_APPLICATION" && props.currentStep?.processState != "INITIAL_MENTOR_REVIEW"
                && props.currentStep?.processState != "STUDENT_SERVICE_REVIEW" && props.currentStep?.processState != "INITIAL_NNK_REVIEW");
    }

    const setArchiveNumber = (archiveNumber: string | undefined) => {
        if(archiveNumber == undefined){
            setOpenDialog(false);
            return;
        }
        console.log(archiveNumber);
        masterService.setArchiveNumber(props.processId, archiveNumber)
            .then(_ => {
                setOpenDialog(false);
                window.location.reload();
            })
    }

    return (
            <Box sx={{display: 'flex', marginTop: '50px'}}>
                <CssBaseline/>
                <AppBar
                    position="fixed"
                    sx={{width: `calc(100% - ${drawerWidth}px)`, ml: `${drawerWidth}px`}}
                >
                    <Toolbar>
                        <Typography variant="h6" noWrap component="div">
                            {props.student?.fullName + ", " + props.student?.index}
                        </Typography>
                    </Toolbar>
                </AppBar>
                <Drawer
                    sx={{
                        marginTop: '50px',
                        width: drawerWidth,
                        flexShrink: 0,
                        '& .MuiDrawer-paper': {
                            width: drawerWidth,
                            boxSizing: 'border-box',
                        },
                    }}
                    variant="permanent"
                    anchor="left"
                >
                    <Button variant="contained" startIcon={<ArrowBackIcon />} sx={{width: '40%', marginLeft: '10px', marginTop: '10px'}}
                            onClick={goBack}>
                        Назад
                    </Button>
                    <div style={{marginTop: '20px', marginBottom: '50px'}}>
                        {/*<Button variant="contained" color="error" endIcon={<RestartAltIcon />} sx={{width: '40%', marginLeft: '10px',*/}
                        {/*    backgroundColor: 'darkred'}}>*/}
                        {/*    Ресетирај*/}
                        {/*</Button>*/}
                        {/*<Button variant="contained" color="error" endIcon={<DeleteIcon />} sx={{width: '40%', marginLeft: '30px',*/}
                        {/*    backgroundColor: 'darkred'}}*/}
                        {/*>*/}
                        {/*    Избриши*/}
                        {/*</Button>*/}
                        {showCancelLoopButton() &&
                        <Button variant="contained" color="error" endIcon={<HighlightOffIcon />} sx={{width: '70%', marginLeft: '10px',
                            backgroundColor: 'darkred'}} onClick={() => cancelChangeLoop()}
                        >
                            Прекини промени
                        </Button>}
                        {showSetArchiveNumber() &&
                        <Button variant="contained" color="secondary" endIcon={<NoteAddIcon />} sx={{width: '50%', marginLeft: '10px'}}
                                onClick={() => setOpenDialog(true)}
                        >
                            Aрхивски број
                        </Button>}
                    </div>
                    <Divider/>
                    <List>
                        <ListItem selected={0 === activeStep} button onClick={() => {
                            // props.getHistoryStep(step.id);
                            setActiveStep(0);
                            setCurrentStepActive(true);
                        }}>
                            <ListItemIcon>
                                {props.processState == "Validation" ? <RuleIcon/> :
                                    (props.processState == "Finished" ? <HowToRegOutlinedIcon /> : <UploadFileIcon/>)}
                            </ListItemIcon>
                            <ListItemText primary={translate(props.currentStep?.processState!)}/>
                        </ListItem>
                    </List>
                    <Divider/>
                    <List>
                        {props.steps.slice().reverse().map((step, index) => (
                            <ListItem selected={index + 1 === activeStep} button key={step.id} onClick={() => {
                                props.getHistoryStep(step.id);
                                setActiveStep(index + 1);
                                setCurrentStepActive(false);
                                setLoading(true);
                            }}>
                                <ListItemIcon>
                                    {step.type == "Validation" ? <RuleIcon/> : <UploadFileIcon/>}
                                </ListItemIcon>
                                <ListItemText primary={translate(step.name)}/>
                            </ListItem>
                        ))}
                    </List>
                </Drawer>
                {/**/}

                {props.newData &&
                <Box component="div" sx={{
                    display: 'block',
                    transform: 'scale(0.9)',
                    marginTop: '10px',
                    borderRadius: '5px'
                }}>
                    <div style={{minHeight: '85vh', minWidth: '75vw', marginTop: '200px', textAlign: 'center'}}>
                        <CircularProgress disableShrink size={70} />
                    </div>
                </Box>}
                {/*{props.processState != "Validation" && currentStep ? (props.processState == "MasterTopic" ? <DocumentUpload processId={props.processId}/>*/}
                {currentStepActive ? (isAssignedOnThisStep() && (props.processState == "MasterTopic" ? <DocumentUpload processId={props.processId}/>
                    : (props.processState == "Validation" ? <ValidationPage /> :
                        (props.processState == "Attachment" ? <AttachmentPage processId={props.processId} label={props.currentStep?.processState} /> : ""))))
                        // : (props.currentStep?.type == "Attachment" ? < /> : )))
                    : props.historyStep != undefined &&
                    <HistoryStep historyStep={props.historyStep} remarks={props.remarks} validations={props.validations} />}

                <ArchiveNumberDialog open={openDialog} onClose={setArchiveNumber} />
            </Box>
    );
}