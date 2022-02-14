import {Box, Card, CardContent, Typography} from "@mui/material";
import styles from "../ValidationPage.style.module.css";
import IconButton from "@mui/material/IconButton";
import DownloadForOfflineRoundedIcon from "@mui/icons-material/DownloadForOfflineRounded";
import React from "react";
import {StepHistoryDto} from "../../domain/StepHistoryDto";
import {RemarkDTO} from "../../domain/remarkResponse";
import RemarkList from "../RemarkList";
import {StepValidationDto} from "../../domain/StepValidationDto";
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import DangerousIcon from '@mui/icons-material/Dangerous';
import PauseCircleFilledIcon from '@mui/icons-material/PauseCircleFilled';
import masterService from "../../service/masterService";
import FileOpenIcon from '@mui/icons-material/FileOpen';

interface Props {
    historyStep: StepHistoryDto,
    remarks: RemarkDTO[],
    validations: StepValidationDto[]
}

const HistoryStep = (props: Props) => {

    const translateLabel = (label: string) => {
        if (label == "APPROVED")
            return "ОДОБРЕНО";
        if (label == "REFUSED")
            return "ОДБИЕНО";
        if (label == "WAITING")
            return "СЕ ЧЕКА";
    }

    const translateStepName = (name: string) => {
        return masterService.translate(name);
    }

    return (
        <Box component="div" sx={{display: 'block', transform: 'scale(0.9)', marginTop: '10px', border: '2px solid grey', borderRadius: '5px'}}>
            <Card variant="outlined" sx={{minHeight: '65vh', minWidth: '70vw'}} className={`${styles.flex} ${styles.flexColumnDirection} ${styles.spaceBetween}`}>
                <CardContent>
                    <Typography gutterBottom variant="h5" component="div" color="text.secondary">
                        {translateStepName(props.historyStep?.name)}
                    </Typography>
                    <hr className={styles.hr}/>
                    {props.historyStep?.topic != null ?
                        <Typography variant="body2" color="text.secondary" sx={{fontSize: 23, display: 'flex', alignItems: 'center'}}>
                            Тема: {props.historyStep?.topic}
                        </Typography> : ""}
                    {props.historyStep?.description != null ?
                        <Typography variant="body2" color="text.secondary" sx={{fontSize: 23, display: 'flex', alignItems: 'center'}}>
                            Опис: {props.historyStep?.description}
                        </Typography> : ""}
                    {(props.historyStep?.application != null || props.historyStep?.biography != null ||
                        props.historyStep?.mentorApproval != null || props.historyStep?.supplement != null || props.historyStep?.document != null) ?
                        <Typography variant="body2" color="text.secondary" sx={{fontSize: 23}}>
                            Прикачени документи:
                        </Typography> : ""}
                    <Typography variant="body2" color="text.secondary" className={ `${styles.wordBreakFileName}` } sx={{fontSize: 18, marginTop: '30px', display: 'flex', wordBreak: 'break-all'}}>
                        {props.historyStep?.application != null ?
                            <span className={`${styles.flex} ${styles.marginRight15} ${styles.alignItemsCenter}`}>
                                        {props.historyStep?.application.name}
                                <FileOpenIcon fontSize='medium' color="primary" sx={{ marginLeft: '3px' }}/>
                                <IconButton sx={{ marginLeft: '5px',marginRight: '15px' }}
                                            href={"http://localhost:8080/api/document?documentId=" +
                                            props.historyStep?.application.id + "&fileLocation=" + props.historyStep?.application.location} >
                                            <DownloadForOfflineRoundedIcon fontSize='large' color="primary"/>
                                </IconButton>
                            </span>
                            : ""}
                        {props.historyStep?.biography != null ?
                            <span className={`${styles.flex} ${styles.marginRight15} ${styles.alignItemsCenter}`}>
                                        {props.historyStep?.biography.name}
                                <FileOpenIcon fontSize='medium' color="primary" sx={{ marginLeft: '3px' }}/>
                                <IconButton sx={{ marginLeft: '5px',marginRight: '15px' }}
                                            href={"http://localhost:8080/api/document?documentId=" +
                                            props.historyStep?.biography.id + "&fileLocation=" + props.historyStep?.biography.location} >
                                            <DownloadForOfflineRoundedIcon fontSize='large' color="primary"/>
                                </IconButton>
                            </span>
                            : ""}
                        {props.historyStep?.mentorApproval != null ?
                            <span className={`${styles.flex} ${styles.marginRight15} ${styles.alignItemsCenter}`}>
                                        {props.historyStep?.mentorApproval.name}
                                <FileOpenIcon fontSize='medium' color="primary" sx={{ marginLeft: '3px' }}/>
                                <IconButton sx={{ marginLeft: '5px',marginRight: '15px' }}
                                            href={"http://localhost:8080/api/document?documentId=" +
                                            props.historyStep?.mentorApproval.id + "&fileLocation=" + props.historyStep?.mentorApproval.location} >
                                            <DownloadForOfflineRoundedIcon fontSize='large' color="primary"/>
                                </IconButton>
                            </span>
                            : ""}
                        {props.historyStep?.supplement != null ?
                            <span className={`${styles.flex} ${styles.marginRight15} ${styles.alignItemsCenter}`}>
                                        {props.historyStep?.supplement.name}
                                <FileOpenIcon fontSize='medium' color="primary" sx={{ marginLeft: '3px' }}/>
                                <IconButton sx={{ marginLeft: '5px',marginRight: '15px' }} href={"http://localhost:8080/api/document?documentId=" +
                                props.historyStep?.supplement.id + "&fileLocation=" + props.historyStep?.supplement.location} >
                                            <DownloadForOfflineRoundedIcon fontSize='large' color="primary"/>
                                </IconButton>
                            </span>
                            : ""}
                        {props.historyStep?.document != null ?
                            <span className={`${styles.flex} ${styles.marginRight15} ${styles.alignItemsCenter} ${styles.wordBreakFileName}`}>
                                        {props.historyStep?.document.name}
                                <FileOpenIcon fontSize='medium' color="primary" sx={{ marginLeft: '3px' }}/>
                                <IconButton sx={{ marginLeft: '5px',marginRight: '15px' }} href={"http://localhost:8080/api/document?documentId=" +
                                props.historyStep?.document.id + "&fileLocation=" + props.historyStep?.document.location} >
                                            <DownloadForOfflineRoundedIcon fontSize='large' color="primary"/>
                                </IconButton>
                            </span>
                            : ""}
                    </Typography>
                    {props.validations.length != 0 ?
                        <Typography variant="body2" color="text.secondary" sx={{fontSize: 23, marginLeft: '60px'}}>
                            Валидирано од:
                        </Typography>
                        : ""}
                    {props.validations.map((value, index) => (
                        <Typography key={value.id} variant="body2" color="text.secondary"
                                    sx={{fontSize: 23, marginLeft: '80px', display: 'flex', alignItems: 'center'}}>
                            {value.person} - {translateLabel(value.status)}
                            {value.status == "APPROVED" ? <CheckCircleIcon fontSize='large' sx={{marginLeft: '15px', color: 'darkgreen'}} />
                            : (value.status == "REFUSED" ? <DangerousIcon fontSize='large' sx={{marginLeft: '15px', color: 'darkred'}}/> :
                                    <PauseCircleFilledIcon fontSize='large' sx={{marginLeft: '15px'}}/>)}
                        </Typography>
                        ))
                    }
                    {props.remarks.length != 0 ?
                        <Typography variant="body2" color="text.secondary" sx={{fontSize: 23, marginLeft: '60px', marginTop: '50px'}}>
                            Забелешки:
                        </Typography>
                        : ""}
                    <RemarkList remarks={props.remarks}/>
                </CardContent>
            </Card>
        </Box>
    )
}

export default HistoryStep;