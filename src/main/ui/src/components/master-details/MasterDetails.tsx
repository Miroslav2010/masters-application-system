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
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
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
import RestartAltIcon from '@mui/icons-material/RestartAlt';
import DeleteIcon from '@mui/icons-material/Delete';
import CircularProgress from '@mui/material/CircularProgress';
import masterService from "../../service/masterService";
import AttachmentPage from "../attachment/AttachmentPage";
import {CurrentStepDto} from "../../domain/CurrentStepDto";

const drawerWidth = 400;

interface Props {
    processId: string,
    steps: StepPreviewDto[],
    currentStep: CurrentStepDto | undefined,
    processState: string,
    getHistoryStep: Function,
    historyStep: StepHistoryDto | undefined,
    student: StudentDto | undefined,
    remarks: RemarkDTO[],
    validations: StepValidationDto[],
    newData: boolean
}

export default function MasterDetails(props: Props) {
    const [activeStep, setActiveStep] = useState(0);
    // console.log(props);
    const [currentStepActive, setCurrentStepActive] = useState(true);
    const [loading, setLoading] = useState(false);
    // console.log(loading);
    const navigate = useNavigate();

    const goBack = () => {
        navigate("/masters");
    }

    const translate = (name: string) => {
        return masterService.translate(name);
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
                        <Button variant="contained" color="error" endIcon={<RestartAltIcon />} sx={{width: '40%', marginLeft: '10px',
                            backgroundColor: 'darkred'}}>
                            Ресетирај
                        </Button>
                        <Button variant="contained" color="error" endIcon={<DeleteIcon />} sx={{width: '40%', marginLeft: '30px',
                            backgroundColor: 'darkred'}}
                        >
                            Избриши
                        </Button>
                    </div>
                    <Divider/>
                    <List>
                        <ListItem selected={0 === activeStep} button onClick={() => {
                            // props.getHistoryStep(step.id);
                            setActiveStep(0);
                            setCurrentStepActive(true);
                        }}>
                            <ListItemIcon>
                                <InboxIcon/>
                            </ListItemIcon>
                            <ListItemText primary={props.processState}/>
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
                                    {index % 2 === 0 ? <InboxIcon/> : <MailIcon/>}
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
                {currentStepActive ? (props.processState == "MasterTopic" ? <DocumentUpload processId={props.processId}/>
                    : (props.processState == "Validation" ? <ValidationPage /> :
                        (props.processState == "Attachment" ? <AttachmentPage processId={props.processId} label={props.currentStep?.processState} /> : "")))
                        // : (props.currentStep?.type == "Attachment" ? < /> : )))
                    : props.historyStep != undefined &&
                    <HistoryStep historyStep={props.historyStep} remarks={props.remarks} validations={props.validations} />}

            </Box>
    );
}