import React from "react";
import {useParams} from 'react-router-dom';
import ValidationService from "../service/validationService";
import {Box, Button, Card, CardActions, CardContent, Typography} from "@mui/material";
import AddBoxIcon from '@mui/icons-material/AddBox';
import DownloadForOfflineRoundedIcon from '@mui/icons-material/DownloadForOfflineRounded';
import IconButton from '@mui/material/IconButton';
import RemarkDialog from "../dialog/RemarkDialog";
import RemarkList from "./RemarkList";
import RemarkService from "../service/remarkService";
import {RemarkDTO} from "../domain/remarkResponse";
import styles from './ValidationPage.style.module.css'
import FileOpenIcon from "@mui/icons-material/FileOpen";
import {DocumentDto} from "../domain/DocumentDto";

interface Props {
    params: string;
    navigation: any
}

interface State {
    processId: string | undefined;
    stepName: string;
    studentName: string;
    downloadFileUrls: DocumentDto[];
    isDisabled: boolean;
    open: boolean;
    remark: string;
    defaultRemark: string;
    remarks: RemarkDTO[];
    editRemarkId: string | undefined;
}

class ValidationPage extends React.Component<Props, State> {

    constructor(props: Props) {
        super(props);
        console.log(props.params);
        this.state = {
            processId: this.props.params,
            stepName: "",
            studentName: "",
            downloadFileUrls: [],
            isDisabled: false,
            open: false,
            remark: "",
            defaultRemark: "",
            remarks: [],
            editRemarkId: undefined
        }
    }


    componentDidMount() {
        console.log("did mount");
        ValidationService.getValidationDetails(this.state.processId)
            .then(details => {
                console.log(details);
                this.setState({
                    stepName: details.stepName,
                    studentName: details.studentName,
                    downloadFileUrls: details.downloadUrl
                })
            })
        this.getAllRemarks(this.state.processId);
    }

    render() {
        return (
            // <PageLayout>
                <Box component="div" sx={{display: 'block', transform: 'scale(0.9)', marginTop: '10px', border: '2px solid grey', borderRadius: '5px'}}>
                    <Card variant="outlined" sx={{minHeight: '65vh', minWidth: '70vw'}} className={`${styles.flex} ${styles.flexColumnDirection} ${styles.spaceBetween}`}>
                        <CardContent>
                            <Typography gutterBottom variant="h5" component="div" color="text.secondary">
                                {this.state.stepName}
                            </Typography>
                            <Typography variant="body2" color="text.secondary" sx={{fontSize: 18, display: 'flex', alignItems: 'center'}}>
                                Судент: {this.state.studentName}
                                <span className={styles.commentMarginLeft}>
                                    Add Comment
                                <IconButton onClick={() => {
                                    this.handleDialogOpen();
                                }}>
                                    <AddBoxIcon fontSize='large' color='secondary'/>
                                </IconButton>
                                </span>
                            </Typography>
                            <hr className={styles.hr}/>
                            <Typography variant="body2" color="text.secondary" sx={{fontSize: 18, marginTop: '30px', display: 'flex', wordBreak: 'break-all'}}>
                                {this.state.downloadFileUrls.map((value, index) => (
                                    <span key={index} className={`${styles.flex} ${styles.marginRight15} ${styles.alignItemsCenter}`}>
                                        {value.name}
                                        <FileOpenIcon fontSize='medium' color="secondary" sx={{ marginLeft: '3px' }}/>
                                        <IconButton sx={{ marginLeft: '5px',marginRight: '15px' }}
                                                    href={"http://localhost:8080/api/document?documentId=" + value.id + "&fileLocation=" + value.location} >
                                            <DownloadForOfflineRoundedIcon fontSize='large'/>
                                        </IconButton>
                                    </span>
                                ))}
                            </Typography>
                            <RemarkList remarks={this.state.remarks} editRemark={this.editRemark} deleteRemark={this.deleteRemark}/>
                            {/*<List sx={{ bgcolor: 'background.paper', display: 'flex', flexDirection: 'column', justifyContent: 'center', alignItems: 'center' }}>
                                {this.state.remarks.map((value, index) => (
                                    <ListItem key={index}  sx={{ border: '1px solid grey', borderRadius: '5px', width: '90%', display: 'block', marginBottom: '20px'}}
                                              secondaryAction={
                                                  value.personName === 'Mentor' &&
                                                  <Stack>
                                                      <IconButton onClick={() => this.editRemark(index)}>
                                                          <EditIcon />
                                                      </IconButton>
                                                      <IconButton onClick={() => this.deleteRemark(value.remarkId)}>
                                                          <DeleteIcon />
                                                      </IconButton>
                                                  </Stack>
                                              }>
                                        <ListItemText
                                            primary= {
                                                <Typography
                                                    component="div"
                                                    variant="h6"
                                                    color="text.secondary">
                                                    <strong>{value.personName}</strong>
                                                </Typography>}
                                            secondary={
                                                <Typography
                                                    sx={{  fontSize: 20 }}
                                                    component="div"
                                                    variant="body2"
                                                    color="text.secondary">
                                                    {value.remark}
                                                </Typography>
                                            }/>
                                    </ListItem>
                                ))}
                            </List>*/}
                        </CardContent>
                        <CardActions sx={{marginBottom: "20px", marginRight: "45px", justifyContent: 'end'}}>
                            <Button disabled={this.state.isDisabled} onClick={() => {
                                this.validateStep("REFUSED");
                            }} variant="contained" color="error" size="large">
                                Refuse
                            </Button>
                            <Button disabled={this.state.isDisabled} onClick={() => {
                                this.validateStep("APPROVED");
                            }} variant="contained" color="success" size="large" style={{marginLeft: "25px"}}>
                                Approve
                            </Button>
                        </CardActions>
                    </Card>
                    <RemarkDialog open={this.state.open} onClose={this.handleDialogClose} defaultRemark={this.state.defaultRemark}
                                  handleChanges={this.handleRemarkChanges}/>
            {/*        <Dialog fullWidth={true} open={this.state.open} onClose={() => {*/}
            {/*        this.handleDialogClose(false);*/}
            {/*    }}>*/}
            {/*        <DialogTitle>Remark</DialogTitle>*/}
            {/*    <DialogContent>*/}
            {/*        <TextField*/}
            {/*            autoFocus*/}
            {/*            margin="dense"*/}
            {/*            id="name"*/}
            {/*            label="Remark"*/}
            {/*            multiline={true}*/}
            {/*            defaultValue={this.state.defaultRemark}*/}
            {/*            onChange={(e) => this.handleRemarkChanges(e)}*/}
            {/*            rows={4}*/}
            {/*            fullWidth*/}
            {/*            variant="standard"*/}
            {/*        />*/}
            {/*    </DialogContent>*/}
            {/*    <DialogActions>*/}
            {/*        <Button onClick={() => {*/}
            {/*            this.handleDialogClose(false);*/}
            {/*        }}>Cancel</Button>*/}
            {/*        <Button onClick={() => {*/}
            {/*            this.handleDialogClose(true);*/}
            {/*        }}>Save</Button>*/}
            {/*    </DialogActions>*/}
            {/*</Dialog>*/}
                </Box>
        )
    }

    private handleDialogClose = (save: boolean) => {
        console.log(save);
        console.log(this.state.editRemarkId);
        if (save) {
            RemarkService.addRemark(this.state.processId, this.state.remark, this.state.editRemarkId)
                .then(() => this.getAllRemarks(this.state.processId));
        }
        // console.log(this.state.remark);
        this.setState({
            open: false,
            remark: "",
            defaultRemark: "",
            editRemarkId: undefined
        })
    }

    private login() {
        ValidationService.login();
    }

    // private downloadFile(fileUrl: string) {
    //     DocumentService.downloadFile(fileUrl)
    //         .then(blob => saveAs(blob, 'foaf'));
    // }

    private getAllRemarks(processId: string | undefined) {
        RemarkService.getRemarks(processId).then(remarks => {
            console.log(remarks);
            this.setState({
                remarks: remarks
            })
        })
    }

    private editRemark = (index: number) => {
        console.log(index);
        this.setState({
            defaultRemark: this.state.remarks[index].remark,
            open: true,
            remark: this.state.remarks[index].remark,
            editRemarkId: this.state.remarks[index].remarkId
        });
    }

    private deleteRemark = (remarkId: string) => {
        RemarkService.deleteRemark(remarkId)
            .then(() => this.getAllRemarks(this.state.processId));
    }

    private handleRemarkChanges = (event: any) => {
        // console.log(event.target.value);
        this.setState({
            remark: event.target.value
        })
    }

    private handleDialogOpen() {
        this.setState({
            open: true
        })
    }

    private disableButtons() {
        this.setState({
            isDisabled: true
        })
    }

    validateStep(validationStatus: string) {
        this.disableButtons();
        ValidationService.validate(this.state.processId, validationStatus)
            .then(_ => {
                window.location.reload();
            });
    }

}

// export default ValidationPage;
export default (props: any) => (
    <ValidationPage
        {...props}
        params={useParams()["id"]}
    />
);