import React from "react";
import {useParams} from 'react-router-dom';
import {PageLayout} from "../PageLayout";
import ValidationService from "../service/validationService";
import {
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    List,
    ListItem,
    ListItemText,
    Stack,
    Typography
} from "@mui/material";
import AddBoxIcon from '@mui/icons-material/AddBox';
import DownloadForOfflineRoundedIcon from '@mui/icons-material/DownloadForOfflineRounded';
import IconButton from '@mui/material/IconButton';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import RemarkDialog from "../dialog/RemarkDialog";
import RemarkList from "./RemarkList";
import RemarkService from "../service/remarkService";
import {RemarkDTO} from "../domain/remarkResponse";
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import styles from './ValidationPage.style.module.css'

interface Props {
    params: string;
}

interface State {
    processId: string | undefined;
    stepName: string;
    studentName: string;
    downloadFileUrls: string[];
    isDisabled: boolean;
    open: boolean;
    remark: string;
    defaultRemark: string;
    remarks: RemarkDTO[];
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
            remarks: []
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
            <PageLayout>
                <Box component="div" sx={{display: 'block', transform: 'scale(0.9)', marginTop: '10px', border: '2px solid grey', borderRadius: '5px'}}>
                    <Card variant="outlined" sx={{minHeight: '25vw'}} className={`${styles.flex} ${styles.flexColumnDirection} ${styles.spaceBetween}`}>
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
                            <Typography variant="body2" color="text.secondary" sx={{fontSize: 18, marginTop: '30px', display: 'flex'}}>
                                {this.state.downloadFileUrls.map((value, index) => (
                                    <span key={index} className={`${styles.flex} ${styles.marginRight15} ${styles.alignItemsCenter}`}>
                                        {value}
                                        <IconButton sx={{ marginLeft: '5px' }}>
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
                <form method="post" action="http://localhost:8080/login">
                    <p>
                        <label htmlFor="username" >Username</label>
                        <input type="text" id="username" name="username"
                              />
                    </p>
                    <p>
                        <label htmlFor="password" >Password</label>
                        <input type="password" id="password" name="password"
                              />
                    </p>
                    <button type="submit">Sign in</button>
                </form>
            </PageLayout>
        )
    }

    private handleDialogClose = (save: boolean) => {
        console.log(save);
        if (save) {
            RemarkService.addRemark(this.state.processId, this.state.remark)
                .then(() => this.getAllRemarks(this.state.processId));
        }
        // console.log(this.state.remark);
        this.setState({
            open: false,
            remark: ""
        })
    }

    private login() {
        ValidationService.login();
    }

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
            remark: this.state.remarks[index].remark
        })
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
        ValidationService.validate(this.state.processId, validationStatus)
            .then(_ => this.disableButtons());
    }

}

// export default ValidationPage;
export default (props: any) => (
    <ValidationPage
        {...props}
        params={useParams()["id"]}
    />
);