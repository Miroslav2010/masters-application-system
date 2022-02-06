import * as React from 'react';
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
import {useState} from "react";
import ValidationPage from "../ValidationPage";
import MasterTopic from "../master/MasterTopic";
import DocumentUpload from "../document-upload/DocumentUploadPage";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import {Button} from "@mui/material";
import {useNavigate} from "react-router-dom";

const drawerWidth = 400;

interface Props {
    processId: string,
    steps: StepPreviewDto[],
    currentStep: StepPreviewDto | undefined,
    getHistoryStep: Function,
    historyStep: StepHistoryDto | undefined,
    student: StudentDto | undefined,
    remarks: RemarkDTO[],
    validations: StepValidationDto[]
}

export default function MasterDetails(props: Props) {
    const [activeStep, setActiveStep] = useState(0);
    const [currentStep, setCurrentStep] = useState(true);
    const navigate = useNavigate();

    const goBack = () => {
        navigate("/masters");
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
                    <Toolbar/>
                    <Button variant="contained" startIcon={<ArrowBackIcon />} sx={{width: '40%', marginLeft: '10px'}}
                            onClick={goBack}>
                        Назад
                    </Button>
                    <Divider/>
                    <List>
                        <ListItem selected={0 === activeStep} button onClick={() => {
                            // props.getHistoryStep(step.id);
                            setActiveStep(0);
                            setCurrentStep(true);
                        }}>
                            <ListItemIcon>
                                <InboxIcon/>
                            </ListItemIcon>
                            <ListItemText primary={props.currentStep?.name}/>
                        </ListItem>
                    </List>
                    <Divider/>
                    <List>
                        {props.steps.slice().reverse().map((step, index) => (
                            <ListItem selected={index + 1 === activeStep} button key={step.id} onClick={() => {
                                props.getHistoryStep(step.id);
                                setActiveStep(index + 1);
                                setCurrentStep(false);
                            }}>
                                <ListItemIcon>
                                    {index % 2 === 0 ? <InboxIcon/> : <MailIcon/>}
                                </ListItemIcon>
                                <ListItemText primary={step.name}/>
                            </ListItem>
                        ))}
                    </List>
                </Drawer>
                {/**/}
                {currentStep ? (props.currentStep?.type == "MasterTopic" ? <DocumentUpload processId={props.processId}/>
                    : (props.currentStep?.type == "Validation" ? <ValidationPage /> : ""))
                        // : (props.currentStep?.type == "Attachment" ? < /> : )))
                    : props.historyStep != undefined &&
                    <HistoryStep historyStep={props.historyStep} remarks={props.remarks} validations={props.validations} />}
            </Box>
    );
}